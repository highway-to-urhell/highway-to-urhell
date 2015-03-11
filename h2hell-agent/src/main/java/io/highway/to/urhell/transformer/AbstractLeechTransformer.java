package io.highway.to.urhell.transformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLeechTransformer implements ClassFileTransformer {

    private final Logger log = LoggerFactory
            .getLogger(this.getClass());
    private final String classNameToTransformNormalized;
    private final String classNameToTransform;

    private List<String> importPackages = new ArrayList<String>();

    public AbstractLeechTransformer(String classNameToTransform) {
        this.classNameToTransform = classNameToTransform;
        this.classNameToTransformNormalized = classNameToTransform.replace("/", ".");
        addImportPackage(
                "io.highway.to.urhell",
                "io.highway.to.urhell.domain",
                "io.highway.to.urhell.service");
    }

    protected void addImportPackage(String... packages) {
        for (String packageName : packages) {
            this.importPackages.add(packageName);
        }
    }

    public byte[] transform(ClassLoader loader, String className,
                            Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
    	/*if(className.equals("io/test/H2hellServlet")){
    		try {
                ClassPool cp = ClassPool.getDefault();
                cp.appendClassPath(new LoaderClassPath(loader));
                CtClass cc = cp
                        .get("io.test.H2hellServlet");
                CtMethod[] tabMethod=cc.getDeclaredMethods();
                for(int i=0;i<tabMethod.length;i++){
                	log.error("#####"+tabMethod[i].getSignature());
                }
                classfileBuffer = cc.toBytecode();
                cc.detach();

            } catch (NotFoundException ex) {
                log.error("not found ", ex);
            } catch (Exception ex) {
                log.error("Fail to Transform {} with {}", classNameToTransform, this.getClass(), ex);
            }

    	}
    	if(className.equals("io/test/H2hellFilter")){
    		try {
                ClassPool cp = ClassPool.getDefault();
                cp.appendClassPath(new LoaderClassPath(loader));
                CtClass cc = cp
                        .get("io.test.H2hellFilter");
                CtMethod[] tabMethod=cc.getDeclaredMethods();
                for(int i=0;i<tabMethod.length;i++){
                	log.error("#####"+tabMethod[i].getSignature());
                }
                classfileBuffer = cc.toBytecode();
                cc.detach();

            } catch (NotFoundException ex) {
                log.error("not found ", ex);
            } catch (Exception ex) {
                log.error("Fail to Transform {} with {}", classNameToTransform, this.getClass(), ex);
            }

    	}*/
    	
        if (className.equals(classNameToTransform)) {
            log.info("Going to Transform {} with {}", classNameToTransform, this.getClass());
            try {
                ClassPool cp = ClassPool.getDefault();
                cp.appendClassPath(new LoaderClassPath(loader));
                for (String importPackage : importPackages) {
                    cp.importPackage(importPackage);
                }
                CtClass cc = cp
                        .get(classNameToTransformNormalized);

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
