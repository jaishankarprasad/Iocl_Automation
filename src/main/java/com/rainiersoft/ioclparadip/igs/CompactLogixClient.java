package com.rainiersoft.ioclparadip.igs;

import java.util.List;

import org.apache.log4j.Logger;

import com.rainiersoft.api.OpcClientImpl;
import com.rainiersoft.core.Tag;
import com.rainiersoft.core.TagType;
import com.rainiersoft.exception.RSException;
//import com.rainiersoft.iocl.resources.HashMap;
import com.rainiersoft.util.RSContants;
import com.rainiersoft.util.RSUtil;

import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.variant.Variant;

import java.util.ArrayList;
import java.util.HashMap;

public class CompactLogixClient
{
	
	public final String bayPrefix ="Bay";
	
	private OpcClientImpl opcClientimpl;

	final static Logger logger=Logger.getLogger(CompactLogixClient.class);
	private final String localhost="localhost";
//	private final String progId="Intellution.IntellutionGatewayOPCServer";
	private final String progId="FactoryTalk Gateway";
	private final String clientHandle="JOPC2";
	
    private static CompactLogixClient plcConnectClient = null;

    public HashMap<String, OpcItem> plcItemMap = new HashMap<>();



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
			logger.info("Connected::IGS");
			
		} 
		catch (RSException e) 
		{

			logger.error(e);
			e.printStackTrace();
		}
		 
	}

    public void deInit()
	{
		//below line is Commented on 02/07/2018 by Madhu for closing error			
		//opcClientimpl=OpcClientImpl.getInstance();
			
		OpcClientImpl.deinitialize();
	}

    //**************
    private String getCompteTagName (String tagName, int bayNumber)
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
		   //OpcItem [] items = null;
    	
    	OpcItem [] items = generateOpcItems ();
		  
		  OpcGroup group = new OpcGroup("group1", true, 4000, 0.0f);
		  
		  for (int i=0; i< items.length; i++)
		    {
		     System.out.println("Item Name "+ items[i].getItemName());
		     group.addItem(items[i]);
		     
		    }
		  
		  try 
		  {
		   OpcGroup currentGroup =  opcClientimpl.ReadGroup(group);
		   if (null != currentGroup)
		   {
		    items = currentGroup.getItemsAsArray();
		    for (int i=0; i< items.length; i++)
		    {
		     System.out.println("Item Name "+ items[i]);
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
		  
		     
		    }
 
    public OpcItem ReadTag (Tag tag)
	{
			System.out.println("inside readTag..");	
	    OpcItem item = new OpcItem(tag.getName(), true, "");
	    
	    System.out.println("item:"+item);
	    OpcItem responseItem = null;
	    
				int varType = RSUtil.getVariantType(tag.getTagType().getName());
				System.out.println("varType:"+varType);
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
					item.setValue(new Variant(0.0f));
					System.out.println("item:after");
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
			logger.info("responseItem:"+responseItem);
			System.out.println("responseItem:"+responseItem);
            logger.info(Variant.getVariantName(responseItem.getDataType()) + ": " + responseItem.getValue());
			System.out.println(Variant.getVariantName(responseItem.getDataType()) + ": " + responseItem.getValue());

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
			logger.info("Thread :"+threadNo);	
			System.out.println("Thread :"+threadNo);
		 
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

}

