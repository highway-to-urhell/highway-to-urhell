package com.highway2urhell;

import org.reflections.Reflections;
import java.util.HashSet;
import java.util.Set;


public class PluginUtils {

    private PluginUtils() {
    }

    public static <T> Set<T> autodiscoverPlugin(Class<T> pluginClass) {
        Set pluginList = new HashSet();
        Reflections reflections = new Reflections("com.highway2urhell");
        Set<Class<? extends T>> pluginsAvailable = reflections.getSubTypesOf(pluginClass);
        for (Class<? extends T> plugin : pluginsAvailable) {
            try {
                System.out.println("registering "+pluginClass.getName()+" : "+ plugin.getCanonicalName());
                T instance = plugin.newInstance();
                pluginList.add(instance);
            } catch (InstantiationException e) {
                System.err.println("An error occurred while registering " + pluginClass.getName() + " : " + plugin.getCanonicalName()+ e);
            } catch (IllegalAccessException e) {
                System.err.println("An error occurred while registering " + pluginClass.getName() + " : " + plugin.getCanonicalName()+ e);
            }
        }
        return pluginList;
    }
}
