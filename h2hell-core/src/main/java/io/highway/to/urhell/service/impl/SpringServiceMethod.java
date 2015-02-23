package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.VersionUtils;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.service.AbstractLeechService;

import java.util.List;

public class SpringServiceMethod extends AbstractLeechService {

	public static final String FRAMEWORK_NAME = "SPRING_METHOD";

	public SpringServiceMethod() {
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


}
