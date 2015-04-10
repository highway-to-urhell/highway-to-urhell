package io.highway.to.urhell.service;

import com.google.gson.Gson;
import io.highway.to.urhell.CoreEngine;
import io.highway.to.urhell.domain.BreakerData;
import io.highway.to.urhell.domain.MessageBreaker;
import io.highway.to.urhell.domain.MessageThunderApp;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ThunderService {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private static ThunderService instance;

    private ThunderService() {
    }

    public static ThunderService getInstance() {
        if (instance == null) {
            synchronized (ThunderService.class) {
                if (instance == null) {
                    instance = new ThunderService();
                }
            }
        }
        return instance;
    }

    public void registerAppInThunder() {
        String token = sendDataHTTP("/createThunderApp/", CoreEngine.getInstance().getConfig());
        LOGGER.info("application registred with token {} for application {}", token, CoreEngine.getInstance().getConfig().getNameApplication());
        CoreEngine.getInstance().getConfig().setToken(token);
    }

    public void initApp() {
        TransformerService ts = new TransformerService();
        List<BreakerData> res = ts.transforDataH2hToList(CoreEngine.getInstance().getLeechServiceRegistered());
        MessageThunderApp msg = new MessageThunderApp();
        msg.setListBreakerData(res);
        msg.setToken(CoreEngine.getInstance().getConfig().getToken());
        sendDataHTTP("/initThunderApp", msg);
    }

    public void sendRemoteBreaker(String pathClassMethodName) {
        MessageBreaker msg = new MessageBreaker();
        msg.setPathClassMethodName(pathClassMethodName);
        msg.setToken(CoreEngine.getInstance().getConfig().getToken());
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
                            + response.getStatusLine().getStatusCode() + " for urlServer " + urlServer);
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
