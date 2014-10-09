package io.highway.to.urhell.transformer;

import io.highway.to.urhell.service.impl.Struts2Service;
import javassist.CtClass;
import javassist.CtMethod;

public class Struts2Transformer extends AbstractLeechTransformer {

	public Struts2Transformer() {
		super(
				"org/apache/struts2/dispatcher/ng/listener/StrutsPrepareAndExecuteFilter");
	}

	@Override
	protected void doTransform(CtClass cc) throws Exception {
		CtMethod m = cc.getMethod("initDispatcher", "(Lorg/apache/struts2/dispatcher/ng/HostConfig;)V");
		m.insertAfter(buildReceiveDataStatement(Struts2Service.FRAMEWORK_NAME,
				"dispatcher.getContainer()"));
	}

}
