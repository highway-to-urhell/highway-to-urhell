package com.highway2urhell.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.highway2urhell.domain.enumeration.TypeMessageEvent;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_message_event")
    private TypeMessageEvent typeMessageEvent;

    @Lob
    @Column(name = "data")
    private byte[] data;

    @Column(name = "data_content_type")
    private String dataContentType;

    @Column(name = "consumed")
    private Boolean consumed;

    @ManyToOne
    private Analysis analysis;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeMessageEvent getTypeMessageEvent() {
        return typeMessageEvent;
    }

    public Event typeMessageEvent(TypeMessageEvent typeMessageEvent) {
        this.typeMessageEvent = typeMessageEvent;
        return this;
    }

    public void setTypeMessageEvent(TypeMessageEvent typeMessageEvent) {
        this.typeMessageEvent = typeMessageEvent;
    }

    public byte[] getData() {
        return data;
    }

    public Event data(byte[] data) {
        this.data = data;
        return this;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getDataContentType() {
        return dataContentType;
    }

    public Event dataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
        return this;
    }

    public void setDataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
    }

    public Boolean isConsumed() {
        return consumed;
    }

    public Event consumed(Boolean consumed) {
        this.consumed = consumed;
        return this;
    }

    public void setConsumed(Boolean consumed) {
        this.consumed = consumed;
    }

    public Analysis getAnalysis() {
        return analysis;
    }

    public Event analysis(Analysis analysis) {
        this.analysis = analysis;
        return this;
    }

    public void setAnalysis(Analysis analysis) {
        this.analysis = analysis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        if (event.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + id +
            ", typeMessageEvent='" + typeMessageEvent + "'" +
            ", data='" + data + "'" +
            ", dataContentType='" + dataContentType + "'" +
            ", consumed='" + consumed + "'" +
            '}';
    }
}
