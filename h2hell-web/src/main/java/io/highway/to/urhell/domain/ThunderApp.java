package io.highway.to.urhell.domain;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ThunderApp {
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;
	@NotNull
	private String nameApp;
	@NotNull
	private String token ="NO_TOKEN";
	@NotNull
	private String dateCreation;
	private String urlApp;
	private String description;
	private String pathSource;
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "thunderApp", cascade =  CascadeType.REMOVE)
	@JsonIgnore
    private Set<ThunderStat> thunderStatSet = new LinkedHashSet<ThunderStat>();
	

	public Set<ThunderStat> getThunderStatSet() {
		return thunderStatSet;
	}

	public void setThunderStatSet(Set<ThunderStat> thunderStatSet) {
		this.thunderStatSet = thunderStatSet;
	}

	public String getPathSource() {
		return pathSource;
	}

	public void setPathSource(String pathSource) {
		this.pathSource = pathSource;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrlApp() {
		return urlApp;
	}

	public void setUrlApp(String urlApp) {
		this.urlApp = urlApp;
	}

	public String getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(String dateCreation) {
		this.dateCreation = dateCreation;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNameApp() {
		return nameApp;
	}

	public void setNameApp(String nameApp) {
		this.nameApp = nameApp;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
