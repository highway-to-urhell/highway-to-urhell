package com.highway2urhell.transformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.Map;



import com.highway2urhell.CoreEngine;
import com.highway2urhell.domain.EntryPathData;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

public class LineNumberEntryPointTransformer implements ClassFileTransformer {

    private Map<String, List<EntryPathData>> mapToTransform = null;
    
    public LineNumberEntryPointTransformer(Map<String, List<EntryPathData>> mapTransform) {
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
                classNameToTransform = listbd.get(0).getClassName();
                CtClass cc = cp.get(classNameToTransform);
                for (EntryPathData entry : listbd) {
                	getLineNumber(entry, cc);
                }
            } catch (Exception ex) {
                System.err.println("Fail to Transform " + classNameToTransform+ ex);
            }
        }
        return classfileBuffer;
    }
    
    private void getLineNumber(EntryPathData entry, CtClass cc) {
        System.out.println(
                "Going to Transform insertCodeWithPerf "+entry.getClassName()+"with methodName "+ entry.getMethodName()+" and signature "+entry.getSignatureName());


        CtMethod m;
        try {
            m = cc.getMethod(entry.getMethodName(), entry.getSignatureName());
            int lineNumber = m.getMethodInfo().getLineNumber(0);
            entry.setLineNumber(Integer.valueOf(lineNumber));
            // Update the data
            CoreEngine.getInstance().updateLineNumberEntryPoint(entry);
        } catch (Exception e) {
            System.err.println("Error during read line number Code for className " + entry.getClassName() + "  and methodName " + entry.getMethodName() + "  fails msg "+e);
        }
    }

  
}
