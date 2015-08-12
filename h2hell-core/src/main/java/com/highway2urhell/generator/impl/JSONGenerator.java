package com.highway2urhell.generator.impl;

import com.highway2urhell.domain.FrameworkInformations;
import com.highway2urhell.generator.TheJack;
import com.highway2urhell.service.LeechService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;

public class JSONGenerator implements TheJack {

	@Override
	public String generatePage(Collection<LeechService> collectionService) {

		List<FrameworkInformations> listFwk = new ArrayList<FrameworkInformations>();
		for (LeechService leech : collectionService) {
			listFwk.add(leech.getFrameworkInformations());
		}
		Gson gson = new Gson();
		return gson.toJson(listFwk);
	}

}
