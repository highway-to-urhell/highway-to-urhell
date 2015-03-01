package io.highway.to.urhell.service;

import io.highway.to.urhell.dao.ThunderAppDao;
import io.highway.to.urhell.domain.ThunderApp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class ThunderLocalSourceService {
	private static final Logger LOG = LoggerFactory
			.getLogger(ThunderLocalSourceService.class);
	@Inject
	private ThunderAppDao thunderAppDao;
	
	public String findClassAndMethod(String token,String classAndMethod){
		String className = extractClass(classAndMethod);
		String res =null;
		ThunderApp th  =thunderAppDao.findByToken(token); 
		if(th == null){
			return "no Application";
		}
		String path = th.getPathSource()+className+".java";
		try {
			res= new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
		} catch (IOException e) {
			res = "FileNotFound  source "+path;
			LOG.error("FileNotFound source "+path+" "+e);
		}
		return res;
	}

	private String extractClass(String classAndMethod){
		String[] tabClass = classAndMethod.split("\\.");
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<tabClass.length-1;i++){
			sb.append("/");
			sb.append(tabClass[i]);
		}
		
		return sb.toString();
	}
	
}
