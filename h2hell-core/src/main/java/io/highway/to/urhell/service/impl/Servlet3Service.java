package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.CoreEngine;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import java.util.Map;

@WebListener
public class Servlet3Service implements ServletContextListener {

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        Map<String, ? extends ServletRegistration> map = servletContext.getServletRegistrations();
        CoreEngine.getInstance().getFramework(ServletService.FRAMEWORK_NAME).receiveData(map);
    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
        //?!
    }

}
