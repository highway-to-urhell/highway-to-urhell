package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.VersionUtils;
import io.highway.to.urhell.service.AbstractLeechService;

public class JSF2Service extends AbstractLeechService {

	public static final String FRAMEWORK_NAME = "JSF_2";

	public JSF2Service() {
		super(FRAMEWORK_NAME, VersionUtils.getVersion(
				"javax.faces.webapp.FacesServlet", "com.sun.faces", "jsf-impl"));
	}
}
