package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.VersionUtils;
import io.highway.to.urhell.service.AbstractLeechService;

public class Struts1Service extends AbstractLeechService {

	public static final String FRAMEWORK_NAME = "STRUTS_1";

	public Struts1Service() {
		super(FRAMEWORK_NAME, VersionUtils.getVersion(
				"org.apache.struts.config.impl.ModuleConfigImpl", "struts",
				"struts"));
	}
}
