package com.rainiersoft.response.dto;

public class QuantityDeletionResponseBean 
{
	private boolean successFlag;
	private String message;
	public boolean getSuccessFlag() {
		return successFlag;
	}
	public void setSuccessFlag(boolean successFlag) {
		this.successFlag = successFlag;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
