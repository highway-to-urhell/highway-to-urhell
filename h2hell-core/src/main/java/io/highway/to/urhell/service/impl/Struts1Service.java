package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.VersionUtils;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.EntryPathParam;
import io.highway.to.urhell.domain.TypeParam;
import io.highway.to.urhell.domain.TypePath;
import io.highway.to.urhell.service.AbstractLeechService;

import org.apache.commons.digester.Digester;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.impl.ModuleConfigImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
                List<ActionMapping> res= (ArrayList<ActionMapping>) f.get(m);
                if (res != null) {
                    for (ActionMapping action : res) {
                        findEntryPathDataStruts(action);
                        
                    }
                }

            } catch (Exception e) {
                LOGGER.error("Exception in "
                        + Struts1Service.class.getCanonicalName()
                        + " receiveData " + dataIncoming + " msg :"
                        + e);
            }
        }
    }
    
    private void findEntryPathDataStruts(ActionMapping action){
    	
    	try {
			Class<?> c = Class.forName(action.getType());
			for(Method m : c.getDeclaredMethods()){
				EntryPathData res = new EntryPathData();
		    	res.setClassName(action.getType());
		    	res.setMethodEntry(m.getName());
		    	if (action.getPrefix() != null
		                && !"null".equals(action.getPrefix())) {
		        	res.setUri(action.getPrefix() + action.getPath());
		        } else {
		        	res.setUri(action.getPath());
		        }
		        res.setTypePath(TypePath.DYNAMIC);
		        res.setListEntryPathData(searchParameterMethod(m.getParameterTypes()));
		        res.setSignatureName(getInternalSignature(m));
		        addEntryPath(res);
			}
			
			
		} catch (ClassNotFoundException e) {
			 LOGGER.error("Exception in "
                     + Struts1Service.class.getCanonicalName()
                     + " entryClass " + action.getType() + " msg :"
                     + e);
		}
    	
    }
    
    private List<EntryPathParam> searchParameterMethod(Class<?>[] tabParam){
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
