package com.rainiersoft.ioclparadip.ftg;

import com.rainiersoft.core.Tag;
import com.rainiersoft.core.TagType;

import javafish.clients.opc.component.OpcItem;

public class TruckWeightEvent implements IEventStatusChangedListener
{

 	public final String bayPrefix ="Bay";
	
	private int bayNo = 1;
 
	TruckWeight truckWeight = null;
	CompactLogixClient clc = null;
	
	
	public TruckWeightEvent (int bayNo)
	{
		clc = CompactLogixClient.getInstance();
		clc.init();
		this.bayNo = bayNo;
	}
	
	
	public void StatusChanged()
	{
		// write the business logic for PinCode ValidationEvent change.
		
		if ( checkTruckWeightReqChange (this.bayNo) == true)
		{
			// Populate PinCode Info Object from iocl db
			// TBD
			writeTruckWeightDetails(truckWeight);
 		}
	}

	private void writeTruckWeightDetails(TruckWeight truckWeight)
	{
		// write back the Truck details to IOCL db.

		 return;
	}
	
	private Tag getReadTag (String ioclTagName, int bayNumber)
	{
		String tagPrefix ="[TrialTopic]"+bayPrefix+ String.format("%02d", bayNumber)+ "_";
		// read pin code and bay number from plc.
		
		String tagName = tagPrefix + ioclTagName;
		TagType tagType = new TagType((String)IOCLUtil.ioclTagMap.get(ioclTagName));
		Tag tag = new Tag(tagName, "", tagType, tagName); 
		
		return tag;
		
	}

 
	// Upon successfull pin code validation request, read pin and bay and authenticate it 
	public boolean checkTruckWeightReqChange (int bayNumber )
	{
		Tag truckWeightTag = getReadTag (IOCLUtil.TRUCKWEIGHT_WEIGHT, bayNumber);
 		
		// get truck weight from plc
		OpcItem truckWeightTagItem  = clc.ReadTag(truckWeightTag);
 		
		// Check if any business logic need to be implimented after getting truck weight 
		// TBD
		
		// Write the truck weight back to IOCL db.

		truckWeight = new TruckWeight ();
		truckWeight.setBayNumber(bayNumber);
		truckWeight.setTruckWeight(truckWeightTagItem.getValue().getFloat());
		return false;
	}

 
}
