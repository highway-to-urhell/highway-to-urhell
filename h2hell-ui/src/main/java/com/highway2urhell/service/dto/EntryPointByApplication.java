package com.highway2urhell.service.dto;

/**
 * Created by guillaumedufour on 30/01/2017.
 */
public class EntryPointByApplication {

    private Long data;

    private String application;

    private String analyse;

    public EntryPointByApplication(String application, String analyse, Long data) {
        this.application = application;
        this.analyse = analyse;
        this.data = data;
    }

    public Long getData() {
        return data;
    }

    public String getApplication() {
        return application;
    }

    public String getAnalyse() {
        return analyse;
    }
}
