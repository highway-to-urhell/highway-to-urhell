package io.highway.to.urhell.transformer;

import io.highway.to.urhell.service.impl.Struts1Service;
import javassist.CtClass;
import javassist.CtMethod;

public class Struts1Transformer extends AbstractLeechTransformer {

    public Struts1Transformer() {
        super("org/apache/struts/action/ActionServlet");
    }

    @Override
    protected void doTransform(CtClass cc) throws Exception {
        CtMethod m = cc.getDeclaredMethod("destroyConfigDigester");
        m.insertBefore("CoreEngine.getInstance().getFramework(\"" + Struts1Service.FRAMEWORK_NAME + "\").receiveData(configDigester);");

    }

}
