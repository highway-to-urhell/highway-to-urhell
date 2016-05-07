package com.highway2urhell;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.highway2urhell.agent.InstrumentationHolder;
import com.highway2urhell.domain.EntryPathData;
import com.highway2urhell.domain.FilterEntryPath;
import com.highway2urhell.domain.H2hConfig;
import com.highway2urhell.domain.OutputSystem;
import com.highway2urhell.service.AbstractLeechService;
import com.highway2urhell.service.LeechService;
import com.highway2urhell.service.ListenerService;
import com.highway2urhell.service.ReporterService;
import com.highway2urhell.service.ThunderExporterService;
import com.highway2urhell.service.TransformerService;
import com.highway2urhell.transformer.EntryPointTransformer;
import com.highway2urhell.transformer.LineNumberEntryPointTransformer;

public class CoreEngine {

	private static CoreEngine instance;

	private Map<String, LeechService> leechPluginRegistry = new HashMap<String, LeechService>();
	private Set<ReporterService> reporterPluginRegistry = new HashSet<ReporterService>();
	protected final transient Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private final static String H2H_CONFIG = "H2H_CONFIG";
	private final static String JAVA = "Java";
	private final static Integer DEFAULT_TIMER = 2000;
	private final static String NO_URL = "NO_URL";
	private final static String NO_SOURCE = "NO_SOURCE";

	private H2hConfig config;

	private CoreEngine() {
		// nothing
	}

	public static CoreEngine getInstance() {
		if (instance == null) {
			synchronized (CoreEngine.class) {
				if (instance == null) {
					instance = new CoreEngine();
					instance.registerPlugins();
					instance.runPluginsTriggeredAtStartup();
					instance.configure();
					instance.runListener();
				}
			}
		}
		return instance;
	}

	public void runListener() {
		ScheduledExecutorService schExService = Executors.newScheduledThreadPool(1);
		schExService.scheduleAtFixedRate(new Runnable() {

			public void run() {
				LOGGER.debug("Call the H2H-Web server");
				ListenerService.getInstance().callServerH2H();
			}
		}, 5, 10, TimeUnit.SECONDS);
	}

	public void enableEntryPointCoverage(FilterEntryPath filter)
			throws ClassNotFoundException, UnmodifiableClassException {
		LOGGER.info("enabling entry point coverage");
		Instrumentation instrumentation = InstrumentationHolder.getInstance().getInst();
		if (instrumentation != null) {
			TransformerService ts = new TransformerService();
			Map<String, List<EntryPathData>> mapConvert = ts
					.transformDataFromLeechPluginForTransformation(leechPluginRegistry.values(), filter);
			if (!config.getPathSend()) {
				// if the initPath are not called
				initPathsRemote();
			}
			instrumentation.addTransformer(
					new EntryPointTransformer(mapConvert, CoreEngine.getInstance().getConfig().getPerformance()), true);
			ts.transformAllClassScanByH2h(instrumentation, mapConvert.keySet());
		} else {
			LOGGER.error("Instrumentation fail because internal inst is null");
		}
	}

	public void getLineNumberFromEntryPoint(FilterEntryPath filter)
			throws ClassNotFoundException, UnmodifiableClassException {
		LOGGER.info("get Line Number entry point coverage");
		Instrumentation instrumentation = InstrumentationHolder.getInstance().getInst();
		if (instrumentation != null) {
			TransformerService ts = new TransformerService();
			Map<String, List<EntryPathData>> mapConvert = ts
					.transformDataFromLeechPluginForTransformation(leechPluginRegistry.values(), filter);
			instrumentation.addTransformer(new LineNumberEntryPointTransformer(mapConvert));
			ts.transformAllClassScanByH2h(instrumentation, mapConvert.keySet());
		} else {
			LOGGER.error("Instrumentation fail because internal inst is null");
		}
	}

	public void initPathsRemote() {
		if (config.getToken() != null) {
			try {
				FilterEntryPath filter = new FilterEntryPath();
				getLineNumberFromEntryPoint(filter);
				ThunderExporterService.getInstance().initPathsRemoteApp();
				config.setPathSend(true);
			} catch (ClassNotFoundException e) {
				LOGGER.error("Error while launchTransformer ", e);
			} catch (UnmodifiableClassException e) {
				LOGGER.error("Error while launchTransformer ", e);
			}
		}
	}

