package com.rainiersoft.ioclparadip.ftg;

public class TruckWeight
{

	public enum REQUEST 
	{
		FALSE (0),
		TRUE (1);
		
		private int value;
	    
	    private REQUEST(int value) 
	    {
	        this.value = value;
	    }
	    
	    public int getValue() 
	    {
	        return value;
	    }
	}

	private int bayNumber;
	private boolean truckWeight_Req;
	private float truckWeight;
	
	public boolean isTruckWeight_Req() {
		return truckWeight_Req;
	}
	public void setTruckWeight_Req(boolean truckWeight_Req) {
		this.truckWeight_Req = truckWeight_Req;
	}
	public float getTruckWeight() {
		return truckWeight;
	}
	public void setTruckWeight(float truckWeight) {
		this.truckWeight = truckWeight;
	}

	public int getBayNumber() {
		return bayNumber;
	}
	public void setBayNumber(int bayNumber) {
		this.bayNumber = bayNumber;
	}

}
