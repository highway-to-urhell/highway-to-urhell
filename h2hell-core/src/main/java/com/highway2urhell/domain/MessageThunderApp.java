package com.highway2urhell.domain;

import java.util.List;

public class MessageThunderApp {

    private String token;
    private List<EntryPathData> listentryPathData;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

	public List<EntryPathData> getListentryPathData() {
		return listentryPathData;
	}

	public void setListentryPathData(List<EntryPathData> listentryPathData) {
		this.listentryPathData = listentryPathData;
	}
}
