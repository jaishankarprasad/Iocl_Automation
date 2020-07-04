package com.rainiersoft.iocl.dao.impl;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Singleton;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.rainiersoft.iocl.dao.IOCLReallocationFanslipDetailsDAO;
import com.rainiersoft.iocl.entity.IoclContractorDetail;
import com.rainiersoft.iocl.entity.IoclLocationDetail;
import com.rainiersoft.iocl.entity.IoclQuantitiesDetail;
import com.rainiersoft.iocl.entity.IoclReallocationFanSlipDetails;
import com.rainiersoft.iocl.entity.IoclSupportedPinstatus;

@Repository
@Singleton
public class IOCLReallocationFanslipDetailsDAOImpl extends GenericDAOImpl<IoclReallocationFanSlipDetails, Long> implements IOCLReallocationFanslipDetailsDAO
{
	private static final Logger LOG = LoggerFactory.getLogger(IOCLReallocationFanslipDetailsDAOImpl.class);

	public IOCLReallocationFanslipDetailsDAOImpl() {}

	public void insertFanSlipDetails(int bayNo, String fanPin, IoclSupportedPinstatus fanPinStatusId, int truckID, Date createdOn, String quantity, String vehicleWgt, String destination, IoclLocationDetail locationId,Date fanPinExpirationOn,IoclContractorDetail ioclContractorDetail,int fanCreatedBy,IoclQuantitiesDetail ioclQuantitiesDetail,int fanId) 
	{
		Session session=getCurrentSession();
		IoclReallocationFanSlipDetails ioclFanslipDetail = new IoclReallocationFanSlipDetails();
		ioclFanslipDetail.setFanId(fanId);
		ioclFanslipDetail.setBayNo(bayNo);
		ioclFanslipDetail.setFanPin(fanPin);
		ioclFanslipDetail.setTruckId(truckID);
		ioclFanslipDetail.setFanCreationOn(createdOn);
		ioclFanslipDetail.setQuantity(quantity);
		ioclFanslipDetail.setTransformedPreset(Long.toHexString(Double.doubleToLongBits(Long.parseLong(quantity))));
		ioclFanslipDetail.setVehicleWgt(vehicleWgt);
		//ioclFanslipDetail.setDestination(destination);
		ioclFanslipDetail.setIoclLocationDetail(locationId);
		ioclFanslipDetail.setIoclContractorDetail(ioclContractorDetail);
		ioclFanslipDetail.setIoclSupportedPinstatus(fanPinStatusId);
		ioclFanslipDetail.setFanExpirationOn(fanPinExpirationOn);
		ioclFanslipDetail.setFanCreatedBy(fanCreatedBy);
		ioclFanslipDetail.setIoclQuantitiesDetail(ioclQuantitiesDetail);
		session.save(ioclFanslipDetail);
	}


	public List<IoclReallocationFanSlipDetails> findAnyBayIsAssignedInPast(int bayNo, Date currDate, Date pastDate)
	{
		Session session = getCurrentSession();
		Query query = session.getNamedQuery("findReallocationAnyBayIsAssignedInPast");
		query.setParameter("bayNo", Integer.valueOf(bayNo));
		query.setParameter("currDate", currDate);
		query.setParameter("pastDate", pastDate);
		List<IoclReallocationFanSlipDetails> ioclFanslipDetail = findObjectCollection(query);
		return ioclFanslipDetail;
	}


	public IoclReallocationFanSlipDetails findFanPinStatusByFanPin(String fanPin)
	{
		Session session = getCurrentSession();
		Query query = session.getNamedQuery("findReallocationFanPinStatusByFanPin");
		query.setParameter("fanPin", fanPin);
		query.setFirstResult(0);
		query.setMaxResults(1);
		IoclReallocationFanSlipDetails ioclFanslipDetail = (IoclReallocationFanSlipDetails)findObject(query);
		return ioclFanslipDetail;
	}

	@Override
	public List<IoclReallocationFanSlipDetails> findAllLatestFanSlips(Date currDate, Date pastDate) 
	{
		Session session = getCurrentSession();
		Query query = session.getNamedQuery("findReallocationAllLatestFanSlips");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("dateFormat.parse(dateFormat.format(currDate)..."+dateFormat.format(currDate));
		System.out.println(dateFormat.format(pastDate));

		try
		{
			query.setParameter("currDate", (Date)dateFormat.parse(dateFormat.format(currDate)));
			query.setParameter("pastDate", (Date)dateFormat.parse(dateFormat.format(pastDate)));
		}
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
		List<IoclReallocationFanSlipDetails> ioclFanslipDetail = findObjectCollection(query);
		return ioclFanslipDetail;
	}

	@Override
	public IoclReallocationFanSlipDetails findFanPinByFanId(int fanId) 
	{
		Session session = getCurrentSession();
		Query query = session.getNamedQuery("findReallocationFanPinByFanId");
		query.setParameter("fanId", fanId);
		IoclReallocationFanSlipDetails ioclFanslipDetail = (IoclReallocationFanSlipDetails)findObject(query);
		return ioclFanslipDetail;
	}

	@Override
	public void updateFanPinDetails(IoclReallocationFanSlipDetails ioclFanslipDetail,IoclSupportedPinstatus ioclSupportedPinstatus,int userID,Date fanUpdatedOn,String comments) 
	{
		ioclFanslipDetail.setIoclSupportedPinstatus(ioclSupportedPinstatus);
		ioclFanslipDetail.setFanUpdatedBy(userID);
		ioclFanslipDetail.setFanUpdatedOn(fanUpdatedOn);
		ioclFanslipDetail.setComments(comments);
		saveOrUpdate(ioclFanslipDetail);
	}

	@Override
	public IoclReallocationFanSlipDetails findFanSlipDetailsByFanPinAndBayNum(String fanPin, int bayNo) {
		Session session = getCurrentSession();
		Query query = session.getNamedQuery("findReallocationFanSlipDetailsByFanPinAndBayNum");
		query.setParameter("fanPin", fanPin);
		query.setParameter("bayNo", bayNo);
		IoclReallocationFanSlipDetails ioclFanslipDetail = (IoclReallocationFanSlipDetails)findObject(query);
		return ioclFanslipDetail;
	}

	@Override
	public void updateFanpinExpirationTime(IoclReallocationFanSlipDetails ioclFanslipDetail,IoclSupportedPinstatus ioclSupportedPinstatus, int userID, Date updatedOn,Date fanExpirationDate,String comments) 
	{
		ioclFanslipDetail.setIoclSupportedPinstatus(ioclSupportedPinstatus);
		ioclFanslipDetail.setFanUpdatedBy(userID);
		ioclFanslipDetail.setFanUpdatedOn(updatedOn);
		ioclFanslipDetail.setFanExpirationOn(fanExpirationDate);
		ioclFanslipDetail.setComments(comments);
		saveOrUpdate(ioclFanslipDetail);
	}

	@Override
	public List<IoclReallocationFanSlipDetails> findFanDetailsByFanId(int fanId)
	{
		Session session = getCurrentSession();
		Query query = session.getNamedQuery("findReallocationFanDetailsByFanId");
		query.setParameter("fanId", fanId);
		List<IoclReallocationFanSlipDetails> ioclFanslipDetail = (List<IoclReallocationFanSlipDetails>)findObjectCollection(query);
		return ioclFanslipDetail;
	}
}