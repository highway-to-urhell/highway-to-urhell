package io.highway.to.urhell.transformer;

import io.highway.to.urhell.domain.BreakerData;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericTransformer implements ClassFileTransformer {

	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private Map<String, List<BreakerData>> mapToTransform = null;

	public GenericTransformer(Map<String, List<BreakerData>> mapTransform) {
		mapToTransform = mapTransform;
	}

	public byte[] transform(ClassLoader loader, String className,
			Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {
		String classNameToTransform = null;
		if (!mapToTransform.get(className).isEmpty()) {
			try {
				List<BreakerData> listbd = mapToTransform.get(className);
				ClassPool cp = ClassPool.getDefault();
				cp.appendClassPath(new LoaderClassPath(loader));
				addImportPackage(cp);
				classNameToTransform = listbd.get(0).getClassName();
				CtClass cc = cp.get(classNameToTransform);
				for (BreakerData bd : listbd) {
					insertCode(bd,cc);
				}
				classfileBuffer = cc.toBytecode();
				cc.detach();
			} catch (NotFoundException ex) {
				LOGGER.error("not found ", ex);
			} catch (Exception ex) {
				LOGGER.error("Fail to Transform {} with {}",
						classNameToTransform, this.getClass(), ex);
			}
		}
		return classfileBuffer;
	}
	
	private void insertCode(BreakerData bd,CtClass cc){
		LOGGER.info(
				"Going to Transform {} with methodName {} and signature {} this class {}",
				bd.getClassName(), bd.getMethodName(),
				bd.getSignatureName(), this.getClass());

		CtMethod m;
		try {
			m = cc.getMethod(bd.getMethodName(),bd.getSignatureName());
			m.insertBefore(generateCmd(bd.getClassName(),bd.getMethodName()));
		} catch (NotFoundException | CannotCompileException e) {
			LOGGER.error("Insert Code for className {} and methodName {} fails msg {}",bd.getClassName(),bd.getMethodName(),e.getMessage());
		}
	}

	private String generateCmd(String className, String methodName) {
		return "GatherService.getInstance().gather(\"" + className + "."+ methodName + "\");";
	}

	private void addImportPackage(ClassPool cp) {
		cp.importPackage("io.highway.to.urhell");
		cp.importPackage("io.highway.to.urhell.domain");
		cp.importPackage("io.highway.to.urhell.service");
		cp.importPackage("io.highway.to.urhell.service.impl");
	}

}
