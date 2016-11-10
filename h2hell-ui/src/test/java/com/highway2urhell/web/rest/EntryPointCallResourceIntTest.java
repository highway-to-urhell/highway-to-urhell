package com.highway2urhell.web.rest;

import com.highway2urhell.H2HellUiApp;

import com.highway2urhell.domain.EntryPointCall;
import com.highway2urhell.repository.EntryPointCallRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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

    private static final ZonedDateTime DEFAULT_DATE_INCOMING = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_INCOMING = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_INCOMING_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_DATE_INCOMING);

    private static final byte[] DEFAULT_PARAMETERS = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PARAMETERS = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PARAMETERS_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PARAMETERS_CONTENT_TYPE = "image/png";

    @Inject
    private EntryPointCallRepository entryPointCallRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEntryPointCallMockMvc;

    private EntryPointCall entryPointCall;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EntryPointCallResource entryPointCallResource = new EntryPointCallResource();
        ReflectionTestUtils.setField(entryPointCallResource, "entryPointCallRepository", entryPointCallRepository);
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
        List<EntryPointCall> entryPointCalls = entryPointCallRepository.findAll();
        assertThat(entryPointCalls).hasSize(databaseSizeBeforeCreate + 1);
        EntryPointCall testEntryPointCall = entryPointCalls.get(entryPointCalls.size() - 1);
        assertThat(testEntryPointCall.getDateIncoming()).isEqualTo(DEFAULT_DATE_INCOMING);
        assertThat(testEntryPointCall.getParameters()).isEqualTo(DEFAULT_PARAMETERS);
        assertThat(testEntryPointCall.getParametersContentType()).isEqualTo(DEFAULT_PARAMETERS_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllEntryPointCalls() throws Exception {
        // Initialize the database
        entryPointCallRepository.saveAndFlush(entryPointCall);

        // Get all the entryPointCalls
        restEntryPointCallMockMvc.perform(get("/api/entry-point-calls?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(entryPointCall.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateIncoming").value(hasItem(DEFAULT_DATE_INCOMING_STR)))
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
            .andExpect(jsonPath("$.dateIncoming").value(DEFAULT_DATE_INCOMING_STR))
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
        List<EntryPointCall> entryPointCalls = entryPointCallRepository.findAll();
        assertThat(entryPointCalls).hasSize(databaseSizeBeforeUpdate);
        EntryPointCall testEntryPointCall = entryPointCalls.get(entryPointCalls.size() - 1);
        assertThat(testEntryPointCall.getDateIncoming()).isEqualTo(UPDATED_DATE_INCOMING);
        assertThat(testEntryPointCall.getParameters()).isEqualTo(UPDATED_PARAMETERS);
        assertThat(testEntryPointCall.getParametersContentType()).isEqualTo(UPDATED_PARAMETERS_CONTENT_TYPE);
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
        List<EntryPointCall> entryPointCalls = entryPointCallRepository.findAll();
        assertThat(entryPointCalls).hasSize(databaseSizeBeforeDelete - 1);
    }
}
