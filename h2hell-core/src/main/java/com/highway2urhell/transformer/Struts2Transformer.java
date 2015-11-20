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
        String h2hHookCode = "Struts2Collector.collect(dispatcher);";
        m.insertBefore(h2hHookCode);
    }
}
