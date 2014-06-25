package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.EntryPathParam;
import io.highway.to.urhell.domain.FrameworkEnum;
import io.highway.to.urhell.domain.FrameworkInformations;
import io.highway.to.urhell.domain.TypePath;
import io.highway.to.urhell.service.LeechService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.NameValueExpression;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import com.google.gson.Gson;

public class SpringServiceMethod implements LeechService {

	private static final Logger LOG = LoggerFactory
			.getLogger(SpringServiceMethod.class);
	private List<EntryPathData> listData = new ArrayList<EntryPathData>();

	@Override
	public void receiveData(Object dataIncoming) {
		LOG.info("receive dataIncoming for : "
				+ SpringServiceMethod.class.getCanonicalName());
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = (Map<RequestMappingInfo, HandlerMethod>) dataIncoming;
		for (RequestMappingInfo element : handlerMethods.keySet()) {
			EntryPathData entry = new EntryPathData();
			entry.setTypePath(TypePath.DYNAMIC);
			entry.setUri(element.getPatternsCondition().toString());
			
			entry.setMethodEntry(element
					.getMethodsCondition().toString());
			if (element.getParamsCondition().getExpressions() != null) {
				List<EntryPathParam> listEntryPathData = new ArrayList<EntryPathParam>();
				for (NameValueExpression<String> nv : element
						.getParamsCondition().getExpressions()) {
					EntryPathParam param = new EntryPathParam();
					param.setKey(nv.getName());
					param.setValue(nv.getValue());
					listEntryPathData.add(param);
				}
				entry.setListEntryPathData(listEntryPathData);

			}
			entry.setMethodName(handlerMethods.get(element).toString());
			listData.add(entry);
		}
		LOG.info("complete data for : "
				+ SpringServiceMethod.class.getCanonicalName()
				+ "number elements loaded " + listData.size());
		if(LOG.isDebugEnabled()){
			Gson gson = new Gson();
			LOG.debug(" JSON elements :"+gson.toJson(listData));
		}

	}

	@Override
	public FrameworkInformations getFrameworkInformations() {
		FrameworkInformations fwk = new FrameworkInformations();
		fwk.setFrameworkEnum(FrameworkEnum.SPRING_METHOD);
		fwk.setListEntryPath(listData);
		return fwk;
	}

}
