package com.highway2urhell.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.highway2urhell.domain.EntryPointCall;

import com.highway2urhell.repository.EntryPointCallRepository;
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
 * REST controller for managing EntryPointCall.
 */
@RestController
@RequestMapping("/api")
public class EntryPointCallResource {

    private final Logger log = LoggerFactory.getLogger(EntryPointCallResource.class);
        
    private final EntryPointCallRepository entryPointCallRepository;

    public EntryPointCallResource(EntryPointCallRepository entryPointCallRepository) {
        this.entryPointCallRepository = entryPointCallRepository;
    }

    /**
     * POST  /entry-point-calls : Create a new entryPointCall.
     *
     * @param entryPointCall the entryPointCall to create
     * @return the ResponseEntity with status 201 (Created) and with body the new entryPointCall, or with status 400 (Bad Request) if the entryPointCall has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/entry-point-calls")
    @Timed
    public ResponseEntity<EntryPointCall> createEntryPointCall(@RequestBody EntryPointCall entryPointCall) throws URISyntaxException {
        log.debug("REST request to save EntryPointCall : {}", entryPointCall);
        if (entryPointCall.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("entryPointCall", "idexists", "A new entryPointCall cannot already have an ID")).body(null);
        }
        EntryPointCall result = entryPointCallRepository.save(entryPointCall);
        return ResponseEntity.created(new URI("/api/entry-point-calls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("entryPointCall", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /entry-point-calls : Updates an existing entryPointCall.
     *
     * @param entryPointCall the entryPointCall to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated entryPointCall,
     * or with status 400 (Bad Request) if the entryPointCall is not valid,
     * or with status 500 (Internal Server Error) if the entryPointCall couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/entry-point-calls")
    @Timed
    public ResponseEntity<EntryPointCall> updateEntryPointCall(@RequestBody EntryPointCall entryPointCall) throws URISyntaxException {
        log.debug("REST request to update EntryPointCall : {}", entryPointCall);
        if (entryPointCall.getId() == null) {
            return createEntryPointCall(entryPointCall);
        }
        EntryPointCall result = entryPointCallRepository.save(entryPointCall);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("entryPointCall", entryPointCall.getId().toString()))
            .body(result);
    }

    /**
     * GET  /entry-point-calls : get all the entryPointCalls.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of entryPointCalls in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/entry-point-calls")
    @Timed
    public ResponseEntity<List<EntryPointCall>> getAllEntryPointCalls(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of EntryPointCalls");
        Page<EntryPointCall> page = entryPointCallRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/entry-point-calls");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /entry-point-calls/:id : get the "id" entryPointCall.
     *
     * @param id the id of the entryPointCall to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the entryPointCall, or with status 404 (Not Found)
     */
    @GetMapping("/entry-point-calls/{id}")
    @Timed
    public ResponseEntity<EntryPointCall> getEntryPointCall(@PathVariable Long id) {
        log.debug("REST request to get EntryPointCall : {}", id);
        EntryPointCall entryPointCall = entryPointCallRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(entryPointCall));
    }

    /**
     * DELETE  /entry-point-calls/:id : delete the "id" entryPointCall.
     *
     * @param id the id of the entryPointCall to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/entry-point-calls/{id}")
    @Timed
    public ResponseEntity<Void> deleteEntryPointCall(@PathVariable Long id) {
        log.debug("REST request to delete EntryPointCall : {}", id);
        entryPointCallRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("entryPointCall", id.toString())).build();
    }

}
