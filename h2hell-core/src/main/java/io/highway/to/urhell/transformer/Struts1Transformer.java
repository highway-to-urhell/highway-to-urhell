package io.highway.to.urhell.transformer;

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Struts1Transformer implements LeechTransformer {
	private static final Logger LOG = LoggerFactory
			.getLogger(Struts1Transformer.class);
	private static final String CLASSNAME = "org/apache/struts/action/ActionServlet";

	public byte[] transform(ClassLoader loader, String className,
			Class classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {

		if (className.equals("org/apache/struts/action/ActionServlet")) {
			LOG.info(" Transformer "
					+ Struts1Transformer.class.getCanonicalName()
					+ " transform " + CLASSNAME);
			try {
				ClassPool cp = ClassPool.getDefault();
				cp.appendClassPath(new LoaderClassPath(loader));
				CtClass cc = cp.get("org.apache.struts.action.ActionServlet");
				cp.importPackage("io.highway.to.urhell");
				cp.importPackage("io.highway.to.urhell.domain");
				CtMethod m = cc.getDeclaredMethod("destroyConfigDigester");
				m.insertBefore("CoreEngine.getInstance().getFramework(FrameworkEnum.STRUTS_1_X).receiveData(configDigester);");

				classfileBuffer = cc.toBytecode();
				cc.detach();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return classfileBuffer;
	}

}
