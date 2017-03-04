package com.highway2urhell.web.rest.vm;

import com.highway2urhell.domain.EntryPoint;

import java.util.List;

/**
 * View Model object for storing a Home stats on applications and entrypoints.
 */
public class VizualisationPathVM {

    private String appName;
    private List<EntryPoint> entrypoints;

    private  String messageConfig;
    private int totalStat;
    private int totalNoTest;
    private int totalFalsePositive;
    private boolean analysed;


    public String getMessageConfig() {
        return messageConfig;
    }

    public void setMessageConfig(String messageConfig) {
        this.messageConfig = messageConfig;
    }

    public int getTotalStat() {
        return totalStat;
    }

    public void setTotalStat(int totalStat) {
        this.totalStat = totalStat;
    }

    public int getTotalNoTest() {
        return totalNoTest;
    }

    public void setTotalNoTest(int totalNoTest) {
        this.totalNoTest = totalNoTest;
    }

    public int getTotalFalsePositive() {
        return totalFalsePositive;
    }

    public void setTotalFalsePositive(int totalFalsePositive) {
        this.totalFalsePositive = totalFalsePositive;
    }

    public List<EntryPoint> getEntrypoints() {
        return entrypoints;
    }

    public void setEntrypoints(List<EntryPoint> entrypoints) {
        this.entrypoints = entrypoints;
    }

    @Override
    public String toString() {
        return "HomeVM{" +
            "entrypoints=" + entrypoints +
            "totalStat=" + totalStat +
            "totalNoTest=" + totalNoTest +
            "totalFalsePositive=" + totalFalsePositive +
            "messageConfig='" + entrypoints + "'" +
            '}';
    }

    public void setAnalysed(Boolean analysed) {
        this.analysed = analysed;
    }

    public String getAppName() {
        return appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }


}
