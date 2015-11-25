package com.highway2urhell.service;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.highway2urhell.CoreEngine;
import com.highway2urhell.domain.FilterEntryPath;
import com.highway2urhell.domain.MessageEvent;
import com.highway2urhell.domain.TypeMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.UnmodifiableClassException;

public class ListenerService {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private static ListenerService instance;

    public static ListenerService getInstance() {
        if (instance == null) {
            synchronized (ListenerService.class) {
                if (instance == null) {
                    instance = new ListenerService();
                }
            }
        }
        return instance;
    }

    private ListenerService() {

    }

    /**
     * Step 1 : create the application
     * Next Step : get the event H2H
     */
    public void callServerH2H() {
        if(CoreEngine.getInstance().getConfig().getToken() == null){
            LOGGER.debug("Call init Token");
            ThunderExporterService.getInstance().registerAppInThunder();
        }else{
            MessageEvent me = new MessageEvent();
            me.setToken(CoreEngine.getInstance().getConfig().getToken());
            me.setReference(CoreEngine.getInstance().getConfig().getReference());
            String result = ThunderExporterService.getInstance().sendDataHTTP("/event",me);
            LOGGER.debug("Result on /event ",result);
            Gson gson = new Gson();
            try {
                MessageEvent meResult = gson.fromJson(result, MessageEvent.class);
                switch (meResult.getTypeMessageEvent()) {
                    case INIT_PATH:
                        CoreEngine.getInstance().initPathsRemote();
                        break;
                    case ENABLE_ENTRY_POINT:
                        enableEntryPoint(meResult);
                        break;
                    default:
                        throw new IllegalStateException(meResult.getTypeMessageEvent() + " is not supported");
                }

            }catch (JsonParseException e){
                LOGGER.error("Error on /event with token %s and reference %s",CoreEngine.getInstance().getConfig().getToken(),CoreEngine.getInstance().getConfig().getReference());
            }
        }
    }

    private void enableEntryPoint(MessageEvent meResult){
        try {
            FilterEntryPath filter = createFilter(meResult.getData());
            CoreEngine.getInstance().enableEntryPointCoverage(filter);
        } catch (ClassNotFoundException e) {
            LOGGER.error("Error while launchTransformer ", e);
        } catch (UnmodifiableClassException e) {
            LOGGER.error("Error while launchTransformer ", e);
        }
    }

    private FilterEntryPath createFilter(Object data) {
        Gson gson = new Gson();
        FilterEntryPath res = null;
        try {
            res = gson.fromJson((String) data, FilterEntryPath.class);
        } catch (Exception e) {
            LOGGER.error(" Not a JSON format for FilterEntryPath " + data.toString());
            res = new FilterEntryPath();
        }
        return res;
    }
}
