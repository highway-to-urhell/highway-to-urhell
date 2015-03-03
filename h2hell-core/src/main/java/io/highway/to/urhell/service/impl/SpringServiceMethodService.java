package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.VersionUtils;
import io.highway.to.urhell.service.AbstractLeechService;

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
