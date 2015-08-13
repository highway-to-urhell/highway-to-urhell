package com.highway2urhell.service.impl;

import com.highway2urhell.VersionUtils;
import com.highway2urhell.service.AbstractLeechService;

public class JmsQueueService extends AbstractLeechService {

    public static final String FRAMEWORK_NAME = "JMS_11_CTX";

    public JmsQueueService() {
        super(FRAMEWORK_NAME, VersionUtils.getVersion(
                " javax.jms.Queue", "org.apache.geronimo.specs",
                "geronimo-jms_1.1_spec"));
    }

}
