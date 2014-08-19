package io.highway.to.urhell.service;

import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.FrameworkInformations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLeechService implements LeechService {

    protected final transient Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private FrameworkInformations frameworkInformations = new FrameworkInformations();
    private boolean triggeredAtStartup = false;

    public AbstractLeechService(String frameworkName) {
        frameworkInformations.setFrameworkName(frameworkName);
    }

    public AbstractLeechService(String frameworkName, String version) {
        this(frameworkName);
        frameworkInformations.setVersion(version);
    }

    @Override
    public boolean isTriggeredAtStartup() {
        return triggeredAtStartup;
    }

    protected void setTriggerAtStartup(boolean triggeredAtStartup) {
        this.triggeredAtStartup = triggeredAtStartup;
    }

    public void receiveData(Object incoming) {
        clearPreviousData();
        LOGGER.debug("receive incoming data");
        gatherData(incoming);
        LOGGER.debug("data gathering complete. Found {} entries",
                frameworkInformations.getListEntryPath().size());
    }

    private void clearPreviousData() {
        frameworkInformations.getListEntryPath().clear();
    }

    protected abstract void gatherData(Object incoming);

    @Override
    public FrameworkInformations getFrameworkInformations() {
        return frameworkInformations;
    }

    protected void addEntryPath(EntryPathData entryPath) {
        frameworkInformations.getListEntryPath().add(entryPath);
    }
}
