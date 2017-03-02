package com.highway2urhell.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.highway2urhell.domain.Analysis;
import com.highway2urhell.domain.Application;
import com.highway2urhell.domain.EntryPoint;
import com.highway2urhell.repository.AnalysisRepository;
import com.highway2urhell.repository.EntryPointRepository;
import com.highway2urhell.service.VizualisationService;
import com.highway2urhell.web.rest.vm.MessageStat;
import com.highway2urhell.web.rest.vm.VizualisationPathVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Application.
 */
@RestController
@RequestMapping("/api")
public class VizualisationResource {

    private final Logger log = LoggerFactory.getLogger(VizualisationResource.class);

    private final AnalysisRepository analysisRepository;

    private final VizualisationService vizualisationService;

    public VizualisationResource(AnalysisRepository analysisRepository, VizualisationService vizualisationService) {
        this.analysisRepository = analysisRepository;
        this.vizualisationService = vizualisationService;
    }


    /**
     * GET  /vizualisation : get all the applications.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of analysis in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/vizualisation")
    @Timed
    public List<Analysis> getAllAnalysis()
        throws URISyntaxException {
        log.debug("REST request to get a page of vizualisation");
        List<Analysis> analysisList = analysisRepository.findAllWithApplication();
        return analysisList;
    }

    /**
     * GET  /vizualisation/:id : get the "id" analysis.
     *
     * @param id the id of the analysis to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the analysis, or with status 404 (Not Found)
     */
    @GetMapping("/vizualisation/{id}")
    @Timed
    public ResponseEntity<VizualisationPathVM> findPath(@PathVariable String id) {
        log.debug("REST request to get Vizualisation : {}", id);
        VizualisationPathVM pathVM = vizualisationService.findPath(id);
        return Optional.ofNullable(pathVM)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/vizualisation/findAllPaths/{id}")
    @Timed
    public ResponseEntity<Void> findAllPaths(@PathVariable("id") String id) {
        log.info("Call findAllPaths ");
        vizualisationService.findAllPaths(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/vizualisation/findStatByTokenAndFilter/{id}")
    @Timed
    public ResponseEntity<MessageStat> findStatByTokenAndFilter(@PathVariable("id") String id) {
        log.info("Call findStatByTokenAndFilter ");
        MessageStat ms = vizualisationService.analysisStat(id);
        //filter
        vizualisationService.filterFramework(ms.getListThunderStat());
        //TODO dirty clean this
        Application app = vizualisationService.findApplicationByToken(id);
        ms.setAnalysis(app.isAnalysed());
        ms.setAppName(app.getName());
        return ResponseEntity.ok().body(ms);
    }

}
