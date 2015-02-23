package io.highway.to.urhell.transformer;

import javassist.CtClass;
import javassist.CtMethod;

public class Struts1Transformer extends AbstractLeechTransformer {

	public Struts1Transformer() {
		super("org/apache/struts/action/ActionServlet",
				"org.apache.struts.action;java.lang.reflect;org.objectweb.asm;org.apache.struts.config.impl;java.util");
	}

	@Override
	protected void doTransform(CtClass cc) throws Exception {
		CtMethod m = cc.getDeclaredMethod("destroyConfigDigester");
		// m.insertBefore(buildReceiveDataStatement("STRUTS_1",
		// "configDigester"));
		StringBuilder sb = new StringBuilder();

		sb.append("ModuleConfigImpl m = (ModuleConfigImpl) configDigester.getRoot();");
		sb.append("Field f;");
		sb.append("List listEntryPath = new ArrayList();");
		sb.append("try {");
		sb.append("f = m.getClass().getDeclaredField(\"actionConfigList\");");
		sb.append("f.setAccessible(true);");
		sb.append("List res = (ArrayList) f.get(m);");
		sb.append("if (res != null) {");
		sb.append("Iterator iter = res.iterator();");
		sb.append("while(iter.hasNext()){");
		sb.append("ActionMapping action = (ActionMapping) iter.next();");
		sb.append("if(action.getType() != null && !\"\".equals(action.getType())){");
		sb.append("try { Class c = Class.forName(action.getType());");
		sb.append("Method[] tabMet = c.getDeclaredMethods(); ");
		sb.append("for (int i=0;i<tabMet.length;i++) {");
		sb.append("EntryPathData entry = new EntryPathData();");
		sb.append("entry.setClassName(action.getType());");
		sb.append("entry.setMethodEntry(tabMet[i].getName());");
		sb.append("if (action.getPrefix() != null && !\"null\".equals(action.getPrefix())) {");
		sb.append("entry.setUri(action.getPrefix() + action.getPath()); }");
		sb.append("else {");
		sb.append("entry.setUri(action.getPath());");
		sb.append(" }");
		sb.append("entry.setTypePath(TypePath.DYNAMIC);");
		sb.append("List listEntryPathData = new ArrayList();");
		sb.append("for (int j=0;j<tabMet[i].getParameterTypes().length;j++) {");
		sb.append("EntryPathParam param = new EntryPathParam();");
		sb.append("param.setKey(\"\");");
		sb.append("param.setTypeParam(TypeParam.PARAM_DATA);");
		sb.append("param.setValue(tabMet[i].getParameterTypes()[j].getName());");
		sb.append("listEntryPathData.add(param);");
		sb.append("}");
		sb.append("entry.setListEntryPathData(listEntryPathData);");
		sb.append("entry.setSignatureName(org.objectweb.asm.Type.getMethodDescriptor(tabMet[i]));");
		sb.append("listEntryPath.add(entry);");
		sb.append("}");
		sb.append(" } catch (ClassNotFoundException e) { e.printStackTrace(); }}");
		sb.append("} } } catch (Exception e) { e.printStackTrace(); }");
		sb.append("CoreEngine.getInstance().getFramework(\"STRUTS_1\").receiveData(listEntryPath);");
		m.insertBefore(sb.toString());

	}
}