	public void updateLineNumberEntryPoint(EntryPathData entry) {
		LOGGER.debug("Try to update entry {}",entry);
		for (LeechService leech : leechPluginRegistry.values()) {
			for (EntryPathData entryPath : leech.getFrameworkInformations().getListEntryPath()) {
				if(entryPath.getClassName().equals(entry.getClassName()) && entryPath.getMethodName().equals(entry.getMethodName()) && entryPath.getSignatureName().equals(entry.getSignatureName())){
					entryPath.setLineNumber(entry.getLineNumber());
					LOGGER.debug("Update complete entry {}",entry);
				}
			}
		}
	}

	public void leech() {
		for (ReporterService reporterService : reporterPluginRegistry) {
			for (LeechService leechService : leechPluginRegistry.values()) {
				reporterService.report(leechService.getFrameworkInformations());
			}
		}
	}

	public Collection<LeechService> getLeechServiceRegistered() {
		return leechPluginRegistry.values();
	}

	public LeechService getFramework(String frameworkName) {
		return leechPluginRegistry.get(frameworkName);
	}

	private void registerPlugins() {
		autoDiscoverLeechPlugins();
		autoDiscoverReporterPlugins();
	}

	private void autoDiscoverLeechPlugins() {
		Set<AbstractLeechService> leechServices = PluginUtils.autodiscoverPlugin(AbstractLeechService.class);
		for (AbstractLeechService leechService : leechServices) {
			leechPluginRegistry.put(leechService.getFrameworkInformations().getFrameworkName(), leechService);
		}
	}

	private void autoDiscoverReporterPlugins() {
		reporterPluginRegistry = PluginUtils.autodiscoverPlugin(ReporterService.class);
	}

	private void runPluginsTriggeredAtStartup() {
		for (LeechService leechService : leechPluginRegistry.values()) {
			if (leechService.isTriggeredAtStartup()) {
				leechService.receiveData(null);
			}
		}
	}

	private void configure() {
		// Grab Env
		String rootH2h = System.getProperty(H2H_CONFIG);
		if (rootH2h == null) {
			defaultConfig();
		} else if ("".equals(rootH2h)) {
			defaultConfig();
		} else {
			parseConfig(rootH2h);
		}
	}

	public void defaultConfig() {
		config = new H2hConfig();
		config.setTypeAppz(JAVA);
		config.setUrlApplication(NO_URL);
		config.setNameApplication(String.valueOf(System.currentTimeMillis()));
		config.setPathSource(NO_SOURCE);
		config.setDescription("Default Name.");
		config.setPerformance(false);
		config.setTimer(OutputSystem.REMOTE);
		config.setHigherTime(DEFAULT_TIMER);
		// TODO
		// Hardcoded la valeur de la plateforme --> le jour ou la plateforme
		// fonctionnera ... one day ... one day ..
		// Actuellement on hardcode le local
		config.setUrlH2hWeb("http://localhost:8090/core/api/ThunderEntry");
		// TODO
		// Recuperer le token comme parametre le jour ou la plate fonctionnera
		// ... one day ... one day ..
		config.setToken(null);
	}

	public void parseConfig(String pathFile) {
		config = new H2hConfig();
		config.setTypeAppz(JAVA);
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(pathFile);
			prop.load(input);
			config.setUrlApplication(prop.getProperty("urlapplication"));
			config.setNameApplication(prop.getProperty("nameapplication"));
			config.setPathH2h(prop.getProperty("pathH2h"));
			config.setPathSource(prop.getProperty("pathSource"));
			config.setDescription(prop.getProperty("description"));
			config.setVersionApp(prop.getProperty("versionApp"));
			config.setToken(prop.getProperty("token"));
			config.setReference(prop.getProperty("reference"));
			String performance = prop.getProperty("performance");
			if (performance != null && performance.equals("true")) {
				config.setPerformance(true);
			} else {
				config.setPerformance(false);
			}
			String timer = prop.getProperty("timer");
			if (timer != null) {
				config.setTimer(OutputSystem.valueOf(timer));
			} else {
				throw new RuntimeException("Variable timer is not defined");
			}
			config.setUrlH2hWeb(prop.getProperty("urlh2hweb"));
			String higherTimer = prop.getProperty("higherTimer");
			if (higherTimer != null) {
				try {
					config.setHigherTime(Integer.valueOf(higherTimer));
				} catch (NumberFormatException e) {
					// default value
					config.setHigherTime(DEFAULT_TIMER);
				}
			} else {
				// default value
				config.setHigherTime(DEFAULT_TIMER);
			}

		} catch (IOException ex) {
			throw new RuntimeException("Error while reading H2hConfigFile " + pathFile, ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					// Don't care
				}
			}
		}
	}

	public H2hConfig getConfig() {
		return config;
	}

}
