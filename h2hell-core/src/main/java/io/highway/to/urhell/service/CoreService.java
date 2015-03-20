package io.highway.to.urhell.service;

import io.highway.to.urhell.CoreEngine;
import io.highway.to.urhell.generator.TheJack;
import io.highway.to.urhell.generator.impl.JSONGenerator;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.instrument.UnmodifiableClassException;
import java.util.Collection;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoreService {
	private static final Logger LOG = LoggerFactory
			.getLogger(CoreService.class);

	
	public void treatRequest(ServletRequest request, ServletResponse response,
			String launch, String classGenerate) throws IOException {
		if (launch != null) {
			treatLaunchTransformer(response);
		} else {
			Collection<LeechService> leechServiceRegistered = CoreEngine.getInstance().getLeechServiceRegistered();
			CoreEngine.getInstance().leech();
			if (classGenerate != null && !"".equals(classGenerate)) {
				treatClassGenerate(response, classGenerate,	leechServiceRegistered);
			} else {
				defaultClassGenerate(response, leechServiceRegistered);
			}
		}
	}

	private void defaultClassGenerate(ServletResponse response,
			Collection<LeechService> leechServiceRegistered) throws IOException {
		PrintWriter out = response.getWriter();
		LOG.info("No class generator - using default json");
		out.println(generateJSon(leechServiceRegistered));
		response.setContentType("application/json");
		out.close();
	}

	private void treatClassGenerate(ServletResponse response,
			String classGenerate,
			Collection<LeechService> leechServiceRegistered) throws IOException {
		PrintWriter out = response.getWriter();
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
		out.close();
	}

	private void treatLaunchTransformer(ServletResponse response)
			throws IOException {
		PrintWriter out = response.getWriter();
		try {
			CoreEngine.getInstance().launchTransformerGeneric();
			out.print("Transformer activated for App "
					+ CoreEngine.getInstance().getConfig().getNameApplication());
		} catch (ClassNotFoundException | UnmodifiableClassException e) {
			LOG.error("Error while launchTransformer ", e);
			out.print(e);
		}
		out.close();
	}

	
	private String generateJSon(Collection<LeechService> leechServiceRegistered) {
		JSONGenerator gen = new JSONGenerator();
		return gen.generatePage(leechServiceRegistered);
	}

}
