package com.highway2urhell.service.impl;

import com.highway2urhell.VersionUtils;
import com.highway2urhell.domain.EntryPathData;
import com.highway2urhell.domain.TypePath;
import com.highway2urhell.service.AbstractLeechService;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import javax.jms.MessageListener;
import java.util.List;
import java.util.Set;

public class JmsAsyncMessageListener extends AbstractLeechService {

    public static final String FRAMEWORK_NAME = "JMS_MESSAGE_LISTENER";

    public JmsAsyncMessageListener() {
        super(FRAMEWORK_NAME, VersionUtils.getVersion(
                " javax.jms.MessageListener", "org.apache.geronimo.specs",
                "geronimo-jms_1.1_spec"));
        setTriggerAtStartup(true);
    }

    @Override
    protected void gatherData(List<EntryPathData> incoming) {
        if (!getFrameworkInformations().getVersion().equals(
                VersionUtils.NO_FRAMEWORK)) {
            // scan
            LOGGER.info("Start Scan reflections JMS ! ");
            Reflections reflections = new Reflections(
                    new ConfigurationBuilder().setUrls(ClasspathHelper
                            .forClassLoader()));
            LOGGER.info("End Scan reflections JMS ! ");
            Set<Class<? extends MessageListener>> setPathMessageListener = reflections.getSubTypesOf(MessageListener.class);
            if (setPathMessageListener != null && !setPathMessageListener.isEmpty()) {
                // Grab all class extends
                for (Class<?> service : setPathMessageListener) {
                    // search annotation type javax.ws.rs.Path
                    EntryPathData ep = new EntryPathData();
                    ep.setClassName(service.getName());
                    ep.setMethodName("onMessage");
                    //see {JmsAsyncMessageListener}
                    ep.setUri("see-queue-declaration");
                    ep.setTypePath(TypePath.DYNAMIC);
                }
            }

        }

    }
}
