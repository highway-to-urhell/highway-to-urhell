package io.highway.to.urhell.trigger;

import io.highway.to.urhell.CoreEngine;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.EntryPathParam;
import io.highway.to.urhell.service.impl.ServletService;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@WebListener
public class Servlet3Trigger implements ServletContextListener {

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        Map<String, ? extends ServletRegistration> map = servletContext.getServletRegistrations();
        List<EntryPathData> results = new ArrayList<>();
        for (ServletRegistration sv : map.values()) {
            if (sv.getMappings() != null) {
                for (String mapping : sv.getMappings()) {
                    EntryPathData entry = new EntryPathData();
                    entry.setClassName(sv.getName());
                    entry.setUri(mapping);
                    if (sv.getInitParameters() != null) {
                        List<EntryPathParam> listEntryPathParams = new ArrayList<EntryPathParam>();
                        for (Entry<String, String> initParameter : sv.getInitParameters().entrySet()) {
                            EntryPathParam en = new EntryPathParam();
                            en.setKey(initParameter.getKey());
                            en.setValue(initParameter.getValue());
                        }
                        entry.setListEntryPathData(listEntryPathParams);
                    }
                    results.add(entry);
                }
            }
        }

        CoreEngine.getInstance().getFramework(ServletService.FRAMEWORK_NAME).receiveData(results);
    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
        //?!
    }

}
