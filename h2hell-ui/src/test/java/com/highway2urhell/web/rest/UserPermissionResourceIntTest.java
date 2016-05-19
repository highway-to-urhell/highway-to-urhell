package com.highway2urhell.web.rest;

import com.highway2urhell.H2HellUiApp;
import com.highway2urhell.domain.UserPermission;
import com.highway2urhell.repository.UserPermissionRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.highway2urhell.domain.enumeration.Permission;

/**
 * Test class for the UserPermissionResource REST controller.
 *
 * @see UserPermissionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = H2HellUiApp.class)
@WebAppConfiguration
@IntegrationTest
public class UserPermissionResourceIntTest {


    private static final Permission DEFAULT_PERMISSION = Permission.READ;
    private static final Permission UPDATED_PERMISSION = Permission.EXECUTE;

    @Inject
    private UserPermissionRepository userPermissionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserPermissionMockMvc;

    private UserPermission userPermission;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserPermissionResource userPermissionResource = new UserPermissionResource();
        ReflectionTestUtils.setField(userPermissionResource, "userPermissionRepository", userPermissionRepository);
        this.restUserPermissionMockMvc = MockMvcBuilders.standaloneSetup(userPermissionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        userPermission = new UserPermission();
        userPermission.setPermission(DEFAULT_PERMISSION);
    }

    @Test
    @Transactional
    public void createUserPermission() throws Exception {
        int databaseSizeBeforeCreate = userPermissionRepository.findAll().size();

        // Create the UserPermission

        restUserPermissionMockMvc.perform(post("/api/user-permissions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userPermission)))
                .andExpect(status().isCreated());

        // Validate the UserPermission in the database
        List<UserPermission> userPermissions = userPermissionRepository.findAll();
        assertThat(userPermissions).hasSize(databaseSizeBeforeCreate + 1);
        UserPermission testUserPermission = userPermissions.get(userPermissions.size() - 1);
        assertThat(testUserPermission.getPermission()).isEqualTo(DEFAULT_PERMISSION);
    }

    @Test
    @Transactional
    public void checkPermissionIsRequired() throws Exception {
        int databaseSizeBeforeTest = userPermissionRepository.findAll().size();
        // set the field null
        userPermission.setPermission(null);

        // Create the UserPermission, which fails.

        restUserPermissionMockMvc.perform(post("/api/user-permissions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userPermission)))
                .andExpect(status().isBadRequest());

        List<UserPermission> userPermissions = userPermissionRepository.findAll();
        assertThat(userPermissions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserPermissions() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);

        // Get all the userPermissions
        restUserPermissionMockMvc.perform(get("/api/user-permissions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userPermission.getId().intValue())))
                .andExpect(jsonPath("$.[*].permission").value(hasItem(DEFAULT_PERMISSION.toString())));
    }

    @Test
    @Transactional
    public void getUserPermission() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);

        // Get the userPermission
        restUserPermissionMockMvc.perform(get("/api/user-permissions/{id}", userPermission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userPermission.getId().intValue()))
            .andExpect(jsonPath("$.permission").value(DEFAULT_PERMISSION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserPermission() throws Exception {
        // Get the userPermission
        restUserPermissionMockMvc.perform(get("/api/user-permissions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserPermission() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);
        int databaseSizeBeforeUpdate = userPermissionRepository.findAll().size();

        // Update the userPermission
        UserPermission updatedUserPermission = new UserPermission();
        updatedUserPermission.setId(userPermission.getId());
        updatedUserPermission.setPermission(UPDATED_PERMISSION);

        restUserPermissionMockMvc.perform(put("/api/user-permissions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUserPermission)))
                .andExpect(status().isOk());

        // Validate the UserPermission in the database
        List<UserPermission> userPermissions = userPermissionRepository.findAll();
        assertThat(userPermissions).hasSize(databaseSizeBeforeUpdate);
        UserPermission testUserPermission = userPermissions.get(userPermissions.size() - 1);
        assertThat(testUserPermission.getPermission()).isEqualTo(UPDATED_PERMISSION);
    }

    @Test
    @Transactional
    public void deleteUserPermission() throws Exception {
        // Initialize the database
        userPermissionRepository.saveAndFlush(userPermission);
        int databaseSizeBeforeDelete = userPermissionRepository.findAll().size();

        // Get the userPermission
        restUserPermissionMockMvc.perform(delete("/api/user-permissions/{id}", userPermission.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserPermission> userPermissions = userPermissionRepository.findAll();
        assertThat(userPermissions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
