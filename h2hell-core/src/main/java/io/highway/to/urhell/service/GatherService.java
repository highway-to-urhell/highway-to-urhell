package io.highway.to.urhell.service;

import io.highway.to.urhell.CoreEngine;
import io.highway.to.urhell.domain.H2hConfig;
import io.highway.to.urhell.domain.ThunderData;
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

    public void gather(String fullMethodName) {
        for (ThunderData th : mapThunderData.values()) {
            LOGGER.info("th :" + th.toString());
        }
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
                ThunderService.getInstance().sendRemoteBreaker(fullMethodName);
                break;
            default:
                throw new IllegalStateException(hc.getOutputSystem()
                        + " is not supported");
        }
    }

}
