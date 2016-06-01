package com.highway2urhell.web.rest.dto.v1api;

public class MessageMetrics {
    private String pathClassMethodName;
    private String token;
    private String dateIncoming;
    private int timeExec;
    private Double cpuLoadSystem;
    private Double cpuLoadProcess;
    private String parameters;

    public MessageMetrics() {
    }

    public String getParameters() {
        return this.parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public Double getCpuLoadSystem() {
        return this.cpuLoadSystem;
    }

    public void setCpuLoadSystem(Double cpuLoadSystem) {
        this.cpuLoadSystem = cpuLoadSystem;
    }

    public Double getCpuLoadProcess() {
        return this.cpuLoadProcess;
    }

    public void setCpuLoadProcess(Double cpuLoadProcess) {
        this.cpuLoadProcess = cpuLoadProcess;
    }

    public String getPathClassMethodName() {
        return this.pathClassMethodName;
    }

    public void setPathClassMethodName(String pathClassMethodName) {
        this.pathClassMethodName = pathClassMethodName;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDateIncoming() {
        return this.dateIncoming;
    }

    public void setDateIncoming(String dateIncoming) {
        this.dateIncoming = dateIncoming;
    }

    public int getTimeExec() {
        return this.timeExec;
    }

    public void setTimeExec(int timeExec) {
        this.timeExec = timeExec;
    }
}
