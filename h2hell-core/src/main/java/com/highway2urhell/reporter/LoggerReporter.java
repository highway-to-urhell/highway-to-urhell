package com.highway2urhell.reporter;

import com.google.gson.Gson;
import com.highway2urhell.domain.FrameworkInformations;
import com.highway2urhell.service.ReporterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerReporter implements ReporterService {
    private static final Logger LOG = LoggerFactory.getLogger(LoggerReporter.class);

    @Override
    public void report(FrameworkInformations frameworkInformations) {
        if (frameworkInformations.hasEntryPaths()) {
            LOG.info("framework {} version {}",
                    frameworkInformations.getFrameworkName(),
                    frameworkInformations.getVersion());
            LOG.info("{} urls found", frameworkInformations.getListEntryPath()
                    .size());
            if (LOG.isDebugEnabled()) {
                Gson gson = new Gson();
                LOG.debug(" JSON elements :" + gson.toJson(frameworkInformations.getListEntryPath()));
            }
        }
    }

}
