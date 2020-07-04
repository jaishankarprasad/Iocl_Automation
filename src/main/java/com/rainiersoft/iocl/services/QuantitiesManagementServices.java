package com.rainiersoft.iocl.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rainiersoft.iocl.dao.IOCLQuantityDetailsDAO;
import com.rainiersoft.iocl.dao.IOCLSupportedQunatityStatusDAO;
import com.rainiersoft.iocl.dao.IOCLUserDetailsDAO;
import com.rainiersoft.iocl.entity.IoclQuantitiesDetail;
import com.rainiersoft.iocl.entity.IoclSupportedQuantitystatus;
import com.rainiersoft.iocl.entity.IoclUserDetail;
import com.rainiersoft.iocl.exception.IOCLWSException;
import com.rainiersoft.iocl.util.ErrorMessageConstants;
import com.rainiersoft.response.dto.GetQuantityStaticDataResponseBean;
import com.rainiersoft.response.dto.QuantityCreationResponseBean;
import com.rainiersoft.response.dto.QuantityDeletionResponseBean;
import com.rainiersoft.response.dto.QuantityDetailsResponseBean;


/**
 * This is the class for QuantitiesManagementServices
 * @author RahulKumarPamidi
 */

@Service
@Singleton
public class QuantitiesManagementServices 
{
	private static final Logger LOG = LoggerFactory.getLogger(QuantitiesManagementServices.class);

	@Autowired
	IOCLQuantityDetailsDAO ioclQuantityDetailsDAO;

	@Autowired
	IOCLSupportedQunatityStatusDAO iOCLSupportedQunatityStatusDAO;
	
	@Autowired
	IOCLUserDetailsDAO iOCLUserDetailsDAO;
	
