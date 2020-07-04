package com.rainiersoft.iocl.dao.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Singleton;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.rainiersoft.iocl.dao.IOCLTruckRegistrationDetailsDAO;
import com.rainiersoft.iocl.entity.IoclTruckregistrationDetail;

@Repository
@Singleton
public class IOCLTruckRegistrationDetailsDAOImpl extends GenericDAOImpl<IoclTruckregistrationDetail, Long> implements IOCLTruckRegistrationDetailsDAO
{
	public IOCLTruckRegistrationDetailsDAOImpl() {}

	public IoclTruckregistrationDetail findTruckByTruckNo(String truckNo)
	{
		Session session = getCurrentSession();
		Query query = session.getNamedQuery("findTruckByTruckNo");
		query.setParameter("truckNo", truckNo);
		IoclTruckregistrationDetail ioclTruckregistrationDetail = (IoclTruckregistrationDetail)findObject(query);
		return ioclTruckregistrationDetail;
	}

	public void insertTruckregistrationDetail(String truckNo, String driverName, String driverLicNo, String cutomer, String mobileNumber,Date permitExpDate,Date insuranceExpDate,int locationId,int contractorId,int qtyId,String poesVal)
	{
		IoclTruckregistrationDetail ioclTruckregistrationDetail = new IoclTruckregistrationDetail();
		//ioclTruckregistrationDetail.setCustomer(cutomer);
		//ioclTruckregistrationDetail.setDriverLicNo(driverLicNo);
		//ioclTruckregistrationDetail.setDriverName(driverName);
		//ioclTruckregistrationDetail.setMobileNumber(mobileNumber);
		ioclTruckregistrationDetail.setTruckNo(truckNo);
		ioclTruckregistrationDetail.setLocationId(locationId);
		//ioclTruckregistrationDetail.setQuantityId(qtyId);
		ioclTruckregistrationDetail.setContractorId(contractorId);
		//ioclTruckregistrationDetail.setPermitNo(permitNo);
		ioclTruckregistrationDetail.setPermitExpDate(permitExpDate);
		ioclTruckregistrationDetail.setInsuranceExpDate(insuranceExpDate);
		ioclTruckregistrationDetail.setPesoValue(poesVal);
		//ioclTruckregistrationDetail.setInsuranceNo(insuranceNo);
		saveOrUpdate(ioclTruckregistrationDetail);
	}

	@Override
	public IoclTruckregistrationDetail findTruckByTruckId(int truckId) 
	{
		Session session = getCurrentSession();
		Query query = session.getNamedQuery("findTruckByTruckId");
		query.setParameter("truckId", truckId);
		IoclTruckregistrationDetail ioclTruckregistrationDetail = (IoclTruckregistrationDetail)findObject(query);
		return ioclTruckregistrationDetail;
	}

	@Override
	public List<IoclTruckregistrationDetail> findAllTrucks() 
	{
		List<IoclTruckregistrationDetail> listOfTrucksNo=findAll(IoclTruckregistrationDetail.class);
		return listOfTrucksNo;
	}
}
