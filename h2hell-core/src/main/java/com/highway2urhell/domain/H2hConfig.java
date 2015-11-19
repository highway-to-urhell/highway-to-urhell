package com.highway2urhell.domain;

public class H2hConfig {
	private OutputSystem outputSystem;
	private String urlApplication;
	private String nameApplication;
	private String urlH2hWeb;
	private String description;
	private String pathSource;
	private String pathH2h;
	private String versionApp;
	private String typeAppz;
	private OutputSystem timer;

	public Boolean getPerformance() {
		return performance;
	}

	public void setPerformance(Boolean performance) {
		this.performance = performance;
	}

	private Integer higherTime;
	private Boolean performance;

	public Integer getHigherTime() {
		return higherTime;
	}

	public void setHigherTime(Integer higherTime) {
		this.higherTime = higherTime;
	}

	public Boolean getPathSend() {
		return pathSend;
	}

	public void setPathSend(Boolean pathSend) {
		this.pathSend = pathSend;
	}

	private String token;
	private Boolean pathSend = false;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public OutputSystem getTimer() {
		return timer;
	}
	public void setTimer(OutputSystem timer) {
		this.timer = timer;
	}
	public String getTypeAppz() {
		return typeAppz;
	}
	public void setTypeAppz(String typeAppz) {
		this.typeAppz = typeAppz;
	}
	public String getVersionApp() {
		return versionApp;
	}
	public void setVersionApp(String versionApp) {
		this.versionApp = versionApp;
	}
	public String getPathSource() {
		return pathSource;
	}
	public void setPathSource(String pathSource) {
		this.pathSource = pathSource;
	}
	public String getPathH2h() {
		return pathH2h;
	}
	public void setPathH2h(String pathH2h) {
		this.pathH2h = pathH2h;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
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
		return "H2hConfig [outputSystem=" + outputSystem + ", urlApplication="
				+ urlApplication + ", nameApplication=" + nameApplication
				+ ", urlH2hWeb=" + urlH2hWeb + "]";
	}
	
	

}
