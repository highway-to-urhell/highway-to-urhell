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
 * A Team.
 */
@Entity
@Table(name = "team")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "team")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserPermission> userPermissions = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "team_members",
               joinColumns = @JoinColumn(name="teams_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="members_id", referencedColumnName="id"))
    private Set<User> members = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Team name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserPermission> getUserPermissions() {
        return userPermissions;
    }

    public Team userPermissions(Set<UserPermission> userPermissions) {
        this.userPermissions = userPermissions;
        return this;
    }

    public Team addUserPermissions(UserPermission userPermission) {
        this.userPermissions.add(userPermission);
        userPermission.setTeam(this);
        return this;
    }

    public Team removeUserPermissions(UserPermission userPermission) {
        this.userPermissions.remove(userPermission);
        userPermission.setTeam(null);
        return this;
    }

    public void setUserPermissions(Set<UserPermission> userPermissions) {
        this.userPermissions = userPermissions;
    }

    public Set<User> getMembers() {
        return members;
    }

    public Team members(Set<User> users) {
        this.members = users;
        return this;
    }

    public Team addMembers(User user) {
        this.members.add(user);
        return this;
    }

    public Team removeMembers(User user) {
        this.members.remove(user);
        return this;
    }

    public void setMembers(Set<User> users) {
        this.members = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Team team = (Team) o;
        if (team.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, team.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Team{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
