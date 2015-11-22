package com.highway2urhell.trigger;

import com.highway2urhell.CoreEngine;
import com.highway2urhell.domain.EntryPathData;
import com.highway2urhell.domain.EntryPathParam;
import com.highway2urhell.service.impl.ServletService;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@WebListener
public class Servlet3Trigger implements ServletContextListener {

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        Map map = servletContext.getServletRegistrations();
        List<EntryPathData> results = new ArrayList<EntryPathData>();
        Collection<ServletRegistration> col = (Collection<ServletRegistration>) map.values();
        for (ServletRegistration sv : col) {
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
