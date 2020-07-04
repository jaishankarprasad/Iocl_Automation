package com.rainiersoft.response.dto;

public class AutofillDetailsResponse 
{
	private String truckNo;
	//private String permitNo;
	private String permitExpDate;
	private String insuranceExpDate;
	private String trukCapacity;
	private String locationName;
	private String transporterName;
	private String poesVal;
	//private String InsuranceNo;
	
	public String getPoesVal() {
		return poesVal;
	}
	public void setPoesVal(String poesVal) {
		this.poesVal = poesVal;
	}
	public String getTrukCapacity() {
		return trukCapacity;
	}
	public void setTrukCapacity(String trukCapacity) {
		this.trukCapacity = trukCapacity;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getTransporterName() {
		return transporterName;
	}
	public void setTransporterName(String transporterName) {
		this.transporterName = transporterName;
	}
	public String getInsuranceExpDate() {
		return insuranceExpDate;
	}
	public void setInsuranceExpDate(String insuranceExpDate) {
		this.insuranceExpDate = insuranceExpDate;
	}
	public String getTruckNo() {
		return truckNo;
	}
	public void setTruckNo(String truckNo) {
		this.truckNo = truckNo;
	}
	/*public String getPermitNo() {
		return permitNo;
	}
	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}*/
	public String getPermitExpDate() {
		return permitExpDate;
	}
	public void setPermitExpDate(String permitExpDate) {
		this.permitExpDate = permitExpDate;
	}
	/*public String getInsuranceNo() {
		return InsuranceNo;
	}
	public void setInsuranceNo(String insuranceNo) {
		InsuranceNo = insuranceNo;
	}*/
}
