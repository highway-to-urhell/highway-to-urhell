package com.highway2urhell.web.rest.dto.v1api;

/**
 * Created by duff on 23/05/2016.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
//
public class H2hConfigDTO {
    private String urlApplication;
    private String nameApplication;
    private String urlH2hWeb;
    private String description;
    private String pathSource;
    private String pathH2h;
    private String versionApp;
    private String typeAppz;
    private OutputSystemDTO timer;
    private String reference;
    private Integer higherTime;
    private Boolean performance;
    private String token;
    private Boolean pathSend = Boolean.valueOf(false);

    public H2hConfigDTO() {
    }

    public String getReference() {
        return this.reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Boolean getPerformance() {
        return this.performance;
    }

    public void setPerformance(Boolean performance) {
        this.performance = performance;
    }

    public Integer getHigherTime() {
        return this.higherTime;
    }

    public void setHigherTime(Integer higherTime) {
        this.higherTime = higherTime;
    }

    public Boolean getPathSend() {
        return this.pathSend;
    }

    public void setPathSend(Boolean pathSend) {
        this.pathSend = pathSend;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public OutputSystemDTO getTimer() {
        return this.timer;
    }

    public void setTimer(OutputSystemDTO timer) {
        this.timer = timer;
    }

    public String getTypeAppz() {
        return this.typeAppz;
    }

    public void setTypeAppz(String typeAppz) {
        this.typeAppz = typeAppz;
    }

    public String getVersionApp() {
        return this.versionApp;
    }

    public void setVersionApp(String versionApp) {
        this.versionApp = versionApp;
    }

    public String getPathSource() {
        return this.pathSource;
    }

    public void setPathSource(String pathSource) {
        this.pathSource = pathSource;
    }

    public String getPathH2h() {
        return this.pathH2h;
    }

    public void setPathH2h(String pathH2h) {
        this.pathH2h = pathH2h;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlApplication() {
        return this.urlApplication;
    }

    public void setUrlApplication(String urlApplication) {
        this.urlApplication = urlApplication;
    }

    public String getNameApplication() {
        return this.nameApplication;
    }

    public void setNameApplication(String nameApplication) {
        this.nameApplication = nameApplication;
    }

    public String getUrlH2hWeb() {
        return this.urlH2hWeb;
    }

    public void setUrlH2hWeb(String urlH2hWeb) {
        this.urlH2hWeb = urlH2hWeb;
    }

    public String toString() {
        return "H2hConfig{urlApplication=\'" + this.urlApplication + '\'' + ", nameApplication=\'" + this.nameApplication + '\'' + ", urlH2hWeb=\'" + this.urlH2hWeb + '\'' + ", description=\'" + this.description + '\'' + ", pathSource=\'" + this.pathSource + '\'' + ", pathH2h=\'" + this.pathH2h + '\'' + ", versionApp=\'" + this.versionApp + '\'' + ", typeAppz=\'" + this.typeAppz + '\'' + ", timer=" + this.timer + ", reference=\'" + this.reference + '\'' + ", higherTime=" + this.higherTime + ", performance=" + this.performance + ", token=\'" + this.token + '\'' + ", pathSend=" + this.pathSend + '}';
    }
}
