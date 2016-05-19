package com.highway2urhell.web.rest;

import com.highway2urhell.H2HellUiApp;
import com.highway2urhell.domain.MetricsTimer;
import com.highway2urhell.repository.MetricsTimerRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the MetricsTimerResource REST controller.
 *
 * @see MetricsTimerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = H2HellUiApp.class)
@WebAppConfiguration
@IntegrationTest
public class MetricsTimerResourceIntTest {


    private static final Integer DEFAULT_TIME_EXEC = 1;
    private static final Integer UPDATED_TIME_EXEC = 2;

    private static final Double DEFAULT_CPU_LOAD_SYSTEM = 1D;
    private static final Double UPDATED_CPU_LOAD_SYSTEM = 2D;

    private static final Double DEFAULT_CPU_LOAD_PROCESS = 1D;
    private static final Double UPDATED_CPU_LOAD_PROCESS = 2D;

    @Inject
    private MetricsTimerRepository metricsTimerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMetricsTimerMockMvc;

    private MetricsTimer metricsTimer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MetricsTimerResource metricsTimerResource = new MetricsTimerResource();
        ReflectionTestUtils.setField(metricsTimerResource, "metricsTimerRepository", metricsTimerRepository);
        this.restMetricsTimerMockMvc = MockMvcBuilders.standaloneSetup(metricsTimerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        metricsTimer = new MetricsTimer();
        metricsTimer.setTimeExec(DEFAULT_TIME_EXEC);
        metricsTimer.setCpuLoadSystem(DEFAULT_CPU_LOAD_SYSTEM);
        metricsTimer.setCpuLoadProcess(DEFAULT_CPU_LOAD_PROCESS);
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
        List<MetricsTimer> metricsTimers = metricsTimerRepository.findAll();
        assertThat(metricsTimers).hasSize(databaseSizeBeforeCreate + 1);
        MetricsTimer testMetricsTimer = metricsTimers.get(metricsTimers.size() - 1);
        assertThat(testMetricsTimer.getTimeExec()).isEqualTo(DEFAULT_TIME_EXEC);
        assertThat(testMetricsTimer.getCpuLoadSystem()).isEqualTo(DEFAULT_CPU_LOAD_SYSTEM);
        assertThat(testMetricsTimer.getCpuLoadProcess()).isEqualTo(DEFAULT_CPU_LOAD_PROCESS);
    }

    @Test
    @Transactional
    public void getAllMetricsTimers() throws Exception {
        // Initialize the database
        metricsTimerRepository.saveAndFlush(metricsTimer);

        // Get all the metricsTimers
        restMetricsTimerMockMvc.perform(get("/api/metrics-timers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(metricsTimer.getId().intValue())))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(metricsTimer.getId().intValue()))
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
        MetricsTimer updatedMetricsTimer = new MetricsTimer();
        updatedMetricsTimer.setId(metricsTimer.getId());
        updatedMetricsTimer.setTimeExec(UPDATED_TIME_EXEC);
        updatedMetricsTimer.setCpuLoadSystem(UPDATED_CPU_LOAD_SYSTEM);
        updatedMetricsTimer.setCpuLoadProcess(UPDATED_CPU_LOAD_PROCESS);

        restMetricsTimerMockMvc.perform(put("/api/metrics-timers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMetricsTimer)))
                .andExpect(status().isOk());

        // Validate the MetricsTimer in the database
        List<MetricsTimer> metricsTimers = metricsTimerRepository.findAll();
        assertThat(metricsTimers).hasSize(databaseSizeBeforeUpdate);
        MetricsTimer testMetricsTimer = metricsTimers.get(metricsTimers.size() - 1);
        assertThat(testMetricsTimer.getTimeExec()).isEqualTo(UPDATED_TIME_EXEC);
        assertThat(testMetricsTimer.getCpuLoadSystem()).isEqualTo(UPDATED_CPU_LOAD_SYSTEM);
        assertThat(testMetricsTimer.getCpuLoadProcess()).isEqualTo(UPDATED_CPU_LOAD_PROCESS);
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
        List<MetricsTimer> metricsTimers = metricsTimerRepository.findAll();
        assertThat(metricsTimers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
