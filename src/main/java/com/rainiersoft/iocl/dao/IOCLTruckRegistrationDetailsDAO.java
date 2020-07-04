package com.rainiersoft.iocl.dao;

import java.util.Date;
import java.util.List;

import com.rainiersoft.iocl.entity.IoclTruckregistrationDetail;

public interface IOCLTruckRegistrationDetailsDAO extends GenericDAO<IoclTruckregistrationDetail, Long>
{
	public IoclTruckregistrationDetail findTruckByTruckNo(String truckNo);
	
	public IoclTruckregistrationDetail findTruckByTruckId(int truckId);
	
	public List<IoclTruckregistrationDetail> findAllTrucks();

	public void insertTruckregistrationDetail(String truckNo, String driverName, String driverLicNo, String cutomer, String mobileNumber,Date permitExpDate,Date insuranceExpDate,int locationId,int contractorId,int qtyId,String poesVal);
}
