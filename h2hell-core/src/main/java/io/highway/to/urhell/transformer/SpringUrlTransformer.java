package io.highway.to.urhell.transformer;

import io.highway.to.urhell.service.impl.SpringServiceUrl;
import javassist.CtClass;
import javassist.CtMethod;

public class SpringUrlTransformer extends AbstractLeechTransformer {

    public SpringUrlTransformer() {
        super("org/springframework/web/servlet/handler/AbstractUrlHandlerMapping");
    }

    @Override
    protected void doTransform(CtClass cc) throws Exception {
        CtMethod m = cc.getMethod("registerHandler", "(Ljava/lang/String;Ljava/lang/Object;)V");
        m.insertAfter(buildReceiveDataStatement(SpringServiceUrl.FRAMEWORK_NAME, "handlerMap"));

    }
}
