package io.highway.to.urhell.agent;

import io.highway.to.urhell.transformer.AbstractLeechTransformer;

import java.lang.instrument.Instrumentation;

public class H2hellAgent {
    

    public static void premain(String agentArgs, Instrumentation inst) {
    	for (AbstractLeechTransformer transformer : PluginUtils.autodiscoverPlugin(AbstractLeechTransformer.class)) {
            inst.addTransformer(transformer);
        }
    	AgentService.getInstance().persistInMemory(inst);
        
    }
}
