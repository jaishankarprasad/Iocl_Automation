package com.rainiersoft.iocl.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rainiersoft.iocl.dao.IOCLBCBayOperationsDAO;
import com.rainiersoft.iocl.dao.IOCLCommentsDAO;
import com.rainiersoft.iocl.dao.IOCLContractorDetailsDAO;
import com.rainiersoft.iocl.dao.IOCLFanslipDetailsDAO;
import com.rainiersoft.iocl.dao.IOCLLocationDetailsDAO;
import com.rainiersoft.iocl.dao.IOCLQuantityDetailsDAO;
import com.rainiersoft.iocl.dao.IOCLReallocationFanslipDetailsDAO;
import com.rainiersoft.iocl.dao.IOCLSupportedPinStatusDAO;
import com.rainiersoft.iocl.dao.IOCLTruckRegistrationDetailsDAO;
import com.rainiersoft.iocl.dao.IOCLUserDetailsDAO;
import com.rainiersoft.iocl.entity.IoclBcBayoperation;
import com.rainiersoft.iocl.entity.IoclComments;
import com.rainiersoft.iocl.entity.IoclContractorDetail;
import com.rainiersoft.iocl.entity.IoclFanslipDetail;
import com.rainiersoft.iocl.entity.IoclLocationDetail;
import com.rainiersoft.iocl.entity.IoclQuantitiesDetail;
import com.rainiersoft.iocl.entity.IoclReallocationFanSlipDetails;
import com.rainiersoft.iocl.entity.IoclSupportedPinstatus;
import com.rainiersoft.iocl.entity.IoclTruckregistrationDetail;
import com.rainiersoft.iocl.entity.IoclUserDetail;
import com.rainiersoft.iocl.exception.IOCLWSException;
import com.rainiersoft.iocl.util.CommonUtilites;
import com.rainiersoft.iocl.util.ErrorMessageConstants;
import com.rainiersoft.response.dto.AutofillDetailsResponse;
import com.rainiersoft.response.dto.FanPinCancellationResponseBean;
import com.rainiersoft.response.dto.FanPinCreatedResponse;
import com.rainiersoft.response.dto.FanReauthorizationResponseBean;
import com.rainiersoft.response.dto.GetAllLatestFanSlipsDataResponseBean;
import com.rainiersoft.response.dto.GetFanMangStaticDataResponseBean;
import com.rainiersoft.response.dto.StoppingBatchResponseBean;


/**
 * This is the class for FanSlipManagementServices
 * @author RahulKumarPamidi
 */

@Service
@Singleton
public class FanSlipManagementServices 
{
	private static final Logger LOG = LoggerFactory.getLogger(FanSlipManagementServices.class);

	@Autowired
	IOCLTruckRegistrationDetailsDAO iOCLTruckRegistrationDetailsDAO;

	@Autowired
	IOCLUserDetailsDAO iOCLUserDetailsDAO;

	@Autowired
	IOCLFanslipDetailsDAO ioclFanslipDetailsDAO;

	@Autowired
	IOCLReallocationFanslipDetailsDAO ioclReallocationFanslipDetailsDAO;

	@Autowired
	IOCLLocationDetailsDAO iOCLLocationDetailsDAO;

	@Autowired
	IOCLSupportedPinStatusDAO iOCLSupportedPinStatusDAO;

	@Autowired
	IOCLContractorDetailsDAO iOCLContractorDetailsDAO;

	@Autowired
	IOCLBCBayOperationsDAO iOCLBCBayOperationsDAO;

	@Autowired
	IOCLQuantityDetailsDAO iOCLQuantityDetailsDAO;

	@Autowired
	IOCLCommentsDAO iOCLCommentsDAO;

