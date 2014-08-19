package io.highway.to.urhell;

import io.highway.to.urhell.service.AbstractLeechService;
import io.highway.to.urhell.service.LeechService;
import io.highway.to.urhell.service.ReporterService;
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
                    instance.runPluginsTriggeredAtStartup();
                }
            }
        }
        return instance;
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

}

