package io.highway.to.urhell.transformer;

import javassist.CtClass;
import javassist.CtMethod;

public class JSF2NavigationTransformer extends AbstractLeechTransformer {

	public JSF2NavigationTransformer() {
		super("com/sun/faces/config/processor/NavigationConfigProcessor");
		addImportPackage("com.sun.faces.application");
	}

	@Override
	protected void doTransform(CtClass cc) throws Exception {
		CtMethod m = cc
				.getMethod("process",
						"(Ljavax/servlet/ServletContext;[Lcom/sun/faces/config/DocumentInfo;)V");
		m.insertAfter("CoreEngine.getInstance().getFramework(FrameworkEnum.JSF_2_NAVIGATION).receiveData(ApplicationAssociate.getInstance(sc).getNavigationCaseListMappings());");
	}
}
