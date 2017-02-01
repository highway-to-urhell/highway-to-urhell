package com.highway2urhell.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.highway2urhell.domain.Analysis;
import com.highway2urhell.service.AgentV1ApiService;
import com.highway2urhell.web.rest.dto.v1api.H2hConfigDTO;
import com.highway2urhell.web.rest.dto.v1api.MessageBreaker;
import com.highway2urhell.web.rest.dto.v1api.MessageMetrics;
import com.highway2urhell.web.rest.dto.v1api.MessageThunderApp;
import com.highway2urhell.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


@RestController
@RequestMapping("/api/ThunderEntry")
public class AgentV1ApiResource {

    private static final Logger LOG = LoggerFactory
        .getLogger(AgentV1ApiResource.class);

    @Inject
    private AgentV1ApiService agentV1ApiService;

    @RequestMapping(value = "/createThunderApp",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> createThunderApp(@Valid @RequestBody H2hConfigDTO configDTO) throws URISyntaxException {
        LOG.info("Call createThunderApp with {}", configDTO.getNameApplication());
        Analysis result =  agentV1ApiService.createAnalysis(configDTO);
        return ResponseEntity.created(new URI("/api/applications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("application", result.getId().toString()))
            .body(result.getApplication().getToken());
    }

    @RequestMapping(value = "/initThunderApp",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> initThunderApp(@Valid @RequestBody MessageThunderApp msg) throws URISyntaxException {
        LOG.info("Call createThunderApp with token {} and list size {}",
            msg.getToken(), msg.getListentryPathData().size());
        agentV1ApiService.initThunderAppAndStat(msg.getToken(),
            msg.getListentryPathData());
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/addBreaker",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> addBreaker(@Valid @RequestBody List<MessageBreaker> listBreaker) throws URISyntaxException {
        agentV1ApiService.addListBreaker(listBreaker);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/addPerformance",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> addPerformance(@Valid @RequestBody List<MessageMetrics> listPerf) throws URISyntaxException {
        agentV1ApiService.addListPerformance(listPerf);
        return ResponseEntity.ok().build();
    }

}
