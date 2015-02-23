package io.highway.to.urhell.agent;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;


public class PluginUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(PluginUtils.class);

    private PluginUtils() {
    }

    public static <T> Set<T> autodiscoverPlugin(Class<T> pluginClass) {
        Set<T> pluginList = new HashSet<>();
        Reflections reflections = new Reflections("io.highway.to.urhell");
        Set<Class<? extends T>> pluginsAvailable = reflections
                .getSubTypesOf(pluginClass);
        for (Class<? extends T> plugin : pluginsAvailable) {
            try {
                LOGGER.info("registering {} : {}", pluginClass.getName(),
                        plugin.getCanonicalName());
                T instance = plugin.newInstance();
                pluginList.add(instance);
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.error("An error occurred while registering " + pluginClass.getName() + " : " + plugin.getCanonicalName(), e);
            }
        }
        return pluginList;

    }

}
