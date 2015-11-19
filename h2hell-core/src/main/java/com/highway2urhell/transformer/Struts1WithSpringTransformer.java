package com.highway2urhell.transformer;

import javassist.CtClass;
import javassist.CtMethod;

public class Struts1WithSpringTransformer extends AbstractLeechTransformer {

    public Struts1WithSpringTransformer() {
        super("org/springframework/web/struts/ContextLoaderPlugIn");
        addImportPackage(
                "org.apache.struts.action",
                "java.lang.reflect",
                "org.objectweb.asm",
                "org.apache.struts.config.impl",
                "com.google.gson.Gson",
                "java.util");
    }

    @Override
    protected void doTransform(CtClass cc) throws Exception {
        CtMethod m = cc.getMethod("init", "(Lorg/apache/struts/action/ActionServlet;Lorg/apache/struts/config/ModuleConfig;)V");
        String h2hHookCode =
                "List listEntryPath = new ArrayList();" +
                        " if(webApplicationContext!=null && " +
                        "webApplicationContext.getBeanDefinitionNames()!= null && " +
                        "webApplicationContext.getBeanDefinitionNames().length>0){ " +
                        "   for(int i=0;i<webApplicationContext.getBeanDefinitionNames().length;i++){" +
                        "       String tmp = webApplicationContext.getBeanDefinitionNames()[i];" +
                        "       if(tmp.startsWith(\"/\")){" +
                        "           Action toAdd = (Action) webApplicationContext.getBean(tmp, Action.class);" +
                        "           try {" +
                        "               Class c = Class.forName(toAdd.getClass().getName());" +
                        "               Method[] tabDeclared = c.getDeclaredMethods();" +
                        "               for (int i = 0; i < tabDeclared.length; i++) {" +
                        "                       EntryPathData entry = new EntryPathData();" +
                        "                       String resSignature = org.objectweb.asm.Type.getMethodDescriptor(tabDeclared[i]);" +
                        "                       entry.setUri(tmp);" +
                        "                       entry.setClassName(toAdd.getClass().getName());" +
                        "                       entry.setMethodName(tabDeclared[i].getName());" +
                        "                       entry.setSignatureName(resSignature);" +
                        "                       List listEntryPathData = new ArrayList();" +
                        "                       for (int j=0;j<tabDeclared[i].getParameterTypes().length;j++) {" +
                        "                           EntryPathParam param = new EntryPathParam();" +
                        "                           param.setKey(\"\");" +
                        "                           param.setTypeParam(TypeParam.PARAM_DATA);" +
                        "                           param.setValue(tabDeclared[i].getParameterTypes()[j].getName());" +
                        "                           listEntryPathData.add(param);" +
                        "                       }" +
                        "                       entry.setListEntryPathData(listEntryPathData);" +
                        "                       System.err.println(entry.toString());" +
                        "                       listEntryPath.add(entry);" +
                        "               }" +
                        "           } catch (ClassNotFoundException e) {" +
                        "	            System.err.println(\"Error on invoke \"+toAdd.getClass().getName());e.printStackTrace();" +
                        "           }" +
                        "       }" +
                        "   }" +
                        "}" +
                        "CoreEngine.getInstance().getFramework(\"STRUTS_SPRING_1\").receiveData(listEntryPath);";
        m.insertAfter(h2hHookCode);

    }
}
