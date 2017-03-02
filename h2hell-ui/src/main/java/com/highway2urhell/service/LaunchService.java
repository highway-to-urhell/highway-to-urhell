package com.highway2urhell.service;

import com.highway2urhell.domain.Analysis;
import com.highway2urhell.domain.Event;
import com.highway2urhell.domain.enumeration.TypeMessageEvent;
import com.highway2urhell.repository.AnalysisRepository;
import com.highway2urhell.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
public class LaunchService {

    @Inject
	private AnalysisRepository analysisRepository;
	@Inject
	private EventRepository eventRepository;

	@Transactional
	public void findAllPaths(String token, String appVersion) {
		Analysis analysis = analysisRepository.findOneByTokenAndVersionWithApplication(token, appVersion);
		Event ev = new Event();
		ev.setAnalysis(analysis);
		ev.setConsumed(false);
		ev.setTypeMessageEvent(TypeMessageEvent.INIT_PATH);
		eventRepository.save(ev);
	}

}
