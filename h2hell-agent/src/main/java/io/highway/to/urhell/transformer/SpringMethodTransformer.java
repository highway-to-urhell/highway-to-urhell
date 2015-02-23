package io.highway.to.urhell.transformer;

import javassist.CtClass;
import javassist.CtMethod;

public class SpringMethodTransformer extends AbstractLeechTransformer {

	public SpringMethodTransformer() {
		super(
				"org/springframework/web/servlet/mvc/method/annotation/RequestMappingHandlerMapping",
				"org.springframework.core;org.springframework.web.method;org.springframework.web.servlet.mvc.method;java.util.Map;java.util.Map.Entry;java.util;java.lang.reflect;org.objectweb.asm");
	}

	@Override
	protected void doTransform(CtClass cc) throws Exception {
		CtMethod m = cc.getMethod("afterPropertiesSet", "()V");
		// m.insertAfter(buildReceiveDataStatement("SPRING_METHOD",
		// "getHandlerMethods()"));
		StringBuilder sb = new StringBuilder();
		sb.append("List listEntryPath = new ArrayList();");
		sb.append("Iterator iter = getHandlerMethods().keySet().iterator();");
		sb.append("while (iter.hasNext()) {");
		sb.append("RequestMappingInfo requestMappingInfo = (RequestMappingInfo) iter.next();");
		sb.append("HandlerMethod handler = (HandlerMethod) getHandlerMethods().get(requestMappingInfo);");
		sb.append("EntryPathData entrypath = new EntryPathData();");
		sb.append("entrypath.setTypePath(TypePath.DYNAMIC);");
		sb.append("entrypath.setUri(requestMappingInfo.getPatternsCondition().toString());");
		sb.append("String removeClass = \"\";");
		sb.append("if (handler.getBeanType().toString().contains(\"class\")) {");
		sb.append("removeClass = handler.getBeanType().toString().replace(\"class \", \"\");");
		sb.append("} else {");
		sb.append("removeClass = handler.getBeanType().toString();");
		sb.append("}");
		sb.append("String className = removeClass;");
		sb.append("String methodName = handler.getMethod().getName();");
		sb.append("entrypath.setClassName(className);");
		sb.append("entrypath.setMethodEntry(methodName);");
		sb.append("String internalSignature = \"\";");
		sb.append("try {");
		sb.append("Class c = Class.forName(className);");
		sb.append("Method[] tabMethod = c.getDeclaredMethods();");
		sb.append("for (int i = 0; i < tabMethod.length; i++) {");
		sb.append("if (tabMethod[i].getName().equals(methodName)) {");
		sb.append("internalSignature = org.objectweb.asm.Type.getMethodDescriptor(tabMethod[i]);");
		sb.append("}");
		sb.append("}");
		sb.append("} catch (ClassNotFoundException e) {");
		sb.append("e.printStackTrace();");
		sb.append("}");
		sb.append("entrypath.setSignatureName(internalSignature);");
		sb.append("List listEntryPathData = new ArrayList();");
		sb.append("if (handler.getMethodParameters() != null) {");
		sb.append("MethodParameter[] mParam = handler.getMethodParameters();");
		sb.append("for (int j = 0; j < mParam.length; j++) {");
		sb.append("EntryPathParam param = new EntryPathParam();");
		sb.append("param.setKey(String.valueOf(mParam[j].getParameterIndex()));");
		sb.append("param.setTypeParam(TypeParam.PARAM_DATA);");
		sb.append("param.setValue(mParam[j].getParameterType().toString());");
		sb.append("listEntryPathData.add(param);");
		sb.append("}");
		sb.append("}");
		sb.append("entrypath.setListEntryPathData(listEntryPathData);");
		sb.append("listEntryPath.add(entrypath);");
		sb.append("}");
		sb.append("CoreEngine.getInstance().getFramework(\"SPRING_METHOD\").receiveData(listEntryPath);");
		m.insertAfter(sb.toString());
	}
}
