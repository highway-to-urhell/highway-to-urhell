package com.highway2urhell.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.highway2urhell.domain.EntryPointParameters;

import com.highway2urhell.repository.EntryPointParametersRepository;
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
 * REST controller for managing EntryPointParameters.
 */
@RestController
@RequestMapping("/api")
public class EntryPointParametersResource {

    private final Logger log = LoggerFactory.getLogger(EntryPointParametersResource.class);

    private static final String ENTITY_NAME = "entryPointParameters";
        
    private final EntryPointParametersRepository entryPointParametersRepository;

    public EntryPointParametersResource(EntryPointParametersRepository entryPointParametersRepository) {
        this.entryPointParametersRepository = entryPointParametersRepository;
    }

    /**
     * POST  /entry-point-parameters : Create a new entryPointParameters.
     *
     * @param entryPointParameters the entryPointParameters to create
     * @return the ResponseEntity with status 201 (Created) and with body the new entryPointParameters, or with status 400 (Bad Request) if the entryPointParameters has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/entry-point-parameters")
    @Timed
    public ResponseEntity<EntryPointParameters> createEntryPointParameters(@RequestBody EntryPointParameters entryPointParameters) throws URISyntaxException {
        log.debug("REST request to save EntryPointParameters : {}", entryPointParameters);
        if (entryPointParameters.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entryPointParameters cannot already have an ID")).body(null);
        }
        EntryPointParameters result = entryPointParametersRepository.save(entryPointParameters);
        return ResponseEntity.created(new URI("/api/entry-point-parameters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /entry-point-parameters : Updates an existing entryPointParameters.
     *
     * @param entryPointParameters the entryPointParameters to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated entryPointParameters,
     * or with status 400 (Bad Request) if the entryPointParameters is not valid,
     * or with status 500 (Internal Server Error) if the entryPointParameters couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/entry-point-parameters")
    @Timed
    public ResponseEntity<EntryPointParameters> updateEntryPointParameters(@RequestBody EntryPointParameters entryPointParameters) throws URISyntaxException {
        log.debug("REST request to update EntryPointParameters : {}", entryPointParameters);
        if (entryPointParameters.getId() == null) {
            return createEntryPointParameters(entryPointParameters);
        }
        EntryPointParameters result = entryPointParametersRepository.save(entryPointParameters);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entryPointParameters.getId().toString()))
            .body(result);
    }

    /**
     * GET  /entry-point-parameters : get all the entryPointParameters.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of entryPointParameters in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/entry-point-parameters")
    @Timed
    public ResponseEntity<List<EntryPointParameters>> getAllEntryPointParameters(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of EntryPointParameters");
        Page<EntryPointParameters> page = entryPointParametersRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/entry-point-parameters");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /entry-point-parameters/:id : get the "id" entryPointParameters.
     *
     * @param id the id of the entryPointParameters to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the entryPointParameters, or with status 404 (Not Found)
     */
    @GetMapping("/entry-point-parameters/{id}")
    @Timed
    public ResponseEntity<EntryPointParameters> getEntryPointParameters(@PathVariable Long id) {
        log.debug("REST request to get EntryPointParameters : {}", id);
        EntryPointParameters entryPointParameters = entryPointParametersRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(entryPointParameters));
    }

    /**
     * DELETE  /entry-point-parameters/:id : delete the "id" entryPointParameters.
     *
     * @param id the id of the entryPointParameters to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/entry-point-parameters/{id}")
    @Timed
    public ResponseEntity<Void> deleteEntryPointParameters(@PathVariable Long id) {
        log.debug("REST request to delete EntryPointParameters : {}", id);
        entryPointParametersRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
