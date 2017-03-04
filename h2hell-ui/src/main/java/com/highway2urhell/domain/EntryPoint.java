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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "draw_analysis")
    private Boolean drawAnalysis;

    @OneToMany(mappedBy = "entryPoint")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EntryPointPerf> perfs = new HashSet<>();

    @OneToMany(mappedBy = "entryPoint")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EntryPointCall> calls = new HashSet<>();

    @ManyToOne
    private Analysis analysis;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPathClassMethodName() {
        return pathClassMethodName;
    }

    public EntryPoint pathClassMethodName(String pathClassMethodName) {
        this.pathClassMethodName = pathClassMethodName;
        return this;
    }

    public void setPathClassMethodName(String pathClassMethodName) {
        this.pathClassMethodName = pathClassMethodName;
    }

    public Long getCount() {
        return count;
    }

    public EntryPoint count(Long count) {
        this.count = count;
        return this;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Boolean isFalsePositive() {
        return falsePositive;
    }

    public EntryPoint falsePositive(Boolean falsePositive) {
        this.falsePositive = falsePositive;
        return this;
    }

    public void setFalsePositive(Boolean falsePositive) {
        this.falsePositive = falsePositive;
    }

    public String getUri() {
        return uri;
    }

    public EntryPoint uri(String uri) {
        this.uri = uri;
        return this;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getHttpmethod() {
        return httpmethod;
    }

    public EntryPoint httpmethod(String httpmethod) {
        this.httpmethod = httpmethod;
        return this;
    }

    public void setHttpmethod(String httpmethod) {
        this.httpmethod = httpmethod;
    }

    public Boolean isAudit() {
        return audit;
    }

    public EntryPoint audit(Boolean audit) {
        this.audit = audit;
        return this;
    }

    public void setAudit(Boolean audit) {
        this.audit = audit;
    }

    public Long getAverageTime() {
        return averageTime;
    }

    public EntryPoint averageTime(Long averageTime) {
        this.averageTime = averageTime;
        return this;
    }

    public void setAverageTime(Long averageTime) {
        this.averageTime = averageTime;
    }

    public Boolean isCheckLaunch() {
        return checkLaunch;
    }

    public EntryPoint checkLaunch(Boolean checkLaunch) {
        this.checkLaunch = checkLaunch;
        return this;
    }

    public void setCheckLaunch(Boolean checkLaunch) {
        this.checkLaunch = checkLaunch;
    }

    public Boolean isDrawAnalysis() {
        return drawAnalysis;
    }

    public EntryPoint drawAnalysis(Boolean drawAnalysis) {
        this.drawAnalysis = drawAnalysis;
        return this;
    }

    public void setDrawAnalysis(Boolean drawAnalysis) {
        this.drawAnalysis = drawAnalysis;
    }

    public Set<EntryPointPerf> getPerfs() {
        return perfs;
    }

    public EntryPoint perfs(Set<EntryPointPerf> entryPointPerfs) {
        this.perfs = entryPointPerfs;
        return this;
    }

    public EntryPoint addPerfs(EntryPointPerf entryPointPerf) {
        this.perfs.add(entryPointPerf);
        entryPointPerf.setEntryPoint(this);
        return this;
    }

    public EntryPoint removePerfs(EntryPointPerf entryPointPerf) {
        this.perfs.remove(entryPointPerf);
        entryPointPerf.setEntryPoint(null);
        return this;
    }

    public void setPerfs(Set<EntryPointPerf> entryPointPerfs) {
        this.perfs = entryPointPerfs;
    }

    public Set<EntryPointCall> getCalls() {
        return calls;
    }

    public EntryPoint calls(Set<EntryPointCall> entryPointCalls) {
        this.calls = entryPointCalls;
        return this;
    }

    public EntryPoint addCalls(EntryPointCall entryPointCall) {
        this.calls.add(entryPointCall);
        entryPointCall.setEntryPoint(this);
        return this;
    }

    public EntryPoint removeCalls(EntryPointCall entryPointCall) {
        this.calls.remove(entryPointCall);
        entryPointCall.setEntryPoint(null);
        return this;
    }

    public void setCalls(Set<EntryPointCall> entryPointCalls) {
        this.calls = entryPointCalls;
    }

    public Analysis getAnalysis() {
        return analysis;
    }

    public EntryPoint analysis(Analysis analysis) {
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
        EntryPoint entryPoint = (EntryPoint) o;
        if (entryPoint.id == null || id == null) {
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
            ", drawAnalysis='" + drawAnalysis + "'" +
            '}';
    }
}
