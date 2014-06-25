package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.EntryPathParam;
import io.highway.to.urhell.domain.FrameworkEnum;
import io.highway.to.urhell.domain.FrameworkInformations;
import io.highway.to.urhell.domain.MethodEntry;
import io.highway.to.urhell.service.LeechService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletService implements LeechService {

	private static final Logger LOG = LoggerFactory
			.getLogger(ServletService.class);
	private List<EntryPathData> listData;

	@Override
	public void receiveData(Object incoming) {
		listData = new ArrayList<EntryPathData>();
		LOG.info("receive dataIncoming for : "
				+ ServletService.class.getCanonicalName());
		Map<String, ? extends ServletRegistration> map = (Map<String, ? extends ServletRegistration>) incoming;
		if (map.values() != null) {
			for (ServletRegistration sv : map.values()) {
				if (sv.getMappings() != null) {
					for (String mapping : sv.getMappings()) {
						EntryPathData entry = new EntryPathData();
						entry.setMethodName(sv.getName());
						entry.setUri(mapping);
						entry.setMethodEntry(MethodEntry.UNKNOWN.toString());
						if (sv.getInitParameters() != null) {
							List<EntryPathParam> listEntryPathParams = new ArrayList<EntryPathParam>();
							for (String key : sv.getInitParameters().keySet()) {
								EntryPathParam en = new EntryPathParam();
								en.setKey(key);
								en.setValue(sv.getInitParameter(key));
							}
							entry.setListEntryPathData(listEntryPathParams);
						}
						listData.add(entry);
					}
				}
			}

		}
		LOG.info("complete data for : "
				+ ServletService.class.getCanonicalName()
				+ "number elements loaded " + listData.size());
	}

	@Override
	public FrameworkInformations getFrameworkInformations() {
		FrameworkInformations fwk = new FrameworkInformations();
		fwk.setFrameworkEnum(FrameworkEnum.SERVLET_3);
		fwk.setVersion("OPENJAR");
		fwk.setDetails("openjar");
		fwk.setListEntryPath(listData);
		return fwk;
	}

}
