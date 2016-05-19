package com.highway2urhell.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "time_exec")
    private Integer timeExec;

    @Column(name = "cpu_load_system")
    private Double cpuLoadSystem;

    @Column(name = "cpu_load_process")
    private Double cpuLoadProcess;

    @ManyToOne
    private EntryPointParameters log;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTimeExec() {
        return timeExec;
    }

    public void setTimeExec(Integer timeExec) {
        this.timeExec = timeExec;
    }

    public Double getCpuLoadSystem() {
        return cpuLoadSystem;
    }

    public void setCpuLoadSystem(Double cpuLoadSystem) {
        this.cpuLoadSystem = cpuLoadSystem;
    }

    public Double getCpuLoadProcess() {
        return cpuLoadProcess;
    }

    public void setCpuLoadProcess(Double cpuLoadProcess) {
        this.cpuLoadProcess = cpuLoadProcess;
    }

    public EntryPointParameters getLog() {
        return log;
    }

    public void setLog(EntryPointParameters entryPointParameters) {
        this.log = entryPointParameters;
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
        if(metricsTimer.id == null || id == null) {
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
            ", timeExec='" + timeExec + "'" +
            ", cpuLoadSystem='" + cpuLoadSystem + "'" +
            ", cpuLoadProcess='" + cpuLoadProcess + "'" +
            '}';
    }
}
