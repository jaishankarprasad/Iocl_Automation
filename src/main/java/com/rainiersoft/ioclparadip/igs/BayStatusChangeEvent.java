package com.rainiersoft.ioclparadip.igs;

import com.rainiersoft.core.Tag;
import com.rainiersoft.core.TagType;
import com.rainiersoft.util.RSUtil;

import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.variant.Variant;

public class BayStatusChangeEvent implements IEventStatusChangedListener
{

	public final String bayPrefix ="Bay";
	
	private int bayNo = 1;
 
	BayStatus bayStatus = null;
	CompactLogixClient clc = null;
	
	
	public BayStatusChangeEvent (int bayNo)
	{
		clc = CompactLogixClient.getInstance();
		clc.init();
		this.bayNo = bayNo;
	}
	
	
	public void StatusChanged()
	{
		// write the business logic for PinCode ValidationEvent change.
		
		if ( checkBayStatusChange (this.bayNo) == true)
		{
			// Populate PinCode Info Object from iocl db
			// TBD
			writeStatusDetails (bayStatus);
 		}
	}

	private void writeStatusDetails(BayStatus bayStatus)
	{
		// write back the Truck details to IOCL db.
		
		return ;
	}
	
	private Tag getReadTag (String ioclTagName, int bayNumber)
	{
		String tagPrefix = bayPrefix+ String.format("%02d", bayNumber)+ "_";
		// read pin code and bay number from plc.
		
		String tagName = tagPrefix + ioclTagName;
		TagType tagType = new TagType((String)IOCLUtil.ioclTagMap.get(ioclTagName));
		Tag tag = new Tag(tagName, "", tagType, tagName); 
		
		return tag;
		
	}

 
	// Upon successfull pin code validation request, read pin and bay and authenticate it 
	public boolean checkBayStatusChange (int bayNumber )
	{
		Tag statusBayNumber = getReadTag (IOCLUtil.STATUS_BAYNUMBER, bayNumber);
		Tag statusPinCode  =  getReadTag (IOCLUtil.STATUS_PINCODE, bayNumber);
		Tag statusStatus  =  getReadTag (IOCLUtil.STATUS_STATUS, bayNumber);
		
		
		// get bay number status plc
		OpcItem statusBayNumberItem = clc.ReadTag(statusBayNumber);

		// get bay bay status pin code from plc
		OpcItem statusPinCodeItem = clc.ReadTag(statusPinCode);
		
		// get bay status from plc;
		OpcItem statusStatusItem = clc.ReadTag(statusStatus);
		
		// Check if pin code retrieved from plc matches with that of IOCL DB.
		//TBD
		
		bayStatus = new BayStatus ();
		bayStatus.setBayNumber(bayNumber);
		bayStatus.setPinCode(statusPinCodeItem.getValue().getInteger());
		bayStatus.setStatus_change(statusStatusItem.getValue().getInteger());

		return false;
	}

 
}
