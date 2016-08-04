package com.highway2urhell.agent;

import com.highway2urhell.PluginUtils;
import com.highway2urhell.transformer.AbstractLeechTransformer;

import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.net.URLClassLoader;

public class H2hellAgent {


    public static void premain(String agentArgs, Instrumentation inst) {
        // find the jar
        findJarFromThread();
        findJarFromClassLoader();

        for (AbstractLeechTransformer transformer : PluginUtils.autodiscoverPlugin(AbstractLeechTransformer.class)) {
            inst.addTransformer(transformer);
        }
        InstrumentationHolder.getInstance().persistInMemory(inst);

    }



    private static void findJarFromThread(){
        URLClassLoader currentThreadClassLoader= (URLClassLoader) Thread.currentThread().getContextClassLoader();
        URL urls[] = currentThreadClassLoader.getURLs();
        for (int i = 0; i < urls.length; i++) {
            System.err.println("THREAD : "+urls[i].toString());
        }
  }

    private static void findJarFromClassLoader(){
        URLClassLoader currentThreadClassLoader= (URLClassLoader) ClassLoader.getSystemClassLoader();
        URL urls[] = currentThreadClassLoader.getURLs();
        for (int i = 0; i < urls.length; i++) {
            System.err.println("CLASSLOADER : "+urls[i].toString());
        }
    }



}
