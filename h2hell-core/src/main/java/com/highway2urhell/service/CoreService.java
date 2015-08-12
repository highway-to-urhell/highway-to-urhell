package com.highway2urhell.service;

import com.highway2urhell.CoreEngine;
import com.highway2urhell.generator.TheJack;
import com.highway2urhell.generator.impl.JSONGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.instrument.UnmodifiableClassException;
import java.util.Collection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CoreService {
    private static final Logger LOG = LoggerFactory
            .getLogger(CoreService.class);
    private static CoreService instance;

    private CoreService() {
    }

    public static CoreService getInstance() {
        if (instance == null) {
            synchronized (GatherService.class) {
                if (instance == null) {
                    instance = new CoreService();
                }
            }
        }
        return instance;
    }

    public void findSource(ServletResponse response, String srcPath) throws IOException  {
        String path = CoreEngine.getInstance().getConfig().getPathSource()+srcPath;
        String res;
        try {
            res= new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            res = "FileNotFound  source "+path;
            LOG.error("FileNotFound source "+path+" "+e);
        }
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        out.println(res);
        out.close();
    }

    public void generateReport(ServletResponse response, String customGeneratorClass) throws IOException {
        Collection<LeechService> leechServiceRegistered = CoreEngine.getInstance().getLeechServiceRegistered();
        CoreEngine.getInstance().leech();
        if (customGeneratorClass != null && !"".equals(customGeneratorClass)) {
            generateReportWithCustomGenerator(response, customGeneratorClass, leechServiceRegistered);
        } else {
            generateReportWithDefaultGenerator(response, leechServiceRegistered);
        }
    }

    private void generateReportWithDefaultGenerator(ServletResponse response,
                                                    Collection<LeechService> leechServiceRegistered) throws IOException {
        PrintWriter out = response.getWriter();
        LOG.info("No class generator - using default json");
        out.println(generateJSon(leechServiceRegistered));
        response.setContentType("application/json");
        out.close();
    }

    private void generateReportWithCustomGenerator(ServletResponse response,
                                                   String customGeneratorClass,
                                                   Collection<LeechService> leechServiceRegistered) throws IOException {
        PrintWriter out = response.getWriter();
        LOG.debug("HTML Response");
        response.setContentType("text/html");
        TheJack gen;
        try {
            gen = (TheJack) Class.forName(customGeneratorClass).newInstance();
            out.println(gen.generatePage(leechServiceRegistered));
        } catch (InstantiationException | IllegalAccessException
                | ClassNotFoundException e) {
            LOG.error("Can't generate page using " + customGeneratorClass, e);
            out.println(generateJSon(leechServiceRegistered));
            response.setContentType("application/json");
        }
        out.close();
    }

    public void enableEntryPointCoverage(ServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();
        try {
            CoreEngine.getInstance().enableEntryPointCoverage();
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
