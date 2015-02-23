package io.highway.to.urhell.service;

import io.highway.to.urhell.dao.ThunderAppDao;
import io.highway.to.urhell.domain.ThunderApp;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

@Named
public class LaunchService {
	private static final Logger LOG = LoggerFactory
			.getLogger(LaunchService.class);
	@Inject
	private ThunderAppDao thunderAppDao;

	public String launchAnalysis(String token) {
		RestTemplate restTemplate = new RestTemplate();
		ThunderApp th = thunderAppDao.findByToken(token);
		if (th == null) {
			return "No config for application with token" + token;
		}
		String url =th.getUrlApp()+"h2h/?launch=true";
		LOG.info("Call app {}",url);
		String responseEntity = restTemplate.postForObject(url, null, String.class);
		return responseEntity;
	}

}
