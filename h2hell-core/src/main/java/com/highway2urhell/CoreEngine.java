package com.highway2urhell;

import com.highway2urhell.agent.InstrumentationHolder;
import com.highway2urhell.domain.EntryPathData;
import com.highway2urhell.domain.FilterEntryPath;
import com.highway2urhell.domain.H2hConfig;
import com.highway2urhell.domain.OutputSystem;
import com.highway2urhell.service.*;
import com.highway2urhell.transformer.EntryPointTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.*;

public class CoreEngine {

    private static CoreEngine instance;

    private Map<String, LeechService> leechPluginRegistry = new HashMap<String, LeechService>();
    private Set<ReporterService> reporterPluginRegistry = new HashSet<ReporterService>();
    protected final transient Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final static String H2H_CONFIG = "H2H_CONFIG";
    private final static String JAVA = "Java";
    private final static Integer DEFAULT_TIMER = 1;
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
                }
            }
        }
        return instance;
    }

    public void enableEntryPointCoverage(FilterEntryPath filter) throws ClassNotFoundException, UnmodifiableClassException {
        LOGGER.info("enabling entry point coverage");
        Instrumentation instrumentation = InstrumentationHolder.getInstance().getInst();
        if (instrumentation != null) {
            TransformerService ts = new TransformerService();
            Map<String, List<EntryPathData>> mapConvert = ts.transformDataFromLeechPluginForTransformation(leechPluginRegistry.values(),filter);
            if(!config.getPathSend()){
                //if the initPath are not called
                initPathsRemote();
            }
            instrumentation.addTransformer(new EntryPointTransformer(mapConvert), true);
            ts.transformAllClassScanByH2h(instrumentation, mapConvert.keySet());
        } else {
            LOGGER.error("Instrumentation fail because internal inst is null");
        }
    }

    public void initPathsRemote(){
        if (config.getOutputSystem().equals(OutputSystem.REMOTE) && config.getToken()!=null) {
            ThunderExporterService.getInstance().initPathsRemoteApp();
            config.setPathSend(true);
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
            throw new RuntimeException("Unknow Variable H2H_CONFIG. Please Set H2H_CONFIG to location application deployment.");
        }
        if ("".equals(rootH2h)) {
            throw new RuntimeException("Variable Path H2H_CONFIG. Please Set H2H_CONFIG to location application deployment.");
        }
        parseConfig(rootH2h);
        if (config.getOutputSystem() == OutputSystem.REMOTE && config.getToken() ==null ) {
            ThunderExporterService.getInstance().registerAppInThunder();
        }else{
            //TODO valid the token with the server H2H-web
            LOGGER.info("application reuse the token {} for application {}",
                    config.getToken(), config.getNameApplication());
        }
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
            String outputSystem = prop.getProperty("outputSystem");
            if(outputSystem!=null){
                config.setOutputSystem(OutputSystem.valueOf(outputSystem));
            }else{
                throw new RuntimeException("Variable outpuSystem is not defined");
            }
            String timer = prop.getProperty("timer");
            if(timer!=null){
                config.setTimer(OutputSystem.valueOf(timer));
            }else{
                throw new RuntimeException("Variable timer is not defined");
            }
            config.setUrlH2hWeb(prop.getProperty("urlh2hweb"));
            String higherTimer = prop.getProperty("higherTimer");
            if(higherTimer!=null){
                try {
                    config.setHigherTime(Integer.valueOf(higherTimer));
                }catch (NumberFormatException e){
                    //default value
                    config.setHigherTime(DEFAULT_TIMER);
                }
            }else{
                //default value
                config.setHigherTime(DEFAULT_TIMER);
            }

        } catch (IOException ex) {
            throw new RuntimeException("Error while reading H2hConfigFile " + pathFile, ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    //Don't care
                }
            }
        }
    }

    public H2hConfig getConfig() {
        return config;
    }


}

