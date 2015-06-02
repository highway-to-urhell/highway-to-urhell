package io.highway.to.urhell.service;

import io.highway.to.urhell.dao.BreakerLogDao;
import io.highway.to.urhell.dao.ThunderStatDao;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.ThunderApp;
import io.highway.to.urhell.domain.ThunderStat;
import io.highway.to.urhell.rest.domain.MessageStat;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@Named
@Lazy(false)
public class ThunderStatService {
	private static final Logger LOG = LoggerFactory
			.getLogger(ThunderStatService.class);

	@Inject
	private ThunderStatDao thunderStatDao;
	@Inject
	private BreakerLogDao breakerLogDao;
	
	public MessageStat analysisStat(String token){
		MessageStat ms = new MessageStat();
		LOG.info("analysis for token {}",token);
		List<ThunderStat> listThunderStat = thunderStatDao.findByToken(token);
		for (ThunderStat ts : listThunderStat) {
			Long count = breakerLogDao.findByPathClassMethodNameAndToken(
					ts.getPathClassMethodName(), ts.getThunderApp().getToken());
			ts.setCount(count);
		}
		Collections.sort(listThunderStat);
		ms.setListThunderStat(listThunderStat);
		ms.setTotalStat(listThunderStat.size());
		Integer falsePositive = 0;
		Integer totalNoTest = 0;
		for(ThunderStat ts : listThunderStat){
			if(ts.getFalsePositive()){
				falsePositive++;
			}
			if(ts.getCount()==0){
				totalNoTest++;
			}
		}
		ms.setTotalFalsePositive(falsePositive);
		ms.setTotalNoTest(totalNoTest);
		ms.setToken(token);
		return ms;
	}
	
	
	@Transactional
	public void createOrUpdateThunderStat(EntryPathData entry,
			ThunderApp ta) {
		String className=entry.getClassName();
		String methodName=entry.getMethodName();
		String uri = entry.getUri();
		String httpmethod=entry.getHttpMethod().name();
		String pathClassMethodName = className + "." + methodName;
		ThunderStat ts = thunderStatDao.findByPathClassMethodNameAndToken(
				pathClassMethodName, ta.getToken());
		if (ts == null) {
			ts = new ThunderStat();
			ts.setPathClassMethodName(pathClassMethodName);
			ts.setThunderApp(ta);
			ts.setCount(new Long(0));
			ts.setHttpmethod(httpmethod);
			ts.setUri(uri);
			thunderStatDao.save(ts);
		} else {
			ts.setCount(new Long(0));
		}

	}

	@Transactional
	public void updateThunderStatFalsePositive(String id,String status){
		ThunderStat ts = thunderStatDao.findOne(id);
		Boolean res = !Boolean.valueOf(status);
		ts.setFalsePositive(res);
		thunderStatDao.save(ts);
	}
	
}
