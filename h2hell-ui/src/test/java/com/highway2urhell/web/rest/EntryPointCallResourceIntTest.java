package com.highway2urhell.web.rest;

import com.highway2urhell.H2HellUiApp;

import com.highway2urhell.domain.EntryPointCall;
import com.highway2urhell.repository.EntryPointCallRepository;

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
 * Test class for the EntryPointCallResource REST controller.
 *
 * @see EntryPointCallResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = H2HellUiApp.class)
public class EntryPointCallResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE_INCOMING = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_INCOMING = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final byte[] DEFAULT_PARAMETERS = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PARAMETERS = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PARAMETERS_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PARAMETERS_CONTENT_TYPE = "image/png";

    @Autowired
    private EntryPointCallRepository entryPointCallRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restEntryPointCallMockMvc;

    private EntryPointCall entryPointCall;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            EntryPointCallResource entryPointCallResource = new EntryPointCallResource(entryPointCallRepository);
        this.restEntryPointCallMockMvc = MockMvcBuilders.standaloneSetup(entryPointCallResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntryPointCall createEntity(EntityManager em) {
        EntryPointCall entryPointCall = new EntryPointCall();
        entryPointCall.setDateIncoming(DEFAULT_DATE_INCOMING);
        entryPointCall.setParameters(DEFAULT_PARAMETERS);
        entryPointCall.setParametersContentType(DEFAULT_PARAMETERS_CONTENT_TYPE);
        return entryPointCall;
    }

    @Before
    public void initTest() {
        entryPointCall = createEntity(em);
    }

    @Test
    @Transactional
    public void createEntryPointCall() throws Exception {
        int databaseSizeBeforeCreate = entryPointCallRepository.findAll().size();

        // Create the EntryPointCall

        restEntryPointCallMockMvc.perform(post("/api/entry-point-calls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entryPointCall)))
            .andExpect(status().isCreated());

        // Validate the EntryPointCall in the database
        List<EntryPointCall> entryPointCallList = entryPointCallRepository.findAll();
        assertThat(entryPointCallList).hasSize(databaseSizeBeforeCreate + 1);
        EntryPointCall testEntryPointCall = entryPointCallList.get(entryPointCallList.size() - 1);
        assertThat(testEntryPointCall.getDateIncoming()).isEqualTo(DEFAULT_DATE_INCOMING);
        assertThat(testEntryPointCall.getParameters()).isEqualTo(DEFAULT_PARAMETERS);
        assertThat(testEntryPointCall.getParametersContentType()).isEqualTo(DEFAULT_PARAMETERS_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createEntryPointCallWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entryPointCallRepository.findAll().size();

        // Create the EntryPointCall with an existing ID
        EntryPointCall existingEntryPointCall = new EntryPointCall();
        existingEntryPointCall.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntryPointCallMockMvc.perform(post("/api/entry-point-calls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingEntryPointCall)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EntryPointCall> entryPointCallList = entryPointCallRepository.findAll();
        assertThat(entryPointCallList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEntryPointCalls() throws Exception {
        // Initialize the database
        entryPointCallRepository.saveAndFlush(entryPointCall);

        // Get all the entryPointCallList
        restEntryPointCallMockMvc.perform(get("/api/entry-point-calls?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entryPointCall.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateIncoming").value(hasItem(sameInstant(DEFAULT_DATE_INCOMING))))
            .andExpect(jsonPath("$.[*].parametersContentType").value(hasItem(DEFAULT_PARAMETERS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].parameters").value(hasItem(Base64Utils.encodeToString(DEFAULT_PARAMETERS))));
    }

    @Test
    @Transactional
    public void getEntryPointCall() throws Exception {
        // Initialize the database
        entryPointCallRepository.saveAndFlush(entryPointCall);

        // Get the entryPointCall
        restEntryPointCallMockMvc.perform(get("/api/entry-point-calls/{id}", entryPointCall.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(entryPointCall.getId().intValue()))
            .andExpect(jsonPath("$.dateIncoming").value(sameInstant(DEFAULT_DATE_INCOMING)))
            .andExpect(jsonPath("$.parametersContentType").value(DEFAULT_PARAMETERS_CONTENT_TYPE))
            .andExpect(jsonPath("$.parameters").value(Base64Utils.encodeToString(DEFAULT_PARAMETERS)));
    }

    @Test
    @Transactional
    public void getNonExistingEntryPointCall() throws Exception {
        // Get the entryPointCall
        restEntryPointCallMockMvc.perform(get("/api/entry-point-calls/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntryPointCall() throws Exception {
        // Initialize the database
        entryPointCallRepository.saveAndFlush(entryPointCall);
        int databaseSizeBeforeUpdate = entryPointCallRepository.findAll().size();

        // Update the entryPointCall
        EntryPointCall updatedEntryPointCall = entryPointCallRepository.findOne(entryPointCall.getId());
        updatedEntryPointCall.setDateIncoming(UPDATED_DATE_INCOMING);
        updatedEntryPointCall.setParameters(UPDATED_PARAMETERS);
        updatedEntryPointCall.setParametersContentType(UPDATED_PARAMETERS_CONTENT_TYPE);

        restEntryPointCallMockMvc.perform(put("/api/entry-point-calls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntryPointCall)))
            .andExpect(status().isOk());

        // Validate the EntryPointCall in the database
        List<EntryPointCall> entryPointCallList = entryPointCallRepository.findAll();
        assertThat(entryPointCallList).hasSize(databaseSizeBeforeUpdate);
        EntryPointCall testEntryPointCall = entryPointCallList.get(entryPointCallList.size() - 1);
        assertThat(testEntryPointCall.getDateIncoming()).isEqualTo(UPDATED_DATE_INCOMING);
        assertThat(testEntryPointCall.getParameters()).isEqualTo(UPDATED_PARAMETERS);
        assertThat(testEntryPointCall.getParametersContentType()).isEqualTo(UPDATED_PARAMETERS_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingEntryPointCall() throws Exception {
        int databaseSizeBeforeUpdate = entryPointCallRepository.findAll().size();

        // Create the EntryPointCall

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEntryPointCallMockMvc.perform(put("/api/entry-point-calls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entryPointCall)))
            .andExpect(status().isCreated());

        // Validate the EntryPointCall in the database
        List<EntryPointCall> entryPointCallList = entryPointCallRepository.findAll();
        assertThat(entryPointCallList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEntryPointCall() throws Exception {
        // Initialize the database
        entryPointCallRepository.saveAndFlush(entryPointCall);
        int databaseSizeBeforeDelete = entryPointCallRepository.findAll().size();

        // Get the entryPointCall
        restEntryPointCallMockMvc.perform(delete("/api/entry-point-calls/{id}", entryPointCall.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EntryPointCall> entryPointCallList = entryPointCallRepository.findAll();
        assertThat(entryPointCallList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntryPointCall.class);
    }
}
