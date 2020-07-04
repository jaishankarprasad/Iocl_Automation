package com.rainiersoft.ioclparadip.ftg;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;

import com.rainiersoft.core.DBConnector;

public class IOCLDButil 
{
	DBConnector dbCon = null;
	Connection con = null;
	final static Logger logger = Logger.getLogger(IOCLDButil.class);

	public static void main(String[] args) {
		
		IOCLDButil iocl=new IOCLDButil();
		iocl.callPinCodeValidation(5699,2);
	}
	public IOCLDButil ()
	{
		dbCon = DBConnector.getInstance();
	}
	
	public PinCodeInfo callPinCodeValidation (int fanPINNumber, int bayNum)
	{
 
		PinCodeInfo pinCodeObject = null;
			
		CallableStatement stmt = null;
		con = dbCon.getConnection();
		
			
		try
		{
	 
		stmt = con.prepareCall("{call PinValidation(?,?)}");
		
		stmt.setString(1, ""+fanPINNumber);
		stmt.setInt(2, bayNum);
	 
		ResultSet rs=stmt.executeQuery();
		
		while(rs.next()) {
			pinCodeObject = new PinCodeInfo();
			
			pinCodeObject.setPinCodeInfo_PinCode(fanPINNumber);
			pinCodeObject.setBayNumber(bayNum);
			pinCodeObject.setPinCodeInfo_PinCodStatus(rs.getInt(3));
			String fannum=rs.getString(4);
			System.out.println("FanNum"+fannum);
			pinCodeObject.setPinCodeInfo_FanNo(Long.parseLong(rs.getString(4)));
			pinCodeObject.setPinCodeInfo_PresetValue(123);
			pinCodeObject.setPinCodeInfo_TruckNo( rs.getString(7));
			pinCodeObject.setPinCodeInfo_TruckNo_Len(rs.getString(7).length());
					
			pinCodeObject.setFanCreationOn(rs.getTimestamp(8));
			pinCodeObject.setFanExpirationOn(rs.getTimestamp(9));
	 
			pinCodeObject.setPinCodeInfo_TransporterName_Len(rs.getString(10).length());
			pinCodeObject.setPinCodeInfo_TransporterName(rs.getString(10));
			pinCodeObject.setPinCodeInfo_Capacity(rs.getFloat(5));
			pinCodeObject.setPinCodeInfo_Tare_Weight(7897.30f);
			pinCodeObject.setPinCodeInfo_TruckStatus(9);
			pinCodeObject.setPinCodeInfo_Location_Len(rs.getString(11).length());
			pinCodeObject.setPinCodeInfo_Location(rs.getString(11));			
		}

		stmt.close();
		//con.close();
		
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			logger.error(e);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e);
		}
				
		return  pinCodeObject;
	}
	
	public boolean fanPinStatusUpdation (long fanNo, int fanPIN, int bayNum, String fanpinstatus, Timestamp bclastupdatedtimestamp)
	{
		// FanNo
		// FanPinStatus
		// Bay Num
		//
		boolean result = false;
			
		CallableStatement stmt = null;
		con = dbCon.getConnection();
		
		try
		{
	 
		stmt = con.prepareCall("{call FanPinStatusUpdation(?,?,?,?,?)}");
		
		stmt.setInt(1,bayNum);
		stmt.setLong(2,fanNo);
		stmt.setInt(3,fanPIN);
		stmt.setString(4, ""+fanpinstatus);
		stmt.setTimestamp(5, bclastupdatedtimestamp);
 	 
		stmt.executeQuery();
		
		stmt.close();
		//con.close();
		result=true;
		
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			logger.error(e);
			result=false;
		}
				
		return  result;
	}
	
	
	public boolean bayStatusUpdation (long fanNo, int fanPIN, int bayNum, String fanpinstatus, Date bcinputtimestamp,float totalizerstartvalue ,float totalizerendvalue ,float loadedquantity  )
	{
 
		boolean result = false;
			
		CallableStatement stmt = null;
		con = dbCon.getConnection();
				
		try
		{
	 
		stmt = con.prepareCall("{call BayStatusUpdation(?,?,?,?,?,?,?,?)}");
		
		stmt.setInt(1,bayNum);
		stmt.setLong(2,fanNo);
		stmt.setInt(3,fanPIN);
		stmt.setString(4, ""+fanpinstatus);
		stmt.setString(5,""+bcinputtimestamp);
		stmt.setFloat(6, totalizerstartvalue);
		stmt.setFloat(7, totalizerendvalue);
		stmt.setFloat(8, loadedquantity);
 	 
		stmt.executeQuery();
		
		stmt.close();
//		con.close();
		
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			logger.error(e);
		}
				
		return  result;
	}

}
