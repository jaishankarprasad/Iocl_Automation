package com.rainiersoft.ioclparadip.ftg;

import org.apache.log4j.Logger;

import com.rainiersoft.core.Tag;
import com.rainiersoft.core.TagType;


public class TestPinCodeEvent
{
	final static Logger logger = Logger.getLogger(TestPinCodeEvent.class);
	IEventStatusChangedListener PinCodeValidationEvent; 
	
	public final String PINCODE_VALIDATION_REQUEST= "PinCodeValidation.Req";
	
	public final String bayPrefix ="Bay";
	
	public Tag tag;
	
	PollEvent ts;
	
	public static void main (String arg[])
	{
		System.out.println("Starting to Poll Event..");
		
		logger.info("Starting to Poll Event..");
		
		TestPinCodeEvent te = new TestPinCodeEvent ();
		te.ts.start();
		
	}
	
	public TestPinCodeEvent ()
	{
		PinCodeValidationEvent = new PinCodeValidationEvent(2);
		tag = getReadTag(PINCODE_VALIDATION_REQUEST, 2);
		ts = new PollEvent (tag, 5000, PinCodeValidationEvent);
	}
	
	private Tag getReadTag (String ioclTagName, int bayNumber)
	{
//		String tagPrefix ="Channel1.Device1.Global."+bayPrefix+ String.format("%02d", bayNumber)+ "_";
		String tagPrefix ="[TrialTopic]"+bayPrefix+ String.format("%02d", bayNumber)+ "_";

		// read pin code and bay number from plc.
		
		String tagName = tagPrefix + ioclTagName;
		TagType tagType = new TagType((String)IOCLUtil.ioclTagMap.get(ioclTagName));
		Tag tag = new Tag(tagName, "", tagType, tagName); 
		
		return tag;
		
	}
}
