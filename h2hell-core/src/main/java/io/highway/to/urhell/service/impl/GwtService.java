package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.VersionUtils;
import io.highway.to.urhell.service.AbstractLeechService;

public class GwtService extends AbstractLeechService {

	public static final String FRAMEWORK_NAME = "GWT";

	public GwtService() {
		super(FRAMEWORK_NAME, VersionUtils.getVersion(
				"com.google.gwt.user.client.rpc.RemoteServiceRelativePath",
				"com.google.gwt", "GWT"));
	}


}
