package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.FrameworkEnum;
import io.highway.to.urhell.domain.FrameworkInformations;
import io.highway.to.urhell.domain.TypePath;
import io.highway.to.urhell.service.LeechService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.Digester;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.impl.ModuleConfigImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class Struts1Service implements LeechService {

	private static final Logger LOG = LoggerFactory
			.getLogger(Struts1Service.class);

	private List<EntryPathData> listData;

	@Override
	public void receiveData(Object dataIncoming) {
		listData = new ArrayList<EntryPathData>();
		Digester configDigester = (Digester) dataIncoming;
		ModuleConfigImpl m = (ModuleConfigImpl) configDigester.getRoot();
		Field f;
		try {
			f = m.getClass().getDeclaredField("actionConfigList");
			f.setAccessible(true);
			List<ActionMapping> res = (ArrayList) f.get(m);
			if (res != null) {
				for (ActionMapping action : res) {
					EntryPathData entry = new EntryPathData();
					entry.setMethodName(action.getName());
					if(action.getPrefix()!=null && !"null".equals(action.getPrefix())){
						entry.setUri(action.getPrefix()+action.getPath());
					}else{
						entry.setUri(action.getPath());
					}
					entry.setTypePath(TypePath.DYNAMIC);
					entry.setMethodEntry(action.getInput());
					listData.add(entry);
				}
			}

		} catch (Exception e) {
			LOG.error("Exception in "+Struts1Service.class.getCanonicalName()+" receiveData "+dataIncoming+" msg :"+e.getMessage());
		}
		LOG.info("complete data for : "
				+ Struts1Service.class.getCanonicalName()
				+ "number elements loaded " + listData.size());
		if(LOG.isDebugEnabled()){
			Gson gson = new Gson();
			LOG.debug(" JSON elements :"+gson.toJson(listData));
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
