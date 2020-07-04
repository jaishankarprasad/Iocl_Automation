package com.rainiersoft.response.dto;

public class ContractorDetailsResponseBean 
{
	@Override
	public String toString() {
		return "ContractorDetailsResponseBean [contractorName=" + contractorName + ", contractorType=" + contractorType
				+ ", contractorAddress=" + contractorAddress + ", contractorCity=" + contractorCity
				+ ", contractorState=" + contractorState + ", contractorPinCode=" + contractorPinCode
				+ ", contractorOperationalStatus=" + contractorOperationalStatus + "]";
	}
	private String contractorName;
	private String contractorType;
	private String contractorAddress;
	private String contractorCity;
	private String contractorState;
	private String contractorPinCode;
	private String contractorOperationalStatus;
	private int contractorId;
	private String contractorCreatedBy;
	private String contractorUpdatedBy;
	private String contractorCreatedOn;
	private String contractorUpdatedOn;
	
	public String getContractorCreatedBy() {
		return contractorCreatedBy;
	}
	public void setContractorCreatedBy(String contractorCreatedBy) {
		this.contractorCreatedBy = contractorCreatedBy;
	}
	public String getContractorUpdatedBy() {
		return contractorUpdatedBy;
	}
	public void setContractorUpdatedBy(String contractorUpdatedBy) {
		this.contractorUpdatedBy = contractorUpdatedBy;
	}
	public String getContractorCreatedOn() {
		return contractorCreatedOn;
	}
	public void setContractorCreatedOn(String contractorCreatedOn) {
		this.contractorCreatedOn = contractorCreatedOn;
	}
	public String getContractorUpdatedOn() {
		return contractorUpdatedOn;
	}
	public void setContractorUpdatedOn(String contractorUpdatedOn) {
		this.contractorUpdatedOn = contractorUpdatedOn;
	}
	public int getContractorId() {
		return contractorId;
	}
	public void setContractorId(int contractorId) {
		this.contractorId = contractorId;
	}
	public String getContractorName() {
		return contractorName;
	}
	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}
	public String getContractorType() {
		return contractorType;
	}
	public void setContractorType(String contractorType) {
		this.contractorType = contractorType;
	}
	public String getContractorAddress() {
		return contractorAddress;
	}
	public void setContractorAddress(String contractorAddress) {
		this.contractorAddress = contractorAddress;
	}
	public String getContractorCity() {
		return contractorCity;
	}
	public void setContractorCity(String contractorCity) {
		this.contractorCity = contractorCity;
	}
	public String getContractorState() {
		return contractorState;
	}
	public void setContractorState(String contractorState) {
		this.contractorState = contractorState;
	}
	public String getContractorPinCode() {
		return contractorPinCode;
	}
	public void setContractorPinCode(String contractorPinCode) {
		this.contractorPinCode = contractorPinCode;
	}
	public String getContractorOperationalStatus() {
		return contractorOperationalStatus;
	}
	public void setContractorOperationalStatus(String contractorOperationalStatus) {
		this.contractorOperationalStatus = contractorOperationalStatus;
	}
}
