package io.highway.to.urhell.domain;

public class MessageMetrics {
	private String pathClassMethodName;
	private String token;
	private String dateIncoming;
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
	public String getDateIncoming() {
		return dateIncoming;
	}
	public void setDateIncoming(String dateIncoming) {
		this.dateIncoming = dateIncoming;
	}
	public long getTimeExec() {
		return timeExec;
	}
	public void setTimeExec(long timeExec) {
		this.timeExec = timeExec;
	}

}
