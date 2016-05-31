package com.highway2urhell.service;

import com.highway2urhell.domain.Analysis;
import com.highway2urhell.domain.Application;
import com.highway2urhell.domain.EntryPoint;
import com.highway2urhell.repository.AnalysisRepository;
import com.highway2urhell.repository.ApplicationRepository;
import com.highway2urhell.repository.EntryPointRepository;
import com.highway2urhell.web.rest.dto.v1api.EntryPathData;
import com.highway2urhell.web.rest.dto.v1api.H2hConfigDTO;
import com.highway2urhell.web.rest.errors.V1ApiNotExistThunderAppException;
import com.highway2urhell.web.rest.errors.V1ApiTokenException;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.List;

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

    @Inject
    private EntryPointRepository entryPointRepository;

    public Analysis createAnalysis(H2hConfigDTO configDTO) {
        Application app = new Application();
        app.setName(configDTO.getNameApplication());
        app.setUrlApp(configDTO.getUrlApplication());
        if(configDTO.getToken() == null) {
            String token = RandomStringUtils.randomAlphanumeric(16).toUpperCase();
            app.setToken(token);
        } else {
            app.setToken(configDTO.getToken());
        }
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

    public void initThunderAppAndStat(String token, List<EntryPathData> listEntryPathData) {
        Application app = validateToken(token);
        if (listEntryPathData != null && !listEntryPathData.isEmpty()) {
            for (EntryPathData entry : listEntryPathData) {
                // in V1 api we have one analysis by token
                createOrUpdateThunderStat(entry, app, app.getAnalyses().toArray(new Analysis[1])[0]);
            }
        }
    }

    public void createOrUpdateThunderStat(EntryPathData entry, Application app,
                                          Analysis analysis) {
        String className=entry.getClassName();
        String methodName=entry.getMethodName();
        String uri = entry.getUri();
        String httpmethod=entry.getHttpMethod();
        Boolean audit = entry.getAudit();
        String pathClassMethodName = className + "." + methodName;
        EntryPoint ep = entryPointRepository.findByPathClassMethodNameAndToken(
            pathClassMethodName, app.getToken());
        if (ep == null) {
            ep = new EntryPoint();
            ep.setPathClassMethodName(pathClassMethodName);
            ep.setAnalysis(analysis);
            ep.setCount(0L);
            ep.setHttpmethod(httpmethod);
            ep.setUri(uri);
            ep.setAudit(audit);
            entryPointRepository.save(ep);
        } else {
            ep.setCount(0L);
        }

    }

    public Application findAppByToken(String token) {
        return validateToken(token) ;
    }

    private Application validateToken(String token) {
        if (token == null) {
            throw new V1ApiTokenException();
        }
        List<Application> apps = applicationRepository.findByTokenWithEagerRelationships(token);
        if (apps == null || apps.size() == 0) {
            throw new V1ApiNotExistThunderAppException();
        }
        if (apps.size() > 1) {
            throw new V1ApiNotExistThunderAppException();
        }
        return apps.get(0);
    }
}
