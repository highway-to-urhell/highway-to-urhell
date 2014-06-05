package io.highway.to.urhell.transformer;

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Struts2Transformer implements LeechTransformer {
	private static final Logger LOG = LoggerFactory.getLogger(Struts2Transformer.class);
	private static final String CLASSNAME="org/apache/struts2/dispatcher/Dispatcher";
	
    public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

      
        if (className.equals(CLASSNAME)) {
        	 LOG.info(" Transformer "+Struts2Transformer.class.getCanonicalName()+" transform "+CLASSNAME);
            try {
            	ClassPool cp = ClassPool.getDefault();
				cp.appendClassPath(new LoaderClassPath(loader));
                CtClass cc = cp.get("org.apache.struts2.dispatcher.Dispatcher");
                cp.importPackage("io.highway.to.urlhell.CoreEngine");
				cp.importPackage("io.highway.to.urlhell.domain.FrameworkEnum");
                CtMethod m = cc.getDeclaredMethod("init");
                m.insertAfter("CoreEngine.getInstance().getFramework(FrameworkEnum.STRUTS_2_X).receiveData(configurationManager);");
                classfileBuffer = cc.toBytecode();
                cc.detach();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return classfileBuffer;
    }

}
