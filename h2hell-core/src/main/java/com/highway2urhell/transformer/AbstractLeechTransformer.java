package com.highway2urhell.transformer;

import javassist.*;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.Mnemonic;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractLeechTransformer implements ClassFileTransformer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final String classNameToTransformNormalized;
    private final String classNameToTransform;

    private List<String> importPackages = new ArrayList<String>();

    public AbstractLeechTransformer(String classNameToTransform) {
        this.classNameToTransform = classNameToTransform;
        this.classNameToTransformNormalized = classNameToTransform.replace("/", ".");
        addImportPackage(
                "com.highway2urhell",
                "com.highway2urhell.domain",
                "com.highway2urhell.service",
                "com.highway2urhell.transformer");
    }

    protected void addImportPackage(String... packages) {
        addImportPackage(Arrays.asList(packages));
    }

    protected void addImportPackage(Iterable<String> packages) {
        for (String packageName : packages) {
            this.importPackages.add(packageName);
        }
    }

    public byte[] transform(ClassLoader loader, String className,
                            Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className.equals(classNameToTransform)) {
            log.info("Going to Transform {} with {}", classNameToTransform, this.getClass());
            try {
                ClassPool cp = ClassPool.getDefault();
                cp.insertClassPath(new LoaderClassPath(loader));
                for (String importPackage : importPackages) {
                    cp.importPackage(importPackage);
                }
                CtClass cc = cp.get(classNameToTransformNormalized);
                if (log.isDebugEnabled()) {
                    grabAllMethod(cc);
                }

                doTransform(cc);

                classfileBuffer = cc.toBytecode();
                cc.detach();

            } catch (NotFoundException ex) {
                log.error("not found ", ex);
            } catch (Exception ex) {
                log.error("Fail to Transform {} with {}", classNameToTransform, this.getClass(), ex);
            }
        }

        return classfileBuffer;
    }

    private void grabAllMethod(CtClass cc) {
        for (CtMethod method : cc.getMethods()) {
            log.error(method.getLongName() + "-" + method.getSignature() + " at line " + method.getMethodInfo().getLineNumber(0));
            try {
                method.instrument(
                        new ExprEditor() {
                            public void edit(MethodCall m) throws CannotCompileException {
                                log.error("method invoke into the method observe "+m.getClassName() + "." + m.getMethodName() + " " + m.getSignature());
                            }
                        });
            } catch (CannotCompileException e) {
                log.error("Cannot Compil Exception"+e.getMessage());
            }

        }
        for (CtConstructor c : cc.getConstructors()) {
            log.error(c.getLongName() + "-" + c.getSignature()+" at line "+c.getMethodInfo().getLineNumber(0));
        }

    }

    /**
     * Build receiveDataStatement to be produced by transformer where you want to collectdata
     *
     * @param frameworkName a given frameworkname to invoke
     * @param dataToCollect statement representing data
     * @return produced statement
     */
    public String buildReceiveDataStatement(String frameworkName, String dataToCollect) {
        return "CoreEngine.getInstance().getFramework(\"" + frameworkName + "\").receiveData(" + dataToCollect + ");";
    }

    protected abstract void doTransform(CtClass cc) throws Exception;
}
