package com.rainiersoft.iocl.dao;

import java.util.Date;
import java.util.List;

import com.rainiersoft.iocl.entity.IoclContractorDetail;
import com.rainiersoft.iocl.entity.IoclLocationDetail;
import com.rainiersoft.iocl.entity.IoclQuantitiesDetail;
import com.rainiersoft.iocl.entity.IoclReallocationFanSlipDetails;
import com.rainiersoft.iocl.entity.IoclSupportedPinstatus;

public interface IOCLReallocationFanslipDetailsDAO extends GenericDAO<IoclReallocationFanSlipDetails, Long>
{

	public void insertFanSlipDetails(int bayNo, String fanPin, IoclSupportedPinstatus fanPinStatusId, int truckID, Date createdOn, String quantity, String vehicleWgt, String destination, IoclLocationDetail locationId,Date fanExpirationTime,IoclContractorDetail ioclContractorDetail,int userId,IoclQuantitiesDetail quantityID,int fanId);

	public List<IoclReallocationFanSlipDetails> findAnyBayIsAssignedInPast(int bayNo, Date currDate, Date pastDate);

	public List<IoclReallocationFanSlipDetails> findAllLatestFanSlips(Date currDate, Date pastDate);
	
	public List<IoclReallocationFanSlipDetails> findFanDetailsByFanId(int fanId);

	public IoclReallocationFanSlipDetails findFanPinStatusByFanPin(String fanPin);

	public IoclReallocationFanSlipDetails findFanSlipDetailsByFanPinAndBayNum(String fanPin,int bayNo);

	public IoclReallocationFanSlipDetails findFanPinByFanId(int FanId);

	public void updateFanPinDetails(IoclReallocationFanSlipDetails ioclFanslipDetail,IoclSupportedPinstatus ioclSupportedPinstatus,int userID,Date updatedOn,String comments);

	public void updateFanpinExpirationTime(IoclReallocationFanSlipDetails ioclFanslipDetail,IoclSupportedPinstatus ioclSupportedPinstatus,int userID,Date updatedOn,Date fanExpirationDate,String comments);
}