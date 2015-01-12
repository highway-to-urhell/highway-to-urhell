package io.highway.to.urhell.generator.impl;

import io.highway.to.urhell.CoreEngine;
import io.highway.to.urhell.generator.TheJack;
import io.highway.to.urhell.service.LeechService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import com.google.gson.Gson;

public class FileGenerator implements TheJack {

	@Override
	public String generatePage(Collection<LeechService> collectionService) {
		Gson gson = new Gson();
		String flux = gson.toJson(CoreEngine.getInstance()
				.getLeechServiceRegistered());
		SimpleDateFormat format = new SimpleDateFormat("ssmmHHddMMyy-h2h.json");
		Date date = new Date();
		String path = format.format(date);
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(flux);
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		return path;
	}

}
