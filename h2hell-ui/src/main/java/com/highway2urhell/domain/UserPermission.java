package com.highway2urhell.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.highway2urhell.domain.enumeration.Permission;

/**
 * A UserPermission.
 */
@Entity
@Table(name = "user_permission")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "permission", nullable = false)
    private Permission permission;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "user_permission_projects",
               joinColumns = @JoinColumn(name="user_permissions_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="projects_id", referencedColumnName="ID"))
    private Set<Application> projects = new HashSet<>();

    @ManyToOne
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Set<Application> getProjects() {
        return projects;
    }

    public void setProjects(Set<Application> applications) {
        this.projects = applications;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserPermission userPermission = (UserPermission) o;
        if(userPermission.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, userPermission.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserPermission{" +
            "id=" + id +
            ", permission='" + permission + "'" +
            '}';
    }
}