	@Autowired
	Properties appProps;


	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,rollbackFor=IOCLWSException.class)
	public Response fanSlipGeneration(String truckNo,String driverName,String driverLicNo,String customer,String quantity,String vehicleWgt,String destination,String locationCode,String mobileNumber,int bayNum,String contarctorName,String createdBy,int quantityID,String permitExpDate,String insuranceExpDate,boolean imodifiedFlag,boolean pModifiedFlag,String poesVal) throws IOCLWSException
	{
		try
		{
			FanPinCreatedResponse fanPinCreatedResponse=new FanPinCreatedResponse();

			//Check if the truck is there in truck table,If not insert truck details.  else get the truck id and insert new row in fan table
			int truckID=0;
			IoclTruckregistrationDetail ioclTruckregistrationDetail=iOCLTruckRegistrationDetailsDAO.findTruckByTruckNo(truckNo);

			//GET THE LOCATIONID and STATUSID
			//IoclLocationDetail ioclLocationDetail=iOCLLocationDetailsDAO.findLocationIdByLocationCode(locationCode);

			IoclLocationDetail ioclLocationDetail=iOCLLocationDetailsDAO.findLocationByLocationName(locationCode);
			IoclContractorDetail ioclContractorDetail=iOCLContractorDetailsDAO.findContractorByContractorName(contarctorName);
			IoclQuantitiesDetail ioclQuantitiesDetail=null;
			//IoclQuantitiesDetail ioclQuantitiesDetail=iOCLQuantityDetailsDAO.findQuantityByQuantityId(quantityID);

			DateFormat dformat=new SimpleDateFormat(appProps.getProperty("DatePickerFormat"));
			if(ioclTruckregistrationDetail==null)
			{	
				Date permitExp=dformat.parse(permitExpDate);
				Date insuranceExp=dformat.parse(insuranceExpDate);
				iOCLTruckRegistrationDetailsDAO.insertTruckregistrationDetail(truckNo, driverName, driverLicNo, customer, mobileNumber,permitExp,insuranceExp,ioclLocationDetail.getLocationID(),ioclContractorDetail.getContractorId(),0,poesVal);
				IoclTruckregistrationDetail ioclTruckDetails=iOCLTruckRegistrationDetailsDAO.findTruckByTruckNo(truckNo);
				if(null!=ioclTruckDetails)
				{
					truckID=ioclTruckDetails.getTruckId();
				}
				LOG.info("New Truck::::::::"+truckID);
			}
			else
			{
				Date permitExp=dformat.parse(permitExpDate);
				Date insuranceExp=dformat.parse(insuranceExpDate);
				IoclTruckregistrationDetail ioclTruckDetails=iOCLTruckRegistrationDetailsDAO.findTruckByTruckNo(truckNo);
				ioclTruckDetails.setPesoValue(poesVal);
				if(imodifiedFlag)
				{
					ioclTruckDetails.setInsuranceExpDate(insuranceExp);
				}
				if(pModifiedFlag)
				{
					ioclTruckDetails.setPermitExpDate(permitExp);
				}
				truckID=ioclTruckDetails.getTruckId();

				DateFormat dateFormat = new SimpleDateFormat(appProps.getProperty("AppDateFormat"));
				Date currentDate = new Date();
				String currDate=dateFormat.format(currentDate);
				LOG.info("Current Date:::::"+currDate);

				Calendar cal = Calendar.getInstance();
				cal.setTime(currentDate);
				cal.add(Calendar.HOUR, -24);
				Date hoursBack = cal.getTime();
				String pasteDate=dateFormat.format(hoursBack);
				LOG.info("PastDate::::::"+pasteDate);

				List<IoclFanslipDetail> listOfPastFanslips=ioclFanslipDetailsDAO.findAllLatestFanSlipsAndTrucks(currentDate,hoursBack,truckID);

				if(null!=listOfPastFanslips && listOfPastFanslips.size()>0)
				{
					if(listOfPastFanslips.get(0).getTruckId()==truckID)
					{
						if(!(listOfPastFanslips.get(0).getIoclSupportedPinstatus().getFanPinStatus().equalsIgnoreCase("Completed")))
						{
							if(!(listOfPastFanslips.get(0).getIoclSupportedPinstatus().getFanPinStatus().equalsIgnoreCase("Cancelled") || listOfPastFanslips.get(0).getIoclSupportedPinstatus().getFanPinStatus().equalsIgnoreCase("Expired") || listOfPastFanslips.get(0).getIoclSupportedPinstatus().getFanPinStatus().equalsIgnoreCase("PartiallyLoaded")))
							{
								throw new IOCLWSException(ErrorMessageConstants.Unprocessable_Entity_Code,ErrorMessageConstants.Truck_Inprogress);
							}
						}
					}
				}
				LOG.info("Existing Truck::::::::"+truckID);
			}

			if(truckID!=0)
			{
				int fanPin=0;
				while(true)
				{
					fanPin=CommonUtilites.pinGen();
					boolean flag=CommonUtilites.checkPinHasFourDigits(fanPin);
					if(flag)
					{
						break;
					}
				}

				DateFormat dateFormat = new SimpleDateFormat(appProps.getProperty("AppDateFormat"));
				Date createddateobj = new Date();

				Calendar cal = Calendar.getInstance();
				cal.setTime(createddateobj);
				cal.add(Calendar.HOUR, Integer.parseInt(appProps.getProperty("FanExpirationNumberOfHours")));
				Date fanExpirationTime = cal.getTime();
				LOG.info("fanExpirationTime::::::"+dateFormat.format(fanExpirationTime));

				IoclSupportedPinstatus ioclSupportedPinstatus=iOCLSupportedPinStatusDAO.findPinStatusIdByPinStatus(appProps.getProperty("FanCreatedStatus"));

				LOG.info("createdBy::::::"+createdBy);
				IoclUserDetail ioclUserDetail=iOCLUserDetailsDAO.findUserByUserName(createdBy);
				LOG.info("ioclUserDetail:::::::"+ioclUserDetail);
				int userID=ioclUserDetail.getUserId();

				LOG.info("usrId:::"+userID);
				if(null!=ioclLocationDetail && null!=ioclSupportedPinstatus && null!=ioclUserDetail)
				{
					Long fanId=ioclFanslipDetailsDAO.insertFanSlipDetails(bayNum, String.valueOf(fanPin),ioclSupportedPinstatus, truckID,createddateobj,quantity, vehicleWgt, destination, ioclLocationDetail,fanExpirationTime,ioclContractorDetail,userID,ioclQuantitiesDetail);
					fanPinCreatedResponse.setFanPin(String.valueOf(fanPin));
					fanPinCreatedResponse.setBayNum(bayNum);
					fanPinCreatedResponse.setQuantity(quantity);
					fanPinCreatedResponse.setContractorName(contarctorName);
					//fanPinCreatedResponse.setDestination(destination);
					fanPinCreatedResponse.setFanId(fanId);
					fanPinCreatedResponse.setFanPinCreation(dateFormat.format(createddateobj));
					fanPinCreatedResponse.setFanPinExpiration(dateFormat.format(fanExpirationTime));
					fanPinCreatedResponse.setFanPinStatus(appProps.getProperty("FanCreatedStatus"));
					fanPinCreatedResponse.setLocationCode(locationCode);
					fanPinCreatedResponse.setQuantity(quantity);
					fanPinCreatedResponse.setQuantityID(quantityID);
					fanPinCreatedResponse.setTruckNumber(truckNo);
					//fanPinCreatedResponse.setDriverName(driverName);
					//fanPinCreatedResponse.setCustomer(customer);
					fanPinCreatedResponse.setVehicleWeight(vehicleWgt);
				}
			}
			return Response.status(Response.Status.OK).entity(fanPinCreatedResponse).build();
		}
		catch (IOCLWSException iOCLWSException) 
		{
			throw iOCLWSException;
		}
		catch(Exception exception)
		{
			LOG.info("Logging the occured exception in the service class fanSlipGeneration method catch block........"+exception);
			throw new IOCLWSException(ErrorMessageConstants.Unprocessable_Entity_Code,ErrorMessageConstants.Internal_Error);
		}	
	}

	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,rollbackFor=IOCLWSException.class)
	public Response getFanStaticData() throws IOCLWSException
	{
		LOG.info("Entered into getFanStaticData service class method........");
		try
		{
			GetFanMangStaticDataResponseBean getFanMangStaticDataResponseBean=new GetFanMangStaticDataResponseBean();
			Map<String,List<String>> data=new HashMap<String,List<String>>();
			Map<String,Map<Integer,String>> quantityData=new HashMap<String,Map<Integer,String>>();

			List<IoclContractorDetail> lIoclContractorDetail=iOCLContractorDetailsDAO.findAllContractorNames();
			LOG.info("Got the lIoclContractorDetail object......"+lIoclContractorDetail);

			List<IoclLocationDetail> lIoclLocationDetail=iOCLLocationDetailsDAO.findAllLocationCodes();
			LOG.info("Got the lIoclLocationDetail object......"+lIoclLocationDetail);

			List<IoclQuantitiesDetail> lIoclQuantitiesDetail=iOCLQuantityDetailsDAO.findAllQuantites();
			LOG.info("Got the lIoclQuantitiesDetail object......"+lIoclQuantitiesDetail);

			Set<String> setContractorNames=new HashSet<String>();
			Set<String> setLocationCodes=new HashSet<String>();
			Map<Integer,String> setQuantites=new HashMap<Integer,String>();
			for(IoclContractorDetail ioclContractorDetail:lIoclContractorDetail)
			{
				if((!ioclContractorDetail.getIoclSupportedContractorstatus().equals(appProps.getProperty("ContractorSupportedInActiveStatus"))))
					setContractorNames.add(ioclContractorDetail.getContractorName());
			}
			for(IoclLocationDetail ioclLocationDetail:lIoclLocationDetail)
			{
				if((!ioclLocationDetail.getIoclSupportedLocationstatus().equals(appProps.getProperty("LocationSupportedInActiveStatus"))))
					setLocationCodes.add(ioclLocationDetail.getLocationName());
			}
			for(IoclQuantitiesDetail ioclQuantitiesDetail:lIoclQuantitiesDetail)
			{
				if((!ioclQuantitiesDetail.getIoclSupportedQuantitystatus().equals(appProps.getProperty("LocationSupportedInActiveStatus"))))
					setQuantites.put(ioclQuantitiesDetail.getQuantityId(),ioclQuantitiesDetail.getQuantityName()+"("+ioclQuantitiesDetail.getQuantity()+")");
			}
			List<String> contractorNames=new ArrayList<String>(setContractorNames);
			List<String> locationCodes=new ArrayList<String>(setLocationCodes);

			data.put("ContractorNames",contractorNames);
			data.put("LocationCodes",locationCodes);
			quantityData.put("Quantity", setQuantites);
			getFanMangStaticDataResponseBean.setData(data);
			getFanMangStaticDataResponseBean.setQuantitesData(quantityData);
			LOG.info("getFanStaticData response object......."+getFanMangStaticDataResponseBean);
			return  Response.status(Response.Status.OK).entity(getFanMangStaticDataResponseBean).build();
		}
		catch (Exception exception) 
		{
			LOG.info("Logging the occured exception in the service class getFanStaticData method catch block........"+exception);
			throw new IOCLWSException(ErrorMessageConstants.Unprocessable_Entity_Code,ErrorMessageConstants.Internal_Error);
		}
	}

	/**
	 * @param selectedDate
	 * @return
	 * @throws IOCLWSException
	 */
	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,rollbackFor=IOCLWSException.class)
	public Response getAllLatestFanslipsData(String selectedDate) throws IOCLWSException
	{
		LOG.info("Entered into getAllLatestFanslipsData service class method........");
		try
		{
			List<GetAllLatestFanSlipsDataResponseBean> listGetAllLatestFanSlipsDataResponseBean=new ArrayList<GetAllLatestFanSlipsDataResponseBean>();

			DateFormat dateFormat = new SimpleDateFormat(appProps.getProperty("DatePickerFormat"));
			Date selDate=(Date)dateFormat.parse(selectedDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(selDate);
			LOG.info("PastFanSlipDetails Configured Value....."+appProps.getProperty("NumberOfHoursPastFanSlipDetails"));
			cal.add(Calendar.HOUR, Integer.parseInt(appProps.getProperty("NumberOfHoursPastFanSlipDetails")));
			Date hoursBack = cal.getTime();
			LOG.info("PastDate......."+dateFormat.format(hoursBack));

			cal.setTime(selDate);
			cal.add(Calendar.HOUR,Integer.parseInt(appProps.getProperty("GetAllLatestFanslipsData")));
			Date selectDateWithTime = cal.getTime();

			LOG.info("selectDateWithTime....."+dateFormat.format(selectDateWithTime));

			List<IoclFanslipDetail> listOfPastFanslips=ioclFanslipDetailsDAO.findAllLatestFanSlips(selectDateWithTime,hoursBack);

			LOG.info("listOfPastFanslips......."+listOfPastFanslips);

			List<String> reallocatedList=new ArrayList<String>(); 

			for(IoclFanslipDetail ioclFanslipDetail:listOfPastFanslips)
			{
				GetAllLatestFanSlipsDataResponseBean getAllLatestFanSlipsDataResponseBean=new GetAllLatestFanSlipsDataResponseBean();
				getAllLatestFanSlipsDataResponseBean.setBayNum(ioclFanslipDetail.getBayNo());
				getAllLatestFanSlipsDataResponseBean.setContractorName(ioclFanslipDetail.getIoclContractorDetail().getContractorName());
				//getAllLatestFanSlipsDataResponseBean.setDestination(ioclFanslipDetail.getDestination());
				getAllLatestFanSlipsDataResponseBean.setFanId(ioclFanslipDetail.getFanId());
				getAllLatestFanSlipsDataResponseBean.setFanPin(ioclFanslipDetail.getFanPin());
				getAllLatestFanSlipsDataResponseBean.setFanPinCreation(new SimpleDateFormat(appProps.getProperty("AppDateFormat")).format(ioclFanslipDetail.getFanCreationOn()));
				getAllLatestFanSlipsDataResponseBean.setFanPinExpiration(new SimpleDateFormat(appProps.getProperty("AppDateFormat")).format(ioclFanslipDetail.getFanExpirationOn()));
				getAllLatestFanSlipsDataResponseBean.setFanPinStatus(ioclFanslipDetail.getIoclSupportedPinstatus().getFanPinStatus());
				if(appProps.getProperty("FanExpiredStatus").toString().equals(ioclFanslipDetail.getIoclSupportedPinstatus().getFanPinStatus()))
				{
					getAllLatestFanSlipsDataResponseBean.setFanStatusExpired(true);
				}
				else
				{
					getAllLatestFanSlipsDataResponseBean.setFanStatusExpired(false);
				}
				getAllLatestFanSlipsDataResponseBean.setLocationCode(ioclFanslipDetail.getIoclLocationDetail().getLocationName());
				//getAllLatestFanSlipsDataResponseBean.setQuantity(ioclFanslipDetail.getIoclQuantitiesDetail().getQuantityName()+"("+ioclFanslipDetail.getIoclQuantitiesDetail().getQuantity()+")");
				IoclTruckregistrationDetail ioclTruckDetails=iOCLTruckRegistrationDetailsDAO.findTruckByTruckId(ioclFanslipDetail.getTruckId());
				getAllLatestFanSlipsDataResponseBean.setTruckNumber(ioclTruckDetails.getTruckNo());
				if(null!=ioclTruckDetails.getPesoValue())
					getAllLatestFanSlipsDataResponseBean.setPoesVal(ioclTruckDetails.getPesoValue());
				else
					getAllLatestFanSlipsDataResponseBean.setPoesVal("");
				//getAllLatestFanSlipsDataResponseBean.setPermitNo(ioclTruckDetails.getPermitNo());
				//getAllLatestFanSlipsDataResponseBean.setInsuranceNo(ioclTruckDetails.getInsuranceNo());
				if(null!=ioclTruckDetails.getInsuranceExpDate())
				{
					getAllLatestFanSlipsDataResponseBean.setInsuranceExpDate(new SimpleDateFormat(appProps.getProperty("DatePickerFormat")).format(ioclTruckDetails.getInsuranceExpDate()));
				}
				if(null!=ioclTruckDetails.getPermitExpDate())
				{
					getAllLatestFanSlipsDataResponseBean.setPermitExpDate(new SimpleDateFormat(appProps.getProperty("DatePickerFormat")).format(ioclTruckDetails.getPermitExpDate()));
				}
				//getAllLatestFanSlipsDataResponseBean.setDriverName(ioclTruckDetails.getDriverName());
				//getAllLatestFanSlipsDataResponseBean.setCustomer(ioclTruckDetails.getCustomer());
				//getAllLatestFanSlipsDataResponseBean.setDriverLicenceNumber(ioclTruckDetails.getDriverLicNo());
				getAllLatestFanSlipsDataResponseBean.setVehicleWeight(ioclFanslipDetail.getVehicleWgt());
				//getAllLatestFanSlipsDataResponseBean.setQuantityID(ioclFanslipDetail.getIoclQuantitiesDetail().getQuantityId());
				getAllLatestFanSlipsDataResponseBean.setPreSet(ioclFanslipDetail.getQuantity());
				getAllLatestFanSlipsDataResponseBean.setComments(ioclFanslipDetail.getComments());
				if(null!=ioclFanslipDetail.getBccompletedtime())
					getAllLatestFanSlipsDataResponseBean.setBatchEndTime(new SimpleDateFormat(appProps.getProperty("AppDateFormat")).format(ioclFanslipDetail.getBccompletedtime()));
				if(null!=ioclFanslipDetail.getBcinputtime())
					getAllLatestFanSlipsDataResponseBean.setBatchStartTime(new SimpleDateFormat(appProps.getProperty("AppDateFormat")).format(ioclFanslipDetail.getBcinputtime()));

				//List<IoclBcBayoperation> listOfBCUpdates=iOCLBCBayOperationsDAO.findBayUpdatesByFanPin(ioclFanslipDetail.getFanPin());

				List<IoclBcBayoperation> listOfBCUpdates=iOCLBCBayOperationsDAO.findBayUpdatesByFanId(String.valueOf(ioclFanslipDetail.getFanId()));

				LOG.info("findBayUpdatesByFanPin::::::::"+listOfBCUpdates);
				//Fpin is generated but truck has not reached the point, so the BC table might not contain records for the truck.
				if(listOfBCUpdates.size()==0)
				{
					getAllLatestFanSlipsDataResponseBean.setBayStatus(appProps.getProperty("NOT_REACHED"));
				}
				else
				{
					if(ioclFanslipDetail.getIoclSupportedPinstatus().getFanPinStatus().equalsIgnoreCase("PartiallyLoaded"))
					{
						reallocatedList.add(ioclFanslipDetail.getFanPin());	
						getAllLatestFanSlipsDataResponseBean.setBayStatus(listOfBCUpdates.get(0).getIoclSupportedBayoperationalstatus().getOperationalStatus());
						if(null!=listOfBCUpdates.get(0).getLoadedQuantity())
							getAllLatestFanSlipsDataResponseBean.setLoadedQuantity(listOfBCUpdates.get(0).getLoadedQuantity());
					}
					else
					{
						if(reallocatedList.contains(ioclFanslipDetail.getFanPin()))
						{
							if(ioclFanslipDetail.getIoclSupportedPinstatus().getFanPinStatus().equalsIgnoreCase("Created"))
							{
								getAllLatestFanSlipsDataResponseBean.setBayStatus(appProps.getProperty("NOT_REACHED"));
								if(null!=listOfBCUpdates.get(0).getLoadedQuantity())
									getAllLatestFanSlipsDataResponseBean.setLoadedQuantity("");
							}
							else
							{
								getAllLatestFanSlipsDataResponseBean.setBayStatus(listOfBCUpdates.get(0).getIoclSupportedBayoperationalstatus().getOperationalStatus());
								if(null!=listOfBCUpdates.get(0).getLoadedQuantity())
									getAllLatestFanSlipsDataResponseBean.setLoadedQuantity(listOfBCUpdates.get(0).getLoadedQuantity());
							}
						}
						else
						{
							getAllLatestFanSlipsDataResponseBean.setBayStatus(listOfBCUpdates.get(0).getIoclSupportedBayoperationalstatus().getOperationalStatus());
							if(null!=listOfBCUpdates.get(0).getLoadedQuantity())
								getAllLatestFanSlipsDataResponseBean.setLoadedQuantity(listOfBCUpdates.get(0).getLoadedQuantity());
						}
					}
				}

				//Check if the data is there in reallocation table
				List<IoclReallocationFanSlipDetails> listIoclReallocationFanSlipDetails=ioclReallocationFanslipDetailsDAO.findFanDetailsByFanId(ioclFanslipDetail.getFanId());
				if(null!=listIoclReallocationFanSlipDetails && listIoclReallocationFanSlipDetails.size()>0)
				{
					for(IoclReallocationFanSlipDetails ioclReallocationFanSlipDetails:listIoclReallocationFanSlipDetails)
					{
						GetAllLatestFanSlipsDataResponseBean getallLatestFanSlipsDataResponseBean=new GetAllLatestFanSlipsDataResponseBean();
						getallLatestFanSlipsDataResponseBean.setBayNum(ioclReallocationFanSlipDetails.getBayNo());
						getallLatestFanSlipsDataResponseBean.setContractorName(ioclReallocationFanSlipDetails.getIoclContractorDetail().getContractorName());
						//getallLatestFanSlipsDataResponseBean.setDestination(ioclReallocationFanSlipDetails.getDestination());
						getallLatestFanSlipsDataResponseBean.setFanId(ioclReallocationFanSlipDetails.getFanId());
						getallLatestFanSlipsDataResponseBean.setFanPin(ioclReallocationFanSlipDetails.getFanPin());
						getallLatestFanSlipsDataResponseBean.setFanPinCreation(new SimpleDateFormat(appProps.getProperty("AppDateFormat")).format(ioclReallocationFanSlipDetails.getFanCreationOn()));
						getallLatestFanSlipsDataResponseBean.setFanPinExpiration(new SimpleDateFormat(appProps.getProperty("AppDateFormat")).format(ioclReallocationFanSlipDetails.getFanExpirationOn()));
						getallLatestFanSlipsDataResponseBean.setFanPinStatus(ioclReallocationFanSlipDetails.getIoclSupportedPinstatus().getFanPinStatus());
						if(appProps.getProperty("FanExpiredStatus").toString().equals(ioclReallocationFanSlipDetails.getIoclSupportedPinstatus().getFanPinStatus()))
						{
							getallLatestFanSlipsDataResponseBean.setFanStatusExpired(true);
						}
						else
						{
							getallLatestFanSlipsDataResponseBean.setFanStatusExpired(false);
						}
						getallLatestFanSlipsDataResponseBean.setLocationCode(ioclReallocationFanSlipDetails.getIoclLocationDetail().getLocationName());
						getallLatestFanSlipsDataResponseBean.setQuantity(ioclReallocationFanSlipDetails.getIoclQuantitiesDetail().getQuantityName()+"("+ioclReallocationFanSlipDetails.getIoclQuantitiesDetail().getQuantity()+")");
						IoclTruckregistrationDetail iocltruckDetails=iOCLTruckRegistrationDetailsDAO.findTruckByTruckId(ioclReallocationFanSlipDetails.getTruckId());
						getallLatestFanSlipsDataResponseBean.setTruckNumber(iocltruckDetails.getTruckNo());
						//getallLatestFanSlipsDataResponseBean.setPermitNo(ioclTruckDetails.getPermitNo());
						//getallLatestFanSlipsDataResponseBean.setInsuranceNo(ioclTruckDetails.getInsuranceNo());
						if(null!=ioclTruckDetails.getInsuranceExpDate())
						{
							getallLatestFanSlipsDataResponseBean.setInsuranceExpDate(new SimpleDateFormat(appProps.getProperty("DatePickerFormat")).format(iocltruckDetails.getInsuranceExpDate()));
						}
						if(null!=ioclTruckDetails.getPermitExpDate())
						{
							getallLatestFanSlipsDataResponseBean.setPermitExpDate(new SimpleDateFormat(appProps.getProperty("DatePickerFormat")).format(iocltruckDetails.getPermitExpDate()));
						}
						//getallLatestFanSlipsDataResponseBean.setDriverName(ioclTruckDetails.getDriverName());
						//getallLatestFanSlipsDataResponseBean.setCustomer(ioclTruckDetails.getCustomer());
						//getallLatestFanSlipsDataResponseBean.setDriverLicenceNumber(ioclTruckDetails.getDriverLicNo());
						getallLatestFanSlipsDataResponseBean.setVehicleWeight(ioclReallocationFanSlipDetails.getVehicleWgt());
						getallLatestFanSlipsDataResponseBean.setQuantityID(ioclReallocationFanSlipDetails.getIoclQuantitiesDetail().getQuantityId());
						getallLatestFanSlipsDataResponseBean.setPreSet(ioclReallocationFanSlipDetails.getQuantity());
						getallLatestFanSlipsDataResponseBean.setComments(ioclReallocationFanSlipDetails.getComments());
						if(null!=ioclReallocationFanSlipDetails.getBccompletedtime())
							getallLatestFanSlipsDataResponseBean.setBatchEndTime(new SimpleDateFormat(appProps.getProperty("AppDateFormat")).format(ioclReallocationFanSlipDetails.getBccompletedtime()));
						if(null!=ioclReallocationFanSlipDetails.getBcinputtime())
							getallLatestFanSlipsDataResponseBean.setBatchStartTime(new SimpleDateFormat(appProps.getProperty("AppDateFormat")).format(ioclReallocationFanSlipDetails.getBcinputtime()));

						//List<IoclBcBayoperation> listBCUpdates=iOCLBCBayOperationsDAO.findBayUpdatesByFanPin(ioclReallocationFanSlipDetails.getFanPin());

						List<IoclBcBayoperation> listBCUpdates=iOCLBCBayOperationsDAO.findBayUpdatesByFanId(String.valueOf(ioclReallocationFanSlipDetails.getFanId()));

						LOG.info("findBayUpdatesByFanPin::::::::"+listBCUpdates);
						//Fpin is generated but truck has not reached the point, so the BC table might not contain records for the truck.
						if(listBCUpdates.size()==0)
						{
							getallLatestFanSlipsDataResponseBean.setBayStatus(appProps.getProperty("NOT_REACHED"));
						}
						else
						{
							getallLatestFanSlipsDataResponseBean.setBayStatus(listBCUpdates.get(0).getIoclSupportedBayoperationalstatus().getOperationalStatus());
							if(null!=listBCUpdates.get(0).getLoadedQuantity())
								getallLatestFanSlipsDataResponseBean.setLoadedQuantity(listBCUpdates.get(0).getLoadedQuantity());
						}
						listGetAllLatestFanSlipsDataResponseBean.add(getallLatestFanSlipsDataResponseBean);
					}
				}
				listGetAllLatestFanSlipsDataResponseBean.add(getAllLatestFanSlipsDataResponseBean);
			}
			LOG.info("getAllLatestFanslipsData response object......."+listGetAllLatestFanSlipsDataResponseBean);
			return  Response.status(Response.Status.OK).entity(listGetAllLatestFanSlipsDataResponseBean).build();
		}
		catch (Exception exception) 
		{
			LOG.info("Logging the occured exception in the service class getAllLatestFanslipsData method catch block........"+exception);
			throw new IOCLWSException(ErrorMessageConstants.Unprocessable_Entity_Code,ErrorMessageConstants.Internal_Error);
		}
	}

	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,rollbackFor=IOCLWSException.class)
	public Response fanslipReGeneration(int fanId,String truckNo,String driverName,String driverLicNo,String customer,String quantity,String vehicleWgt,String destination,String locationCode,String mobileNumber,int bayNum,String contarctorName,String userName,int quantityID,String comments,String remainingQtyLeft,String fanPin,boolean updateBayNumFlag,String poesVal) throws IOCLWSException
	{
		try
		{
			FanPinCreatedResponse fanPinCreatedResponse=new FanPinCreatedResponse();

			IoclSupportedPinstatus ioclSupportedCancelPinstatus=iOCLSupportedPinStatusDAO.findPinStatusIdByPinStatus("PartiallyLoaded");

			IoclFanslipDetail ioclFanslipDetail=ioclFanslipDetailsDAO.findFanPinByFanId(fanId);


			DateFormat dateFormat = new SimpleDateFormat(appProps.getProperty("AppDateFormat"));
			Date updateTime = new Date();

			LOG.info("createdBy::::::"+userName);
			IoclUserDetail ioclUserDetail=iOCLUserDetailsDAO.findUserByUserName(userName);
			int userID=ioclUserDetail.getUserId();
			LOG.info("usrId:::"+userID);

			//Set the fan pin status to cancel
			ioclFanslipDetailsDAO.updateFanPinDetails(ioclFanslipDetail, ioclSupportedCancelPinstatus,userID,updateTime,comments);

			//Check if the truck is there in truck table,If not insert truck details.  else get the truck id and insert new row in fan table
			IoclTruckregistrationDetail ioclTruckDetails=iOCLTruckRegistrationDetailsDAO.findTruckByTruckNo(truckNo);
			int truckID=ioclTruckDetails.getTruckId();

			/*int fanPin=0;
			while(true)
			{
				fanPin=CommonUtilites.pinGen();
				boolean flag=CommonUtilites.checkPinHasFourDigits(fanPin);
				if(flag)
				{
					break;
				}
			}*/

			//DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date createddateobj = new Date();

			Calendar cal = Calendar.getInstance();
			cal.setTime(createddateobj);
			cal.add(Calendar.HOUR, Integer.parseInt(appProps.getProperty("FanExpirationNumberOfHours")));
			Date fanExpirationTime = cal.getTime();

			//GET THE LOCATIONID and STATUSID
			//IoclLocationDetail ioclLocationDetail=iOCLLocationDetailsDAO.findLocationIdByLocationCode(locationCode);
			IoclLocationDetail ioclLocationDetail=iOCLLocationDetailsDAO.findLocationByLocationName(locationCode);

			IoclContractorDetail ioclContractorDetail=iOCLContractorDetailsDAO.findContractorByContractorName(contarctorName);

			IoclSupportedPinstatus ioclSupportedPinstatus=iOCLSupportedPinStatusDAO.findPinStatusIdByPinStatus(appProps.getProperty("FanCreatedStatus"));

			IoclQuantitiesDetail ioclQuantitiesDetail=null;
			//IoclQuantitiesDetail ioclQuantitiesDetail=iOCLQuantityDetailsDAO.findQuantityByQuantityId(quantityID);



			if(null!=ioclLocationDetail && null!=ioclSupportedPinstatus && null!=ioclUserDetail)
			{
				if(updateBayNumFlag)
				{
					Long latestFanId=ioclFanslipDetailsDAO.insertReallocationFanSlipDetails(bayNum, String.valueOf(fanPin),ioclSupportedPinstatus, truckID,createddateobj,quantity, vehicleWgt, destination, ioclLocationDetail,fanExpirationTime,ioclContractorDetail,userID,ioclQuantitiesDetail,remainingQtyLeft);
				}
				else
				{
					ioclFanslipDetail.setBayNo(bayNum);
					ioclFanslipDetailsDAO.updateFanPinDetails(ioclFanslipDetail,ioclSupportedPinstatus,userID,updateTime,"");
				}
				fanPinCreatedResponse.setFanPin(String.valueOf(fanPin));
				fanPinCreatedResponse.setBayNum(bayNum);
				fanPinCreatedResponse.setQuantity(quantity);
				fanPinCreatedResponse.setContractorName(contarctorName);
				//fanPinCreatedResponse.setDestination(destination);
				//fanPinCreatedResponse.setFanId(latestFanId);
				fanPinCreatedResponse.setFanPinCreation(dateFormat.format(createddateobj));
				fanPinCreatedResponse.setFanPinExpiration(dateFormat.format(fanExpirationTime));
				fanPinCreatedResponse.setFanPinStatus(appProps.getProperty("FanCreatedStatus"));
				fanPinCreatedResponse.setLocationCode(locationCode);
				fanPinCreatedResponse.setQuantity(quantity);
				fanPinCreatedResponse.setQuantityID(quantityID);
				fanPinCreatedResponse.setTruckNumber(truckNo);
				//fanPinCreatedResponse.setDriverName(driverName);
				//fanPinCreatedResponse.setCustomer(customer);
				fanPinCreatedResponse.setVehicleWeight(vehicleWgt);
			}
			return Response.status(Response.Status.OK).entity(fanPinCreatedResponse).build();
		}
		catch(Exception exception)
		{
			LOG.info("Logging the occured exception in the service class fanslipReGeneration method catch block........"+exception);
			throw new IOCLWSException(ErrorMessageConstants.Unprocessable_Entity_Code,ErrorMessageConstants.Internal_Error);
		}	
	}

	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,rollbackFor=IOCLWSException.class)
	public Response fanslipCancellation(int fanId,String userName,String comments) throws IOCLWSException
	{
		LOG.info("Entered into fanslipCancellation service class method........");
		try
		{
			FanPinCancellationResponseBean fanPinCancellationResponseBean=new FanPinCancellationResponseBean();

			IoclSupportedPinstatus ioclSupportedCancelPinstatus=iOCLSupportedPinStatusDAO.findPinStatusIdByPinStatus(appProps.getProperty("FanCancelledStatus"));

			IoclFanslipDetail ioclFanslipDetail=ioclFanslipDetailsDAO.findFanPinByFanId(fanId);

			IoclUserDetail ioclUserDetail=iOCLUserDetailsDAO.findUserByUserName(userName);
			int userID=ioclUserDetail.getUserId();

			DateFormat dateFormat = new SimpleDateFormat(appProps.getProperty("AppDateFormat"));
			Date updateTime = new Date();

			//Set the fan pin status to cancel
			ioclFanslipDetailsDAO.updateFanPinDetails(ioclFanslipDetail, ioclSupportedCancelPinstatus,userID,updateTime,comments);

			fanPinCancellationResponseBean.setMessage("SuccessFully Cancelled");
			fanPinCancellationResponseBean.setSuccessFlag(true);

			return  Response.status(Response.Status.OK).entity(fanPinCancellationResponseBean).build();
		}
		catch (Exception exception) 
		{
			LOG.info("Logging the occured exception in the service class fanslipCancellation method catch block........"+exception);
			throw new IOCLWSException(ErrorMessageConstants.Unprocessable_Entity_Code,ErrorMessageConstants.Internal_Error);
		}
	}

	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,rollbackFor=IOCLWSException.class)
	public Response fanslipReauthorization(int fanId,String userName,String comments) throws IOCLWSException
	{
		LOG.info("Entered into fanslipReauthorization service class method........");
		try
		{
			FanReauthorizationResponseBean fanReauthorizationResponseBean=new FanReauthorizationResponseBean();
			IoclFanslipDetail ioclFanslipDetail=ioclFanslipDetailsDAO.findFanPinByFanId(fanId);

			IoclUserDetail ioclUserDetail=iOCLUserDetailsDAO.findUserByUserName(userName);
			int userID=ioclUserDetail.getUserId();

			if(ioclFanslipDetail.getIoclSupportedPinstatus().getFanPinStatus().equalsIgnoreCase(appProps.getProperty("FanExpiredStatus")))
			{
				DateFormat dateFormat = new SimpleDateFormat(appProps.getProperty("AppDateFormat"));
				Date createddateobj = new Date();

				Calendar cal = Calendar.getInstance();
				cal.setTime(createddateobj);
				cal.add(Calendar.HOUR, Integer.parseInt(appProps.getProperty("FanExpirationNumberOfHours")));
				Date fanExpirationTime = cal.getTime();
				LOG.info("fanExpirationTime::::::"+dateFormat.format(fanExpirationTime));

				IoclSupportedPinstatus ioclSupportedCreatedPinstatus=iOCLSupportedPinStatusDAO.findPinStatusIdByPinStatus(appProps.getProperty("FanCreatedStatus"));

				ioclFanslipDetailsDAO.updateFanpinExpirationTime(ioclFanslipDetail, ioclSupportedCreatedPinstatus, userID, createddateobj,fanExpirationTime,comments);

				fanReauthorizationResponseBean.setFlag(true);
				fanReauthorizationResponseBean.setMessage("Successfully Reauthorized!!!");
			}
			else
			{
				fanReauthorizationResponseBean.setFlag(false);
				fanReauthorizationResponseBean.setMessage("Not Successfully Reauthorized!!!");
			}
			return  Response.status(Response.Status.OK).entity(fanReauthorizationResponseBean).build();
		}
		catch (Exception exception) 
		{
			LOG.info("Logging the occured exception in the service class fanslipReauthorization method catch block........"+exception);
			throw new IOCLWSException(ErrorMessageConstants.Unprocessable_Entity_Code,ErrorMessageConstants.Internal_Error);
		}
	}

	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,rollbackFor=IOCLWSException.class)
	public Response stoppingBatch(int fanId,String userName,String comments) throws IOCLWSException
	{
		LOG.info("Entered into stoppingBatch service class method........");
		try
		{
			StoppingBatchResponseBean stoppingBatchResponseBean=new StoppingBatchResponseBean();
			IoclFanslipDetail ioclFanslipDetail=ioclFanslipDetailsDAO.findFanPinByFanId(fanId);

			IoclSupportedPinstatus ioclSupportedPinstatus=iOCLSupportedPinStatusDAO.findPinStatusIdByPinStatus("AbortInitiated");

			IoclUserDetail ioclUserDetail=iOCLUserDetailsDAO.findUserByUserName(userName);
			int userID=ioclUserDetail.getUserId();

			DateFormat dateFormat = new SimpleDateFormat(appProps.getProperty("AppDateFormat"));
			Date updatedOn = new Date();

			ioclFanslipDetailsDAO.updateFanPinDetails(ioclFanslipDetail, ioclSupportedPinstatus, userID, updatedOn,comments);

			stoppingBatchResponseBean.setFlag(true);
			stoppingBatchResponseBean.setMessage("Successfully Aborted!!");

			return Response.status(Response.Status.OK).entity(stoppingBatchResponseBean).build();
		}
		catch (Exception exception) 
		{
			LOG.info("Logging the occured exception in the service class stoppingBatch method catch block........"+exception);
			throw new IOCLWSException(ErrorMessageConstants.Unprocessable_Entity_Code,ErrorMessageConstants.Internal_Error);
		}
	}

	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,rollbackFor=IOCLWSException.class)
	public Response getComments(String type) throws IOCLWSException
	{
		List<String> commentsList=new ArrayList<String>();
		List<IoclComments> listOfComments=iOCLCommentsDAO.findCommentsByType(type);
		for(IoclComments ioclComments:listOfComments)
		{
			commentsList.add(ioclComments.getCommentName());
		}
		return Response.status(Response.Status.OK).entity(commentsList).build();
	}

	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,rollbackFor=IOCLWSException.class)
	public Response getTrucks() throws IOCLWSException
	{
		List<String> truckList=new ArrayList<String>();
		List<IoclTruckregistrationDetail> listOfTrucks=iOCLTruckRegistrationDetailsDAO.findAllTrucks();
		for(IoclTruckregistrationDetail trucks:listOfTrucks)
		{
			truckList.add(trucks.getTruckNo());
		}
		return Response.status(Response.Status.OK).entity(truckList).build();
	}

	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,rollbackFor=IOCLWSException.class)
	public Response getAutoFillDetails(String truckNo) throws IOCLWSException
	{
		IoclTruckregistrationDetail truckDetails=iOCLTruckRegistrationDetailsDAO.findTruckByTruckNo(truckNo);
		AutofillDetailsResponse autofillDetailsResponse=new AutofillDetailsResponse();
		if(null!=truckDetails)
		{
			//autofillDetailsResponse.setInsuranceNo(truckDetails.getInsuranceNo());
			//autofillDetailsResponse.setPermitNo(truckDetails.getPermitNo());
			if(null!=truckDetails.getInsuranceExpDate())
			{
				autofillDetailsResponse.setInsuranceExpDate(new SimpleDateFormat(appProps.getProperty("DatePickerFormat")).format(truckDetails.getInsuranceExpDate()));
			}
			if(null!=truckDetails.getPermitExpDate())
			{
				autofillDetailsResponse.setPermitExpDate(new SimpleDateFormat(appProps.getProperty("DatePickerFormat")).format(truckDetails.getPermitExpDate()));
			}

			if(truckDetails.getContractorId()!=0)
			{
				IoclContractorDetail ioclContractorDetail=iOCLContractorDetailsDAO.findContractorByContractorId(truckDetails.getContractorId());
				autofillDetailsResponse.setTransporterName(ioclContractorDetail.getContractorName());
			}

			if(truckDetails.getLocationId()!=0)
			{
				IoclLocationDetail ioclLocationDetail=iOCLLocationDetailsDAO.findLocationByLocationId(truckDetails.getLocationId());
				autofillDetailsResponse.setLocationName(ioclLocationDetail.getLocationName());
			}
			/*if(truckDetails.getQuantityId()!=0)
			{
				IoclQuantitiesDetail ioclQuantitiesDetail=iOCLQuantityDetailsDAO.findQuantityByQuantityId(truckDetails.getQuantityId());
				autofillDetailsResponse.setTrukCapacity(ioclQuantitiesDetail.getQuantityName()+"("+ioclQuantitiesDetail.getQuantity()+")");
			}*/
			
			if(null!=truckDetails.getPesoValue())
			{
				autofillDetailsResponse.setPoesVal(truckDetails.getPesoValue());
			}
		}
		return Response.status(Response.Status.OK).entity(autofillDetailsResponse).build();
	}

	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,rollbackFor=IOCLWSException.class)
	public Response bayReallocation(String truckNo,String driverName,String driverLicNo,String customer,String quantity,String vehicleWgt,String destination,String locationCode,String mobileNumber,int bayNum,String contarctorName,String createdBy,int quantityID,String permitExpDate,String insuranceExpDate,String qty,boolean iModifiedFlag,boolean pModifiedFlag,int fanId,String poesVal) throws IOCLWSException
	{
		try
		{
			FanPinCreatedResponse fanPinCreatedResponse=new FanPinCreatedResponse();

			//Check if the truck is there in truck table,If not insert truck details.  else get the truck id and insert new row in fan table
			int truckID=0;
			IoclTruckregistrationDetail ioclTruckregistrationDetail=iOCLTruckRegistrationDetailsDAO.findTruckByTruckNo(truckNo);

			//GET THE LOCATIONID and STATUSID
			//IoclLocationDetail ioclLocationDetail=iOCLLocationDetailsDAO.findLocationIdByLocationCode(locationCode);

			IoclLocationDetail ioclLocationDetail=iOCLLocationDetailsDAO.findLocationByLocationName(locationCode);

			IoclContractorDetail ioclContractorDetail=iOCLContractorDetailsDAO.findContractorByContractorName(contarctorName);

			DateFormat dformat=new SimpleDateFormat(appProps.getProperty("DatePickerFormat"));
			IoclQuantitiesDetail ioclQuantitiesDetail=null;
			//IoclQuantitiesDetail ioclQuantitiesDetail=iOCLQuantityDetailsDAO.findQuantityByQuantityId(quantityID);
			if(ioclTruckregistrationDetail==null)
			{
				Date permitExp=dformat.parse(permitExpDate);
				Date insuranceExp=dformat.parse(insuranceExpDate);
				//iOCLTruckRegistrationDetailsDAO.insertTruckregistrationDetail(truckNo, driverName, driverLicNo, customer, mobileNumber,permitExp,insuranceExp,ioclLocationDetail.getLocationID(),ioclContractorDetail.getContractorId(),ioclQuantitiesDetail.getQuantityId());
				iOCLTruckRegistrationDetailsDAO.insertTruckregistrationDetail(truckNo, driverName, driverLicNo, customer, mobileNumber,permitExp,insuranceExp,ioclLocationDetail.getLocationID(),ioclContractorDetail.getContractorId(),0,poesVal);

				IoclTruckregistrationDetail ioclTruckDetails=iOCLTruckRegistrationDetailsDAO.findTruckByTruckNo(truckNo);
				if(null!=ioclTruckDetails)
				{
					truckID=ioclTruckDetails.getTruckId();
				}
				LOG.info("New Truck::::::::"+truckID);
			}
			else
			{
				Date permitExp=dformat.parse(permitExpDate);
				Date insuranceExp=dformat.parse(insuranceExpDate);
				IoclTruckregistrationDetail ioclTruckDetails=iOCLTruckRegistrationDetailsDAO.findTruckByTruckNo(truckNo);
				if(iModifiedFlag)
				{
					ioclTruckDetails.setInsuranceExpDate(insuranceExp);
				}
				if(pModifiedFlag)
				{
					ioclTruckDetails.setPermitExpDate(permitExp);
				}
				ioclTruckDetails.setPesoValue(poesVal);
				truckID=ioclTruckDetails.getTruckId();
				LOG.info("Existing Truck::::::::"+truckID);
			}

			if(truckID!=0)
			{
				int fanPin=0;
				while(true)
				{
					fanPin=CommonUtilites.pinGen();
					boolean flag=CommonUtilites.checkPinHasFourDigits(fanPin);
					if(flag)
					{
						break;
					}
				}

				DateFormat dateFormat = new SimpleDateFormat(appProps.getProperty("AppDateFormat"));
				Date createddateobj = new Date();

				Calendar cal = Calendar.getInstance();
				cal.setTime(createddateobj);
				cal.add(Calendar.HOUR, Integer.parseInt(appProps.getProperty("FanExpirationNumberOfHours")));
				Date fanExpirationTime = cal.getTime();
				LOG.info("fanExpirationTime::::::"+dateFormat.format(fanExpirationTime));

				IoclSupportedPinstatus ioclSupportedPinstatus=iOCLSupportedPinStatusDAO.findPinStatusIdByPinStatus(appProps.getProperty("FanCreatedStatus"));

				LOG.info("createdBy::::::"+createdBy);
				IoclUserDetail ioclUserDetail=iOCLUserDetailsDAO.findUserByUserName(createdBy);
				LOG.info("ioclUserDetail:::::::"+ioclUserDetail);
				int userID=ioclUserDetail.getUserId();

				LOG.info("usrId:::"+userID);
				if(null!=ioclLocationDetail && null!=ioclSupportedPinstatus && null!=ioclUserDetail)
				{
					ioclReallocationFanslipDetailsDAO.insertFanSlipDetails(bayNum, String.valueOf(fanPin),ioclSupportedPinstatus, truckID,createddateobj,qty,vehicleWgt,destination,ioclLocationDetail,fanExpirationTime,ioclContractorDetail,userID,ioclQuantitiesDetail,fanId);
					fanPinCreatedResponse.setFanPin(String.valueOf(fanPin));
					fanPinCreatedResponse.setBayNum(bayNum);
					fanPinCreatedResponse.setQuantity(quantity);
					fanPinCreatedResponse.setContractorName(contarctorName);
					//fanPinCreatedResponse.setDestination(destination);
					//fanPinCreatedResponse.setFanId(fanId);
					fanPinCreatedResponse.setFanPinCreation(dateFormat.format(createddateobj));
					fanPinCreatedResponse.setFanPinExpiration(dateFormat.format(fanExpirationTime));
					fanPinCreatedResponse.setFanPinStatus(appProps.getProperty("FanCreatedStatus"));
					fanPinCreatedResponse.setLocationCode(locationCode);
					fanPinCreatedResponse.setQuantity(qty);
					fanPinCreatedResponse.setQuantityID(quantityID);
					fanPinCreatedResponse.setTruckNumber(truckNo);
					//fanPinCreatedResponse.setDriverName(driverName);
					//fanPinCreatedResponse.setCustomer(customer);
					fanPinCreatedResponse.setVehicleWeight(vehicleWgt);
				}
			}
			return Response.status(Response.Status.OK).entity(fanPinCreatedResponse).build();
		}
		catch(Exception exception)
		{
			LOG.info("Logging the occured exception in the service class bayReallocation method catch block........"+exception);
			throw new IOCLWSException(ErrorMessageConstants.Unprocessable_Entity_Code,ErrorMessageConstants.Internal_Error);
		}	
	}
}