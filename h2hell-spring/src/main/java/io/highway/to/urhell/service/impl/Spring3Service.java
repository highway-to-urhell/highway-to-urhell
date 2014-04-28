package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.FrameworkEnum;
import io.highway.to.urhell.domain.FrameworkInformations;
import io.highway.to.urhell.service.LeechService;
import io.highway.to.urhell.service.RegistryService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class Spring3Service implements LeechService {

	private static final Logger LOG = LoggerFactory
			.getLogger(Spring3Service.class);
	private List<EntryPathData> listData;
	private static Spring3Service instance;
	private final static String highwaytourhell = "highwaytourhell";

	private Spring3Service() {
		// Nothing
	}

	public static Spring3Service getInstance() {
		if (instance == null) {
			synchronized (highwaytourhell) {
				if (instance == null) {
					instance = new Spring3Service();
				}
			}
		}
		return instance;
	}

	@Override
	public void registry() {
		RegistryService.getInstance().addServiceRegistry(this);
	}

	@Override
	public void receiveData(Object dataIncoming) {
		// Faut r�cup�rer ce bordel pour SpringMVC
		RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping) dataIncoming;

		
		StringBuilder sb = new StringBuilder();
		for (RequestMappingInfo element : requestMappingHandlerMapping
				.getHandlerMethods().keySet()) {

			sb.append("element.getPatternsCondition()"+ element.getPatternsCondition());
			sb.append("element.getMethodsCondition()"+ element.getMethodsCondition());
			sb.append("element.getParamsCondition()"+ element.getParamsCondition());
			sb.append("element.getConsumesCondition()"+ element.getConsumesCondition());
		}

		//Push Into ListData
		//TODO a finir

	}

	@Override
	public FrameworkInformations getFrameworkInformations() {
		FrameworkInformations fwk = new FrameworkInformations();
		fwk.setFrameworkEnum(FrameworkEnum.SPRING_3X);
		fwk.setVersion("OPENJAR");
		fwk.setDetails("openjar");
		fwk.setListEntryPath(listData);
		return fwk;
	}

}
