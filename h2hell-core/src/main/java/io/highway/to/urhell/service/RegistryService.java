package io.highway.to.urhell.service;

import io.highway.to.urhell.domain.FrameworkInformations;
import io.highway.to.urhell.service.impl.FileSystemService;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegistryService {

	private List<LeechService> registry = new ArrayList<LeechService>();
	private static final Logger LOG = LoggerFactory
			.getLogger(FileSystemService.class);
	private static RegistryService instance;

	private final static String highwaytourhell = "highwaytourhell";

	private RegistryService() {
		// Nothing
	}

	public static RegistryService getInstance() {
		if (instance == null) {
			synchronized (highwaytourhell) {
				if (instance == null) {
					instance = new RegistryService();
				}
			}
		}
		return instance;
	}

	public void addServiceRegistry(LeechService serviceToAdd) {
		synchronized (highwaytourhell) {
			// Possible multi access because scan static file or an another
			// service
			registry.add(serviceToAdd);
		}
	}

	public String executeServiceRegistry() {
		StringBuilder sb = new StringBuilder();
		synchronized (highwaytourhell) {
			// Possible multi access because scan static file or an another
			// service
			for (LeechService leech : registry) {
				FrameworkInformations fwk = leech.getFrameworkInformations();
				sb.append(fwk.toString());
				LOG.error(fwk.toString());
			}
		}
		return sb.toString();
	}
}
