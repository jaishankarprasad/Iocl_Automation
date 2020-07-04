package com.rainiersoft.api;

import java.io.IOException;

import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.rainiersoft.core.DeviceConfig;
import com.rainiersoft.exception.RSException;
import com.rainiersoft.util.*;

import de.re.easymodbus.datatypes.RegisterOrder;
import de.re.easymodbus.modbusclient.*;
import javafish.clients.opc.*;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.exception.ComponentNotFoundException;
import javafish.clients.opc.exception.ConnectivityException;
import javafish.clients.opc.exception.SynchWriteException;
import javafish.clients.opc.exception.UnableAddGroupException;
import javafish.clients.opc.exception.UnableAddItemException;
import javafish.clients.opc.exception.UnableRemoveGroupException;
import javafish.clients.opc.exception.UnableRemoveItemException;
import javafish.clients.opc.variant.Variant;

public class OpcClientImpl implements OpcClient
{
	final static Logger logger = Logger.getLogger(OpcClientImpl.class);
	private int adapterType = RSContants.ADAPTER_TYPE_DEFAULT;
	private int WRITE_RETRY_COUNT = 5;
	ModbusClient modbusClient = null;
	private boolean modBusConnected = false;
	private DeviceConfig dc =null;
	JOpc  opcClient = null;
	private static OpcClientImpl opcClientImpl = null;
	
	public void setDeviceConfig (DeviceConfig dc)
	{
		this.dc = dc;
	}
	public static void initialize()
	{
		JOpc.coInitialize();
	}
	public static void deinitialize()
	{
		JOpc.coUninitialize();
	}
	
	/*private OpcClientImpl ()
	{
		
	}*/	
	// Ensure always only single instance class.
/*	public static OpcClientImpl getInstance ()
	{
		   if(opcClientImpl == null)
		   {
			   opcClientImpl = new OpcClientImpl();
			   
	        }
		   
	        return opcClientImpl;
	}
	*/
	 public OpcClientImpl()
	 {
		 adapterType  = RSContants.ADAPTER_TYPE_IGS;
	 }


//	public OpcClientImpl () 
//	{
//		adapterType  = RSContants.ADAPTER_TYPE_DEFAULT;
//	}
//	
//	public OpcClientImpl (int adapterType) throws RSException
//	{
//		setAdapter( adapterType);
//	}
//	
	public boolean isModBusConnected ()
	{
		return modBusConnected;
	}
	public void setAdapter(int adapter) throws RSException
	{
		switch (adapter)
		{
		  case RSContants.ADAPTER_TYPE_DEFAULT:
			  
			adapterType = RSContants.ADAPTER_TYPE_DEFAULT;
			break;
		  case RSContants.ADAPTER_TYPE_MODBUS:
		  {
			adapterType = RSContants.ADAPTER_TYPE_MODBUS;
			modbusClient =ModbusClient.getInstance();
			break;
		  }
		  case RSContants.ADAPTER_TYPE_IGS:
		  {
			adapterType = RSContants.ADAPTER_TYPE_IGS;	
			//opcClient=new JOpc(host, serverProgID, serverClientHandle);
		  break;
		  }
		  case RSContants.ADAPTER_TYPE_ETHERNET:
		  {
			adapterType = RSContants.ADAPTER_TYPE_ETHERNET;	
			//opcClient=new JOpc(host, serverProgID, serverClientHandle);
		  break;
		  }
			default:
			
				//adapterType = RSContants.ADAPTER_TYPE_DEFAULT;
		}
	}
	
		
	public void Connect (String host, String serverProgID, String serverClientHandle) throws RSException
	{
		
		if (adapterType != RSContants.ADAPTER_TYPE_IGS)
		{
			logger.error("invalid  parameters to connect");
			throw new RSException("invalid  parameters to connect");
		}
		 
		opcClient = new JOpc(host, serverProgID,serverClientHandle);
		
		if(opcClient != null) {
			
		   // System.out.println("connected to host "+host);
			try
			{
				opcClient.connect();
			}
			catch (ConnectivityException e) 
			{
				logger.error(e);
				e.printStackTrace();
			}
		}
	}
	
