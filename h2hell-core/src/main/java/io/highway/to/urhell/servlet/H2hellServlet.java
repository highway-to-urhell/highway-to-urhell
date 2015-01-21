package io.highway.to.urhell.servlet;

import io.highway.to.urhell.CoreEngine;
import io.highway.to.urhell.generator.TheJack;
import io.highway.to.urhell.generator.impl.JSONGenerator;
import io.highway.to.urhell.service.LeechService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

/**
 * Servlet add in your web.xml
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

        CoreEngine.getInstance().leech();

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        String classGenerate = request.getParameter("classGenerate");
        if (classGenerate != null && !"".equals(classGenerate)) {
            LOG.debug("HTML Response");
            response.setContentType("text/html");
            TheJack gen;
            try {
                gen = (TheJack) Class.forName(classGenerate).newInstance();
                out.println(gen.generatePage(leechServiceRegistered));
            } catch (InstantiationException | IllegalAccessException
                    | ClassNotFoundException e) {
                LOG.error("Can't generate page using " + classGenerate, e);
                out.println(generateJSon(leechServiceRegistered));
                response.setContentType("application/json");
            }
        } else {
            LOG.info("No class generator - using default json");
            out.println(generateJSon(leechServiceRegistered));
            response.setContentType("application/json");
        }
        out.close();
    }

    private String generateJSon(Collection<LeechService> leechServiceRegistered) {
        JSONGenerator gen = new JSONGenerator();
        return gen.generatePage(leechServiceRegistered);
    }

}
