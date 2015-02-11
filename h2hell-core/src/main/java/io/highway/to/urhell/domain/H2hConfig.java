package io.highway.to.urhell.domain;

public class H2hConfig {
	private OutputSystem outputSystem;
	private String urlApplication;
	private String nameApplication;
	private String urlH2hWeb;
	
	public String getUrlApplication() {
		return urlApplication;
	}
	public OutputSystem getOutputSystem() {
		return outputSystem;
	}
	public void setOutputSystem(OutputSystem outputSystem) {
		this.outputSystem = outputSystem;
	}
	public void setUrlApplication(String urlApplication) {
		this.urlApplication = urlApplication;
	}
	public String getNameApplication() {
		return nameApplication;
	}
	public void setNameApplication(String nameApplication) {
		this.nameApplication = nameApplication;
	}
	public String getUrlH2hWeb() {
		return urlH2hWeb;
	}
	public void setUrlH2hWeb(String urlH2hWeb) {
		this.urlH2hWeb = urlH2hWeb;
	}
	@Override
	public String toString() {
		return "H2hConfig [urlApplication=" + urlApplication
				+ ", nameApplication=" + nameApplication + ", urlH2hWeb="
				+ urlH2hWeb + "]";
	}
	
	

}
