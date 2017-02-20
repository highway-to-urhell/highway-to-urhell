package com.highway2urhell.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.highway2urhell.domain.Analysis;
import com.highway2urhell.domain.Application;
import com.highway2urhell.repository.AnalysisRepository;
import com.highway2urhell.repository.ApplicationRepository;
import com.highway2urhell.web.rest.util.HeaderUtil;
import com.highway2urhell.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
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

    public VizualisationResource(AnalysisRepository analysisRepository) {
        this.analysisRepository = analysisRepository;
    }


    /**
     * GET  /applications : get all the applications.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of analysis in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/vizualisation")
    @Timed
    public List<Analysis> getAllAnalysis()
        throws URISyntaxException {
        log.debug("REST request to get a page of Applications");
        List<Analysis> analysisList = analysisRepository.findWithEagerRelationships();
        return analysisList;
    }

}
