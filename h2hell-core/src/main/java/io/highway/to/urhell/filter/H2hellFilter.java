package io.highway.to.urhell.filter;

import io.highway.to.urhell.CoreEngine;
import io.highway.to.urhell.generator.TheJack;
import io.highway.to.urhell.generator.impl.JSONGenerator;
import io.highway.to.urhell.service.LeechService;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.instrument.UnmodifiableClassException;
import java.util.Collection;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class H2hellFilter implements Filter {
	FilterConfig filterConfig = null;
	private static final Logger LOG = LoggerFactory
			.getLogger(H2hellFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		String launch = request.getParameter("launch");
		String classGenerate = request.getParameter("classGenerate");
		if(launch!=null || classGenerate!=null){
			treatRequest(request,response,launch,classGenerate);
		}else{
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		// Nothing

	}
	
	private void treatRequest(ServletRequest request, ServletResponse response,String launch, String classGenerate) throws IOException{
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		
		if (launch != null) {
			try {
				CoreEngine.getInstance().launchTransformerGeneric();
				out.print("Transformer activated for App "
						+ CoreEngine.getInstance().getConfig()
								.getNameApplication());
			} catch (ClassNotFoundException | UnmodifiableClassException e) {
				LOG.error("Error while launchTransformer ", e);
				out.print(e);
			}
		} else {
			Collection<LeechService> leechServiceRegistered = CoreEngine
					.getInstance().getLeechServiceRegistered();

			CoreEngine.getInstance().leech();
			
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
		}
		out.close();
	}
	
	private String generateJSon(Collection<LeechService> leechServiceRegistered) {
		JSONGenerator gen = new JSONGenerator();
		return gen.generatePage(leechServiceRegistered);
	}

}
