package com.highway2urhell.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A EntryPointParameters.
 */
@Entity
@Table(name = "entry_point_parameters")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EntryPointParameters implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_incoming")
    private ZonedDateTime dateIncoming;

    @Lob
    @Column(name = "parameters")
    private byte[] parameters;

    @Column(name = "parameters_content_type")
    private String parametersContentType;

    @ManyToOne
    private EntryPoint entryPoint;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateIncoming() {
        return dateIncoming;
    }

    public EntryPointParameters dateIncoming(ZonedDateTime dateIncoming) {
        this.dateIncoming = dateIncoming;
        return this;
    }

    public void setDateIncoming(ZonedDateTime dateIncoming) {
        this.dateIncoming = dateIncoming;
    }

    public byte[] getParameters() {
        return parameters;
    }

    public EntryPointParameters parameters(byte[] parameters) {
        this.parameters = parameters;
        return this;
    }

    public void setParameters(byte[] parameters) {
        this.parameters = parameters;
    }

    public String getParametersContentType() {
        return parametersContentType;
    }

    public EntryPointParameters parametersContentType(String parametersContentType) {
        this.parametersContentType = parametersContentType;
        return this;
    }

    public void setParametersContentType(String parametersContentType) {
        this.parametersContentType = parametersContentType;
    }

    public EntryPoint getEntryPoint() {
        return entryPoint;
    }

    public EntryPointParameters entryPoint(EntryPoint entryPoint) {
        this.entryPoint = entryPoint;
        return this;
    }

    public void setEntryPoint(EntryPoint entryPoint) {
        this.entryPoint = entryPoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EntryPointParameters entryPointParameters = (EntryPointParameters) o;
        if (entryPointParameters.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, entryPointParameters.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EntryPointParameters{" +
            "id=" + id +
            ", dateIncoming='" + dateIncoming + "'" +
            ", parameters='" + parameters + "'" +
            ", parametersContentType='" + parametersContentType + "'" +
            '}';
    }
}
