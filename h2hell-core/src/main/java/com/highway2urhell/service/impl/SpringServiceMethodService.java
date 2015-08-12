package com.highway2urhell.service.impl;

import com.highway2urhell.VersionUtils;
import com.highway2urhell.service.AbstractLeechService;

public class SpringServiceMethodService extends AbstractLeechService {

	public static final String FRAMEWORK_NAME = "SPRING_METHOD";

	public SpringServiceMethodService() {
		super(
				FRAMEWORK_NAME,
				VersionUtils
						.getVersion(
								"org.springframework.web.servlet.mvc.method.RequestMappingInfo",
								"org.springframework", "spring-webmvc"));
	}
}
