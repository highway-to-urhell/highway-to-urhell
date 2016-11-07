package com.highway2urhell.collector;

import com.highway2urhell.CoreEngine;
import com.highway2urhell.domain.EntryPathData;
import com.highway2urhell.domain.EntryPathParam;
import com.highway2urhell.domain.TypeParam;
import org.apache.struts.action.Action;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Struts1WithSpringCollector {
    public static void collectBody(WebApplicationContext webApplicationContext) {
        List listEntryPath = new ArrayList();
        if (webApplicationContext != null &&
                webApplicationContext.getBeanDefinitionNames() != null &&
                webApplicationContext.getBeanDefinitionNames().length > 0) {
            for (int i = 0; i < webApplicationContext.getBeanDefinitionNames().length; i++) {
                String tmp = webApplicationContext.getBeanDefinitionNames()[i];
                if (tmp.startsWith("/")) {
                    Action toAdd = (Action) webApplicationContext.getBean(tmp, Action.class);
                    try {
                        Class c = Class.forName(toAdd.getClass().getName());
                        Method[] tabDeclared = c.getDeclaredMethods();
                        for (int m = 0; i < tabDeclared.length; i++) {
                            EntryPathData entry = new EntryPathData();
                            String resSignature = org.objectweb.asm.Type.getMethodDescriptor(tabDeclared[i]);
                            entry.setUri(tmp);
                            entry.setClassName(toAdd.getClass().getName());
                            entry.setMethodName(tabDeclared[m].getName());
                            entry.setSignatureName(resSignature);
                            List listEntryPathData = new ArrayList();
                            for (int j = 0; j < tabDeclared[m].getParameterTypes().length; j++) {
                                EntryPathParam param = new EntryPathParam();
                                param.setKey("");
                                param.setTypeParam(TypeParam.PARAM_DATA);
                                param.setValue(tabDeclared[m].getParameterTypes()[j].getName());
                                listEntryPathData.add(param);
                            }
                            entry.setListEntryPathData(listEntryPathData);
                            System.err.println(entry.toString());
                            listEntryPath.add(entry);
                        }
                    } catch (ClassNotFoundException e) {
                        System.err.println("Error on invoke " + toAdd.getClass().getName());
                        e.printStackTrace();
                    }
                }
            }
        }
        CoreEngine.getInstance().getFramework("STRUTS_SPRING_1").receiveData(listEntryPath);
    }
}
