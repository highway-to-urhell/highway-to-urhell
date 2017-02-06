package com.highway2urhell.web.rest;

import com.highway2urhell.H2HellUiApp;

import com.highway2urhell.domain.EntryPoint;
import com.highway2urhell.repository.EntryPointRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EntryPointResource REST controller.
 *
 * @see EntryPointResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = H2HellUiApp.class)
public class EntryPointResourceIntTest {

    private static final String DEFAULT_PATH_CLASS_METHOD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PATH_CLASS_METHOD_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_COUNT = 1L;
    private static final Long UPDATED_COUNT = 2L;

    private static final Boolean DEFAULT_FALSE_POSITIVE = false;
    private static final Boolean UPDATED_FALSE_POSITIVE = true;

    private static final String DEFAULT_URI = "AAAAAAAAAA";
    private static final String UPDATED_URI = "BBBBBBBBBB";

    private static final String DEFAULT_HTTPMETHOD = "AAAAAAAAAA";
    private static final String UPDATED_HTTPMETHOD = "BBBBBBBBBB";

    private static final Boolean DEFAULT_AUDIT = false;
    private static final Boolean UPDATED_AUDIT = true;

    private static final Long DEFAULT_AVERAGE_TIME = 1L;
    private static final Long UPDATED_AVERAGE_TIME = 2L;

    private static final Boolean DEFAULT_CHECK_LAUNCH = false;
    private static final Boolean UPDATED_CHECK_LAUNCH = true;

    @Autowired
    private EntryPointRepository entryPointRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restEntryPointMockMvc;

