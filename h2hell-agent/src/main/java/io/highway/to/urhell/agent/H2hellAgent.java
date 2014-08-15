package io.highway.to.urhell.agent;

import io.highway.to.urhell.transformer.AbstractLeechTransformer;

import java.lang.instrument.Instrumentation;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class H2hellAgent {
    private static final Logger LOGGER = LoggerFactory.getLogger(H2hellAgent.class);

    public static void premain(String agentArgs, Instrumentation inst) {
        for (AbstractLeechTransformer transformer : autoDiscoverTransformer()) {
            inst.addTransformer(transformer);
        }
    }
    
   
    private static Set<AbstractLeechTransformer> autoDiscoverTransformer() {
        Reflections reflections = new Reflections("io.highway.to.urhell");
        Set<AbstractLeechTransformer> leechPluginRegistry = new HashSet<AbstractLeechTransformer>();

        Set<Class<? extends AbstractLeechTransformer>> pluginsAvailable = reflections.getSubTypesOf(AbstractLeechTransformer.class);
        for (Class<? extends AbstractLeechTransformer> plugin : pluginsAvailable) {
            try {
                LOGGER.info("registering leech transformer {}", plugin.getCanonicalName());
                AbstractLeechTransformer transformer = plugin.newInstance();
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
