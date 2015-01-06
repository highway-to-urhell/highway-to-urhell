package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.VersionUtils;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.EntryPathParam;
import io.highway.to.urhell.domain.TypeParam;
import io.highway.to.urhell.domain.TypePath;
import io.highway.to.urhell.service.AbstractLeechService;

import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.NameValueExpression;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SpringServiceMethod extends AbstractLeechService {

	public static final String FRAMEWORK_NAME = "SPRING_METHOD";

	public SpringServiceMethod() {
		super(
				FRAMEWORK_NAME,
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
				String className = removeClassName(element.getValue().getBeanType().toString());
				String methodName = element.getValue().getMethod().getName();
				entrypath.setClassName(className);
				entrypath.setMethodEntry(methodName);
				//search parameter
				entrypath.setListEntryPathData(searchParameterMethod(element));
				addEntryPath(entrypath);
			}
		}
	}
	
	private List<EntryPathParam> searchParameterMethod(Entry<RequestMappingInfo, HandlerMethod> element){
		List<EntryPathParam> listEntryPathData = new ArrayList<EntryPathParam>();
		if (element.getValue().getMethodParameters() != null) {
			MethodParameter[] mParam = element.getValue()
					.getMethodParameters();
			for (MethodParameter mp : mParam) {
				EntryPathParam param = new EntryPathParam();
				param.setKey(String.valueOf(mp.getParameterIndex()));
				param.setTypeParam(TypeParam.PARAM_DATA);
				param.setValue(mp.getParameterType().toString());
				listEntryPathData.add(param);
			}
			
		}
		return listEntryPathData;
	}
	
	 private String removeClassName(String fullNameDescriptor){
	    	if(fullNameDescriptor.contains("class")){
	    		String elem = fullNameDescriptor.replace("class ", "");
	    		return elem;
	    	}else{
	    		return fullNameDescriptor;
	    	}
	    }

}
