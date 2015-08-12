package com.highway2urhell.filter;

import com.highway2urhell.service.CoreService;

import javax.servlet.*;
import java.io.IOException;

public class H2hellFilter implements Filter {
    FilterConfig filterConfig = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        String launch = request.getParameter("launch");
        String customGeneratorClass = request.getParameter("customGeneratorClass");
        String srcPath = request.getParameter("srcPath");
        if (srcPath !=null && !"".equals(srcPath)){
            CoreService.getInstance().findSource(response,srcPath);
        }else {
            if (launch != null) {
                CoreService.getInstance().enableEntryPointCoverage(response);
            }
            CoreService.getInstance().generateReport(response,customGeneratorClass);
        }
    }

    @Override
    public void destroy() {
        // Nothing
    }


}
