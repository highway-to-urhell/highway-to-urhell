package com.highway2urhell.transformer;

import javassist.CtClass;
import javassist.CtMethod;

public class Struts2Transformer extends AbstractLeechTransformer {

    public Struts2Transformer() {
        super("org/apache/struts2/dispatcher/ng/filter/StrutsPrepareAndExecuteFilter");
        addImportPackage(
                "com.opensymphony.xwork2.config",
                "com.opensymphony.xwork2.config.entities",
                "java.util",
                "java.lang.reflect",
                "org.objectweb.asm");
    }

    @Override
    protected void doTransform(CtClass cc) throws Exception {
        CtMethod m = cc
                .getMethod("postInit",
                        "(Lorg/apache/struts2/dispatcher/Dispatcher;Ljavax/servlet/FilterConfig;)V");
        m.insertBefore(collectBody());
    }

    public static String collectBody() {
        return "{\n" +
                "    List listEntryPath = new ArrayList();\n" +
                "    ConfigurationManager cm = dispatcher.getConfigurationManager();\n" +
                "    Configuration cf = cm.getConfiguration();\n" +
                "    Collection colPackages = cf.getPackageConfigs().values();\n" +
                "    if (colPackages != null) {\n" +
                "        Iterator ite = colPackages.iterator();\n" +
                "        while (ite.hasNext()) {\n" +
                "            PackageConfig pack = (PackageConfig) ite.next();\n" +
                "            Collection colActionConfigs = pack.getActionConfigs().values();\n" +
                "            Iterator iteCol = colActionConfigs.iterator();\n" +
                "            while (iteCol.hasNext()) {\n" +
                "                ActionConfig action = (ActionConfig) iteCol.next();\n" +
                "                if (action.getClassName() != null && !\"\".equals(action.getClassName())) {\n" +
                "                    try {\n" +
                "                        Class c = Class.forName(action.getClassName());\n" +
                "                        Method[] tabM = c.getDeclaredMethods();\n" +
                "                        for (int i = 0; i < tabM.length; i++) {\n" +
                "                            String scope = Modifier.toString(tabM[i].getModifiers());\n" +
                "                            if (scope.startsWith(\"public\") && !\"wait\".equals(tabM[i].getName()) && !\"notifyall\".equals(tabM[i].getName().toLowerCase()) && !\"notify\".equals(tabM[i].getName().toLowerCase()) && !\"getclass\".equals(tabM[i].getName().toLowerCase()) && !\"equals\".equals(tabM[i].getName().toLowerCase()) && !\"tostring\".equals(tabM[i].getName().toLowerCase()) && !\"wait\".equals(tabM[i].getName().toLowerCase()) && !\"hashcode\".equals(tabM[i].getName().toLowerCase())) {\n" +
                "                                Method m = tabM[i];\n" +
                "                                EntryPathData entry = new EntryPathData();\n" +
                "                                entry.setTypePath(TypePath.DYNAMIC);\n" +
                "                                entry.setClassName(action.getClassName());\n" +
                "                                entry.setMethodName(m.getName());\n" +
                "                                entry.setUri(action.getName());\n" +
                "                                entry.setSignatureName(org.objectweb.asm.Type.getMethodDescriptor(m));\n" +
                "                                List listEntryPathData = new ArrayList();\n" +
                "                                for (int j = 0; j < m.getParameterTypes().length; j++) {\n" +
                "                                    EntryPathParam param = new EntryPathParam();\n" +
                "                                    param.setKey(\"\");\n" +
                "                                    param.setTypeParam(TypeParam.PARAM_DATA);\n" +
                "                                    param.setValue(m.getParameterTypes()[j].getName());\n" +
                "                                    listEntryPathData.add(param);\n" +
                "                                }\n" +
                "                                entry.setListEntryPathData(listEntryPathData);\n" +
                "                                listEntryPath.add(entry);\n" +
                "                            }\n" +
                "                        }\n" +
                "                    } catch (ClassNotFoundException e) {\n" +
                "                        e.printStackTrace();\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "    CoreEngine.getInstance().getFramework(\"STRUTS_2\").receiveData(listEntryPath);\n" +
                "}";
    }
}
