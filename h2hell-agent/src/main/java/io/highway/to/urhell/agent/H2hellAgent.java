package io.highway.to.urhell.agent;

import io.highway.to.urhell.transformer.Struts2Transformer;

import java.lang.instrument.Instrumentation;

public class H2hellAgent {
	
	public static void premain(String agentArgs, Instrumentation inst) {
        // registers the transformer Struts 2
        inst.addTransformer(new Struts2Transformer());
    }

}
