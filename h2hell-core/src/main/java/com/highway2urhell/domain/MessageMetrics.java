package com.highway2urhell.domain;

import java.util.Date;

public class MessageMetrics {
	private String pathClassMethodName;
	private String token;
	private Date dateIncoming;
	private long timeExec;
	public String getPathClassMethodName() {
		return pathClassMethodName;
	}
	public void setPathClassMethodName(String pathClassMethodName) {
		this.pathClassMethodName = pathClassMethodName;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getDateIncoming() {
		return dateIncoming;
	}
	public void setDateIncoming(Date dateIncoming) {
		this.dateIncoming = dateIncoming;
	}
	public long getTimeExec() {
		return timeExec;
	}
	public void setTimeExec(long timeExec) {
		this.timeExec = timeExec;
	}

}
