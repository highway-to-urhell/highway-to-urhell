package io.highway.to.urhell.service;

import io.highway.to.urhell.domain.FrameworkInformations;

/**
 * 
 * @author Angus
 * 
 */
public interface LeechService {

    /**
     * Registry 
     */
    void registry();

    /**
     * Receive incoming Data to analyze
     * 
     *
     */
    void receiveData(Object incoming);

    /**
     * Details Framework Informations
     * 
     * @return FrameworkInformations
     */
    FrameworkInformations getFrameworkInformations();

}
