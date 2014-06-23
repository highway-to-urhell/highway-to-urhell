package io.highway.to.urhell.servlet;

import io.highway.to.urhell.CoreEngine;
import io.highway.to.urhell.service.impl.FileSystemService;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Servlet add in your web.xml
 * 
 */
@WebServlet(value = "/h2h/*", name = "h2h-servlet")
public class H2hellServlet extends HttpServlet {

	private static final Logger LOG = LoggerFactory
			.getLogger(H2hellServlet.class);
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		String data = request.getParameter("html");
		if (data != null && !"".equals(data)) {
			LOG.info("HTML Response");
			response.setContentType("text/html");
			out.println(GeneratorResult.getInstance().createPage(
					CoreEngine.getInstance().dumpLeechResult()));
			
		} else {
			LOG.info("JSON Response");
			out.println(CoreEngine.getInstance().dumpLeechResultString());
			response.setContentType("application/json");
			
		}
		out.close();
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

}
