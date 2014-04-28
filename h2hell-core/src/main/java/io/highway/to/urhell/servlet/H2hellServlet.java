package io.highway.to.urhell.servlet;

import io.highway.to.urhell.domain.FrameworkInformations;
import io.highway.to.urhell.service.LeechService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 
 * Servlet add in your web.xml
 *
 */
public class H2hellServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private List<LeechService> registry = new ArrayList<LeechService>();
	private final static String highwaytourhell = "highwaytourhell";

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		out.println(executeServiceRegistry());
		out.close();
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void addServiceRegistry(LeechService serviceToAdd) {
		synchronized (highwaytourhell) {
			//Possible multi acces because scan static file or an another service
			registry.add(serviceToAdd);
		}
	}

	private String executeServiceRegistry() {
		StringBuilder sb = new StringBuilder();
		synchronized (highwaytourhell) {
			//Possible multi acces because scan static file or an another service
			for (LeechService leech : registry) {
				FrameworkInformations fwk= leech.getFrameworkInformations();
				sb.append(fwk.toString());
			}
		}
		return sb.toString();
	}

}
