package io.highway.to.urhell.transformer;

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringUrlTransformer implements LeechTransformer {

	private static final Logger LOG = LoggerFactory
			.getLogger(SpringUrlTransformer.class);
	private static final String CLASSNAME = "org/springframework/web/servlet/mvc/support/ControllerBeanNameHandlerMapping";

	public byte[] transform(ClassLoader loader, String className,
			Class classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {

		if (className.startsWith(CLASSNAME)) {
			LOG.info(" Transformer "
					+ SpringUrlTransformer.class.getCanonicalName()
					+ " transform " + CLASSNAME);
			try {
				ClassPool cp = ClassPool.getDefault();
				cp.appendClassPath(new LoaderClassPath(loader));
				CtClass cc = cp
						.get("org.springframework.web.servlet.mvc.support.ControllerBeanNameHandlerMapping");
				cp.importPackage("io.highway.to.urhell.CoreEngine");
				CtMethod m = cc.getMethod("registerHandlerMethod","(Ljava/lang/String,Ljava.kang.Object;)V");
				m.insertAfter("CoreEngine.getInstance().getFramework(FrameworkEnum.SPRING_URL).receiveData(handlerMap);");

				classfileBuffer = cc.toBytecode();
				cc.detach();
			} catch (NotFoundException ex) {
				LOG.error("not found " + ex.getMessage(), ex);
			} catch (Exception ex) {
				LOG.error(" Fail to Transform "
						+ SpringUrlTransformer.class.getCanonicalName()
						+ " transform " + CLASSNAME);
				ex.printStackTrace();
			}
		}

		return classfileBuffer;
	}
}
