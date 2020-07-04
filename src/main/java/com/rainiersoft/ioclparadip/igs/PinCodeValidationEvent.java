package com.rainiersoft.ioclparadip.igs;

import java.util.ArrayList;

import java.util.List;

import com.rainiersoft.core.Tag;
import com.rainiersoft.core.TagType;
import com.rainiersoft.util.RSUtil;

import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.variant.Variant;

public class PinCodeValidationEvent implements IEventStatusChangedListener
{

	public final String PINCODE_VALIDATION_REQUEST= "PinCodeValidation.Req";
	
	public final String bayPrefix ="Bay";
	
	public final boolean runInParallel = false;
	public final boolean runInGroup = false;
	
	
	private int bayNo = 1;
 
	PinCodeInfo pinCodeInfo = null;
	CompactLogixClient clc = null;
	IOCLDButil ioclUtil = null;

	public PinCodeValidationEvent (int bayNo)
	{
		clc = CompactLogixClient.getInstance();
		clc.init();
		this.bayNo = bayNo;
		ioclUtil = new IOCLDButil ();
	}
	
	
	public void StatusChanged()
	{
		// write the business logic for PinCode ValidationEvent change.
		
		if ( validatePinCodeInfo (this.bayNo) == true)
		{
		  System.out.print(" Valid pin, Writing back to PLC with details");
		}
		else
		{
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
		String tagPrefix ="Channel1.Device1.Global."+bayPrefix+ String.format("%02d", bayNumber)+ "_";
		// read pin code and bay number from plc.
		
		String tagName = tagPrefix + ioclTagName;
		TagType tagType = new TagType((String)IOCLUtil.ioclTagMap.get(ioclTagName));
		Tag tag = new Tag(tagName, "", tagType, tagName); 
		
		return tag;
		
	}

	private OpcItem getWriteItem (String ioclTagName, int bayNumber, Object value)
	{
		String tagPrefix ="Channel1.Device1.Global."+bayPrefix+ String.format("%02d", bayNumber)+ "_";
		// read pin code and bay number from plc.
		
		String tagName = tagPrefix + ioclTagName;
		TagType tagType = new TagType((String)IOCLUtil.ioclTagMap.get(ioclTagName));
		
	    OpcItem item = new OpcItem(tagName, true, "");
 	    
		int variantType = RSUtil.getVariantType(tagType.getName());
		
		Variant varObj = RSUtil.getVariantValue(variantType,""+value);
		
		item.setValue(varObj);
	
		return item;
		
	}

	// Upon successfull pin code validation request, read pin and bay and authenticate it 
	public boolean validatePinCodeInfo(int bayNumber )
	{
		Tag bayNumberTag = getReadTag (IOCLUtil.PINCODE_VALIDATION_BAYNUMBER, bayNumber);
		Tag pinCodeTag  =  getReadTag (IOCLUtil.PINCODE_VALIDATION_PINCODE, bayNumber);
		
		// get bay number tag details from plc
		OpcItem bayNumberItem = clc.ReadTag(bayNumberTag);

		// get bay number tag details from plc
		OpcItem pinCodeItem = clc.ReadTag(pinCodeTag);
		
		if (null == pinCodeItem ||  null == bayNumberItem)
		{
			return false;
		}
		
		int fanPINNumber= pinCodeItem.getValue().getWord();
		int bayNo = bayNumberItem.getValue().getWord();
			
		// Check if pin code retrieved from plc matches with that of IOCL DB.
		pinCodeInfo = getPinCodeDetails (fanPINNumber , bayNo);
		
		if (pinCodeInfo.getPinCodeInfo_PinCodStatus() != 1)
		{
			return false;
		}
		
		// Write back the pin code, truck and other details back to PLC.
		writePinCodeInfo(bayNo, pinCodeInfo);
		
		return false;
	}

	// Upon successfull pin code validation request, read pin and bay and authenticate it 
	public boolean writePinCodeInfo(int bayNumber, PinCodeInfo pinCodeInfo)
	{

		List<OpcItem> itemList = new ArrayList<OpcItem>();
		
		// if matched then read pincode info from iocl db and write it back to plc
		OpcItem pinCodeStausItem = getWriteItem(IOCLUtil.PINCODE_PINCODESTATUS, bayNumber, pinCodeInfo.getPinCodeInfo_PinCodStatus());
		OpcItem pinCodeFanNoItem = getWriteItem(IOCLUtil.PINCODE_FANNO, bayNumber, pinCodeInfo.getPinCodeInfo_FanNo());
		OpcItem pinCodePinCodeItem = getWriteItem(IOCLUtil.PINCODE_PINCODE, bayNumber, pinCodeInfo.getPinCodeInfo_PinCode());
		OpcItem pinCodePreSetValueItem =  getWriteItem(IOCLUtil.PINCODE_PRESETVALUE, bayNumber, pinCodeInfo.getPinCodeInfo_PresetValue());
		OpcItem pinCodeTruckNoLen = getWriteItem(IOCLUtil.PINCODE_TRUCKNO_LEN, bayNumber, pinCodeInfo.getPinCodeInfo_TruckNo_Len());
		OpcItem pinCodeTruckNoItem = getWriteItem(IOCLUtil.PINCODE_TRUCKNO, bayNumber, pinCodeInfo.getPinCodeInfo_TruckNo());
		OpcItem pinCodeTransporterNameLen = getWriteItem(IOCLUtil.PINCODE_TRANSPORTERNAME_LEN, bayNumber, pinCodeInfo.getPinCodeInfo_TransporterName_Len());
		OpcItem pinCodeTransporterNameItem = getWriteItem(IOCLUtil.PINCODE_TRANSPORTERNAME, bayNumber, pinCodeInfo.getPinCodeInfo_TransporterName());
		OpcItem pinCodeLocationLen = getWriteItem(IOCLUtil.PINCODE_LOCATION_LEN, bayNumber, pinCodeInfo.getPinCodeInfo_Location_Len());
		OpcItem pinCodeLocationItem = getWriteItem(IOCLUtil.PINCODE_LOCATION, bayNumber, pinCodeInfo.getPinCodeInfo_Location());
		OpcItem pinCodeCapacityItem = getWriteItem(IOCLUtil.PINCODE_CAPACITY, bayNumber, pinCodeInfo.getPinCodeInfo_Capacity());
		OpcItem pinCodeTareWeightItem = getWriteItem(IOCLUtil.PINCODE_TAREWEIGHT, bayNumber, pinCodeInfo.getPinCodeInfo_Tare_Weight());
		OpcItem pinCodeTruckStatusItem = getWriteItem(IOCLUtil.PINCODE_TRUCKSTATUS, bayNumber, pinCodeInfo.getPinCodeInfo_TruckStatus());
		
 		itemList.add(pinCodeStausItem);
		itemList.add(pinCodeFanNoItem);
		itemList.add(pinCodePinCodeItem);
		itemList.add(pinCodePreSetValueItem);
		itemList.add(pinCodeTruckNoLen);
		itemList.add(pinCodeTruckNoItem);
		itemList.add(pinCodeTransporterNameLen);
		itemList.add(pinCodeTransporterNameItem);
		itemList.add(pinCodeLocationLen);
		itemList.add(pinCodeLocationItem);
		itemList.add(pinCodeCapacityItem);
		itemList.add(pinCodeTareWeightItem);
		itemList.add(pinCodeStausItem);
		itemList.add(pinCodeTruckStatusItem);
		itemList.add(pinCodeTruckStatusItem);
		
		
		clc.WriteItems(itemList);
 
		return true;
	}
	
	 
}

