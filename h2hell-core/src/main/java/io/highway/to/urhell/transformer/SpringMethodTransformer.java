package io.highway.to.urhell.transformer;

import java.io.InputStream;
import java.lang.instrument.IllegalClassFormatException;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.Iterator;

import javassist.ClassClassPath;
import javassist.ClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringMethodTransformer implements LeechTransformer {
	private static final Logger LOG = LoggerFactory
			.getLogger(SpringMethodTransformer.class);
	private static final String CLASSNAMEMETHOD = "org/springframework/web/servlet/mvc/method/annotation/RequestMappingHandlerMapping";

	public byte[] transform(ClassLoader loader, String className,
			Class classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {

		
		if(className.startsWith("org/springframework/web/servlet/mvc/method/annotation")){
			LOG.error(className);
		}
		if (className.startsWith(CLASSNAMEMETHOD)) {
			LOG.info(" Going to Transform "
					+ SpringMethodTransformer.class.getCanonicalName()
					+ " transform " + CLASSNAMEMETHOD);
			try {
				ClassPool cp = ClassPool.getDefault();
				cp.appendClassPath(new LoaderClassPath(loader));
				cp.importPackage("io.highway.to.urlhell.CoreEngine");
				CtClass cc = cp
						.get("org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping");
				CtMethod m = cc.getMethod("registerHandlerMethod","(Ljava/lang/Object;Ljava/lang/reflect/Method;Ljava/lang/Object;)V");
				m.insertAfter("CoreEngine.getInstance().getFramework(FrameworkEnum.SPRING_METHOD).receiveData(handlerMethods);");
				classfileBuffer = cc.toBytecode();
				cc.detach();
				
			} catch (NotFoundException ex) {
				LOG.error("not found "+ex.getMessage(),ex);
			} catch (Exception ex) {
				LOG.error(" Fail to Transform "
						+ SpringMethodTransformer.class.getCanonicalName()
						+ " transform " + CLASSNAMEMETHOD);
				ex.printStackTrace();
			}
		}

		return classfileBuffer;
	}
}
