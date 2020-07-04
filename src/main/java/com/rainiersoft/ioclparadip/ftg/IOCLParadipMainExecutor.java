package com.rainiersoft.ioclparadip.ftg;

import org.apache.log4j.Logger;

import com.rainiersoft.core.Tag;

public class IOCLParadipMainExecutor
{
	
	final static Logger logger = Logger.getLogger(IOCLParadipMainExecutor.class);
	IEventStatusChangedListener bayStatusEvent; 
	
	public final String BAY_STATUSEVENT= "Status.Change";
	
	public final String bayPrefix ="Bay";
	
	public Tag tag;
	
	PollEvent pollEvent;
	
	ProducerThread produerThread = null;
	
	ConsumerThread consumerThread = null;
	
	DBThread dbThread = null;
	
	DBCleanThread dbCleanThread = null;
	
  public IOCLParadipMainExecutor ()
  {
	  
  }
  public void uncaughtException(Thread t, Throwable e)
  {
	logger.info(" Exception in the thread "+ e.getClass().getName() + ":" + e.getMessage());

     e.printStackTrace(System.out);
     
	if (t.getName().equalsIgnoreCase("PROCUDERTHREAD"))
	{
	     logger.info(" Re-starting producer thread.....: ");
		  produerThread = new ProducerThread ();
		  produerThread.start();
	}
	else if( t.getName().equalsIgnoreCase("CONSUMERTHREAD"))
	{
	     logger.info("Thread status: "+ t.getState());
		  consumerThread = new ConsumerThread ();
		  consumerThread.start();
	}
	else if (t.getName().equalsIgnoreCase("DBCLEANTHREAD"))
	{
	     logger.info("Thread status: "+ t.getState());
		  dbCleanThread = new DBCleanThread ();
		  dbCleanThread.start();
}

  }
  
  public void startThreads ()
  {
	  try
	  {
	  logger.info("Producer thread Starting.....");
	  
	  produerThread = new ProducerThread ();
	  produerThread.start();
	  
	  Thread.sleep(1000);
	  
	  logger.info("Consumer thread Starting.....");
	  
	  consumerThread = new ConsumerThread ();
	//  consumerThread.setDbItemMap(produerThread.getPlcItemMap());
	  consumerThread.start();
	  
	  logger.info("DB Clean thread Starting.....");
	  
	  dbCleanThread = new DBCleanThread ();
	  dbCleanThread.start();

	  }
	  catch (Exception e)
	  {
		  logger.info("IoclParadipMainExecutor Initialization failed.. ");
		  logger.error(e.getMessage()); 
		  e.printStackTrace();
		 
	  }
	  
	  
  }
 
  public static void main(String args[])
  {
		System.out.println("Starting to IoclParadipMainExecutor ....");
		
		logger.info("IoclParadipMainExecutor Starting.....");
		
		IOCLParadipMainExecutor mainThread = new IOCLParadipMainExecutor ();
		
		mainThread.startThreads ();

  }
}
