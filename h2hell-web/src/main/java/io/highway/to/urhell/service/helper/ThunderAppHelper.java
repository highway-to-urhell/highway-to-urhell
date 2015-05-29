package io.highway.to.urhell.service.helper;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

@Named
public class ThunderAppHelper {
	
	private static Map<String,String> mapTypeConversion = new HashMap<String,String>();
	
	
	public static Map<String, String> getMapTypeConversion() {
		if(mapTypeConversion == null){
			synchronized (ThunderAppHelper.class) {
				mapTypeConversion.put("java", ".java");
				mapTypeConversion.put("php", ".php");
				mapTypeConversion.put("nodejs", ".js");
			}
		}
		return mapTypeConversion;
	}

}
