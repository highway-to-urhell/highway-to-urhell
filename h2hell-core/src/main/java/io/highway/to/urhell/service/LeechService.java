package io.highway.to.urhell.service;

import io.highway.to.urhell.domain.FrameworkInformations;

/**
 * 
 * @author Angus
 * 
 */
public interface LeechService {

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
