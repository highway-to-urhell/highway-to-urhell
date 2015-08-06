package io.highway.to.urhell.service.impl;

import io.highway.to.urhell.domain.EntryPathData;
import io.highway.to.urhell.domain.TypePath;
import io.highway.to.urhell.service.AbstractLeechService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PortService extends AbstractLeechService {

    public static final String FRAMEWORK_NAME = "SYSTEM-PORT";
    private static final int MIN_PORT = 1024;
    private static final int MAX_PORT = 65535;

    public PortService() {
        super(FRAMEWORK_NAME);
        setTriggerAtStartup(true);
    }

    @Override
    protected void gatherData(List<EntryPathData> incoming) {
        // List Ports
        List<Integer> listPorts = new ArrayList<Integer>();
        for (int i = MIN_PORT; i < MAX_PORT; i++) {
            listPorts.add(i);
        }

        for (Integer port : listPorts) {
            try {
                Socket serverSok = new Socket("127.0.0.1", port.intValue());
                EntryPathData entry = new EntryPathData();
                entry.setTypePath(TypePath.STATIC);
                entry.setClassName("portAnalysis");
                entry.setMethodName(String.valueOf(port));
                entry.setUri(String.valueOf("lsof -i :"+String.valueOf(port)+" -t | xargs ps -o cmd"));
                entry.setAudit(false);
                addEntryPath(entry);
                serverSok.close();
            } catch (IOException ex) {
                continue; // try next
            }
        }
    }

}
