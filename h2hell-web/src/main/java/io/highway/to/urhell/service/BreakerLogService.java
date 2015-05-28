package io.highway.to.urhell.service;

import io.highway.to.urhell.dao.BreakerLogDao;
import io.highway.to.urhell.dao.ThunderAppDao;
import io.highway.to.urhell.domain.BreakerLog;
import io.highway.to.urhell.domain.MessageBreaker;
import io.highway.to.urhell.domain.ThunderApp;
import io.highway.to.urhell.exception.DateIncomingException;
import io.highway.to.urhell.exception.PathNameException;
import io.highway.to.urhell.exception.TokenException;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Named
public class BreakerLogService {
	private static final Logger LOG = LoggerFactory.getLogger(BreakerLogService.class);
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

	public void addListBreaker(List<MessageBreaker> listBreaker) {
		if (listBreaker != null && !listBreaker.isEmpty()) {
			for (MessageBreaker msg : listBreaker) {
				LOG.info(
						"Call addBreaker with pathClassMethodName {} token {} and dateIncoming {}",
						msg.getPathClassMethodName(), msg.getToken(),
						msg.getDateIncoming());
				addBreaker(msg.getPathClassMethodName(), msg.getToken(),
						msg.getDateIncoming());
			}
		}
	}

	private void addBreaker(String pathClassMethodName, String token,
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
