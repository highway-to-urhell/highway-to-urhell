package io.highway.to.urhell.service.impl;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

@WebListener
public class H2hellServlet3 implements ServletContextListener {
	
    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        Map<String, ? extends ServletRegistration> map = servletContext.getServletRegistrations();
//        ServletService.getInstance().receiveData(map);
    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
        //?!
    }

}
