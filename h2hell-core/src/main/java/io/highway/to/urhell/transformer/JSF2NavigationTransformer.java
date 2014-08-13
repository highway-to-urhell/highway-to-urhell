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

import com.sun.faces.application.ApplicationAssociate;

public class JSF2NavigationTransformer implements LeechTransformer {
	private static final Logger LOG = LoggerFactory
			.getLogger(JSF2NavigationTransformer.class);
	private static final String CLASSNAMEMETHOD = "com/sun/faces/config/processor/NavigationConfigProcessor";
	private static final String CLASSMETHODNORMALIZED="com.sun.faces.config.processor.NavigationConfigProcessor";

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
				cp.importPackage("com.sun.faces.application");
				CtClass cc = cp
						.get(CLASSMETHODNORMALIZED);
			
				CtMethod[] tab = cc.getMethods();
				for(int i=0;i<tab.length;i++){
					LOG.error(tab[i].getSignature());
				}
				
				CtMethod m = cc.getMethod("process","(Ljavax/servlet/ServletContext;[Lcom/sun/faces/config/DocumentInfo;)V");
				m.insertAfter("CoreEngine.getInstance().getFramework(FrameworkEnum.JSF_2_NAVIGATION).receiveData(ApplicationAssociate.getInstance(sc).getNavigationCaseListMappings());");
				classfileBuffer = cc.toBytecode();
				cc.detach();
				
			} catch (NotFoundException ex) {
				LOG.error("not found "+ex.getMessage(),ex);
			} catch (Exception ex) {
				LOG.error(" Fail to Transform "
						+ JSF2NavigationTransformer.class.getCanonicalName()
						+ " transform " + CLASSNAMEMETHOD);
				ex.printStackTrace();
			}
		}

		return classfileBuffer;
	}
	
	
}
