package io.highway.to.urhell.service;

import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.FrameworkEnum;
import io.highway.to.urhell.domain.FrameworkInformations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLeechService implements LeechService {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private FrameworkInformations frameworkInformations = new FrameworkInformations();

    public AbstractLeechService(FrameworkEnum framework) {
        frameworkInformations.setFrameworkEnum(framework);
    }

    public AbstractLeechService(FrameworkEnum framework, String version) {
    	this(framework);
    	frameworkInformations.setVersion(version);
    }

    public void receiveData(Object incoming) {
        clearPreviousData();
        LOGGER.debug("receive incoming data");
        gatherData(incoming);
        LOGGER.debug("data gathering complete. Found {} entries",
                frameworkInformations.getListEntryPath().size());
    }

    protected void clearPreviousData() {
        frameworkInformations.getListEntryPath().clear();
    }

    protected abstract void gatherData(Object incoming);

    @Override
    public FrameworkInformations getFrameworkInformations() {
        return frameworkInformations;
    }
    
    public void addEntryPath(EntryPathData entryPath) {
        frameworkInformations.getListEntryPath().add(entryPath);
    }

}
