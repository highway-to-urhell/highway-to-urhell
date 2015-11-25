package com.highway2urhell.domain;


public class MessageEvent {

    private String id;
    private String token;
    private String reference;
    private TypeMessageEvent typeMessageEvent;
    private Object data;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TypeMessageEvent getTypeMessageEvent() {
        return typeMessageEvent;
    }

    public void setTypeMessageEvent(TypeMessageEvent typeMessageEvent) {
        this.typeMessageEvent = typeMessageEvent;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
