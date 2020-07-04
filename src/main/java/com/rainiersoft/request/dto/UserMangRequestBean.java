package com.rainiersoft.request.dto;

import java.util.List;

public class UserMangRequestBean 
{
	@Override
	public String toString() {
		return "UserMangRequestBean [UserName=" + UserName + ", UserFirstName=" + UserFirstName + ", UserLastName="
				+ UserLastName + ", UserDOB=" + UserDOB + ", UserAadharNum=" + UserAadharNum + ", UserMobileNum="
				+ UserMobileNum + ", UserPassword=" + UserPassword + ", UserType=" + UserType + ", UserRole=" + UserRole
				+ ", UserStatus=" + UserStatus + ", userId=" + userId + ", editUserNameFlag=" + editUserNameFlag + "]";
	}
	private String UserName;
	private String UserFirstName;
	private String UserLastName;
	private String UserDOB;
	private String UserAadharNum;
	private String UserMobileNum;
	private String UserPassword;
	private List<String> UserType;
	private String UserRole;
	private String UserStatus;
	private int userId;
	private boolean editUserNameFlag;
	private boolean editPwdFlag;
	private String userCreatedBy;
	private String userUpdatedBy;

	public String getUserUpdatedBy() {
		return userUpdatedBy;
	}
	public void setUserUpdatedBy(String userUpdatedBy) {
		this.userUpdatedBy = userUpdatedBy;
	}
	public String getUserCreatedBy() {
		return userCreatedBy;
	}
	public void setUserCreatedBy(String userCreatedBy) {
		this.userCreatedBy = userCreatedBy;
	}
	public boolean getEditPwdFlag() {
		return editPwdFlag;
	}
	public void setEditPwdFlag(boolean editPwdFlag) {
		this.editPwdFlag = editPwdFlag;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public boolean getEditUserNameFlag() {
		return editUserNameFlag;
	}
	public void setEditUserNameFlag(boolean editUserNameFlag) {
		this.editUserNameFlag = editUserNameFlag;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getUserFirstName() {
		return UserFirstName;
	}
	public String getUserRole() {
		return UserRole;
	}
	public void setUserRole(String userRole) {
		UserRole = userRole;
	}
	public void setUserFirstName(String userFirstName) {
		UserFirstName = userFirstName;
	}
	public String getUserLastName() {
		return UserLastName;
	}
	public void setUserLastName(String userLastName) {
		UserLastName = userLastName;
	}
	public String getUserDOB() {
		return UserDOB;
	}
	public void setUserDOB(String userDOB) {
		UserDOB = userDOB;
	}
	public String getUserAadharNum() {
		return UserAadharNum;
	}
	public void setUserAadharNum(String userAadharNum) {
		UserAadharNum = userAadharNum;
	}
	public String getUserMobileNum() {
		return UserMobileNum;
	}
	public void setUserMobileNum(String userMobileNum) {
		UserMobileNum = userMobileNum;
	}
	public String getUserPassword() {
		return UserPassword;
	}
	public void setUserPassword(String userPassword) {
		UserPassword = userPassword;
	}
	public List<String> getUserType() {
		return UserType;
	}
	public void setUserType(List<String> userType) {
		UserType = userType;
	}
	public String getUserStatus() {
		return UserStatus;
	}
	public void setUserStatus(String userStatus) {
		UserStatus = userStatus;
	}
}