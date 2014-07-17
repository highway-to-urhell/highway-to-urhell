package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.VersionUtils;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.FrameworkEnum;
import io.highway.to.urhell.domain.HttpMethod;
import io.highway.to.urhell.domain.TypePath;
import io.highway.to.urhell.service.AbstractLeechService;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

public class GwtService extends AbstractLeechService {

	public GwtService() {
		super(FrameworkEnum.GWT, VersionUtils.getVersion(
				"com.google.gwt.user.client.rpc.RemoteServiceRelativePath",
				"com.google.gwt", "GWT"));
	}

	@Override
	protected void gatherData(Object incoming) {
		// scan
		LOGGER.info("Scan GWT ! ");
		Reflections reflections = new Reflections(
				new ConfigurationBuilder().setUrls(ClasspathHelper
						.forClassLoader()));
		LOGGER.info("End reflections GWT ! ");
		if (!getFrameworkInformations().getVersion().equals(
				VersionUtils.NO_FRAMEWORK)) {
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
							LOGGER.error(entry.toString());
							addEntryPath(entry);
						}
					}
				}
			}
		}

	}
	
}
