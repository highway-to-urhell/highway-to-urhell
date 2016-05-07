package com.highway2urhell.service.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.highway2urhell.VersionUtils;
import com.highway2urhell.domain.EntryPathData;
import com.highway2urhell.domain.EntryPathParam;
import com.highway2urhell.domain.TypeParam;
import com.highway2urhell.domain.TypePath;
import com.highway2urhell.service.AbstractLeechService;


public class JaxRsService extends AbstractLeechService {

    public static final String FRAMEWORK_NAME = "JAX_RS";

    public JaxRsService() {
        super(FRAMEWORK_NAME, VersionUtils.getVersion(
                "javax.ws.rs.core.Application", "javax.ws.rs",
                "javax.ws.rs-api"));
        setTriggerAtStartup(false);
    }

    @Override
    protected void gatherData(List<EntryPathData> incoming) {
        if (!getFrameworkInformations().getVersion().equals(
                VersionUtils.NO_FRAMEWORK)) {
            // scan
            LOGGER.info("Start Scan reflections JAX-RS ! ");
            Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forClassLoader()));
            LOGGER.info("End Scan reflections JAX-RS ! ");
            Set<Class<?>> setPathJAXRS = reflections.getTypesAnnotatedWith(Path.class);
            if (setPathJAXRS != null && !setPathJAXRS.isEmpty()) {
                // Grab all class extends
                for (Class<?> service : setPathJAXRS) {
                    // search annotation type javax.ws.rs.Path
                    for (Annotation annotation : service.getAnnotations()) {
                        if (annotation instanceof Path) {
                            Path remoteAnnotation = (Path) annotation;
                            searchAllMethodPublic(remoteAnnotation, service);
                        }
                    }
                }
            }
        }
    }

    private void searchAllMethodPublic(Path remoteAnnotation, Class<?> service) {
        String pathForClass = remoteAnnotation.value();
        // Search all method public with annotation Path
        if (service.getDeclaredMethods().length > 0) {
            for (Method m : service.getDeclaredMethods()) {
                Path pMethod = m.getAnnotation(Path.class);
                if (pMethod != null) {
                    addEntryPath(getEntryForClassJAXRS(pathForClass, m, service.getName(), pMethod));
                }
            }
        }
    }

    private EntryPathData getEntryForClassJAXRS(String pathClass, Method m,
                                                String nameClass, Path pMethod) {
        EntryPathData entry = new EntryPathData();
        // method
        if (m.getAnnotation(GET.class) != null) {
            entry.setHttpMethod("GET");
        } else if (m.getAnnotation(POST.class) != null) {
            entry.setHttpMethod("POST");
        } else if (m.getAnnotation(PUT.class) != null) {
            entry.setHttpMethod("PUT");
        } else if (m.getAnnotation(DELETE.class) != null) {
            entry.setHttpMethod("DELETE");
        } else {
            entry.setHttpMethod("");
        }
        //
        entry.setUri(pathClass + pMethod.value());
        entry.setClassName(nameClass);
        entry.setMethodName(m.getName());
        entry.setTypePath(TypePath.DYNAMIC);
        entry.setSignatureName(getInternalSignature(m));
        entry.setListEntryPathData(searchParameterMethod(m.getParameterTypes()));
        return entry;
    }

    private List<EntryPathParam> searchParameterMethod(Class<?>[] tabParam) {
        List<EntryPathParam> listEntryPathData = new ArrayList<EntryPathParam>();
        for (Class<?> mMethod : tabParam) {
            EntryPathParam param = new EntryPathParam();
            param.setKey("");
            param.setTypeParam(TypeParam.PARAM_DATA);
            param.setValue(mMethod.getName());
            listEntryPathData.add(param);
        }
        return listEntryPathData;
    }
}
