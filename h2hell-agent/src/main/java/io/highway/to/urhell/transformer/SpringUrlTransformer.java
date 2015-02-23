package io.highway.to.urhell.transformer;

import javassist.CtClass;
import javassist.CtMethod;

public class SpringUrlTransformer extends AbstractLeechTransformer {

	public SpringUrlTransformer() {
		super(
				"org/springframework/web/servlet/handler/AbstractUrlHandlerMapping","java.util.Map;java.util");
	}

	@Override
	protected void doTransform(CtClass cc) throws Exception {
		CtMethod m = cc.getMethod("registerHandler",
				"(Ljava/lang/String;Ljava/lang/Object;)V");
		//m.insertAfter(buildReceiveDataStatement("SPRING_URL", "handlerMap"));

		StringBuilder sb = new StringBuilder();
		sb.append("List listEntryPath = new ArrayList();");
		sb.append("Iterator iter = handlerMap.keySet().iterator();");
		sb.append("while(iter.hasNext()){");
		sb.append("String key = (String) iter.next();");
		sb.append("EntryPathData entry = new EntryPathData();");
		sb.append("entry.setTypePath(TypePath.DYNAMIC);");
		sb.append("entry.setUri(key);");
		sb.append("String fullNameDescriptor = handlerMap.get(key).getClass().toString();");
		sb.append("if (fullNameDescriptor.contains(\"class\")) {");
		sb.append("entry.setClassName(fullNameDescriptor.replace(\"class \", \"\"));");
		sb.append("} else {");
		sb.append("entry.setClassName(fullNameDescriptor);");
		sb.append("}");
		sb.append("listEntryPath.add(entry);}");
		sb.append("CoreEngine.getInstance().getFramework(\"SPRING_URL\").receiveData(listEntryPath);");
		m.insertAfter(sb.toString());

	}
}
