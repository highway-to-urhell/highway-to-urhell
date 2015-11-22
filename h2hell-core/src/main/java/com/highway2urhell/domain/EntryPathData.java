package com.highway2urhell.domain;

import java.util.ArrayList;
import java.util.List;

public class EntryPathData {

    private String uri;
    private String methodName;
    private String className;
    private String classNameNormalized;
    private String signatureName;
    private TypePath typePath = TypePath.UNKNOWN;
    private Boolean audit = true;
    private HttpMethod httpMethod = HttpMethod.POST;
    private List<EntryPathParam> listEntryPathData = new ArrayList<EntryPathParam>();

    public Boolean getAudit() {
        return audit;
    }

    public void setAudit(Boolean audit) {
        this.audit = audit;
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


    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }


    public String getClassName() {
        return className;
    }


    public void setClassName(String className) {
        this.className = className;
    }


    public String getClassNameNormalized() {
        return classNameNormalized;
    }


    public void setClassNameNormalized(String classNameNormalized) {
        this.classNameNormalized = classNameNormalized;
    }


    public String getSignatureName() {
        return signatureName;
    }


    public void setSignatureName(String signatureName) {
        this.signatureName = signatureName;
    }


    public TypePath getTypePath() {
        return typePath;
    }


    public void setTypePath(TypePath typePath) {
        this.typePath = typePath;
    }


    public HttpMethod getHttpMethod() {
        return httpMethod;
    }


    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
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
