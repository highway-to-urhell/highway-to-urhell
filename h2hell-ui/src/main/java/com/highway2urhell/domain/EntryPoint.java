package com.highway2urhell.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A EntryPoint.
 */
@Entity
@Table(name = "entry_point")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EntryPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "path_class_method_name", nullable = false)
    private String pathClassMethodName;

    @Column(name = "count")
    private Long count;

    @Column(name = "false_positive")
    private Boolean falsePositive;

    @Column(name = "uri")
    private String uri;

    @Column(name = "httpmethod")
    private String httpmethod;

    @Column(name = "audit")
    private Boolean audit;

    @Column(name = "average_time")
    private Long averageTime;

    @Column(name = "check_launch")
    private Boolean checkLaunch;

    @OneToMany(mappedBy = "entryPoint")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EntryPointParameters> logs = new HashSet<>();

    @ManyToOne
    private Analysis application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPathClassMethodName() {
        return pathClassMethodName;
    }

    public void setPathClassMethodName(String pathClassMethodName) {
        this.pathClassMethodName = pathClassMethodName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Boolean isFalsePositive() {
        return falsePositive;
    }

    public void setFalsePositive(Boolean falsePositive) {
        this.falsePositive = falsePositive;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getHttpmethod() {
        return httpmethod;
    }

    public void setHttpmethod(String httpmethod) {
        this.httpmethod = httpmethod;
    }

    public Boolean isAudit() {
        return audit;
    }

    public void setAudit(Boolean audit) {
        this.audit = audit;
    }

    public Long getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(Long averageTime) {
        this.averageTime = averageTime;
    }

    public Boolean isCheckLaunch() {
        return checkLaunch;
    }

    public void setCheckLaunch(Boolean checkLaunch) {
        this.checkLaunch = checkLaunch;
    }

    public Set<EntryPointParameters> getLogs() {
        return logs;
    }

    public void setLogs(Set<EntryPointParameters> entryPointParameters) {
        this.logs = entryPointParameters;
    }

    public Analysis getApplication() {
        return application;
    }

    public void setApplication(Analysis analysis) {
        this.application = analysis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EntryPoint entryPoint = (EntryPoint) o;
        if(entryPoint.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, entryPoint.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EntryPoint{" +
            "id=" + id +
            ", pathClassMethodName='" + pathClassMethodName + "'" +
            ", count='" + count + "'" +
            ", falsePositive='" + falsePositive + "'" +
            ", uri='" + uri + "'" +
            ", httpmethod='" + httpmethod + "'" +
            ", audit='" + audit + "'" +
            ", averageTime='" + averageTime + "'" +
            ", checkLaunch='" + checkLaunch + "'" +
            '}';
    }
}
