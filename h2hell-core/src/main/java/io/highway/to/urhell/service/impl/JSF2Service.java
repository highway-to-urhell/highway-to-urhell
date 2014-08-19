package io.highway.to.urhell.service.impl;

import com.sun.faces.mgbean.BeanBuilder;
import io.highway.to.urhell.VersionUtils;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.HttpMethod;
import io.highway.to.urhell.domain.TypePath;
import io.highway.to.urhell.service.AbstractLeechService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JSF2Service extends AbstractLeechService {

    public static final String FRAMEWORK_NAME = "JSF_2";

    public JSF2Service() {
        super(FRAMEWORK_NAME, VersionUtils.getVersion(
                "javax.faces.webapp.FacesServlet", "com.sun.faces", "jsf-impl"));
    }

    @Override
    protected void gatherData(Object incoming) {
        Map<String, BeanBuilder> managedBeans = (Map<String, BeanBuilder>) incoming;
        for (Entry<String, BeanBuilder> element : managedBeans.entrySet()) {
            //search all public method
            List<String> ListMethod = getPublicMethod(element.getValue().getManagedBeanInfo().getClassName());
            for (String nameMethod : ListMethod) {
                EntryPathData entry = new EntryPathData();
                entry.setHttpMethod(HttpMethod.UNKNOWN);
                entry.setMethodEntry(nameMethod);
                entry.setMethodName(element.getValue().getManagedBeanInfo().getClassName());
                entry.setTypePath(TypePath.DYNAMIC);
                addEntryPath(entry);
            }

        }
    }

    private List<String> getPublicMethod(String className) {
        List<String> res = new ArrayList<String>();
        try {
            Method[] tabMethod = Class.forName(className).getDeclaredMethods();
            for (int i = 0; i < tabMethod.length; i++) {
                res.add(tabMethod[i].getName());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return res;
    }

}
