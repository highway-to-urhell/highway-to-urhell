package com.highway2urhell.service;

import com.highway2urhell.domain.Analysis;
import com.highway2urhell.domain.Application;
import com.highway2urhell.repository.AnalysisRepository;
import com.highway2urhell.repository.ApplicationRepository;
import com.highway2urhell.web.rest.dto.v1api.H2hConfigDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;

/**
 * Service class for managing agent v1 api.
 */
@Service
@Transactional
public class AgentV1ApiService {

    private final Logger log = LoggerFactory.getLogger(AgentV1ApiService.class);


    @Inject
    private ApplicationRepository applicationRepository;

    @Inject
    private AnalysisRepository analysisRepository;

    public Analysis createAnalysis(H2hConfigDTO configDTO) {
        Application app = new Application();
        app.setName(configDTO.getNameApplication());
        app.setUrlApp(configDTO.getUrlApplication());
        app.setToken(configDTO.getToken());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy:hh-mm-ss");
        app.setDateCreation(ZonedDateTime.now());
        app.setDescription(configDTO.getDescription());
        if(configDTO.getTypeAppz()!=null){
            app.setAppType(configDTO.getTypeAppz());
        }else{
            app.setAppType("UNKNOWN");
        }
        app = applicationRepository.save(app);

        Analysis analysis = new Analysis();
        analysis.setApplication(app);
        analysis.setAppVersion(configDTO.getVersionApp());
        analysis.setPathSource(configDTO.getPathSource());
        analysis = analysisRepository.save(analysis);

        log.debug("Created Information for Analysis: {}", analysis);
        return analysis;
    }

}
