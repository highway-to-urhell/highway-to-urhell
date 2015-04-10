package io.highway.to.urhell.domain;

import java.util.ArrayList;
import java.util.List;

public class EntryPathData {

    private String uri;
    private String methodName;
    private String className;
    private String signatureName;
    private TypePath typePath=TypePath.UNKNOWN;
    private HttpMethod httpMethod= HttpMethod.UNKNOWN;
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

    public String getNormalizedClassName() {
        return className.replaceAll("\\.","/");
    }

	public void setClassName(String className) {
		this.className = className;
	}

	public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public TypePath getTypePath() {
        return typePath;
    }

    public void setTypePath(TypePath typePath) {
        this.typePath = typePath;
    }

    public String getMethodName() {
        return methodName;
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

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    @Override
    public String toString() {
        return "EntryPathData [uri=" + uri + ", typePath=" + typePath
                + ", methodName=" + methodName + ", className=" + className
                + ", listEntryPathData=" + listEntryPathData + ", httpMethod="
                + httpMethod + "]";
    }

    

}
