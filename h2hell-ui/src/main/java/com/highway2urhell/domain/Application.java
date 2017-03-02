package com.highway2urhell.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Application.
 */
@Entity
@Table(name = "application")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Application implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "date_creation")
    private ZonedDateTime dateCreation;

    @Column(name = "url_app")
    private String urlApp;

    @Column(name = "description")
    private String description;

    @Column(name = "app_type")
    private String appType;

    @Column(name = "analysed")
    private Boolean analysed;

    @OneToMany(mappedBy = "application")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Analysis> analyses = new HashSet<>();

    @ManyToMany(mappedBy = "projects")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserPermission> userPermissions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Application name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public Application token(String token) {
        this.token = token;
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ZonedDateTime getDateCreation() {
        return dateCreation;
    }

    public Application dateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getUrlApp() {
        return urlApp;
    }

    public Application urlApp(String urlApp) {
        this.urlApp = urlApp;
        return this;
    }

    public void setUrlApp(String urlApp) {
        this.urlApp = urlApp;
    }

    public String getDescription() {
        return description;
    }

    public Application description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAppType() {
        return appType;
    }

    public Application appType(String appType) {
        this.appType = appType;
        return this;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public Boolean isAnalysed() {
        return analysed;
    }

    public Application analysed(Boolean analysed) {
        this.analysed = analysed;
        return this;
    }

    public void setAnalysed(Boolean analysed) {
        this.analysed = analysed;
    }

    public Set<Analysis> getAnalyses() {
        return analyses;
    }

    public Application analyses(Set<Analysis> analyses) {
        this.analyses = analyses;
        return this;
    }

    public Application addAnalyses(Analysis analysis) {
        this.analyses.add(analysis);
        analysis.setApplication(this);
        return this;
    }

    public Application removeAnalyses(Analysis analysis) {
        this.analyses.remove(analysis);
        analysis.setApplication(null);
        return this;
    }

    public void setAnalyses(Set<Analysis> analyses) {
        this.analyses = analyses;
    }

    public Set<UserPermission> getUserPermissions() {
        return userPermissions;
    }

    public Application userPermissions(Set<UserPermission> userPermissions) {
        this.userPermissions = userPermissions;
        return this;
    }

    public Application addUserPermissions(UserPermission userPermission) {
        this.userPermissions.add(userPermission);
        userPermission.getProjects().add(this);
        return this;
    }

    public Application removeUserPermissions(UserPermission userPermission) {
        this.userPermissions.remove(userPermission);
        userPermission.getProjects().remove(this);
        return this;
    }

    public void setUserPermissions(Set<UserPermission> userPermissions) {
        this.userPermissions = userPermissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Application application = (Application) o;
        if (application.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, application.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Application{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", token='" + token + "'" +
            ", dateCreation='" + dateCreation + "'" +
            ", urlApp='" + urlApp + "'" +
            ", description='" + description + "'" +
            ", appType='" + appType + "'" +
            ", analysed='" + analysed + "'" +
            '}';
    }
}
