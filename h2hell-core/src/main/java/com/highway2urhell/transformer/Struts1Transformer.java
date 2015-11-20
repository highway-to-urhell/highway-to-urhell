package com.highway2urhell.transformer;

import javassist.CtClass;
import javassist.CtMethod;

public class Struts1Transformer extends AbstractLeechTransformer {

    public Struts1Transformer() {
        super("org/apache/struts/action/ActionServlet");
        addImportPackage(collectPackages());
    }

    public static String[] collectPackages() {
        return new String[]{
                "com.highway2urhell",
                "com.highway2urhell.domain",
                "java.lang.reflect",
                "java.util",
                "org.apache.commons.digester",
                "org.apache.struts.action",
                "org.apache.struts.config.impl"
        };
    }

    @Override
    protected void doTransform(CtClass cc) throws Exception {
        CtMethod m = cc.getMethod("destroyConfigDigester", "()V");
        m.insertBefore(collectBody());
    }

    public static String collectBody() {
        return "{\n" +
                "    ModuleConfigImpl m = (ModuleConfigImpl) configDigester.getRoot();\n" +
                "    Field f;\n" +
                "    List listEntryPath = new ArrayList();\n" +
                "    try {\n" +
                "        f = m.getClass().getDeclaredField(\"actionConfigList\");\n" +
                "        f.setAccessible(true);\n" +
                "        List res = (ArrayList) f.get(m);\n" +
                "        if (res != null) {\n" +
                "            Iterator iter = res.iterator();\n" +
                "            while (iter.hasNext()) {\n" +
                "                ActionMapping action = (ActionMapping) iter.next();\n" +
                "                if (action.getType() != null && !\"\".equals(action.getType())) {\n" +
                "                    try {\n" +
                "                        Class c = Class.forName(action.getType());\n" +
                "                        Method[] tabMet = c.getDeclaredMethods();\n" +
                "                        for (int i = 0; i < tabMet.length; i++) {\n" +
                "                            EntryPathData entry = new EntryPathData();\n" +
                "                            entry.setClassName(action.getType());\n" +
                "                            entry.setMethodName(tabMet[i].getName());\n" +
                "                            if (action.getPrefix() != null && !\"null\".equals(action.getPrefix())) {\n" +
                "                                entry.setUri(action.getPrefix() + action.getPath());\n" +
                "                            } else {\n" +
                "                                entry.setUri(action.getPath());\n" +
                "                            }\n" +
                "                            entry.setTypePath(TypePath.DYNAMIC);\n" +
                "                            List listEntryPathData = new ArrayList();\n" +
                "                            for (int j = 0; j < tabMet[i].getParameterTypes().length; j++) {\n" +
                "                                EntryPathParam param = new EntryPathParam();\n" +
                "                                param.setKey(\"\");\n" +
                "                                param.setTypeParam(TypeParam.PARAM_DATA);\n" +
                "                                param.setValue(tabMet[i].getParameterTypes()[j].getName());\n" +
                "                                listEntryPathData.add(param);\n" +
                "                            }\n" +
                "                            entry.setListEntryPathData(listEntryPathData);\n" +
                "                            entry.setSignatureName(org.objectweb.asm.Type.getMethodDescriptor(tabMet[i]));\n" +
                "                            listEntryPath.add(entry);\n" +
                "                        }\n" +
                "                    } catch (ClassNotFoundException e) {\n" +
                "                        e.printStackTrace();\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    } catch (Exception e) {\n" +
                "        e.printStackTrace();\n" +
                "    }\n" +
                "    CoreEngine.getInstance().getFramework(\"STRUTS_1\").receiveData(listEntryPath);\n" +
                "}";
    }
}
