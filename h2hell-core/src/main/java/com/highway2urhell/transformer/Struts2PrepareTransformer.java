package com.highway2urhell.transformer;

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
        m.insertBefore(Struts2Transformer.collect());
    }
}