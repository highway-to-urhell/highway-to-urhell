package com.highway2urhell.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.highway2urhell.CoreEngine;
import com.highway2urhell.domain.H2hConfig;
import com.highway2urhell.domain.ThunderData;

public class GatherService {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private static GatherService instance;
    private Map<String, ThunderData> mapThunderData;

    public static GatherService getInstance() {
        if (instance == null) {
            synchronized (GatherService.class) {
                if (instance == null) {
                    instance = new GatherService();
                }
            }
        }
        return instance;
    }

    private GatherService() {
        mapThunderData = new HashMap<String, ThunderData>();
    }

    public void gatherPerformance(String fullMethodName, long timeExec, String listParams) {
        H2hConfig hc = CoreEngine.getInstance().getConfig();
        switch (hc.getTimer()) {
            case MEMORY:
                LOGGER.info(" fullMethodName " + fullMethodName + " timeExec " + timeExec);
                break;
            case REMOTE:
                long timerConfig = CoreEngine.getInstance().getConfig().getHigherTime().longValue();
                if (timeExec > timerConfig) {
                    int tExec = (int) timeExec;
                    ThunderExporterService.getInstance().sendRemotePerformance(fullMethodName, tExec, listParams);
                }
                break;
            default:
                LOGGER.info("No config for Timer");
        }
    }

    public void gatherInvocation(String fullMethodName, String listParams) {
        ThunderExporterService.getInstance().sendRemoteBreaker(fullMethodName, listParams);
    }
}
