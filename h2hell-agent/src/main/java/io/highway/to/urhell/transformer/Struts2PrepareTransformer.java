package io.highway.to.urhell.transformer;

import javassist.CtClass;
import javassist.CtMethod;

public class Struts2PrepareTransformer extends AbstractLeechTransformer {

    public Struts2PrepareTransformer() {
        super("org/apache/struts2/dispatcher/ng/filter/StrutsPrepareFilter");
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
        String h2hHookCode = "" +
                "List listEntryPath = new ArrayList();" +
                "ConfigurationManager cm = dispatcher.getConfigurationManager();" +
                "Configuration cf = cm.getConfiguration();" +
                "Collection colPackages = cf.getPackageConfigs().values();" +
                "if (colPackages != null) {" +
                "   Iterator ite = colPackages.iterator();" +
                "   while(ite.hasNext()) {" +
                "       PackageConfig pack = (PackageConfig) ite.next();" +
                "       Collection colActionConfigs = pack.getActionConfigs().values();" +
                "       Iterator iteCol = colActionConfigs.iterator();" +
                "       while(iteCol.hasNext()){" +
                "          ActionConfig action = (ActionConfig) iteCol.next();" +
                "          if (action.getClassName() != null && !\"\".equals(action.getClassName())) {" +
                "             try {" +
                "                 Class c = Class.forName(action.getClassName());" +
                "                 Method[] tabM = c.getDeclaredMethods();" +
                "                 for (int i=0;i<tabM.length;i++) {" +
                "                     Method m = tabM[i];" +
                "                     EntryPathData entry = new EntryPathData();" +
                "                     entry.setTypePath(TypePath.DYNAMIC);" +
                "                     entry.setClassName(action.getClassName());" +
                "                     entry.setMethodEntry(m.getName());" +
                "                     entry.setUri(action.getName());" +
                "                     entry.setSignatureName(org.objectweb.asm.Type.getMethodDescriptor(m));" +
                "                     List listEntryPathData = new ArrayList();" +
                "                     for (int j=0;j<m.getParameterTypes().length;j++) {" +
                "                         EntryPathParam param = new EntryPathParam();" +
                "                         param.setKey(\"\");" +
                "                         param.setTypeParam(TypeParam.PARAM_DATA);" +
                "                         param.setValue(m.getParameterTypes()[j].getName());" +
                "                         listEntryPathData.add(param);" +
                "                     }" +
                "                     entry.setListEntryPathData(listEntryPathData);" +
                "                     listEntryPath.add(entry);" +
                "                 }" +
                "             } catch (ClassNotFoundException e) {" +
                "                 e.printStackTrace();" +
                "             }" +
                "         }" +
                "      }" +
                "   }" +
                "}" +
                "CoreEngine.getInstance().getFramework(\"STRUTS_2\").receiveData(listEntryPath);";
        m.insertBefore(h2hHookCode);
    }
}