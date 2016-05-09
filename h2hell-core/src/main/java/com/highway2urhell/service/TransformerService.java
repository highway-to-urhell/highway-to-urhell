package com.highway2urhell.service;

import com.highway2urhell.domain.EntryPathData;
import com.highway2urhell.domain.FilterEntryPath;
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
        } catch (ClassNotFoundException e) {
            LOGGER.error("Error while transform Class {} msg {}", className, e);
        } catch (UnmodifiableClassException e) {
            LOGGER.error("Error while transform Class {} msg {}", className, e);
        }
    }

    private Boolean filterEntry(FilterEntryPath filterEntryPath, EntryPathData entryPath) {
        if (filterEntryPath.getFilter()) {
            return true;
        }
        if (filterEntryPath.getClassMethod()) {
            return filterEntryPath.getListFilter().contains(entryPath.getClassName() + "." + entryPath.getMethodName());
        }
        if (filterEntryPath.getClassOnly()) {
            return filterEntryPath.getListFilter().contains(entryPath.getClassName());
        }
        if (filterEntryPath.getPackageOnly()) {
            String[] tabPackage = entryPath.getClassName().split("\\.");
            StringBuilder packageName = new StringBuilder();
            for (int i = 0; i < tabPackage.length - 1; i++) {
                packageName.append(tabPackage[i]);
            }
            return filterEntryPath.getListFilter().contains(packageName);
        }
        return true;
    }

    public Map<String, List<EntryPathData>> transformDataFromLeechPluginForTransformation(
            Collection<LeechService> leechService, FilterEntryPath filterEntryPath) {
        Map<String, List<EntryPathData>> mapToTransform = new HashMap<String, List<EntryPathData>>();
        for (LeechService leech : leechService) {
            for (EntryPathData entryPath : leech.getFrameworkInformations()
                    .getListEntryPath()) {
                if (entryPath.getMethodName() != null && entryPath.getAudit() && filterEntry(filterEntryPath, entryPath)) {
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
                                            Map<String, List<EntryPathData>> mapToTransform, String methodName,
                                            String signatureName) {
        EntryPathData entry = new EntryPathData();
        entry.setClassName(entryPath.getClassName());
        entry.setMethodName(methodName);
        String classNameNormalized = entryPath.getClassName().replaceAll("\\.",
                "/");
        entry.setClassNameNormalized(classNameNormalized);
        entry.setSignatureName(signatureName);
        entry.setTypePath(entryPath.getTypePath());
        List<EntryPathData> listEntry = mapToTransform.get(classNameNormalized);
        if (listEntry == null) {
            // first time
            listEntry = new ArrayList<EntryPathData>();
            listEntry.add(entry);
            mapToTransform.put(classNameNormalized, listEntry);
        } else {
            listEntry.add(entry);
        }
    }

    public List<EntryPathData> collectBreakerDataFromLeechPlugin(
            Collection<LeechService> leechService) {
        List<EntryPathData> listBreaker = new ArrayList<EntryPathData>();
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

    private EntryPathData createBreakerData(EntryPathData entryPath,
                                            String methodName, String signatureName) {
        entryPath.setMethodName(methodName);
        String classNameNormalized = entryPath.getClassName().replaceAll("\\.",
                "/");
        entryPath.setClassNameNormalized(classNameNormalized);
        entryPath.setSignatureName(signatureName);
        return entryPath;
    }
}
