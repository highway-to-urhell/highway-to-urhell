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
        String paths = request.getParameter("paths");
        String customGeneratorClass = request.getParameter("customGeneratorClass");
        String srcPath = request.getParameter("srcPath");
        if (srcPath != null && !"".equals(srcPath)) {
            CoreService.getInstance().findSource(response, srcPath);
        } else {
            if (paths != null) {
                CoreService.getInstance().initPathsRemote(response);
            } else if (launch != null) {
                CoreService.getInstance().enableEntryPointCoverage(request, response);
            } else {
                CoreService.getInstance().generateReport(response, customGeneratorClass);
            }
        }
    }

    @Override
    public void destroy() {
        // Nothing
    }


}
