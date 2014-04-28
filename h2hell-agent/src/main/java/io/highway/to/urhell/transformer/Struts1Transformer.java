package io.highway.to.urhell.transformer;

import io.highway.to.urhell.service.impl.Struts1Service;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class Struts1Transformer implements ClassFileTransformer {

	
	public byte[] transform(ClassLoader loader, String className,
			Class classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {

		byte[] byteCode = classfileBuffer;

		if (className.equals("org/apache/struts/action/ActionServlet")) {

			try {
				ClassPool cp = ClassPool.getDefault();
				CtClass cc = cp
						.get("org.apache.struts.action.ActionServlet");
				cp.importPackage("io.highway.to.urlhell.*");
				CtMethod m = cc.getDeclaredMethod("destroyConfigDigester");
				m.insertBefore("{");
				m.insertBefore("Struts1Service.getInstance().receiveData(configDigester);");
				m.insertBefore("LOG.error(\"PASSAGE DANS LE TRANSFORMER\");}");
				
				byteCode = cc.toBytecode();
				cc.detach();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			Struts1Service.getInstance().registry();
		}
		
		return byteCode;
	}
	
	
}
