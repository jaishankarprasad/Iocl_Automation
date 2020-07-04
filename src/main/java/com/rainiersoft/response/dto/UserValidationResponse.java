package com.rainiersoft.response.dto;

import java.util.List;

public class UserValidationResponse 
{
	@Override
	public String toString() {
		return "UserValidationResponse [successfulMsg=" + successfulMsg + ", successfulFlag=" + successfulFlag
				+ ", userPrivilages=" + userPrivilages + ", userRole=" + userRole + "]";
	}
	private String successfulMsg;
	private boolean successfulFlag;
	private List<String> userPrivilages;
	private String userRole;
	private int userId;
	private String userName;

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getSuccessfulMsg() {
		return successfulMsg;
	}
	public void setSuccessfulMsg(String successfulMsg) {
		this.successfulMsg = successfulMsg;
	}
	public boolean getSuccessfulFlag() {
		return successfulFlag;
	}
	public void setSuccessfulFlag(boolean successfulFlag) {
		this.successfulFlag = successfulFlag;
	}
	public List<String> getUserPrivilages() {
		return userPrivilages;
	}
	public void setUserPrivilages(List<String> userPrivilages) {
		this.userPrivilages = userPrivilages;
	}

}
