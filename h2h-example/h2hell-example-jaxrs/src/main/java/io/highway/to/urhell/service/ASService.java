package io.highway.to.urhell.service;

import io.highway.to.urhell.domain.ASResponse;
import io.highway.to.urhell.domain.LoginResponse;

import javax.inject.Named;

@Named
public class ASService {

	
	public ASResponse checklogin(String login, String password){
		ASResponse response = new ASResponse();
		StringBuilder sb = new StringBuilder();
		sb.append("login = ");
		sb.append(login);
		sb.append(" - password = ");
		sb.append(password);
		
		response.setResponse(sb.toString());
		return response;
	}
	
	public LoginResponse checkLoginAs24(String login, String password, String externaluser){
		LoginResponse response = new LoginResponse();
		response.setClientId("123456789");
		response.setCorporateName("corporate Name");
		response.setEmail("boss@boss.com");
		response.setFirstName("prenom");
		response.setLastName("nom");
		response.setIsCardAlert(true);
		response.setIsMultipass(true);
		response.setIsPrepaid(true);
		return response;
		
	}
	
}
