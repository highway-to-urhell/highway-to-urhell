package com.highway2urhell.service.impl;

import com.highway2urhell.VersionUtils;
import com.highway2urhell.domain.EntryPathData;
import com.highway2urhell.service.AbstractLeechService;

import java.util.ArrayList;
import java.util.List;

public class SpringServiceMethodService extends AbstractLeechService {

    public static final String FRAMEWORK_NAME = "SPRING_METHOD";

    public SpringServiceMethodService() {
        super(
                FRAMEWORK_NAME,
                VersionUtils.getVersion(
                        "org.springframework.web.servlet.mvc.method.RequestMappingInfo",
                        "org.springframework",
                        "spring-webmvc"));
    }

    @Override
    public void receiveData(List<EntryPathData> incoming) {
        List<EntryPathData> res = new ArrayList<EntryPathData>();
        System.out.println("receive incoming data ON SPRING_METHOD");
        for(EntryPathData e : incoming) {
            if(e.getUri().startsWith("[")){
                e.setUri(e.getUri().replace("[",""));
                e.setUri(e.getUri().replace("]",""));
            }
            res.add(e);
        }
        gatherData(res);
        System.out.println("data gathering complete.SPRING_METHOD Found entries"+ getFrameworkInformations().getListEntryPath().size());
    }
}
