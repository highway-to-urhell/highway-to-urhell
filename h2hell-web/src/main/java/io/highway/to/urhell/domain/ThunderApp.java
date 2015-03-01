package io.highway.to.urhell.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

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
