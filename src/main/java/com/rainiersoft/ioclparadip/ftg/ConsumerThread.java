package com.rainiersoft.ioclparadip.ftg;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.rainiersoft.util.PropertyLoader;
import javafish.clients.opc.component.OpcItem;

public class ConsumerThread extends Thread 
{
	  
	 final static Logger logger = Logger.getLogger(ConsumerThread.class);
     private int pollInterval = 10;	 
	 private String propFile = "iocl.properties";
	 private String CONSUMER_POLL_INTERVAL = "CONSUMER_POLL_INTERVAL";
	 private String NO_BAYS_ACTIVE="NO_BAYS_ACTIVE";
	 private int noOfBays=1;
	 public HashMap<String, OpcItem> dbItemMap = new HashMap<>();
	 CompactLogixClient clc = null;
	 DBThread dbThread=null;
	 
	 public ConsumerThread ()
	 {
		 dbThread=new DBThread();
		 dbThread.start();
		 pollInterval = getPollInterval();
		clc = CompactLogixClient.getInstance();
		clc.init();
		populatedbItemMapFirstTime();
	 }
	 
	 private int getPollInterval ()
	 {
		 PropertyLoader prop = new PropertyLoader ();
		 prop.loadProperties(propFile);
		 pollInterval = Integer.parseInt(prop.getValue(CONSUMER_POLL_INTERVAL));
		 noOfBays = Integer.parseInt(prop.getValue(NO_BAYS_ACTIVE));
		 
		 return pollInterval;
	 }
	 
	  private String getTime ()
	  {
			Timestamp ts = new Timestamp (new java.util.Date().getTime());
			return ts.toString();
	  }
	  
	  private void populatedbItemMapFirstTime()
	  {
		  DBThread dbThread = new DBThread ();
		  dbItemMap = dbThread.readSnapshotFromDB();
	  }
	
	    public HashMap<String, OpcItem> getDbItemMap() {
		return dbItemMap;
	}

	public void setDbItemMap(HashMap<String, OpcItem> dbItemMap) {
		this.dbItemMap = dbItemMap;
	}
	
	public void updateDBItemMap(List<OpcItem> itemList)
	{
		OpcItem  items[]  = itemList.toArray(new OpcItem[itemList.size()]); 
		
	    logger.info("Updating DB Item Map in Consumer thread post PLC update");
	    for (int i=0; i< items.length; i++)
	    {
	     System.out.println("Item Name "+ items[i]);
	     dbItemMap.put(items[i].getItemName(), items[i]);
	    }

	}

		@Override
	    public void run()
	    {
	       
	        System.out.println("Initating Consumer THREAD ....- START "+Thread.currentThread().getName()+":"+getTime());
	        logger.info("Initating Consumer THREAD ....- START "+Thread.currentThread().getName()+":"+getTime());
	        
	        while(true) 
	        {
		        System.out.println("Scanning the Tag Informatoin from In memory ..- START "+Thread.currentThread().getName()+":"+getTime());
		        logger.info("Scanning the Tag Informatoin from In memory ..- START "+Thread.currentThread().getName()+":"+getTime());
		        try
		        {
		            
		            doProcessing();
		            Thread.sleep(pollInterval);
		           
		        }
		        catch (InterruptedException e)
		        {
		        	logger.error(e);
		            e.printStackTrace();
		        }
		        
		        System.out.println("Update the Inmemory Tag table ..- "+Thread.currentThread().getName()+":"+getTime());
		        logger.info("Update the Inmemory Tag table ..- "+Thread.currentThread().getName()+":"+getTime());
	        }
	    }

	    private  void  doProcessing() throws InterruptedException
	    {
	    	
	    	HashMap<String, OpcItem> plcItemMap = clc.getInMemoryTagTable() ; 
	    	setDbItemMap(plcItemMap);
	    	dbThread.setDbItemMap(plcItemMap);
	    	
	    	processPinCodeValidationEvent (plcItemMap);
	    	processBayStatusEvent (plcItemMap);
	    }
	    
	    private void processPinCodeValidationEvent (HashMap<String, OpcItem> plcMap)
	    {
	    	//for (int i=0; i< noOfBays; i++)
	       for (int i=1; i<= noOfBays; i++)
	    	{
	    		String tagName = clc.getCompteTagName (IOCLUtil.PINCODE_VALIDATION_REQ, i);
	    		OpcItem iPinCodeValidation = plcMap.get(tagName);
	    		OpcItem dBPinCodeValidation = dbItemMap.get(tagName);
	    		
	    		
	    		if (null != iPinCodeValidation)
	    		{
	    			// Pin Code Validation request arrived !!
	    			if (iPinCodeValidation.getValue().getBoolean() == true)
	    			{
	    				// Pin Code validation request arrived for this BAY.
	    		        System.out.println("Pin code validation requested from BAY["+i+"] on:"+getTime());
	    		        logger.info("Pin code validation requested from BAY["+i+"] on:"+getTime());
	    				PinCodeValidationEvent pinCodeHandler = new PinCodeValidationEvent (i);
	    				pinCodeHandler.StatusChanged();
	    		        System.out.println("Pin code validation processed for BAY["+i+"] on:"+getTime());
	    		        logger.info("Pin code validation processed for BAY["+i+"] on:"+getTime());
	    		        
	    		        updateDBItemMap (pinCodeHandler.getItemList());
	    			}
	    		}
	    	}
	    	
	    }

	    private void processBayStatusEvent (HashMap<String, OpcItem> plcMap)
	    {
	    	for (int i=1; i<= noOfBays; i++)
	    	{
	    		String tagName = clc.getCompteTagName (IOCLUtil.STATUS_CHANGE, i);
	    		OpcItem iBayStatus = plcMap.get(tagName);
	    		OpcItem dBBayStatus = dbItemMap.get(tagName);
	    		
	    		if (null != iBayStatus)
	    		{
	    			if (iBayStatus.getValue().getWord() != dBBayStatus.getValue().getWord())
	    			{
	    				// Bays status got changed.
	    		        System.out.println("Bay Status changed for BAY["+i+"] on:"+getTime());
	    		        logger.info("Bay Status changed for BAY["+i+"] on:"+getTime());
	    				BayStatusChangeEvent bayStatusChangeHandler = new BayStatusChangeEvent (i);
	    				bayStatusChangeHandler.StatusChanged();
	    		        System.out.println("Bay Status even processed for BAY["+i+"] on:"+getTime());
	    		        logger.info("Bay Status even processed for BAY["+i+"] on:"+getTime());
	    		        
	    		        updateDBItemMap (bayStatusChangeHandler.getItemList());

	    			}
	    		}
	    	}
	    	
	    }

	    public static void main (String args[])
	    {
	    	ConsumerThread pthread = new ConsumerThread ();
	    	pthread.start();
	    	
	    }

	}
