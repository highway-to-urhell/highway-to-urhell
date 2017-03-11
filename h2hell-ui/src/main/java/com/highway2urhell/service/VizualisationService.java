package com.highway2urhell.service;

import com.highway2urhell.domain.Analysis;
import com.highway2urhell.domain.Application;
import com.highway2urhell.domain.EntryPoint;
import com.highway2urhell.domain.Event;
import com.highway2urhell.domain.enumeration.TypeMessageEvent;
import com.highway2urhell.repository.*;
import com.highway2urhell.web.rest.errors.V1ApiNotExistThunderAppException;
import com.highway2urhell.web.rest.vm.VizualisationPathVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.*;

/**
 * Service to get home data.
 */
@Service
@Transactional
public class VizualisationService {

    private static List<String> listFrameworkNoAnalysis = new ArrayList<String>(){{
        add("org.apache.struts");
        add("com.google.gwt");
        add("org.springframework");
        add("javax.servlet");
        add("com.sun.faces");
        add("org.apache.activemq");
        add("org.apache.struts2");
        add("org.glassfish.jersey.server");
        add("portAnalysis");
        add("org.springframework.web.struts");
        add("com.highway2urhell.filter");
        add("com.highway2urhell.servlet");
    }};

    private final Logger log = LoggerFactory.getLogger(VizualisationService.class);

    @Inject
    private ApplicationRepository applicationRepository;

    @Inject
    private AnalysisRepository analysisRepository;

    @Inject
    private EntryPointPerfRepository entryPointPerfRepository;

    @Inject
    private EntryPointCallRepository entryPointCallRepository;

    @Inject
    private EntryPointRepository entryPointRepository;

    @Inject
    private EventRepository eventRepository;

    @Inject
    private AgentV1ApiService agentV1ApiService;

    @Transactional(readOnly = true)
    public VizualisationPathVM findPath(String token) {
        VizualisationPathVM vizualisationPathVM = new VizualisationPathVM();
        List<EntryPoint> entrypoints = entryPointRepository.findAllByTokenWithApplicationAndEntrypoint(token);
        vizualisationPathVM.setEntrypoints(entrypoints);
        vizualisationPathVM.setTotalStat(entrypoints.size());
        int falsePositive = 0;
        int totalNoTest = 0;
        for(EntryPoint ep : entrypoints){
            if(ep.isFalsePositive() != null && ep.isFalsePositive()){
                falsePositive++;
            }
            if(ep.getCount() == null || ep.getCount()==0){
                totalNoTest++;
            }
        }
        vizualisationPathVM.setTotalFalsePositive(falsePositive);
        vizualisationPathVM.setTotalNoTest(totalNoTest);
        return vizualisationPathVM;
    }

    public void findAllPaths(String token) {
        Event ev = new Event();
        List<Analysis> analyses = analysisRepository.findAllByTokenWithApplication(token);
        if (analyses == null || analyses.size() == 0) {
            throw new V1ApiNotExistThunderAppException();
        }
        ev.setAnalysis(analyses.get(0));
        ev.setConsumed(false);
        ev.setTypeMessageEvent(TypeMessageEvent.INIT_PATH);
        eventRepository.save(ev);
    }

    public VizualisationPathVM analysisStat(String token){
        log.info("analysis for token {}",token);
        VizualisationPathVM vizualisationPathVM = findPath(token);
        for (EntryPoint ep : vizualisationPathVM.getEntrypoints()) {
            Long count = entryPointCallRepository.findByPathClassMethodNameAndToken(
                ep.getPathClassMethodName(), ep.getAnalysis().getApplication().getToken());
            Long averageTime = entryPointPerfRepository.findAverageFromPathClassMethodNameAndToken(ep.getPathClassMethodName(), ep.getAnalysis().getApplication().getToken());
            ep.setCount(count);
            ep.setAverageTime(averageTime);
            entryPointRepository.save(ep);
        }
        Collections.sort(vizualisationPathVM.getEntrypoints(), (o1, o2) -> Long.compare(o1.getCount(), o2.getCount()));
        return vizualisationPathVM;
    }

    public void filterFramework(List<EntryPoint> listTS) {
        if(listTS!=null && !listTS.isEmpty()){
            for(EntryPoint ts : listTS){
                if(isAframeworkClass(ts.getPathClassMethodName())){
                    ts.setDrawAnalysis(false);
                }else{
                    if(!ts.isAudit()){
                        ts.setDrawAnalysis(true);
                    }else{
                        ts.setDrawAnalysis(false);
                    }
                }
            }
        }
    }


    private Boolean isAframeworkClass(String classMethodName){
        for(String elem : listFrameworkNoAnalysis){
            if(classMethodName.contains(elem)) {
                return true;
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    public Application findApplicationByToken(String token) {
        return applicationRepository.findByToken(token);
    }


    public EntryPoint updatePathFalsePositive(Long id, String status) {
        EntryPoint entryPoint = entryPointRepository.findOne(id);
        Boolean res = Boolean.valueOf(status);
        entryPoint.setFalsePositive(res);
        entryPointRepository.save(entryPoint);
        return entryPoint;
    }

    public String findSource(String token, String classAndMethod) {
        String className = extractClass(classAndMethod);
        Analysis th  =agentV1ApiService.validateToken(token);
        if(th == null){
            return "no Application";
        }
        String path = className + getMapTypeConversion().get(th.getApplication().getAppType().toLowerCase());

        RestTemplate restTemplate = new RestTemplate();
        String url = th.getApplication().getUrlApp() + "h2h/?srcPath="+path;
        String responseEntity = null;
        log.info("Call app for find source {}", url);
        try {
            responseEntity = restTemplate
                .getForObject(url, String.class);

        } catch (RestClientException r) {
            log.error("error call {} exception {}", url,r);
            responseEntity = r.getMessage();
        }
        log.info("result call {}", responseEntity);
        return responseEntity;
    }

    private String extractClass(String classAndMethod){
        String[] tabClass = classAndMethod.split("\\.");
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<tabClass.length-1;i++){
            sb.append("/");
            sb.append(tabClass[i]);
        }

        return sb.toString();
    }

    private static Map<String,String> mapTypeConversion ;


    private Map<String, String> getMapTypeConversion() {
        if(mapTypeConversion == null){
            synchronized (this) {
                mapTypeConversion = new HashMap<String,String>();
                mapTypeConversion.put("java", ".java");
                mapTypeConversion.put("php", ".php");
                mapTypeConversion.put("nodejs", ".js");
            }
        }
        return mapTypeConversion;
    }

}
