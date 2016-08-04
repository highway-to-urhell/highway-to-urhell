package com.highway2urhell.service.impl;

import com.highway2urhell.VersionUtils;
import com.highway2urhell.domain.EntryPathData;
import com.highway2urhell.service.AbstractLeechService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scun on 19/04/16.
 */
public class SpringBootServletService extends AbstractLeechService {

    public static final String FRAMEWORK_NAME = "SPRING_BOOT_SERVLET";

    public SpringBootServletService() {
        super(
                FRAMEWORK_NAME,
                VersionUtils.getVersion(
                        "org.springframework.boot.context.embedded.ServletRegistrationBea",
                        "org.springframework.boot",
                        "spring-boot"));
    }

    @Override
    public void receiveData(List<EntryPathData> incoming) {
        List<EntryPathData> res = new ArrayList<EntryPathData>();
        System.out.println("receive incoming data ON SPRING_BOOT_SERVLET");
        for(EntryPathData e : incoming) {
            e.setAudit(new Boolean(false));
            res.add(e);
        }
        gatherData(res);
        System.out.println("data gathering complete. SPRING_BOOT_SERVLET Found  entries"+ getFrameworkInformations().getListEntryPath().size());
    }

}
