package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.VersionUtils;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.service.AbstractLeechService;

import java.util.List;

public class SpringServiceUrl extends AbstractLeechService {

	public static final String FRAMEWORK_NAME = "SPRING_URL";

	public SpringServiceUrl() {
		super(
				FRAMEWORK_NAME,
				VersionUtils
						.getVersion(
								"org.springframework.web.servlet.mvc.method.RequestMappingInfo",
								"org.springframework", "spring-webmvc"));
	}

	public void gatherData(Object dataIncoming) {

		List<EntryPathData> listEntryPath = (List<EntryPathData>) dataIncoming;
		for (EntryPathData entry : listEntryPath) {
			addEntryPath(entry);
		}
	}

	@Override
	public void receiveData(Object incoming) {
		// clearPreviousData();
		LOGGER.debug("receive incoming data");
		gatherData(incoming);
		LOGGER.debug("data gathering complete. Found {} entries",
				getFrameworkInformations().getListEntryPath().size());
	}

}
