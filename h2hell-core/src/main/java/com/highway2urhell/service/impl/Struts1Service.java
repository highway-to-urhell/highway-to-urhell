package com.highway2urhell.service.impl;

import com.highway2urhell.VersionUtils;
import com.highway2urhell.service.AbstractLeechService;

public class Struts1Service extends AbstractLeechService {

	public static final String FRAMEWORK_NAME = "STRUTS_1";

	public Struts1Service() {
		super(FRAMEWORK_NAME, VersionUtils.getVersion(
				"org.apache.struts.config.impl.ModuleConfigImpl", "struts",
				"struts"));
	}
}
