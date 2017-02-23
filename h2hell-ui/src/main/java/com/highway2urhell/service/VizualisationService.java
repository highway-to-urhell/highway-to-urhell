package com.highway2urhell.service;

import com.highway2urhell.repository.AnalysisRepository;
import com.highway2urhell.repository.ApplicationRepository;
import com.highway2urhell.repository.EntryPointRepository;
import com.highway2urhell.web.rest.vm.VizualisationPathVM;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Service to get home data.
 */
@Service
@Transactional
public class VizualisationService {

    @Inject
    private ApplicationRepository applicationRepository;

    @Inject
    private AnalysisRepository analysisRepository;

    @Inject
    private EntryPointRepository entryPointRepository;

    public VizualisationPathVM findPath(String token) {
        VizualisationPathVM vizualisationPathVM = new VizualisationPathVM();
        vizualisationPathVM.setEntrypoints(entryPointRepository.findAllByTokenWithApplicationAndEntrypoint(token));
        return vizualisationPathVM;
    }
}
