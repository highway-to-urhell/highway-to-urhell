package io.highway.to.urhell.transformer;

import javassist.CtClass;
import javassist.CtMethod;

public class JSF2Transformer extends AbstractLeechTransformer {

    public JSF2Transformer() {
        super("com/sun/faces/mgbean/BeanManager");
    }

    @Override
    protected void doTransform(CtClass cc) throws Exception {
        CtMethod m = cc.getMethod("register", "(Lcom/sun/faces/mgbean/ManagedBeanInfo;)V");
        m.insertAfter("CoreEngine.getInstance().getFramework(FrameworkEnum.JSF_2).receiveData(managedBeans);");
    }
}
