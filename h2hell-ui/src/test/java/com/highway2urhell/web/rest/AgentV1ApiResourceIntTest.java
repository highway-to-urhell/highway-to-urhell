package com.highway2urhell.web.rest;

import com.highway2urhell.H2HellUiApp;
import com.highway2urhell.domain.Analysis;
import com.highway2urhell.domain.Application;
import com.highway2urhell.repository.AnalysisRepository;
import com.highway2urhell.repository.ApplicationRepository;
import com.highway2urhell.web.rest.dto.v1api.H2hConfigDTO;
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

    @Inject
    private ApplicationRepository applicationRepository;

    @Inject
    private AnalysisRepository analysisRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restApplicationMockMvc;

    private H2hConfigDTO configDTO;

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
        configDTO = new H2hConfigDTO();
        configDTO.setNameApplication(DEFAULT_NAME);
        configDTO.setToken(DEFAULT_TOKEN);
        configDTO.setUrlApplication(DEFAULT_URL_APP);
        configDTO.setDescription(DEFAULT_DESCRIPTION);
        configDTO.setTypeAppz(DEFAULT_APP_TYPE);
        configDTO.setVersionApp(DEFAULT_VERSION_APP);
        configDTO.setPathSource(DEFAULT_PATH_SOURCE);

    }

    @Test
    @Transactional
    public void createApplication() throws Exception {
        int databaseSizeBeforeCreate = applicationRepository.findAll().size();

        // Create the Application

        restApplicationMockMvc.perform(post("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(configDTO)))
                .andExpect(status().isCreated());

        // Validate the Application in the database
        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeCreate + 1);
        Application testApplication = applications.get(applications.size() - 1);
        assertThat(testApplication.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApplication.getToken()).isEqualTo(DEFAULT_TOKEN);
        assertThat(testApplication.getUrlApp()).isEqualTo(DEFAULT_URL_APP);
        assertThat(testApplication.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testApplication.getAppType()).isEqualTo(DEFAULT_APP_TYPE);

        // Validate the Analysis in the database
        List<Analysis> analysis = analysisRepository.findAll();
        assertThat(analysis).hasSize(databaseSizeBeforeCreate + 1);
        Analysis testAnalysis = analysis.get(analysis.size() - 1);
        assertThat(testAnalysis.getAppVersion()).isEqualTo(DEFAULT_VERSION_APP);
        assertThat(testAnalysis.getPathSource()).isEqualTo(DEFAULT_PATH_SOURCE);
    }

}
