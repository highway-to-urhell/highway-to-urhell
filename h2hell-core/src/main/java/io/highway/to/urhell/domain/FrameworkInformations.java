package io.highway.to.urhell.domain;

import java.util.List;

public class FrameworkInformations {

    private FrameworkEnum frameworkEnum;
    private String version;
    private String details;
    private List<EntryPathData> listEntryPath;

    public FrameworkEnum getFrameworkEnum() {
        return frameworkEnum;
    }

    public void setFrameworkEnum(FrameworkEnum frameworkEnum) {
        this.frameworkEnum = frameworkEnum;
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

	@Override
	public String toString() {
		return "FrameworkInformations [frameworkEnum=" + frameworkEnum
				+ ", version=" + version + ", details=" + details
				+ ", listEntryPath=" + listEntryPath + "]";
	}

}
