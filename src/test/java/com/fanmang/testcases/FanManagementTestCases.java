package com.fanmang.testcases;

import javax.ws.rs.core.Response;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rainiersoft.iocl.services.FanSlipManagementServices;

public class FanManagementTestCases
{
	public FanManagementTestCases() {}

	public static void main(String[] args) throws Exception
	{
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		System.out.println("sc::" + ac);

		FanSlipManagementServices fms=(FanSlipManagementServices)ac.getBean("fanSlipManagementServices");
		//Response fanCreationResp=fms.fanSlipGeneration("AP0J8080","Rahul","1121233211","Iocl","1000","10000","Hyderabad","HYD1","999999999",1,"Rahul","Rahul");
		//System.out.println("fanCreationResp::::::"+fanCreationResp.getEntity());
		
		Response allFanSlips=fms.getAllLatestFanslipsData("10-01-2018");
		System.out.println("All FanSlips........."+allFanSlips.getEntity());
		
		//Response allFanSlips=fms.bayReallocation(, driverName, driverLicNo, customer, quantity, vehicleWgt, destination, locationCode, mobileNumber, bayNum, contarctorName, createdBy, quantityID, permitExpDate, insuranceExpDate, qty)
		
		//Response reauth=fms.fanslipReauthorization(14,"Supervisor","REEEEEE");
	}
}