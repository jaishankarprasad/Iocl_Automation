package com.rainiersoft.ioclparadip.ftg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.apache.log4j.Logger;

import com.rainiersoft.core.DBConnector;
import com.rainiersoft.util.PropertyLoader;

public class DBCleanThread extends Thread 
{
	 final static Logger logger = Logger.getLogger(DBCleanThread.class);
	 private long pollInterval = 60 * 60 * 1000;	 
	 private String propFile = "iocl.properties";
	 private String DB_CLEAN_SNAPSHOT_INTERVAL = "DB_CLEAN_SNAPSHOT_INTERVAL";
	 DBConnector dbCon = null;
	 Connection con = null;
	 
	 public DBCleanThread ()
	 {
		 super.setName("DBCLEANTHREAD");
		 pollInterval = getPollInterval();
		 dbCon = DBConnector.getInstance();
		 con=dbCon.getConnection();
	 }
	 
	 private long getPollInterval ()
	 {
		 PropertyLoader prop = new PropertyLoader ();
		 prop.loadProperties(propFile);
		 pollInterval = Integer.parseInt(prop.getValue(DB_CLEAN_SNAPSHOT_INTERVAL));
 		 
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
	    	Thread.currentThread().setUncaughtExceptionHandler(new IOCLParadipMainExecutor());

	        System.out.println("Initating DB CLEAN THREAD ....- START "+Thread.currentThread().getName()+":"+getTime());
	        logger.info("Initating DB CLEAN THREAD ....- START "+Thread.currentThread().getName()+":"+getTime());
	        
	        while(true) 
	        {
		        try
		        {
		        	 Thread.sleep(pollInterval);
		             doProcessing();
		           
			        logger.info("DB CLEAN thread snapshots cleaned up ..- END"+Thread.currentThread().getName()+":"+getTime());
		           
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
	    	
	    	CleanSnapshotsFromDB ();
 	    	
	    }
	    
	    
	    public int GetLatestSnapShotId()
	    {
	    	int snapShotId=0;
	    	
	    	try
			{
				PreparedStatement ps=con.prepareStatement("select ids from snapshotid group by ids having ids=max(ids)");				
				ResultSet rs=ps.executeQuery();
				
				while(rs.next())
		        {
					snapShotId=rs.getInt("ids");
 		        }
				
				rs.close();
			}
			
			catch (SQLException e)
			{
				logger.error(e);
				e.printStackTrace();
			}
			return snapShotId;
 	    	
	    }

	    public void CleanSnapshotsFromDB ()
	    {
	    	int latestSnapShotId = GetLatestSnapShotId();
	    	
	    	// Keep one snapshot for reference.
	    	latestSnapShotId = latestSnapShotId -1;
	    	
	    	try
			{
				PreparedStatement ps=con.prepareStatement("delete from tagsnapshot where snapshotid ="+latestSnapShotId);

				int rowsDeleted = ps.executeUpdate();
				
				logger.info(" Cleaned up snapshots"+ rowsDeleted + ":" + Thread.currentThread().getName()+":"+ getTime());
 				
				ps.close();
				
				
				ps=con.prepareStatement("delete from snapshotid where ids="+latestSnapShotId);				

				rowsDeleted = ps.executeUpdate();

				logger.info(" Cleaned up snapshot Ids "+ rowsDeleted + ":" + Thread.currentThread().getName()+":"+ getTime());
 				
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
	    	DBCleanThread dbCleanThread = new DBCleanThread ();
	    	dbCleanThread.start();
	    	
	    }
	  
	    

	}
