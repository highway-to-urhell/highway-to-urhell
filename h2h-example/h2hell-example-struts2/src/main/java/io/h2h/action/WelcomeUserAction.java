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
		int range = (10 - 0) + 1;
		int cal = (int) (Math.random() * range) + 0;
		if (cal%2 == 0){
			log.error(" Modulo 2 ");
			return "SUCCESS";
		}else{
			log.error(" No Module 2 ");
			return "SUCCESS";
		}
	
	}

}