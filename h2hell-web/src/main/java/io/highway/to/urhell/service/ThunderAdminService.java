package io.highway.to.urhell.service;

import io.highway.to.urhell.dao.BreakerLogDao;
import io.highway.to.urhell.dao.ThunderAppDao;
import io.highway.to.urhell.dao.ThunderStatDao;
import io.highway.to.urhell.domain.BreakerLog;
import io.highway.to.urhell.domain.ThunderApp;
import io.highway.to.urhell.domain.ThunderStat;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Transactional;

@Named
public class ThunderAdminService {
	@Inject
	private BreakerLogDao breakerLogDao;
	@Inject
	private ThunderStatDao thunderStatDao;
	@Inject
	private ThunderAppDao thunderAppDao;
	

	@Transactional
	public void purgeStatByToken(String token) {
		List<ThunderStat> liststat = thunderStatDao.findByToken(token);
		for(ThunderStat ts : liststat){
			ts.setCount(new Long(0));
			thunderStatDao.save(ts);
		}
		purgeBreakerByToken(token);
	}

	@Transactional
	public void purgeBreakerByToken(String token) {
		List<BreakerLog> listBreaker = breakerLogDao.findByToken(token);
		breakerLogDao.deleteInBatch(listBreaker);
	}
	
	@Transactional
	public void deleteThunderAppByToken(String token) {
		ThunderApp ta = thunderAppDao.findByToken(token);
		thunderAppDao.delete(ta);
		purgeBreakerByToken(token);
		purgeStatByToken(token);
		
	}
	
	

}
