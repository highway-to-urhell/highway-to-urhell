package com.highway2urhell.web.rest;

import com.highway2urhell.H2HellUiApp;

import com.highway2urhell.domain.EntryPointParameters;
import com.highway2urhell.repository.EntryPointParametersRepository;

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
import org.springframework.util.Base64Utils;

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
 * Test class for the EntryPointParametersResource REST controller.
 *
 * @see EntryPointParametersResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = H2HellUiApp.class)
public class EntryPointParametersResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE_INCOMING = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_INCOMING = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final byte[] DEFAULT_PARAMETERS = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PARAMETERS = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PARAMETERS_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PARAMETERS_CONTENT_TYPE = "image/png";

    @Autowired
    private EntryPointParametersRepository entryPointParametersRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restEntryPointParametersMockMvc;

    private EntryPointParameters entryPointParameters;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            EntryPointParametersResource entryPointParametersResource = new EntryPointParametersResource(entryPointParametersRepository);
        this.restEntryPointParametersMockMvc = MockMvcBuilders.standaloneSetup(entryPointParametersResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntryPointParameters createEntity(EntityManager em) {
        EntryPointParameters entryPointParameters = new EntryPointParameters()
                .dateIncoming(DEFAULT_DATE_INCOMING)
                .parameters(DEFAULT_PARAMETERS)
                .parametersContentType(DEFAULT_PARAMETERS_CONTENT_TYPE);
        return entryPointParameters;
    }

    @Before
    public void initTest() {
        entryPointParameters = createEntity(em);
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
        List<EntryPointParameters> entryPointParametersList = entryPointParametersRepository.findAll();
        assertThat(entryPointParametersList).hasSize(databaseSizeBeforeCreate + 1);
        EntryPointParameters testEntryPointParameters = entryPointParametersList.get(entryPointParametersList.size() - 1);
        assertThat(testEntryPointParameters.getDateIncoming()).isEqualTo(DEFAULT_DATE_INCOMING);
        assertThat(testEntryPointParameters.getParameters()).isEqualTo(DEFAULT_PARAMETERS);
        assertThat(testEntryPointParameters.getParametersContentType()).isEqualTo(DEFAULT_PARAMETERS_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createEntryPointParametersWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entryPointParametersRepository.findAll().size();

        // Create the EntryPointParameters with an existing ID
        EntryPointParameters existingEntryPointParameters = new EntryPointParameters();
        existingEntryPointParameters.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntryPointParametersMockMvc.perform(post("/api/entry-point-parameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingEntryPointParameters)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EntryPointParameters> entryPointParametersList = entryPointParametersRepository.findAll();
        assertThat(entryPointParametersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEntryPointParameters() throws Exception {
        // Initialize the database
        entryPointParametersRepository.saveAndFlush(entryPointParameters);

        // Get all the entryPointParametersList
        restEntryPointParametersMockMvc.perform(get("/api/entry-point-parameters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entryPointParameters.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateIncoming").value(hasItem(sameInstant(DEFAULT_DATE_INCOMING))))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(entryPointParameters.getId().intValue()))
            .andExpect(jsonPath("$.dateIncoming").value(sameInstant(DEFAULT_DATE_INCOMING)))
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
        EntryPointParameters updatedEntryPointParameters = entryPointParametersRepository.findOne(entryPointParameters.getId());
        updatedEntryPointParameters
                .dateIncoming(UPDATED_DATE_INCOMING)
                .parameters(UPDATED_PARAMETERS)
                .parametersContentType(UPDATED_PARAMETERS_CONTENT_TYPE);

        restEntryPointParametersMockMvc.perform(put("/api/entry-point-parameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntryPointParameters)))
            .andExpect(status().isOk());

        // Validate the EntryPointParameters in the database
        List<EntryPointParameters> entryPointParametersList = entryPointParametersRepository.findAll();
        assertThat(entryPointParametersList).hasSize(databaseSizeBeforeUpdate);
        EntryPointParameters testEntryPointParameters = entryPointParametersList.get(entryPointParametersList.size() - 1);
        assertThat(testEntryPointParameters.getDateIncoming()).isEqualTo(UPDATED_DATE_INCOMING);
        assertThat(testEntryPointParameters.getParameters()).isEqualTo(UPDATED_PARAMETERS);
        assertThat(testEntryPointParameters.getParametersContentType()).isEqualTo(UPDATED_PARAMETERS_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingEntryPointParameters() throws Exception {
        int databaseSizeBeforeUpdate = entryPointParametersRepository.findAll().size();

        // Create the EntryPointParameters

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEntryPointParametersMockMvc.perform(put("/api/entry-point-parameters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entryPointParameters)))
            .andExpect(status().isCreated());

        // Validate the EntryPointParameters in the database
        List<EntryPointParameters> entryPointParametersList = entryPointParametersRepository.findAll();
        assertThat(entryPointParametersList).hasSize(databaseSizeBeforeUpdate + 1);
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
        List<EntryPointParameters> entryPointParametersList = entryPointParametersRepository.findAll();
        assertThat(entryPointParametersList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntryPointParameters.class);
    }
}
