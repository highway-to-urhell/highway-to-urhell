package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.VersionUtils;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.EntryPathParam;
import io.highway.to.urhell.domain.FrameworkEnum;
import io.highway.to.urhell.domain.TypePath;
import io.highway.to.urhell.service.AbstractLeechService;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

public class SpringServiceUrl  extends AbstractLeechService{

	public SpringServiceUrl() {
        super(FrameworkEnum.SPRING_URL, VersionUtils.getVersion(
                RequestMappingInfo.class, "org.springframework",
                "spring-webmvc"));
    }

	public void gatherData(Object dataIncoming) {
		Map<String, Object> mapUrl = (Map<String, Object>) dataIncoming;
		for (String element : mapUrl.keySet()) {
			EntryPathData entry = new EntryPathData();
			entry.setTypePath(TypePath.DYNAMIC);
			entry.setUri(element);
			entry.setMethodName(mapUrl.get(element).getClass().toString());
			entry.setListEntryPathData(new ArrayList<EntryPathParam>());
			addEntryPath(entry);
		}
	}
	


}
