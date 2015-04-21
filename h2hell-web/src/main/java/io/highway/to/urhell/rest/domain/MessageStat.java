package io.highway.to.urhell.rest.domain;

import io.highway.to.urhell.domain.ThunderStat;

import java.util.List;

public class MessageStat {
	
	private String appName;
	private List<ThunderStat> listThunderStat;
	private String token;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	private Integer totalFalsePositive;
	private Integer totalNoTest;
	private Integer totalStat;
	
	public List<ThunderStat> getListThunderStat() {
		return listThunderStat;
	}
	public void setListThunderStat(List<ThunderStat> listThunderStat) {
		this.listThunderStat = listThunderStat;
	}
	public Integer getTotalFalsePositive() {
		return totalFalsePositive;
	}
	public void setTotalFalsePositive(Integer totalFalsePositive) {
		this.totalFalsePositive = totalFalsePositive;
	}
	public Integer getTotalNoTest() {
		return totalNoTest;
	}
	public void setTotalNoTest(Integer totalNoTest) {
		this.totalNoTest = totalNoTest;
	}
	public Integer getTotalStat() {
		return totalStat;
	}
	public void setTotalStat(Integer totalStat) {
		this.totalStat = totalStat;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
}
