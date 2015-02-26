package io.highway.to.urhell.transformer;

import javassist.CtClass;
import javassist.CtMethod;

public class Struts2PrepareTransformer extends AbstractLeechTransformer {

	public Struts2PrepareTransformer() {
		super(
				"org/apache/struts2/dispatcher/ng/filter/StrutsPrepareFilter",
				"com.opensymphony.xwork2.config;com.opensymphony.xwork2.config.entities;java.util;java.lang.reflect;org.objectweb.asm");
	}

	@Override
	protected void doTransform(CtClass cc) throws Exception {

		CtMethod m = cc
				.getMethod("postInit",
						"(Lorg/apache/struts2/dispatcher/Dispatcher;Ljavax/servlet/FilterConfig;)V");
		// m.insertBefore(buildReceiveDataStatement("STRUTS_2",
		// "dispatcher.getConfigurationManager()"));
		StringBuilder sb = new StringBuilder();
		sb.append("List listEntryPath = new ArrayList();");
		sb.append("ConfigurationManager cm = dispatcher.getConfigurationManager();");
		sb.append("Configuration cf = cm.getConfiguration();");
		sb.append("Collection colPackages = cf.getPackageConfigs().values();");
		sb.append("if (colPackages != null) {");
		sb.append("Iterator ite = colPackages.iterator();while(ite.hasNext()) {");
		sb.append("PackageConfig pack = (PackageConfig) ite.next();");
		sb.append("Collection colActionConfigs = pack.getActionConfigs().values();");
		sb.append("Iterator iteCol = colActionConfigs.iterator();while(iteCol.hasNext()){");
		sb.append("ActionConfig action = (ActionConfig) iteCol.next();");
		sb.append("if (action.getClassName() != null && !\"\".equals(action.getClassName())) {");
		sb.append("try {");
		sb.append("Class c = Class.forName(action.getClassName());");
		sb.append("Method[] tabM = c.getDeclaredMethods();for (int i=0;i<tabM.length;i++) {");
		sb.append("Method m = tabM[i];");
		sb.append("EntryPathData entry = new EntryPathData();");
		sb.append("entry.setTypePath(TypePath.DYNAMIC);");
		sb.append("entry.setClassName(action.getClassName());");
		sb.append("entry.setMethodEntry(m.getName());");
		sb.append("entry.setUri(action.getName());");
		sb.append("entry.setSignatureName(org.objectweb.asm.Type.getMethodDescriptor(m));");
		sb.append("List listEntryPathData = new ArrayList();");
		sb.append("for (int j=0;j<m.getParameterTypes().length;j++) {");
		sb.append("EntryPathParam param = new EntryPathParam();");
		sb.append("param.setKey(\"\");");
		sb.append("param.setTypeParam(TypeParam.PARAM_DATA);");
		sb.append("param.setValue(m.getParameterTypes()[j].getName());");
		sb.append("listEntryPathData.add(param);");
		sb.append("}");
		sb.append("entry.setListEntryPathData(listEntryPathData);");
		sb.append("listEntryPath.add(entry);");
		sb.append("}");
		sb.append("} catch (ClassNotFoundException e) {");
		sb.append("e.printStackTrace();");
		sb.append("}");
		sb.append("}");
		sb.append("}");
		sb.append("}");
		sb.append("}");
		sb.append("CoreEngine.getInstance().getFramework(\"STRUTS_2\").receiveData(listEntryPath);");
		m.insertBefore(sb.toString());
	}
}
