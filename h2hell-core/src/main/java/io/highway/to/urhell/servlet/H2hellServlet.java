package io.highway.to.urhell.servlet;

import io.highway.to.urhell.CoreEngine;
import io.highway.to.urhell.service.LeechService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

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
        Collection<LeechService> leechServiceRegistered = CoreEngine
                .getInstance().getLeechServiceRegistered();

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        if (leechServiceRegistered.isEmpty()) {
            out.println("No leech service are registered");
        } else {
            String data = request.getParameter("html");
            if (data != null && !"".equals(data)) {
                LOG.debug("HTML Response");
                response.setContentType("text/html");
                out.println(GeneratorResult.getInstance().createPage(
                        leechServiceRegistered));
            } else {
                LOG.debug("JSON Response");
                Gson gson = new Gson();
                out.println(gson.toJson(CoreEngine.getInstance()
                        .getLeechServiceRegistered()));
                response.setContentType("application/json");

            }
        }
        out.close();
    }

}
