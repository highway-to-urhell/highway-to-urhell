package com.highway2urhell.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.highway2urhell.domain.Analysis;
import com.highway2urhell.service.AgentV1ApiService;
import com.highway2urhell.web.rest.dto.v1api.H2hConfigDTO;
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


@RestController
@RequestMapping("/ThunderEntry")
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

}
