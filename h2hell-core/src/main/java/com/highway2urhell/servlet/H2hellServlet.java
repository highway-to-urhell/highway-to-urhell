package com.highway2urhell.servlet;

import com.highway2urhell.service.CoreService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/h2h/*", name = "h2h-servlet")
public class H2hellServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
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

}
