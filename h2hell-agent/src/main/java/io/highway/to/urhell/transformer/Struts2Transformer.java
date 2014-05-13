package io.highway.to.urhell.transformer;

import io.highway.to.urhell.domain.FrameworkEnum;

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class Struts2Transformer implements LeechTransformer{

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
                m.insertAfter("CoreEngine.getInstance().getFramework(FrameworkEnum.STRUTS_2_X).receiveData(configurationManager);");
				m.insertAfter("}");
				
				byteCode = cc.toBytecode();
				cc.detach();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return byteCode;
	}
	
	
	
	

}
