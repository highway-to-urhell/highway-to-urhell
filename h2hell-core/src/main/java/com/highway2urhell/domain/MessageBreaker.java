package com.highway2urhell.domain;

import java.util.List;

public class MessageBreaker {
    private String pathClassMethodName;
    private String token;
    private String dateIncoming;
    private List<String> parameters;

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }


    public String getPathClassMethodName() {
        return pathClassMethodName;
    }

    public void setPathClassMethodName(String pathClassMethodName) {
        this.pathClassMethodName = pathClassMethodName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDateIncoming() {
        return dateIncoming;
    }

    public void setDateIncoming(String dateIncoming) {
        this.dateIncoming = dateIncoming;
    }

}
