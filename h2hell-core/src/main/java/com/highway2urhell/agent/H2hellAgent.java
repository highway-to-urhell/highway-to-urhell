package com.highway2urhell.agent;

import com.highway2urhell.PluginUtils;
import com.highway2urhell.transformer.AbstractLeechTransformer;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class H2hellAgent {


    public static void premain(String agentArgs, Instrumentation inst) {
        // Hack for load Jar
        findJarAndLoadIfNecessary();
        for (AbstractLeechTransformer transformer : PluginUtils.autodiscoverPlugin(AbstractLeechTransformer.class)) {
            inst.addTransformer(transformer);
        }
        InstrumentationHolder.getInstance().persistInMemory(inst);

    }


    private static void findJarAndLoadIfNecessary(){
        List<String> listJar = new ArrayList<String>();

        URLClassLoader currentThreadClassLoader= (URLClassLoader) Thread.currentThread().getContextClassLoader();
        URL urlsThread[] = currentThreadClassLoader.getURLs();
        for (int i = 0; i < urlsThread.length; i++) {
            listJar.add(urlsThread[i].getFile());
        }
        URLClassLoader currentClassLoader= (URLClassLoader) ClassLoader.getSystemClassLoader();
        URL urlsClass[] = currentClassLoader.getURLs();
        for (int i = 0; i < urlsClass.length; i++) {
            listJar.add(urlsClass[i].getFile());
        }

        addH2HJar(listJar);
    }

    private static void addH2HJar(List<String> listJar){
        String rootH2h = System.getProperty("H2H_INSTALL");
        System.out.println("H2H_INSTALL is "+rootH2h);
        if(rootH2h != null){
            if(!rootH2h.endsWith("/")){
                rootH2h = rootH2h+"/";
            }
            if(!checkList(listJar,"reflections")){
                addJar(rootH2h+"reflections-0.9.10");
            }
            if(!checkList(listJar,"guava")){
                addJar(rootH2h+"guava-15.0");
            }
            if(!checkList(listJar,"javassist")){
                addJar(rootH2h+"javassist-3.19.0-GA");
            }
            if(!checkList(listJar,"gson")){
                addJar(rootH2h+"gson-2.2.4");
            }
            if(!checkList(listJar,"asm-all")){
                addJar(rootH2h+"asm-all-5.0.3");
            }
            if(!checkList(listJar,"javax.servlet-api")){
                addJar(rootH2h+"javax.servlet-api-3.1.0");
            }
            if(!checkList(listJar,"javax.ws.rs-api")){
                addJar(rootH2h+"javax.ws.rs-api-2.0");
            }

        }else{
            System.err.println("No Load dynamically jar because H2H_INSTALL is not configure");
        }

    }

    private static Boolean checkList(List<String> listJar,String nameJar){
        System.err.println(nameJar);
        for(String item : listJar){
            if(item.contains(nameJar)){
                return true;
            }
        }
        return false;
    }

    private static void addJar(String pathJar){
        System.out.println("ADD pathjar "+pathJar);
        try {
            addURL(new File(pathJar + ".jar").toURL());

        }catch (Exception e){
            System.err.println("Error during add jar "+pathJar+ " stack "+e);
        }
    }


    public static void addURL(URL url) throws Exception {
        URLClassLoader classLoader
                = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class clazz= URLClassLoader.class;

        // Use reflection
        Method method= clazz.getDeclaredMethod("addURL", new Class[] { URL.class });
        method.setAccessible(true);
        method.invoke(classLoader, new Object[] { url });
    }




}
