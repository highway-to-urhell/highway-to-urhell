package com.highway2urhell.service;

import com.highway2urhell.CoreEngine;
import com.highway2urhell.domain.H2hConfig;
import com.highway2urhell.domain.ThunderData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

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
    
    public void gatherPerformance(String fullMethodName,long timeExec){
    	 H2hConfig hc = CoreEngine.getInstance().getConfig();
         switch (hc.getTimer()) {
             case MEMORY:
            	 LOGGER.info(" fullMethodName "+fullMethodName+" timeExec "+timeExec);
             	 break;
             case REMOTE:
                 ThunderExporterService.getInstance().sendRemotePerformance(fullMethodName,timeExec);
                 break;
             default:
                 LOGGER.info("No config for Timer");
         }
    }

    public void gatherInvocation(String fullMethodName) {
        H2hConfig hc = CoreEngine.getInstance().getConfig();
        switch (hc.getOutputSystem()) {
            case MEMORY:
                ThunderData td = mapThunderData.get(fullMethodName);
                if (td == null) {
                    // first time
                    td = new ThunderData(fullMethodName);
                    mapThunderData.put(fullMethodName, td);
                }
                td.incrementCounter();
                break;
            case REMOTE:
                ThunderExporterService.getInstance().sendRemoteBreaker(fullMethodName);
                break;
            default:
                throw new IllegalStateException(hc.getOutputSystem()
                        + " is not supported");
        }
    }

}
