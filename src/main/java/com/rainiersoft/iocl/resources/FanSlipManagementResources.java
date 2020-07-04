package com.rainiersoft.iocl.resources;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rainiersoft.iocl.exception.IOCLWSException;
import com.rainiersoft.iocl.services.FanSlipManagementServices;
import com.rainiersoft.request.dto.RegenerationAndCancellationRequestBean;
import com.rainiersoft.request.dto.FanSlipMangRequestBean;


/**
 * This is the class for FanSlip Management Resources
 * @author RahulKumarPamidi
 */

@Path("/fanslipmanagement")
@Component
public class FanSlipManagementResources
{
	private static final Logger LOG = LoggerFactory.getLogger(FanSlipManagementResources.class);

	@Autowired
	FanSlipManagementServices fanSlipManagementServices;

	public FanSlipManagementResources() {}

	@Path("/fanslipgen")
	@RolesAllowed({"Admin","Super Admin","TTES Operator","Supervisor"})
	@POST
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public Response fanSlipGeneration(FanSlipMangRequestBean request) throws IOCLWSException
	{
		try
		{
			LOG.info("Entered into fanSlipGeneration resource class method........");
			String truckNo = request.getTruckNo();
			String driverName = request.getDriverName();
			String driverLicNo = request.getDriverLicNo();
			String customer = request.getCustomer();
			String quantity = request.getQuantity();
			int quantityID=request.getQuantityID();
			String vehicleWgt = request.getVehicleWgt();
			String destination = request.getDestination();
			String locationCode = request.getLocationCode();
			int bayNum = request.getBayNum();
			String mobileNumber = request.getMobileNumber();
			String contractorName=request.getContractorName();
			String createdBy=request.getFanCreatedBy();
			//String permitNo=request.getPermitNo();
			String permitExpDate=request.getPermitExpDate();
			String insuranceExpDate=request.getInsuranceExpDate();
			//String insuranceNo=request.getInsuranceNo();
			return fanSlipManagementServices.fanSlipGeneration(truckNo, driverName, driverLicNo, customer, quantity, vehicleWgt, destination, locationCode, mobileNumber, bayNum,contractorName,createdBy,quantityID,permitExpDate,insuranceExpDate,request.isInsuranceExpModifiedFlag(),request.isPermitExpModifiedFlag(),request.getPesoVal());
		}
		catch(IOCLWSException iOCLWSException)
		{
			LOG.info("Logging the occured exception in the resouce class fanSlipGeneration method........"+iOCLWSException);
			throw iOCLWSException;
		}
	}

	@Path("/getFanStaticData")
	@RolesAllowed({"Admin","Super Admin","TTES Operator","Supervisor"})
	@GET
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public Response getData() throws IOCLWSException
	{
		LOG.info("Entered into getData resource class method........");
		try
		{
			return fanSlipManagementServices.getFanStaticData();
		}
		catch(IOCLWSException iOCLWSException)
		{
			LOG.info("Logging the occured exception in the resouce class getData method........"+iOCLWSException);
			throw iOCLWSException;
		}
	}

	@Path("/getFanslipsDetails")
	@RolesAllowed({"Admin","Super Admin","TTES Operator","Supervisor"})
	@GET
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public Response getFanslipsDetails(@QueryParam("selectedDate") String selectedDate) throws IOCLWSException
	{
		LOG.info("Entered into getFanslipsDetails resource class method........");
		try
		{
			return fanSlipManagementServices.getAllLatestFanslipsData(selectedDate);
		}
		catch(IOCLWSException iOCLWSException)
		{
			LOG.info("Logging the occured exception in the resouce class getAllLatestFanslipsData method........"+iOCLWSException);
			throw iOCLWSException;
		}
	}

	@Path("/fanslipReGeneration")
	@RolesAllowed({"Admin","Super Admin","TTES Operator","Supervisor"})
	@POST
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public Response fanslipReGeneration(FanSlipMangRequestBean request) throws IOCLWSException
	{
		try
		{
			LOG.info("Entered into fanSlipReGeneration resource class method........");
			String truckNo = request.getTruckNo();
			String driverName = request.getDriverName();
			String driverLicNo = request.getDriverLicNo();
			String customer = request.getCustomer();
			String quantity = request.getQuantity();
			String vehicleWgt = request.getVehicleWgt();
			String destination = request.getDestination();
			String locationCode = request.getLocationCode();
			int bayNum = request.getBayNum();
			String mobileNumber = request.getMobileNumber();
			String contractorName=request.getContractorName();
			int fanId=request.getFanId();
			String userName=request.getFanCreatedBy();
			int quantityID=request.getQuantityID();
			String comments=request.getComments();
			String fanPin=request.getFanPin();
			String permitExpDate=request.getPermitExpDate();
			String insuranceExpDate=request.getInsuranceExpDate();
			return fanSlipManagementServices.fanslipReGeneration(fanId,truckNo, driverName, driverLicNo, customer, quantity, vehicleWgt, destination, locationCode, mobileNumber, bayNum,contractorName,userName,quantityID,comments,request.getRemainingQtyLeft(),fanPin,request.getUpdateBayNumFlag(),request.getPesoVal());
		}
		catch(IOCLWSException iOCLWSException)
		{
			LOG.info("Logging the occured exception in the resouce class fanSlipGeneration method........"+iOCLWSException);
			throw iOCLWSException;
		}
	}

