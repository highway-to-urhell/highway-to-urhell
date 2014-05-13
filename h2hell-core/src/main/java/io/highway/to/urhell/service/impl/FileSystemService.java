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

	private static final Logger LOG = LoggerFactory
			.getLogger(FileSystemService.class);
	private List<EntryPathData> listData;
	private static FileSystemService instance;
	private final static String highwaytourhell = "highwaytourhell";
	
	private FileSystemService() {
		// Nothing
	}

	public static FileSystemService getInstance() {
		if (instance == null) {
			synchronized (highwaytourhell) {
				if (instance == null) {
					instance = new FileSystemService();
				}
			}
		}
		return instance;
	}

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
