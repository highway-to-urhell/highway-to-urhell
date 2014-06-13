package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.EntryPathParam;
import io.highway.to.urhell.domain.FrameworkEnum;
import io.highway.to.urhell.domain.FrameworkInformations;
import io.highway.to.urhell.domain.MethodEntry;
import io.highway.to.urhell.domain.TypeParam;
import io.highway.to.urhell.domain.TypePath;
import io.highway.to.urhell.service.LeechService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.config.entities.ResultConfig;

public class Struts2Service implements LeechService {
	private static final Logger LOG = LoggerFactory
			.getLogger(Struts2Service.class);

	private List<EntryPathData> listData = new ArrayList<>();

	public Struts2Service() {
		LOG.info("Constructor Struts2Service");
	}

	private List<EntryPathParam> findParam(Map<String, String> map) {
		List<EntryPathParam> res = new ArrayList<EntryPathParam>();

		for (Map.Entry<String, String> entry : map.entrySet()) {
			EntryPathParam entryData = new EntryPathParam();
			entryData.setKey(entry.getKey());
			entryData.setValue(entry.getValue());
			entryData.setTypeParam(TypeParam.PARAM_DATA);
			res.add(entryData);
		}
		return res;
	}

	@Override
	public void receiveData(Object dataIncoming) {
		LOG.info("receive dataIncoming for : "
				+ Struts2Service.class.getCanonicalName());
		ConfigurationManager configurationManager = (ConfigurationManager) dataIncoming;
		Configuration config = configurationManager.getConfiguration();
		listData = new ArrayList<EntryPathData>();
		Collection<PackageConfig> colPackages = config.getPackageConfigs()
				.values();
		if (colPackages != null) {
			for (PackageConfig value : colPackages) {
				Collection<ActionConfig> colActionConfigs = value
						.getActionConfigs().values();
				for (ActionConfig action : colActionConfigs) {
					for (ResultConfig resultConfig : action.getResults()
							.values()) {
						EntryPathData entry = new EntryPathData();
						entry.setTypePath(TypePath.DYNAMIC);
						entry.setMethodEntry(MethodEntry.GET.toString());
						entry.setUri(action.getName());
						entry.setListEntryPathData(findParam(resultConfig
								.getParams()));
						listData.add(entry);
					}
				}
			}
		}
		LOG.info("complete data for : "
				+ Struts2Service.class.getCanonicalName()
				+ "number elements loaded " + listData.size());
		if(LOG.isDebugEnabled()){
			Gson gson = new Gson();
			LOG.debug(" JSON elements :"+gson.toJson(listData));
		}

	}

	@Override
	public FrameworkInformations getFrameworkInformations() {
		FrameworkInformations fwk = new FrameworkInformations();
		fwk.setFrameworkEnum(FrameworkEnum.STRUTS_2_X);
		fwk.setVersion("OPENJAR");
		fwk.setDetails("openjar");
		fwk.setListEntryPath(listData);
		return fwk;
	}
}
