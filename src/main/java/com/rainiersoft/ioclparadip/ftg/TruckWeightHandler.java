package com.rainiersoft.ioclparadip.ftg;

import java.sql.Timestamp;
import java.util.HashMap;

import com.rainiersoft.core.Tag;
import com.rainiersoft.core.TagType;
import com.rainiersoft.util.PropertyLoader;
import com.rainiersoft.util.RSUtil;

import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.variant.Variant;

public class TruckWeightHandler
{
	 private String TRUCKWEIGHT_DATAREQ = "Channel1.Device1.Global"+ IOCLUtil.TRUCKWEIGHT_REQ;
	 
	 CompactLogixClient clc = null;
	 
	 public TruckWeightHandler ()
	 {
	 }
	 
	  
	    public float getTruckWeight ()
	    {
	    	Tag truckDataReqTag = getReadTag (TRUCKWEIGHT_DATAREQ);
	    	
	    	float truckWeight = 0.0f;
	    	boolean truckWeightReceived = false;
	    	
	    	do 
	    	{
	    		
		    	// get bay number tag details from plc
				OpcItem truckDataReqItem = clc.ReadTag(truckDataReqTag);
				
				if (null != truckDataReqItem && truckDataReqItem.getValue().getBoolean() == true)
				{
					
					Tag truckWeightTag = getReadTag (IOCLUtil.TRUCKWEIGHT_WEIGHT);
					
					OpcItem truckWeightItem = clc.ReadTag(truckWeightTag);	
					
					if (null != truckWeightItem )
					{
						truckWeight = truckWeightItem.getValue().getFloat();
						truckWeightReceived = true;
						resetBackTruckDataReq ();
						break;
					}
					
				}
				
				try
				{
					Thread.sleep(1000);
				} catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
	    	
	    	} while(!truckWeightReceived);
	    	
	    	return truckWeight;
	    	
	    }
	    
	    
	    private OpcItem resetBackTruckDataReq ()
		{
			// read pin code and bay number from plc.
			
			TagType tagType = new TagType((String)IOCLUtil.ioclTagMap.get(IOCLUtil.TRUCKWEIGHT_REQ));
			
		    OpcItem item = new OpcItem(IOCLUtil.TRUCKWEIGHT_REQ, true, "");
	 	    
			int variantType = RSUtil.getVariantType(tagType.getName());
			
			Variant varObj = RSUtil.getVariantValue(variantType,""+false);
			
			item.setValue(varObj);
		
			return item;
			
		}
	    private Tag getReadTag (String tagName)
		{
			TagType tagType = new TagType((String)IOCLUtil.ioclTagMap.get(tagName));
			Tag tag = new Tag(tagName, "", tagType, tagName); 
			
			return tag;
			
		}
 
	    public static void main (String args[])
	    {
	    	ProducerThread pthread = new ProducerThread ();
	    	pthread.start();
	    	
	    }

	}
