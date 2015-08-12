package com.highway2urhell.service.impl;

import com.highway2urhell.VersionUtils;
import com.highway2urhell.service.AbstractLeechService;

public class JSF2Service extends AbstractLeechService {

	public static final String FRAMEWORK_NAME = "JSF_2";

	public JSF2Service() {
		super(FRAMEWORK_NAME, VersionUtils.getVersion(
				"javax.faces.webapp.FacesServlet", "com.sun.faces", "jsf-impl"));
	}
}
