package com.highway2urhell.service.impl;

import com.highway2urhell.VersionUtils;
import com.highway2urhell.service.AbstractLeechService;

public class StrutsSpring1Service extends AbstractLeechService {

    public static final String FRAMEWORK_NAME = "STRUTS_SPRING_1";

    public StrutsSpring1Service() {
        super(FRAMEWORK_NAME, VersionUtils.getVersion(
                "org.springframework.web.struts.DelegatingActionProxy",
                "",
                ""));
    }
}
