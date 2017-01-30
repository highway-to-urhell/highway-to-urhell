package com.highway2urhell.service;

import com.highway2urhell.repository.AnalysisRepository;
import com.highway2urhell.repository.ApplicationRepository;
import com.highway2urhell.repository.EntryPointRepository;
import com.highway2urhell.web.rest.vm.ApplicationByTypesVM;
import com.highway2urhell.web.rest.vm.EntryPointByApplicationVM;
import com.highway2urhell.web.rest.vm.HomeVM;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Service to get home data.
 */
@Service
@Transactional
public class HomeService {

    @Inject
    private ApplicationRepository applicationRepository;

    @Inject
    private AnalysisRepository analysisRepository;

    @Inject
    private EntryPointRepository entryPointRepository;

    public HomeVM find() {
        HomeVM homeVM = new HomeVM();
        homeVM.setApplicationsCount(applicationRepository.count());
        homeVM.setEntrypointsCount(entryPointRepository.count());
        EntryPointByApplicationVM statsVM = new EntryPointByApplicationVM(entryPointRepository.countGroupByApplication());
        homeVM.setStats(statsVM);
        ApplicationByTypesVM typesVM = new ApplicationByTypesVM(applicationRepository.countGroupByAppType());
        homeVM.setTypes(typesVM);
        return homeVM;
        //return Optional.ofNullable(persistenceAuditEventRepository.findOne(id)).map
        //    (auditEventConverter::convertToAuditEvent);
    }
}
