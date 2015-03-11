package io.highway.to.urhell.transformer;

import javassist.CtClass;
import javassist.CtConstructor;

public class GWTTransformer extends AbstractLeechTransformer {

	public GWTTransformer() {
		super("com/google/gwt/user/server/rpc/RemoteServiceServlet");
		addImportPackage("java.lang.reflect", "java.util",
				"org.reflections", "org.reflections.util",
				"org.reflections.util.ClasspathHelper","org.objectweb.asm",
				"com.google.gwt.user.client.rpc", "java.util.Map");
	}

	@Override
	protected void doTransform(CtClass cc) throws Exception {
		CtConstructor c = cc.getConstructor("()V");
		StringBuilder sb = new StringBuilder();
		sb.append("List listEntryPath = new ArrayList();");
		sb.append("final Set urlClassLoader = ClasspathHelper.forClassLoader(null);");
		sb.append("Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(urlClassLoader));");
		sb.append("Set setGwtService = reflections.getTypesAnnotatedWith(RemoteServiceRelativePath.class);");
		sb.append("Iterator iter = setGwtService.iterator();");
		sb.append("while (iter.hasNext()) {");
		sb.append("Class c = (Class) iter.next();");
		sb.append("RemoteServiceRelativePath remoteAnnotation = (RemoteServiceRelativePath) c.getAnnotation(RemoteServiceRelativePath.class);");
		sb.append("try {");
		sb.append("	Set setGwtServiceServer = reflections.getSubTypesOf(Class.forName(c.getName()));");
		sb.append("	Iterator iterServer = setGwtServiceServer.iterator();");
		sb.append("	while (iterServer.hasNext()) {");
		sb.append("Class realName = (Class) iterServer.next();");
		sb.append("		Method[] tabMethod = realName.getDeclaredMethods();");
		sb.append("			for (int j=0;j<tabMethod.length;j++) {");
		sb.append("				EntryPathData entry = new EntryPathData();");
		sb.append("				entry.setTypePath(TypePath.DYNAMIC);");
		sb.append("				entry.setHttpMethod(HttpMethod.POST);");
		sb.append("				entry.setClassName(realName.getName());");
		sb.append("				entry.setMethodEntry(tabMethod[j].getName());");
		sb.append("				entry.setUri(remoteAnnotation.value());");
		sb.append("				entry.setSignatureName(org.objectweb.asm.Type.getMethodDescriptor(tabMethod[j]));");
		sb.append("				listEntryPath.add(entry);");
		sb.append("			}");
		sb.append("		}");
		sb.append("	} catch (ClassNotFoundException e) {");
		sb.append("e.printStackTrace();");
		sb.append("	}");
		sb.append("}");
		sb.append("CoreEngine.getInstance().getFramework(\"GWT\").receiveData(listEntryPath);");

		c.insertBefore(sb.toString());

	}
}
