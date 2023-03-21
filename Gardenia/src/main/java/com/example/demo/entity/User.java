package com.example.demo.entity;

import java.time.LocalDate;
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
@Table(name = "user")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "title", columnDefinition = "Text")
	private String title;

	@Column(name = "login", columnDefinition = "Text")
	private String login;

	@Column(name = "firstName", columnDefinition = "Text")
	private String firstName;

	@Column(name = "lastName", columnDefinition = "Text")
	private String lastName;

	@Column(name = "fullName", columnDefinition = "Text")
	private String fullName;

	@Column(name = "empCode", columnDefinition = "Text")
	private String empCode;

	@Column(name = "reportingTo", columnDefinition = "Text")
	private String reportingTo;

	@Column(name = "role", columnDefinition = "Text")
	private String role;

	@Column(name = "status", columnDefinition = "Text")
	private String status;

	@Column(name = "createDate")
	private LocalDateTime createDate;

	@Column(name = "resignDate")
	private LocalDate resignDate;
	
	@Column(name = "inactiveDate")
	private LocalDateTime inactiveDate;
	
	@Column(name = "updatedDateTime")
	private LocalDateTime updatedDateTime;

	@Column(name = "approvalStatus", columnDefinition = "Text")
	private String approvalStatus;

	@Column(name = "maritalStatus", columnDefinition = "Text")
	private String maritalStatus;

	@Column(name = "gender", columnDefinition = "Text")
	private String gender;
	
	@Column(name = "aadharNo", columnDefinition = "Text")
	private String aadharNo;

	@Column(name = "birthDate")
	private LocalDate birthDate;

	@Column(name = "joinDate")
	private LocalDate joinDate;

	@Column(name = "grade", columnDefinition = "Text")
	private String grade;

	@Column(name = "branch", columnDefinition = "Text")
	private String branch;

	@Column(name = "department", columnDefinition = "Text")
	private String department;

	@Column(name = "paymentMode", columnDefinition = "Text")
	private String paymentMode;

	@Column(name = "email", columnDefinition = "Text")
	private String email;

	@Column(name = "middleName", columnDefinition = "Text")
	private String middleName;

	@Column(name = "processStartDate")
	private LocalDate processStartDate;

	@Column(name = "companyCode", columnDefinition = "Text")
	private String companyCode;
	
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
	@JoinColumn(name = "areaId")
	Area area;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rsmId")
	User rsm;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asmId")
	User asm;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "aseId")
	User ase;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hqId")
	HqMaster hqMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "aadharFile")
	UserFileDB aadharFile;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "panFile")
	UserFileDB panFile;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resumeFile")
	UserFileDB resumeFile;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "paySlipFile")
	UserFileDB paySlipFile;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bankStatementFile")
	UserFileDB bankStatementFile;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "beatPlanFile")
	UserFileDB beatPlanFile;
	
	@Transient
	private String regionId;
	
	@Transient
	private String stateId;
	
	@Transient
	private String districtId;
	
	@Transient
	private String cityId;
	
	@Transient
	private String areaId;
	
	@Transient
	private String rsmId;
	
	@Transient
	private String asmId;
	
	@Transient
	private String aseId;
	
	@Transient
	private String hqId;
	
	@Transient
	private List<Map<String, Object>> userTargetDetails;
	
	@Transient
	private List<Map<String, Object>> userTeam;
	
	public List<Map<String, Object>> getUserTeam() {
		return userTeam;
	}
	
	public List<Map<String, Object>> getUserTargetDetails() {
		return userTargetDetails;
	}
	
	public String getAadharNo() {
		return aadharNo;
	}
	
	public void setAadharNo(String aadharNo) {
		this.aadharNo = aadharNo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getReportingTo() {
		return reportingTo;
	}
	
	public void setReportingTo(String reportingTo) {
		this.reportingTo = reportingTo;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public LocalDate getResignDate() {
		return resignDate;
	}

	public void setResignDate(LocalDate resignDate) {
		this.resignDate = resignDate;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirth_date(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public LocalDate getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(LocalDate joinDate) {
		this.joinDate = joinDate;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public LocalDate getProcessStartDate() {
		return processStartDate;
	}

	public void setProcessStartDate(LocalDate processStartDate) {
		this.processStartDate = processStartDate;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
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

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public User getRsm() {
		return rsm;
	}

	public void setRsm(User rsm) {
		this.rsm = rsm;
	}

	public User getAsm() {
		return asm;
	}

	public void setAsm(User asm) {
		this.asm = asm;
	}

	public User getAse() {
		return ase;
	}

	public void setAse(User ase) {
		this.ase = ase;
	}

	public HqMaster getHqMaster() {
		return hqMaster;
	}

	public void setHqMaster(HqMaster hqMaster) {
		this.hqMaster = hqMaster;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public LocalDateTime getInactiveDate() {
		return inactiveDate;
	}

	public void setInactiveDate(LocalDateTime inactiveDate) {
		this.inactiveDate = inactiveDate;
	}

	public LocalDateTime getUpdatedDateTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateTime(LocalDateTime updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

	public UserFileDB getAadharFile() {
		return aadharFile;
	}

	public void setAadharFile(UserFileDB aadharFile) {
		this.aadharFile = aadharFile;
	}

	public UserFileDB getPanFile() {
		return panFile;
	}

	public void setPanFile(UserFileDB panFile) {
		this.panFile = panFile;
	}

	public UserFileDB getResumeFile() {
		return resumeFile;
	}

	public void setResumeFile(UserFileDB resumeFile) {
		this.resumeFile = resumeFile;
	}

	public UserFileDB getPaySlipFile() {
		return paySlipFile;
	}

	public void setPaySlipFile(UserFileDB paySlipFile) {
		this.paySlipFile = paySlipFile;
	}

	public UserFileDB getBankStatementFile() {
		return bankStatementFile;
	}

	public void setBankStatementFile(UserFileDB bankStatementFile) {
		this.bankStatementFile = bankStatementFile;
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
	
	public String getAreaId() {
		return areaId;
	}
	
	public String getHqId() {
		return hqId;
	}
	
	public String getRsmId() {
		return rsmId;
	}
	
	public String getAsmId() {
		return asmId;
	}
	
	public String getAseId() {
		return aseId;
	}
	
	public UserFileDB getBeatPlanFile() {
		return beatPlanFile;
	}
	
	public void setBeatPlanFile(UserFileDB beatPlanFile) {
		this.beatPlanFile = beatPlanFile;
	}
	
	public String getRejectReason() {
		return rejectReason;
	}
	
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
	
	// Last/Current Organisation Details
	@Column(name = "lcOrgName", columnDefinition = "Text")
	private String lcOrgName;

	@Column(name = "lcJoiningDate")
	private LocalDate lcJoiningDate;
	
	@Column(name = "lcLastDate")
	private LocalDate lcLastDate;
	
	@Column(name = "lcOrgDesignation", columnDefinition = "Text")
	private String lcOrgDesignation;
	
	@Column(name = "lcOrgSalary", columnDefinition = "Text")
	private String lcOrgSalary;
	
	@Column(name = "lcOrgManagerMobile", columnDefinition = "Text")
	private String lcOrgManagerMobile;
	
	@Column(name = "lcOrgManagerEmailID", columnDefinition = "Text")
	private String lcOrgManagerEmailID;
	
	@Column(name = "lcOrgHREmailID", columnDefinition = "Text")
	private String lcOrgHREmailID;
	
	//Recommendations
	@Column(name = "dateOfJoining")
	private LocalDate dateOfJoining;
	
	@Column(name = "designationRecommended", columnDefinition = "Text")
	private String designationRecommended;
	
	@Column(name = "goSalary", columnDefinition = "Text")
	private String goSalary;
	
	@Column(name = "growthPercentage", columnDefinition = "Text")
	private String growthPercentage;
	
	
	public String getlcOrgName() {
		return lcOrgName;
	}

	public void setlcOrgName(String lcOrgName) {
		this.lcOrgName = lcOrgName;
	}

	public LocalDate getlcJoiningDate() {
		return lcJoiningDate;
	}

	public void setlcJoiningDate(LocalDate lcJoiningDate) {
		this.lcJoiningDate = lcJoiningDate;
	}

	public LocalDate getlcLastDate() {
		return lcLastDate;
	}

	public void setlcLastDate(LocalDate lcLastDate) {
		this.lcLastDate = lcLastDate;
	}

	public String getlcOrgDesignation() {
		return lcOrgDesignation;
	}

	public void setlcOrgDesignation(String lcOrgDesignation) {
		this.lcOrgDesignation = lcOrgDesignation;
	}

	public String getlcOrgSalary() {
		return lcOrgSalary;
	}

	public void setlcOrgSalary(String lcOrgSalary) {
		this.lcOrgSalary = lcOrgSalary;
	}

	public String getlcOrgManagerMobile() {
		return lcOrgManagerMobile;
	}

	public void setlcOrgManagerMobile(String lcOrgManagerMobile) {
		this.lcOrgManagerMobile = lcOrgManagerMobile;
	}

	public String getlcOrgManagerEmailID() {
		return lcOrgManagerEmailID;
	}

	public void setlcOrgManagerEmailID(String lcOrgManagerEmailID) {
		this.lcOrgManagerEmailID = lcOrgManagerEmailID;
	}

	public String getlcOrgHREmailID() {
		return lcOrgHREmailID;
	}

	public void setlcOrgHREmailID(String lcOrgHREmailID) {
		this.lcOrgHREmailID = lcOrgHREmailID;
	}

	public LocalDate getDateOfJoining() {
		return dateOfJoining;
	}

	public void setDateOfJoining(LocalDate dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	public String getDesignationRecommended() {
		return designationRecommended;
	}

	public void setDesignationRecommended(String designationRecommended) {
		this.designationRecommended = designationRecommended;
	}

	public String getgoSalary() {
		return goSalary;
	}

	public void setgoSalary(String goSalary) {
		this.goSalary = goSalary;
	}

	public String getGrowthPercentage() {
		return growthPercentage;
	}

	public void setGrowthPercentage(String growthPercentage) {
		this.growthPercentage = growthPercentage;
	}

	public User(Long id, String login, String firstName, String lastName, String empCode, String reportingTo, String role,
			String status, LocalDateTime createDate, LocalDate resignDate, String fullName, String approvalStatus,
			String title, String maritalStatus, String gender, LocalDate birthDate, LocalDate joinDate, String grade,
			String branch, String department, String paymentMode, String email, String middleName,
			LocalDate processStartDate, String companyCode, Region region, State state, District district, City city,
			Area area, HqMaster hqMaster, User rsm, User asm, User ase,LocalDateTime inactiveDateTime,LocalDateTime updatedDateTime,
			UserFileDB aadharFile,UserFileDB panFile,UserFileDB resumeFile,UserFileDB paySlipFile,UserFileDB bankStatementFile,
			String lcOrgName,LocalDate lcJoiningDate,LocalDate lcLastDate,String lcDesignation,String lcOrgSalary, String lcOrgManagerMobile,
			String lcOrgManagerEmailID, String lcOrgHREmailID,LocalDate dateOfJoining,String designationRecommended,String goSalary,String growthPercentage,
			String rejectReason, UserFileDB beatPlanFile, String aadharNo) {
		super();
		this.id = id;
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
		this.empCode = empCode;
		this.reportingTo = reportingTo;
		this.role = role;
		this.status = status;
		this.createDate = createDate;
		this.resignDate = resignDate;
		this.fullName = fullName;
		this.approvalStatus = approvalStatus;
		this.title = title;
		this.maritalStatus = maritalStatus;
		this.gender = gender;
		this.birthDate = birthDate;
		this.joinDate = joinDate;
		this.grade = grade;
		this.branch = branch;
		this.department = department;
		this.paymentMode = paymentMode;
		this.email = email;
		this.middleName = middleName;
		this.processStartDate = processStartDate;
		this.companyCode = companyCode;
		this.region = region;
		this.state = state;
		this.district = district;
		this.city = city;
		this.area = area;
		this.hqMaster = hqMaster;
		this.rsm = rsm;
		this.asm = asm;
		this.ase = ase;
		this.inactiveDate = inactiveDateTime;
		this.updatedDateTime = updatedDateTime;
		this.aadharFile = aadharFile;
		this.panFile = panFile;
		this.resumeFile = resumeFile;
		this.paySlipFile = paySlipFile;
		this.bankStatementFile = bankStatementFile;
		this.lcOrgName = lcOrgName;
		this.lcJoiningDate = lcJoiningDate;
		this.lcLastDate = lcLastDate;
		this.lcOrgDesignation = lcDesignation;
		this.lcOrgSalary = lcOrgSalary;
		this.lcOrgManagerMobile = lcOrgManagerMobile;
		this.lcOrgManagerEmailID = lcOrgManagerEmailID;
		this.lcOrgHREmailID = lcOrgHREmailID;
		this.dateOfJoining = dateOfJoining;
		this.designationRecommended = designationRecommended;
		this.goSalary = goSalary;
		this.growthPercentage = growthPercentage;
		this.rejectReason = rejectReason;
		this.beatPlanFile = beatPlanFile;
		this.aadharNo = aadharNo;
	}

	public User() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", title=" + title + ", login=" + login + ", firstName=" + firstName + ", lastName="
				+ lastName + ", fullName=" + fullName + ", empCode=" + empCode + ", reportingTo=" + reportingTo + ", role=" + role
				+ ", status=" + status + ", createDate=" + createDate + ", resignDate=" + resignDate + ", inactiveDate="
				+ inactiveDate + ", updatedDateTime=" + updatedDateTime + ", approvalStatus=" + approvalStatus
				+ ", maritalStatus=" + maritalStatus + ", gender=" + gender + ", birthDate=" + birthDate + ", joinDate="
				+ joinDate + ", grade=" + grade + ", branch=" + branch + ", department=" + department + ", paymentMode="
				+ paymentMode + ", email=" + email + ", middleName=" + middleName + ", processStartDate="
				+ processStartDate + ", companyCode=" + companyCode + ", region=" + region + ", state=" + state
				+ ", city=" + city + ", district=" + district + ", area=" + area + ", rsm=" + rsm + ", asm=" + asm
				+ ", ase=" + ase + ", hqMaster=" + hqMaster + ", aadharFile=" + aadharFile + ", panFile=" + panFile
				+ ", resumeFile=" + resumeFile + ", paySlipFile=" + paySlipFile + ", bankStatementFile="
				+ bankStatementFile + ", regionId=" + regionId + ", stateId=" + stateId + ", districtId=" + districtId
				+ ", cityId=" + cityId + ", areaId=" + areaId + ", rsmId=" + rsmId + ", asmId=" + asmId + ", aseId="
				+ aseId + ", hqId=" + hqId + ", userTargetDetails=" + userTargetDetails + "]";
	}

}
