package com.rainiersoft.ioclparadip.ftg;

public enum TruckStatus  
{
	TruckOnBay(1),
	LoadingInProgress (2),
	LoadingCompleted (3),
	LoadingAborted (4);
	
	
	private int value;
    
    private TruckStatus(int value) 
    {
        this.value = value;
    }
    
    public int getValue() 
    {
        return value;
    }
}