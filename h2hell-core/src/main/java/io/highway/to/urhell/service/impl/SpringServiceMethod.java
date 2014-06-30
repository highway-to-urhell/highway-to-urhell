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

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.NameValueExpression;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

public class SpringServiceMethod extends AbstractLeechService {

    public SpringServiceMethod() {
        super(FrameworkEnum.SPRING_METHOD, VersionUtils.getVersion(
                RequestMappingInfo.class, "org.springframework",
                "spring-webmvc"));
    }

    public void gatherData(Object dataIncoming) {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = (Map<RequestMappingInfo, HandlerMethod>) dataIncoming;
        for (RequestMappingInfo element : handlerMethods.keySet()) {
            EntryPathData entry = new EntryPathData();
            entry.setTypePath(TypePath.DYNAMIC);
            entry.setUri(element.getPatternsCondition().toString());

            entry.setMethodEntry(element.getMethodsCondition().toString());
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
            addEntryPath(entry);
        }

    }

}
