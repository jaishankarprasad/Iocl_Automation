package com.rainiersoft.ioclparadip.ftg;

import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.rainiersoft.core.TagType;

import javafish.clients.opc.exception.VariantTypeException;
import javafish.clients.opc.lang.Translate;
import javafish.clients.opc.variant.Variant;

public class IOCLUtil 
{
 	public static final HashMap<Integer, String> variantMap = new HashMap<>();
 	public static final HashMap<String, Integer> tagMap = new HashMap<>();
 	
 	private static Logger logger=Logger.getLogger(IOCLUtil.class);
 	
 	public static final HashMap<String, String> ioclTagMap = new HashMap<>();
 	
 	public static final String PINCODE_VALIDATION_REQ="PinCodeValidation.Req";
	public static final String PINCODE_VALIDATION_BAYNUMBER="PinCodeValidation.BayNumber";
	public static final String PINCODE_VALIDATION_PINCODE="PinCodeValidation.PinCode";
	public static final String PINCODE_PINCODESTATUS="PinCodeInfo.PinCodStatus";
	public static final String PINCODE_FANNO="PinCodeInfo.FanNo";
	public static final String PINCODE_PINCODE="PinCodeInfo.PinCode";
	public static final String PINCODE_PRESETVALUE="PinCodeInfo.PresetValue";
	
	public static final String PINCODE_TRUCKNO_LEN="PinCodeInfo.TruckNo.LEN";
//	public static final String PINCODE_TRUCKNO="PinCodeInfo.TruckNo.DATA";
	public static final String PINCODE_TRANSPORTERNAME_LEN="PinCodeInfo.TransporterName.LEN";
//	public static final String PINCODE_TRANSPORTERNAME="PinCodeInfo.TransporterName.DATA";	

	public static final String PINCODE_LOCATION_LEN="PinCodeInfo.Location.LEN";
//	public static final String PINCODE_LOCATION="PinCodeInfo.Location.DATA";

	public static final String PINCODE_CAPACITY="PinCodeInfo.Capacity";
	public static final String PINCODE_TAREWEIGHT="PinCodeInfo.Tare_Weight";

	public static final String PINCODE_READINGATDLVRYSTART="PinCodeInfo.ReadingAtDlvryStart";
	public static final String PINCODE_READINGATDLVRYCOMPLETED="PinCodeInfo.ReadingAtDlvryCmpltd";
	public static final String PINCODE_LOADEDQTY="PinCodeInfo.LoadedQty";
	
	public static final String PINCODE_TRUCKSTATUS="PinCodeInfo.TruckStatus";
	
	public static final String STATUS_CHANGE="Status.Change";
	public static final String STATUS_BAYNUMBER="Status.BayNumber";
	public static final String STATUS_PINCODE="Status.PinCode";
	public static final String STATUS_STATUS="Status.Status";
  	
	public static final String BAY_ABORT_REQ="AbortReq";
		
	public static final String PINCODE_TRUCKNO="PinCodeInfo.TruckNo";
	public static final String PINCODE_TRANSPORTERNAME="PinCodeInfo.TransporterName";	
	public static final String PINCODE_LOCATION="PinCodeInfo.Location";
	
		
	public static final String TRUCKWEIGHT_REQ="TruckWeight.Req";
	public static final String TRUCKWEIGHT_WEIGHT="TruckWeight.Weight";
	public static final String TRUCKWEIGHT_DATAREADY= "TruckWeight.DataReady";
	
