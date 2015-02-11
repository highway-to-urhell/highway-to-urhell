package io.highway.to.urhell.transformer;

import io.highway.to.urhell.service.impl.Struts1Service;
import javassist.CtClass;
import javassist.CtMethod;

public class Struts1Transformer extends AbstractLeechTransformer {

    public Struts1Transformer() {
        super("org/apache/struts/action/²");
    }

    @Override
    protected void doTransform(CtClass cc) throws Exception {
        CtMethod m = cc.getDeclaredMethod("destroyConfigDigester");
        m.insertBefore(buildReceiveDataStatement(Struts1Service.FRAMEWORK_NAME, "configDigester"));

    }

}
