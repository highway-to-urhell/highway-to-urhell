package io.highway.to.urhell.domain;

import java.util.ArrayList;
import java.util.List;

public class EntryPathData {

    private String uri="";
    private TypePath typePath=TypePath.UNKNOWN;
    private String methodEntry="";
    private String className="";
    private String signatureName="";
    private List<EntryPathParam> listEntryPathData = new ArrayList<EntryPathParam>();
    private HttpMethod httpMethod= HttpMethod.UNKNOWN;
    
    

	public String getSignatureName() {
		return signatureName;
	}

	public void setSignatureName(String signatureName) {
		this.signatureName = signatureName;
	}

	public String getClassName() {
		return className;
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

  

    public String getMethodEntry() {
		return methodEntry;
	}

	public void setMethodEntry(String methodEntry) {
		this.methodEntry = methodEntry;
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
                + ", methodEntry=" + methodEntry + ", className=" + className
                + ", listEntryPathData=" + listEntryPathData + ", httpMethod="
                + httpMethod + "]";
    }

    

}
