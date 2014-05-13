package io.highway.to.urhell;

import io.highway.to.urhell.domain.FrameworkEnum;
import io.highway.to.urhell.service.LeechService;
import io.highway.to.urhell.service.ReporterService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoreEngine {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CoreEngine.class);

    private static CoreEngine instance;

    private Map<FrameworkEnum, LeechService> leechPluginRegistry = new HashMap<FrameworkEnum, LeechService>();
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
                }
            }
        }
        return instance;
    }

    public void leech() {
        for (LeechService leechService : leechPluginRegistry.values()) {
            for (ReporterService reporterService : reporterPluginRegistry) {
                reporterService.report(leechService.getFrameworkInformations());
                // FIXME: extract to consoleReporter?
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(leechService.getFrameworkInformations()
                            .toString());
                }
            }
        }
    }

    private void registerPlugins() {
        autoDiscoverLeechPlugins();
        autoDiscoverReporterPlugins();
    }

    public LeechService getFramework(FrameworkEnum framework) {
        return leechPluginRegistry.get(framework);
    }

    private void autoDiscoverLeechPlugins() {
        Reflections reflections = new Reflections();
        Set<Class<? extends LeechService>> pluginsAvailable = reflections
                .getSubTypesOf(LeechService.class);
        for (Class<? extends LeechService> plugin : pluginsAvailable) {
            try {
                LOGGER.info("registering leech plugin {}",
                        plugin.getCanonicalName());
                LeechService leechService = plugin.newInstance();
                leechPluginRegistry.put(leechService.getFrameworkInformations()
                        .getFrameworkEnum(), leechService);
            } catch (InstantiationException e) {
                LOGGER.error("An error occured while registering leech plugin "
                        + plugin.getCanonicalName(), e);
            } catch (IllegalAccessException e) {
                LOGGER.error("An error occured while registering leech plugin "
                        + plugin.getCanonicalName(), e);
            }
        }
    }

    private void autoDiscoverReporterPlugins() {
        Reflections reflections = new Reflections();
        Set<Class<? extends ReporterService>> pluginsAvailable = reflections
                .getSubTypesOf(ReporterService.class);
        for (Class<? extends ReporterService> plugin : pluginsAvailable) {
            try {
                LOGGER.info("registering reporter plugin {}",
                        plugin.getCanonicalName());
                reporterPluginRegistry.add(plugin.newInstance());
            } catch (InstantiationException e) {
                LOGGER.error(
                        "An error occured while registering reporter plugin "
                                + plugin.getCanonicalName(), e);
            } catch (IllegalAccessException e) {
                LOGGER.error(
                        "An error occured while registering reporter plugin "
                                + plugin.getCanonicalName(), e);
            }
        }
    }

}
