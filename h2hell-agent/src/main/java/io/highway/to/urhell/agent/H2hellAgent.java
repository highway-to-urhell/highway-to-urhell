package io.highway.to.urhell.agent;

import io.highway.to.urhell.transformer.Spring3Transformer;
import io.highway.to.urhell.transformer.Struts1Transformer;
import io.highway.to.urhell.transformer.Struts2Transformer;

import java.lang.instrument.Instrumentation;

public class H2hellAgent {
	
	public static void premain(String agentArgs, Instrumentation inst) {
//		 //launch the scanner fileSystem
//        FileSystemService.getInstance().receiveData(null);
        // registers the transformer Struts 2
        inst.addTransformer(new Struts2Transformer());
        inst.addTransformer(new Struts1Transformer());
        inst.addTransformer(new Spring3Transformer());
       
    }

}
