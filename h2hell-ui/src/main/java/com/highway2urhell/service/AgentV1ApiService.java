package com.highway2urhell.service;

import com.highway2urhell.domain.Analysis;
import com.highway2urhell.domain.Application;
import com.highway2urhell.domain.Authority;
import com.highway2urhell.domain.User;
import com.highway2urhell.repository.ApplicationRepository;
import com.highway2urhell.repository.AuthorityRepository;
import com.highway2urhell.repository.PersistentTokenRepository;
import com.highway2urhell.repository.UserRepository;
import com.highway2urhell.security.SecurityUtils;
import com.highway2urhell.service.util.RandomUtil;
import com.highway2urhell.web.rest.dto.ManagedUserDTO;
import com.highway2urhell.web.rest.dto.v1api.H2hConfigDTO;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Service class for managing agent v1 api.
 */
@Service
@Transactional
public class AgentV1ApiService {

    private final Logger log = LoggerFactory.getLogger(AgentV1ApiService.class);


    @Inject
    private ApplicationRepository applicationRepository;

    public Application createApplication(H2hConfigDTO configDTO) {
        Application app = new Application();
        Analysis analysis = new Analysis();
        analysis.setApplication(app);
        analysis.setAppVersion(configDTO.getVersionApp());
        app.setName(configDTO.getNameApplication());
        app.setUrlApp(configDTO.getUrlApplication());
        String token = RandomStringUtils.randomAlphanumeric(16).toUpperCase();
        app.setToken(token);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy:hh-mm-ss");
        app.setDateCreation(ZonedDateTime.now());
        app.setDescription(configDTO.getDescription());
        analysis.setPathSource(configDTO.getPathSource());
        if(configDTO.getTypeAppz()!=null){
            app.setAppType(configDTO.getTypeAppz());
        }else{
            app.setAppType("UNKNOWN");
        }
        applicationRepository.save(app);
        log.debug("Created Information for Application: {}", app);
        return app;
    }

}
