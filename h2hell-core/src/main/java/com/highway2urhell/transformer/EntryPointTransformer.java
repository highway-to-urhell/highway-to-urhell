package com.highway2urhell.transformer;

import com.highway2urhell.domain.EntryPathData;

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

public class EntryPointTransformer implements ClassFileTransformer {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private Map<String, List<EntryPathData>> mapToTransform = null;

    public EntryPointTransformer(Map<String, List<EntryPathData>> mapTransform) {
        mapToTransform = mapTransform;
    }

    public byte[] transform(ClassLoader loader, String className,
                            Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        String classNameToTransform = null;
        if (!mapToTransform.get(className).isEmpty()) {
            try {
                List<EntryPathData> listbd = mapToTransform.get(className);
                ClassPool cp = ClassPool.getDefault();
                cp.appendClassPath(new LoaderClassPath(loader));
                addImportPackage(cp);
                classNameToTransform = listbd.get(0).getClassName();
                CtClass cc = cp.get(classNameToTransform);
                for (EntryPathData entry : listbd) {
                    insertCode(entry, cc);
                }
                classfileBuffer = cc.toBytecode();
                cc.detach();
            } catch (Exception ex) {
                LOGGER.error("Fail to Transform " + classNameToTransform, ex);
            }
        }
        return classfileBuffer;
    }

    private void insertCode(EntryPathData entry, CtClass cc) {
        LOGGER.info(
                "Going to Transform {} with methodName {} and signature {}",
                entry.getClassName(), entry.getMethodName(),
                entry.getSignatureName());

        CtMethod m;
        try {
            m = cc.getMethod(entry.getMethodName(), entry.getSignatureName());
            m.addLocalVariable("startH2H", CtClass.longType);
            m.insertBefore(generateCmd(entry.getClassName(), entry.getMethodName())+"startH2H = System.currentTimeMillis();");
            m.insertAfter("final long endH2H = System.currentTimeMillis();"+generateCmdPerf(entry.getClassName(), entry.getMethodName()));
        } catch (NotFoundException | CannotCompileException e) {
            LOGGER.error("Insert Code for className " + entry.getClassName() + "  and methodName " + entry.getMethodName() + "  fails msg {}", e);
        }
    }

    private String generateCmd(String className, String methodName) {
        return "GatherService.getInstance().gatherInvocation(\"" + className + "." + methodName + "\");";
    }
    
    private String generateCmdPerf(String className, String methodName) {
        return "GatherService.getInstance().gatherPerformance(\"" + className + "." + methodName + "\",(endH2H-startH2H));";
    }

    private void addImportPackage(ClassPool cp) {
        cp.importPackage("com.highway2urhell");
        cp.importPackage("com.highway2urhell.domain");
        cp.importPackage("com.highway2urhell.service");
        cp.importPackage("com.highway2urhell.service.impl");
    }

}
