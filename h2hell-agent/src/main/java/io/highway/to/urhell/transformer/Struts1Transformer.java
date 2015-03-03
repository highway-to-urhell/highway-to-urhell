package io.highway.to.urhell.transformer;

import javassist.CtClass;
import javassist.CtMethod;

public class Struts1Transformer extends AbstractLeechTransformer {

    public Struts1Transformer() {
        super("org/apache/struts/action/ActionServlet",
                "org.apache.struts.action;java.lang.reflect;org.objectweb.asm;org.apache.struts.config.impl;java.util");
    }

    @Override
    protected void doTransform(CtClass cc) throws Exception {
        CtMethod m = cc.getDeclaredMethod("destroyConfigDigester");
        String h2hHookCode = "" +
                "ModuleConfigImpl m = (ModuleConfigImpl) configDigester.getRoot();" +
                "Field f;" +
                "List listEntryPath = new ArrayList();" +
                "try {" +
                "   f = m.getClass().getDeclaredField(\"actionConfigList\");" +
                "   f.setAccessible(true);" +
                "   List res = (ArrayList) f.get(m);" +
                "   if (res != null) {" +
                "       Iterator iter = res.iterator();" +
                "       while(iter.hasNext()){" +
                "           ActionMapping action = (ActionMapping) iter.next();" +
                "           if(action.getType() != null && !\"\".equals(action.getType())){" +
                "               try {" +
                "                  Class c = Class.forName(action.getType());" +
                "                   Method[] tabMet = c.getDeclaredMethods(); " +
                "                  for (int i=0;i<tabMet.length;i++) {" +
                "                      EntryPathData entry = new EntryPathData();" +
                "                      entry.setClassName(action.getType());" +
                "                      entry.setMethodEntry(tabMet[i].getName());" +
                "                      if (action.getPrefix() != null && !\"null\".equals(action.getPrefix())) {" +
                "                          entry.setUri(action.getPrefix() + action.getPath()); }" +
                "                      else {" +
                "                          entry.setUri(action.getPath());" +
                "                      }" +
                "                      entry.setTypePath(TypePath.DYNAMIC);" +
                "                      List listEntryPathData = new ArrayList();" +
                "                      for (int j=0;j<tabMet[i].getParameterTypes().length;j++) {" +
                "                          EntryPathParam param = new EntryPathParam();" +
                "                          param.setKey(\"\");" +
                "                          param.setTypeParam(TypeParam.PARAM_DATA);" +
                "                          param.setValue(tabMet[i].getParameterTypes()[j].getName());" +
                "                          listEntryPathData.add(param);" +
                "                      }" +
                "                      entry.setListEntryPathData(listEntryPathData);" +
                "                      entry.setSignatureName(org.objectweb.asm.Type.getMethodDescriptor(tabMet[i]));" +
                "                      listEntryPath.add(entry);" +
                "                  }" +
                "              } catch (ClassNotFoundException e) {" +
                "                  e.printStackTrace();" +
                "              }" +
                "          }" +
                "      }" +
                "   }" +
                "} catch (Exception e) {" +
                "   e.printStackTrace();" +
                "}" +
                "CoreEngine.getInstance().getFramework(\"STRUTS_1\").receiveData(listEntryPath);";
        m.insertBefore(h2hHookCode);

    }
}
