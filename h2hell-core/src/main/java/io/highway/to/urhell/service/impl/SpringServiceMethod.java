package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.VersionUtils;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.EntryPathParam;
import io.highway.to.urhell.domain.FrameworkEnum;
import io.highway.to.urhell.domain.TypePath;
import io.highway.to.urhell.service.AbstractLeechService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.NameValueExpression;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

public class SpringServiceMethod extends AbstractLeechService {

	public SpringServiceMethod() {
		super(
				FrameworkEnum.SPRING_METHOD,
				VersionUtils
						.getVersion(
								"org.springframework.web.servlet.mvc.method.RequestMappingInfo",
								"org.springframework", "spring-webmvc"));
	}

	public void gatherData(Object dataIncoming) {
		if (!getFrameworkInformations().getVersion().equals(
				VersionUtils.NO_FRAMEWORK)) {
			Map<RequestMappingInfo, HandlerMethod> handlerMethods = (Map<RequestMappingInfo, HandlerMethod>) dataIncoming;
			for (Entry<RequestMappingInfo, HandlerMethod> element : handlerMethods
					.entrySet()) {
				RequestMappingInfo requestMappingInfo = element.getKey();
				EntryPathData entrypath = new EntryPathData();
				entrypath.setTypePath(TypePath.DYNAMIC);
				entrypath.setUri(requestMappingInfo.getPatternsCondition()
						.toString());

				entrypath.setMethodEntry(requestMappingInfo
						.getMethodsCondition().toString());
				if (requestMappingInfo.getParamsCondition().getExpressions() != null) {
					List<EntryPathParam> listEntryPathData = new ArrayList<EntryPathParam>();
					for (NameValueExpression<String> nv : requestMappingInfo
							.getParamsCondition().getExpressions()) {
						EntryPathParam param = new EntryPathParam();
						param.setKey(nv.getName());
						param.setValue(nv.getValue());
						listEntryPathData.add(param);
					}
					entrypath.setListEntryPathData(listEntryPathData);

				}
				entrypath.setMethodName(element.getValue().toString());
				addEntryPath(entrypath);
			}
		}
	}

}
