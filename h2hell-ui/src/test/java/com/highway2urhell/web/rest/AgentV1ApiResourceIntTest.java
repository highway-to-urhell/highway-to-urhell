package com.highway2urhell.web.rest;

import com.highway2urhell.H2HellUiApp;
import com.highway2urhell.domain.*;
import com.highway2urhell.repository.*;
import com.highway2urhell.service.AgentV1ApiService;
import com.highway2urhell.web.rest.dto.v1api.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Test class for the ApplicationResource REST controller.
 *
 * @see ApplicationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = H2HellUiApp.class)
@WebAppConfiguration
@IntegrationTest
public class AgentV1ApiResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String DEFAULT_TOKEN = "AAAAA";

    private static final String DEFAULT_URL_APP = "AAAAA";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String DEFAULT_APP_TYPE = "AAAAA";

    private static final String DEFAULT_PATH_SOURCE = "AAAA";
    private static final String DEFAULT_VERSION_APP = "AAAA";


    private static final String DEFAULT_CLASS_NAME = "AAAAA";
    private static final String DEFAULT_METHODE_NAME = "AAAAA";
    private static final String DEFAULT_SIGNATURE_NAME = "AAAAA";
    private static final int DEFAULT_LINE_NUMBER = 1;
    private static final TypePath DEFAULT_TYPE_PATH = TypePath.STATIC;
    private static final String DEFAULT_HTTPMETHOD = "AAAAA";

    private static final String DEFAULT_PARAMETERS = "toto, tata, titi";

    private static final Integer DEFAULT_TIME_EXEC = 150;
    private static final Double DEFAULT_CPU_LOAD_SYSTEM = 12D;
    private static final Double DEFAULT_CPU_LOAD_PROCESS = 5D;

    @Inject
    private AgentV1ApiService agentV1ApiService;

    @Inject
    private ApplicationRepository applicationRepository;

    @Inject
    private AnalysisRepository analysisRepository;

    @Inject
    private EntryPointRepository entryPointRepository;

    @Inject
    private EntryPointCallRepository entryPointCallRepository;

    @Inject
    private EntryPointPerfRepository entryPointPerfRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restApplicationMockMvc;

    private H2hConfigDTO configDTO;
    private MessageThunderApp msg;
    private List<MessageBreaker> listBreaker;
    private List<MessageMetrics> listPerformance;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AgentV1ApiResource agentV1ApiResource = new AgentV1ApiResource();
        ReflectionTestUtils.setField(agentV1ApiResource, "agentV1ApiService", agentV1ApiService);
        this.restApplicationMockMvc = MockMvcBuilders.standaloneSetup(agentV1ApiResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        configDTO = new H2hConfigDTO();
        configDTO.setNameApplication(DEFAULT_NAME);
        configDTO.setToken(DEFAULT_TOKEN);
        configDTO.setUrlApplication(DEFAULT_URL_APP);
        configDTO.setDescription(DEFAULT_DESCRIPTION);
        configDTO.setTypeAppz(DEFAULT_APP_TYPE);
        configDTO.setVersionApp(DEFAULT_VERSION_APP);
        configDTO.setPathSource(DEFAULT_PATH_SOURCE);

        msg = new MessageThunderApp();
        msg.setToken(DEFAULT_TOKEN);
        List<EntryPathData> listEntryPathData = new ArrayList<>();
        EntryPathData entryPathData = new EntryPathData();
        entryPathData.setClassName(DEFAULT_CLASS_NAME);
        entryPathData.setMethodName(DEFAULT_METHODE_NAME);
        entryPathData.setSignatureName(DEFAULT_SIGNATURE_NAME);
        entryPathData.setLineNumber(DEFAULT_LINE_NUMBER);
        entryPathData.setTypePath(DEFAULT_TYPE_PATH);
        entryPathData.setHttpMethod(DEFAULT_HTTPMETHOD);
        listEntryPathData.add(entryPathData);
        msg.setListentryPathData(listEntryPathData);

        listBreaker = new ArrayList<>();
        MessageBreaker mb = new MessageBreaker();
        mb.setToken(DEFAULT_TOKEN);
        mb.setParameters(DEFAULT_PARAMETERS);
        mb.setPathClassMethodName(DEFAULT_CLASS_NAME + "." + DEFAULT_METHODE_NAME);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy:hh-mm-ss");
        mb.setDateIncoming(sdf.format(new Date()));
        listBreaker.add(mb);

        listPerformance = new ArrayList<>();
        MessageMetrics mm = new MessageMetrics();
        mm.setToken(DEFAULT_TOKEN);
        mm.setPathClassMethodName(DEFAULT_CLASS_NAME + "." + DEFAULT_METHODE_NAME);
        mm.setDateIncoming(sdf.format(new Date()));
        mm.setParameters(DEFAULT_PARAMETERS);
        mm.setCpuLoadProcess(DEFAULT_CPU_LOAD_PROCESS);
        mm.setCpuLoadSystem(DEFAULT_CPU_LOAD_SYSTEM);
        mm.setTimeExec(DEFAULT_TIME_EXEC);
        listPerformance.add(mm);

    }

    @Test
    @Transactional
    public void makeAStandardScenarii() throws Exception {
        createApplication();
        initThunderApp();
        addBreakers();
        addPerformance();
    }

    private void createApplication() throws Exception {
        int databaseApplicationSizeBeforeCreate = (int) applicationRepository.count();
        int databaseAnalysisSizeBeforeCreate = (int) analysisRepository.count();

        // Create the Application

        restApplicationMockMvc.perform(post("/ThunderEntry/createThunderApp")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(configDTO)))
                .andExpect(status().isCreated());

        // Validate the Application in the database
        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseApplicationSizeBeforeCreate + 1);
        Application testApplication = applications.get(applications.size() - 1);
        assertThat(testApplication.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApplication.getToken()).isEqualTo(DEFAULT_TOKEN);
        assertThat(testApplication.getUrlApp()).isEqualTo(DEFAULT_URL_APP);
        assertThat(testApplication.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testApplication.getAppType()).isEqualTo(DEFAULT_APP_TYPE);

        // Validate the Analysis in the database
        List<Analysis> analysis = analysisRepository.findAll();
        assertThat(analysis).hasSize(databaseAnalysisSizeBeforeCreate + 1);
        Analysis testAnalysis = analysis.get(analysis.size() - 1);
        assertThat(testAnalysis.getAppVersion()).isEqualTo(DEFAULT_VERSION_APP);
        assertThat(testAnalysis.getPathSource()).isEqualTo(DEFAULT_PATH_SOURCE);
    }

    private void initThunderApp() throws Exception {
        int databaseEntryPointSizeBeforeCreate = (int) entryPointRepository.count();

        restApplicationMockMvc.perform(post("/ThunderEntry/initThunderApp")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(msg)))
            .andExpect(status().isOk());

        // Validate the Application in the database
        List<EntryPoint> entryPoints = entryPointRepository.findAll();
        assertThat(entryPoints).hasSize(databaseEntryPointSizeBeforeCreate + 1);
        EntryPoint entryPoint = entryPoints.get(entryPoints.size() - 1);
        assertThat(entryPoint.getPathClassMethodName()).isEqualTo(DEFAULT_CLASS_NAME + "." + DEFAULT_METHODE_NAME);
    }

    private void addBreakers() throws Exception {
        int databaseEntryPointCallSizeBeforeCreate = (int) entryPointCallRepository.count();

        restApplicationMockMvc.perform(post("/ThunderEntry/addBreaker")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(listBreaker)))
            .andExpect(status().isOk());

        // Validate the Application in the database
        List<EntryPointCall> entryPointsCalls = entryPointCallRepository.findAll();
        assertThat(entryPointsCalls).hasSize(databaseEntryPointCallSizeBeforeCreate + 1);
        EntryPointCall entryPointCall = entryPointsCalls.get(entryPointsCalls.size() - 1);
        assertThat(entryPointCall.getParameters()).isEqualTo(DEFAULT_PARAMETERS.getBytes());
        assertThat(entryPointCall.getEntryPoint().getPathClassMethodName()).isEqualTo(DEFAULT_CLASS_NAME + "." + DEFAULT_METHODE_NAME);
    }

    private void addPerformance() throws Exception {
        int databaseMetricsTimersSizeBeforeCreate = (int) entryPointPerfRepository.count();

        restApplicationMockMvc.perform(post("/ThunderEntry/addPerformance")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(listPerformance)))
            .andExpect(status().isOk());

        // Validate the Application in the database
        List<EntryPointPerf> entryPointPerfs = entryPointPerfRepository.findAll();
        assertThat(entryPointPerfs).hasSize(databaseMetricsTimersSizeBeforeCreate + 1);
        EntryPointPerf entryPointPerf = entryPointPerfs.get(entryPointPerfs.size() - 1);
        assertThat(entryPointPerf.getCpuLoadProcess()).isEqualTo(DEFAULT_CPU_LOAD_PROCESS);
        assertThat(entryPointPerf.getCpuLoadSystem()).isEqualTo(DEFAULT_CPU_LOAD_SYSTEM);
        assertThat(entryPointPerf.getTimeExec()).isEqualTo(DEFAULT_TIME_EXEC);
        assertThat(entryPointPerf.getEntryPoint().getPathClassMethodName()).isEqualTo(DEFAULT_CLASS_NAME + "." + DEFAULT_METHODE_NAME);
    }

}
