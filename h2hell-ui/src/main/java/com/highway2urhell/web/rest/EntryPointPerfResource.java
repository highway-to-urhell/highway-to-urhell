package com.highway2urhell.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.highway2urhell.domain.EntryPointPerf;

import com.highway2urhell.repository.EntryPointPerfRepository;
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
 * REST controller for managing EntryPointPerf.
 */
@RestController
@RequestMapping("/api")
public class EntryPointPerfResource {

    private final Logger log = LoggerFactory.getLogger(EntryPointPerfResource.class);

    private static final String ENTITY_NAME = "entryPointPerf";
        
    private final EntryPointPerfRepository entryPointPerfRepository;

    public EntryPointPerfResource(EntryPointPerfRepository entryPointPerfRepository) {
        this.entryPointPerfRepository = entryPointPerfRepository;
    }

    /**
     * POST  /entry-point-perfs : Create a new entryPointPerf.
     *
     * @param entryPointPerf the entryPointPerf to create
     * @return the ResponseEntity with status 201 (Created) and with body the new entryPointPerf, or with status 400 (Bad Request) if the entryPointPerf has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/entry-point-perfs")
    @Timed
    public ResponseEntity<EntryPointPerf> createEntryPointPerf(@RequestBody EntryPointPerf entryPointPerf) throws URISyntaxException {
        log.debug("REST request to save EntryPointPerf : {}", entryPointPerf);
        if (entryPointPerf.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entryPointPerf cannot already have an ID")).body(null);
        }
        EntryPointPerf result = entryPointPerfRepository.save(entryPointPerf);
        return ResponseEntity.created(new URI("/api/entry-point-perfs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /entry-point-perfs : Updates an existing entryPointPerf.
     *
     * @param entryPointPerf the entryPointPerf to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated entryPointPerf,
     * or with status 400 (Bad Request) if the entryPointPerf is not valid,
     * or with status 500 (Internal Server Error) if the entryPointPerf couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/entry-point-perfs")
    @Timed
    public ResponseEntity<EntryPointPerf> updateEntryPointPerf(@RequestBody EntryPointPerf entryPointPerf) throws URISyntaxException {
        log.debug("REST request to update EntryPointPerf : {}", entryPointPerf);
        if (entryPointPerf.getId() == null) {
            return createEntryPointPerf(entryPointPerf);
        }
        EntryPointPerf result = entryPointPerfRepository.save(entryPointPerf);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entryPointPerf.getId().toString()))
            .body(result);
    }

    /**
     * GET  /entry-point-perfs : get all the entryPointPerfs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of entryPointPerfs in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/entry-point-perfs")
    @Timed
    public ResponseEntity<List<EntryPointPerf>> getAllEntryPointPerfs(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of EntryPointPerfs");
        Page<EntryPointPerf> page = entryPointPerfRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/entry-point-perfs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /entry-point-perfs/:id : get the "id" entryPointPerf.
     *
     * @param id the id of the entryPointPerf to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the entryPointPerf, or with status 404 (Not Found)
     */
    @GetMapping("/entry-point-perfs/{id}")
    @Timed
    public ResponseEntity<EntryPointPerf> getEntryPointPerf(@PathVariable Long id) {
        log.debug("REST request to get EntryPointPerf : {}", id);
        EntryPointPerf entryPointPerf = entryPointPerfRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(entryPointPerf));
    }

    /**
     * DELETE  /entry-point-perfs/:id : delete the "id" entryPointPerf.
     *
     * @param id the id of the entryPointPerf to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/entry-point-perfs/{id}")
    @Timed
    public ResponseEntity<Void> deleteEntryPointPerf(@PathVariable Long id) {
        log.debug("REST request to delete EntryPointPerf : {}", id);
        entryPointPerfRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
