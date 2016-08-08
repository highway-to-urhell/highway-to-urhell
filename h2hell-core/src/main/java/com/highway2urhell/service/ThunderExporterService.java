package com.highway2urhell.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.highway2urhell.CoreEngine;
import com.highway2urhell.domain.EntryPathData;
import com.highway2urhell.domain.MessageBreaker;
import com.highway2urhell.domain.MessageMetrics;
import com.highway2urhell.domain.MessageThunderApp;
import com.sun.management.OperatingSystemMXBean;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;


public class ThunderExporterService {
    private static ThunderExporterService instance;
    private BlockingQueue<MessageBreaker> queueRemoteBreaker = new LinkedBlockingQueue<MessageBreaker>(10000);
    private BlockingQueue<MessageMetrics> queueRemotePerformance = new LinkedBlockingQueue<MessageMetrics>(10000);
    private final static int MSG_SIZE = 50;

    private ThunderExporterService() {
        ScheduledExecutorService schExService = Executors.newScheduledThreadPool(1);
        schExService.scheduleAtFixedRate(new Runnable() {

            public void run() {
                System.out.println("Drain the queue Remote");
                List<MessageBreaker> listBreaker = new ArrayList<MessageBreaker>();
                int result = queueRemoteBreaker.drainTo(listBreaker, MSG_SIZE);
                System.out.println("Drain Remote size " + result);
                if (result > 0) {
                    System.out.println("Send the Data ");
                    sendDataHTTPOldSchool("/addBreaker", listBreaker);
                }
                if (CoreEngine.getInstance().getConfig().getPerformance()) {
                    System.out.println("Drain the queue Performance");
                    List<MessageMetrics> listPerformance = new ArrayList<MessageMetrics>();
                    int resultPerf = queueRemotePerformance.drainTo(listPerformance, MSG_SIZE);
                    System.out.println("Drain Performance size " + resultPerf);
                    if (resultPerf > 0) {
                        System.out.println("Send the Data ");
                        sendDataHTTPOldSchool("/addPerformance", listPerformance);
                    }
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
        String token = sendDataHTTPOldSchool("/createThunderApp/", CoreEngine.getInstance().getConfig());
        CoreEngine.getInstance().getConfig().setToken(token);
        System.out.println("application registred with token" + token + "for application" + CoreEngine.getInstance().getConfig().getNameApplication());
    }

    public void initPathsRemoteApp() {
        TransformerService ts = new TransformerService();
        List<EntryPathData> res = ts.collectBreakerDataFromLeechPlugin(CoreEngine.getInstance().getLeechServiceRegistered());
        System.out.println("List EntryPathData for init Path");
        for (EntryPathData en : res) {
            System.out.println(en.toString());
        }
        MessageThunderApp msg = new MessageThunderApp();
        msg.setListentryPathData(res);
        msg.setToken(CoreEngine.getInstance().getConfig().getToken());
        sendDataHTTPOldSchool("/initThunderApp", msg);
    }

    public void sendRemotePerformance(String fullMethodName, int timeExec, String parameters) {
        MessageMetrics msg = new MessageMetrics();
        msg.setPathClassMethodName(fullMethodName);
        msg.setToken(CoreEngine.getInstance().getConfig().getToken());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date date = new Date();
        msg.setDateIncoming(sdf.format(date));
        msg.setTimeExec(timeExec);
        msg.setParameters(parameters);
        getCpuInformation(msg);
        queueRemotePerformance.add(msg);
    }

    public void sendRemoteBreaker(String pathClassMethodName, String parameters) {
        MessageBreaker msg = new MessageBreaker();
        msg.setPathClassMethodName(pathClassMethodName);
        msg.setToken(CoreEngine.getInstance().getConfig().getToken());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy:hh-mm-ss");
        Date date = new Date();
        msg.setDateIncoming(sdf.format(date));
        msg.setParameters(parameters);
        queueRemoteBreaker.add(msg);
    }

    public String sendDataHTTPOldSchool(String uri, Object message) {
        Gson gson = new GsonBuilder().setExclusionStrategies(new StrategyGsonH2H()).create();
        String data = gson.toJson(message);
        String urlServer = CoreEngine.getInstance().getConfig().getUrlH2hWeb() + uri;
        String response = null;
        try {
            URL url = new URL(urlServer);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput( true );
            conn.setInstanceFollowRedirects( false );
            conn.setRequestMethod( "POST" );
            conn.setRequestProperty( "Content-Type", "application/json");
            conn.setRequestProperty( "charset", "utf-8");
            conn.setUseCaches( false );
            byte[] postDataBytes = data.toString().getBytes("UTF-8");
            conn.setRequestProperty( "Content-Length", String.valueOf(postDataBytes.length));
            DataOutputStream writer = new DataOutputStream(conn.getOutputStream());
            writer.write(postDataBytes);
            writer.flush();
            //read the response
            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) >= 0;)
                sb.append((char)c);
            response = sb.toString();
            in.close();
        } catch (Exception e) {
            System.err.println("Error during the call http for request "+uri+e);
        }
        return response;


    }

    /*public String sendDataHTTP(String uri, Object message) {
        Gson gson = new Gson();
        String data = gson.toJson(message);
        String urlServer = CoreEngine.getInstance().getConfig().getUrlH2hWeb() + uri;
        System.out.println("Send Request to Server with uri" + urlServer + " and data" + data);
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
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpURLConnection.HTTP_CREATED
                        || statusCode == HttpURLConnection.HTTP_OK
                        || statusCode == HttpURLConnection.HTTP_ACCEPTED
                        || statusCode == HttpURLConnection.HTTP_NO_CONTENT) {
                    result = EntityUtils.toString(response.getEntity());
                    System.out.println("Message from H2H server" + result);
                } else {
                    System.err.println("Failed : HTTP error code : "
                            + statusCode
                            + " for urlServer " + urlServer + "msg "
                            + EntityUtils.toString(response.getEntity()));
                }
            } else {
                System.err.println("Failed : Response Null from urlServer : " + urlServer);
            }

        } catch (IOException e) {
            System.err.println("Error while sending dataHttp to " + urlServer + " : " + e);
        }
        return result;
    }*/

    private void getCpuInformation(MessageMetrics mb) {
        OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        Double cpuLoadProcess = Double.valueOf(operatingSystemMXBean.getProcessCpuLoad() * 100);
        Double cpuLoadSystem = Double.valueOf(operatingSystemMXBean.getSystemCpuLoad() * 100);
        mb.setCpuLoadProcess(cpuLoadProcess);
        mb.setCpuLoadSystem(cpuLoadSystem);
    }
}