	public void Connect (String comPort) throws RSException
	{
		
		if (adapterType != RSContants.ADAPTER_TYPE_MODBUS)
		{
			logger.error("invalid  parameters to connect");
			throw new RSException("invalid  parameters to connect");
		}
		
		try
		{
			if (isModBusConnected () == false)
			{
				modbusClient =ModbusClient.getInstance();
				configureModbusParameters();
				modbusClient.Connect(comPort);
				logger.info("connected::"+comPort);
				System.out.println("connected::"+comPort);
				modBusConnected = true;
				
			}
		}
		catch (Exception e)
		{
			logger.error(e);
			e.printStackTrace();
			logger.error("Serial port connection failed");
			throw new RSException("Serial port connection failed");
		}
	}
	
	public void Connect (String ipAddress, int port) throws RSException
	{
		if (adapterType != RSContants.ADAPTER_TYPE_ETHERNET)
			
		{
			logger.error("invalid  parameters to connect");
			throw new RSException("invalid  parameters to connect");
		}
		
		try
		{
			modbusClient =ModbusClient.getInstance();
			//configureModbusParameters();
			modbusClient.Connect(ipAddress, port);
		} 
		catch (UnknownHostException e)
		{	
			logger.error(e);
			logger.error("TCP Connection failed");
			e.printStackTrace();
			throw new RSException("TCP Connection failed");
		} 
		catch (IOException e)
		{	
			logger.error(e);
			logger.error("TCP Connection failed");
			e.printStackTrace();
			throw new RSException("TCP Connection failed");
		}
	}
	
	public void unconnect () throws RSException
	{
		if (adapterType == RSContants.ADAPTER_TYPE_MODBUS)
		{
			try
			{
				modbusClient.Disconnect();
				
			} catch (IOException e)
			{
				logger.error("TCP Connection failed");
				throw new RSException("TCP Connection failed");
				
			}
		}
		
	}
	
	public void configureModbusParameters()
	{
		modbusClient.setConfiguration(this.dc.getComport(), Integer.parseInt(this.dc.getBaudrate().trim()), this.dc.getParity(), this.dc.getStopbits(),this.dc.getIsHighByteFirst(),this.dc.getDeviceid());
 		
	}
////////
	
