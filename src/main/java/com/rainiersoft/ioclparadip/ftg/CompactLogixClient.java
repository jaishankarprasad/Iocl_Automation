package com.rainiersoft.ioclparadip.ftg;

import java.util.List;


import org.apache.log4j.Logger;

import com.rainiersoft.api.OpcClientImpl;
import com.rainiersoft.core.Tag;
import com.rainiersoft.core.TagType;
import com.rainiersoft.exception.RSException;
import com.rainiersoft.util.RSContants;
import com.rainiersoft.util.RSUtil;

import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.variant.Variant;

import java.util.ArrayList;
import java.util.HashMap;

public class CompactLogixClient
{
	final static Logger logger=Logger.getLogger(CompactLogixClient.class);
	
	public final String topic="[TrialTopic]";
	public final String bayPrefix ="Bay";

	private OpcClientImpl opcClientimpl;

	private final String localhost="localhost";
  //private final String progId="Intellution.IntellutionGatewayOPCServer";
	private final String progId="FactoryTalk Gateway";
	private final String clientHandle="JOPC2";
	
    private static CompactLogixClient plcConnectClient = null;

    public HashMap<String, OpcItem> plcItemMap = new HashMap<>();
    public HashMap<String, OpcItem> dbItemMap = new HashMap<>();



    private CompactLogixClient ()
    {
    	
    }
	
    public static CompactLogixClient getInstance ()
    {
    	if (null == plcConnectClient)
    	{
    		plcConnectClient = new CompactLogixClient ();	
     		
    	}
    	
    	return plcConnectClient;   	
    }


    public void init()
	{
		//below line is Commented on 02/07/2018 by Madhu for closing error			
		//opcClientimpl=OpcClientImpl.getInstance();
			
		  opcClientimpl=new OpcClientImpl();
		OpcClientImpl.initialize();

		try 
		{
			opcClientimpl.setAdapter(RSContants.ADAPTER_TYPE_IGS);
			opcClientimpl.Connect(localhost, progId, clientHandle);
			System.out.println("Connected::IGS");
			
		} 
		catch (RSException e) 
		{
			logger.error(e);
			e.printStackTrace();
		}
		 
	}

    
    public void setInMemoryTagTable (HashMap<String, OpcItem> plcItemMap)
    {
    	this.plcItemMap = plcItemMap;
    }
    public HashMap<String, OpcItem>  getInMemoryTagTable ()
    {
    	return this.plcItemMap;
    }
    
    public void setDBTagTable (HashMap<String, OpcItem> dbItemMap)
    {
    	this.dbItemMap = dbItemMap;
    }
    public HashMap<String, OpcItem>  getDBTagTable ()
    {
    	return this.dbItemMap;
    }
    
    public void getTruckWeight()
    {
    	
    }	

    public void deInit()
	{
		//below line is Commented on 02/07/2018 by Madhu for closing error			
		//opcClientimpl=OpcClientImpl.getInstance();
			
		OpcClientImpl.deinitialize();
	}

    //**************
    public String getCompteTagName (String tagName, int bayNumber)
	{
		String tagPrefix ="[TrialTopic]"+bayPrefix+ String.format("%02d", bayNumber)+ "_";
		// read pin code and bay number from plc.
		return (tagPrefix + tagName);
	}
	
	private Tag getReadTag (String tagName, int bayNumber)
	{
		TagType tagType = new TagType((String)IOCLUtil.ioclTagMap.get(tagName));
		String tagCompleteName = getCompteTagName(tagName, bayNumber);
		Tag tag = new Tag(tagCompleteName, "", tagType, tagCompleteName); 
		
		return tag;
		
	}