	@Path("/fanslipCancellation")
	@RolesAllowed({"Admin","Super Admin","TTES Operator","Supervisor"})
	@PUT
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public Response fanslipCancellation(RegenerationAndCancellationRequestBean cancellationRequestBean) throws IOCLWSException
	{
		try
		{
			LOG.info("Entered into fanslipCancellation resource class method........");
			return fanSlipManagementServices.fanslipCancellation(cancellationRequestBean.getFanId(),cancellationRequestBean.getUserName(),cancellationRequestBean.getComments());
		}
		catch(IOCLWSException iOCLWSException)
		{
			LOG.info("Logging the occured exception in the resouce class fanSlipGeneration method........"+iOCLWSException);
			throw iOCLWSException;
		}
	}

	@Path("/fanslipReauthorizationOfPin")
	@RolesAllowed({"Admin","Super Admin","TTES Operator","Supervisor"})
	@POST
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public Response fanslipReauthorization(RegenerationAndCancellationRequestBean regenerationRequestBean) throws IOCLWSException
	{
		try
		{
			LOG.info("Entered into fanslipReauthorization resource class method........");
			return fanSlipManagementServices.fanslipReauthorization(regenerationRequestBean.getFanId(),regenerationRequestBean.getUserName(),regenerationRequestBean.getComments());
		}
		catch(IOCLWSException iOCLWSException)
		{
			LOG.info("Logging the occured exception in the resouce class fanslipReauthorization method........"+iOCLWSException);
			throw iOCLWSException;
		}
	}

	@Path("/stoppingBatch")
	@RolesAllowed({"Admin","Super Admin","TTES Operator","Supervisor"})
	@POST
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public Response stoppingBatch(RegenerationAndCancellationRequestBean regenerationRequestBean) throws IOCLWSException
	{
		try
		{
			LOG.info("Entered into fanslipReauthorization resource class method........");
			return fanSlipManagementServices.stoppingBatch(regenerationRequestBean.getFanId(),regenerationRequestBean.getUserName(),regenerationRequestBean.getComments());
		}
		catch(IOCLWSException iOCLWSException)
		{
			LOG.info("Logging the occured exception in the resouce class fanslipReauthorization method........"+iOCLWSException);
			throw iOCLWSException;
		}
	}

	@Path("/getComments")
	@RolesAllowed({"Admin","Super Admin","TTES Operator","Supervisor"})
	@GET
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public Response getComments(@QueryParam("Type") String type) throws IOCLWSException
	{
		try
		{
			LOG.info("Entered into getComments resource class method........");
			return fanSlipManagementServices.getComments(type);
		}
		catch(IOCLWSException iOCLWSException)
		{
			LOG.info("Logging the occured exception in the resouce class getComments method........"+iOCLWSException);
			throw iOCLWSException;
		}
	}

	@Path("/getTrucks")
	@RolesAllowed({"Admin","Super Admin","TTES Operator","Supervisor"})
	@GET
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public Response getTrucks() throws IOCLWSException
	{
		try
		{
			LOG.info("Entered into getComments resource class method........");
			return fanSlipManagementServices.getTrucks();
		}
		catch(IOCLWSException iOCLWSException)
		{
			LOG.info("Logging the occured exception in the resouce class getComments method........"+iOCLWSException);
			throw iOCLWSException;
		}
	}

	@Path("/getAutoFillDetails")
	@RolesAllowed({"Admin","Super Admin","TTES Operator","Supervisor"})
	@GET
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public Response getAutoFillDetails(@QueryParam("TruckNo") String truckNo) throws IOCLWSException
	{
		try
		{
			LOG.info("Entered into getAutoFillDetails resource class method........");
			return fanSlipManagementServices.getAutoFillDetails(truckNo);
		}
		catch(IOCLWSException iOCLWSException)
		{
			LOG.info("Logging the occured exception in the resouce class getComments method........"+iOCLWSException);
			throw iOCLWSException;
		}
	}

	@Path("/bayReallocation")
	//@RolesAllowed({"Admin", "Super Admin"})
	@PermitAll
	@POST
	@Consumes({"application/json"})
	@Produces({"application/json"})
	public Response bayReallocation(FanSlipMangRequestBean request) throws IOCLWSException
	{
		try
		{
			LOG.info("Entered into bayReallocation resource class method........");
			String truckNo = request.getTruckNo();
			String driverName = request.getDriverName();
			String driverLicNo = request.getDriverLicNo();
			String customer = request.getCustomer();
			String quantity = request.getQuantity();
			int quantityID=request.getQuantityID();
			String vehicleWgt = request.getVehicleWgt();
			String destination = request.getDestination();
			String locationCode = request.getLocationCode();
			int bayNum = request.getBayNum();
			String mobileNumber = request.getMobileNumber();
			String contractorName=request.getContractorName();
			String createdBy=request.getFanCreatedBy();
			//String permitNo=request.getPermitNo();
			String permitExpDate=request.getPermitExpDate();
			String insuranceExpDate=request.getInsuranceExpDate();
			//String insuranceNo=request.getInsuranceNo();
			String remainingQty=request.getRemainingQtyLeft();
			return fanSlipManagementServices.bayReallocation(truckNo, driverName, driverLicNo, customer, quantity, vehicleWgt, destination, locationCode, mobileNumber, bayNum,contractorName,createdBy,quantityID,permitExpDate,insuranceExpDate,remainingQty,request.isInsuranceExpModifiedFlag(),request.isPermitExpModifiedFlag(),request.getFanId(),request.getPesoVal());
		}
		catch(IOCLWSException iOCLWSException)
		{
			LOG.info("Logging the occured exception in the resouce class bayReallocation method........"+iOCLWSException);
			throw iOCLWSException;
		}
	}
}