package com.highway2urhell.service;

import java.util.List;

import com.highway2urhell.domain.EntryPathData;
import com.highway2urhell.domain.FrameworkInformations;

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
