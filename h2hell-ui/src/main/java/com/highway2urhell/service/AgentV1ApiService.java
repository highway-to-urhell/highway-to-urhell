package com.highway2urhell.service;

import com.highway2urhell.domain.*;
import com.highway2urhell.repository.*;
import com.highway2urhell.service.mapper.TypeMessageEventMapper;
import com.highway2urhell.web.rest.dto.v1api.*;
import com.highway2urhell.web.rest.errors.V1ApiDateIncomingException;
import com.highway2urhell.web.rest.errors.V1ApiNotExistThunderAppException;
import com.highway2urhell.web.rest.errors.V1ApiPathNameException;
import com.highway2urhell.web.rest.errors.V1ApiTokenException;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
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

    @Inject
    private EntryPointCallRepository entryPointCallRepository;

    @Inject
    private EntryPointPerfRepository entryPointPerfRepository;

    @Inject
    private EventRepository eventRepository;

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
        Analysis analysis = validateToken(token);
        if (listEntryPathData != null && !listEntryPathData.isEmpty()) {
            for (EntryPathData entry : listEntryPathData) {
                // in V1 api we have one analysis by token
                createOrUpdateThunderStat(entry, analysis.getApplication(), analysis);
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
            pathClassMethodName, analysis.getId());
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

    public Analysis findAppByToken(String token) {
        return validateToken(token) ;
    }

    private Analysis validateToken(String token) {
        if (token == null) {
            throw new V1ApiTokenException();
        }
        List<Analysis> analyses = analysisRepository.findAllByTokenWithApplication(token);
        if (analyses == null || analyses.size() == 0) {
            throw new V1ApiNotExistThunderAppException();
        }
        if (analyses.size() > 1) {
            throw new V1ApiNotExistThunderAppException();
        }
        return analyses.get(0);
    }



    public void addListBreaker(List<MessageBreaker> listBreaker) {
        if (listBreaker != null && !listBreaker.isEmpty()) {
            for (MessageBreaker msg : listBreaker) {
                log.info(
                    "Call addBreaker with pathClassMethodName {} token {} and dateIncoming {}",
                    msg.getPathClassMethodName(), msg.getToken(),
                    msg.getDateIncoming());
                addBreaker(msg.getPathClassMethodName(), msg.getToken(),
                    msg.getDateIncoming(),msg.getParameters());
            }
        }
    }

    private void addBreaker(String pathClassMethodName, String token,
                            String dateIncoming,String parameters) {
        validate(pathClassMethodName, token, dateIncoming);
        Analysis analysis = findAppByToken(token);
        createBreakerLog(analysis, pathClassMethodName, dateIncoming,parameters);
    }


    private void validate(String pathClassMethodName, String token,
                          String dateIncoming) {
        if (pathClassMethodName == null) {
            throw new V1ApiPathNameException();
        }
        if (token == null) {
            throw new V1ApiTokenException();
        }
        if (dateIncoming == null) {
            throw new V1ApiDateIncomingException();
        }

    }

    public void createBreakerLog(Analysis analysis, String pathClassMethodName,
                                 String dateIncoming,String parameters) {
        EntryPoint ep = entryPointRepository.findByPathClassMethodNameAndToken(pathClassMethodName, analysis.getId());

        EntryPointCall epc = new EntryPointCall();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy:hh-mm-ss");
        try {
            epc.setDateIncoming(sdf.parse(dateIncoming).toInstant().atZone(ZoneId.systemDefault()));
        } catch (ParseException e) {
            log.error("unable to parse incoming date",e);
        }
        epc.setParameters(parameters.getBytes());
        epc.setEntryPoint(ep);
        entryPointCallRepository.save(epc);
    }



    public void addListPerformance(List<MessageMetrics> listPerf) {
        log.error("size listPerf !! "+listPerf.size());
        if (listPerf != null && !listPerf.isEmpty()) {
            for (MessageMetrics msg : listPerf) {
                log.info(
                    "add indicator Performance for pathClassMethodName {} time {} token {} and dateIncoming {}",
                    msg.getPathClassMethodName(), msg.getTimeExec(),
                    msg.getToken(), msg.getDateIncoming());
                createMetricsTimer(msg);
            }
        }
    }

    public void createMetricsTimer(MessageMetrics mm) {
        Analysis analysis = findAppByToken(mm.getToken());
        EntryPoint ep = entryPointRepository.findByPathClassMethodNameAndToken(mm.getPathClassMethodName(), analysis.getId());

        EntryPointPerf epp = new EntryPointPerf();
        epp.setTimeExec(Integer.valueOf(mm.getTimeExec()));
        epp.setCpuLoadProcess(mm.getCpuLoadProcess());
        epp.setCpuLoadSystem(mm.getCpuLoadSystem());
        epp.setDateIncoming(epp.getDateIncoming());
        epp.setParameters(mm.getParameters().getBytes());
        epp.setEntryPoint(ep);
        entryPointPerfRepository.save(epp);
    }

    public void addEvent(MessageEvent me) {
        Event event = new Event();
        if(me.getTypeMessageEvent() != null) {
            event.setTypeMessageEvent(TypeMessageEventMapper.domainToDto(me.getTypeMessageEvent()));
        }
        Analysis analysis = findAppByToken(me.getToken());
        event.setAnalysis(analysis);
        if(me.getData() != null) {
            event.setData(me.getData().getBytes());
        }
        eventRepository.save(event);
    }
}
