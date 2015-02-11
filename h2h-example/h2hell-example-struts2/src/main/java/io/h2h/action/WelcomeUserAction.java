package io.h2h.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WelcomeUserAction {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	// all struts logic here
	public String execute() {
		log.error(" init passage ");
		return "SUCCESS";

	}
}