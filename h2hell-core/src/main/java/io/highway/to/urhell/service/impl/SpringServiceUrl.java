package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.EntryPathParam;
import io.highway.to.urhell.domain.FrameworkEnum;
import io.highway.to.urhell.domain.FrameworkInformations;
import io.highway.to.urhell.domain.MethodEntry;
import io.highway.to.urhell.domain.TypePath;
import io.highway.to.urhell.service.LeechService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringServiceUrl  implements LeechService {

	private static final Logger LOG = LoggerFactory
			.getLogger(SpringServiceUrl.class);
	private List<EntryPathData> listData = new ArrayList<EntryPathData>();

	public SpringServiceUrl() {
		LOG.info("Constructor SpringServiceUrl");
	}
	
	@Override
	public void receiveData(Object dataIncoming) {
		LOG.info("receive dataIncoming for : "+SpringServiceMethod.class.getCanonicalName());
		Map<String, Object> mapUrl = (Map<String, Object>) dataIncoming;
		for (String element : mapUrl.keySet()) {
			EntryPathData entry = new EntryPathData();
			entry.setTypePath(TypePath.DYNAMIC);
			entry.setUri(element);
			entry.setMethodName(mapUrl.get(element).getClass().toString());
			entry.setMethodEntry(MethodEntry.UNKNOWN.toString());
			entry.setListEntryPathData(new ArrayList<EntryPathParam>());
			listData.add(entry);
		}
		LOG.info("complete data for : "+SpringServiceUrl.class.getCanonicalName()+ "number elements loaded "+listData.size());
	}
	
	@Override
	public FrameworkInformations getFrameworkInformations() {
		FrameworkInformations fwk = new FrameworkInformations();
		fwk.setFrameworkEnum(FrameworkEnum.SPRING_URL);
		fwk.setVersion("OPENJAR");
		fwk.setDetails("openjar");
		fwk.setListEntryPath(listData);
		return fwk;
	}


}
