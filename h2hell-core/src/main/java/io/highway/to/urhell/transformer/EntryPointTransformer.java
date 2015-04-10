package io.highway.to.urhell.transformer;

import io.highway.to.urhell.domain.BreakerData;
import javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.Map;

public class EntryPointTransformer implements ClassFileTransformer {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private Map<String, List<BreakerData>> mapToTransform = null;

    public EntryPointTransformer(Map<String, List<BreakerData>> mapTransform) {
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
                    insertCode(bd, cc);
                }
                classfileBuffer = cc.toBytecode();
                cc.detach();
            } catch (Exception ex) {
                LOGGER.error("Fail to Transform " + classNameToTransform, ex);
            }
        }
        return classfileBuffer;
    }

    private void insertCode(BreakerData bd, CtClass cc) {
        LOGGER.info(
                "Going to Transform {} with methodName {} and signature {}",
                bd.getClassName(), bd.getMethodName(),
                bd.getSignatureName());

        CtMethod m;
        try {
            m = cc.getMethod(bd.getMethodName(), bd.getSignatureName());
            m.insertBefore(generateCmd(bd.getClassName(), bd.getMethodName()));
        } catch (NotFoundException | CannotCompileException e) {
            LOGGER.error("Insert Code for className " + bd.getClassName() + "  and methodName " + bd.getMethodName() + "  fails msg {}", e);
        }
    }

    private String generateCmd(String className, String methodName) {
        return "GatherService.getInstance().gatherInvocation(\"" + className + "." + methodName + "\");";
    }

    private void addImportPackage(ClassPool cp) {
        cp.importPackage("io.highway.to.urhell");
        cp.importPackage("io.highway.to.urhell.domain");
        cp.importPackage("io.highway.to.urhell.service");
        cp.importPackage("io.highway.to.urhell.service.impl");
    }

}