 	static 
 	{
 		
 		ioclTagMap.put(PINCODE_VALIDATION_REQ, "1bit boolean");
 		ioclTagMap.put(PINCODE_VALIDATION_BAYNUMBER, "16bit Int");
 		ioclTagMap.put(PINCODE_VALIDATION_PINCODE, "16bit Int");
 		ioclTagMap.put(PINCODE_PINCODESTATUS, "16bit Int");
 		ioclTagMap.put(PINCODE_FANNO, "32bit Int");
 		ioclTagMap.put(PINCODE_PINCODE, "16bit Int");
 		ioclTagMap.put(PINCODE_PRESETVALUE, "32bit Int");

 		ioclTagMap.put(PINCODE_TRUCKNO_LEN, "16bit Int");
 		ioclTagMap.put(PINCODE_TRUCKNO, "String");
 		ioclTagMap.put(PINCODE_TRANSPORTERNAME_LEN, "16bit Int");
 		ioclTagMap.put(PINCODE_TRANSPORTERNAME, "String");
 		ioclTagMap.put(PINCODE_LOCATION_LEN, "16bit Int");
 		ioclTagMap.put(PINCODE_LOCATION, "String");

 		ioclTagMap.put(PINCODE_CAPACITY, "32bit Float");
 		ioclTagMap.put(PINCODE_TAREWEIGHT, "32bit Float");
 		
 		ioclTagMap.put(PINCODE_READINGATDLVRYSTART, "32bit Float");
 		ioclTagMap.put(PINCODE_READINGATDLVRYCOMPLETED, "32bit Float");
 		ioclTagMap.put(PINCODE_LOADEDQTY, "32bit Float");

 		ioclTagMap.put(PINCODE_TRUCKSTATUS, "16bit Int");
 		
		ioclTagMap.put(STATUS_CHANGE, "16bit Int");
 		ioclTagMap.put(STATUS_BAYNUMBER, "16bit Int");
 		ioclTagMap.put(STATUS_PINCODE, "16bit Int");
 		ioclTagMap.put(STATUS_STATUS, "16bit Int");

 		ioclTagMap.put(BAY_ABORT_REQ, "1bit boolean");
 		 
 		ioclTagMap.put(TRUCKWEIGHT_REQ, "1bit boolean");
 		ioclTagMap.put(TRUCKWEIGHT_WEIGHT, "32bit Float");
 		ioclTagMap.put(TRUCKWEIGHT_DATAREADY, "1bit boolean");
 		
 		
 		variantMap.put(new Integer(Variant.VT_I2), "16bit Int");
 		variantMap.put(new Integer(Variant.VT_I4), "32bit Int");
 		variantMap.put(new Integer(Variant.VT_R4), "32bit Float");
 		variantMap.put(new Integer(Variant.VT_R8), "64bit Float");
 		variantMap.put(new Integer(Variant.VT_BOOL), "1bit boolean");
 		variantMap.put(new Integer(Variant.VT_BSTR), "String");
 		

 		tagMap.put("16bit Int", new Integer(Variant.VT_I2));
 		tagMap.put("32bit Int", new Integer(Variant.VT_I4));
 		tagMap.put("32bit Float", new Integer(Variant.VT_R4));
 		tagMap.put("64bit Float", new Integer(Variant.VT_R8));
 		tagMap.put("1bit boolean", new Integer(Variant.VT_BOOL));
 		tagMap.put("String", new Integer(Variant.VT_BSTR));
 	}
 	
	public static int getVariantType (String tagType)
	{
		Integer intObj = tagMap.get(tagType); 
		return intObj.intValue();
		
	}
	
	public static String getTagType(int variantType)
	{
		return (String) variantMap.get(new Integer(variantType)); 
		
	}
	
	public static Variant getVariantValue(int tagType, Object value)
	{
		
		 switch (tagType) 
		 {
	      case Variant.VT_I4:
		        return new Variant(Double.parseDouble((String)value));
	      case Variant.VT_I2:	  
	    	  return new Variant(Integer.parseInt((String)value));
	      case Variant.VT_BOOL:
	    	  return new Variant(Boolean.parseBoolean((String)value));
	      case Variant.VT_R4:
	    	  return new Variant(Float.parseFloat((String)value));
	      case Variant.VT_R8:
	    	  return new Variant(Double.parseDouble((String)value));
	      case Variant.VT_BSTR:
	    	  return new Variant((String)value);
	      case Variant.VT_ERROR:
	      case Variant.VT_NULL:
	      case Variant.VT_EMPTY:
	        return new Variant();
	      default :
	    	logger.error("VARIANT_TYPE_EXCEPTION");
	        throw new VariantTypeException(Translate.getString("VARIANT_TYPE_EXCEPTION"));
       
	    }		
	}
	
	public static String getTagValue(Variant varObj)
	{
	    int varianType = varObj.getVariantType();

	      switch(varianType)
	      {
	 	  case Variant.VT_I2 :
	      case Variant.VT_INT :
	 		  return ""+varObj.getInteger();
	      case Variant.VT_R4 :
	    	  return ""+varObj.getFloat();
	      case Variant.VT_BOOL :
	    	  return ""+varObj.getBoolean();
	      case Variant.VT_R8:
	    	  return ""+varObj.getDouble();
	      case Variant.VT_BSTR:
	    	  return ""+varObj.getString();
	      default :
		        return "";
	      
	      }
	}
}
