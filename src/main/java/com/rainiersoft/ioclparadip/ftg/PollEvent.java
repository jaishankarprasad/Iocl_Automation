package com.rainiersoft.ioclparadip.ftg;

import com.rainiersoft.core.Tag;

import javafish.clients.opc.component.OpcItem;

public class PollEvent extends Thread 
{
	  
	 private int pollInterval = 10;
	 private Tag monitorTag = null;
	 private CompactLogixClient plcClient = null;
	 private OpcItem responseItem = null;
	 private OpcItem currentItem = null;
	 private boolean currentStatus = false;
	 private boolean isFirstTimeRead = true;

	 IEventStatusChangedListener eventStatusChangedListener ;
	 
	  public PollEvent(Tag tag, int secs, IEventStatusChangedListener eventListner)
	  {
		 monitorTag = tag;
		 pollInterval = secs;
		 eventStatusChangedListener = eventListner;

		  plcClient = CompactLogixClient.getInstance();
		  plcClient.init();
	  }
	  
	  private boolean isChanged(OpcItem currentItem)
	  {
		 
		   if (null == currentItem || null == responseItem  )
			  return false;
		  
		  if (currentItem.getDataType() != responseItem.getDataType())
			  return false;
		  
		  int val = currentItem.getValue().compareTo(responseItem.getValue());
		  
		  if (val != 0)
		  {
			  responseItem = currentItem;
			  return true;
		  }
		  
		  return false; 
		  
		  //return true;
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
	    	
	    	currentItem = plcClient.ReadTag2(monitorTag);
	    //	currentItem = plcClient.populateItems();
	    		
	    		if (null != currentItem)
	    		{
	    			if (isFirstTimeRead == true)
	    			{
	    				responseItem = currentItem;
	    				isFirstTimeRead = false;
	    			}
	    			if (isChanged (currentItem))
	    			{
 	    				eventStatusChangedListener.StatusChanged();
 	    			}
	    		}
	    	}

	}
