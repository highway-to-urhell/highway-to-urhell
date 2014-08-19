package io.highway.to.urhell;

import io.highway.to.urhell.service.AbstractLeechService;
import io.highway.to.urhell.service.LeechService;
import io.highway.to.urhell.service.ReporterService;
import io.highway.to.urhell.service.impl.FileSystemService;
import io.highway.to.urhell.service.impl.GwtService;
import io.highway.to.urhell.service.impl.JaxRsService;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CoreEngine {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CoreEngine.class);

    private static CoreEngine instance;

    private Map<String, LeechService> leechPluginRegistry = new HashMap<String, LeechService>();
    private Set<ReporterService> reporterPluginRegistry = new HashSet<ReporterService>();

    private CoreEngine() {
        // nothing
    }

    public static CoreEngine getInstance() {
        if (instance == null) {
            synchronized (CoreEngine.class) {
                if (instance == null) {
                    instance = new CoreEngine();
                    instance.registerPlugins();
                    instance.collectSystemData();
                    instance.collectGwtData();
                    instance.collectJAXRSData();
                }
            }
        }
        return instance;
    }

    private void collectJAXRSData() {
        getFramework(JaxRsService.FRAMEWORK_NAME).receiveData(null);
    }

    private void collectGwtData() {
        getFramework(GwtService.FRAMEWORK_NAME).receiveData(null);
    }

    private void collectSystemData() {
        getFramework(FileSystemService.FRAMEWORK_NAME).receiveData(null);
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

    private void registerPlugins() {
        autoDiscoverLeechPlugins();
        autoDiscoverReporterPlugins();
    }

    public LeechService getFramework(String frameworkName) {
        return leechPluginRegistry.get(frameworkName);
    }

    private void autoDiscoverLeechPlugins() {
        Reflections reflections = new Reflections("io.highway.to.urhell");
        Set<Class<? extends AbstractLeechService>> pluginsAvailable = reflections
                .getSubTypesOf(AbstractLeechService.class);
        for (Class<? extends LeechService> plugin : pluginsAvailable) {
            try {
                LOGGER.info("registering leech plugin {}",
                        plugin.getCanonicalName());
                LeechService leechService = plugin.newInstance();
                leechPluginRegistry.put(leechService.getFrameworkInformations()
                        .getFrameworkName(), leechService);
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.error("An error occured while registering leech plugin "
                        + plugin.getCanonicalName(), e);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void autoDiscoverReporterPlugins() {
        Reflections reflections = new Reflections("io.highway.to.urhell");
        Set<Class<? extends ReporterService>> pluginsAvailable = reflections
                .getSubTypesOf(ReporterService.class);
        for (Class<? extends ReporterService> plugin : pluginsAvailable) {
            try {
                LOGGER.info("registering reporter plugin {}",
                        plugin.getCanonicalName());
                reporterPluginRegistry.add(plugin.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.error(
                        "An error occured while registering reporter plugin "
                                + plugin.getCanonicalName(), e);
            }
        }
    }

}
