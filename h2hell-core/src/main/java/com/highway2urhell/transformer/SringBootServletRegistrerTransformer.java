package com.highway2urhell.transformer;

import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;

/**
 * Created by scun on 19/04/16.
 */
public class SringBootServletRegistrerTransformer extends AbstractLeechTransformer {

    public SringBootServletRegistrerTransformer() {
        super("org/apache/catalina/core/ApplicationServletRegistration");
        addImportPackage(
                "java.util.Map",
                "java.util","javax.servlet");
    }

    @Override
    protected void doTransform(CtClass cc) throws Exception {
        CtMethod m =cc.getMethod("addMapping","([Ljava/lang/String;)Ljava/util/Set;");
        String h2hHookCode = "" +
                "List listEntryPath = new ArrayList();" +
                "   String[] tab =urlPatterns   ;" +
                "   for(int i=0;i<tab.length;i++){" +
                "            EntryPathData entry = new EntryPathData();" +
                "            entry.setClassName(wrapper.getName());" +
                "            entry.setMethodName(wrapper.getName());" +
                "            entry.setTypePath(TypePath.DYNAMIC);" +
                "            entry.setUri(tab[i]);    "+
                "            entry.setAudit(Boolean.FALSE);    "+
                "            listEntryPath.add(entry);" +
                "  }" +
                "CoreEngine.getInstance().getFramework(\"SPRING_BOOT_SERVLET\").receiveData(listEntryPath);";
        m.insertBefore(h2hHookCode);

    }


}
