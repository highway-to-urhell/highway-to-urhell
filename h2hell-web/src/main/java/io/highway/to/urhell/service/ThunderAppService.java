package io.highway.to.urhell.service;

import io.highway.to.urhell.dao.ThunderAppDao;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.H2hConfig;
import io.highway.to.urhell.domain.ThunderApp;
import io.highway.to.urhell.exception.NotExistThunderAppException;
import io.highway.to.urhell.exception.TokenException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.transaction.annotation.Transactional;

@Named
public class ThunderAppService {

	@Inject
	private ThunderAppDao thunderAppDao;
	@Inject
	private ThunderStatService thunderStatService;

	@Transactional
	public List<ThunderApp> findAll() {
		return thunderAppDao.findAll();
	}

	@Transactional
	public String createThunderApp(H2hConfig config) {
		ThunderApp th = new ThunderApp();
		th.setVersionApp(config.getVersionApp());
		th.setNameApp(config.getNameApplication());
		th.setUrlApp(config.getUrlApplication());
		String token = RandomStringUtils.randomAlphanumeric(16).toUpperCase();
		th.setToken(token);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy:hh-mm-ss");
		th.setDateCreation(sdf.format(new Date()));
		th.setDescription(config.getDescription());
		th.setPathSource(config.getPathSource());
		thunderAppDao.save(th);
		return token;
	}

	@Transactional
	public void initThunderAppAndStat(String token,
			List<EntryPathData> listEntryPathData) {
		ThunderApp ta = validateToken(token);
		if (listEntryPathData != null && !listEntryPathData.isEmpty()) {
			for (EntryPathData entry : listEntryPathData) {
				thunderStatService.createOrUpdateThunderStat(entry, ta);
			}
		}
	}

	public ThunderApp findAppByToken(String token) {
		return validateToken(token) ;
	}
	
	private ThunderApp validateToken(String token) {
		if (token == null) {
			throw new TokenException();
		}
		ThunderApp ta = thunderAppDao.findByToken(token);
		if (ta == null) {
			throw new NotExistThunderAppException();
		}
		return ta;
	}

}
