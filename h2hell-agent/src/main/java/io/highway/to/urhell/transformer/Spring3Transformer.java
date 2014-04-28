package io.highway.to.urhell.transformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class Spring3Transformer implements ClassFileTransformer {

	public byte[] transform(ClassLoader loader, String className,
			Class classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {

		byte[] byteCode = classfileBuffer;

		if (className.equals("pachage.to.fix")) {

			try {
				ClassPool cp = ClassPool.getDefault();
				CtClass cc = cp
						.get("pachage.to.fix");
				cp.importPackage("io.highway.to.urlhell.*");
				CtMethod m = cc.getDeclaredMethod("method");
				m.insertAfter("{");
				m.insertAfter("");
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
