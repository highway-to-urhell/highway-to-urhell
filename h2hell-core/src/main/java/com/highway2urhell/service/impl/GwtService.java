package com.highway2urhell.service.impl;

import com.highway2urhell.VersionUtils;
import com.highway2urhell.service.AbstractLeechService;

public class GwtService extends AbstractLeechService {

	public static final String FRAMEWORK_NAME = "GWT";

	public GwtService() {
		super(FRAMEWORK_NAME, VersionUtils.getVersion(
				"com.google.gwt.user.client.rpc.RemoteServiceRelativePath",
				"com.google.gwt", "GWT"));
	}


}
