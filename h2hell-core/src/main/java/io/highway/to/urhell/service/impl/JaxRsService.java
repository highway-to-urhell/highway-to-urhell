package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.VersionUtils;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.HttpMethod;
import io.highway.to.urhell.domain.TypePath;
import io.highway.to.urhell.service.AbstractLeechService;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import javax.ws.rs.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

public class JaxRsService extends AbstractLeechService {

    public static final String FRAMEWORK_NAME = "JAX_RS";

    public JaxRsService() {
        super(FRAMEWORK_NAME, VersionUtils.getVersion(
                "javax.ws.rs.core.Application", "javax.ws.rs",
                "javax.ws.rs-api"));
    }

    @Override
    protected void gatherData(Object incoming) {
        if (!getFrameworkInformations().getVersion().equals(
                VersionUtils.NO_FRAMEWORK)) {
            // scan
            LOGGER.info("Start Scan reflections JAX-RS ! ");
            Reflections reflections = new Reflections(
                    new ConfigurationBuilder().setUrls(ClasspathHelper
                            .forClassLoader()));
            LOGGER.info("End Scan reflections JAX-RS ! ");
            Set<Class<?>> setPathJAXRS = reflections
                    .getTypesAnnotatedWith(Path.class);
            if (setPathJAXRS != null && !setPathJAXRS.isEmpty()) {
                // Grab all class extends
                for (Class<?> service : setPathJAXRS) {
                    // search path
                    Annotation[] tabAnnotation = service.getAnnotations();
                    for (Annotation annotation : tabAnnotation) {
                        if (annotation instanceof Path) {
                            Path remoteAnnotation = (Path) annotation;
                            String pathForClass = remoteAnnotation.value();
                            // Search all method public with annotation Path
                            Method[] tabMethod = service.getDeclaredMethods();
                            if (tabMethod.length > 0) {
                                for (Method m : tabMethod) {
                                    Path pMethod = m.getAnnotation(Path.class);
                                    if (pMethod != null) {
                                        addEntryPath(getEntryForClassJAXRS(
                                                pathForClass, m,
                                                service.getName(), pMethod));
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    private EntryPathData getEntryForClassJAXRS(String pathClass, Method m,
                                                String nameClass, Path pMethod) {
        EntryPathData entry = new EntryPathData();
        // method
        if (m.getAnnotation(GET.class) != null) {
            entry.setHttpMethod(HttpMethod.GET);
        } else if (m.getAnnotation(POST.class) != null) {
            entry.setHttpMethod(HttpMethod.POST);
        } else if (m.getAnnotation(PUT.class) != null) {
            entry.setHttpMethod(HttpMethod.PUT);
        } else if (m.getAnnotation(DELETE.class) != null) {
            entry.setHttpMethod(HttpMethod.DELETE);
        } else {
            entry.setHttpMethod(HttpMethod.UNKNOWN);
        }
        //
        entry.setUri(pathClass + pMethod.value());
        entry.setMethodEntry(nameClass);
        entry.setMethodName(m.getName());
        entry.setTypePath(TypePath.DYNAMIC);
        return entry;
    }

}
