package com.highway2urhell.transformer;

import javassist.CtClass;
import javassist.CtMethod;

public class Struts1WithSpringTransformer extends AbstractLeechTransformer {

    public Struts1WithSpringTransformer() {
        super("org/springframework/web/struts/ContextLoaderPlugIn");
        addImportPackage(collectPackages());
    }

    public static String[] collectPackages() {
        return new String[]{
                "com.highway2urhell",
                "com.highway2urhell.domain",
                "java.lang.reflect",
                "java.util",
                "org.apache.struts.action",
                "org.springframework.web.context"
        };
    }

    @Override
    protected void doTransform(CtClass cc) throws Exception {
        CtMethod m = cc.getMethod("init", "(Lorg/apache/struts/action/ActionServlet;Lorg/apache/struts/config/ModuleConfig;)V");
        m.insertAfter(collectBody());

    }

    public static String collectBody() {
        return "{\n" +
                "    List listEntryPath = new ArrayList();\n" +
                "    if (webApplicationContext != null && webApplicationContext.getBeanDefinitionNames() != null && webApplicationContext.getBeanDefinitionNames().length > 0) {\n" +
                "        for (int i = 0; i < webApplicationContext.getBeanDefinitionNames().length; i++) {\n" +
                "            String tmp = webApplicationContext.getBeanDefinitionNames()[i];\n" +
                "            if (tmp.startsWith(\"/\")) {\n" +
                "                Action toAdd = (Action) webApplicationContext.getBean(tmp, Action.class);\n" +
                "                try {\n" +
                "                    Class c = Class.forName(toAdd.getClass().getName());\n" +
                "                    Method[] tabDeclared = c.getDeclaredMethods();\n" +
                "                    for (int m = 0; i < tabDeclared.length; i++) {\n" +
                "                        EntryPathData entry = new EntryPathData();\n" +
                "                        String resSignature = org.objectweb.asm.Type.getMethodDescriptor(tabDeclared[i]);\n" +
                "                        entry.setUri(tmp);\n" +
                "                        entry.setClassName(toAdd.getClass().getName());\n" +
                "                        entry.setMethodName(tabDeclared[m].getName());\n" +
                "                        entry.setSignatureName(resSignature);\n" +
                "                        List listEntryPathData = new ArrayList();\n" +
                "                        for (int j = 0; j < tabDeclared[m].getParameterTypes().length; j++) {\n" +
                "                            EntryPathParam param = new EntryPathParam();\n" +
                "                            param.setKey(\"\");\n" +
                "                            param.setTypeParam(TypeParam.PARAM_DATA);\n" +
                "                            param.setValue(tabDeclared[m].getParameterTypes()[j].getName());\n" +
                "                            listEntryPathData.add(param);\n" +
                "                        }\n" +
                "                        entry.setListEntryPathData(listEntryPathData);\n" +
                "                        System.err.println(entry.toString());\n" +
                "                        listEntryPath.add(entry);\n" +
                "                    }\n" +
                "                } catch (ClassNotFoundException e) {\n" +
                "                    System.err.println(\"Error on invoke \" + toAdd.getClass().getName());\n" +
                "                    e.printStackTrace();\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "    CoreEngine.getInstance().getFramework(\"STRUTS_SPRING_1\").receiveData(listEntryPath);\n" +
                "}";
    }
}
