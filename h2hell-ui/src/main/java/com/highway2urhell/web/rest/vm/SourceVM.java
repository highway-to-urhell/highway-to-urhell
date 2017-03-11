package com.highway2urhell.web.rest.vm;

/**
 * Created by guillaumedufour on 10/03/2017.
 */
public class SourceVM {

    public SourceVM(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    String data;
}
