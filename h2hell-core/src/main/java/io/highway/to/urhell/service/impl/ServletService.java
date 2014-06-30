package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.EntryPathParam;
import io.highway.to.urhell.domain.FrameworkEnum;
import io.highway.to.urhell.service.AbstractLeechService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRegistration;

public class ServletService extends AbstractLeechService {

	public ServletService() {
        super(FrameworkEnum.SERVLET_3);
    }

	public void gatherData(Object incoming) {
		Map<String, ? extends ServletRegistration> map = (Map<String, ? extends ServletRegistration>) incoming;
		if (map.values() != null) {
			for (ServletRegistration sv : map.values()) {
				if (sv.getMappings() != null) {
					for (String mapping : sv.getMappings()) {
						EntryPathData entry = new EntryPathData();
						entry.setMethodName(sv.getName());
						entry.setUri(mapping);
						if (sv.getInitParameters() != null) {
							List<EntryPathParam> listEntryPathParams = new ArrayList<EntryPathParam>();
							for (Entry<String, String> initParameter: sv.getInitParameters().entrySet()) {
								EntryPathParam en = new EntryPathParam();
								en.setKey(initParameter.getKey());
								en.setValue(initParameter.getValue());
							}
							entry.setListEntryPathData(listEntryPathParams);
						}
						addEntryPath(entry);
					}
				}
			}

		}
	}
}
