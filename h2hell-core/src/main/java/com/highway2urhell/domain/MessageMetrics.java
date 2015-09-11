package com.highway2urhell.domain;

public class MessageMetrics {
	private String pathClassMethodName;
	private String token;
	private String dateIncoming;
	private long timeExec;
	private Double cpuLoadSystem;
	private Double cpuLoadProcess;

	public Double getCpuLoadSystem() {
		return cpuLoadSystem;
	}

	public void setCpuLoadSystem(Double cpuLoadSystem) {
		this.cpuLoadSystem = cpuLoadSystem;
	}

	public Double getCpuLoadProcess() {
		return cpuLoadProcess;
	}

	public void setCpuLoadProcess(Double cpuLoadProcess) {
		this.cpuLoadProcess = cpuLoadProcess;
	}
	public String getPathClassMethodName() {
		return pathClassMethodName;
	}
	public void setPathClassMethodName(String pathClassMethodName) {
		this.pathClassMethodName = pathClassMethodName;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getDateIncoming() {
		return dateIncoming;
	}
	public void setDateIncoming(String dateIncoming) {
		this.dateIncoming = dateIncoming;
	}
	public long getTimeExec() {
		return timeExec;
	}
	public void setTimeExec(long timeExec) {
		this.timeExec = timeExec;
	}

}
