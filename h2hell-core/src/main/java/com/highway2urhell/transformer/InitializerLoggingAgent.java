package com.highway2urhell.transformer;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class InitializerLoggingAgent implements ClassFileTransformer {
    private final ClassPool pool = new ClassPool(true);


    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            if (className.equals("java/lang/ExceptionInInitializerError")) {
                CtClass klass = pool.makeClass(new ByteArrayInputStream(classfileBuffer));
                CtConstructor[] ctors = klass.getConstructors();
                for (int i = 0; i < ctors.length; i++) {
                    ctors[i].insertAfter("this.printStackTrace();");
                }
                return klass.toBytecode();
            } else {
                return null;
            }
        } catch (Throwable t) {
            return null;
        }
    }
}