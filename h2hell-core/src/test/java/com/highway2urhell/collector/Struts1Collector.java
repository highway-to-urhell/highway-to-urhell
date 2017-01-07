package com.highway2urhell.collector;

import com.highway2urhell.CoreEngine;
import com.highway2urhell.domain.EntryPathData;
import com.highway2urhell.domain.EntryPathParam;
import com.highway2urhell.domain.TypeParam;
import com.highway2urhell.domain.TypePath;
import org.apache.commons.digester.Digester;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.impl.ModuleConfigImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Struts1Collector {
    public static void collectBody(Digester configDigester) {
        ModuleConfigImpl m = (ModuleConfigImpl) configDigester.getRoot();
        Field f;
        List listEntryPath = new ArrayList();
        try {
            f = m.getClass().getDeclaredField("actionConfigList");
            f.setAccessible(true);
            List res = (ArrayList) f.get(m);
            if (res != null) {
                Iterator iter = res.iterator();
                while (iter.hasNext()) {
                    ActionMapping action = (ActionMapping) iter.next();
                    if (action.getType() != null && !"".equals(action.getType())) {
                        try {
                            Class c = Class.forName(action.getType());
                            Method[] tabMet = c.getDeclaredMethods();
                            for (int i = 0; i < tabMet.length; i++) {
                                EntryPathData entry = new EntryPathData();
                                entry.setClassName(action.getType());
                                entry.setMethodName(tabMet[i].getName());
                                if (action.getPrefix() != null && !"null".equals(action.getPrefix())) {
                                    entry.setUri(action.getPrefix() + action.getPath());
                                } else {
                                    entry.setUri(action.getPath());
                                }
                                entry.setTypePath(TypePath.DYNAMIC);
                                List listEntryPathData = new ArrayList();
                                for (int j = 0; j < tabMet[i].getParameterTypes().length; j++) {
                                    EntryPathParam param = new EntryPathParam();
                                    param.setKey("");
                                    param.setTypeParam(TypeParam.PARAM_DATA);
                                    param.setValue(tabMet[i].getParameterTypes()[j].getName());
                                    listEntryPathData.add(param);
                                }
                                entry.setListEntryPathData(listEntryPathData);
                                entry.setSignatureName(org.objectweb.asm.Type.getMethodDescriptor(tabMet[i]));
                                listEntryPath.add(entry);
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        CoreEngine.getInstance().getFramework("STRUTS_1").receiveData(listEntryPath);
    }
}
