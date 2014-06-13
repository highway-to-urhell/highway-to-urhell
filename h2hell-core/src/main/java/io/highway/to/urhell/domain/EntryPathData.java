package io.highway.to.urhell.domain;

import java.util.List;

public class EntryPathData {

    private String uri;
    private TypePath typePath;
    private String methodEntry;
    private List<EntryPathParam> listEntryPathData;
    private String methodName;

    public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
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

    @Override
    public String toString() {
        return "EntryPathData [uri=" + uri + ", typePath=" + typePath + ", methodEntry=" + methodEntry
                + ", listEntryPathData=" + listEntryPathData + "]";
    }

}
