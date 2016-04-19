package com.highway2urhell.service.impl;

import com.highway2urhell.VersionUtils;
import com.highway2urhell.domain.EntryPathData;
import com.highway2urhell.service.AbstractLeechService;

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
        // clearPreviousData();
        LOGGER.debug("receive incoming data ON SPRING_BOOT_SERVLET");
        gatherData(incoming);
        LOGGER.debug("data gathering complete. Found {} entries", getFrameworkInformations().getListEntryPath().size());
    }

}
