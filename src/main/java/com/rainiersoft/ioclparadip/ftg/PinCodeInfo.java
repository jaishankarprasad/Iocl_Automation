package com.rainiersoft.ioclparadip.ftg;

import java.sql.Timestamp;

public class PinCodeInfo 
{
	    int bayNumber;
	    
	boolean pinCodeValidation_Req;
	    int pinCodeValidation_BayNumber;
	    int pinCodeValidation_PinCode;
	    
	    int pinCodeInfo_PinCodStatus;
	   long pinCodeInfo_FanNo;
	    int pinCodeInfo_PinCode;
	   long pinCodeInfo_PresetValue;
	 String pinCodeInfo_TruckNo;
	 String pinCodeInfo_TransporterName;
	 String pinCodeInfo_Location;
	  float pinCodeInfo_Capacity;
	  float pinCodeInfo_Tare_Weight;
	   int pinCodeInfo_TruckStatus;	
	   int pinCodeInfo_TruckNo_Len;
	   int pinCodeInfo_TransporterName_Len;
	   int pinCodeInfo_Location_Len;
	
  Timestamp FanCreationOn;
  Timestamp FanExpirationOn;

	public Timestamp getFanCreationOn() {
		return FanCreationOn;
	}
	public void setFanCreationOn(Timestamp fanCreationOn) {
		FanCreationOn = fanCreationOn;
	}
	public Timestamp getFanExpirationOn() {
		return FanExpirationOn;
	}
	public void setFanExpirationOn(Timestamp fanExpirationOn) {
		FanExpirationOn = fanExpirationOn;
	}
	public long getPinCodeInfo_TruckNo_Len() {
		return pinCodeInfo_TruckNo_Len;
	}
	public void setPinCodeInfo_TruckNo_Len(int pinCodeInfo_TruckNo_Len) {
		this.pinCodeInfo_TruckNo_Len = pinCodeInfo_TruckNo_Len;
	}
	public long getPinCodeInfo_TransporterName_Len() {
		return pinCodeInfo_TransporterName_Len;
	}
	public void setPinCodeInfo_TransporterName_Len(int pinCodeInfo_TransporterName_Len) {
		this.pinCodeInfo_TransporterName_Len = pinCodeInfo_TransporterName_Len;
	}
	public long getPinCodeInfo_Location_Len() {
		return pinCodeInfo_Location_Len;
	}
	public void setPinCodeInfo_Location_Len(int pinCodeInfo_Location_Len) {
		this.pinCodeInfo_Location_Len = pinCodeInfo_Location_Len;
	}
	public int getBayNumber() {
		return bayNumber;
	}
	public void setBayNumber(int bayNumber) {
		this.bayNumber = bayNumber;
	}
	public boolean isPinCodeValidation_Req() {
		return pinCodeValidation_Req;
	}
	public void setPinCodeValidation_Req(boolean pinCodeValidation_Req) {
		this.pinCodeValidation_Req = pinCodeValidation_Req;
	}
	public int getPinCodeValidation_BayNumber() {
		return pinCodeValidation_BayNumber;
	}
	public void setPinCodeValidation_BayNumber(int pinCodeValidation_BayNumber) {
		this.pinCodeValidation_BayNumber = pinCodeValidation_BayNumber;
	}
	public int getPinCodeValidation_PinCode() {
		return pinCodeValidation_PinCode;
	}
	public void setPinCodeValidation_PinCode(int pinCodeValidation_PinCode) {
		this.pinCodeValidation_PinCode = pinCodeValidation_PinCode;
	}
	public int getPinCodeInfo_PinCodStatus() {
		return pinCodeInfo_PinCodStatus;
	}
	public void setPinCodeInfo_PinCodStatus(int pinCodeInfo_PinCodStatus) {
		this.pinCodeInfo_PinCodStatus = pinCodeInfo_PinCodStatus;
	}
	public long getPinCodeInfo_FanNo() {
		return pinCodeInfo_FanNo;
	}
	public void setPinCodeInfo_FanNo(long pinCodeInfo_FanNo) {
		this.pinCodeInfo_FanNo = pinCodeInfo_FanNo;
	}
	public int getPinCodeInfo_PinCode() {
		return pinCodeInfo_PinCode;
	}
	public void setPinCodeInfo_PinCode(int pinCodeInfo_PinCode) {
		this.pinCodeInfo_PinCode = pinCodeInfo_PinCode;
	}
	public long getPinCodeInfo_PresetValue() {
		return pinCodeInfo_PresetValue;
	}
	public void setPinCodeInfo_PresetValue(long pinCodeInfo_PresetValue) {
		this.pinCodeInfo_PresetValue = pinCodeInfo_PresetValue;
	}
	public String getPinCodeInfo_TruckNo() {
		return pinCodeInfo_TruckNo;
	}
	public void setPinCodeInfo_TruckNo(String pinCodeInfo_TruckNo) {
		this.pinCodeInfo_TruckNo = pinCodeInfo_TruckNo;
	}
	public String getPinCodeInfo_TransporterName() {
		return pinCodeInfo_TransporterName;
	}
	public void setPinCodeInfo_TransporterName(String pinCodeInfo_TransporterName) {
		this.pinCodeInfo_TransporterName = pinCodeInfo_TransporterName;
	}
	public String getPinCodeInfo_Location() {
		return pinCodeInfo_Location;
	}
	public void setPinCodeInfo_Location(String pinCodeInfo_Location) {
		this.pinCodeInfo_Location = pinCodeInfo_Location;
	}
	public float getPinCodeInfo_Capacity() {
		return pinCodeInfo_Capacity;
	}
	public void setPinCodeInfo_Capacity(float pinCodeInfo_Capacity) {
		this.pinCodeInfo_Capacity = pinCodeInfo_Capacity;
	}
	public float getPinCodeInfo_Tare_Weight() {
		return pinCodeInfo_Tare_Weight;
	}
	public void setPinCodeInfo_Tare_Weight(float pinCodeInfo_Tare_Weight) {
		this.pinCodeInfo_Tare_Weight = pinCodeInfo_Tare_Weight;
	}
	public int getPinCodeInfo_TruckStatus() {
		return pinCodeInfo_TruckStatus;
	}
	public void setPinCodeInfo_TruckStatus(int pinCodeInfo_TruckStatus) {
		this.pinCodeInfo_TruckStatus = pinCodeInfo_TruckStatus;
	}

}