	@Autowired
	Properties appProps;


	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,rollbackFor=IOCLWSException.class)
	public Response getQuantityDetails() throws IOCLWSException
	{
		LOG.info("Entered into getQuantityDetails service class method........");
		try
		{
			List<QuantityDetailsResponseBean> listQuantityDetailsResponseBean=new ArrayList<QuantityDetailsResponseBean>();
			List<IoclQuantitiesDetail> lIoclQuantitiesDetail=ioclQuantityDetailsDAO.findAllQuantites();
			DateFormat dateFormat = new SimpleDateFormat(appProps.getProperty("AppDateFormat"));
			for(IoclQuantitiesDetail ioclQuantitiesDetail:lIoclQuantitiesDetail)
			{
				System.out.println("IoclQuantitiesDetail......"+ioclQuantitiesDetail);
				QuantityDetailsResponseBean quantityDetailsResponseBean=new QuantityDetailsResponseBean();
				quantityDetailsResponseBean.setQunatityId(ioclQuantitiesDetail.getQuantityId());
				quantityDetailsResponseBean.setOperationalStatus(ioclQuantitiesDetail.getIoclSupportedQuantitystatus().getQuantityStatus());
				quantityDetailsResponseBean.setQuantity(ioclQuantitiesDetail.getQuantity());
				quantityDetailsResponseBean.setQuantityName(ioclQuantitiesDetail.getQuantityName());
				if(ioclQuantitiesDetail.getQuantityUpdatedBy()!=0)
				{
					IoclUserDetail ioclUserDetail=iOCLUserDetailsDAO.findUserByUserId(ioclQuantitiesDetail.getQuantityUpdatedBy());
					quantityDetailsResponseBean.setQuantityUpdatedBy(ioclUserDetail.getUserName());
					quantityDetailsResponseBean.setQuantityUpdatedOn(dateFormat.format(ioclQuantitiesDetail.getQuantityUpdatedOn()));
				}
				if(ioclQuantitiesDetail.getQuantityCreatedBy()!=0)
				{
					IoclUserDetail ioclUserDetail=iOCLUserDetailsDAO.findUserByUserId(ioclQuantitiesDetail.getQuantityCreatedBy());
					quantityDetailsResponseBean.setQuantityCreatedBy(ioclUserDetail.getUserName());
					quantityDetailsResponseBean.setQuantityCreatedOn(dateFormat.format(ioclQuantitiesDetail.getQuantityCreatedOn()));
				}
				//quantityDetailsResponseBean.setQuantityUnit(ioclQuantitiesDetail.getQuantityUnits());
				listQuantityDetailsResponseBean.add(quantityDetailsResponseBean);
			}
			return Response.status(Response.Status.OK).entity(listQuantityDetailsResponseBean).build();
		}
		catch(Exception exception)
		{
			LOG.info("Logging the occured exception in the service class getQuantityDetails method catch block........"+exception);
			throw new IOCLWSException(ErrorMessageConstants.Unprocessable_Entity_Code,ErrorMessageConstants.Internal_Error);
		}
	}

	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,rollbackFor=IOCLWSException.class)
	public Response addQunatity(String quantityName,String quantity,String quantityStatus,String userName) throws IOCLWSException
	{
		LOG.info("Entered into addQunatity service class method........");
		QuantityCreationResponseBean quantityCreationResponseBean=new QuantityCreationResponseBean();
		try
		{
			IoclQuantitiesDetail ioclQuantitiesDetail=ioclQuantityDetailsDAO.findQuantityByQuantityName(quantityName);
			LOG.info("ioclQuantitiesDetail:::::::"+ioclQuantitiesDetail);
			if(ioclQuantitiesDetail!=null)
			{
				throw new IOCLWSException(Response.Status.CONFLICT.getStatusCode(),ErrorMessageConstants.QunatityName_Already_Exist_Msg);
			}
			IoclQuantitiesDetail ioclQuantitiesDetailNum=ioclQuantityDetailsDAO.findQuantityByQunatity(quantity);
			if(ioclQuantitiesDetailNum!=null)
			{
				LOG.info("ioclQuantitiesDetail Already Exist!!!!!!!");
				throw new IOCLWSException(Response.Status.CONFLICT.getStatusCode(),ErrorMessageConstants.QunatityNum_Already_Exist_Msg);
			}
			else
			{
				IoclSupportedQuantitystatus ioclSupportedQuantitystatus=iOCLSupportedQunatityStatusDAO.findStatusIdByQuantityStatus(quantityStatus);
				if(null!=ioclSupportedQuantitystatus)
				{
					LOG.info("createdBy::::::"+userName);
					IoclUserDetail ioclUserDetail=iOCLUserDetailsDAO.findUserByUserName(userName);
					LOG.info("ioclUserDetail:::::::"+ioclUserDetail);
					int userID=ioclUserDetail.getUserId();
					LOG.info("usrId:::"+userID);
					
					DateFormat dateFormat = new SimpleDateFormat(appProps.getProperty("AppDateFormat"));
					Date quantityCreatedDateobj = new Date();
					
					Long quantityId=ioclQuantityDetailsDAO.insertQuantitiesDetails(quantityName,quantity,ioclSupportedQuantitystatus,userID,quantityCreatedDateobj);
					quantityCreationResponseBean.setQuantityId(quantityId);
					quantityCreationResponseBean.setQunatity(quantity);
					quantityCreationResponseBean.setQunatityName(quantityName);
					quantityCreationResponseBean.setOperationalStatus(quantityStatus);
					quantityCreationResponseBean.setUserName(userName);
					quantityCreationResponseBean.setTimeStamp(dateFormat.format(quantityCreatedDateobj));
					quantityCreationResponseBean.setMessage("Quantity SuccessFully Created : "+quantityName);
					quantityCreationResponseBean.setSuccessFlag(true);
				}
			}			
		}
		catch(IOCLWSException ioclexception)
		{
			LOG.info("Logging the occured exception in the service class addQunatity method custom catch block........"+ioclexception);
			throw ioclexception;
		}
		catch(Exception exception)
		{
			LOG.info("Logging the occured exception in the service class addQunatity method catch block........"+exception);
			throw new IOCLWSException(ErrorMessageConstants.Unprocessable_Entity_Code,ErrorMessageConstants.Internal_Error);
		}
		return Response.status(Response.Status.OK).entity(quantityCreationResponseBean).build();	
	}	

	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,rollbackFor=IOCLWSException.class)
	public Response updateQuantity(int qunatityId,String quantityName,String quantity,String quantityStatus,boolean editQunatityNameFlag,boolean editQunatityFlag,String userName) throws IOCLWSException
	{
		LOG.info("Entered into updateQuantity service class method........");
		QuantityCreationResponseBean quantityUpdationResponseBean=new QuantityCreationResponseBean();
		try
		{
			if(editQunatityNameFlag)
			{
				IoclQuantitiesDetail ioclQuantitiesDetail=ioclQuantityDetailsDAO.findQuantityByQuantityName(quantityName);
				LOG.info("ioclQuantitiesDetail:::::::"+ioclQuantitiesDetail);
				if(ioclQuantitiesDetail!=null)
				{
					LOG.info("ioclQuantitiesDetail Already Exist!!!!!!!");
					throw new IOCLWSException(Response.Status.CONFLICT.getStatusCode(),ErrorMessageConstants.QunatityName_Already_Exist_Msg);
				}
			}
			if(editQunatityFlag)
			{
				IoclQuantitiesDetail ioclQuantitiesDetailNum=ioclQuantityDetailsDAO.findQuantityByQunatity(quantity);
				if(ioclQuantitiesDetailNum!=null)
				{
					throw new IOCLWSException(Response.Status.CONFLICT.getStatusCode(),ErrorMessageConstants.QunatityNum_Already_Exist_Msg);
				}
			}
			IoclQuantitiesDetail ioclQuantitiesDetail=ioclQuantityDetailsDAO.findQuantityByQuantityId(qunatityId);
			IoclSupportedQuantitystatus ioclSupportedQuantitystatus=iOCLSupportedQunatityStatusDAO.findStatusIdByQuantityStatus(quantityStatus);
			if(null!=ioclSupportedQuantitystatus)
			{
				LOG.info("createdBy::::::"+userName);
				IoclUserDetail ioclUserDetail=iOCLUserDetailsDAO.findUserByUserName(userName);
				LOG.info("ioclUserDetail:::::::"+ioclUserDetail);
				int userID=ioclUserDetail.getUserId();
				LOG.info("usrId:::"+userID);
				
				DateFormat dateFormat = new SimpleDateFormat(appProps.getProperty("AppDateFormat"));
				Date quantityUpdateddateobj = new Date();
				
				ioclQuantityDetailsDAO.updateQuantitiesDetails(quantityName,quantity,ioclSupportedQuantitystatus,ioclQuantitiesDetail,userID,quantityUpdateddateobj);
				quantityUpdationResponseBean.setQuantityId(Long.valueOf(qunatityId));
				quantityUpdationResponseBean.setQunatity(quantity);
				quantityUpdationResponseBean.setQunatityName(quantityName);
				quantityUpdationResponseBean.setOperationalStatus(quantityStatus);
				quantityUpdationResponseBean.setUserName(userName);
				quantityUpdationResponseBean.setTimeStamp(dateFormat.format(quantityUpdateddateobj));
				quantityUpdationResponseBean.setMessage("Quantity SuccessFully Updated : "+quantityName);
				quantityUpdationResponseBean.setSuccessFlag(true);
			}
		}
		catch(IOCLWSException ioclexception)
		{
			LOG.info("Logging the occured exception in the service class updateQuantity method custom catch block........"+ioclexception);
			throw ioclexception;
		}
		catch(Exception exception)
		{
			LOG.info("Logging the occured exception in the service class updateQuantity method catch block........"+exception);
			throw new IOCLWSException(ErrorMessageConstants.Unprocessable_Entity_Code,ErrorMessageConstants.Internal_Error);
		}
		return Response.status(Response.Status.OK).entity(quantityUpdationResponseBean).build();
	}

	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,readOnly=false,rollbackFor=IOCLWSException.class)
	public Response deleteQuantity(int quantityID) throws IOCLWSException
	{
		LOG.info("Entered into deleteQuantity service class method........");
		try
		{
			QuantityDeletionResponseBean  quantityDeletionResponseBean=new QuantityDeletionResponseBean();
			boolean deleteFalg=ioclQuantityDetailsDAO.deleteQunatity(quantityID);
			if(deleteFalg)
			{
				quantityDeletionResponseBean.setSuccessFlag(true);
				quantityDeletionResponseBean.setMessage("Quantity Deleted SuccessFully");
				return  Response.status(Response.Status.OK).entity(quantityDeletionResponseBean).build();	
			}
			else
			{
				quantityDeletionResponseBean.setSuccessFlag(false);
				quantityDeletionResponseBean.setMessage("Failed To Delete Qunatity");
				return  Response.status(Response.Status.OK).entity(quantityDeletionResponseBean).build();
			}
		}
		catch (Exception exception) 
		{
			LOG.info("Logging the occured exception in the service class deleteQuantity method catch block........"+exception);
			throw new IOCLWSException(ErrorMessageConstants.Unprocessable_Entity_Code,ErrorMessageConstants.Internal_Error);
		}
	}

	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED,readOnly=false,rollbackFor=IOCLWSException.class)
	public Response getQuantityStaticData() throws IOCLWSException
	{
		LOG.info("Entered into getQuantityStaticData service class method........");
		try
		{
			GetQuantityStaticDataResponseBean getQuantityStaticDataResponseBean=new GetQuantityStaticDataResponseBean();
			Map<String,List<String>> data=new HashMap<String,List<String>>();
			List<IoclSupportedQuantitystatus> lIoclSupportedQuantitystatus=iOCLSupportedQunatityStatusDAO.findAllSupportedQuantityStatus();
			List<String> supportedStatus=new ArrayList<String>();
			for(IoclSupportedQuantitystatus ioclSupportedQuantitystatus:lIoclSupportedQuantitystatus)
			{
				supportedStatus.add(ioclSupportedQuantitystatus.getQuantityStatus());
			}
			data.put("Status",supportedStatus);
			getQuantityStaticDataResponseBean.setData(data);
			return  Response.status(Response.Status.OK).entity(getQuantityStaticDataResponseBean).build();
		}
		catch (Exception exception) 
		{
			LOG.info("Logging the occured exception in the service class getQuantityStaticData method catch block........"+exception);
			throw new IOCLWSException(ErrorMessageConstants.Unprocessable_Entity_Code,ErrorMessageConstants.Internal_Error);
		}
	}
}