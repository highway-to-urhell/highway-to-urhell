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

public class JSF2Transformer implements LeechTransformer {
	private static final Logger LOG = LoggerFactory
			.getLogger(JSF2Transformer.class);
	private static final String CLASSNAMEMETHOD = "com/sun/faces/mgbean/BeanManager";
	private static final String CLASSMETHODNORMALIZED="com.sun.faces.mgbean.BeanManager";

	public byte[] transform(ClassLoader loader, String className,
			Class classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {

		
	if (className.startsWith(CLASSNAMEMETHOD)) {
			LOG.info(" Going to Transform "
					+ JSF2Transformer.class.getCanonicalName()
					+ " transform " + CLASSNAMEMETHOD);
			try {
				ClassPool cp = ClassPool.getDefault();
				cp.appendClassPath(new LoaderClassPath(loader));
				cp.importPackage("io.highway.to.urhell");
				cp.importPackage("io.highway.to.urhell.domain");
				CtClass cc = cp
						.get(CLASSMETHODNORMALIZED);
				
				CtMethod m = cc.getMethod("register","(Lcom/sun/faces/mgbean/ManagedBeanInfo;)V");
				m.insertAfter("CoreEngine.getInstance().getFramework(FrameworkEnum.JSF_2).receiveData(managedBeans);");
				classfileBuffer = cc.toBytecode();
				cc.detach();
				
			} catch (NotFoundException ex) {
				LOG.error("not found "+ex.getMessage(),ex);
			} catch (Exception ex) {
				LOG.error(" Fail to Transform "
						+ JSF2Transformer.class.getCanonicalName()
						+ " transform " + CLASSNAMEMETHOD);
				ex.printStackTrace();
			}
		}

		return classfileBuffer;
	}
}
