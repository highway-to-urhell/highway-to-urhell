package com.highway2urhell.domain;


public class LeechParamMethodData {

    private String nameParameter;
    private String nameClass;
    private Object data;

    public String getNameClass() {
        return nameClass;
    }

    public void setNameClass(String nameClass) {
        this.nameClass = nameClass;
    }

    public Object getData() {
        return data;
    }

    public String getNameParameter() {
        return nameParameter;
    }

    public void setNameParameter(String nameParameter) {
        this.nameParameter = nameParameter;
    }

    public void setData(Object data) {
        this.data = data;

    }
}
