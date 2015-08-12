package com.highway2urhell.transformer;

import javassist.CtClass;
import javassist.CtMethod;

public class SpringMethodTransformer extends AbstractLeechTransformer {

    public SpringMethodTransformer() {
        super("org/springframework/web/servlet/mvc/method/annotation/RequestMappingHandlerMapping");
        addImportPackage(
                "org.springframework.core",
                "org.springframework.web.method",
                "org.springframework.web.servlet.mvc.method",
                "java.util.Map",
                "java.util.Map.Entry",
                "java.util",
                "java.lang.reflect",
                "org.objectweb.asm");
    }

    @Override
    protected void doTransform(CtClass cc) throws Exception {
        CtMethod m = cc.getMethod("afterPropertiesSet", "()V");
        String h2hHookCode = "" +
                "List listEntryPath = new ArrayList();" +
                "Iterator iter = getHandlerMethods().keySet().iterator();" +
                "while (iter.hasNext()) {" +
                "   RequestMappingInfo requestMappingInfo = (RequestMappingInfo) iter.next();" +
                "   HandlerMethod handler = (HandlerMethod) getHandlerMethods().get(requestMappingInfo);" +
                "   EntryPathData entrypath = new EntryPathData();" +
                "   entrypath.setTypePath(TypePath.DYNAMIC);" +
                "   entrypath.setUri(requestMappingInfo.getPatternsCondition().toString());" +
                "   String removeClass = \"\";" +
                "   if (handler.getBeanType().toString().contains(\"class\")) {" +
                "       removeClass = handler.getBeanType().toString().replace(\"class \", \"\" );" +
                "   } else {" +
                "       removeClass = handler.getBeanType().toString();" +
                "   }" +
                "   String className = removeClass;" +
                "   String methodName = handler.getMethod().getName();" +
                "   entrypath.setClassName(className);" +
                "   entrypath.setMethodName(methodName);" +
                "   String internalSignature = \"\";" +
                "   try {" +
                "       Class c = Class.forName(className);" +
                "       Method[] tabMethod = c.getDeclaredMethods();" +
                "       for (int i = 0; i < tabMethod.length; i++) {" +
                "           if (tabMethod[i].getName().equals(methodName)) {" +
                "               internalSignature = org.objectweb.asm.Type.getMethodDescriptor(tabMethod[i]);" +
                "           }" +
                "       }" +
                "   } catch (ClassNotFoundException e) {" +
                "       e.printStackTrace();" +
                "   }" +
                "   entrypath.setSignatureName(internalSignature);" +
                "   List listEntryPathData = new ArrayList();" +
                "   if (handler.getMethodParameters() != null) {" +
                "       MethodParameter[] mParam = handler.getMethodParameters();" +
                "       for (int j = 0; j < mParam.length; j++) {" +
                "           EntryPathParam param = new EntryPathParam();" +
                "           param.setKey(String.valueOf(mParam[j].getParameterIndex()));" +
                "           param.setTypeParam(TypeParam.PARAM_DATA);" +
                "           param.setValue(mParam[j].getParameterType().toString());" +
                "           listEntryPathData.add(param);" +
                "       }" +
                "   }" +
                "   entrypath.setListEntryPathData(listEntryPathData);" +
                "   listEntryPath.add(entrypath);" +
                "}" +
                "CoreEngine.getInstance().getFramework(\"SPRING_METHOD\").receiveData(listEntryPath);";
        m.insertAfter(h2hHookCode);
    }
}
