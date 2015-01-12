package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.VersionUtils;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.EntryPathParam;
import io.highway.to.urhell.domain.HttpMethod;
import io.highway.to.urhell.domain.TypeParam;
import io.highway.to.urhell.domain.TypePath;
import io.highway.to.urhell.service.AbstractLeechService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

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
			// Search class with annotation RemoteServiceRelativePath
			Set<Class<?>> setGwtService = reflections
					.getTypesAnnotatedWith(RemoteServiceRelativePath.class);
			if (setGwtService != null && !setGwtService.isEmpty()) {
				for (Class<?> service : setGwtService) {
					RemoteServiceRelativePath remoteAnnotation = service
							.getAnnotation(RemoteServiceRelativePath.class);
					searchImplementationServerGwt(reflections,service,remoteAnnotation);
		
				}
			}
		}
	}
	
	private void searchImplementationServerGwt(Reflections reflections,Class<?> service,RemoteServiceRelativePath remoteAnnotation){
		try {
			//Search class extends service.getName(), class represents implementation server for gwt
			Set<?> setGwtServiceServer = reflections.getSubTypesOf(Class.forName(service.getName()));
			for (Object serviceServer : setGwtServiceServer) {
				Class<?> realName = (Class<?>) serviceServer;
				Method[] tabMethod =  realName.getDeclaredMethods();
				for (Method mMethod : tabMethod) {
					EntryPathData entry = new EntryPathData();
					entry.setTypePath(TypePath.DYNAMIC);
					entry.setHttpMethod(HttpMethod.POST);
					entry.setClassName(realName.getName());
					entry.setMethodEntry(mMethod.getName());
					entry.setSignatureName(getInternalSignature(realName.getName(),mMethod.getName()));
					entry.setUri(remoteAnnotation.value());
					entry.setListEntryPathData(searchParameterMethod(mMethod.getParameterTypes()));
					addEntryPath(entry);
				}
			}
		} catch (ClassNotFoundException e) {
			LOGGER.error(" impossible find class "
					+ service.getName());
		}
	}
	
	private List<EntryPathParam> searchParameterMethod(Class<?>[] tabParam){
		List<EntryPathParam> listEntryPathData = new ArrayList<EntryPathParam>();
		for (Class<?> mMethod : tabParam) {
			EntryPathParam param = new EntryPathParam();
			param.setKey("");
			param.setTypeParam(TypeParam.PARAM_DATA);
			param.setValue(mMethod.getName());
			listEntryPathData.add(param);
		}
		return listEntryPathData;
	}
}
