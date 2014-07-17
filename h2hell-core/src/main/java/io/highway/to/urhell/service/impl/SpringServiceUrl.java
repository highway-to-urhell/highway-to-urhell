package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.VersionUtils;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.FrameworkEnum;
import io.highway.to.urhell.domain.TypePath;
import io.highway.to.urhell.service.AbstractLeechService;

import java.util.Map;
import java.util.Map.Entry;

public class SpringServiceUrl extends AbstractLeechService {

	public SpringServiceUrl() {
		super(
				FrameworkEnum.SPRING_URL,
				VersionUtils
						.getVersion(
								"org.springframework.web.servlet.mvc.method.RequestMappingInfo",
								"org.springframework", "spring-webmvc"));
	}

	public void gatherData(Object dataIncoming) {
		if (!getFrameworkInformations().getVersion().equals(
				VersionUtils.NO_FRAMEWORK)) {
			Map<String, Object> mapUrl = (Map<String, Object>) dataIncoming;
			for (Entry<String, Object> element : mapUrl.entrySet()) {
				EntryPathData entry = new EntryPathData();
				entry.setTypePath(TypePath.DYNAMIC);
				entry.setUri(element.getKey());
				entry.setMethodName(element.getValue().getClass().toString());
				addEntryPath(entry);
			}
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
