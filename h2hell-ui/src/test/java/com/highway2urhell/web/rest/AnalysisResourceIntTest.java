package com.highway2urhell.web.rest;

import com.highway2urhell.H2HellUiApp;

import com.highway2urhell.domain.Analysis;
import com.highway2urhell.repository.AnalysisRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.highway2urhell.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AnalysisResource REST controller.
 *
 * @see AnalysisResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = H2HellUiApp.class)
public class AnalysisResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_PATH_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_PATH_SOURCE = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_ENTRY_POINTS = 1;
    private static final Integer UPDATED_NUMBER_ENTRY_POINTS = 2;

    private static final String DEFAULT_APP_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_APP_VERSION = "BBBBBBBBBB";

    @Autowired
    private AnalysisRepository analysisRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restAnalysisMockMvc;

    private Analysis analysis;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            AnalysisResource analysisResource = new AnalysisResource(analysisRepository);
        this.restAnalysisMockMvc = MockMvcBuilders.standaloneSetup(analysisResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Analysis createEntity(EntityManager em) {
        Analysis analysis = new Analysis();
        analysis.setDateCreation(DEFAULT_DATE_CREATION);
        analysis.setPathSource(DEFAULT_PATH_SOURCE);
        analysis.setNumberEntryPoints(DEFAULT_NUMBER_ENTRY_POINTS);
        analysis.setAppVersion(DEFAULT_APP_VERSION);
        return analysis;
    }

    @Before
    public void initTest() {
        analysis = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnalysis() throws Exception {
        int databaseSizeBeforeCreate = analysisRepository.findAll().size();

        // Create the Analysis

        restAnalysisMockMvc.perform(post("/api/analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analysis)))
            .andExpect(status().isCreated());

        // Validate the Analysis in the database
        List<Analysis> analysisList = analysisRepository.findAll();
        assertThat(analysisList).hasSize(databaseSizeBeforeCreate + 1);
        Analysis testAnalysis = analysisList.get(analysisList.size() - 1);
        assertThat(testAnalysis.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testAnalysis.getPathSource()).isEqualTo(DEFAULT_PATH_SOURCE);
        assertThat(testAnalysis.getNumberEntryPoints()).isEqualTo(DEFAULT_NUMBER_ENTRY_POINTS);
        assertThat(testAnalysis.getAppVersion()).isEqualTo(DEFAULT_APP_VERSION);
    }

    @Test
    @Transactional
    public void createAnalysisWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = analysisRepository.findAll().size();

        // Create the Analysis with an existing ID
        Analysis existingAnalysis = new Analysis();
        existingAnalysis.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnalysisMockMvc.perform(post("/api/analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingAnalysis)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Analysis> analysisList = analysisRepository.findAll();
        assertThat(analysisList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAnalyses() throws Exception {
        // Initialize the database
        analysisRepository.saveAndFlush(analysis);

        // Get all the analysisList
        restAnalysisMockMvc.perform(get("/api/analyses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analysis.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(sameInstant(DEFAULT_DATE_CREATION))))
            .andExpect(jsonPath("$.[*].pathSource").value(hasItem(DEFAULT_PATH_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].numberEntryPoints").value(hasItem(DEFAULT_NUMBER_ENTRY_POINTS)))
            .andExpect(jsonPath("$.[*].appVersion").value(hasItem(DEFAULT_APP_VERSION.toString())));
    }

    @Test
    @Transactional
    public void getAnalysis() throws Exception {
        // Initialize the database
        analysisRepository.saveAndFlush(analysis);

        // Get the analysis
        restAnalysisMockMvc.perform(get("/api/analyses/{id}", analysis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(analysis.getId().intValue()))
            .andExpect(jsonPath("$.dateCreation").value(sameInstant(DEFAULT_DATE_CREATION)))
            .andExpect(jsonPath("$.pathSource").value(DEFAULT_PATH_SOURCE.toString()))
            .andExpect(jsonPath("$.numberEntryPoints").value(DEFAULT_NUMBER_ENTRY_POINTS))
            .andExpect(jsonPath("$.appVersion").value(DEFAULT_APP_VERSION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAnalysis() throws Exception {
        // Get the analysis
        restAnalysisMockMvc.perform(get("/api/analyses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnalysis() throws Exception {
        // Initialize the database
        analysisRepository.saveAndFlush(analysis);
        int databaseSizeBeforeUpdate = analysisRepository.findAll().size();

        // Update the analysis
        Analysis updatedAnalysis = analysisRepository.findOne(analysis.getId());
        updatedAnalysis.setDateCreation(UPDATED_DATE_CREATION);
        updatedAnalysis.setPathSource(UPDATED_PATH_SOURCE);
        updatedAnalysis.setNumberEntryPoints(UPDATED_NUMBER_ENTRY_POINTS);
        updatedAnalysis.setAppVersion(UPDATED_APP_VERSION);

        restAnalysisMockMvc.perform(put("/api/analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnalysis)))
            .andExpect(status().isOk());

        // Validate the Analysis in the database
        List<Analysis> analysisList = analysisRepository.findAll();
        assertThat(analysisList).hasSize(databaseSizeBeforeUpdate);
        Analysis testAnalysis = analysisList.get(analysisList.size() - 1);
        assertThat(testAnalysis.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testAnalysis.getPathSource()).isEqualTo(UPDATED_PATH_SOURCE);
        assertThat(testAnalysis.getNumberEntryPoints()).isEqualTo(UPDATED_NUMBER_ENTRY_POINTS);
        assertThat(testAnalysis.getAppVersion()).isEqualTo(UPDATED_APP_VERSION);
    }

    @Test
    @Transactional
    public void updateNonExistingAnalysis() throws Exception {
        int databaseSizeBeforeUpdate = analysisRepository.findAll().size();

        // Create the Analysis

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAnalysisMockMvc.perform(put("/api/analyses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analysis)))
            .andExpect(status().isCreated());

        // Validate the Analysis in the database
        List<Analysis> analysisList = analysisRepository.findAll();
        assertThat(analysisList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAnalysis() throws Exception {
        // Initialize the database
        analysisRepository.saveAndFlush(analysis);
        int databaseSizeBeforeDelete = analysisRepository.findAll().size();

        // Get the analysis
        restAnalysisMockMvc.perform(delete("/api/analyses/{id}", analysis.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Analysis> analysisList = analysisRepository.findAll();
        assertThat(analysisList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Analysis.class);
    }
}
