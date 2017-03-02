package com.highway2urhell.web.rest.dto.v1api;

import com.highway2urhell.domain.Event;
import com.highway2urhell.service.mapper.TypeMessageEventMapper;

/**
 * Created by guillaumedufour on 12/02/2017.
 */
public class MessageEvent {

    private String id;
    private String token;
    private String reference;
    private TypeMessageEvent typeMessageEvent;
    private String data;

    public MessageEvent() {
        //Empty one
    }

    public MessageEvent(Event event) {
        this.id = String.valueOf(event.getId());
        this.token = event.getAnalysis().getApplication().getToken();
        this.typeMessageEvent = TypeMessageEventMapper.domainToDto(event.getTypeMessageEvent());
    }

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MessageEvent{" +
            "id='" + id + '\'' +
            ", token='" + token + '\'' +
            ", reference='" + reference + '\'' +
            ", typeMessageEvent=" + typeMessageEvent +
            ", data='" + data + '\'' +
            '}';
    }
}
