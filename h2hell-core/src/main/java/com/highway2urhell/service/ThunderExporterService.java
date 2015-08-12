package com.highway2urhell.service;

import com.highway2urhell.CoreEngine;
import com.highway2urhell.domain.EntryPathData;
import com.highway2urhell.domain.MessageBreaker;
import com.highway2urhell.domain.MessageMetrics;
import com.highway2urhell.domain.MessageThunderApp;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class ThunderExporterService {
	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private static ThunderExporterService instance;
	private BlockingQueue<MessageBreaker> queueRemoteBreaker = new LinkedBlockingQueue<MessageBreaker>(
			10000);
	private BlockingQueue<MessageMetrics> queueRemotePerformance = new LinkedBlockingQueue<MessageMetrics>(
			10000);
	private final static int MSG_SIZE = 50;

	private ThunderExporterService() {
		ScheduledExecutorService schExService = Executors
				.newScheduledThreadPool(1);
		schExService.scheduleAtFixedRate(new Runnable() {

			public void run() {
				LOGGER.info("Drain the queue Remote");
				List<MessageBreaker> listBreaker = new ArrayList<MessageBreaker>();
				int result = queueRemoteBreaker.drainTo(listBreaker, MSG_SIZE);
				LOGGER.info("Drain Remote size " + result);
				if (result > 0) {
					LOGGER.info("Send the Data");
					sendDataHTTP("/addBreaker", listBreaker);
				}

				LOGGER.info("Drain the queue Performance");
				List<MessageMetrics> listPerformance = new ArrayList<MessageMetrics>();
				int resultPerf = queueRemotePerformance.drainTo(listPerformance, MSG_SIZE);
				LOGGER.info("Drain Performance size " + result);
				if (resultPerf > 0) {
					LOGGER.info("Send the Data");
					sendDataHTTP("/addPerformance", listPerformance);
				}

			}
		}, 5, 30, TimeUnit.SECONDS);
	}

	public static ThunderExporterService getInstance() {
		if (instance == null) {
			synchronized (ThunderExporterService.class) {
				if (instance == null) {
					instance = new ThunderExporterService();
				}
			}
		}
		return instance;
	}

	public void registerAppInThunder() {
		String token = sendDataHTTP("/createThunderApp/", CoreEngine
				.getInstance().getConfig());
		CoreEngine.getInstance().getConfig().setToken(token);
		LOGGER.info("application registred with token {} for application {}",
				token, CoreEngine.getInstance().getConfig()
						.getNameApplication());
	}

	public void initRemoteApp() {
		LOGGER.info("initRemoteApp ");
		TransformerService ts = new TransformerService();
		List<EntryPathData> res = ts
				.collectBreakerDataFromLeechPlugin(CoreEngine.getInstance()
						.getLeechServiceRegistered());
		MessageThunderApp msg = new MessageThunderApp();
		msg.setListentryPathData(res);
		msg.setToken(CoreEngine.getInstance().getConfig().getToken());
		sendDataHTTP("/initThunderApp", msg);
	}

	public void sendRemotePerformance(String fullMethodName,long timeExec){
		MessageMetrics msg = new MessageMetrics();
		msg.setPathClassMethodName(fullMethodName);
		msg.setToken(CoreEngine.getInstance().getConfig().getToken());
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy:hh-mm-ss");
		Date date = new Date();
		msg.setDateIncoming(sdf.format(date));
		msg.setTimeExec(timeExec);
		queueRemotePerformance.add(msg);
	}
	
	public void sendRemoteBreaker(String pathClassMethodName) {
		MessageBreaker msg = new MessageBreaker();
		msg.setPathClassMethodName(pathClassMethodName);
		msg.setToken(CoreEngine.getInstance().getConfig().getToken());
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy:hh-mm-ss");
		Date date = new Date();
		msg.setDateIncoming(sdf.format(date));
		queueRemoteBreaker.add(msg);
	}

	private String sendDataHTTP(String uri, Object message) {
		Gson gson = new Gson();
		String data = gson.toJson(message);
		String urlServer = CoreEngine.getInstance().getConfig().getUrlH2hWeb()
				+ uri;
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(urlServer);
		if (data != null) {
			httpPost.setEntity(new StringEntity(data, "UTF8"));
		}
		httpPost.setHeader("Content-type", "application/json");
		String result = null;
		try {
			HttpResponse response = client.execute(httpPost);
			if (response != null) {
				if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_CREATED
						|| response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK
						|| response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_ACCEPTED
						|| response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_NO_CONTENT) {
					result = EntityUtils.toString(response.getEntity());
					LOGGER.info("Message from H2H server" + result);
				} else {
					LOGGER.error("Failed : HTTP error code : "
							+ response.getStatusLine().getStatusCode()
							+ " for urlServer " + urlServer + "msg "
							+ EntityUtils.toString(response.getEntity()));
				}
			} else {
				LOGGER.error("Failed : Response Null from urlServer : "
						+ urlServer);
			}

		} catch (IOException e) {
			LOGGER.error("Error while sending dataHttp to " + urlServer, e);
		}
		return result;

	}
}
