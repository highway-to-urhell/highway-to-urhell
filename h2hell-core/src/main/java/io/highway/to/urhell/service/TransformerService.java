package io.highway.to.urhell.service;

import io.highway.to.urhell.domain.BreakerData;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.service.LeechService;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransformerService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	public void transformAllClassScanByH2h(Instrumentation inst,Set<String> entryClassName){
		for(String classNameNormalized : entryClassName){
			String className = classNameNormalized.replaceAll("/", ".");
			LOGGER.error("Transform class {}",className);
			transformOneClass(inst, className);
		}
		
	}
	
	private void transformOneClass(Instrumentation inst,String className){
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    	try {
			inst.retransformClasses(classLoader.loadClass(className));
		} catch (ClassNotFoundException | UnmodifiableClassException e) {
			LOGGER.error("Error while transform Class {} msg {}",className,e);
		}
	}
	
	public Map<String, List<BreakerData>> transformDataH2h(Collection<LeechService> leechService){
		 Map<String, List<BreakerData>> mapToTransform = new HashMap<String, List<BreakerData>>();
		 for(LeechService leech : leechService){
			 for(EntryPathData entryPath : leech.getFrameworkInformations().getListEntryPath()){
				 if (!entryPath.getMethodEntry().equals("")) {
		                BreakerData bd = new BreakerData();
		                bd.setClassName(entryPath.getClassName());
		                bd.setMethodName(entryPath.getMethodEntry());
		                String classNameNormalized = entryPath.getClassName().replaceAll("\\.", "/");
		                bd.setClassNameNormalized(classNameNormalized);
		                bd.setSignatureName(entryPath.getSignatureName());
		                bd.setTypePath(entryPath.getTypePath().toString());
		                List<BreakerData> listbd = mapToTransform.get(classNameNormalized);
		                if (listbd == null) {
		                    //first time
		                    listbd = new ArrayList<BreakerData>();
		                    listbd.add(bd);
		                    mapToTransform.put(classNameNormalized, listbd);
		                } else {
		                    listbd.add(bd);
		                }
		            }
			 }
		 }
		 
		 return mapToTransform;
	}
	
	public List<BreakerData> transforDataH2hToList(Collection<LeechService> leechService){
		List<BreakerData> listBreaker = new ArrayList<BreakerData>();
		for(LeechService leech : leechService){
			 for(EntryPathData entryPath : leech.getFrameworkInformations().getListEntryPath()){
				 if (!entryPath.getMethodEntry().equals("")) {
		                BreakerData bd = new BreakerData();
		                bd.setClassName(entryPath.getClassName());
		                bd.setMethodName(entryPath.getMethodEntry());
		                String classNameNormalized = entryPath.getClassName().replaceAll("\\.", "/");
		                bd.setClassNameNormalized(classNameNormalized);
		                bd.setSignatureName(entryPath.getSignatureName());
		                bd.setUri(entryPath.getUri());
		                bd.setHttpMethod(entryPath.getHttpMethod().toString());
		                listBreaker.add(bd);
		            }
			 }
		}
		return listBreaker;
	}
	
}
