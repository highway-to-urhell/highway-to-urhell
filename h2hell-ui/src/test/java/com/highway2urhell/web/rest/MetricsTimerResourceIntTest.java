package com.highway2urhell.web.rest;

import com.highway2urhell.H2HellUiApp;

import com.highway2urhell.domain.MetricsTimer;
import com.highway2urhell.repository.MetricsTimerRepository;

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
 * Test class for the MetricsTimerResource REST controller.
 *
 * @see MetricsTimerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = H2HellUiApp.class)
public class MetricsTimerResourceIntTest {

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
    private MetricsTimerRepository metricsTimerRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restMetricsTimerMockMvc;

    private MetricsTimer metricsTimer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            MetricsTimerResource metricsTimerResource = new MetricsTimerResource(metricsTimerRepository);
        this.restMetricsTimerMockMvc = MockMvcBuilders.standaloneSetup(metricsTimerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MetricsTimer createEntity(EntityManager em) {
        MetricsTimer metricsTimer = new MetricsTimer()
                .dateIncoming(DEFAULT_DATE_INCOMING)
                .parameters(DEFAULT_PARAMETERS)
                .parametersContentType(DEFAULT_PARAMETERS_CONTENT_TYPE)
                .timeExec(DEFAULT_TIME_EXEC)
                .cpuLoadSystem(DEFAULT_CPU_LOAD_SYSTEM)
                .cpuLoadProcess(DEFAULT_CPU_LOAD_PROCESS);
        return metricsTimer;
    }

    @Before
    public void initTest() {
        metricsTimer = createEntity(em);
    }

    @Test
    @Transactional
    public void createMetricsTimer() throws Exception {
        int databaseSizeBeforeCreate = metricsTimerRepository.findAll().size();

        // Create the MetricsTimer

        restMetricsTimerMockMvc.perform(post("/api/metrics-timers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metricsTimer)))
            .andExpect(status().isCreated());

        // Validate the MetricsTimer in the database
        List<MetricsTimer> metricsTimerList = metricsTimerRepository.findAll();
        assertThat(metricsTimerList).hasSize(databaseSizeBeforeCreate + 1);
        MetricsTimer testMetricsTimer = metricsTimerList.get(metricsTimerList.size() - 1);
        assertThat(testMetricsTimer.getDateIncoming()).isEqualTo(DEFAULT_DATE_INCOMING);
        assertThat(testMetricsTimer.getParameters()).isEqualTo(DEFAULT_PARAMETERS);
        assertThat(testMetricsTimer.getParametersContentType()).isEqualTo(DEFAULT_PARAMETERS_CONTENT_TYPE);
        assertThat(testMetricsTimer.getTimeExec()).isEqualTo(DEFAULT_TIME_EXEC);
        assertThat(testMetricsTimer.getCpuLoadSystem()).isEqualTo(DEFAULT_CPU_LOAD_SYSTEM);
        assertThat(testMetricsTimer.getCpuLoadProcess()).isEqualTo(DEFAULT_CPU_LOAD_PROCESS);
    }

    @Test
    @Transactional
    public void createMetricsTimerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = metricsTimerRepository.findAll().size();

        // Create the MetricsTimer with an existing ID
        MetricsTimer existingMetricsTimer = new MetricsTimer();
        existingMetricsTimer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetricsTimerMockMvc.perform(post("/api/metrics-timers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingMetricsTimer)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MetricsTimer> metricsTimerList = metricsTimerRepository.findAll();
        assertThat(metricsTimerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMetricsTimers() throws Exception {
        // Initialize the database
        metricsTimerRepository.saveAndFlush(metricsTimer);

        // Get all the metricsTimerList
        restMetricsTimerMockMvc.perform(get("/api/metrics-timers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metricsTimer.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateIncoming").value(hasItem(sameInstant(DEFAULT_DATE_INCOMING))))
            .andExpect(jsonPath("$.[*].parametersContentType").value(hasItem(DEFAULT_PARAMETERS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].parameters").value(hasItem(Base64Utils.encodeToString(DEFAULT_PARAMETERS))))
            .andExpect(jsonPath("$.[*].timeExec").value(hasItem(DEFAULT_TIME_EXEC)))
            .andExpect(jsonPath("$.[*].cpuLoadSystem").value(hasItem(DEFAULT_CPU_LOAD_SYSTEM.doubleValue())))
            .andExpect(jsonPath("$.[*].cpuLoadProcess").value(hasItem(DEFAULT_CPU_LOAD_PROCESS.doubleValue())));
    }

    @Test
    @Transactional
    public void getMetricsTimer() throws Exception {
        // Initialize the database
        metricsTimerRepository.saveAndFlush(metricsTimer);

        // Get the metricsTimer
        restMetricsTimerMockMvc.perform(get("/api/metrics-timers/{id}", metricsTimer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(metricsTimer.getId().intValue()))
            .andExpect(jsonPath("$.dateIncoming").value(sameInstant(DEFAULT_DATE_INCOMING)))
            .andExpect(jsonPath("$.parametersContentType").value(DEFAULT_PARAMETERS_CONTENT_TYPE))
            .andExpect(jsonPath("$.parameters").value(Base64Utils.encodeToString(DEFAULT_PARAMETERS)))
            .andExpect(jsonPath("$.timeExec").value(DEFAULT_TIME_EXEC))
            .andExpect(jsonPath("$.cpuLoadSystem").value(DEFAULT_CPU_LOAD_SYSTEM.doubleValue()))
            .andExpect(jsonPath("$.cpuLoadProcess").value(DEFAULT_CPU_LOAD_PROCESS.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMetricsTimer() throws Exception {
        // Get the metricsTimer
        restMetricsTimerMockMvc.perform(get("/api/metrics-timers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMetricsTimer() throws Exception {
        // Initialize the database
        metricsTimerRepository.saveAndFlush(metricsTimer);
        int databaseSizeBeforeUpdate = metricsTimerRepository.findAll().size();

        // Update the metricsTimer
        MetricsTimer updatedMetricsTimer = metricsTimerRepository.findOne(metricsTimer.getId());
        updatedMetricsTimer
                .dateIncoming(UPDATED_DATE_INCOMING)
                .parameters(UPDATED_PARAMETERS)
                .parametersContentType(UPDATED_PARAMETERS_CONTENT_TYPE)
                .timeExec(UPDATED_TIME_EXEC)
                .cpuLoadSystem(UPDATED_CPU_LOAD_SYSTEM)
                .cpuLoadProcess(UPDATED_CPU_LOAD_PROCESS);

        restMetricsTimerMockMvc.perform(put("/api/metrics-timers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMetricsTimer)))
            .andExpect(status().isOk());

        // Validate the MetricsTimer in the database
        List<MetricsTimer> metricsTimerList = metricsTimerRepository.findAll();
        assertThat(metricsTimerList).hasSize(databaseSizeBeforeUpdate);
        MetricsTimer testMetricsTimer = metricsTimerList.get(metricsTimerList.size() - 1);
        assertThat(testMetricsTimer.getDateIncoming()).isEqualTo(UPDATED_DATE_INCOMING);
        assertThat(testMetricsTimer.getParameters()).isEqualTo(UPDATED_PARAMETERS);
        assertThat(testMetricsTimer.getParametersContentType()).isEqualTo(UPDATED_PARAMETERS_CONTENT_TYPE);
        assertThat(testMetricsTimer.getTimeExec()).isEqualTo(UPDATED_TIME_EXEC);
        assertThat(testMetricsTimer.getCpuLoadSystem()).isEqualTo(UPDATED_CPU_LOAD_SYSTEM);
        assertThat(testMetricsTimer.getCpuLoadProcess()).isEqualTo(UPDATED_CPU_LOAD_PROCESS);
    }

    @Test
    @Transactional
    public void updateNonExistingMetricsTimer() throws Exception {
        int databaseSizeBeforeUpdate = metricsTimerRepository.findAll().size();

        // Create the MetricsTimer

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMetricsTimerMockMvc.perform(put("/api/metrics-timers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metricsTimer)))
            .andExpect(status().isCreated());

        // Validate the MetricsTimer in the database
        List<MetricsTimer> metricsTimerList = metricsTimerRepository.findAll();
        assertThat(metricsTimerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMetricsTimer() throws Exception {
        // Initialize the database
        metricsTimerRepository.saveAndFlush(metricsTimer);
        int databaseSizeBeforeDelete = metricsTimerRepository.findAll().size();

        // Get the metricsTimer
        restMetricsTimerMockMvc.perform(delete("/api/metrics-timers/{id}", metricsTimer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MetricsTimer> metricsTimerList = metricsTimerRepository.findAll();
        assertThat(metricsTimerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetricsTimer.class);
    }
}
