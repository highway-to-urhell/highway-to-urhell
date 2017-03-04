package com.highway2urhell.web.rest;

import com.highway2urhell.H2HellUiApp;

import com.highway2urhell.domain.EntryPointPerf;
import com.highway2urhell.repository.EntryPointPerfRepository;

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
 * Test class for the EntryPointPerfResource REST controller.
 *
 * @see EntryPointPerfResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = H2HellUiApp.class)
public class EntryPointPerfResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE_INCOMING = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_INCOMING = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final byte[] DEFAULT_PARAMETERS = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PARAMETERS = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PARAMETERS_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PARAMETERS_CONTENT_TYPE = "image/png";

    private static final Integer DEFAULT_TIME_EXEC = 1;
    private static final Integer UPDATED_TIME_EXEC = 2;

    private static final Double DEFAULT_CPU_LOAD_SYSTEM = 1D;
    private static final Double UPDATED_CPU_LOAD_SYSTEM = 2D;

    private static final Double DEFAULT_CPU_LOAD_PROCESS = 1D;
    private static final Double UPDATED_CPU_LOAD_PROCESS = 2D;

    @Autowired
    private EntryPointPerfRepository entryPointPerfRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restEntryPointPerfMockMvc;

    private EntryPointPerf entryPointPerf;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            EntryPointPerfResource entryPointPerfResource = new EntryPointPerfResource(entryPointPerfRepository);
        this.restEntryPointPerfMockMvc = MockMvcBuilders.standaloneSetup(entryPointPerfResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntryPointPerf createEntity(EntityManager em) {
        EntryPointPerf entryPointPerf = new EntryPointPerf()
                .dateIncoming(DEFAULT_DATE_INCOMING)
                .parameters(DEFAULT_PARAMETERS)
                .parametersContentType(DEFAULT_PARAMETERS_CONTENT_TYPE)
                .timeExec(DEFAULT_TIME_EXEC)
                .cpuLoadSystem(DEFAULT_CPU_LOAD_SYSTEM)
                .cpuLoadProcess(DEFAULT_CPU_LOAD_PROCESS);
        return entryPointPerf;
    }

    @Before
    public void initTest() {
        entryPointPerf = createEntity(em);
    }

    @Test
    @Transactional
    public void createEntryPointPerf() throws Exception {
        int databaseSizeBeforeCreate = entryPointPerfRepository.findAll().size();

        // Create the EntryPointPerf

        restEntryPointPerfMockMvc.perform(post("/api/entry-point-perfs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entryPointPerf)))
            .andExpect(status().isCreated());

        // Validate the EntryPointPerf in the database
        List<EntryPointPerf> entryPointPerfList = entryPointPerfRepository.findAll();
        assertThat(entryPointPerfList).hasSize(databaseSizeBeforeCreate + 1);
        EntryPointPerf testEntryPointPerf = entryPointPerfList.get(entryPointPerfList.size() - 1);
        assertThat(testEntryPointPerf.getDateIncoming()).isEqualTo(DEFAULT_DATE_INCOMING);
        assertThat(testEntryPointPerf.getParameters()).isEqualTo(DEFAULT_PARAMETERS);
        assertThat(testEntryPointPerf.getParametersContentType()).isEqualTo(DEFAULT_PARAMETERS_CONTENT_TYPE);
        assertThat(testEntryPointPerf.getTimeExec()).isEqualTo(DEFAULT_TIME_EXEC);
        assertThat(testEntryPointPerf.getCpuLoadSystem()).isEqualTo(DEFAULT_CPU_LOAD_SYSTEM);
        assertThat(testEntryPointPerf.getCpuLoadProcess()).isEqualTo(DEFAULT_CPU_LOAD_PROCESS);
    }

    @Test
    @Transactional
    public void createEntryPointPerfWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entryPointPerfRepository.findAll().size();

        // Create the EntryPointPerf with an existing ID
        EntryPointPerf existingEntryPointPerf = new EntryPointPerf();
        existingEntryPointPerf.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntryPointPerfMockMvc.perform(post("/api/entry-point-perfs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingEntryPointPerf)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EntryPointPerf> entryPointPerfList = entryPointPerfRepository.findAll();
        assertThat(entryPointPerfList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEntryPointPerfs() throws Exception {
        // Initialize the database
        entryPointPerfRepository.saveAndFlush(entryPointPerf);

        // Get all the entryPointPerfList
        restEntryPointPerfMockMvc.perform(get("/api/entry-point-perfs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entryPointPerf.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateIncoming").value(hasItem(sameInstant(DEFAULT_DATE_INCOMING))))
            .andExpect(jsonPath("$.[*].parametersContentType").value(hasItem(DEFAULT_PARAMETERS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].parameters").value(hasItem(Base64Utils.encodeToString(DEFAULT_PARAMETERS))))
            .andExpect(jsonPath("$.[*].timeExec").value(hasItem(DEFAULT_TIME_EXEC)))
            .andExpect(jsonPath("$.[*].cpuLoadSystem").value(hasItem(DEFAULT_CPU_LOAD_SYSTEM.doubleValue())))
            .andExpect(jsonPath("$.[*].cpuLoadProcess").value(hasItem(DEFAULT_CPU_LOAD_PROCESS.doubleValue())));
    }

    @Test
    @Transactional
    public void getEntryPointPerf() throws Exception {
        // Initialize the database
        entryPointPerfRepository.saveAndFlush(entryPointPerf);

        // Get the entryPointPerf
        restEntryPointPerfMockMvc.perform(get("/api/entry-point-perfs/{id}", entryPointPerf.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(entryPointPerf.getId().intValue()))
            .andExpect(jsonPath("$.dateIncoming").value(sameInstant(DEFAULT_DATE_INCOMING)))
            .andExpect(jsonPath("$.parametersContentType").value(DEFAULT_PARAMETERS_CONTENT_TYPE))
            .andExpect(jsonPath("$.parameters").value(Base64Utils.encodeToString(DEFAULT_PARAMETERS)))
            .andExpect(jsonPath("$.timeExec").value(DEFAULT_TIME_EXEC))
            .andExpect(jsonPath("$.cpuLoadSystem").value(DEFAULT_CPU_LOAD_SYSTEM.doubleValue()))
            .andExpect(jsonPath("$.cpuLoadProcess").value(DEFAULT_CPU_LOAD_PROCESS.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEntryPointPerf() throws Exception {
        // Get the entryPointPerf
        restEntryPointPerfMockMvc.perform(get("/api/entry-point-perfs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntryPointPerf() throws Exception {
        // Initialize the database
        entryPointPerfRepository.saveAndFlush(entryPointPerf);
        int databaseSizeBeforeUpdate = entryPointPerfRepository.findAll().size();

        // Update the entryPointPerf
        EntryPointPerf updatedEntryPointPerf = entryPointPerfRepository.findOne(entryPointPerf.getId());
        updatedEntryPointPerf
                .dateIncoming(UPDATED_DATE_INCOMING)
                .parameters(UPDATED_PARAMETERS)
                .parametersContentType(UPDATED_PARAMETERS_CONTENT_TYPE)
                .timeExec(UPDATED_TIME_EXEC)
                .cpuLoadSystem(UPDATED_CPU_LOAD_SYSTEM)
                .cpuLoadProcess(UPDATED_CPU_LOAD_PROCESS);

        restEntryPointPerfMockMvc.perform(put("/api/entry-point-perfs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntryPointPerf)))
            .andExpect(status().isOk());

        // Validate the EntryPointPerf in the database
        List<EntryPointPerf> entryPointPerfList = entryPointPerfRepository.findAll();
        assertThat(entryPointPerfList).hasSize(databaseSizeBeforeUpdate);
        EntryPointPerf testEntryPointPerf = entryPointPerfList.get(entryPointPerfList.size() - 1);
        assertThat(testEntryPointPerf.getDateIncoming()).isEqualTo(UPDATED_DATE_INCOMING);
        assertThat(testEntryPointPerf.getParameters()).isEqualTo(UPDATED_PARAMETERS);
        assertThat(testEntryPointPerf.getParametersContentType()).isEqualTo(UPDATED_PARAMETERS_CONTENT_TYPE);
        assertThat(testEntryPointPerf.getTimeExec()).isEqualTo(UPDATED_TIME_EXEC);
        assertThat(testEntryPointPerf.getCpuLoadSystem()).isEqualTo(UPDATED_CPU_LOAD_SYSTEM);
        assertThat(testEntryPointPerf.getCpuLoadProcess()).isEqualTo(UPDATED_CPU_LOAD_PROCESS);
    }

    @Test
    @Transactional
    public void updateNonExistingEntryPointPerf() throws Exception {
        int databaseSizeBeforeUpdate = entryPointPerfRepository.findAll().size();

        // Create the EntryPointPerf

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEntryPointPerfMockMvc.perform(put("/api/entry-point-perfs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entryPointPerf)))
            .andExpect(status().isCreated());

        // Validate the EntryPointPerf in the database
        List<EntryPointPerf> entryPointPerfList = entryPointPerfRepository.findAll();
        assertThat(entryPointPerfList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEntryPointPerf() throws Exception {
        // Initialize the database
        entryPointPerfRepository.saveAndFlush(entryPointPerf);
        int databaseSizeBeforeDelete = entryPointPerfRepository.findAll().size();

        // Get the entryPointPerf
        restEntryPointPerfMockMvc.perform(delete("/api/entry-point-perfs/{id}", entryPointPerf.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EntryPointPerf> entryPointPerfList = entryPointPerfRepository.findAll();
        assertThat(entryPointPerfList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntryPointPerf.class);
    }
}
