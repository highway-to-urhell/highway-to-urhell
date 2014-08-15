package io.highway.to.urhell.transformer;

import javassist.CtClass;
import javassist.CtMethod;

public class Struts2Transformer extends AbstractLeechTransformer {

    public Struts2Transformer() {
        super("org/apache/struts2/dispatcher/Dispatcher");
    }

    @Override
    protected void doTransform(CtClass cc) throws Exception {
        CtMethod m = cc.getDeclaredMethod("init");
        m.insertAfter("CoreEngine.getInstance().getFramework(FrameworkEnum.STRUTS_2_X).receiveData(configurationManager);");
    }

}
