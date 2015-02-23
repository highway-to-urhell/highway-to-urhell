package io.highway.to.urhell.rest.domain;

import io.highway.to.urhell.domain.BreakerData;

import java.util.List;

public class MessageThunderApp {
	
	private String token;
	private List<BreakerData> listBreakerData;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public List<BreakerData> getListBreakerData() {
		return listBreakerData;
	}
	public void setListBreakerData(List<BreakerData> listBreakerData) {
		this.listBreakerData = listBreakerData;
	}
	

}
