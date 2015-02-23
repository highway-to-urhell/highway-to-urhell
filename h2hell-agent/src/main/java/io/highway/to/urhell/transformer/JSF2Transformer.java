package io.highway.to.urhell.transformer;

import javassist.CtClass;
import javassist.CtMethod;

public class JSF2Transformer extends AbstractLeechTransformer {

	public JSF2Transformer() {
		super(
				"com/sun/faces/mgbean/BeanManager",
				"org.objectweb.asm;java.lang.reflect;java.util;java.util.Map;com.sun.faces.mgbean");
	}

	@Override
	protected void doTransform(CtClass cc) throws Exception {
		CtMethod m = cc.getMethod("register",
				"(Lcom/sun/faces/mgbean/ManagedBeanInfo;)V");
		// m.insertAfter(buildReceiveDataStatement("JSF_2", "managedBeans"));
		StringBuilder sb = new StringBuilder();
		sb.append("List listEntryPath = new ArrayList();");
		sb.append("Iterator iter = managedBeans.entrySet().iterator();");
		sb.append("while (iter.hasNext()) {");
		sb.append("java.util.Map.Entry element = (java.util.Map.Entry) iter.next();");
		sb.append("BeanBuilder bb = (BeanBuilder) element.getValue();");
		sb.append("String className = bb.getManagedBeanInfo().getClassName();");
		sb.append("List res = new ArrayList();");
		sb.append("	try {");
		sb.append("Method[] tabMethod = Class.forName(className).getDeclaredMethods();");
		sb.append("for (int i = 0; i < tabMethod.length; i++) {");
		sb.append("res.add(tabMethod[i].getName());");
		sb.append("}");
		sb.append("} catch (ClassNotFoundException e) {");
		sb.append("	e.printStackTrace();");
		sb.append("}");
		sb.append("Iterator iterName = res.iterator();");
		sb.append("while (iterName.hasNext()) {");
		sb.append("String nameMethod = (String) iterName.next();");
		sb.append("EntryPathData entry = new EntryPathData();");
		sb.append("entry.setHttpMethod(HttpMethod.UNKNOWN);");
		sb.append("entry.setMethodEntry(nameMethod);");
		sb.append("entry.setClassName(className);");
		sb.append("entry.setTypePath(TypePath.DYNAMIC);");
		sb.append("List listSignature = new ArrayList();");
		sb.append("String resSignature = \"\";");
		sb.append("try {");
		sb.append("Class c = Class.forName(className);");
		sb.append("Method[] tabDeclared = c.getDeclaredMethods();");
		sb.append("for (int i = 0; i < tabDeclared.length; i++) {");
		sb.append("if (tabDeclared[i].getName().equals(nameMethod)) {");
		sb.append("resSignature = org.objectweb.asm.Type.getMethodDescriptor(tabDeclared[i]);");
		sb.append("}");
		sb.append("}");
		sb.append("} catch (ClassNotFoundException e) {");
		sb.append("	e.printStackTrace();");
		sb.append("}");
		sb.append("entry.setSignatureName(resSignature);");
		sb.append("listEntryPath.add(entry);");
		sb.append("}");
		sb.append("}");
		sb.append("CoreEngine.getInstance().getFramework(\"JSF_2\").receiveData(listEntryPath);");
		m.insertAfter(sb.toString());
	}
}
