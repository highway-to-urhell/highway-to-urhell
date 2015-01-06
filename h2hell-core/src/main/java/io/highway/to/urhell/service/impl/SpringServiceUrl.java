package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.VersionUtils;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.TypePath;
import io.highway.to.urhell.service.AbstractLeechService;

import java.util.Map;
import java.util.Map.Entry;

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
        if (!getFrameworkInformations().getVersion().equals(
                VersionUtils.NO_FRAMEWORK)) {
            Map<String, Object> mapUrl = (Map<String, Object>) dataIncoming;
            for (Entry<String, Object> element : mapUrl.entrySet()) {
                EntryPathData entry = new EntryPathData();
                entry.setTypePath(TypePath.DYNAMIC);
                entry.setUri(element.getKey());
                entry.setClassName(removeClassName(element.getValue().getClass().toString()));
                addEntryPath(entry);
            }
        }
    }
    
    private String removeClassName(String fullNameDescriptor){
    	if(fullNameDescriptor.contains("class")){
    		String elem = fullNameDescriptor.replace("class ", "");
    		return elem;
    	}else{
    		return fullNameDescriptor;
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
