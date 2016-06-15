package com.highway2urhell.web.rest;

import com.highway2urhell.H2HellUiApp;
import com.highway2urhell.domain.EntryPointPerf;
import com.highway2urhell.repository.EntryPointPerfRepository;

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
 * Test class for the EntryPointPerfResource REST controller.
 *
 * @see EntryPointPerfResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = H2HellUiApp.class)
@WebAppConfiguration
@IntegrationTest
public class EntryPointPerfResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_DATE_INCOMING = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_INCOMING = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_INCOMING_STR = dateTimeFormatter.format(DEFAULT_DATE_INCOMING);

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

    @Inject
    private EntryPointPerfRepository entryPointPerfRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEntryPointPerfMockMvc;

    private EntryPointPerf entryPointPerf;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EntryPointPerfResource entryPointPerfResource = new EntryPointPerfResource();
        ReflectionTestUtils.setField(entryPointPerfResource, "entryPointPerfRepository", entryPointPerfRepository);
        this.restEntryPointPerfMockMvc = MockMvcBuilders.standaloneSetup(entryPointPerfResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        entryPointPerf = new EntryPointPerf();
        entryPointPerf.setDateIncoming(DEFAULT_DATE_INCOMING);
        entryPointPerf.setParameters(DEFAULT_PARAMETERS);
        entryPointPerf.setParametersContentType(DEFAULT_PARAMETERS_CONTENT_TYPE);
        entryPointPerf.setTimeExec(DEFAULT_TIME_EXEC);
        entryPointPerf.setCpuLoadSystem(DEFAULT_CPU_LOAD_SYSTEM);
        entryPointPerf.setCpuLoadProcess(DEFAULT_CPU_LOAD_PROCESS);
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
        List<EntryPointPerf> entryPointPerfs = entryPointPerfRepository.findAll();
        assertThat(entryPointPerfs).hasSize(databaseSizeBeforeCreate + 1);
        EntryPointPerf testEntryPointPerf = entryPointPerfs.get(entryPointPerfs.size() - 1);
        assertThat(testEntryPointPerf.getDateIncoming()).isEqualTo(DEFAULT_DATE_INCOMING);
        assertThat(testEntryPointPerf.getParameters()).isEqualTo(DEFAULT_PARAMETERS);
        assertThat(testEntryPointPerf.getParametersContentType()).isEqualTo(DEFAULT_PARAMETERS_CONTENT_TYPE);
        assertThat(testEntryPointPerf.getTimeExec()).isEqualTo(DEFAULT_TIME_EXEC);
        assertThat(testEntryPointPerf.getCpuLoadSystem()).isEqualTo(DEFAULT_CPU_LOAD_SYSTEM);
        assertThat(testEntryPointPerf.getCpuLoadProcess()).isEqualTo(DEFAULT_CPU_LOAD_PROCESS);
    }

    @Test
    @Transactional
    public void getAllEntryPointPerfs() throws Exception {
        // Initialize the database
        entryPointPerfRepository.saveAndFlush(entryPointPerf);

        // Get all the entryPointPerfs
        restEntryPointPerfMockMvc.perform(get("/api/entry-point-perfs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(entryPointPerf.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateIncoming").value(hasItem(DEFAULT_DATE_INCOMING_STR)))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(entryPointPerf.getId().intValue()))
            .andExpect(jsonPath("$.dateIncoming").value(DEFAULT_DATE_INCOMING_STR))
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
        EntryPointPerf updatedEntryPointPerf = new EntryPointPerf();
        updatedEntryPointPerf.setId(entryPointPerf.getId());
        updatedEntryPointPerf.setDateIncoming(UPDATED_DATE_INCOMING);
        updatedEntryPointPerf.setParameters(UPDATED_PARAMETERS);
        updatedEntryPointPerf.setParametersContentType(UPDATED_PARAMETERS_CONTENT_TYPE);
        updatedEntryPointPerf.setTimeExec(UPDATED_TIME_EXEC);
        updatedEntryPointPerf.setCpuLoadSystem(UPDATED_CPU_LOAD_SYSTEM);
        updatedEntryPointPerf.setCpuLoadProcess(UPDATED_CPU_LOAD_PROCESS);

        restEntryPointPerfMockMvc.perform(put("/api/entry-point-perfs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEntryPointPerf)))
                .andExpect(status().isOk());

        // Validate the EntryPointPerf in the database
        List<EntryPointPerf> entryPointPerfs = entryPointPerfRepository.findAll();
        assertThat(entryPointPerfs).hasSize(databaseSizeBeforeUpdate);
        EntryPointPerf testEntryPointPerf = entryPointPerfs.get(entryPointPerfs.size() - 1);
        assertThat(testEntryPointPerf.getDateIncoming()).isEqualTo(UPDATED_DATE_INCOMING);
        assertThat(testEntryPointPerf.getParameters()).isEqualTo(UPDATED_PARAMETERS);
        assertThat(testEntryPointPerf.getParametersContentType()).isEqualTo(UPDATED_PARAMETERS_CONTENT_TYPE);
        assertThat(testEntryPointPerf.getTimeExec()).isEqualTo(UPDATED_TIME_EXEC);
        assertThat(testEntryPointPerf.getCpuLoadSystem()).isEqualTo(UPDATED_CPU_LOAD_SYSTEM);
        assertThat(testEntryPointPerf.getCpuLoadProcess()).isEqualTo(UPDATED_CPU_LOAD_PROCESS);
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
        List<EntryPointPerf> entryPointPerfs = entryPointPerfRepository.findAll();
        assertThat(entryPointPerfs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
