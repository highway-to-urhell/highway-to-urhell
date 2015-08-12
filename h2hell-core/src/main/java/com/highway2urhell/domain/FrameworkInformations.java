package com.highway2urhell.domain;

import java.util.ArrayList;
import java.util.List;

public class FrameworkInformations {

    private String frameworkName;
    private String version = "UNKNOWN";
    private String details;
    private List<EntryPathData> listEntryPath = new ArrayList<EntryPathData>();

    public String getFrameworkName() {
        return frameworkName;
    }

    public void setFrameworkName(String frameworkName) {
        this.frameworkName = frameworkName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<EntryPathData> getListEntryPath() {
        return listEntryPath;
    }

    public void setListEntryPath(List<EntryPathData> listEntryPath) {
        this.listEntryPath = listEntryPath;
    }

    public boolean hasEntryPaths() {
        return !listEntryPath.isEmpty();
    }

    @Override
    public String toString() {
        return "FrameworkInformations [frameworkName=" + frameworkName
                + ", version=" + version + ", details=" + details
                + ", listEntryPath=" + listEntryPath + "]";
    }

}
