package com.highway2urhell.transformer;

import com.highway2urhell.CoreEngine;
import com.highway2urhell.domain.EntryPathData;
import com.highway2urhell.domain.EntryPathParam;
import com.highway2urhell.domain.TypeParam;
import com.highway2urhell.domain.TypePath;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import org.apache.struts2.dispatcher.Dispatcher;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by neoverflow on 19/11/15.
 */
public class Struts2PrepareCollector {

    public static void collect(Dispatcher dispatcher) {
        List listEntryPath = new ArrayList();
        ConfigurationManager cm = dispatcher.getConfigurationManager();
        Configuration cf = cm.getConfiguration();
        Collection colPackages = cf.getPackageConfigs().values();
        if (colPackages != null) {
            Iterator ite = colPackages.iterator();
            while (ite.hasNext()) {
                PackageConfig pack = (PackageConfig) ite.next();
                Collection colActionConfigs = pack.getActionConfigs().values();
                Iterator iteCol = colActionConfigs.iterator();
                while (iteCol.hasNext()) {
                    ActionConfig action = (ActionConfig) iteCol.next();
                    if (action.getClassName() != null && !"".equals(action.getClassName())) {
                        try {
                            Class c = Class.forName(action.getClassName());
                            Method[] tabM = c.getDeclaredMethods();
                            for (int i = 0; i < tabM.length; i++) {
                                String scope = Modifier.toString(tabM[i].getModifiers());
                                if (scope.startsWith("public") && !"wait".equals(tabM[i].getName())
                                        && !"notifyall".equals(tabM[i].getName().toLowerCase())
                                        && !"notify".equals(tabM[i].getName().toLowerCase())
                                        && !"getclass".equals(tabM[i].getName().toLowerCase())
                                        && !"equals".equals(tabM[i].getName().toLowerCase())
                                        && !"tostring".equals(tabM[i].getName().toLowerCase())
                                        && !"wait".equals(tabM[i].getName().toLowerCase())
                                        && !"hashcode".equals(tabM[i].getName().toLowerCase())) {
                                    Method m = tabM[i];
                                    EntryPathData entry = new EntryPathData();
                                    entry.setTypePath(TypePath.DYNAMIC);
                                    entry.setClassName(action.getClassName());
                                    entry.setMethodName(m.getName());
                                    entry.setUri(action.getName());
                                    entry.setSignatureName(org.objectweb.asm.Type.getMethodDescriptor(m));
                                    List listEntryPathData = new ArrayList();
                                    for (int j = 0; j < m.getParameterTypes().length; j++) {
                                        EntryPathParam param = new EntryPathParam();
                                        param.setKey("");
                                        param.setTypeParam(TypeParam.PARAM_DATA);
                                        param.setValue(m.getParameterTypes()[j].getName());
                                        listEntryPathData.add(param);
                                    }
                                    entry.setListEntryPathData(listEntryPathData);
                                    listEntryPath.add(entry);
                                }
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        CoreEngine.getInstance().getFramework("STRUTS_2").receiveData(listEntryPath);
    }
}
