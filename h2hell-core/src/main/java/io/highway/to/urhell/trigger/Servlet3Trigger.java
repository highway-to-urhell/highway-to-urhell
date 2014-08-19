package io.highway.to.urhell.trigger;

import io.highway.to.urhell.CoreEngine;
import io.highway.to.urhell.service.impl.ServletService;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import java.util.Map;

@WebListener
public class Servlet3Trigger implements ServletContextListener {

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
