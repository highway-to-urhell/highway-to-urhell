package com.highway2urhell.service.dto;

/**
 * Created by guillaumedufour on 30/01/2017.
 */
public class ApplicationByType {


    private Long data;

    private String label;

    public ApplicationByType(String label, Long data) {
        this.label = label;
        this.data = data;
    }

    public Long getData() {
        return data;
    }

    public String getLabel() {
        return label;
    }
}
