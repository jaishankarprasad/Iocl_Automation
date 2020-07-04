package com.rainiersoft.response.dto;

import java.util.Date;

public class FanPinCreatedResponse 
{
	private Long fanId;
	private String truckNumber;
	private String driverName;
	private String fanPin;
	private String customer;
	private int bayNum;
	//private String bayStatus;
	private String quantity;
	private int quantityID;
	private String vehicleWeight;
	private String destination;
	private String locationCode;
	private String contractorName;
	private String fanPinStatus;
	private String fanPinCreation;
	private String fanPinExpiration;

	public FanPinCreatedResponse()
	{

	}
	
	public int getQuantityID() {
		return quantityID;
	}

	public void setQuantityID(int quantityID) {
		this.quantityID = quantityID;
	}

	public Long getFanId() {
		return fanId;
	}

	public void setFanId(Long fanId) {
		this.fanId = fanId;
	}

	public String getTruckNumber() {
		return truckNumber;
	}

	public void setTruckNumber(String truckNumber) {
		this.truckNumber = truckNumber;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getFanPin() {
		return fanPin;
	}

	public void setFanPin(String fanPin) {
		this.fanPin = fanPin;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public int getBayNum() {
		return bayNum;
	}

	public void setBayNum(int bayNum) {
		this.bayNum = bayNum;
	}

	/*public String getBayStatus() {
		return bayStatus;
	}

	public void setBayStatus(String bayStatus) {
		this.bayStatus = bayStatus;
	}*/

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
