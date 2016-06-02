package com.highway2urhell.web.rest;

import com.highway2urhell.H2HellUiApp;
import com.highway2urhell.domain.Application;
import com.highway2urhell.repository.ApplicationRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ApplicationResource REST controller.
 *
 * @see ApplicationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = H2HellUiApp.class)
@WebAppConfiguration
@IntegrationTest
public class ApplicationResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_TOKEN = "AAAAA";
    private static final String UPDATED_TOKEN = "BBBBB";

    private static final ZonedDateTime DEFAULT_DATE_CREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_CREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_CREATION_STR = dateTimeFormatter.format(DEFAULT_DATE_CREATION);
    private static final String DEFAULT_URL_APP = "AAAAA";
    private static final String UPDATED_URL_APP = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_APP_TYPE = "AAAAA";
    private static final String UPDATED_APP_TYPE = "BBBBB";

    private static final Boolean DEFAULT_IS_ANALYSED = false;
    private static final Boolean UPDATED_IS_ANALYSED = true;

    @Inject
    private ApplicationRepository applicationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restApplicationMockMvc;

    private Application application;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ApplicationResource applicationResource = new ApplicationResource();
        ReflectionTestUtils.setField(applicationResource, "applicationRepository", applicationRepository);
        this.restApplicationMockMvc = MockMvcBuilders.standaloneSetup(applicationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        application = new Application();
        application.setName(DEFAULT_NAME);
        application.setToken(DEFAULT_TOKEN);
        application.setDateCreation(DEFAULT_DATE_CREATION);
        application.setUrlApp(DEFAULT_URL_APP);
        application.setDescription(DEFAULT_DESCRIPTION);
        application.setAppType(DEFAULT_APP_TYPE);
        application.setIsAnalysed(DEFAULT_IS_ANALYSED);
    }

    @Test
    @Transactional
    public void createApplication() throws Exception {
        int databaseSizeBeforeCreate = applicationRepository.findAll().size();

        // Create the Application

        restApplicationMockMvc.perform(post("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(application)))
                .andExpect(status().isCreated());

        // Validate the Application in the database
        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeCreate + 1);
        Application testApplication = applications.get(applications.size() - 1);
        assertThat(testApplication.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApplication.getToken()).isEqualTo(DEFAULT_TOKEN);
        assertThat(testApplication.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testApplication.getUrlApp()).isEqualTo(DEFAULT_URL_APP);
        assertThat(testApplication.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testApplication.getAppType()).isEqualTo(DEFAULT_APP_TYPE);
        assertThat(testApplication.isIsAnalysed()).isEqualTo(DEFAULT_IS_ANALYSED);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setName(null);

        // Create the Application, which fails.

        restApplicationMockMvc.perform(post("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(application)))
                .andExpect(status().isBadRequest());

        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTokenIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationRepository.findAll().size();
        // set the field null
        application.setToken(null);

        // Create the Application, which fails.

        restApplicationMockMvc.perform(post("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(application)))
                .andExpect(status().isBadRequest());

        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApplications() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applications
        restApplicationMockMvc.perform(get("/api/applications?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(application.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN.toString())))
                .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION_STR)))
                .andExpect(jsonPath("$.[*].urlApp").value(hasItem(DEFAULT_URL_APP.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].appType").value(hasItem(DEFAULT_APP_TYPE.toString())))
                .andExpect(jsonPath("$.[*].isAnalysed").value(hasItem(DEFAULT_IS_ANALYSED.booleanValue())));
    }

    @Test
    @Transactional
    public void getApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get the application
        restApplicationMockMvc.perform(get("/api/applications/{id}", application.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(application.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.token").value(DEFAULT_TOKEN.toString()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION_STR))
            .andExpect(jsonPath("$.urlApp").value(DEFAULT_URL_APP.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.appType").value(DEFAULT_APP_TYPE.toString()))
            .andExpect(jsonPath("$.isAnalysed").value(DEFAULT_IS_ANALYSED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingApplication() throws Exception {
        // Get the application
        restApplicationMockMvc.perform(get("/api/applications/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();

        // Update the application
        Application updatedApplication = new Application();
        updatedApplication.setId(application.getId());
        updatedApplication.setName(UPDATED_NAME);
        updatedApplication.setToken(UPDATED_TOKEN);
        updatedApplication.setDateCreation(UPDATED_DATE_CREATION);
        updatedApplication.setUrlApp(UPDATED_URL_APP);
        updatedApplication.setDescription(UPDATED_DESCRIPTION);
        updatedApplication.setAppType(UPDATED_APP_TYPE);
        updatedApplication.setIsAnalysed(UPDATED_IS_ANALYSED);

        restApplicationMockMvc.perform(put("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedApplication)))
                .andExpect(status().isOk());

        // Validate the Application in the database
        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeUpdate);
        Application testApplication = applications.get(applications.size() - 1);
        assertThat(testApplication.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApplication.getToken()).isEqualTo(UPDATED_TOKEN);
        assertThat(testApplication.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testApplication.getUrlApp()).isEqualTo(UPDATED_URL_APP);
        assertThat(testApplication.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testApplication.getAppType()).isEqualTo(UPDATED_APP_TYPE);
        assertThat(testApplication.isIsAnalysed()).isEqualTo(UPDATED_IS_ANALYSED);
    }

    @Test
    @Transactional
    public void deleteApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);
        int databaseSizeBeforeDelete = applicationRepository.findAll().size();

        // Get the application
        restApplicationMockMvc.perform(delete("/api/applications/{id}", application.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeDelete - 1);
    }
}
