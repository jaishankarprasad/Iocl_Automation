package com.rainiersoft.ioclparadip.igs;

import com.rainiersoft.core.Tag;

import javafish.clients.opc.component.OpcItem;

public class PollEvent extends Thread 
{
	  
	 private int pollInterval = 10;
	 private Tag monitorTag = null;
	 private CompactLogixClient plcClient = null;
	 private OpcItem responseItem = null;
	 private boolean currentStatus = false;

	 IEventStatusChangedListener eventStatusChangedListener ;
	 
	  public PollEvent(Tag tag, int secs, IEventStatusChangedListener eventListner)
	  {
		 monitorTag = tag;
		 pollInterval = secs;
		 eventStatusChangedListener = eventListner;

		  plcClient = CompactLogixClient.getInstance();
		  plcClient.init();
	  }
	  
	
	    @Override
	    public void run()
	    {
	       
	        
	        while(true) 
	        {
	        System.out.println("Initating refreshing ..- START "+Thread.currentThread().getName());
	        try
	        {
	            Thread.sleep(pollInterval);
	            doProcessing();
	           
	        }
	        catch (InterruptedException e)
	        {
	            e.printStackTrace();
	        }
	        System.out.println("Doing heavy processing - END "+Thread.currentThread().getName());
	     }
	    }

	    private void doProcessing() throws InterruptedException
	    {
	    	
	    		responseItem = plcClient.ReadTag(monitorTag);
	    		
	    		if (null != responseItem)
	    		{
	    			if (responseItem.getValue().getBoolean() != currentStatus)
	    			{
	    				currentStatus = responseItem.getValue().getBoolean();
	    				
 	    				eventStatusChangedListener.StatusChanged();
 	    			}
	    		}
	    	}

	}
