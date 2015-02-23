package io.highway.to.urhell.service;

import io.highway.to.urhell.dao.BreakerLogDao;
import io.highway.to.urhell.dao.ThunderAppDao;
import io.highway.to.urhell.domain.BreakerLog;
import io.highway.to.urhell.domain.ThunderApp;
import io.highway.to.urhell.exception.DateIncomingException;
import io.highway.to.urhell.exception.PathNameException;
import io.highway.to.urhell.exception.TokenException;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Transactional;

@Named
public class BreakerLogService {

	@Inject
	private BreakerLogDao breakerLogDao;
	@Inject
	private ThunderAppDao thunderAppDao;

	@Transactional
	public void createBreakerLog(ThunderApp th, String pathClassMethodName,
			String dateIncoming) {
		BreakerLog breaker = new BreakerLog();
		breaker.setDateIncoming(dateIncoming);
		breaker.setPathClassMethodName(pathClassMethodName);
		breaker.setToken(th.getToken());
		breakerLogDao.save(breaker);
	}

	public void addBreaker(String pathClassMethodName, String token,
			String dateIncoming) {
		validate(pathClassMethodName, token, dateIncoming);
		ThunderApp th = thunderAppDao.findByToken(token);
		// Add breaker log Servive
		createBreakerLog(th, pathClassMethodName, dateIncoming);
	}

	private void validate(String pathClassMethodName, String token,
			String dateIncoming) {
		if (pathClassMethodName == null) {
			throw new PathNameException();
		}
		if (token == null) {
			throw new TokenException();
		}
		if (dateIncoming == null) {
			throw new DateIncomingException();
		}

	}

}
