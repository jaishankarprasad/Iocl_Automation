package com.rainiersoft.api;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.rainiersoft.core.Tag;
import com.rainiersoft.core.TagType;
import com.rainiersoft.exception.RSException;
import com.rainiersoft.ioclparadip.ftg.IOCLUtil;
import com.rainiersoft.ioclparadip.ftg.PinCodeInfo;
import com.rainiersoft.util.RSContants;
import com.rainiersoft.util.RSUtil;

import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.variant.Variant;

public class OpcClientMultiThreadTest
{
	public final String bayPrefix ="Bay";
	
	private static Logger logger=Logger.getLogger(OpcClientMultiThreadTest.class);
	
	OpcClientImpl opcClientimpl;
	
	
	PinCodeInfo pinCodeInfo;

	String localhost="localhost";
	//String progId="Intellution.IntellutionGatewayOPCServer";
	String progId="FactoryTalk Gateway";
	String clientHandle="JOPC2";
	
	private List<writeParallelthread> threadArray = new ArrayList<writeParallelthread>();
	
	public static void main(String[] args)
	{
		OpcClientMultiThreadTest opcclientTest=new OpcClientMultiThreadTest();		
		
		System.out.println(Calendar.getInstance().getTimeInMillis());		
		
		opcclientTest.writePinCodeInfo (1 );
		
		System.out.println(Calendar.getInstance().getTimeInMillis());	}


	public OpcClientMultiThreadTest ()
	{
		Init ();
		
		pinCodeInfo = new PinCodeInfo ();
		
		pinCodeInfo.setBayNumber(5);
		pinCodeInfo.setPinCodeValidation_Req(true);
		pinCodeInfo.setPinCodeValidation_PinCode(1111);
		pinCodeInfo.setPinCodeInfo_PinCodStatus(4);
		pinCodeInfo.setPinCodeInfo_FanNo(1212);
		pinCodeInfo.setPinCodeInfo_PinCode(5555);
		pinCodeInfo.setPinCodeInfo_PresetValue(65464);
		pinCodeInfo.setPinCodeInfo_TruckNo_Len(4);
		pinCodeInfo.setPinCodeInfo_TruckNo("WXYZ");
		pinCodeInfo.setPinCodeInfo_TransporterName_Len(5);
		pinCodeInfo.setPinCodeInfo_TransporterName("heheh");
		pinCodeInfo.setPinCodeInfo_Capacity(12312.566f);
		pinCodeInfo.setPinCodeInfo_Tare_Weight(7897.30f);
		pinCodeInfo.setPinCodeInfo_TruckStatus(9);
		pinCodeInfo.setPinCodeInfo_Location_Len(4);
		pinCodeInfo.setPinCodeInfo_Location("Your");
		
	}
	
	public void Init()
	{
		opcClientimpl=new OpcClientImpl();
		OpcClientImpl.initialize();
		try 
		{
			opcClientimpl.setAdapter(RSContants.ADAPTER_TYPE_IGS);
			opcClientimpl.Connect(localhost, progId, clientHandle);
			System.out.println("Connected::IGS");
			logger.info("Connected::IGS");
						
		} 
		catch (RSException e) 
		{

			e.printStackTrace();
			logger.error(e);
		}
	
	}

	private Tag getReadTag (String ioclTagName, int bayNumber)
	{
		String tagPrefix ="[TrialTopic]"+ bayPrefix+ String.format("%02d", bayNumber)+ "_";
		// read pin code and bay number from plc.
		
		String tagName = tagPrefix + ioclTagName;
		TagType tagType = new TagType((String)IOCLUtil.ioclTagMap.get(ioclTagName));
		Tag tag = new Tag(tagName, "", tagType, tagName); 
		
		return tag;
		
	}

	private OpcItem getWriteItem (String ioclTagName, int bayNumber, Object value)
	{
		String tagPrefix ="[TrialTopic]"+ bayPrefix+ String.format("%02d", bayNumber)+ "_";
		// read pin code and bay number from plc.
		
		String tagName = tagPrefix + ioclTagName;
		TagType tagType = new TagType((String)IOCLUtil.ioclTagMap.get(ioclTagName));
		
	    OpcItem item = new OpcItem(tagName, true, "");
 	    
		int variantType = RSUtil.getVariantType(tagType.getName());
		
		Variant varObj = RSUtil.getVariantValue(variantType,value.toString());
		
		item.setValue(varObj);
	
		return item;
		
	}
	 

	// Upon successfull pin code validation request, read pin and bay and authenticate it 
	public boolean writePinCodeInfo(int bayNumber)
	{
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
		
		if (true)
		{
			threadArray.add(new writeParallelthread(pinCodeStausItem,1));
		    threadArray.add(new writeParallelthread(pinCodeFanNoItem,2));
			threadArray.add(new writeParallelthread(pinCodePinCodeItem,3));
			threadArray.add(new writeParallelthread(pinCodePreSetValueItem,4));
			threadArray.add(new writeParallelthread(pinCodeTruckNoLen,5));
			threadArray.add(new writeParallelthread(pinCodeTruckNoItem,6));
			threadArray.add(new writeParallelthread(pinCodeTransporterNameLen,7));
		    threadArray.add(new writeParallelthread(pinCodeTransporterNameItem,8));
		 	threadArray.add(new writeParallelthread(pinCodeLocationLen,9));
		 	threadArray.add(new writeParallelthread(pinCodeLocationItem,10));
			threadArray.add(new writeParallelthread(pinCodeCapacityItem,11));
			threadArray.add(new writeParallelthread(pinCodeTareWeightItem,12));
			threadArray.add(new writeParallelthread(pinCodeTruckStatusItem,13));
			
		}
			for (writeParallelthread hl : threadArray)
				hl.start();
		
 		return false;
	}
	
	public class writeParallelthread extends Thread
	{
		OpcClientImpl opcClient1;
		OpcItem item = null;
		int threadNo = 0;
		
		
		public writeParallelthread (OpcItem item, int threadNo)
		{
		  this.item = item;	
		  this.threadNo = threadNo;
		  Init ();
		}
		
		public void run ()
		{
			try
			{
				WriteItem(item);
			} 
			catch (Exception e) 
			{
				logger.error(e);
				e.printStackTrace();
			}
			
		}		
				
		public void WriteItem(OpcItem item)  throws Exception
		{
		    OpcGroup group = new OpcGroup("Group1", true, 500, 0.0f);
		    
			group.addItem(item);			
			System.out.println(item);
			logger.info(item);
			
			opcClient1.WriteItem(group,item);
				
			System.out.println("Thread :"+threadNo);
			logger.info("Thread :"+threadNo);
		 
		 }
		public void Init()
		{
			opcClient1=new OpcClientImpl();
			OpcClientImpl.initialize();
			try 
			{
				opcClient1.setAdapter(RSContants.ADAPTER_TYPE_IGS);
				opcClient1.Connect(localhost, progId, ""+threadNo);
				System.out.println("Connected::IGS");
				logger.info("Connected::IGS");
				
			} 
			catch (RSException e) 
			{

				e.printStackTrace();
				logger.error(e);
			}
		
		}

	}

}
