package com.rainiersoft.response.dto;

public class GetAllLatestFanSlipsDataResponseBean 
{
	@Override
	public String toString() {
		return "GetAllLatestFanSlipsDataResponseBean [fanId=" + fanId + ", truckNumber=" + truckNumber + ", driverName="
				+ driverName + ", fanPin=" + fanPin + ", customer=" + customer + ", bayNum=" + bayNum + ", bayStatus="
				+ bayStatus + ", quantity=" + quantity + ", vehicleWeight=" + vehicleWeight + ", destination="
				+ destination + ", locationCode=" + locationCode + ", contractorName=" + contractorName
				+ ", fanPinStatus=" + fanPinStatus + ", fanPinCreation=" + fanPinCreation + ", fanPinExpiration="
				+ fanPinExpiration + "]";
	}
	private int fanId;
	private String truckNumber;
	private String driverName;
	private String driverLicenceNumber;
	private String fanPin;
	private String customer;
	private int bayNum;
	private String bayStatus;
	private String quantity;
	private String preSet;
	private String vehicleWeight;
	private String destination;
	private String locationCode;
	private String contractorName;
	private String fanPinStatus;
	private String fanPinCreation;
	private String fanPinExpiration;
	private int quantityID;
	private boolean isFanStatusExpired;
	//private String PermitNo;
	private String PermitExpDate;
	private String InsuranceExpDate;
	//private String InsuranceNo;
	private String comments;
	private String loadedQuantity;
	private String batchStartTime;
	private String batchEndTime;
	private String poesVal;

	public String getPoesVal() {
		return poesVal;
	}
	public void setPoesVal(String poesVal) {
		this.poesVal = poesVal;
	}
	public String getInsuranceExpDate() {
		return InsuranceExpDate;
	}
	public void setInsuranceExpDate(String insuranceExpDate) {
		InsuranceExpDate = insuranceExpDate;
	}
	/*public String getPermitNo() {
		return PermitNo;
	}
	public void setPermitNo(String permitNo) {
		PermitNo = permitNo;
	}*/
	public String getPermitExpDate() {
		return PermitExpDate;
	}
	public void setPermitExpDate(String permitExpDate) {
		PermitExpDate = permitExpDate;
	}
	/*public String getInsuranceNo() {
		return InsuranceNo;
	}
	public void setInsuranceNo(String insuranceNo) {
		InsuranceNo = insuranceNo;
	}*/
	public String getLoadedQuantity() {
		return loadedQuantity;
	}
	public void setLoadedQuantity(String loadedQuantity) {
		this.loadedQuantity = loadedQuantity;
	}
	public String getBatchStartTime() {
		return batchStartTime;
	}
	public void setBatchStartTime(String batchStartTime) {
		this.batchStartTime = batchStartTime;
	}
	public String getBatchEndTime() {
		return batchEndTime;
	}
	public void setBatchEndTime(String batchEndTime) {
		this.batchEndTime = batchEndTime;
	}
	public boolean isFanStatusExpired() {
		return isFanStatusExpired;
	}
	public void setFanStatusExpired(boolean isFanStatusExpired) {
		this.isFanStatusExpired = isFanStatusExpired;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getPreSet() {
		return preSet;
	}
	public void setPreSet(String preSet) {
		this.preSet = preSet;
	}
	public int getQuantityID() {
		return quantityID;
	}
	public void setQuantityID(int quantityID) {
		this.quantityID = quantityID;
	}

	public int getFanId() {
		return fanId;
	}
	public void setFanId(int fanId) {
		this.fanId = fanId;
	}
	public String getTruckNumber() {
		return truckNumber;
	}
	public String getDriverName() {
		return driverName;
	}
	public String getDriverLicenceNumber() {
		return driverLicenceNumber;
	}
	public void setDriverLicenceNumber(String driverLicenceNumber) {
		this.driverLicenceNumber = driverLicenceNumber;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getBayStatus() {
		return bayStatus;
	}
	public void setBayStatus(String bayStatus) {
		this.bayStatus = bayStatus;
	}
	public void setTruckNumber(String truckNumber) {
		this.truckNumber = truckNumber;
	}
	public String getFanPin() {
		return fanPin;
	}
	public void setFanPin(String fanPin) {
		this.fanPin = fanPin;
	}
	public int getBayNum() {
		return bayNum;
	}
	public void setBayNum(int bayNum) {
		this.bayNum = bayNum;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getVehicleWeight() {
		return vehicleWeight;
	}
	public void setVehicleWeight(String vehicleWeight) {
		this.vehicleWeight = vehicleWeight;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public String getContractorName() {
		return contractorName;
	}
	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}
	public String getFanPinStatus() {
		return fanPinStatus;
	}
	public void setFanPinStatus(String fanPinStatus) {
		this.fanPinStatus = fanPinStatus;
	}
	public String getFanPinCreation() {
		return fanPinCreation;
	}
	public void setFanPinCreation(String fanPinCreation) {
		this.fanPinCreation = fanPinCreation;
	}
	public String getFanPinExpiration() {
		return fanPinExpiration;
	}
	public void setFanPinExpiration(String fanPinExpiration) {
		this.fanPinExpiration = fanPinExpiration;
	}
}