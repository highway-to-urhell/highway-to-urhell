package com.highway2urhell.web.rest;

import com.highway2urhell.H2HellUiApp;
import com.highway2urhell.domain.EntryPointParameters;
import com.highway2urhell.repository.EntryPointParametersRepository;

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
import org.springframework.util.Base64Utils;

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
 * Test class for the EntryPointParametersResource REST controller.
 *
 * @see EntryPointParametersResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = H2HellUiApp.class)
@WebAppConfiguration
@IntegrationTest
public class EntryPointParametersResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_DATE_INCOMING = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_INCOMING = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_INCOMING_STR = dateTimeFormatter.format(DEFAULT_DATE_INCOMING);

    private static final byte[] DEFAULT_PARAMETERS = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PARAMETERS = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PARAMETERS_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PARAMETERS_CONTENT_TYPE = "image/png";

    @Inject
    private EntryPointParametersRepository entryPointParametersRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEntryPointParametersMockMvc;

    private EntryPointParameters entryPointParameters;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EntryPointParametersResource entryPointParametersResource = new EntryPointParametersResource();
        ReflectionTestUtils.setField(entryPointParametersResource, "entryPointParametersRepository", entryPointParametersRepository);
        this.restEntryPointParametersMockMvc = MockMvcBuilders.standaloneSetup(entryPointParametersResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        entryPointParameters = new EntryPointParameters();
        entryPointParameters.setDateIncoming(DEFAULT_DATE_INCOMING);
        entryPointParameters.setParameters(DEFAULT_PARAMETERS);
        entryPointParameters.setParametersContentType(DEFAULT_PARAMETERS_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createEntryPointParameters() throws Exception {
        int databaseSizeBeforeCreate = entryPointParametersRepository.findAll().size();

        // Create the EntryPointParameters

        restEntryPointParametersMockMvc.perform(post("/api/entry-point-parameters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(entryPointParameters)))
                .andExpect(status().isCreated());

        // Validate the EntryPointParameters in the database
        List<EntryPointParameters> entryPointParameters = entryPointParametersRepository.findAll();
        assertThat(entryPointParameters).hasSize(databaseSizeBeforeCreate + 1);
        EntryPointParameters testEntryPointParameters = entryPointParameters.get(entryPointParameters.size() - 1);
        assertThat(testEntryPointParameters.getDateIncoming()).isEqualTo(DEFAULT_DATE_INCOMING);
        assertThat(testEntryPointParameters.getParameters()).isEqualTo(DEFAULT_PARAMETERS);
        assertThat(testEntryPointParameters.getParametersContentType()).isEqualTo(DEFAULT_PARAMETERS_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllEntryPointParameters() throws Exception {
        // Initialize the database
        entryPointParametersRepository.saveAndFlush(entryPointParameters);

        // Get all the entryPointParameters
        restEntryPointParametersMockMvc.perform(get("/api/entry-point-parameters?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(entryPointParameters.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateIncoming").value(hasItem(DEFAULT_DATE_INCOMING_STR)))
                .andExpect(jsonPath("$.[*].parametersContentType").value(hasItem(DEFAULT_PARAMETERS_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].parameters").value(hasItem(Base64Utils.encodeToString(DEFAULT_PARAMETERS))));
    }

    @Test
    @Transactional
    public void getEntryPointParameters() throws Exception {
        // Initialize the database
        entryPointParametersRepository.saveAndFlush(entryPointParameters);

        // Get the entryPointParameters
        restEntryPointParametersMockMvc.perform(get("/api/entry-point-parameters/{id}", entryPointParameters.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(entryPointParameters.getId().intValue()))
            .andExpect(jsonPath("$.dateIncoming").value(DEFAULT_DATE_INCOMING_STR))
            .andExpect(jsonPath("$.parametersContentType").value(DEFAULT_PARAMETERS_CONTENT_TYPE))
            .andExpect(jsonPath("$.parameters").value(Base64Utils.encodeToString(DEFAULT_PARAMETERS)));
    }

    @Test
    @Transactional
    public void getNonExistingEntryPointParameters() throws Exception {
        // Get the entryPointParameters
        restEntryPointParametersMockMvc.perform(get("/api/entry-point-parameters/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntryPointParameters() throws Exception {
        // Initialize the database
        entryPointParametersRepository.saveAndFlush(entryPointParameters);
        int databaseSizeBeforeUpdate = entryPointParametersRepository.findAll().size();

        // Update the entryPointParameters
        EntryPointParameters updatedEntryPointParameters = new EntryPointParameters();
        updatedEntryPointParameters.setId(entryPointParameters.getId());
        updatedEntryPointParameters.setDateIncoming(UPDATED_DATE_INCOMING);
        updatedEntryPointParameters.setParameters(UPDATED_PARAMETERS);
        updatedEntryPointParameters.setParametersContentType(UPDATED_PARAMETERS_CONTENT_TYPE);

        restEntryPointParametersMockMvc.perform(put("/api/entry-point-parameters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEntryPointParameters)))
                .andExpect(status().isOk());

        // Validate the EntryPointParameters in the database
        List<EntryPointParameters> entryPointParameters = entryPointParametersRepository.findAll();
        assertThat(entryPointParameters).hasSize(databaseSizeBeforeUpdate);
        EntryPointParameters testEntryPointParameters = entryPointParameters.get(entryPointParameters.size() - 1);
        assertThat(testEntryPointParameters.getDateIncoming()).isEqualTo(UPDATED_DATE_INCOMING);
        assertThat(testEntryPointParameters.getParameters()).isEqualTo(UPDATED_PARAMETERS);
        assertThat(testEntryPointParameters.getParametersContentType()).isEqualTo(UPDATED_PARAMETERS_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void deleteEntryPointParameters() throws Exception {
        // Initialize the database
        entryPointParametersRepository.saveAndFlush(entryPointParameters);
        int databaseSizeBeforeDelete = entryPointParametersRepository.findAll().size();

        // Get the entryPointParameters
        restEntryPointParametersMockMvc.perform(delete("/api/entry-point-parameters/{id}", entryPointParameters.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<EntryPointParameters> entryPointParameters = entryPointParametersRepository.findAll();
        assertThat(entryPointParameters).hasSize(databaseSizeBeforeDelete - 1);
    }
}
