package io.highway.to.urhell.servlet;

import io.highway.to.urhell.service.CoreService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet add in your web.xml
 */
@WebServlet(value = "/h2h/*", name = "h2h-servlet")
public class H2hellServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,IOException{
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
        String launch = request.getParameter("launch");
        String customGeneratorClass = request.getParameter("customGeneratorClass");

        if(launch!=null) {
            CoreService.getInstance().enableEntryPointCoverage(response);
        }
        CoreService.getInstance().generateReport(response,customGeneratorClass);
	}

}
