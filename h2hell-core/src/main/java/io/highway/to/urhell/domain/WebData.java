package io.highway.to.urhell.domain;

public class WebData {
	
	
	private String name;
	private String className;
	private String path;
	private String type;
	
	public String getType() {
		return type;
	}
	@Override
	public String toString() {
		return "WebData [name=" + name + ", className=" + className + ", path="
				+ path + ", type=" + type + "]";
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	

}
