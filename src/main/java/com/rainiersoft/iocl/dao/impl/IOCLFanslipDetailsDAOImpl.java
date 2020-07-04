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

import com.rainiersoft.iocl.dao.IOCLFanslipDetailsDAO;
import com.rainiersoft.iocl.entity.IoclContractorDetail;
import com.rainiersoft.iocl.entity.IoclFanslipDetail;
import com.rainiersoft.iocl.entity.IoclLocationDetail;
import com.rainiersoft.iocl.entity.IoclQuantitiesDetail;
import com.rainiersoft.iocl.entity.IoclSupportedPinstatus;

@Repository
@Singleton
public class IOCLFanslipDetailsDAOImpl extends GenericDAOImpl<IoclFanslipDetail, Long> implements IOCLFanslipDetailsDAO
{
	private static final Logger LOG = LoggerFactory.getLogger(IOCLFanslipDetailsDAOImpl.class);

	public IOCLFanslipDetailsDAOImpl() {}

	public Long insertFanSlipDetails(int bayNo, String fanPin, IoclSupportedPinstatus fanPinStatusId, int truckID, Date createdOn, String quantity, String vehicleWgt, String destination, IoclLocationDetail locationId,Date fanPinExpirationOn,IoclContractorDetail ioclContractorDetail,int fanCreatedBy,IoclQuantitiesDetail ioclQuantitiesDetail) 
	{
		Session session=getCurrentSession();
		IoclFanslipDetail ioclFanslipDetail = new IoclFanslipDetail();
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
		//ioclFanslipDetail.setIoclQuantitiesDetail(ioclQuantitiesDetail);
		Integer fanId=(Integer) session.save(ioclFanslipDetail);
		return fanId.longValue();
	}


	public List<IoclFanslipDetail> findAnyBayIsAssignedInPast(int bayNo, Date currDate, Date pastDate)
	{
		Session session = getCurrentSession();
		Query query = session.getNamedQuery("findAnyBayIsAssignedInPast");
		query.setParameter("bayNo", Integer.valueOf(bayNo));
		query.setParameter("currDate", currDate);
		query.setParameter("pastDate", pastDate);
		List<IoclFanslipDetail> ioclFanslipDetail = findObjectCollection(query);
		return ioclFanslipDetail;
	}


	public IoclFanslipDetail findFanPinStatusByFanPin(String fanPin)
	{
		Session session = getCurrentSession();
		Query query = session.getNamedQuery("findFanPinStatusByFanPin");
		query.setParameter("fanPin", fanPin);
		query.setFirstResult(0);
		query.setMaxResults(1);
		IoclFanslipDetail ioclFanslipDetail = (IoclFanslipDetail)findObject(query);
		return ioclFanslipDetail;
	}

	@Override
	public List<IoclFanslipDetail> findAllLatestFanSlips(Date currDate, Date pastDate) 
	{
		Session session = getCurrentSession();
		Query query = session.getNamedQuery("findAllLatestFanSlips");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		LOG.info("dateFormat.parse(dateFormat.format(currDate)..."+dateFormat.format(currDate));
		LOG.info(dateFormat.format(pastDate));

		try
		{
			query.setParameter("currDate", (Date)dateFormat.parse(dateFormat.format(currDate)));
			query.setParameter("pastDate", (Date)dateFormat.parse(dateFormat.format(pastDate)));
		}
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
		List<IoclFanslipDetail> ioclFanslipDetail = findObjectCollection(query);
		return ioclFanslipDetail;
	}

	@Override
	public IoclFanslipDetail findFanPinByFanId(int fanId) 
	{
		Session session = getCurrentSession();
		Query query = session.getNamedQuery("findFanPinByFanId");
		query.setParameter("fanId", fanId);
		IoclFanslipDetail ioclFanslipDetail = (IoclFanslipDetail)findObject(query);
		return ioclFanslipDetail;
	}

