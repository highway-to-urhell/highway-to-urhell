package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.FrameworkEnum;
import io.highway.to.urhell.domain.FrameworkInformations;
import io.highway.to.urhell.service.LeechService;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSystemService implements LeechService {
	
	public FileSystemService(){
		LOG.info("Constructor FileSystemService");
	}

	private static final Logger LOG = LoggerFactory
			.getLogger(FileSystemService.class);
	private List<EntryPathData> listData;

	@Override
	public void receiveData(Object incoming) {
		//no incoming object
		//launch scan
		//Step 1 scan web.xml
		
		//Step 2 scan static files 
		listData = new ArrayList<EntryPathData>();
		
	}

	@Override
	public FrameworkInformations getFrameworkInformations() {
		FrameworkInformations fwk = new FrameworkInformations();
		fwk.setFrameworkEnum(FrameworkEnum.SYSTEM);
		fwk.setVersion("OPENJAR");
		fwk.setDetails("openjar");
		fwk.setListEntryPath(listData);
		return fwk;
	}
	
	

}
