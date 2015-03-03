package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.VersionUtils;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.service.AbstractLeechService;

import java.util.List;

public class Struts2Service extends AbstractLeechService {

	public static final String FRAMEWORK_NAME = "STRUTS_2";

	public Struts2Service() {

		super(FRAMEWORK_NAME, VersionUtils.getVersion(
				"com.opensymphony.xwork2.config.ConfigurationManager",
				"org.apache.struts", "struts2-core"));
	}

	public void gatherData(List<EntryPathData> dataIncoming) {

		List<EntryPathData> listEntryPath = dataIncoming;
		for (EntryPathData entry : listEntryPath) {
			addEntryPath(entry);
		}

	}

}
