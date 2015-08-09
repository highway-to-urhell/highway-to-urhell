package io.highway.to.urhell.service.impl;


import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.service.AbstractLeechService;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class RmiService extends AbstractLeechService {

    public static final String FRAMEWORK_NAME = "RMI";

    public RmiService() {
        super(FRAMEWORK_NAME);
    }

    @Override
    protected void gatherData(List<EntryPathData> incoming) {
        try {
            Registry reg = LocateRegistry.getRegistry();
            if(reg!=null){
                for (String name : reg.list()){
                    EntryPathData e = new EntryPathData();
                    e.setClassName(reg.lookup(name).getClass().getName());
                    e.setMethodName(name);
                    e.setUri(name);
                    e.setAudit(false);
                    addEntryPath(e);
                }
            }
        }catch (Exception e){
            LOGGER.info(" No locate registry "+e);
        }


    }
}
