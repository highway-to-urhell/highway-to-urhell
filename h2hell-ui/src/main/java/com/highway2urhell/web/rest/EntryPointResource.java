package com.highway2urhell.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.highway2urhell.domain.EntryPoint;

import com.highway2urhell.repository.EntryPointRepository;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing EntryPoint.
 */
@RestController
@RequestMapping("/api")
public class EntryPointResource {

    private final Logger log = LoggerFactory.getLogger(EntryPointResource.class);
        
    private final EntryPointRepository entryPointRepository;

    public EntryPointResource(EntryPointRepository entryPointRepository) {
        this.entryPointRepository = entryPointRepository;
    }

    /**
     * POST  /entry-points : Create a new entryPoint.
     *
     * @param entryPoint the entryPoint to create
     * @return the ResponseEntity with status 201 (Created) and with body the new entryPoint, or with status 400 (Bad Request) if the entryPoint has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/entry-points")
    @Timed
    public ResponseEntity<EntryPoint> createEntryPoint(@Valid @RequestBody EntryPoint entryPoint) throws URISyntaxException {
        log.debug("REST request to save EntryPoint : {}", entryPoint);
        if (entryPoint.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("entryPoint", "idexists", "A new entryPoint cannot already have an ID")).body(null);
        }
        EntryPoint result = entryPointRepository.save(entryPoint);
        return ResponseEntity.created(new URI("/api/entry-points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("entryPoint", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /entry-points : Updates an existing entryPoint.
     *
     * @param entryPoint the entryPoint to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated entryPoint,
     * or with status 400 (Bad Request) if the entryPoint is not valid,
     * or with status 500 (Internal Server Error) if the entryPoint couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/entry-points")
    @Timed
    public ResponseEntity<EntryPoint> updateEntryPoint(@Valid @RequestBody EntryPoint entryPoint) throws URISyntaxException {
        log.debug("REST request to update EntryPoint : {}", entryPoint);
        if (entryPoint.getId() == null) {
            return createEntryPoint(entryPoint);
        }
        EntryPoint result = entryPointRepository.save(entryPoint);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("entryPoint", entryPoint.getId().toString()))
            .body(result);
    }

    /**
     * GET  /entry-points : get all the entryPoints.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of entryPoints in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/entry-points")
    @Timed
    public ResponseEntity<List<EntryPoint>> getAllEntryPoints(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of EntryPoints");
        Page<EntryPoint> page = entryPointRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/entry-points");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /entry-points/:id : get the "id" entryPoint.
     *
     * @param id the id of the entryPoint to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the entryPoint, or with status 404 (Not Found)
     */
    @GetMapping("/entry-points/{id}")
    @Timed
    public ResponseEntity<EntryPoint> getEntryPoint(@PathVariable Long id) {
        log.debug("REST request to get EntryPoint : {}", id);
        EntryPoint entryPoint = entryPointRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(entryPoint));
    }

    /**
     * DELETE  /entry-points/:id : delete the "id" entryPoint.
     *
     * @param id the id of the entryPoint to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/entry-points/{id}")
    @Timed
    public ResponseEntity<Void> deleteEntryPoint(@PathVariable Long id) {
        log.debug("REST request to delete EntryPoint : {}", id);
        entryPointRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("entryPoint", id.toString())).build();
    }

}
