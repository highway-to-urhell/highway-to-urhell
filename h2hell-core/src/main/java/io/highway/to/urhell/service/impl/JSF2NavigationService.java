package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.VersionUtils;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.HttpMethod;
import io.highway.to.urhell.domain.TypePath;
import io.highway.to.urhell.service.AbstractLeechService;

import javax.faces.application.NavigationCase;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class JSF2NavigationService extends AbstractLeechService {

    public static final String FRAMEWORK_NAME = "JSF_2_NAVIGATION";

    public JSF2NavigationService() {
        super(FRAMEWORK_NAME, VersionUtils.getVersion(
                "javax.faces.webapp.FacesServlet", "com.sun.faces", "jsf-impl"));
    }

    @Override
    protected void gatherData(Object incoming) {
        Map<String, Set<NavigationCase>> listNav = (Map<String, Set<NavigationCase>>) incoming;
        // Entry
        for (Entry<String, Set<NavigationCase>> nav : listNav.entrySet()) {
            for (NavigationCase nLocal : nav.getValue()) {
                EntryPathData entry = new EntryPathData();
                entry.setHttpMethod(HttpMethod.UNKNOWN);
                entry.setMethodEntry(nav.getKey());
                entry.setClassName(nLocal.toString());
                entry.setTypePath(TypePath.DYNAMIC);
                addEntryPath(entry);
            }
        }
    }

}
