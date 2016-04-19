package com.highway2urhell.transformer;

import javassist.CtClass;
import javassist.CtMethod;

/**
 * Created by scun on 19/04/16.
 */
public class SringBootServletRegistrerTransformer extends AbstractLeechTransformer {

    public SringBootServletRegistrerTransformer() {
        super("org/springframework/boot/context/embedded/ServletRegistrationBean");
        addImportPackage(
                "java.util.Map",
                "java.util");
    }

    @Override
    protected void doTransform(CtClass cc) throws Exception {
        CtMethod m =cc.getMethod("configure", "(Ljavax/servlet/ServletRegistration$Dynamic;)V");
        String h2hHookCode = "" +
                "List listEntryPath = new ArrayList();" +
                "Iterator iter = getUrlMappings().iterator();" +
                "while(iter.hasNext()){" +
                "   String value = (String) iter.next();" +
                "   EntryPathData entry = new EntryPathData();" +
                "   entry.setTypePath(TypePath.DYNAMIC);" +
                "   entry.setUri(value);" +
                "   entry.setMethodName(servlet.getServletInfo());" +
                "   entry.setClassName(servlet.getServletInfo());" +
                "   entry.setAudit(Boolean.FALSE);" +
                "   listEntryPath.add(entry);" +
                "}" +
                "CoreEngine.getInstance().getFramework(\"SPRING_BOOT_SERVLET\").receiveData(listEntryPath);";
        m.insertAfter(h2hHookCode);

    }


}
