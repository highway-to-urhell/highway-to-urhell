package com.highway2urhell.domain;

import java.util.List;

public class MessageMetrics {
	private String pathClassMethodName;
	private String token;
	private String dateIncoming;
	private int timeExec;
	private Double cpuLoadSystem;
	private Double cpuLoadProcess;
	private String parameters;

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

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
	public int getTimeExec() {
		return timeExec;
	}
	public void setTimeExec(int timeExec) {
		this.timeExec = timeExec;
	}

}
