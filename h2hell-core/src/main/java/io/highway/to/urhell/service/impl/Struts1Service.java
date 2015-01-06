package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.VersionUtils;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.TypePath;
import io.highway.to.urhell.service.AbstractLeechService;
import org.apache.commons.digester.Digester;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.impl.ModuleConfigImpl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Struts1Service extends AbstractLeechService {

    public static final String FRAMEWORK_NAME = "STRUTS_1";

    public Struts1Service() {
        super(FRAMEWORK_NAME, VersionUtils.getVersion(
                "org.apache.struts.config.impl.ModuleConfigImpl", "struts",
                "struts"));
    }

    public void gatherData(Object dataIncoming) {
        if (!getFrameworkInformations().getVersion().equals(
                VersionUtils.NO_FRAMEWORK)) {
            Digester configDigester = (Digester) dataIncoming;
            ModuleConfigImpl m = (ModuleConfigImpl) configDigester.getRoot();
            Field f;
            try {
                f = m.getClass().getDeclaredField("actionConfigList");
                f.setAccessible(true);
                List<ActionMapping> res = (ArrayList) f.get(m);
                if (res != null) {
                    for (ActionMapping action : res) {
                        EntryPathData entry = new EntryPathData();
                        entry.setClassName(action.getName());
                        if (action.getPrefix() != null
                                && !"null".equals(action.getPrefix())) {
                            entry.setUri(action.getPrefix() + action.getPath());
                        } else {
                            entry.setUri(action.getPath());
                        }
                        entry.setTypePath(TypePath.DYNAMIC);
                        entry.setMethodEntry(action.getInput());
                        addEntryPath(entry);
                    }
                }

            } catch (Exception e) {
                LOGGER.error("Exception in "
                        + Struts1Service.class.getCanonicalName()
                        + " receiveData " + dataIncoming + " msg :"
                        + e.getMessage());
            }
        }
    }

}
