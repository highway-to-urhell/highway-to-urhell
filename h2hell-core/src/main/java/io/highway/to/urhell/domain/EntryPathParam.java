package io.highway.to.urhell.domain;

public class EntryPathParam {

    private String key;
    private String value;
    private String typeParam;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public String getTypeParam() {
		return typeParam;
	}

	public void setTypeParam(String typeParam) {
		this.typeParam = typeParam;
	}

	@Override
    public String toString() {
        return "EntryPathParam [key=" + key + ", value=" + value + ", typeParam=" + typeParam + "]";
    }
}
