package io.highway.to.urhell.service;

import io.highway.to.urhell.CoreEngine;
import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.MessageBreaker;
import io.highway.to.urhell.domain.MessageThunderApp;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    private String token;

    private ThunderExporterService() {
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
        this.token= sendDataHTTP("/createThunderApp/", CoreEngine.getInstance().getConfig());
        LOGGER.info("application registred with token {} for application {}", token, CoreEngine.getInstance().getConfig().getNameApplication());
    }

    public void initRemoteApp() {
        TransformerService ts = new TransformerService();
        List<EntryPathData> res = ts.collectBreakerDataFromLeechPlugin(CoreEngine.getInstance().getLeechServiceRegistered());
        LOGGER.error(" RES "+res.toString());
        MessageThunderApp msg = new MessageThunderApp();
        msg.setListentryPathData(res);
        msg.setToken(token);
        sendDataHTTP("/initThunderApp", msg);
    }

    public void sendRemoteBreaker(String pathClassMethodName) {
        MessageBreaker msg = new MessageBreaker();
        msg.setPathClassMethodName(pathClassMethodName);
        msg.setToken(token);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy:hh-mm-ss");
        Date date = new Date();
        msg.setDateIncoming(sdf.format(date));
        sendDataHTTP("/addBreaker", msg);
    }

    private String sendDataHTTP(String uri, Object message) {
        Gson gson = new Gson();
        String data = gson.toJson(message);
        String urlServer = CoreEngine.getInstance().getConfig().getUrlH2hWeb() + uri;
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
                            + response.getStatusLine().getStatusCode() + " for urlServer " + urlServer + "msg "+EntityUtils.toString(response.getEntity()));
                }
            } else {
                LOGGER.error("Failed : Response Null from urlServer : " + urlServer);
            }

        } catch (IOException e) {
            LOGGER.error("Error while sending dataHttp to " + urlServer, e);
        }
        return result;

    }
}
