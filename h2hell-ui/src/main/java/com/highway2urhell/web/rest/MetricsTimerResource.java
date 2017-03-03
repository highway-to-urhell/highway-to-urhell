package com.highway2urhell.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.highway2urhell.domain.MetricsTimer;

import com.highway2urhell.repository.MetricsTimerRepository;
import com.highway2urhell.web.rest.util.HeaderUtil;
import com.highway2urhell.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MetricsTimer.
 */
@RestController
@RequestMapping("/api")
public class MetricsTimerResource {

    private final Logger log = LoggerFactory.getLogger(MetricsTimerResource.class);

    private static final String ENTITY_NAME = "metricsTimer";
        
    private final MetricsTimerRepository metricsTimerRepository;

    public MetricsTimerResource(MetricsTimerRepository metricsTimerRepository) {
        this.metricsTimerRepository = metricsTimerRepository;
    }

    /**
     * POST  /metrics-timers : Create a new metricsTimer.
     *
     * @param metricsTimer the metricsTimer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new metricsTimer, or with status 400 (Bad Request) if the metricsTimer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/metrics-timers")
    @Timed
    public ResponseEntity<MetricsTimer> createMetricsTimer(@RequestBody MetricsTimer metricsTimer) throws URISyntaxException {
        log.debug("REST request to save MetricsTimer : {}", metricsTimer);
        if (metricsTimer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new metricsTimer cannot already have an ID")).body(null);
        }
        MetricsTimer result = metricsTimerRepository.save(metricsTimer);
        return ResponseEntity.created(new URI("/api/metrics-timers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /metrics-timers : Updates an existing metricsTimer.
     *
     * @param metricsTimer the metricsTimer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated metricsTimer,
     * or with status 400 (Bad Request) if the metricsTimer is not valid,
     * or with status 500 (Internal Server Error) if the metricsTimer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/metrics-timers")
    @Timed
    public ResponseEntity<MetricsTimer> updateMetricsTimer(@RequestBody MetricsTimer metricsTimer) throws URISyntaxException {
        log.debug("REST request to update MetricsTimer : {}", metricsTimer);
        if (metricsTimer.getId() == null) {
            return createMetricsTimer(metricsTimer);
        }
        MetricsTimer result = metricsTimerRepository.save(metricsTimer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, metricsTimer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /metrics-timers : get all the metricsTimers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of metricsTimers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/metrics-timers")
    @Timed
    public ResponseEntity<List<MetricsTimer>> getAllMetricsTimers(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of MetricsTimers");
        Page<MetricsTimer> page = metricsTimerRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/metrics-timers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /metrics-timers/:id : get the "id" metricsTimer.
     *
     * @param id the id of the metricsTimer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the metricsTimer, or with status 404 (Not Found)
     */
    @GetMapping("/metrics-timers/{id}")
    @Timed
    public ResponseEntity<MetricsTimer> getMetricsTimer(@PathVariable Long id) {
        log.debug("REST request to get MetricsTimer : {}", id);
        MetricsTimer metricsTimer = metricsTimerRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(metricsTimer));
    }

    /**
     * DELETE  /metrics-timers/:id : delete the "id" metricsTimer.
     *
     * @param id the id of the metricsTimer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/metrics-timers/{id}")
    @Timed
    public ResponseEntity<Void> deleteMetricsTimer(@PathVariable Long id) {
        log.debug("REST request to delete MetricsTimer : {}", id);
        metricsTimerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
