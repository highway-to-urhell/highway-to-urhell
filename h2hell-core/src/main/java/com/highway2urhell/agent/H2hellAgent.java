package com.highway2urhell.agent;

import com.highway2urhell.PluginUtils;
import com.highway2urhell.transformer.AbstractLeechTransformer;

import java.lang.instrument.Instrumentation;

public class H2hellAgent {


    public static void premain(String agentArgs, Instrumentation inst) {
        for (AbstractLeechTransformer transformer : PluginUtils.autodiscoverPlugin(AbstractLeechTransformer.class)) {
            inst.addTransformer(transformer);
        }
        InstrumentationHolder.getInstance().persistInMemory(inst);

    }
}
