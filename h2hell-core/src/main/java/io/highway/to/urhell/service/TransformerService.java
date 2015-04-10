package io.highway.to.urhell.service;

import io.highway.to.urhell.domain.BreakerData;
import io.highway.to.urhell.domain.EntryPathData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.*;

public class TransformerService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public void transformAllClassScanByH2h(Instrumentation inst,
                                           Set<String> entryClassName) {
        for (String classNameNormalized : entryClassName) {
            String className = classNameNormalized.replaceAll("/", ".");
            LOGGER.error("Transform class {}", className);
            transformOneClass(inst, className);
        }

    }

    private void transformOneClass(Instrumentation inst, String className) {
        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
        try {
            inst.retransformClasses(classLoader.loadClass(className));
        } catch (ClassNotFoundException | UnmodifiableClassException e) {
            LOGGER.error("Error while transform Class {} msg {}", className, e);
        }
    }

    public Map<String, List<BreakerData>> transformDataH2h(
            Collection<LeechService> leechService) {
        Map<String, List<BreakerData>> mapToTransform = new HashMap<String, List<BreakerData>>();
        for (LeechService leech : leechService) {
            for (EntryPathData entryPath : leech.getFrameworkInformations()
                    .getListEntryPath()) {
                if (entryPath.getMethodName() != null) {
                    switch (entryPath.getTypePath()) {
                        case SERVLET:
                            createRegistrerBreakerData(
                                    entryPath,
                                    mapToTransform,
                                    "doGet",
                                    "(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;)V");
                            createRegistrerBreakerData(
                                    entryPath,
                                    mapToTransform,
                                    "doPost",
                                    "(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;)V");
                            createRegistrerBreakerData(
                                    entryPath,
                                    mapToTransform,
                                    "service",
                                    "(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;)V");
                            break;
                        case FILTER:
                            createRegistrerBreakerData(
                                    entryPath,
                                    mapToTransform,
                                    "doFilter",
                                    "(Ljavax/servlet/ServletRequest;Ljavax/servlet/ServletResponse;Ljavax/servlet/FilterChain;)V");
                            break;
                        default:
                            createRegistrerBreakerData(entryPath, mapToTransform,
                                    entryPath.getMethodName(),
                                    entryPath.getSignatureName());
                            break;
                    }
                }
            }
        }

        return mapToTransform;
    }

    private void createRegistrerBreakerData(EntryPathData entryPath,
                                            Map<String, List<BreakerData>> mapToTransform, String methodName,
                                            String signatureName) {
        BreakerData bd = new BreakerData();
        bd.setClassName(entryPath.getClassName());
        bd.setMethodName(methodName);
        String classNameNormalized = entryPath.getClassName().replaceAll("\\.",
                "/");
        bd.setClassNameNormalized(classNameNormalized);
        bd.setSignatureName(signatureName);
        bd.setTypePath(entryPath.getTypePath().toString());
        List<BreakerData> listbd = mapToTransform.get(classNameNormalized);
        if (listbd == null) {
            // first time
            listbd = new ArrayList<BreakerData>();
            listbd.add(bd);
            mapToTransform.put(classNameNormalized, listbd);
        } else {
            listbd.add(bd);
        }
    }

    public List<BreakerData> transforDataH2hToList(
            Collection<LeechService> leechService) {
        List<BreakerData> listBreaker = new ArrayList<BreakerData>();
        for (LeechService leech : leechService) {
            for (EntryPathData entryPath : leech.getFrameworkInformations()
                    .getListEntryPath()) {
                if (entryPath.getMethodName() != null) {
                    switch (entryPath.getTypePath()) {
                        case SERVLET:
                            listBreaker.add(createBreakerData(entryPath, "doGet", "(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;)V"));
                            listBreaker.add(createBreakerData(entryPath, "doPost", "(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;)V"));
                            listBreaker.add(createBreakerData(entryPath, "service", "(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;)V"));
                            break;
                        case FILTER:
                            listBreaker.add(createBreakerData(entryPath, "doFilter", "(Ljavax/servlet/ServletRequest;Ljavax/servlet/ServletResponse;Ljavax/servlet/FilterChain;)V"));
                            break;
                        default:
                            listBreaker.add(createBreakerData(entryPath, entryPath.getMethodName(), entryPath.getSignatureName()));
                            break;
                    }
                }
            }
        }
        return listBreaker;
    }

    private BreakerData createBreakerData(EntryPathData entryPath,
                                          String methodName, String signatureName) {
        BreakerData bd = new BreakerData();
        bd.setClassName(entryPath.getClassName());
        bd.setMethodName(methodName);
        String classNameNormalized = entryPath.getClassName().replaceAll("\\.",
                "/");
        bd.setClassNameNormalized(classNameNormalized);
        bd.setSignatureName(signatureName);
        bd.setUri(entryPath.getUri());
        bd.setHttpMethod(entryPath.getHttpMethod().toString());
        return bd;
    }

}
