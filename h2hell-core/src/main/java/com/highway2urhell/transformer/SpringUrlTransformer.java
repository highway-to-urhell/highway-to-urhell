package com.highway2urhell.transformer;

import javassist.CtClass;
import javassist.CtMethod;

public class SpringUrlTransformer extends AbstractLeechTransformer {

    public SpringUrlTransformer() {
        super("org/springframework/web/servlet/handler/AbstractUrlHandlerMapping");
        addImportPackage(
                "java.util.Map",
                "java.util");
    }

    @Override
    protected void doTransform(CtClass cc) throws Exception {
        CtMethod m = cc.getMethod("registerHandler",
                "(Ljava/lang/String;Ljava/lang/Object;)V");
        String h2hHookCode = "" +
                "List listEntryPath = new ArrayList();" +
                "Iterator iter = handlerMap.keySet().iterator();" +
                "while(iter.hasNext()){" +
                "   String key = (String) iter.next();" +
                "   EntryPathData entry = new EntryPathData();" +
                "   entry.setTypePath(TypePath.DYNAMIC);" +
                "   entry.setUri(key);" +
                "   String fullNameDescriptor = handlerMap.get(key).getClass().toString();" +
                "   if (fullNameDescriptor.contains(\"class\")) {" +
                "       entry.setClassName(fullNameDescriptor.replace(\"class \", \"\"));" +
                "   } else {" +
                "       entry.setClassName(fullNameDescriptor);" +
                "   }" +
                "   listEntryPath.add(entry);" +
                "}" +
                "CoreEngine.getInstance().getFramework(\"SPRING_URL\").receiveData(listEntryPath);";
        m.insertAfter(h2hHookCode);

    }
}
