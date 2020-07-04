package com.rainiersoft.ioclparadip.ftg;

import java.sql.Timestamp;
import java.util.ArrayList;

import java.util.List;

import org.apache.log4j.Logger;

import com.rainiersoft.core.Tag;
import com.rainiersoft.core.TagType;
import com.rainiersoft.util.RSUtil;

import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.variant.Variant;

public class BayStatusChangeEvent implements IEventStatusChangedListener
{
final static Logger logger=Logger.getLogger(BayStatusChangeEvent.class);
	public final String PINCODE_VALIDATION_REQUEST= "Status.Change";
	
	public final String bayPrefix ="Bay";
	private int bayNo = 1;
	
 
	PinCodeInfo pinCodeInfo = null;
	CompactLogixClient clc = null;
	IOCLDButil ioclUtil = null;
	
	public List<OpcItem> itemList = null;

	public List<OpcItem> getItemList() {
		return itemList;
	}


	public void setItemList(List<OpcItem> itemList) {
		this.itemList = itemList;
	}


	public BayStatusChangeEvent (int bayNo)
	{
		clc = CompactLogixClient.getInstance();
		clc.init();
		this.bayNo = bayNo;
		ioclUtil = new IOCLDButil ();
	}
	
	
	public void StatusChanged()
	{
		// write the business logic for PinCode ValidationEvent change.
		
		if ( validateBayInfo (this.bayNo) == true)
		{
			logger.info(" Valid pin, Writing back to PLC with details");
		  System.out.print(" Valid pin, Writing back to PLC with details");
		}
		else
		{
			logger.info(" Invalid pin, Writing back to PLC with details");
		  System.out.print(" Invalid Valid pin, Writing back to PLC with details");
		}
	}

	private PinCodeInfo getPinCodeDetails(int fanPINNumber, int bayNum)
	{
		// getPinCodeObject filled with Pin Code details.		
		return ioclUtil.callPinCodeValidation(fanPINNumber, bayNum);
		

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

	private OpcItem getWriteItem (String ioclTagName, int bayNumber, Object value)
	{
		String tagPrefix ="[TrialTopic]"+bayPrefix+ String.format("%02d", bayNumber)+ "_";
		// read pin code and bay number from plc.
		
		String tagName = tagPrefix + ioclTagName;
		TagType tagType = new TagType((String)IOCLUtil.ioclTagMap.get(ioclTagName));
		
	    OpcItem item = new OpcItem(tagName, true, "");
 	    
		int variantType = RSUtil.getVariantType(tagType.getName());
		
		Variant varObj = RSUtil.getVariantValue(variantType,""+value);
		
		item.setValue(varObj);
	
		return item;
		
	}
	
	private void BayStatusUpdate(BayStatus bayStatus)
	{
		boolean r;
		IOCLDButil util = new IOCLDButil ();
		java.util.Date nowDate = new java.util.Date ();
		java.sql.Timestamp nowTime = new java.sql.Timestamp(nowDate.getTime());
		
		boolean isFanPinStatusUpdationSuccess = util.fanPinStatusUpdation (bayStatus.getFanNo(), bayStatus.getPinCode(), bayStatus.getBayNumber() , ""+bayStatus.getStatus(),nowTime );
		boolean isBayStatusUpdationSuccess = util.bayStatusUpdation (bayStatus.getFanNo(), bayStatus.getPinCode(), bayStatus.getBayNumber(),  ""+bayStatus.getStatus(), nowTime,0,0,0);
		//need to update TRUCK status also in DB
	}

	// Upon successfull pin code validation request, read pin and bay and authenticate it 
	public boolean validateBayInfo(int bayNumber )
	{
		
		itemList = new ArrayList<OpcItem>();
		
		Tag fanNoTag = getReadTag (IOCLUtil.PINCODE_FANNO, bayNumber);
		Tag bayNumberTag = getReadTag (IOCLUtil.STATUS_BAYNUMBER, bayNumber);
		Tag pinCodeTag  =  getReadTag (IOCLUtil.STATUS_PINCODE, bayNumber);
		Tag bayStatusTag  =  getReadTag (IOCLUtil.STATUS_STATUS, bayNumber);
		
		Tag truckStatusTag = getReadTag (IOCLUtil.PINCODE_TRUCKSTATUS, bayNumber);

		OpcItem truckStatusItem = clc.ReadTag2(truckStatusTag);
		// get bay number tag details from plc
		OpcItem fanNoItem = clc.ReadTag2(fanNoTag);

		// get bay number tag details from plc
		OpcItem bayNumberItem = clc.ReadTag2(bayNumberTag);

		// get bay number tag details from plc
		OpcItem pinCodeItem = clc.ReadTag2(pinCodeTag);

		// get bay number tag details from plc
		
		OpcItem bayStatusItem = clc.ReadTag2(bayStatusTag);

		if (null == pinCodeItem ||  null == bayNumberItem || null == truckStatusTag || null == bayStatusItem)
		{
			return false;
		}
		
		long fanNo = fanNoItem.getValue().getInteger();
		int fanPINNumber= pinCodeItem.getValue().getWord();
		int bayNo = bayNumberItem.getValue().getWord();
		int status = bayStatusItem.getValue().getWord();
		int truckStatus = truckStatusItem.getValue().getWord();
		
		BayStatus bayStatus = new BayStatus();
		
		bayStatus.setBayNumber(bayNo);
		bayStatus.setFanNo(fanNo);
		bayStatus.setPinCode(fanPINNumber);
		bayStatus.setStatus(status);
		bayStatus.setTruckStatus(truckStatus);
 		
		
		java.util.Date utilDate = new java.util.Date();
		java.sql.Timestamp bayLastUpdatedTimeStamp = new java.sql.Timestamp(utilDate.getTime());
		
		// Check if pin code retrieved from plc matches with that of IOCL DB.
	
		
		// Write back the pin code, truck and other details back to PLC.

		BayStatusUpdate(bayStatus);
		
		resetBayStatusChange(bayNo);
		
		itemList.add(truckStatusItem);
		itemList.add(fanNoItem);
		itemList.add(bayNumberItem);
		itemList.add(pinCodeItem);
		itemList.add(bayStatusItem);
		
		
		return false;
	}
	// Upon successfull pin code validation request, read pin and bay and authenticate it 
		public boolean resetBayStatusChange (int bayNumber)
		{

			List<OpcItem> itemList = new ArrayList<OpcItem>();
			
			// if matched then read pincode info from iocl db and write it back to plc
			OpcItem bayStatusChangeItem = getWriteItem(IOCLUtil.STATUS_CHANGE, bayNumber, BayStatus.Change.FALSE);

			itemList.add(bayStatusChangeItem);
			 
			clc.WriteItems(itemList);
	 
			return true;
		}

	// Upon successfull pin code validation request, read pin and bay and authenticate it 
	 
}
