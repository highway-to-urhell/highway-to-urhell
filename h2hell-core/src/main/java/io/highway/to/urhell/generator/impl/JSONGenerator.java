package io.highway.to.urhell.generator.impl;

import io.highway.to.urhell.CoreEngine;
import io.highway.to.urhell.generator.TheJack;
import io.highway.to.urhell.service.LeechService;

import java.util.Collection;

import com.google.gson.Gson;

public class JSONGenerator implements TheJack {

	@Override
	public String generatePage(Collection<LeechService> collectionService) {
		Gson gson = new Gson();
		return gson
				.toJson(CoreEngine.getInstance().getLeechServiceRegistered());
	}

}
