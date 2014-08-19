package io.highway.to.urhell.agent;

import io.highway.to.urhell.PluginUtils;
import io.highway.to.urhell.transformer.AbstractLeechTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;

public class H2hellAgent {
    private static final Logger LOGGER = LoggerFactory.getLogger(H2hellAgent.class);

    public static void premain(String agentArgs, Instrumentation inst) {
        for (AbstractLeechTransformer transformer : PluginUtils.autodiscoverPlugin(AbstractLeechTransformer.class)) {
            inst.addTransformer(transformer);
        }
    }
}
