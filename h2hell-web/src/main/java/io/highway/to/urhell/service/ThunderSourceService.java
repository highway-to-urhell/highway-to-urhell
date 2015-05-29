package io.highway.to.urhell.service;

import io.highway.to.urhell.dao.ThunderAppDao;
import io.highway.to.urhell.domain.ThunderApp;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.LoggerFactory;

@Named
public class ThunderSourceService {
	private static final Logger LOG = LoggerFactory
			.getLogger(ThunderSourceService.class);
	@Inject
	private ThunderAppDao thunderAppDao;

	public String findSource(String token,String classAndMethod) {
		String className = extractClass(classAndMethod);
		String res =null;
		ThunderApp th  =thunderAppDao.findByToken(token); 
		if(th == null){
			return "no Application";
		}
		String path = className+".java";

		RestTemplate restTemplate = new RestTemplate();
		String url = th.getUrlApp() + "h2h/?srcPath="+path;
		String responseEntity = null;
		LOG.info("Call app for find source {}", url);
		try {
			responseEntity = restTemplate
					.getForObject(url, String.class);
			
		} catch (RestClientException r) {
			LOG.error("error call {} exception {}", url,r);
			responseEntity = r.getMessage();
		}
		LOG.info("result call {}", responseEntity);
		return responseEntity;
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
