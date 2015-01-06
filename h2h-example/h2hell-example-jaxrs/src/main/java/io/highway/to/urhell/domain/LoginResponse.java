package io.highway.to.urhell.domain;

public class LoginResponse {

	private String clientId;
	private String corporateName;
	private String lastName;
	private String firstName;
	private String email;
	private Boolean isPrepaid;
	private Boolean isMultipass;
	private Boolean isCardAlert;
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getCorporateName() {
		return corporateName;
	}
	public void setCorporateName(String corporateName) {
		this.corporateName = corporateName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Boolean getIsPrepaid() {
		return isPrepaid;
	}
	public void setIsPrepaid(Boolean isPrepaid) {
		this.isPrepaid = isPrepaid;
	}
	public Boolean getIsMultipass() {
		return isMultipass;
	}
	public void setIsMultipass(Boolean isMultipass) {
		this.isMultipass = isMultipass;
	}
	public Boolean getIsCardAlert() {
		return isCardAlert;
	}
	public void setIsCardAlert(Boolean isCardAlert) {
		this.isCardAlert = isCardAlert;
	}
	
}
