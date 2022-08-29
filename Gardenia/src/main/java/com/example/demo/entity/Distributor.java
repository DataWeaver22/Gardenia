package com.example.demo.entity;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "distributortable")
public class Distributor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
		
	@Column(name = "distributor_name", nullable = false, length = 20)
	private String distributor_name;
	
	@Column(name = "distributor_code", nullable = false, length = 20)
	private String distributor_code;
	
	@Column(name = "distributor_type", nullable = false, length = 20)
	private String distributor_type;
	
	@Column(name = "gstin", length = 20)
	private String gstin;
	
	@Column(name = "pan",  length = 20)
	private String pan;
	
	@Column(name = "contact", length = 20)
	private String contact;
	
	@Column(name = "mobile", length = 20)
	private String mobile;
	
	@Column(name = "phone", length = 20)
	private String phone;
	
	@Column(name = "email", length = 20)
	private String email;
	
	@Column(name = "address", length = 20)
	private String address;
	
	@Column(name = "region_name", length = 20)
	private String region_name;
	
	@Column(name = "region_id", length = 20)
	private String region_id;
	
	@Column(name = "state_name", length = 20)
	private String state_name;
	
	@Column(name = "state_id", length = 20)
	private String state_id;
	
	@Column(name = "city_name", length = 20)
	private String city_name;
	
	@Column(name = "city_id", length = 20)
	private String city_id;
	
	@Column(name = "supp_name", length = 20)
	private String supp_name;
	
	@Column(name = "supp_code", length = 20)
	private String supp_code;
	
	@Column(name = "status", length = 20)
	private String status;
	
	@Column(name = "create_date", length = 20)
	private LocalDateTime create_date;
	
	@Column(name = "inactive_date", length = 20)
	private LocalDateTime inactive_date;
	
	@Column(name = "documents", length = 64)
	private String documents;
	
	@OneToMany(mappedBy="distributor",targetEntity = DistNew.class)
	public List<DistNew> distNew;
	
	private String fileName;

    private String fileType;
	
	@Column(name = "service_status", length = 20)
	private String service_status;
	
	@Column(name = "brand_list", length = 20)
	private String brand_list;
	
	@Column(name = "assign_tso", length = 20)
	private String assign_tso;
	
	@Column(name = "assign_tso_id", length = 20)
	private String assign_tso_id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDistributor_name() {
		return distributor_name;
	}

	public void setDistributor_name(String distributor_name) {
		this.distributor_name = distributor_name;
	}

	public String getDistributor_code() {
		return distributor_code;
	}

	public void setDistributor_code(String distributor_code) {
		this.distributor_code = distributor_code;
	}

	public String getDistributor_type() {
		return distributor_type;
	}

	public void setDistributor_type(String distributor_type) {
		this.distributor_type = distributor_type;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRegion_name() {
		return region_name;
	}

	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}

	public String getRegion_id() {
		return region_id;
	}

	public void setRegion_id(String region_id) {
		this.region_id = region_id;
	}

	public String getState_name() {
		return state_name;
	}

	public void setState_name(String state_name) {
		this.state_name = state_name;
	}

	public String getState_id() {
		return state_id;
	}

	public void setState_id(String state_id) {
		this.state_id = state_id;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getSupp_name() {
		return supp_name;
	}

	public void setSupp_name(String supp_name) {
		this.supp_name = supp_name;
	}

	public String getSupp_code() {
		return supp_code;
	}

	public void setSupp_code(String supp_code) {
		this.supp_code = supp_code;
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

	public String getDocuments() {
		return documents;
	}

	public void setDocuments(String documents) {
		this.documents = documents;
	}

	public String getService_status() {
		return service_status;
	}

	public void setService_status(String service_status) {
		this.service_status = service_status;
	}

	public String getBrand_list() {
		return brand_list;
	}

	public void setBrand_list(String brand_list) {
		this.brand_list = brand_list;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getAssign_tso() {
		return assign_tso;
	}

	public void setAssign_tso(String assign_tso) {
		this.assign_tso = assign_tso;
	}

	public String getAssign_tso_id() {
		return assign_tso_id;
	}
	
	public void setAssign_tso_id(String assign_tso_id) {
		this.assign_tso_id = assign_tso_id;
	}
	
	public Distributor(Long id, String distributor_name, String distributor_code, String distributor_type, String gstin,
			String pan, String contact, String mobile, String phone, String email, String address, String region_name,
			String region_id, String state_name, String state_id, String city_name, String city_id, String supp_name,
			String supp_code, String status, LocalDateTime create_date, LocalDateTime inactive_date,
			String service_status, String brand_list, String assign_tso,String assign_tso_id) {
		super();
		this.id = id;
		this.distributor_name = distributor_name;
		this.distributor_code = distributor_code;
		this.distributor_type = distributor_type;
		this.gstin = gstin;
		this.pan = pan;
		this.contact = contact;
		this.mobile = mobile;
		this.phone = phone;
		this.email = email;
		this.address = address;
		this.region_name = region_name;
		this.region_id = region_id;
		this.state_name = state_name;
		this.state_id = state_id;
		this.city_name = city_name;
		this.city_id = city_id;
		this.supp_name = supp_name;
		this.supp_code = supp_code;
		this.status = status;
		this.create_date = create_date;
		this.inactive_date = inactive_date;
		this.service_status = service_status;
		this.brand_list = brand_list;
		this.assign_tso = assign_tso;
		this.assign_tso_id = assign_tso_id;
	}
	
	public Distributor() {
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * public Distributor(String fileName, String fileType, String documents) {
	 * this.fileName = fileName; this.fileType = fileType; this.documents =
	 * documents; }
	 */
	@Transient
    public String getPhotosImagePath() {
        if (documents == null || id == null) return null;
         
        return "/user-photos/" + id + "/" + documents;
    }

}

	
