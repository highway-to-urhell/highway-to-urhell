package com.highway2urhell.service.impl;

import com.highway2urhell.VersionUtils;
import com.highway2urhell.domain.EntryPathData;
import com.highway2urhell.service.AbstractLeechService;

import java.util.List;

public class SpringServiceUrlService extends AbstractLeechService {

	public static final String FRAMEWORK_NAME = "SPRING_URL";

	public SpringServiceUrlService() {
		super(
				FRAMEWORK_NAME,
				VersionUtils
						.getVersion(
								"org.springframework.web.servlet.mvc.method.RequestMappingInfo",
								"org.springframework", "spring-webmvc"));
	}

	@Override
	public void receiveData(List<EntryPathData> incoming) {
		// clearPreviousData();
		LOGGER.debug("receive incoming data");
		gatherData(incoming);
		LOGGER.debug("data gathering complete. Found {} entries",
				getFrameworkInformations().getListEntryPath().size());
	}

}
