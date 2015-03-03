package io.highway.to.urhell.service;

import java.util.List;

import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.FrameworkInformations;

/**
 * @author Angus
 */
public interface LeechService {

    /**
     * Receive incoming Data to analyze
     */
    void receiveData(List<EntryPathData> incoming);

    /**
     * Details Framework Informations
     *
     * @return FrameworkInformations
     */
    FrameworkInformations getFrameworkInformations();

    boolean isTriggeredAtStartup();

}