	private OpcItem getOpcItem (String tagName, int bayNumber)
	{
		OpcItem item = new OpcItem(getCompteTagName(tagName, bayNumber), true, "");
		Tag tag = getReadTag (tagName, bayNumber);
		
		int varType = RSUtil.getVariantType(tag.getTagType().getName());

		switch (varType)
		{
		
			case Variant.VT_I2 :
			{
				item.setValue(new Variant(0));
				break;
			}
			case Variant.VT_R4 :
			{
				item.setValue(new Variant(0.0f));
				break;
			}
			case Variant.VT_R8 :
			{
				item.setValue(new Variant(0.0d));
				break;			    	
			}
			case Variant.VT_BSTR:
			{
				item.setValue(new Variant(""));
				break;			    	
			}
			case Variant.VT_BOOL:
			{
				item.setValue(new Variant(false));
				break;	
			}
			default :
				item.setValue(new Variant(0));
				
		}

		    
		return item;		
		     
		
		
	}
		
	private OpcItem[] generateOpcItems ()
	{
		ArrayList<OpcItem> items = new ArrayList <OpcItem>();
		
		for (int i=1; i<= 8; i++ )
		{
			items.add(getOpcItem (IOCLUtil.PINCODE_VALIDATION_REQ, i));
			items.add(getOpcItem (IOCLUtil.PINCODE_VALIDATION_REQ, i));
			items.add(getOpcItem (IOCLUtil.PINCODE_VALIDATION_BAYNUMBER, i));
			items.add(getOpcItem (IOCLUtil.PINCODE_VALIDATION_PINCODE, i));
			items.add(getOpcItem (IOCLUtil.PINCODE_PINCODESTATUS, i));
			items.add(getOpcItem (IOCLUtil.PINCODE_FANNO, i));
			items.add(getOpcItem (IOCLUtil.PINCODE_PINCODE, i));
			items.add(getOpcItem (IOCLUtil.PINCODE_PRESETVALUE, i));
			items.add(getOpcItem (IOCLUtil.PINCODE_TRUCKNO, i));
			items.add(getOpcItem (IOCLUtil.PINCODE_TRANSPORTERNAME, i));
			items.add(getOpcItem (IOCLUtil.PINCODE_LOCATION, i));
			items.add(getOpcItem (IOCLUtil.PINCODE_CAPACITY, i));
			items.add(getOpcItem (IOCLUtil.PINCODE_TAREWEIGHT, i));
			items.add(getOpcItem (IOCLUtil.PINCODE_TRUCKSTATUS, i));
			items.add(getOpcItem (IOCLUtil.PINCODE_TRUCKNO_LEN, i));
			items.add(getOpcItem (IOCLUtil.PINCODE_TRANSPORTERNAME_LEN, i));
			items.add(getOpcItem (IOCLUtil.PINCODE_LOCATION_LEN, i));
			items.add(getOpcItem (IOCLUtil.STATUS_CHANGE, i));
			items.add(getOpcItem (IOCLUtil.STATUS_BAYNUMBER, i));
			items.add(getOpcItem (IOCLUtil.STATUS_PINCODE, i));
			items.add(getOpcItem (IOCLUtil.STATUS_STATUS, i));
			items.add(getOpcItem (IOCLUtil.TRUCKWEIGHT_REQ, i));
			items.add(getOpcItem (IOCLUtil.TRUCKWEIGHT_WEIGHT, i));
		}
		
		return items.toArray(new OpcItem[items.size()]);
	}

    //************
    public void populateItems()
    {
    	opcClientimpl=new OpcClientImpl();
    	OpcClientImpl.initialize();
    	
    	OpcItem [] items = generateOpcItems ();
		  
		  OpcGroup group = new OpcGroup("Group1", true, 4000, 0.0f);
		  
		  for (int i=0; i< items.length; i++)
		    {
//		     System.out.println("Item Name "+ items[i].getItemName());
		     logger.info("Item Name "+ items[i].getItemName());
		     group.addItem(items[i]);
		     
		    }		  
		  try 
		  {
			//Thread.sleep(100);
		   opcClientimpl.Connect(localhost, progId, clientHandle); 
		   OpcGroup currentGroup =  opcClientimpl.ReadGroup(group);
		   if (null != currentGroup)
		   {
		    items = currentGroup.getItemsAsArray();
		    for (int i=0; i< items.length; i++)
		    {
//		     System.out.println("Item Name "+ items[i]);
		     logger.info("Item Name "+ items[i]);
		     plcItemMap.put(items[i].getItemName(), items[i]);
		     
		    }
		   }
		   
		  }
		  catch (RSException e) 
		  {
			  logger.error(e);
		    e.printStackTrace();
		  }
		/*  catch (InterruptedException e) 
		  {
		    e.printStackTrace();
		  }*/
		  
		     
		    }
 
