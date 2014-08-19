package io.highway.to.urhell.transformer;

import io.highway.to.urhell.service.impl.SpringServiceMethod;
import javassist.CtClass;
import javassist.CtMethod;

public class SpringMethodTransformer extends AbstractLeechTransformer {

    public SpringMethodTransformer() {
        super("org/springframework/web/servlet/mvc/method/annotation/RequestMappingHandlerMapping");
    }

    @Override
    protected void doTransform(CtClass cc) throws Exception {
        CtMethod m = cc.getMethod("afterPropertiesSet", "()V");
        m.insertAfter("CoreEngine.getInstance().getFramework(\"" + SpringServiceMethod.FRAMEWORK_NAME + "\").receiveData(getHandlerMethods());");
    }
}
