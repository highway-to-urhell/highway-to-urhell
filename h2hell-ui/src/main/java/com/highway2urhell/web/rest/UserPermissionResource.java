package com.highway2urhell.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.highway2urhell.domain.UserPermission;

import com.highway2urhell.repository.UserPermissionRepository;
import com.highway2urhell.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserPermission.
 */
@RestController
@RequestMapping("/api")
public class UserPermissionResource {

    private final Logger log = LoggerFactory.getLogger(UserPermissionResource.class);

    private static final String ENTITY_NAME = "userPermission";
        
    private final UserPermissionRepository userPermissionRepository;

    public UserPermissionResource(UserPermissionRepository userPermissionRepository) {
        this.userPermissionRepository = userPermissionRepository;
    }

    /**
     * POST  /user-permissions : Create a new userPermission.
     *
     * @param userPermission the userPermission to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userPermission, or with status 400 (Bad Request) if the userPermission has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-permissions")
    @Timed
    public ResponseEntity<UserPermission> createUserPermission(@Valid @RequestBody UserPermission userPermission) throws URISyntaxException {
        log.debug("REST request to save UserPermission : {}", userPermission);
        if (userPermission.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new userPermission cannot already have an ID")).body(null);
        }
        UserPermission result = userPermissionRepository.save(userPermission);
        return ResponseEntity.created(new URI("/api/user-permissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-permissions : Updates an existing userPermission.
     *
     * @param userPermission the userPermission to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userPermission,
     * or with status 400 (Bad Request) if the userPermission is not valid,
     * or with status 500 (Internal Server Error) if the userPermission couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-permissions")
    @Timed
    public ResponseEntity<UserPermission> updateUserPermission(@Valid @RequestBody UserPermission userPermission) throws URISyntaxException {
        log.debug("REST request to update UserPermission : {}", userPermission);
        if (userPermission.getId() == null) {
            return createUserPermission(userPermission);
        }
        UserPermission result = userPermissionRepository.save(userPermission);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userPermission.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-permissions : get all the userPermissions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userPermissions in body
     */
    @GetMapping("/user-permissions")
    @Timed
    public List<UserPermission> getAllUserPermissions() {
        log.debug("REST request to get all UserPermissions");
        List<UserPermission> userPermissions = userPermissionRepository.findAllWithEagerRelationships();
        return userPermissions;
    }

    /**
     * GET  /user-permissions/:id : get the "id" userPermission.
     *
     * @param id the id of the userPermission to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userPermission, or with status 404 (Not Found)
     */
    @GetMapping("/user-permissions/{id}")
    @Timed
    public ResponseEntity<UserPermission> getUserPermission(@PathVariable Long id) {
        log.debug("REST request to get UserPermission : {}", id);
        UserPermission userPermission = userPermissionRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userPermission));
    }

    /**
     * DELETE  /user-permissions/:id : delete the "id" userPermission.
     *
     * @param id the id of the userPermission to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-permissions/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserPermission(@PathVariable Long id) {
        log.debug("REST request to delete UserPermission : {}", id);
        userPermissionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
