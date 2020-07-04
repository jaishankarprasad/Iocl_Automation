package de.re.easymodbus.modbusclient;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;



public class StoreLogData 
{
	
	FileWriter fileWriter = null;
	private String filename = null;
	
	private static StoreLogData instance;

    	
	private StoreLogData ()
	{

	}		
	
     	
	public synchronized void Store(String message)
	{ 
		try {
			fileWriter = new FileWriter(filename,true);
			while (!new File(filename).canWrite());
				fileWriter.append(DateTime.getDateTimeStringMilliseconds() + " " +message+System.lineSeparator());
			
		} catch (IOException e) 
		{
			
			e.printStackTrace();
		}
		finally
		{
			
			if (fileWriter != null)
				try {
					fileWriter.close();;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}		
	}
	
	
	 	
	public void Store(String message, Exception e)
	{
		e.printStackTrace();
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String exceptionAsString = sw.toString();
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(filename,true);
			fileWriter.append(DateTime.getDateTimeStringMilliseconds() + " "+ message+" " +exceptionAsString+System.lineSeparator());
				
		} catch (IOException exc) 
		{		
			exc.printStackTrace();
		}
		finally
		{
			if (fileWriter != null)
				try {
					fileWriter.close();
					} catch (IOException exc) {
						// TODO Auto-generated catch block
						exc.printStackTrace();
					}
		}	
	}
	
	public void setFilename(String filename)
	{
		this.filename = filename;
	}
	

    /**
    * Returns the instance of the class (Singleton)
    */  
	public static synchronized StoreLogData getInstance ()
	{
		if (StoreLogData.instance == null) 
		{
			StoreLogData.instance = new StoreLogData ();
			
		}
		return StoreLogData.instance;
	}

	

}
