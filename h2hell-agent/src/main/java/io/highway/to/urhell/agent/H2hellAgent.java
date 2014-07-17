package io.highway.to.urhell.agent;

import io.highway.to.urhell.transformer.LeechTransformer;

import java.lang.instrument.Instrumentation;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class H2hellAgent {
    private static final Logger LOGGER = LoggerFactory.getLogger(H2hellAgent.class);

    public static void premain(String agentArgs, Instrumentation inst) {
        for (LeechTransformer transformer : autoDiscoverTransformer()) {
            inst.addTransformer(transformer);
        }
    }
    
   
    private static Set<LeechTransformer> autoDiscoverTransformer() {
        Reflections reflections = new Reflections("io.highway.to.urhell");
        Set<LeechTransformer> leechPluginRegistry = new HashSet<LeechTransformer>();

        Set<Class<? extends LeechTransformer>> pluginsAvailable = reflections.getSubTypesOf(LeechTransformer.class);
        for (Class<? extends LeechTransformer> plugin : pluginsAvailable) {
            try {
                LOGGER.info("registering leech transformer {}", plugin.getCanonicalName());
                LeechTransformer transformer = plugin.newInstance();
                leechPluginRegistry.add(transformer);
                LOGGER.info("registering leech transformer complete {}", plugin.getCanonicalName());
            } catch (InstantiationException e) {
                LOGGER.error("An error occured while registering leech transformer " + plugin.getCanonicalName(), e);
            } catch (IllegalAccessException e) {
                LOGGER.error("An error occured while registering leech transformer" + plugin.getCanonicalName(), e);
            }
        }
        return leechPluginRegistry;
    }

}
