package io.highway.to.urhell.service;

import io.highway.to.urhell.dao.ThunderAppDao;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.H2hConfig;
import io.highway.to.urhell.domain.ThunderApp;
import io.highway.to.urhell.exception.NotExistThunderAppException;
import io.highway.to.urhell.exception.TokenException;
import io.highway.to.urhell.rest.domain.MessageGlobalStat;
import io.highway.to.urhell.rest.domain.MessageType;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Named
public class ThunderAppService {
	private static final Logger LOG = LoggerFactory
			.getLogger(ThunderAppService.class);
	@Inject
	private ThunderAppDao thunderAppDao;
	@Inject
	private ThunderStatService thunderStatService;

	@Transactional
	public List<ThunderApp> findAll() {
		return thunderAppDao.findAll();
	}
	@Transactional
	public MessageGlobalStat findMessageGlobalStat(){
		MessageGlobalStat mg = new MessageGlobalStat();
		List<ThunderApp> listThunderApp = thunderAppDao.findAll();
		mg.setNumberApplications(listThunderApp.size());
		Integer total = 0;
		for (ThunderApp app : listThunderApp) {
			total = total + app.getThunderStatSet().size();
		}
		mg.setNumberEntriesPoint(total);
		return mg;
	}
	
	public Collection<MessageType> findMessageType(){
		Map<String,MessageType> map = new HashMap<String,MessageType>();
		List<ThunderApp> listTa = thunderAppDao.findAll();
		for(ThunderApp ta : listTa){
			MessageType me = map.get(ta.getTypeAppz());
			LOG.error(ta.getTypeAppz()+"-"+map.keySet().size());
			if(me==null){
				MessageType meNew = new MessageType();
				meNew.setTypeApplication(ta.getTypeAppz());
				meNew.setNb(1);
				map.put(ta.getTypeAppz(), meNew);
			}else{
				Integer total = me.getNb();
				me.setNb(total+1);
			}
		}
		return map.values();
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
		if(config.getTypeAppz()!=null){
			th.setTypeAppz(config.getTypeAppz());
		}else{
			th.setTypeAppz("UNKNOWN");
		}
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
