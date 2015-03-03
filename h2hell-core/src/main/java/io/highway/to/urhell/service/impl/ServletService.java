package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.service.AbstractLeechService;

import java.util.List;

public class ServletService extends AbstractLeechService {

    public static final String FRAMEWORK_NAME = "SERVLET_3";

    public ServletService() {
        super(FRAMEWORK_NAME);
    }

    public void gatherData(List<EntryPathData> incoming) {
		List<EntryPathData> listEntryPath = incoming;
		for (EntryPathData entry : listEntryPath) {
			addEntryPath(entry);
		}
    }
}
