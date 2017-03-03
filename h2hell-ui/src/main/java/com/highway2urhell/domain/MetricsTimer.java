package com.highway2urhell.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A MetricsTimer.
 */
@Entity
@Table(name = "metrics_timer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MetricsTimer implements Serializable {

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

    @Column(name = "time_exec")
    private Integer timeExec;

    @Column(name = "cpu_load_system")
    private Double cpuLoadSystem;

    @Column(name = "cpu_load_process")
    private Double cpuLoadProcess;

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

    public MetricsTimer dateIncoming(ZonedDateTime dateIncoming) {
        this.dateIncoming = dateIncoming;
        return this;
    }

    public void setDateIncoming(ZonedDateTime dateIncoming) {
        this.dateIncoming = dateIncoming;
    }

    public byte[] getParameters() {
        return parameters;
    }

    public MetricsTimer parameters(byte[] parameters) {
        this.parameters = parameters;
        return this;
    }

    public void setParameters(byte[] parameters) {
        this.parameters = parameters;
    }

    public String getParametersContentType() {
        return parametersContentType;
    }

    public MetricsTimer parametersContentType(String parametersContentType) {
        this.parametersContentType = parametersContentType;
        return this;
    }

    public void setParametersContentType(String parametersContentType) {
        this.parametersContentType = parametersContentType;
    }

    public Integer getTimeExec() {
        return timeExec;
    }

    public MetricsTimer timeExec(Integer timeExec) {
        this.timeExec = timeExec;
        return this;
    }

    public void setTimeExec(Integer timeExec) {
        this.timeExec = timeExec;
    }

    public Double getCpuLoadSystem() {
        return cpuLoadSystem;
    }

    public MetricsTimer cpuLoadSystem(Double cpuLoadSystem) {
        this.cpuLoadSystem = cpuLoadSystem;
        return this;
    }

    public void setCpuLoadSystem(Double cpuLoadSystem) {
        this.cpuLoadSystem = cpuLoadSystem;
    }

    public Double getCpuLoadProcess() {
        return cpuLoadProcess;
    }

    public MetricsTimer cpuLoadProcess(Double cpuLoadProcess) {
        this.cpuLoadProcess = cpuLoadProcess;
        return this;
    }

    public void setCpuLoadProcess(Double cpuLoadProcess) {
        this.cpuLoadProcess = cpuLoadProcess;
    }

    public EntryPoint getEntryPoint() {
        return entryPoint;
    }

    public MetricsTimer entryPoint(EntryPoint entryPoint) {
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
        MetricsTimer metricsTimer = (MetricsTimer) o;
        if (metricsTimer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, metricsTimer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MetricsTimer{" +
            "id=" + id +
            ", dateIncoming='" + dateIncoming + "'" +
            ", parameters='" + parameters + "'" +
            ", parametersContentType='" + parametersContentType + "'" +
            ", timeExec='" + timeExec + "'" +
            ", cpuLoadSystem='" + cpuLoadSystem + "'" +
            ", cpuLoadProcess='" + cpuLoadProcess + "'" +
            '}';
    }
}