    public OpcItem ReadTag (Tag tag)
	{
			System.out.println("inside readTag..");	
			logger.info("inside readTag..");
	    OpcItem item = new OpcItem(tag.getName(), true, "");
	    
	    System.out.println("item:"+item);
	    OpcItem responseItem = null;
	    
				int varType = RSUtil.getVariantType(tag.getTagType().getName());
				System.out.println("varType:"+varType);
				logger.info("varType:"+varType);
				switch (varType)
				{
				
				case Variant.VT_I2 :
				{
					item.setValue(new Variant(0));
					break;
				}
				case Variant.VT_R4 :
				{
					System.out.println("item:before");
					logger.info("item:before");
					item.setValue(new Variant(0.0f));
					System.out.println("item:after");
					logger.info("item:after");
					break;
				}
				case Variant.VT_R8 :
				{
					item.setValue(new Variant(0.0d));
					break;			    	
				}
				case Variant.VT_BSTR:
				{
					item.setValue(new Variant(""));
					break;			    	
				}
				case Variant.VT_BOOL:
				{
					item.setValue(new Variant(false));
					break;	
				}
				default :
					item.setValue(new Variant(0));
					
				}
	    
	    OpcGroup group = new OpcGroup("group1", true, 500, 0.0f);
	    System.out.println("OPcgroup:before adding item");
		group.addItem(item);
	    System.out.println("OPcgroup:after adding item");
	    logger.info("OPcgroup:after adding item");

		try 
		{	
			try
			{
				Thread.sleep(100);
			} 
			catch (InterruptedException e)
			{
				logger.error(e);
				e.printStackTrace();
			}
			responseItem = opcClientimpl.ReadItem(group,item);
			System.out.println("responseItem:"+responseItem);
			logger.info("responseItem:"+responseItem);
			System.out.println(responseItem);
			System.out.println(Variant.getVariantName(responseItem.getDataType()) + ": " + responseItem.getValue());
			logger.info(Variant.getVariantName(responseItem.getDataType()) + ": " + responseItem.getValue());

		} 
		catch (RSException e)
		{	
			logger.error(e);
			e.printStackTrace();
		}
		
		return responseItem;
 	}
	   
    public void WriteItem(OpcItem item) 
    {
	    OpcGroup group = new OpcGroup("group1", true, 500, 0.0f);	    
		group.addItem(item);  
		try 
		{
			opcClientimpl.WriteItem(group,item);
		}
		catch (RSException e) 
		{
			logger.error(e);
			e.printStackTrace();
		}  

    }
    public boolean bayAbort (int bayNo)
   	{
    	TagType tagType = new TagType((String)IOCLUtil.ioclTagMap.get(IOCLUtil.BAY_ABORT_REQ));
   		
   	    OpcItem item = new OpcItem(IOCLUtil.BAY_ABORT_REQ, true, "");
    	    
   		int variantType = RSUtil.getVariantType(tagType.getName());
   		
   		Variant varObj = RSUtil.getVariantValue(variantType,""+true);
   		
   		item.setValue(varObj);
   	
   		WriteItem(item);
   		
   		return true;
   		
   	}
    
