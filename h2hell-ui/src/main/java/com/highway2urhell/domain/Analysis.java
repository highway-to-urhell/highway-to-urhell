package com.highway2urhell.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Analysis.
 */
@Entity
@Table(name = "analysis")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Analysis implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_creation")
    private ZonedDateTime dateCreation;

    @Column(name = "path_source")
    private String pathSource;

    @Column(name = "number_entry_points")
    private Integer numberEntryPoints;

    @Column(name = "app_version")
    private String appVersion;

    @OneToMany(mappedBy = "analysis")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = "analysis")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EntryPoint> entryPoints = new HashSet<>();

    @ManyToOne
    private Application application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateCreation() {
        return dateCreation;
    }

    public Analysis dateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getPathSource() {
        return pathSource;
    }

    public Analysis pathSource(String pathSource) {
        this.pathSource = pathSource;
        return this;
    }

    public void setPathSource(String pathSource) {
        this.pathSource = pathSource;
    }

    public Integer getNumberEntryPoints() {
        return numberEntryPoints;
    }

    public Analysis numberEntryPoints(Integer numberEntryPoints) {
        this.numberEntryPoints = numberEntryPoints;
        return this;
    }

    public void setNumberEntryPoints(Integer numberEntryPoints) {
        this.numberEntryPoints = numberEntryPoints;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public Analysis appVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Analysis events(Set<Event> events) {
        this.events = events;
        return this;
    }

    public Analysis addEvents(Event event) {
        this.events.add(event);
        event.setAnalysis(this);
        return this;
    }

    public Analysis removeEvents(Event event) {
        this.events.remove(event);
        event.setAnalysis(null);
        return this;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Set<EntryPoint> getEntryPoints() {
        return entryPoints;
    }

    public Analysis entryPoints(Set<EntryPoint> entryPoints) {
        this.entryPoints = entryPoints;
        return this;
    }

    public Analysis addEntryPoint(EntryPoint entryPoint) {
        this.entryPoints.add(entryPoint);
        entryPoint.setAnalysis(this);
        return this;
    }

    public Analysis removeEntryPoint(EntryPoint entryPoint) {
        this.entryPoints.remove(entryPoint);
        entryPoint.setAnalysis(null);
        return this;
    }

    public void setEntryPoints(Set<EntryPoint> entryPoints) {
        this.entryPoints = entryPoints;
    }

    public Application getApplication() {
        return application;
    }

    public Analysis application(Application application) {
        this.application = application;
        return this;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Analysis analysis = (Analysis) o;
        if (analysis.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, analysis.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Analysis{" +
            "id=" + id +
            ", dateCreation='" + dateCreation + "'" +
            ", pathSource='" + pathSource + "'" +
            ", numberEntryPoints='" + numberEntryPoints + "'" +
            ", appVersion='" + appVersion + "'" +
            '}';
    }
}
