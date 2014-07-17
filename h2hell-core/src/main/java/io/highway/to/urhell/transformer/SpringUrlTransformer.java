package io.highway.to.urhell.transformer;

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import javassist.bytecode.Descriptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringUrlTransformer implements LeechTransformer {

	private static final Logger LOG = LoggerFactory
			.getLogger(SpringUrlTransformer.class);
	private static final String CLASSNAME = "org/springframework/web/servlet/handler/AbstractUrlHandlerMapping";

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
						.get("org.springframework.web.servlet.handler.AbstractUrlHandlerMapping");
				cp.importPackage("io.highway.to.urhell");
				cp.importPackage("io.highway.to.urhell.domain");
				CtMethod m = cc.getMethod("registerHandler","(Ljava/lang/String;Ljava/lang/Object;)V");
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
