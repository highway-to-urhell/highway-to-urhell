package io.highway.to.urhell.filter;

import io.highway.to.urhell.service.CoreService;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

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
		String classGenerate = request.getParameter("classGenerate");
		if(launch!=null || classGenerate!=null){
			CoreService coreService = new CoreService();
			coreService.treatRequest(request,response,launch,classGenerate);
		}else{
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		// Nothing
	}
	
	
}
