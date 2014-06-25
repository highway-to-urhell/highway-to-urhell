package io.highway.to.urhell.reporter;

import io.highway.to.urhell.domain.FrameworkInformations;
import io.highway.to.urhell.service.ReporterService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class LoggerReporter implements ReporterService {
    private static final Logger LOG = LoggerFactory
            .getLogger(LoggerReporter.class);

    @Override
    public void report(FrameworkInformations frameworkInformations) {
        if (!frameworkInformations.getListEntryPath().isEmpty()) {
            LOG.info("framework {} version {}",
                    frameworkInformations.getFrameworkEnum(),
                    frameworkInformations.getVersion());
            LOG.info("{} urls found", frameworkInformations.getListEntryPath()
                    .size());
            if (LOG.isDebugEnabled()) {
                Gson gson = new Gson();
                LOG.debug(" JSON elements :"
                        + gson.toJson(frameworkInformations.getListEntryPath()));
            }
        }
    }

}