    public OpcItem ReadTag2 (Tag tag)
 	{
 				
     	opcClientimpl=new OpcClientImpl();

   		OpcClientImpl.initialize();

 	    OpcItem item = new OpcItem(tag.getName(), true, "");
 	    OpcItem responseItem = null;
 	    
 				int varType = RSUtil.getVariantType(tag.getTagType().getName());
 				
 				switch (varType)
 				{
 				
 				case Variant.VT_I2 :
 				{
 					item.setValue(new Variant(0));
 					break;
 				}
 				case Variant.VT_R4 :
 				{
 					item.setValue(new Variant(0.0f));
 					break;
 				}
 				case Variant.VT_R8 :
 				{
 					item.setValue(new Variant(0.0d));
 					break;			    	
 				}
 				case Variant.VT_BSTR:
 				{
 					item.setValue(new Variant(""));
 					break;			    	
 				}
 				case Variant.VT_BOOL:
 				{
 					item.setValue(new Variant(false));
 					break;	
 				}
 				default :
 					item.setValue(new Variant(0));
 					
 				}
 	    
 	   OpcGroup group = new OpcGroup("group1", true, 500, 0.0f);
 	    
 	   group.addItem(item);

 		try 
 		{	
 			Thread.sleep(100);	
 			opcClientimpl.Connect(localhost, progId, clientHandle);

 		    responseItem = opcClientimpl.ReadItem(group,item);
              int n=responseItem.getValue().toString().length();
              
 			System.out.println("length response item value is:::"+n);
 			logger.info("length response item value is:::"+n);
 			
 			if(n==0) 
 			{
 				while(/*responseItem.getValue().toString()*/n==0)
 				{
 				System.out.println("First Attempt failed...Retrying to Read item");
 				logger.info("First Attempt failed...Retrying to Read item");
 				Thread.sleep(100);				
 			    responseItem = opcClientimpl.ReadItem(group,item);
 				}
 				System.out.println(responseItem);
 				logger.info(responseItem);
 				System.out.println(Variant.getVariantName(responseItem.getDataType()) + ": " + responseItem.getValue());
 				logger.info(Variant.getVariantName(responseItem.getDataType()) + ": " + responseItem.getValue());
 			 }
 			else
 			 {					
 				System.out.println(responseItem);
 				logger.info(responseItem);
 				System.out.println(Variant.getVariantName(responseItem.getDataType()) + ": " + responseItem.getValue());
 				logger.info(Variant.getVariantName(responseItem.getDataType()) + ": " + responseItem.getValue());
 			
 			 }
 		   } 
 		catch (RSException e)
 		{
 			logger.error(e);
 			e.printStackTrace();
 		}
 		 catch (InterruptedException e)
 		{
 				logger.error(e);
 				e.printStackTrace();
 		}		
 		OpcClientImpl.deinitialize();
 		
 		return responseItem;
  	}
    
    public void WriteItems(List<OpcItem> itemList)
    {
    	writeParallelthread writeInParallelThread  = null;
    	int threadNo = 0;
    	
      	for (OpcItem hl : itemList)
      	{
      		writeInParallelThread = new writeParallelthread (hl, threadNo++);
      		writeInParallelThread.start();
      	}
     	 
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
				logger.error(e);
				e.printStackTrace();
			}
			
		}
		
	}
    public OpcItem [] retreiveAllItems()
    {
		opcClientimpl=new OpcClientImpl();
		OpcClientImpl.initialize();
		OpcItem [] itemList = generateOpcItems();
		OpcItem [] responseItemList = null;
		
		
		OpcGroup group = new OpcGroup("group1", true, 2000, 0.0f);
		
		for (int i =0; i < itemList.length; i++)
		{
			group.addItem(itemList[i]);
//			System.out.println(itemList[i]);
		}
		
		
		try 
		{
			opcClientimpl.Connect(localhost, progId, clientHandle);
			OpcGroup currentGroup =  opcClientimpl.ReadGroup(group);
//			System.out.println("RESPONSE"+currentGroup);
			if (null != currentGroup)
			{
				responseItemList = currentGroup.getItemsAsArray();
			}
			
		}
		catch (RSException e) 
		{
			logger.error(e);
 			e.printStackTrace();
		}
		
		OpcClientImpl.deinitialize();
		
		return responseItemList;
	}

    public static void main(String[] args) 
    {
    	CompactLogixClient cm=new CompactLogixClient();
    	cm.populateItems();
	}
}

