package io.highway.to.urhell.rest.provider;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
public class JacksonObjectMapperProvider implements ContextResolver<ObjectMapper> {

    public ObjectMapper getContext(Class<?> type) {
        final ObjectMapper result = new ObjectMapper();
        //result.registerModule(new Hibernate4Module());
        return result;
    }
}