	@Override
	public void updateFanPinDetails(IoclFanslipDetail ioclFanslipDetail,IoclSupportedPinstatus ioclSupportedPinstatus,int userID,Date fanUpdatedOn,String comments) 
	{
		ioclFanslipDetail.setIoclSupportedPinstatus(ioclSupportedPinstatus);
		ioclFanslipDetail.setFanUpdatedBy(userID);
		ioclFanslipDetail.setFanUpdatedOn(fanUpdatedOn);
		ioclFanslipDetail.setComments(comments);
		saveOrUpdate(ioclFanslipDetail);
	}

	@Override
	public List<IoclFanslipDetail> findFanSlipDetailsByFanPinAndBayNum(String fanPin, int bayNo) {
		Session session = getCurrentSession();
		Query query = session.getNamedQuery("findFanSlipDetailsByFanPinAndBayNum");
		query.setParameter("fanPin", fanPin);
		query.setParameter("bayNo", bayNo);
		List<IoclFanslipDetail> ioclFanslipDetail = (List<IoclFanslipDetail>)findObjectCollection(query);
		return ioclFanslipDetail;
	}

	@Override
	public void updateFanpinExpirationTime(IoclFanslipDetail ioclFanslipDetail,IoclSupportedPinstatus ioclSupportedPinstatus, int userID, Date updatedOn,Date fanExpirationDate,String comments) 
	{
		ioclFanslipDetail.setIoclSupportedPinstatus(ioclSupportedPinstatus);
		ioclFanslipDetail.setFanUpdatedBy(userID);
		ioclFanslipDetail.setFanUpdatedOn(updatedOn);
		ioclFanslipDetail.setFanExpirationOn(fanExpirationDate);
		ioclFanslipDetail.setComments(comments);
		saveOrUpdate(ioclFanslipDetail);
	}

	@Override
	public List<IoclFanslipDetail> findAllLatestFanSlipsAndTrucks(Date currDate, Date pastDate, int truckId) 
	{
		Session session = getCurrentSession();
		Query query = session.getNamedQuery("findAllLatestFanSlipsAndTrucks");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		LOG.info("dateFormat.parse(dateFormat.format(currDate)..."+dateFormat.format(currDate));
		LOG.info(dateFormat.format(pastDate));

		try
		{
			query.setParameter("currDate", (Date)dateFormat.parse(dateFormat.format(currDate)));
			query.setParameter("pastDate", (Date)dateFormat.parse(dateFormat.format(pastDate)));
			query.setParameter("truckId", truckId);
		}
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
		List<IoclFanslipDetail> ioclFanslipDetail = findObjectCollection(query);
		return ioclFanslipDetail;
	}

	@Override
	public Long insertReallocationFanSlipDetails(int bayNo, String fanPin,
			IoclSupportedPinstatus fanPinStatusId, int truckID, Date createdOn,
			String quantity, String vehicleWgt, String destination,
			IoclLocationDetail locationId, Date fanExpirationTime,
			IoclContractorDetail ioclContractorDetail, int userId,
			IoclQuantitiesDetail quantityID, String remainingQty) 
	{
		Session session=getCurrentSession();
		IoclFanslipDetail ioclFanslipDetail = new IoclFanslipDetail();
		ioclFanslipDetail.setBayNo(bayNo);
		ioclFanslipDetail.setFanPin(fanPin);
		ioclFanslipDetail.setTruckId(truckID);
		ioclFanslipDetail.setFanCreationOn(createdOn);
		ioclFanslipDetail.setQuantity(remainingQty);
		ioclFanslipDetail.setTransformedPreset(Long.toHexString(Double.doubleToLongBits(Long.parseLong(quantity))));
		ioclFanslipDetail.setVehicleWgt(vehicleWgt);
		//ioclFanslipDetail.setDestination(destination);
		ioclFanslipDetail.setIoclLocationDetail(locationId);
		ioclFanslipDetail.setIoclContractorDetail(ioclContractorDetail);
		ioclFanslipDetail.setIoclSupportedPinstatus(fanPinStatusId);
		ioclFanslipDetail.setFanExpirationOn(fanExpirationTime);
		ioclFanslipDetail.setFanCreatedBy(userId);
		//ioclFanslipDetail.setIoclQuantitiesDetail(quantityID);
		Integer fanId=(Integer) session.save(ioclFanslipDetail);
		return fanId.longValue();
	}
}