package com.highway2urhell.service.impl;

import com.highway2urhell.VersionUtils;
import com.highway2urhell.domain.EntryPathData;
import com.highway2urhell.service.AbstractLeechService;

import java.util.List;

public class SpringAMQPRabbitService extends AbstractLeechService {

    public static final String FRAMEWORK_NAME = "SPRING_AMQP";

    public SpringAMQPRabbitService() {
        super(
                FRAMEWORK_NAME,
                VersionUtils.getVersion(
                        "org.springframework.amqp.core.Message",
                        "org.springframework",
                        "spring-amqp"));
    }

    @Override
    public void receiveData(List<EntryPathData> incoming) {
        // clearPreviousData();
        System.out.println("receive incoming data");
        gatherData(incoming);
        System.out.println("data gathering complete. Found entries"+
                getFrameworkInformations().getListEntryPath().size());
    }
}
