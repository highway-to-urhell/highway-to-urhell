package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.VersionUtils;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.EntryPathParam;
import io.highway.to.urhell.domain.HttpMethod;
import io.highway.to.urhell.domain.TypeParam;
import io.highway.to.urhell.domain.TypePath;
import io.highway.to.urhell.service.AbstractLeechService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.config.entities.ResultConfig;

public class Struts2Service extends AbstractLeechService {

	public static final String FRAMEWORK_NAME = "STRUTS_2";

	public Struts2Service() {
		super(FRAMEWORK_NAME, VersionUtils.getVersion(
				"com.opensymphony.xwork2.config.ConfigurationManager",
				"org.apache.struts", "struts2-core"));
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

	public void gatherData(Object dataIncoming) {
		if (!getFrameworkInformations().getVersion().equals(
				VersionUtils.NO_FRAMEWORK)) {
			if (dataIncoming != null) {
				ConfigurationManager configurationManager = (ConfigurationManager) dataIncoming;
				Configuration config = configurationManager.getConfiguration();
				Collection<PackageConfig> colPackages = config
						.getPackageConfigs().values();
				if (colPackages != null) {
					for (PackageConfig value : colPackages) {
						Collection<ActionConfig> colActionConfigs = value
								.getActionConfigs().values();
						for (ActionConfig action : colActionConfigs) {
							for (ResultConfig resultConfig : action
									.getResults().values()) {
								// recursif sur les noms de méthodes possible
								List<String> methodsList = getListMethod(action
										.getClassName());
								for(String fullNameMethod : methodsList){
									EntryPathData entry = new EntryPathData();
									entry.setTypePath(TypePath.DYNAMIC);
									entry.setMethodEntry(HttpMethod.GET.toString());
									entry.setClassName(fullNameMethod);
									entry.setUri(action.getName());
									/*entry.setListEntryPathData(findParam(resultConfig
											.getParams()));*/
									addEntryPath(entry);
								}
							}
						}
					}
				}
			} else {
				// ...
			}
		}
	}

	private List<String> getListMethod(String className) {
		List<String> res = new ArrayList<String>();
		try {
			Object obj = Class.forName(className).newInstance();
			if (obj != null) {
				Method[] tabMethod = obj.getClass().getDeclaredMethods();
				for(int i = 0; i<tabMethod.length;i++){
					res.add(className+"."+tabMethod[i].getName());
				}
			}
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			LOGGER.error("Impossible construct bean " + className);
		}
		return res;
	}

}
