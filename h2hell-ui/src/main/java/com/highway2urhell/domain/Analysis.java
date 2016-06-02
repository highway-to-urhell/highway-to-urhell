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
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getPathSource() {
        return pathSource;
    }

    public void setPathSource(String pathSource) {
        this.pathSource = pathSource;
    }

    public Integer getNumberEntryPoints() {
        return numberEntryPoints;
    }

    public void setNumberEntryPoints(Integer numberEntryPoints) {
        this.numberEntryPoints = numberEntryPoints;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Set<EntryPoint> getEntryPoints() {
        return entryPoints;
    }

    public void setEntryPoints(Set<EntryPoint> entryPoints) {
        this.entryPoints = entryPoints;
    }

    public Application getApplication() {
        return application;
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
        if(analysis.id == null || id == null) {
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
