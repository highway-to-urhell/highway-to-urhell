package io.highway.to.urhell.domain;

import java.util.ArrayList;
import java.util.List;

public class EntryPathData {

	private String uri;
	private String methodName;
	private String className;
	private String classNameNormalized;
	private String signatureName;
	private String typePath = TypePath.UNKNOWN.name();

	public String getClassNameNormalized() {
		return classNameNormalized;
	}

	public void setClassNameNormalized(String classNameNormalized) {
		this.classNameNormalized = classNameNormalized;
	}

	public void setTypePath(String typePath) {
		this.typePath = typePath;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	private String httpMethod = HttpMethod.UNKNOWN.name();
	private List<EntryPathParam> listEntryPathData = new ArrayList<EntryPathParam>();

	public String getSignatureName() {
		return signatureName;
	}

	public void setSignatureName(String signatureName) {
		this.signatureName = signatureName;
	}

	public String getClassName() {
		return className;
	}

	/*public String getNormalizedClassName() {
		return className.replaceAll("\\.", "/");
	}*/

	public void setClassName(String className) {
		this.className = className;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getTypePath() {
		return typePath;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public List<EntryPathParam> getListEntryPathData() {
		return listEntryPathData;
	}

	public void setListEntryPathData(List<EntryPathParam> listEntryPathData) {
		this.listEntryPathData = listEntryPathData;
	}

	@Override
	public String toString() {
		return "EntryPathData [uri=" + uri + ", methodName=" + methodName
				+ ", className=" + className + ", classNameNormalized="
				+ classNameNormalized + ", signatureName=" + signatureName
				+ ", typePath=" + typePath + ", httpMethod=" + httpMethod
				+ ", listEntryPathData=" + listEntryPathData + "]";
	}

}
