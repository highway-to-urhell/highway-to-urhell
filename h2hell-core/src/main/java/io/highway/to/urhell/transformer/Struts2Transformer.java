package io.highway.to.urhell.transformer;

import io.highway.to.urhell.service.impl.Struts2Service;
import javassist.CtClass;
import javassist.CtMethod;

public class Struts2Transformer extends AbstractLeechTransformer {

    public Struts2Transformer() {
        super("org/apache/struts2/dispatcher/Dispatcher");
    }

    @Override
    protected void doTransform(CtClass cc) throws Exception {
        CtMethod m = cc.getDeclaredMethod("init");
        m.insertAfter(buildReceiveDataStatement(Struts2Service.FRAMEWORK_NAME, "configurationManager"));
    }

}
