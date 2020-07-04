package com.rainiersoft.ioclparadip.ftg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.rainiersoft.core.DBConnector;
import com.rainiersoft.util.PropertyLoader;
import com.rainiersoft.util.RSUtil;

import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.variant.Variant;

public class DBThread extends Thread 
{
	 final static Logger logger = Logger.getLogger(DBThread.class);
	 private int pollInterval = 10;	 
	 private String propFile = "iocl.properties";
	 private String DB_SNAPSHOT_INTERVAL = "DB_SNAPSHOT_INTERVAL";
	 public HashMap<String, OpcItem> dbItemMap = new HashMap<>();
	 DBConnector dbCon = null;
	 Connection con = null;
	 
	 public DBThread ()
	 {
		 pollInterval = getPollInterval();
		 dbCon = DBConnector.getInstance();
		 con=dbCon.getConnection();
	 }
	 
	 private int getPollInterval ()
	 {
		 PropertyLoader prop = new PropertyLoader ();
		 prop.loadProperties(propFile);
		 pollInterval = Integer.parseInt(prop.getValue(DB_SNAPSHOT_INTERVAL));
 		 
		 return pollInterval;
	 }
	 
	  private String getTime ()
	  {
			Timestamp ts = new Timestamp (new java.util.Date().getTime());
			return ts.toString();
	  }
	  private Timestamp getTimeS()
	  {
			Timestamp ts = new Timestamp (new java.util.Date().getTime());
			return ts;
	  }
	
	    @Override
	    public void run()
	    {
	       
	        System.out.println("Initating DB THREAD ....- START "+Thread.currentThread().getName()+":"+getTime());
	        logger.info("Initating DB THREAD ....- START "+Thread.currentThread().getName()+":"+getTime());
	        
	        while(true) 
	        {
		        System.out.println("Capturing the snap shot of consumer thread's tag table into DB ..- START "+Thread.currentThread().getName()+":"+getTime());
		        logger.info("Capturing the snap shot of consumer thread's tag table into DB ..- START "+Thread.currentThread().getName()+":"+getTime());
		        try
		        {
		        	 Thread.sleep(pollInterval);
		             doProcessing();
		           
		            System.out.println("Consumer thread snapshot persisted  ..- END"+Thread.currentThread().getName()+":"+getTime());
			        logger.info("Consumer thread snapshot persisted  ..- END"+Thread.currentThread().getName()+":"+getTime());
		           
		        }
		        catch (InterruptedException e)
		        {
		        	logger.error(e);
		            e.printStackTrace();
		        }
		        
 	        }
	    }

	    private  void  doProcessing() throws InterruptedException
	    {
	    	
	    	WriteSnapshotToDB (dbItemMap);
 	    	
	    }
	    
	    public void WriteSnapshotToDB (HashMap<String, OpcItem> dbItemMap)
	    {
	    	try
			{
				PreparedStatement ps=con.prepareStatement("insert into tagsnapshot (snapshotid,itemName, active, itemType, itemValue, itemQuality,lastupdated)values(?,?,?,?,?,?,?)");

				//ps.executeUpdate();
				
				 Iterator it = dbItemMap.entrySet().iterator();
				 int id1=getLatestSnapshotId();
				 
				 Timestamp ts1=getTimeS();
				 
				 while (it.hasNext()) 
				 {
				        Map.Entry pair = (Map.Entry)it.next();
				        OpcItem item = (OpcItem) pair.getValue();
				       // System.out.println("Current Item"+item);

				                ps.setInt(1, id1+1);
				                ps.setString(2, item.getItemName());
								ps.setString(3, String.valueOf(item.isActive()));
								ps.setString(4, RSUtil.getTagType(item.getDataType()));
								ps.setString(5, RSUtil.getTagValue(item.getValue()));
								ps.setString(6, String.valueOf(item.isQuality()));
							    ps.setTimestamp(7,ts1);
 				         
						ps.executeUpdate();
 		        }
				
				ps.close();
			}
			
			catch (SQLException e)
			{
				logger.error(e);
				e.printStackTrace();
			}
 
	    	incrementSnapshotIdBy1();
	
	    }
	    
	    public HashMap<String, OpcItem> getDbItemMap() {
			return dbItemMap;
		}

		public void setDbItemMap(HashMap<String, OpcItem> dbItemMap) {
			this.dbItemMap = dbItemMap;
		}

		public HashMap<String, OpcItem> readSnapshotFromDB ()
	    {
	    	HashMap<String, OpcItem> dbItemMap = new HashMap<String, OpcItem> ();
	    	
	    	try
			{
	    		int latest=getLatestSnapshotId();

				PreparedStatement ps=con.prepareStatement("select * from tagsnapshot where snapshotid="+latest+"");				
				ResultSet rs=ps.executeQuery();
				
				while(rs.next())
		        {
					String itemName=rs.getString("itemName");
					String active=rs.getString("active");
					String itemType=rs.getString("itemType");
					String itemValue=rs.getString("itemValue");
					String itemQuality=rs.getString("itemQuality");
					
					System.out.println("snapshotid::"+latest+"itemname;;"+itemName+"itemvalue;;"+itemValue+"itemType"+itemType);
					
					OpcItem item = new OpcItem (itemName, Boolean.parseBoolean(active), "");
					item.setQuality(Boolean.parseBoolean(itemQuality));
					Variant val = RSUtil.getVariantValue(RSUtil.getVariantType(itemType), itemValue);
					item.setValue(val);

					dbItemMap.put(itemName, item);
 		        }
				
				rs.close();
			}
			
			catch (SQLException e)
			{
				logger.error(e);
				e.printStackTrace();
			}
 
			return dbItemMap;		
	    	
	    }
 
	
	    public int getLatestSnapshotId() 
	    {
//	    	updateSnapId();
	    	int n=0;
	    	
	    	try
			{
				PreparedStatement ps=con.prepareStatement("select ids from snapshotid where ids in(select max(ids) from snapshotid)");				
				ResultSet rs=ps.executeQuery();
				
				while(rs.next())
		        {
					 n=rs.getInt("ids");
 		        }
				
				rs.close();
			}
			
			catch (SQLException e)
			{
				logger.error(e);
				e.printStackTrace();
			}
			return n;
 	    	
	    }
	    
	 
	    public void incrementSnapshotIdBy1()
	    {
	    	try
			{
				PreparedStatement ps=con.prepareStatement("insert into snapshotid(description) values(?)");
				
			  ps.setString(1,"EntryValueee");
			  ps.executeUpdate();
			  ps.close();
			}
			
			catch (SQLException e)
			{
				logger.error(e);
				e.printStackTrace();
			}
 			    	
	    }
	    
	    public static void main (String args[])
	    {
	    	DBThread dbthread = new DBThread ();
	    	dbthread.start();
	    	
	    }
	  
	    

	}
