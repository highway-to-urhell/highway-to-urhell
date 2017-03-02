package com.highway2urhell.service;

import com.highway2urhell.domain.Analysis;
import com.highway2urhell.domain.Application;
import com.highway2urhell.domain.EntryPoint;
import com.highway2urhell.domain.Event;
import com.highway2urhell.domain.enumeration.TypeMessageEvent;
import com.highway2urhell.repository.*;
import com.highway2urhell.web.rest.errors.V1ApiNotExistThunderAppException;
import com.highway2urhell.web.rest.vm.MessageStat;
import com.highway2urhell.web.rest.vm.VizualisationPathVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    public VizualisationPathVM findPath(String token) {
        VizualisationPathVM vizualisationPathVM = new VizualisationPathVM();
        vizualisationPathVM.setEntrypoints(entryPointRepository.findAllByTokenWithApplicationAndEntrypoint(token));
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

    public MessageStat analysisStat(String token){
        MessageStat ms = new MessageStat();
        log.info("analysis for token {}",token);
        List<EntryPoint> listThunderStat = entryPointRepository.findAllByTokenWithApplicationAndEntrypoint(token);
        log.info("SIZE stat for token {}",listThunderStat.size());
        for (EntryPoint ep : listThunderStat) {
            Long count = entryPointCallRepository.findByPathClassMethodNameAndToken(
                ep.getPathClassMethodName(), ep.getAnalysis().getApplication().getToken());
            Long averageTime = entryPointPerfRepository.findAverageFromPathClassMethodNameAndToken(ep.getPathClassMethodName(), ep.getAnalysis().getApplication().getToken());
            ep.setCount(count);
            ep.setAverageTime(averageTime);
        }
        Collections.sort(listThunderStat, (o1, o2) -> Long.compare(o1.getCount(), o2.getCount()));
        ms.setListThunderStat(listThunderStat);
        ms.setTotalStat(listThunderStat.size());
        Integer falsePositive = 0;
        Integer totalNoTest = 0;
        for(EntryPoint ts : listThunderStat){
            if(ts.isFalsePositive()){
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

    public Application findApplicationByToken(String token) {
        return applicationRepository.findByToken(token);
    }


}
