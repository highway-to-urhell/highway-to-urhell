package io.highway.to.urhell.service.impl;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import io.highway.to.urhell.VersionUtils;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.HttpMethod;
import io.highway.to.urhell.domain.TypePath;
import io.highway.to.urhell.service.AbstractLeechService;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.util.Set;

public class GwtService extends AbstractLeechService {

    public static final String FRAMEWORK_NAME = "GWT";

    public GwtService() {
        super(FRAMEWORK_NAME, VersionUtils.getVersion(
                "com.google.gwt.user.client.rpc.RemoteServiceRelativePath",
                "com.google.gwt", "GWT"));
        setTriggerAtStartup(true);
    }

    @Override
    protected void gatherData(Object incoming) {
        if (!getFrameworkInformations().getVersion().equals(
                VersionUtils.NO_FRAMEWORK)) {
            // scan
            LOGGER.info("Start Scan reflections GWT ! ");
            Reflections reflections = new Reflections(
                    new ConfigurationBuilder().setUrls(ClasspathHelper
                            .forClassLoader()));
            LOGGER.info("End Scan reflections GWT ! ");
            Set<Class<?>> setGwtService = reflections
                    .getTypesAnnotatedWith(RemoteServiceRelativePath.class);
            if (setGwtService != null && !setGwtService.isEmpty()) {
                // Grab all class extends
                for (Class<?> service : setGwtService) {
                    // search path
                    Annotation[] tabAnnotation = service.getAnnotations();
                    for (Annotation annotation : tabAnnotation) {
                        if (annotation instanceof RemoteServiceRelativePath) {
                            RemoteServiceRelativePath remoteAnnotation = (RemoteServiceRelativePath) annotation;
                            EntryPathData entry = new EntryPathData();
                            entry.setTypePath(TypePath.DYNAMIC);
                            entry.setHttpMethod(HttpMethod.POST);
                            entry.setMethodName("");
                            entry.setMethodEntry(service.getName());
                            entry.setUri(remoteAnnotation.value());
                            addEntryPath(entry);
                        }
                    }
                }
            }
        }

    }

}
