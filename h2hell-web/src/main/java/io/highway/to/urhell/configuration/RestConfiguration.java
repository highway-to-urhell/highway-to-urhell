package io.highway.to.urhell.configuration;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class RestConfiguration extends ResourceConfig {

    public RestConfiguration() {
        packages("io.highway.to.urhell.rest;com.wordnik.swagger.jersey.listing");
        register(Jackson2Feature.class);
        register(MultiPartFeature.class);
        // jersey uses JUL as logging framework, so we must configure SLF4J
        // bridge
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }
}
