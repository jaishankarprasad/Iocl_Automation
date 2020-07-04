package com.rainiersoft.iocl.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the iocl_truckregistration_details database table.
 * 
 */
@Entity
@Table(name="iocl_truckregistration_details")
@NamedQueries({
@NamedQuery(name="IoclTruckregistrationDetail.findAll", query="SELECT i FROM IoclTruckregistrationDetail i"),
@NamedQuery(name="findTruckByTruckNo", query="SELECT ioclTruckregistrationDetail FROM IoclTruckregistrationDetail ioclTruckregistrationDetail where truckNo=:truckNo"),
@NamedQuery(name="findTruckByTruckId", query="SELECT ioclTruckregistrationDetail FROM IoclTruckregistrationDetail ioclTruckregistrationDetail where truckId=:truckId")
})
public class IoclTruckregistrationDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="TruckId")
	private int truckId;

	@Column(name="Customer")
	private String customer;

	@Column(name="DriverLicNo")
	private String driverLicNo;

	@Column(name="DriverName")
	private String driverName;

	@Column(name="MobileNumber")
	private String mobileNumber;

	@Column(name="TruckNo")
	private String truckNo;

	@Column(name="PermitNo")
	private String permitNo;
	
	@Column(name="PermitExpDate")
	private Date permitExpDate;
	
	@Column(name="InsuranceNo")
	private String insuranceNo;
	
	@Column(name="InsuranceExpDate")
	private Date insuranceExpDate;
	
	@Column(name="ContractorId")
	private int contractorId;
	
	@Column(name="LocationId")
	private int locationId;
	
	@Column(name="QuantityId")
	private int quantityId;
	
	@Column(name="PESOValue")
	private String pesoValue;
	
	public String getPesoValue() {
		return pesoValue;
	}

	public void setPesoValue(String pesoValue) {
		this.pesoValue = pesoValue;
	}

	public int getContractorId() {
		return contractorId;
	}

	public void setContractorId(int contractorId) {
		this.contractorId = contractorId;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public int getQuantityId() {
		return quantityId;
	}

	public void setQuantityId(int quantityId) {
		this.quantityId = quantityId;
	}

	public IoclTruckregistrationDetail() {
	}
	
	public Date getInsuranceExpDate() {
		return insuranceExpDate;
	}

	public void setInsuranceExpDate(Date insuranceExpDate) {
		this.insuranceExpDate = insuranceExpDate;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	public Date getPermitExpDate() {
		return permitExpDate;
	}

	public void setPermitExpDate(Date permitExpDate) {
		this.permitExpDate = permitExpDate;
	}

	public String getInsuranceNo() {
		return insuranceNo;
	}

	public void setInsuranceNo(String insuranceNo) {
		this.insuranceNo = insuranceNo;
	}

	public int getTruckId() {
		return this.truckId;
	}

	public void setTruckId(int truckId) {
		this.truckId = truckId;
	}

	public String getCustomer() {
		return this.customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getDriverLicNo() {
		return this.driverLicNo;
	}

	public void setDriverLicNo(String driverLicNo) {
		this.driverLicNo = driverLicNo;
	}

	public String getDriverName() {
		return this.driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getMobileNumber() {
		return this.mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getTruckNo() {
		return this.truckNo;
	}

	public void setTruckNo(String truckNo) {
		this.truckNo = truckNo;
	}
}