    private EntryPoint entryPoint;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            EntryPointResource entryPointResource = new EntryPointResource(entryPointRepository);
        this.restEntryPointMockMvc = MockMvcBuilders.standaloneSetup(entryPointResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntryPoint createEntity(EntityManager em) {
        EntryPoint entryPoint = new EntryPoint();
        entryPoint.setPathClassMethodName(DEFAULT_PATH_CLASS_METHOD_NAME);
        entryPoint.setCount(DEFAULT_COUNT);
        entryPoint.setFalsePositive(DEFAULT_FALSE_POSITIVE);
        entryPoint.setUri(DEFAULT_URI);
        entryPoint.setHttpmethod(DEFAULT_HTTPMETHOD);
        entryPoint.setAudit(DEFAULT_AUDIT);
        entryPoint.setAverageTime(DEFAULT_AVERAGE_TIME);
        entryPoint.setCheckLaunch(DEFAULT_CHECK_LAUNCH);
        return entryPoint;
    }

    @Before
    public void initTest() {
        entryPoint = createEntity(em);
    }

    @Test
    @Transactional
    public void createEntryPoint() throws Exception {
        int databaseSizeBeforeCreate = entryPointRepository.findAll().size();

        // Create the EntryPoint

        restEntryPointMockMvc.perform(post("/api/entry-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entryPoint)))
            .andExpect(status().isCreated());

        // Validate the EntryPoint in the database
        List<EntryPoint> entryPointList = entryPointRepository.findAll();
        assertThat(entryPointList).hasSize(databaseSizeBeforeCreate + 1);
        EntryPoint testEntryPoint = entryPointList.get(entryPointList.size() - 1);
        assertThat(testEntryPoint.getPathClassMethodName()).isEqualTo(DEFAULT_PATH_CLASS_METHOD_NAME);
        assertThat(testEntryPoint.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testEntryPoint.isFalsePositive()).isEqualTo(DEFAULT_FALSE_POSITIVE);
        assertThat(testEntryPoint.getUri()).isEqualTo(DEFAULT_URI);
        assertThat(testEntryPoint.getHttpmethod()).isEqualTo(DEFAULT_HTTPMETHOD);
        assertThat(testEntryPoint.isAudit()).isEqualTo(DEFAULT_AUDIT);
        assertThat(testEntryPoint.getAverageTime()).isEqualTo(DEFAULT_AVERAGE_TIME);
        assertThat(testEntryPoint.isCheckLaunch()).isEqualTo(DEFAULT_CHECK_LAUNCH);
    }

    @Test
    @Transactional
    public void createEntryPointWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entryPointRepository.findAll().size();

        // Create the EntryPoint with an existing ID
        EntryPoint existingEntryPoint = new EntryPoint();
        existingEntryPoint.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntryPointMockMvc.perform(post("/api/entry-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingEntryPoint)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EntryPoint> entryPointList = entryPointRepository.findAll();
        assertThat(entryPointList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPathClassMethodNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = entryPointRepository.findAll().size();
        // set the field null
        entryPoint.setPathClassMethodName(null);

        // Create the EntryPoint, which fails.

        restEntryPointMockMvc.perform(post("/api/entry-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entryPoint)))
            .andExpect(status().isBadRequest());

        List<EntryPoint> entryPointList = entryPointRepository.findAll();
        assertThat(entryPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEntryPoints() throws Exception {
        // Initialize the database
        entryPointRepository.saveAndFlush(entryPoint);

        // Get all the entryPointList
        restEntryPointMockMvc.perform(get("/api/entry-points?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entryPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].pathClassMethodName").value(hasItem(DEFAULT_PATH_CLASS_METHOD_NAME.toString())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT.intValue())))
            .andExpect(jsonPath("$.[*].falsePositive").value(hasItem(DEFAULT_FALSE_POSITIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].uri").value(hasItem(DEFAULT_URI.toString())))
            .andExpect(jsonPath("$.[*].httpmethod").value(hasItem(DEFAULT_HTTPMETHOD.toString())))
            .andExpect(jsonPath("$.[*].audit").value(hasItem(DEFAULT_AUDIT.booleanValue())))
            .andExpect(jsonPath("$.[*].averageTime").value(hasItem(DEFAULT_AVERAGE_TIME.intValue())))
            .andExpect(jsonPath("$.[*].checkLaunch").value(hasItem(DEFAULT_CHECK_LAUNCH.booleanValue())));
    }

    @Test
    @Transactional
    public void getEntryPoint() throws Exception {
        // Initialize the database
        entryPointRepository.saveAndFlush(entryPoint);

        // Get the entryPoint
        restEntryPointMockMvc.perform(get("/api/entry-points/{id}", entryPoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(entryPoint.getId().intValue()))
            .andExpect(jsonPath("$.pathClassMethodName").value(DEFAULT_PATH_CLASS_METHOD_NAME.toString()))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT.intValue()))
            .andExpect(jsonPath("$.falsePositive").value(DEFAULT_FALSE_POSITIVE.booleanValue()))
            .andExpect(jsonPath("$.uri").value(DEFAULT_URI.toString()))
            .andExpect(jsonPath("$.httpmethod").value(DEFAULT_HTTPMETHOD.toString()))
            .andExpect(jsonPath("$.audit").value(DEFAULT_AUDIT.booleanValue()))
            .andExpect(jsonPath("$.averageTime").value(DEFAULT_AVERAGE_TIME.intValue()))
            .andExpect(jsonPath("$.checkLaunch").value(DEFAULT_CHECK_LAUNCH.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEntryPoint() throws Exception {
        // Get the entryPoint
        restEntryPointMockMvc.perform(get("/api/entry-points/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntryPoint() throws Exception {
        // Initialize the database
        entryPointRepository.saveAndFlush(entryPoint);
        int databaseSizeBeforeUpdate = entryPointRepository.findAll().size();

        // Update the entryPoint
        EntryPoint updatedEntryPoint = entryPointRepository.findOne(entryPoint.getId());
        updatedEntryPoint.setPathClassMethodName(UPDATED_PATH_CLASS_METHOD_NAME);
        updatedEntryPoint.setCount(UPDATED_COUNT);
        updatedEntryPoint.setFalsePositive(UPDATED_FALSE_POSITIVE);
        updatedEntryPoint.setUri(UPDATED_URI);
        updatedEntryPoint.setHttpmethod(UPDATED_HTTPMETHOD);
        updatedEntryPoint.setAudit(UPDATED_AUDIT);
        updatedEntryPoint.setAverageTime(UPDATED_AVERAGE_TIME);
        updatedEntryPoint.setCheckLaunch(UPDATED_CHECK_LAUNCH);

        restEntryPointMockMvc.perform(put("/api/entry-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntryPoint)))
            .andExpect(status().isOk());

        // Validate the EntryPoint in the database
        List<EntryPoint> entryPointList = entryPointRepository.findAll();
        assertThat(entryPointList).hasSize(databaseSizeBeforeUpdate);
        EntryPoint testEntryPoint = entryPointList.get(entryPointList.size() - 1);
        assertThat(testEntryPoint.getPathClassMethodName()).isEqualTo(UPDATED_PATH_CLASS_METHOD_NAME);
        assertThat(testEntryPoint.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testEntryPoint.isFalsePositive()).isEqualTo(UPDATED_FALSE_POSITIVE);
        assertThat(testEntryPoint.getUri()).isEqualTo(UPDATED_URI);
        assertThat(testEntryPoint.getHttpmethod()).isEqualTo(UPDATED_HTTPMETHOD);
        assertThat(testEntryPoint.isAudit()).isEqualTo(UPDATED_AUDIT);
        assertThat(testEntryPoint.getAverageTime()).isEqualTo(UPDATED_AVERAGE_TIME);
        assertThat(testEntryPoint.isCheckLaunch()).isEqualTo(UPDATED_CHECK_LAUNCH);
    }

    @Test
    @Transactional
    public void updateNonExistingEntryPoint() throws Exception {
        int databaseSizeBeforeUpdate = entryPointRepository.findAll().size();

        // Create the EntryPoint

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEntryPointMockMvc.perform(put("/api/entry-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entryPoint)))
            .andExpect(status().isCreated());

        // Validate the EntryPoint in the database
        List<EntryPoint> entryPointList = entryPointRepository.findAll();
        assertThat(entryPointList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEntryPoint() throws Exception {
        // Initialize the database
        entryPointRepository.saveAndFlush(entryPoint);
        int databaseSizeBeforeDelete = entryPointRepository.findAll().size();

        // Get the entryPoint
        restEntryPointMockMvc.perform(delete("/api/entry-points/{id}", entryPoint.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EntryPoint> entryPointList = entryPointRepository.findAll();
        assertThat(entryPointList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntryPoint.class);
    }
}
