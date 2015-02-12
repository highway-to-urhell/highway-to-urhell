package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.VersionUtils;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.EntryPathParam;
import io.highway.to.urhell.domain.TypeParam;
import io.highway.to.urhell.domain.TypePath;
import io.highway.to.urhell.service.AbstractLeechService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.PackageConfig;

public class Struts2Service extends AbstractLeechService {

	public static final String FRAMEWORK_NAME = "STRUTS_2";

	public Struts2Service() {

		super(FRAMEWORK_NAME, VersionUtils.getVersion(
				"com.opensymphony.xwork2.config.ConfigurationManager",
				"org.apache.struts", "struts2-core"));
	}

	public void gatherData(Object dataIncoming) {
		//getFrameworkInformations().setVersion("fu");
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
							findAllClassAndMethod(action);
						}
					}
				}
			}
		}
	}

	private void findAllClassAndMethod(ActionConfig action) {
		if (action.getClassName() != null && !"".equals(action.getClassName())) {
			try {
				Class<?> c = Class.forName(action.getClassName());
				for (Method m : c.getDeclaredMethods()) {
					EntryPathData entry = new EntryPathData();
					entry.setTypePath(TypePath.DYNAMIC);
					entry.setClassName(action.getClassName());
					entry.setMethodEntry(m.getName());
					entry.setUri(action.getName());
					entry.setListEntryPathData(searchParameterMethod(m
							.getParameterTypes()));
					entry.setSignatureName(getInternalSignature(m));
					addEntryPath(entry);
				}
			} catch (ClassNotFoundException e) {
				LOGGER.error("Class Not Found :" + action.getClassName(), e);
			}
		}
	}

	private List<EntryPathParam> searchParameterMethod(Class<?>[] tabParam) {
		List<EntryPathParam> listEntryPathData = new ArrayList<EntryPathParam>();
		for (Class<?> mMethod : tabParam) {
			EntryPathParam param = new EntryPathParam();
			param.setKey("");
			param.setTypeParam(TypeParam.PARAM_DATA);
			param.setValue(mMethod.getName());
			listEntryPathData.add(param);
		}
		return listEntryPathData;
	}

}