	public OpcItem ReadItem (OpcGroup group,OpcItem item) throws RSException
	{
		System.out.println("group:"+group);
		System.out.println("item:"+item);
		if (adapterType == RSContants.ADAPTER_TYPE_IGS)
		{
			System.out.println("inside If block:");
			OpcItem returnItem=null;
			try
			{	
				opcClient.addGroup(group);
				opcClient.registerGroups();
				Thread.sleep(100);
				logger.info("opcClient..:"+opcClient);
				System.out.println("opcClient..:"+opcClient);
				returnItem	= opcClient.synchReadItem(group, item);
				opcClient.unregisterGroups();
				opcClient.removeGroup(group);
				logger.info("returnItem:"+returnItem);
				System.out.println("returnItem:"+returnItem);
				return returnItem;
				 
			} 
			catch (Exception e) 
			{
				logger.error(e);
				logger.error(" Invalid Item ");
				e.printStackTrace();
				throw new RSException (" Invalid Item ");
			}

		}
		
		else if (adapterType == RSContants.ADAPTER_TYPE_MODBUS)
		{
			int register = Integer.parseInt(item.getItemName());
			
			OpcItem returnItem = new OpcItem(""+register, true, "");
			
			if (register > 0 && register < 30000)
			{
				// Reading coils
				
				boolean values[];
				try 
				{
					values = modbusClient.ReadCoils(register, 1);
				} 
				catch (Exception e) 
				{
					logger.error(" Invalid Item ");
					throw new RSException (" Invalid Item ");
				}
				if (null == values || values.length != 1)
				{
					logger.error(" Invalid Item ");
					throw new RSException (" Invalid Item ");
				}
		
				returnItem.setValue(new Variant(values[0]));
			}
			else
			{
				// Reading Holding registers or input registers
				
				int values[];
				Variant returnVariant = null;
				
				try 
				{
					
					int qty = 1;
					int varType = item.getValue().getVariantType();
					
				    switch (varType)
				    {
				    
					    case Variant.VT_I2 :
					    {
					    	qty = 1;
					    	break;
					    }
					    case Variant.VT_R4 :
					    {
					    	qty=2;
					    	break;
					    }
					    case Variant.VT_R8 :
					    {
					    	qty=4;
 					    	break;			    	
					    }
					    default :
					    	qty = 1;
					    	
	 			    }
					
					
					values = modbusClient.ReadHoldingRegisters(register-40001, qty);
					
					//values = modbusClient.ReadInputRegisters(register, 1);
					
					//int dataType = item.getDataType();
					
					System.out.println("Values::"+values);
					
					int dataType = varType;
					
					switch (dataType)
					{
						case Variant.VT_I2:
						{
							returnVariant = new Variant(values[0]);
							break;
						}
						case Variant.VT_INT:
						{
							if(values.length>1)
							{

							    if (this.dc.getIsHighByteFirst())
							    {
							    	returnVariant = new Variant(ModbusClient.ConvertRegistersToInt(values, RegisterOrder.HighLow));
							    }
							    else
							    {
							    	returnVariant = new Variant(ModbusClient.ConvertRegistersToInt(values));
							    }
							}
							else
							{
								returnVariant = new Variant(values[0]);
							}
							break;
							
						}
						case Variant.VT_R4:
						{
							if (this.dc.getIsHighByteFirst())
							{
								returnVariant = new Variant(ModbusClient.ConvertRegistersToFloat(values, RegisterOrder.HighLow));
							}
							else
							{
								returnVariant = new Variant(ModbusClient.ConvertRegistersToFloat(values));								
							}
		
							break;
						}
						case Variant.VT_R8:
						{
							if (this.dc.getIsHighByteFirst())
							{
								
								returnVariant = new Variant(ModbusClient.ConvertRegistersToDouble(values, RegisterOrder.HighLow));
							}
							else
							{
								returnVariant = new Variant(ModbusClient.ConvertRegistersToDouble(values));	
							}
							
							break;
						}
						default :
							returnVariant = new Variant (values[0]);
					}
						
					returnItem.setValue(returnVariant);
				}
				catch (Exception e)
				{
					logger.error(e);
					logger.error(" Invalid Item ");
					e.printStackTrace();
					throw new RSException (" Invalid Item ");
					
				}
				if (null == values)
				{
					logger.error(" Invalid Item ");
					throw new RSException (" Invalid Item ");
				}
			}
			
			
			return returnItem;		
		}
		
		///By Madhu 19/06/2018  5:45 pm
		else if (adapterType == RSContants.ADAPTER_TYPE_ETHERNET)
		{
			int register = Integer.parseInt(item.getItemName());
			
			OpcItem returnItem = new OpcItem(""+register, true, "");
			
			if (register > 0 && register < 30000)
			{
				// Reading coils
				
				boolean values[];
				try
				{
					values = modbusClient.ReadCoils(register, 1);
				} 
				catch (Exception e)
				{
					logger.error(e);
					logger.error(" Invalid Item ");
					throw new RSException (" Invalid Item ");
				}
				if (null == values || values.length != 1)
				{
					logger.error(" Invalid Item ");
					throw new RSException (" Invalid Item ");
				}
		
				returnItem.setValue(new Variant(values[0]));
			}
			else
			{
				// Reading Holding registers or input registers
				
				int values[];
				Variant returnVariant = null;
				
				try 
				{
					
					int qty = 1;
					int varType = item.getValue().getVariantType();
					
				    switch (varType)
				    {
				    
					    case Variant.VT_I2 :
					    {
					    	qty = 1;
					    	break;
					    }
					    case Variant.VT_R4 :
					    {
					    	qty=2;
					    	break;
					    }
					    case Variant.VT_R8 :
					    {
					    	qty=4;
 					    	break;			    	
					    }
					    default :
					    	qty = 1;
					    	
	 			    }
					
					
					values = modbusClient.ReadHoldingRegisters(register-40001, qty);
					
					//values = modbusClient.ReadInputRegisters(register, 1);
					
					//int dataType = item.getDataType();
					
					System.out.println("Values::"+values);
					
					int dataType = varType;
					
					switch (dataType)
					{
						case Variant.VT_I2:
						{
							returnVariant = new Variant(values[0]);
							break;
						}
						case Variant.VT_INT:
						{
							if(values.length>1)
							{

							    if (this.dc.getIsHighByteFirst())
							    {
							    	returnVariant = new Variant(ModbusClient.ConvertRegistersToInt(values, RegisterOrder.HighLow));
							    }
							    else
							    {
							    	returnVariant = new Variant(ModbusClient.ConvertRegistersToInt(values));
							    }
							}
							else
							{
								returnVariant = new Variant(values[0]);
							}
							break;
							
						}
						case Variant.VT_R4:
						{
							if (this.dc.getIsHighByteFirst())
							{
								returnVariant = new Variant(ModbusClient.ConvertRegistersToFloat(values, RegisterOrder.HighLow));
							}
							else
							{
								returnVariant = new Variant(ModbusClient.ConvertRegistersToFloat(values));								
							}
		
							break;
						}
						case Variant.VT_R8:
						{
							if (this.dc.getIsHighByteFirst())
							{
								
								returnVariant = new Variant(ModbusClient.ConvertRegistersToDouble(values, RegisterOrder.HighLow));
							}
							else
							{
								returnVariant = new Variant(ModbusClient.ConvertRegistersToDouble(values));	
							}
							
							break;
						}
						default :
							returnVariant = new Variant (values[0]);
					}
						
					returnItem.setValue(returnVariant);
				}
				catch (Exception e)
				{
					logger.error(e);
					logger.error(" Invalid Item ");
					e.printStackTrace();
					throw new RSException (" Invalid Item ");
					
				}
				if (null == values)
				{
					logger.error(" Invalid Item ");
					throw new RSException (" Invalid Item ");
				}
			}
						
			return returnItem;		
		}
		
		return null;
	}
	
/*	public OpcGroup ReadGroup(OpcGroup group) throws RSException
	{
		if (adapterType == RSContants.ADAPTER_TYPE_IGS)
		{
			try
			{	
				opcClient.addGroup(group);
				opcClient.registerGroups();
				//Thread.sleep(50);
				 return opcClient.synchReadGroup(group);
				 
			} catch (Exception e) {
				e.printStackTrace();
				throw new RSException (" Invalid Item ");
			}
		}
		else if (adapterType == RSContants.ADAPTER_TYPE_MODBUS)
		{
			OpcItem items[] = group.getItemsAsArray();

			if (null == items || items.length <= 0)
			{
				throw new RSException (" Invalid Items");
			}
			
			OpcGroup opcGroup = new OpcGroup(group.getGroupName(), true, 100, 0.0f);
			for (int i=0; i< items.length; i++)
			{
				int register = Integer.parseInt(items[i].getItemName());
				
				OpcItem returnItem = new OpcItem(""+register, true, "");
				
				if (register > 0 && register < 30000)
				{
					
					// Reading coils
					
					boolean values[];
					try {
						values = modbusClient.ReadCoils(register, 1);
					} catch (Exception e)
					{
						throw new RSException (" Invalid Item ");
					}
					if (null == values || values.length != 1)
					{
						throw new RSException (" Invalid Item ");
					}
				
					returnItem.setValue(new Variant(values[0]));
				}
				else
				{
					// Reading Holding registers or input registers
					
					int values[];
					try {
						values = modbusClient.ReadHoldingRegisters(register-40001, 1);
					} catch (Exception e)
					{
						throw new RSException (" Invalid Item ");
					}
					if (null == values || values.length != 1)
					{
						throw new RSException (" Invalid Item ");
					}
										
					returnItem.setValue(new Variant(values[0]));
				}
		
				opcGroup.addItem(returnItem);
			}
			
			return opcGroup;		
		}
		
		//by madhu19/06/2018 5:40 pm
		else if (adapterType == RSContants.ADAPTER_TYPE_ETHERNET)
		{
			OpcItem items[] = group.getItemsAsArray();

			if (null == items || items.length <= 0)
			{
				throw new RSException (" Invalid Items");
			}
			
			OpcGroup opcGroup = new OpcGroup(group.getGroupName(), true, 100, 0.0f);
			for (int i=0; i< items.length; i++)
			{
				int register = Integer.parseInt(items[i].getItemName());
				
				OpcItem returnItem = new OpcItem(""+register, true, "");
				
				if (register > 0 && register < 30000)
				{
					
					// Reading coils
					
					boolean values[];
					try {
						values = modbusClient.ReadCoils(register, 1);
					} catch (Exception e)
					{
						throw new RSException (" Invalid Item ");
					}
					if (null == values || values.length != 1)
					{
						throw new RSException (" Invalid Item ");
					}
				
					returnItem.setValue(new Variant(values[0]));
				}
				else
				{
					// Reading Holding registers or input registers
					
					int values[];
					try {
						values = modbusClient.ReadHoldingRegisters(register-40001, 1);
					} catch (Exception e)
					{
						throw new RSException (" Invalid Item ");
					}
					if (null == values || values.length != 1)
					{
						throw new RSException (" Invalid Item ");
					}
										
					returnItem.setValue(new Variant(values[0]));
				}
		
				opcGroup.addItem(returnItem);
			}
			
			return opcGroup;		
		}
			
		return null;

	}*/
	public OpcGroup ReadGroup(OpcGroup group) throws RSException
	{
		if (adapterType == RSContants.ADAPTER_TYPE_IGS)
		{
			try
			{	
			   OpcGroup downGroup;
				
			   opcClient.addGroup(group);
			  // opcClient.ping();
			   opcClient.registerGroups();
				
				Thread.sleep(500);
			 downGroup =  opcClient.synchReadGroup(group);
			 
			//   downGroup = opcClient.getDownloadGroup(); 
			   
			/*   if (downGroup != null) 
			   { 
				   System.out.println("[Group1]" + downGroup); 
			   } */
				   
			   return downGroup;
				 
			} 
			catch (Exception e)
			{
				logger.error(e);
				logger.error(" Invalid Item ");
				e.printStackTrace();
				throw new RSException (" Invalid Item ");
			}
		}
		else if (adapterType == RSContants.ADAPTER_TYPE_MODBUS)
		{
			OpcItem items[] = group.getItemsAsArray();

			if (null == items || items.length <= 0)
			{
				logger.error(" Invalid Item ");
				throw new RSException (" Invalid Items");
			}
			
			OpcGroup opcGroup = new OpcGroup(group.getGroupName(), true, 100, 0.0f);
			for (int i=0; i< items.length; i++)
			{
				int register = Integer.parseInt(items[i].getItemName());
				
				OpcItem returnItem = new OpcItem(""+register, true, "");
				
				if (register > 0 && register < 30000)
				{
					
					// Reading coils
					
					boolean values[];
					try 
					{
						values = modbusClient.ReadCoils(register, 1);
					} 
					catch (Exception e)
					{
						logger.error(e);
						logger.error(" Invalid Item ");
						throw new RSException (" Invalid Item ");
					}
					if (null == values || values.length != 1)
					{
						logger.error(" Invalid Item ");
						throw new RSException (" Invalid Item ");
					}
				
					returnItem.setValue(new Variant(values[0]));
				}
				else
				{
					// Reading Holding registers or input registers
					
					int values[];
					try 
					{
						values = modbusClient.ReadHoldingRegisters(register-40001, 1);
					} 
					catch (Exception e)
					{
						logger.error(e);
						logger.error(" Invalid Item ");
						throw new RSException (" Invalid Item ");
					}
					if (null == values || values.length != 1)
					{
						logger.error(" Invalid Item ");
						throw new RSException (" Invalid Item ");
					}
										
					returnItem.setValue(new Variant(values[0]));
				}
		
				opcGroup.addItem(returnItem);
			}
			
			return opcGroup;		
		}
		
		//by madhu19/06/2018 5:40 pm
		else if (adapterType == RSContants.ADAPTER_TYPE_ETHERNET)
		{
			OpcItem items[] = group.getItemsAsArray();

			if (null == items || items.length <= 0)
			{
				logger.error(" Invalid Item ");
				throw new RSException (" Invalid Items");
			}
			
			OpcGroup opcGroup = new OpcGroup(group.getGroupName(), true, 100, 0.0f);
			for (int i=0; i< items.length; i++)
			{
				int register = Integer.parseInt(items[i].getItemName());
				
				OpcItem returnItem = new OpcItem(""+register, true, "");
				
				if (register > 0 && register < 30000)
				{
					
					// Reading coils
					
					boolean values[];
					try
					{
						values = modbusClient.ReadCoils(register, 1);
					}
					catch (Exception e)
					{
						logger.error(e);
						logger.error(" Invalid Item ");
						throw new RSException (" Invalid Item ");
					}
					if (null == values || values.length != 1)
					{
						logger.error(" Invalid Item ");
						throw new RSException (" Invalid Item ");
					}
				
					returnItem.setValue(new Variant(values[0]));
				}
				else
				{
					// Reading Holding registers or input registers
					
					int values[];
					try 
					{
						values = modbusClient.ReadHoldingRegisters(register-40001, 1);
					}
					catch (Exception e)
					{
						logger.error(e);
						logger.error(" Invalid Item ");
						throw new RSException (" Invalid Item ");
					}
					if (null == values || values.length != 1)
					{
						logger.error(" Invalid Item ");
						throw new RSException (" Invalid Item ");
					}
										
					returnItem.setValue(new Variant(values[0]));
				}
		
				opcGroup.addItem(returnItem);
			}
			
			return opcGroup;		
		}
			
		return null;

	}
	private boolean WriteItemReAttempt (OpcGroup group,OpcItem item)
	{
		try
		{			
			opcClient.synchWriteItem(group,item);
		}
		catch (ComponentNotFoundException | SynchWriteException e) 
		{
			logger.error(e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void WriteItem(OpcGroup group,OpcItem item) throws RSException
	 {
		 if (adapterType == RSContants.ADAPTER_TYPE_IGS)
			{
			 
			  boolean writeSucceeded = false;
			 
				try 
				{
					
					opcClient.addGroup(group);					
					opcClient.registerGroup(group);
					opcClient.registerItem(group, item);
					
						Thread.sleep(50);
					 
					opcClient.synchWriteItem(group,item);
		   			opcClient.unregisterItem(group, item);					
		    	    opcClient.unregisterGroup(group);
				}
				catch (InterruptedException e)
				{
					logger.error(e);
					e.printStackTrace();
				}
				catch (UnableRemoveGroupException e)
				{ 
					logger.error(e);
					logger.error(" unable to remove group ");
					e.printStackTrace();
					throw new RSException (" unable to remove group ");
				}
				catch (UnableRemoveItemException e) 
				{
					logger.error(e);
					e.printStackTrace();
				}
				catch (UnableAddItemException e1)
				{	
					logger.error(e1);
					e1.printStackTrace();
				}
				catch (UnableAddGroupException e)
				{ 
					logger.error(e);
					logger.error(" unable to add group ");
					e.printStackTrace();
					throw new RSException (" unable to add group ");
				}			
				catch (ComponentNotFoundException e)
				{	
					logger.error(e);
					logger.error(" component not found");
					e.printStackTrace();
					throw new RSException (" component not found");
				} 
				catch (SynchWriteException e)
				{					 
					/*for (int i =0; i < WRITE_RETRY_COUNT; i++)
					{
						try
						{
							System.out.println("Synchnous Writing Issue, ReAttempting to Write Again..Re-Attempt:"+ i);
							
							writeSucceeded = WriteItemReAttempt (group, item);
							if ( writeSucceeded == true )
							{
								break;
							}
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}*/
					logger.error(e);
					e.printStackTrace();
				}
			
				
				/*if (writeSucceeded == false)
				{
					throw new RSException ("Write Item un successfull");
				}*/
			}
		 else if (adapterType == RSContants.ADAPTER_TYPE_MODBUS)
		 {
			 
			  int register = Integer.parseInt(item.getItemName());
				 
				Variant val = item.getValue();
			   if (register > 0 && register < 30000)
				{
					// writing coils
				  if ( val.getVariantType() != Variant.VT_BOOL)
				  {
					  //throw new RSException ("invalid data set");
				  }
						   
				  try 
				  {
					 
					modbusClient.WriteSingleCoil(register-1, val.getBoolean() );					
			       } 
				  catch (Exception e)
				  {
					  logger.error(e);
					  logger.error("invalid data set");
					 throw new RSException ("invalid data set");
				  }
				
				}
			   else
			   {
				  try
				  {
					  
						 switch (val.getVariantType()) 
						 {
						      case Variant.VT_INT:
						      {
						    	 //4 bytes integer
						    	 if (this.dc.getIsHighByteFirst())
						    	 {
								     modbusClient.WriteMultipleRegisters(register-40001, ModbusClient.ConvertIntToRegisters(val.getInteger(), RegisterOrder.HighLow));						    		 
						    	 }
						    	 else
						    	 {
						    		 modbusClient.WriteMultipleRegisters(register-40001, ModbusClient.ConvertIntToRegisters(val.getInteger()));
						    	 }
							     break;
						      }
						      case Variant.VT_I2:
						      {
							      //2 bytes integer
						    	  modbusClient.WriteSingleRegister(register-40000, val.getInteger());
						    	  break;
						      }
						      case Variant.VT_R4:
						      {	
							    	 //4 bytes float
							    if (this.dc.getIsHighByteFirst())
							    {
						    	  modbusClient.WriteMultipleRegisters(register-40001, ModbusClient.ConvertFloatToRegisters(val.getFloat(), RegisterOrder.HighLow));
							    }
							    else
							    {
							    	modbusClient.WriteMultipleRegisters(register-40001, ModbusClient.ConvertFloatToRegisters(val.getFloat()));
							    }
						    	  break;
						      }
						    	  
						      case Variant.VT_R8:
						      {
							    	 //8 bytes double
								    if (this.dc.getIsHighByteFirst())
								    {
								    	
								    	modbusClient.WriteMultipleRegisters (register-40001, ModbusClient.ConvertDoubleToRegisters(val.getDouble(), RegisterOrder.HighLow));
								    }
								    else
								    {
								    	modbusClient.WriteMultipleRegisters (register-40001, ModbusClient.ConvertDoubleToRegisters(val.getDouble()));
								    }
						    	  break;
						      }
						      default :
						    	  System.out.println(" Invalid data type");
						    	  break;
					      }
						 
				  }
				  catch (Exception e) 
				  {
					  logger.error(e);
					  logger.error("invalid data set");
					  e.printStackTrace();
					 throw new RSException ("invalid data set");
				}
			   }
			 
			 	 
		 }
		 else if (adapterType == RSContants.ADAPTER_TYPE_ETHERNET)
		 {
			 
			  int register = Integer.parseInt(item.getItemName());
				 
				Variant val = item.getValue();
			   if (register > 0 && register < 30000)
				{
					// writing coils
				  if ( val.getVariantType() != Variant.VT_BOOL)
				  {
					  //throw new RSException ("invalid data set");
				  }
						   
				  try 
				  {
					 
					modbusClient.WriteSingleCoil(register, val.getBoolean() );
					
				  }
				  catch (Exception e)
				  {
					  logger.error(e);
					  logger.error("invalid data set");
					 throw new RSException ("invalid data set");
				}
				
				}
			   else
			   {
				  try
				  {
					  
						 switch (val.getVariantType()) 
						 {
						      case Variant.VT_INT:
						      {
						    	 //4 bytes integer
						    	 if (this.dc.getIsHighByteFirst())
						    	 {
								     modbusClient.WriteMultipleRegisters(register-40001, ModbusClient.ConvertIntToRegisters(val.getInteger(), RegisterOrder.HighLow));						    		 
						    	 }
						    	 else
						    	 {
						    		 modbusClient.WriteMultipleRegisters(register-40001, ModbusClient.ConvertIntToRegisters(val.getInteger()));
						    	 }
							     break;
						      }
						      case Variant.VT_I2:
						      {
							      //2 bytes integer
						    	  modbusClient.WriteSingleRegister(register-40000, val.getInteger());
						    	  break;
						      }
						      case Variant.VT_R4:
						      {	
							    	 //4 bytes float
							    if (this.dc.getIsHighByteFirst())
							    {
						    	  modbusClient.WriteMultipleRegisters(register-40001, ModbusClient.ConvertFloatToRegisters(val.getFloat(), RegisterOrder.HighLow));
							    }
							    else
							    {
							    	modbusClient.WriteMultipleRegisters(register-40001, ModbusClient.ConvertFloatToRegisters(val.getFloat()));
							    }
						    	  break;
						      }
						    	  
						      case Variant.VT_R8:
						      {
							    	 //8 bytes double
								    if (this.dc.getIsHighByteFirst())
								    {
								    	
								    	modbusClient.WriteMultipleRegisters (register-40001, ModbusClient.ConvertDoubleToRegisters(val.getDouble(), RegisterOrder.HighLow));
								    }
								    else
								    {
								    	modbusClient.WriteMultipleRegisters (register-40001, ModbusClient.ConvertDoubleToRegisters(val.getDouble()));
								    }
						    	  break;
						      }
						      default :
						    	  System.out.println(" Invalid data type");
						    	  break;
					      }
						 
				  }
				  catch (Exception e) 
				  {
					  logger.error(e);
					  logger.error("invalid data set");
					  e.printStackTrace();
					 throw new RSException ("invalid data set");
				}
			   }
			 
			 	 
		 }
		 
	 }
	 public void WriteGroup(OpcGroup group, OpcItem item) throws RSException
	 {
		 if (adapterType == RSContants.ADAPTER_TYPE_IGS)
			{
				try
				{
					opcClient.synchWriteItem(group, item);
				} 
				catch (Exception e)
				{
					logger.error("invalid data set");
					 throw new RSException ("invalid data set");
				}
			}
		 else if (adapterType == RSContants.ADAPTER_TYPE_MODBUS)
		 {
			 
			 OpcItem items[] = group.getItemsAsArray();
			 
			 for (int i =0 ; i < items.length; i++)
			 {
				 int register = Integer.parseInt(items[i].getItemName());
				 
				 Variant val = items[i].getValue();
			   if (register > 0 && register < 30000)
				{
					// writing coils
				  if ( val.getVariantType() != Variant.VT_BOOL)
				  {
					  throw new RSException ("invalid data set");
				  }
				  
				  
				  try
				  {
						   
				  modbusClient.WriteSingleCoil(register, val.getBoolean());
				  }
				  catch (Exception e)
				  {
					  logger.error(e);
					  logger.error("invalid data set");
					  throw new RSException ("invalid data set");  
				  }
				
				}
			   else
			   {
				  try
				  {
					// write registers
					modbusClient.WriteSingleRegister(register, val.getInteger());
				}
				  catch (Exception e)
				  {
					  logger.error(e);
					  logger.error("invalid data set");
					 throw new RSException ("invalid data set");
				}
			   }
			 
			 }	 
		 }
		 
	 }

	@Override
	public boolean isConnected() throws RSException {

	 if (adapterType == RSContants.ADAPTER_TYPE_IGS)
		 {
		
		 }
	 else if(adapterType == RSContants.ADAPTER_TYPE_MODBUS)
	 {
		 modbusClient.isConnected();
	 }
	 return false;
	}

	
}
