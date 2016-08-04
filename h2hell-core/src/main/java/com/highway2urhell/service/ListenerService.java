package com.highway2urhell.service;

import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.highway2urhell.CoreEngine;
import com.highway2urhell.domain.FilterEntryPath;
import com.highway2urhell.domain.MessageEvent;

public class ListenerService {


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
            System.out.println("Call init Token");
            ThunderExporterService.getInstance().registerAppInThunder();
        }else{
            MessageEvent me = new MessageEvent();
            me.setToken(CoreEngine.getInstance().getConfig().getToken());
            me.setReference(CoreEngine.getInstance().getConfig().getReference());
            String result = ThunderExporterService.getInstance().sendDataHTTPOldSchool("/event",me);
            System.out.println("Result on /event "+result);
            Gson gson = new Gson();
            try {
                Type typeList = new TypeToken<ArrayList<MessageEvent>>() {}.getType();
                List<MessageEvent> listEvent = gson.fromJson(result, typeList);
                for(MessageEvent meResult : listEvent) {
                    System.out.println("MessageEvent "+meResult.toString());
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
                }

            }catch (JsonParseException e){
                System.err.println("Error on /event with token "+CoreEngine.getInstance().getConfig().getToken()+" and reference "+CoreEngine.getInstance().getConfig().getReference());
            }
        }
    }

    private void enableEntryPoint(MessageEvent meResult){
        try {
            FilterEntryPath filter = createFilter(meResult.getData());
            CoreEngine.getInstance().enableEntryPointCoverage(filter);
        } catch (ClassNotFoundException e) {
            System.err.println("Error while launchTransformer "+ e);
        } catch (UnmodifiableClassException e) {
            System.err.println("Error while launchTransformer "+ e);
        }
    }

    private FilterEntryPath createFilter(Object data) {
        Gson gson = new Gson();
        FilterEntryPath res = null;
        try {
            res = gson.fromJson((String) data, FilterEntryPath.class);
        } catch (Exception e) {
            System.err.println(" Not a JSON format for FilterEntryPath " + data.toString());
            res = new FilterEntryPath();
        }
        return res;
    }
}
