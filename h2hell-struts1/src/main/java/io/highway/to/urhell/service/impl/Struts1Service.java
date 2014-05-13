package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.FrameworkEnum;
import io.highway.to.urhell.domain.FrameworkInformations;
import io.highway.to.urhell.service.LeechService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.Digester;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.impl.ModuleConfigImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Struts1Service implements LeechService {

	private static final Logger LOG = LoggerFactory
			.getLogger(Struts1Service.class);

	private List<EntryPathData> listData;
	@Override
	public void receiveData(Object dataIncoming) {
		Digester configDigester = (Digester) dataIncoming;
		ModuleConfigImpl m = (ModuleConfigImpl) configDigester.getRoot();
		Field f;
		try {
			f = m.getClass().getDeclaredField("actionConfigList");
			f.setAccessible(true);
			List<ActionMapping> res = (ArrayList) f.get(m);
			if (res != null) {
				for (ActionMapping action : res) {
					LOG.error("action" + action.toString());
				}
			}

		} catch (Exception e) {
			//TODO a gérer
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
