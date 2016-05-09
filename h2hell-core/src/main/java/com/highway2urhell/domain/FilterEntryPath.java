package com.highway2urhell.domain;


import java.util.List;

public class FilterEntryPath {

    private List<String> listFilter;
    private String token;
    private Boolean filter = false;
    private Boolean packageOnly = false;
    private Boolean classOnly = false;
    private Boolean classMethod = false;

    public List<String> getListFilter() {
        return listFilter;
    }

    public Boolean getClassOnly() {
        return classOnly;
    }

    public void setClassOnly(Boolean classOnly) {
        this.classOnly = classOnly;
    }

    public void setListFilter(List<String> listFilter) {
        this.listFilter = listFilter;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getFilter() {
        return filter;
    }

    public void setFilter(Boolean filter) {
        this.filter = filter;
    }

    public Boolean getPackageOnly() {
        return packageOnly;
    }

    public void setPackageOnly(Boolean packageOnly) {
        this.packageOnly = packageOnly;
    }

    public Boolean getClassMethod() {
        return classMethod;
    }

    @Override
    public String toString() {
        return "FilterEntryPath{" +
                "listFilter=" + listFilter +
                ", token='" + token + '\'' +
                ", filter=" + filter +
                ", packageOnly=" + packageOnly +
                ", classOnly=" + classOnly +
                ", classMethod=" + classMethod +
                '}';
    }

    public void setClassMethod(Boolean classMethod) {
        this.classMethod = classMethod;
    }
}
