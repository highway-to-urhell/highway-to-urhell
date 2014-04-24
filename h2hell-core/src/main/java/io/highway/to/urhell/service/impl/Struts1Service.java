package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.FrameworkEnum;
import io.highway.to.urhell.domain.FrameworkInformations;
import io.highway.to.urhell.service.LeechService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.Digester;
import org.apache.struts.config.impl.ModuleConfigImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Struts1Service implements LeechService {

	private static final Logger LOG = LoggerFactory
			.getLogger(Struts1Service.class);

	private List<EntryPathData> listData;
	private static Struts1Service instance;
	private final static String highwaytourhell = "highwaytourhell";

	private Struts1Service() {
		// Nothing
	}

	public static Struts1Service getInstance() {
		if (instance == null) {
			synchronized (highwaytourhell) {
				if (instance == null) {
					instance = new Struts1Service();
				}
			}
		}
		return instance;
	}

	
	@Override
	public String addMethodAndLogic() {
		return ("Struts1Service.getInstance().receiveData(configDigester)");
	}
	
	@Override
	public void receiveData(Object dataIncoming) {
		Digester configDigester = (Digester) dataIncoming;
		ModuleConfigImpl m = (ModuleConfigImpl) configDigester.getRoot();
        Field f;
		try {
			f = m.getClass().getDeclaredField("actionConfigList");
			f.setAccessible(true);
			List res = (ArrayList) f.get(m);
	       
			LOG.error("TRACE"+res.toString());
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	@Override
	public FrameworkInformations getFrameworkInformations() {
		FrameworkInformations fwk = new FrameworkInformations();
		fwk.setFrameworkEnum(FrameworkEnum.STRUTS_1_X);
		fwk.setVersion("OPENJAR");
		fwk.setDetails("openjar");
		fwk.setListEntryPath(listData);
		return fwk;
	}
	
}
