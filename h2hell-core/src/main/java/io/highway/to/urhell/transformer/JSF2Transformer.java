package io.highway.to.urhell.transformer;

import javassist.CtClass;
import javassist.CtMethod;

public class JSF2Transformer extends AbstractLeechTransformer {

    public JSF2Transformer() {
        super("com/sun/faces/mgbean/BeanManager");
        addImportPackage(
                "org.objectweb.asm",
                "java.lang.reflect",
                "java.util",
                "java.util.Map",
                "com.sun.faces.mgbean");
    }

    @Override
    protected void doTransform(CtClass cc) throws Exception {
        CtMethod m = cc.getMethod("register",
                "(Lcom/sun/faces/mgbean/ManagedBeanInfo;)V");
        String h2hHookCode = "" +
                "List listEntryPath = new ArrayList();" +
                "Iterator iter = managedBeans.entrySet().iterator();" +
                "while (iter.hasNext()) {" +
                "   java.util.Map.Entry element = (java.util.Map.Entry) iter.next();" +
                "   BeanBuilder bb = (BeanBuilder) element.getValue();" +
                "   String className = bb.getManagedBeanInfo().getClassName();" +
                "   List res = new ArrayList();" +
                "	try {" +
                "       Method[] tabMethod = Class.forName(className).getDeclaredMethods();" +
                "       for (int i = 0; i < tabMethod.length; i++) {" +
                "           res.add(tabMethod[i].getName());" +
                "       }" +
                "   } catch (ClassNotFoundException e) {" +
                "	    e.printStackTrace();" +
                "   }" +
                "   Iterator iterName = res.iterator();" +
                "   while (iterName.hasNext()) {" +
                "       String nameMethod = (String) iterName.next();" +
                "       EntryPathData entry = new EntryPathData();" +
                "       entry.setHttpMethod(HttpMethod.UNKNOWN.name());" +
                "       entry.setMethodName(nameMethod);" +
                "       entry.setClassName(className);" +
                "       entry.setTypePath(TypePath.DYNAMIC.name());" +
                "       List listSignature = new ArrayList();" +
                "       String resSignature = \"\";" +
                "       try {" +
                "           Class c = Class.forName(className);" +
                "           Method[] tabDeclared = c.getDeclaredMethods();" +
                "           for (int i = 0; i < tabDeclared.length; i++) {" +
                "               if (tabDeclared[i].getName().equals(nameMethod)) {" +
                "                resSignature = org.objectweb.asm.Type.getMethodDescriptor(tabDeclared[i]);" +
                "               }" +
                "          }" +
                "       } catch (ClassNotFoundException e) {" +
                "	        e.printStackTrace();" +
                "       }" +
                "       entry.setSignatureName(resSignature);" +
                "       listEntryPath.add(entry);" +
                "   }" +
                "}" +
                "CoreEngine.getInstance().getFramework(\"JSF_2\").receiveData(listEntryPath);";
        m.insertAfter(h2hHookCode);
    }
}