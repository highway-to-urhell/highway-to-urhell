package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.VersionUtils;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.service.AbstractLeechService;

import java.util.List;

public class JSF2Service extends AbstractLeechService {

	public static final String FRAMEWORK_NAME = "JSF_2";

	public JSF2Service() {
		super(FRAMEWORK_NAME, VersionUtils.getVersion(
				"javax.faces.webapp.FacesServlet", "com.sun.faces", "jsf-impl"));
	}

	public void gatherData(Object dataIncoming) {

		List<EntryPathData> listEntryPath = (List<EntryPathData>) dataIncoming;
		for (EntryPathData entry : listEntryPath) {
			addEntryPath(entry);
		}

	}

}
