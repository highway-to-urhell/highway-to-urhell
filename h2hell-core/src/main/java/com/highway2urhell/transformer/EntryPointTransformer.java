package com.highway2urhell.transformer;

import com.highway2urhell.domain.EntryPathData;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntryPointTransformer implements ClassFileTransformer {

    private Map<String, List<EntryPathData>> mapToTransform = null;
    private Boolean performance = false;

    public EntryPointTransformer(Map<String, List<EntryPathData>> mapTransform, Boolean perf) {
        mapToTransform = mapTransform;
        performance = perf;
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
                    if (performance) {
                        insertCodeWithPerf(entry, cc);
                    } else {
                        insertCode(entry, cc);
                    }
                }
                classfileBuffer = cc.toBytecode();
                cc.detach();
            } catch (Exception ex) {
                System.err.println("Fail to Transform " + classNameToTransform+ex);
            }
        }
        return classfileBuffer;
    }

    private void insertCodeWithPerf(EntryPathData entry, CtClass cc) {
        System.out.println(
                "Going to Transform insertCodeWithPerf "+entry.getClassName()+"with methodName "+ entry.getMethodName()+" and signature "+entry.getSignatureName());

        CtMethod m;
        try {
            m = cc.getMethod(entry.getMethodName(), entry.getSignatureName());
            m.addLocalVariable("startH2H", CtClass.longType);
            CtClass ss = ClassPool.getDefault().get("java.lang.String");
            m.addLocalVariable("listParamsH2H", ss);
            m.insertBefore(generateCmd(m, entry.getClassName(), entry.getMethodName()) + "startH2H = System.currentTimeMillis();");
            m.insertAfter("final long endH2H = System.nanoTime();" + generateCmdPerf(m, entry.getClassName(), entry.getMethodName()));
        } catch (Exception e) {
            System.err.println("Insert Code for className " + entry.getClassName() + "  and methodName " + entry.getMethodName() + "  fails msg "+e);
        }
    }

    private void insertCode(EntryPathData entry, CtClass cc) {
        System.out.println(
                "Going to Transform insertCodeWithPerf "+entry.getClassName()+"with methodName "+ entry.getMethodName()+" and signature "+entry.getSignatureName());

        CtMethod m;
        try {
            m = cc.getMethod(entry.getMethodName(), entry.getSignatureName());
            CtClass ss = ClassPool.getDefault().get("java.lang.String");
            m.addLocalVariable("listParamsH2H", ss);
            m.insertBefore(generateCmd(m, entry.getClassName(), entry.getMethodName()));
        } catch (Exception e) {
            System.err.println("Insert Code for className " + entry.getClassName() + "  and methodName " + entry.getMethodName() + "  fails msg {}"+ e);
        }
    }

    private String generateCmd(CtMethod m, String className, String methodName) throws NotFoundException {
        StringBuilder sbs = new StringBuilder();
        sbs.append(generateParamsLeech(m));
        sbs.append("GatherService.getInstance().gatherInvocation(\"" + className + "." + methodName + "\",listParamsH2H);");
        return sbs.toString();
    }

    private String generateCmdPerf(CtMethod m, String className, String methodName) throws NotFoundException {
        StringBuilder sbs = new StringBuilder();
        sbs.append("GatherService.getInstance().gatherPerformance(\"" + className + "." + methodName + "\",(endH2H-startH2H),listParamsH2H);");
        return sbs.toString();
    }

    private void addImportPackage(ClassPool cp) {
        cp.importPackage("com.highway2urhell");
        cp.importPackage("javax.servlet");
        cp.importPackage("com.highway2urhell.domain");
        cp.importPackage("com.highway2urhell.service");
        cp.importPackage("com.google.gson.Gson");
        cp.importPackage("com.highway2urhell.service.impl");
    }

    private String generateParamsLeech(CtMethod m) {
        StringBuilder sbs = new StringBuilder();
        sbs.append("listParamsH2H=\"\";");
        try {
            Map<Integer, String> hashNameParam = extractNameParameter(m);
            CtClass[] pTypes = m.getParameterTypes();


            sbs.append("Gson gson = new Gson();");
            for (int i = 0; i < pTypes.length; i++) {
                sbs.append("    LeechParamMethodData leech" + i + " = new LeechParamMethodData();");
                sbs.append("    String val" + i + "=\"\";");
                sbs.append("    try{");
                sbs.append("        leech" + i + ".setNameParameter(\"" + hashNameParam.get((i + 2)) + "\");");
                sbs.append("        leech" + i + ".setNameClass($" + (i + 1) + ".getClass().getName());");
                //hack
                sbs.append("        if(leech" + i + ".getNameParameter()!=null && leech" + i + ".getNameClass()!=null){");
                sbs.append("                if($" + (i + 1) + " instanceof ServletRequest || ");
                sbs.append("                   $" + (i + 1) + " instanceof ServletResponse || ");
                sbs.append("                   $" + (i + 1) + " instanceof Filter || ");
                sbs.append("                   $" + (i + 1) + " instanceof FilterChain || ");
                sbs.append("                   leech" + i + ".getNameClass().contains(\"Page\") ){");
                sbs.append("                        if ($" + (i + 1) + " instanceof ServletRequest){");
                sbs.append("                            leech" + i + ".setData(((ServletRequest)$" + (i + 1) + ").getParameterMap());");
                sbs.append("                        }else{");
                sbs.append("                            leech" + i + ".setData(\"data is a stream\");");
                sbs.append("                        }");
                sbs.append("                   }else{");
                sbs.append("                            leech" + i + ".setData($" + (i + 1) + ");");
                sbs.append("                        }");
                sbs.append("         }else{");
                sbs.append("            leech" + i + ".setData(\"data is not convertible\");");
                sbs.append("         }");
                sbs.append("        val" + i + " = gson.toJson(leech" + i + ").toString();");
                sbs.append("    }catch(Throwable e) {");
                sbs.append("        System.err.println(\"name : " + hashNameParam.get((i + 2)) + " class : \" + $" + (i + 1) + ".getClass().getName() );");
                sbs.append("        leech" + i + ".setNameParameter(\"" + hashNameParam.get((i + 2)) + "\");");
                sbs.append("        leech" + i + ".setNameClass($" + (i + 1) + ".getClass().getName());");
                sbs.append("        leech" + i + ".setData(\"ERROR_PARAM\");");
                sbs.append("        val" + i + " = gson.toJson(leech" + i + ").toString();");
                sbs.append("    }");
            }
            sbs.append("        listParamsH2H=");
            if (pTypes.length == 0) {
                sbs.append("\"\";");
            }
            for (int i = 0; i < pTypes.length; i++) {
                sbs.append("val" + i + "+\"@@@@@\"");
                if (i == pTypes.length - 1) {
                    sbs.append(";");
                } else {
                    sbs.append("+");
                }
            }
        } catch (NotFoundException nf) {
            sbs = new StringBuilder();
            sbs.append("listParamsH2H = \"\";");
        }
        //LOGGER.error("INSERTION BORDEL DE MERDE " + sbs.toString());
        return sbs.toString();
    }


    private Map<Integer, String> extractNameParameter(CtMethod m) throws NotFoundException {
        Map<Integer, String> hashNameParam = new HashMap<Integer, String>();
        CodeAttribute codeAttribute = (CodeAttribute) m.getMethodInfo().getAttribute("Code");
        if (codeAttribute != null) {
            LocalVariableAttribute localVariableAttribute = (LocalVariableAttribute) codeAttribute.getAttribute("LocalVariableTable");
            if (localVariableAttribute != null && localVariableAttribute.tableLength() >= m.getParameterTypes().length) {
                for (int i = 0; i < m.getParameterTypes().length + 2; i++) {
                    String name = localVariableAttribute.getConstPool().getUtf8Info(localVariableAttribute.nameIndex(i));
                    if (!name.equals("this")) {
                        hashNameParam.put(i, name);
                    }
                }
            }
        }
        return hashNameParam;
    }

    public void toto() {

    }
}
