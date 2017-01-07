package com.highway2urhell.service.impl;


import com.highway2urhell.VersionUtils;
import com.highway2urhell.service.AbstractLeechService;

public class ActiveMQConnectionFactoryService extends AbstractLeechService {

    public static final String FRAMEWORK_NAME = "ACTIVEMQ_CONNECTION_FACTORY";

    public ActiveMQConnectionFactoryService() {
        super(FRAMEWORK_NAME, VersionUtils.getVersion(
                "org.apache.activemq.ActiveMQConnectionFactory", "org.apache.activemq", "activemq-jms-pool"));
    }
}
