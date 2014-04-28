package io.highway.to.urhell.transformer;

import io.highway.to.urhell.service.impl.Struts2Service;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class Struts2Transformer implements ClassFileTransformer {

	public byte[] transform(ClassLoader loader, String className,
			Class classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {

		byte[] byteCode = classfileBuffer;

		if (className.equals("org/apache/struts2/dispatcher/Dispatcher")) {

			try {
				ClassPool cp = ClassPool.getDefault();
				CtClass cc = cp
						.get("org.apache.struts2.dispatcher.Dispatcher");
				cp.importPackage("io.highway.to.urlhell.*");
				CtMethod m = cc.getDeclaredMethod("init");
				m.insertAfter("{");
				m.insertAfter("Struts2Service.getInstance().receiveData(configurationManager);");
				m.insertAfter("LOG.error(\"PASSAGE DANS LE TRANSFORMER\");}");
				
				byteCode = cc.toBytecode();
				cc.detach();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			Struts2Service.getInstance().registry();
		}

		return byteCode;
	}
	
	
	
	

}
