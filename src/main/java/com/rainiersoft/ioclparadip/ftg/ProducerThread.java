package com.rainiersoft.ioclparadip.ftg;

import java.sql.Timestamp;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.rainiersoft.util.PropertyLoader;
import javafish.clients.opc.component.OpcItem;

public class ProducerThread extends Thread 
{
	final static Logger logger = Logger.getLogger(ProducerThread.class);
	 private int pollInterval = 10;	 
	 private String propFile = "iocl.properties";
	 private String PRODUCER_POLL_INTERVAL = "PRODUCER_POLL_INTERVAL";
	 public HashMap<String, OpcItem> plcItemMap = new HashMap<>();
	 
	 CompactLogixClient clc = null;
	 
	 public ProducerThread ()
	 {
		 pollInterval = getPollInterval();
		clc = CompactLogixClient.getInstance();
		clc.init();

	 }
	 
	 private int getPollInterval ()
	 {
		 PropertyLoader prop = new PropertyLoader ();
		 prop.loadProperties(propFile);
		 pollInterval = Integer.parseInt(prop.getValue(PRODUCER_POLL_INTERVAL));		 
		 return pollInterval;
	 }
	 
	  private String getTime ()
	  {
			Timestamp ts = new Timestamp (new java.util.Date().getTime());
			return ts.toString();
	  }
	
	    @Override
	    public void run()
	    {
	       
//	        System.out.println("Initating Producer THREAD ....- START "+Thread.currentThread().getName()+":"+getTime());
//	        logger.info("Initating Producer THREAD ....- START "+Thread.currentThread().getName()+":"+getTime());
	        while(true) 
	        {
//		        System.out.println("Retreiving the Tag Informatoin from PLC ..- START "+Thread.currentThread().getName()+":"+getTime());
//		        logger.info("Retreiving the Tag Informatoin from PLC ..- START "+Thread.currentThread().getName()+":"+getTime());
		        try
		        {
		            
		            doProcessing();
		            synchronized (this)
		            {
		            	clc.setInMemoryTagTable(plcItemMap);
		            }
		            Thread.sleep(pollInterval);
		           
		        }
		        catch (InterruptedException e)
		        {
		        	logger.error(e);
		            e.printStackTrace();
		        }
		        
//		        System.out.println("Update the Inmemory Tag table ..- "+Thread.currentThread().getName()+":"+getTime());
//		        logger.info("Update the Inmemory Tag table ..- "+Thread.currentThread().getName()+":"+getTime());
	        }
	    }

	    private  void  doProcessing() throws InterruptedException
	    {
	    	
	    	OpcItem [] responseItemList = clc.retreiveAllItems ();
	    	
	    	if (null != responseItemList && responseItemList.length > 0)
	    	{
	    		for (int i =0; i < responseItemList.length; i++)
				{
	    			
	    			if (responseItemList[i].isQuality() == true)
	    			{
	    				
	    				plcItemMap.put(responseItemList[i].getItemName(), responseItemList[i]);
	    			}
					 
				}
	    		
	    	}
	    }
	    
	    public HashMap<String, OpcItem> getPlcItemMap() {
			return plcItemMap;
		}

		public void setPlcItemMap(HashMap<String, OpcItem> plcItemMap) {
			this.plcItemMap = plcItemMap;
		}

		public static void main (String args[])
	    {
	    	ProducerThread pthread = new ProducerThread ();
	    	pthread.start();
	    	
	    }

	}
