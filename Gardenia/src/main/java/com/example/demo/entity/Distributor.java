package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "distributorTable")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Distributor{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
		
	@Column(name = "distributorName", columnDefinition = "Text")
	private String distributorName;
	
	@Column(name = "distributorCode", columnDefinition = "Text")
	private String distributorCode;
	
	@Column(name = "distributorType", columnDefinition = "Text")
	private String distributorType;
	
	@Column(name = "gstin",columnDefinition = "Text")
	private String gstin;
	
	@Column(name = "pan",  columnDefinition = "Text")
	private String pan;
	
	@Column(name = "contact", columnDefinition = "Text")
	private String contact;
	
	@Column(name = "mobile", columnDefinition = "Text")
	private String mobile;
	
	@Column(name = "phone", columnDefinition = "Text")
	private String phone;
	
	@Column(name = "email", columnDefinition = "Text")
	private String email;
	
	@Column(name = "billingAddress", columnDefinition = "Text")
	private String billingAddress;
	
	@Column(name = "deliveryAddress", columnDefinition = "Text")
	private String deliveryAddress;
	
	@Column(name = "suppName", columnDefinition = "Text")
	private String suppName;
	
	@Column(name = "suppCode", columnDefinition = "Text")
	private String suppCode;
	
	@Column(name = "status", columnDefinition = "Text")
	private String status;
	
	@Column(name = "pinCode", columnDefinition = "Text")
	private String pinCode;
	
	@Column(name = "create_date")
	private LocalDateTime create_date;
	
	@Column(name = "inactive_date")
	private LocalDateTime inactive_date;
	
	@Column(name = "updatedDateTime")
	private LocalDateTime updatedDateTime;
	
	@Column(name = "serviceStatus", columnDefinition = "Text")
	private String serviceStatus;
	
	@Column(name = "approvalStatus", columnDefinition = "Text")
	private String approvalStatus;
	
	@Column(name = "rejectReason", columnDefinition = "Text")
	private String rejectReason;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regionId")
	Region region;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stateId")
	State state;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cityId")
	City city;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "districtId")
	District district;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hqId")
	HqMaster hqMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gstFileId")
	FileDB gstFile;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "panFileId")
	FileDB panFile;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scannedCopyFileId")
	FileDB scannedCopyFile;
	
	@Transient
	private List<Integer> brandList;
	
	@Transient
	private List<Map<String, Object>> currentBusinessAssociation;
	
	public List<Map<String, Object>> getCurrentBusinessAssociation() {
		return currentBusinessAssociation;
	}
	
	@Transient
	private String regionId;
	
	@Transient
	private String stateId;
	
	@Transient
	private String districtId;
	
	@Transient
	private String cityId;
	
	@Transient
	private String hqId;
	
	public List<Integer> getBrandList() {
		return brandList;
	}
	
	public String getHqId() {
		return hqId;
	}

	public String getRegionId() {
		return regionId;
	}
	
	public String getStateId() {
		return stateId;
	}
	
	public String getDistrictId() {
		return districtId;
	}
	
	public String getCityId() {
		return cityId;
	}
	
	public District getDistrict() {
		return district;
	}
	
	public void setDistrict(District district) {
		this.district = district;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDistributorName() {
		return distributorName;
	}

	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}

	public String getDistributorCode() {
		return distributorCode;
	}

	public void setDistributorCode(String distributorCode) {
		this.distributorCode = distributorCode;
	}

	public String getDistributorType() {
		return distributorType;
	}

	public void setDistributorType(String distributorType) {
		this.distributorType = distributorType;
	}

	public String getGstin() {
		return gstin;
	}

	public void setGstin(String gstin) {
		this.gstin = gstin;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBillingAddress() {
		return billingAddress;
	}
	
	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}
	
	public String getDeliveryAddress() {
		return deliveryAddress;
	}
	
	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getSuppName() {
		return suppName;
	}

	public void setSuppName(String suppName) {
		this.suppName = suppName;
	}

	public String getSuppCode() {
		return suppCode;
	}

	public void setSuppCode(String suppCode) {
		this.suppCode = suppCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreate_date() {
		return create_date;
	}

	public void setCreate_date(LocalDateTime create_date) {
		this.create_date = create_date;
	}

	public LocalDateTime getInactive_date() {
		return inactive_date;
	}

	public void setInactive_date(LocalDateTime inactive_date) {
		this.inactive_date = inactive_date;
	}

	public String getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
	
	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	
	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public HqMaster getHqMaster() {
		return hqMaster;
	}
	
	public void setHqMaster(HqMaster hqMaster) {
		this.hqMaster = hqMaster;
	}

	public LocalDateTime getUpdatedDateTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateTime(LocalDateTime updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

	public FileDB getGstFile() {
		return gstFile;
	}

	public void setGstFile(FileDB gstFile) {
		this.gstFile = gstFile;
	}

	public FileDB getPanFile() {
		return panFile;
	}

	public void setPanFile(FileDB panFile) {
		this.panFile = panFile;
	}

	public FileDB getScannedCopyFile() {
		return scannedCopyFile;
	}
	
	public void setScannedCopyFile(FileDB scannedCopyFile) {
		this.scannedCopyFile = scannedCopyFile;
	}
	
	public String getPinCode() {
		return pinCode;
	}
	
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	
	public String getRejectReason() {
		return rejectReason;
	}
	
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public Distributor(Long id, String distributorName, String distributorCode, String distributorType, String gstin,
			String pan, String contact, String mobile, String phone, String email, String billingAddress, String suppName,
			String suppCode, String status, LocalDateTime create_date, LocalDateTime inactive_date,LocalDateTime updatedDateTime,
			String serviceStatus, String approvalStatus,Region region,State state,City city,HqMaster hqMaster,FileDB gstFile,FileDB panFile,
			FileDB scannedFile,District district,String pinCode,String deliveryAddress,String rejectReason) {
		super();
		this.id = id;
		this.distributorName = distributorName;
		this.distributorCode = distributorCode;
		this.distributorType = distributorType;
		this.gstin = gstin;
		this.pan = pan;
		this.contact = contact;
		this.mobile = mobile;
		this.phone = phone;
		this.email = email;
		this.billingAddress = billingAddress;
		this.suppName = suppName;
		this.suppCode = suppCode;
		this.status = status;
		this.create_date = create_date;
		this.inactive_date = inactive_date;
		this.serviceStatus = serviceStatus;
		this.approvalStatus = approvalStatus;
		this.region = region;
		this.state = state;
		this.city = city;
		this.hqMaster = hqMaster;
		this.updatedDateTime = updatedDateTime;
		this.gstFile = gstFile;
		this.panFile = panFile;
		this.scannedCopyFile = scannedFile;
		this.district = district;
		this.pinCode = pinCode;
		this.deliveryAddress = deliveryAddress;
		this.rejectReason = rejectReason;
	}
	
	public Distributor() {
		// TODO Auto-generated constructor stub
	}

}

	
