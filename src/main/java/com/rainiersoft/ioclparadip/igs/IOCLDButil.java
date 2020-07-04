package com.rainiersoft.ioclparadip.igs;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.rainiersoft.core.DBConnector;

public class IOCLDButil 
{
	DBConnector dbCon = null;
	Connection con = null;
	
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
			pinCodeObject.setPinCodeInfo_PresetValue(0);
			pinCodeObject.setPinCodeInfo_TruckNo( rs.getString(7));
			pinCodeObject.setPinCodeInfo_TruckNo_Len(rs.getString(7).length());
					
			pinCodeObject.setFanCreationOn(rs.getTimestamp(8));
			pinCodeObject.setFanExpirationOn(rs.getTimestamp(9));
	 
			pinCodeObject.setPinCodeInfo_TransporterName_Len(5);
			pinCodeObject.setPinCodeInfo_TransporterName("heheh");
			pinCodeObject.setPinCodeInfo_Capacity(12312.566f);
			pinCodeObject.setPinCodeInfo_Tare_Weight(7897.30f);
			pinCodeObject.setPinCodeInfo_TruckStatus(9);
			pinCodeObject.setPinCodeInfo_Location_Len(4);
			pinCodeObject.setPinCodeInfo_Location("Your");			
		}

		stmt.close();
		con.close();
		
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
				
		return  pinCodeObject;
	}

}
