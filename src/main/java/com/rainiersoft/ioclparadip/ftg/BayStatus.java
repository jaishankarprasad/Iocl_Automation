package com.rainiersoft.ioclparadip.ftg;

public class BayStatus
{
	public static final int BAY_STATUS_EMPTY= 0;
	public static final int BAY_STATUS_STARTED= 1;
	public static final int BAY_STATUS_STOPPED= 2;
	public static final int BAY_STATUS_PAUSED= 3;
	public static final int BAY_STATUS_RESUMED=4;
	
	
	private int status_change;
	private int bayNumber;
	private int pinCode;
	private int status;
	private long fanNo;
	private int truckStatus;
	
	public int getTruckStatus() {
		return truckStatus;
	}
	public void setTruckStatus(int truckStatus) {
		this.truckStatus = truckStatus;
	}
	public long getFanNo() {
		return fanNo;
	}
	public void setFanNo(long fanNo) {
		this.fanNo = fanNo;
	}
	public int getStatus_change() {
		return status_change;
	}
	public void setStatus_change(int status_change) {
		this.status_change = status_change;
	}
	public int getBayNumber() {
		return bayNumber;
	}
	public void setBayNumber(int bayNumber) {
		this.bayNumber = bayNumber;
	}
	public int getPinCode() {
		return pinCode;
	}
	public void setPinCode(int pinCode) {
		this.pinCode = pinCode;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public enum Status 
	{
		BAY_EMTPTY (0),
		BAY_OCCUPIED (1);
		
		private int value;
	    
	    private Status(int value) 
	    {
	        this.value = value;
	    }
	    
	    public int getValue() 
	    {
	        return value;
	    }
	}
	
	public enum Change 
	{
		FALSE (0),
		TRUE (1);
		
		private int value;
	    
	    private Change(int value) 
	    {
	        this.value = value;
	    }
	    
	    public int getValue() 
	    {
	        return value;
	    }
	}
	
	
	
 
}
