package io.highway.to.urhell.transformer;

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class Struts1Transformer implements LeechTransformer {

    public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        byte[] byteCode = classfileBuffer;

        if (className.equals("org/apache/struts/action/ActionServlet")) {

            try {
                ClassPool cp = ClassPool.getDefault();
                CtClass cc = cp.get("org.apache.struts.action.ActionServlet");
                cp.importPackage("io.highway.to.urlhell.*");
                CtMethod m = cc.getDeclaredMethod("destroyConfigDigester");
                m.insertBefore("{");
                m.insertBefore("CoreEngine.getInstance().getFramework(FrameworkEnum.STRUTS_1_X).receiveData(configDigester);");
                m.insertBefore("}");

                byteCode = cc.toBytecode();
                cc.detach();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return byteCode;
    }

}
