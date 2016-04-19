package com.highway2urhell.transformer;

import javassist.CtClass;
import javassist.CtConstructor;

public class GWTTransformer extends AbstractLeechTransformer {

    public GWTTransformer() {
        super("com/google/gwt/user/server/rpc/RemoteServiceServlet");
        addImportPackage("java.lang.reflect", "java.util",
                "org.reflections", "org.reflections.util",
                "org.reflections.util.ClasspathHelper", "org.objectweb.asm",
                "com.google.gwt.user.client.rpc", "java.util.Map");
    }

    @Override
    protected void doTransform(CtClass cc) throws Exception {
        CtConstructor c = cc.getConstructor("()V");
        String h2hHookCode = "" +
                "List listEntryPath = new ArrayList();" +
                "final Set urlClassLoader = ClasspathHelper.forClassLoader(null);" +
                "Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(urlClassLoader));" +
                "Set setGwtService = reflections.getTypesAnnotatedWith(RemoteServiceRelativePath.class);" +
                "Iterator iter = setGwtService.iterator();" +
                "while (iter.hasNext()) {" +
                "   Class c = (Class) iter.next();" +
                "   RemoteServiceRelativePath remoteAnnotation = (RemoteServiceRelativePath) c.getAnnotation(RemoteServiceRelativePath.class);" +
                "   try {" +
                "	    Set setGwtServiceServer = reflections.getSubTypesOf(Class.forName(c.getName()));" +
                "	    Iterator iterServer = setGwtServiceServer.iterator();" +
                "	    while (iterServer.hasNext()) {" +
                "           Class realName = (Class) iterServer.next();" +
                "		    Method[] tabMethod = realName.getDeclaredMethods();" +
                "			for (int j=0;j<tabMethod.length;j++) {" +
                "				EntryPathData entry = new EntryPathData();" +
                "				entry.setTypePath(TypePath.DYNAMIC);" +
                "				entry.setHttpMethod(\"POST\");" +
                "				entry.setClassName(realName.getName());" +
                "				entry.setMethodName(tabMethod[j].getName());" +
                "				entry.setUri(remoteAnnotation.value());" +
                "				entry.setSignatureName(org.objectweb.asm.Type.getMethodDescriptor(tabMethod[j]));" +
                "				listEntryPath.add(entry);" +
                "			}" +
                "		}" +
                "	} catch (ClassNotFoundException e) {" +
                "       e.printStackTrace();" +
                "	}" +
                "}" +
                "CoreEngine.getInstance().getFramework(\"GWT\").receiveData(listEntryPath);";
        c.insertBefore(h2hHookCode);
    }
}
