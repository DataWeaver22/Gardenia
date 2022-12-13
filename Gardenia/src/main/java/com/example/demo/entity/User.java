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

	@Column(name = "team", columnDefinition = "Text")
	private String team;

	@Column(name = "role", columnDefinition = "Text")
	private String role;

	@Column(name = "status", columnDefinition = "Text")
	private String status;

	@Column(name = "createDate")
	private LocalDateTime createDate;

	@Column(name = "resignDate")
	private LocalDateTime resignDate;
	
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
	private List<Map<String, Object>> userTargetDetailsList;
	
	public List<Map<String, Object>> getUserTargetDetailsList() {
		return userTargetDetailsList;
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

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
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

	public LocalDateTime getResignDate() {
		return resignDate;
	}

	public void setResignDate(LocalDateTime resignDate) {
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
	
	public User(Long id, String login, String firstName, String lastName, String empCode, String team, String role,
			String status, LocalDateTime createDate, LocalDateTime resignDate, String fullName, String approvalStatus,
			String title, String maritalStatus, String gender, LocalDate birthDate, LocalDate joinDate, String grade,
			String branch, String department, String paymentMode, String email, String middleName,
			LocalDate processStartDate, String companyCode, Region region, State state, District district, City city,
			Area area, HqMaster hqMaster, User rsm, User asm, User ase,LocalDateTime inactiveDateTime,LocalDateTime updatedDateTime,
			UserFileDB aadharFile,UserFileDB panFile,UserFileDB resumeFile,UserFileDB paySlipFile,UserFileDB bankStatementFile) {
		super();
		this.id = id;
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
		this.empCode = empCode;
		this.team = team;
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
	}

	public User() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", title=" + title + ", login=" + login + ", firstName=" + firstName + ", lastName="
				+ lastName + ", fullName=" + fullName + ", empCode=" + empCode + ", team=" + team + ", role=" + role
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
				+ aseId + ", hqId=" + hqId + ", userTargetDetailsList=" + userTargetDetailsList + "]";
	}

}
