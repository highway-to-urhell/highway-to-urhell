package com.highway2urhell.service.impl;

import com.highway2urhell.VersionUtils;
import com.highway2urhell.service.AbstractLeechService;

public class Struts2Service extends AbstractLeechService {

    public static final String FRAMEWORK_NAME = "STRUTS_2";

    public Struts2Service() {
        super(FRAMEWORK_NAME, VersionUtils.getVersion(
                "com.opensymphony.xwork2.config.ConfigurationManager",
                "org.apache.struts",
                "struts2-core"));
    }
}
