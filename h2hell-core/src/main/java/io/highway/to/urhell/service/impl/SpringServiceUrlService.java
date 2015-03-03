package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.VersionUtils;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.service.AbstractLeechService;

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
