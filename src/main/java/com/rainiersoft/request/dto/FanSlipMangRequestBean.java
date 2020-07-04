package com.rainiersoft.request.dto;

public class FanSlipMangRequestBean 
{
	private String TruckNo;
	private String FanCreatedBy;
	private String FanUpdatedBy;
	private String DriverName;
	private String ContractorName;
	private String DriverLicNo;
	private String Customer;
	private String Quantity;
	private int QuantityID;
	private String VehicleWgt;
	private String Destination;
	private String LocationCode;
	private String MobileNumber;
	private int BayNum;
	private int FanId;
	private String remainingQtyLeft;
	//private String PermitNo;
	private String PermitExpDate;
	private String InsuranceExpDate;
	private boolean PermitExpModifiedFlag;
	private boolean InsuranceExpModifiedFlag;
	private String fanPin;
	//private String InsuranceNo;
	private String comments;
	private boolean updateBayNumFlag;
	private String pesoVal;

	public String getPesoVal() {
		return pesoVal;
	}
	public void setPesoVal(String pesoVal) {
		this.pesoVal = pesoVal;
	}
	public boolean getUpdateBayNumFlag() {
		return updateBayNumFlag;
	}
	public void setUpdateBayNumFlag(boolean updateBayNumFlag) {
		this.updateBayNumFlag = updateBayNumFlag;
	}
	public String getFanPin() {
		return fanPin;
	}
	public void setFanPin(String fanPin) {
		this.fanPin = fanPin;
	}
	public boolean isPermitExpModifiedFlag() {
		return PermitExpModifiedFlag;
	}
	public void setPermitExpModifiedFlag(boolean permitExpModifiedFlag) {
		PermitExpModifiedFlag = permitExpModifiedFlag;
	}
	public boolean isInsuranceExpModifiedFlag() {
		return InsuranceExpModifiedFlag;
	}
	public void setInsuranceExpModifiedFlag(boolean insuranceExpModifiedFlag) {
		InsuranceExpModifiedFlag = insuranceExpModifiedFlag;
	}
	public String getRemainingQtyLeft() {
		return remainingQtyLeft;
	}
	public void setRemainingQtyLeft(String remainingQtyLeft) {
		this.remainingQtyLeft = remainingQtyLeft;
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
	public String getFanUpdatedBy() {
		return FanUpdatedBy;
	}
	public void setFanUpdatedBy(String fanUpdatedBy) {
		FanUpdatedBy = fanUpdatedBy;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public int getQuantityID() {
		return QuantityID;
	}
	public void setQuantityID(int quantityID) {
		QuantityID = quantityID;
	}

	public int getFanId() {
		return FanId;
	}
	public void setFanId(int fanId) {
		FanId = fanId;
	}

	public String getFanCreatedBy() {
		return FanCreatedBy;
	}
	public void setFanCreatedBy(String fanCreatedBy) {
		FanCreatedBy = fanCreatedBy;
	}

	public String getContractorName() {
		return ContractorName;
	}
	public void setContractorName(String contractorName) {
		ContractorName = contractorName;
	}

	public String getMobileNumber() {
		return MobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		MobileNumber = mobileNumber;
	}

	public String getTruckNo() {
		return TruckNo;
	}
	public void setTruckNo(String truckNo) {
		TruckNo = truckNo;
	}
	public String getDriverName() {
		return DriverName;
	}
	public void setDriverName(String driverName) {
		DriverName = driverName;
	}
	public String getDriverLicNo() {
		return DriverLicNo;
	}
	public void setDriverLicNo(String driverLicNo) {
		DriverLicNo = driverLicNo;
	}
	public String getCustomer() {
		return Customer;
	}
	public void setCustomer(String customer) {
		Customer = customer;
	}
	public String getQuantity() {
		return Quantity;
	}
	public void setQuantity(String quantity) {
		Quantity = quantity;
	}
	public String getVehicleWgt() {
		return VehicleWgt;
	}
	public void setVehicleWgt(String vehicleWgt) {
		VehicleWgt = vehicleWgt;
	}
	public String getDestination() {
		return Destination;
	}
	public void setDestination(String destination) {
		Destination = destination;
	}
	public String getLocationCode() {
		return LocationCode;
	}
	public void setLocationCode(String locationCode) {
		LocationCode = locationCode;
	}
	public int getBayNum() {
		return BayNum;
	}
	public void setBayNum(int bayNum) {
		BayNum = bayNum;
	}
}
