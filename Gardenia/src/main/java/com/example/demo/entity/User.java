package com.example.demo.entity;

import java.beans.Transient;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
		
	@Column(name = "login",length = 20)
	private String login;
	
	@Column(name = "firstName",length = 20)
	private String firstName;
	
	@Column(name = "lastName",length = 20)
	private String lastName;
	
	@Column(name = "fullName",length = 20)
	private String fullName;
	
	@Column(name = "emp_code", length = 20)
	private String emp_code;
	
	@Column(name = "team", length = 20)
	private String team;
	
	@Column(name = "roles", length = 20)
	private String roles;
	
	@Column(name = "status", length = 20)
	private String status;
	
	@Column(name = "create_date", length = 20)
	private LocalDateTime create_date;
	
	@Column(name = "resign_date", length = 20)
	private LocalDateTime resign_date;
	
	@Column(name = "region_name", length = 20)
	private String region_name;

	@Column(name = "region_id", length = 20)
	private String region_id;
	
	@Column(name = "state_name", length = 20)
	private String state_name;
	
	@Column(name = "state_id", length = 20)
	private String state_id;
	
	@Column(name = "area_name",length = 20)
	private String area_name;
	
	@Column(name = "area_id", length = 20)
	private String area_id;
	
	@Column(name = "hq_name", length = 20)
	private String hq_name;
	
	@Column(name = "hq_id", length = 20)
	private String hq_id;
	
	@Column(name = "documents", length = 20)
	private String documents;
	
	@Column(name = "approval_status", length=20)
	private String approval_status;
	
	@Column(name = "approval_action", length=20)
	private String approval_action;
	
	@Column(name = "title", length = 20)
	private String title;
	
	@Column(name = "employee_name", length = 20)
	private String employee_name;
	
	@Column(name = "marital_status", length = 20)
	private String marital_status;
	
	@Column(name = "gender", length = 20)
	private String gender;
	
	@Column(name = "birth_date", length = 20)
	private String birth_date;
	
	@Column(name = "join_date", length = 20)
	private String join_date;
	
	@Column(name = "grade", length = 20)
	private String grade;
	
	@Column(name = "branch", length = 20)
	private String branch;
	
	@Column(name = "department", length = 20)
	private String department;
	
	@Column(name = "payment_mode", length = 20)
	private String payment_mode;
	
	@Column(name = "email", length = 20)
	private String email;
	
	@Column(name = "middle_name", length = 20)
	private String middle_name;
	
	@Column(name = "process_start_date", length = 20)
	private String process_start_date;
	
	@Column(name = "company_code", length = 20)
	private String company_code;
	
	@Column(name = "rsm_id", length = 20)
	private String rsm_id;
	
	@Column(name = "rsm", length = 20)
	private String rsm;
	
	@Column(name = "asm_id", length = 20)
	private String asm_id;
	
	@Column(name = "asm", length = 20)
	private String asm;
	
	@Column(name = "ase_id", length = 20)
	private String ase_id;
	
	@Column(name = "ase", length = 20)
	private String ase;
	
	@OneToOne
    @JoinColumn(name="id")
    private State state;
	
	@OneToOne
    @JoinColumn(name="id")
    private Region region;
	
	@OneToOne
    @JoinColumn(name="id")
    private HqMaster hqmaster;
	
	@OneToOne
    @JoinColumn(name="id")
    private Area area;

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

	public String getEmp_code() {
		return emp_code;
	}

	public void setEmp_code(String emp_code) {
		this.emp_code = emp_code;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
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

	public LocalDateTime getResign_date() {
		return resign_date;
	}

	public void setResign_date(LocalDateTime resign_date) {
		this.resign_date = resign_date;
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

	public String getHq_name() {
		return hq_name;
	}

	public void setHq_name(String hq_name) {
		this.hq_name = hq_name;
	}

	public String getHq_id() {
		return hq_id;
	}

	public void setHq_id(String hq_id) {
		this.hq_id = hq_id;
	}

	public String getDocuments() {
		return documents;
	}

	public void setDocuments(String documents) {
		this.documents = documents;
	}
	
	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public String getArea_id() {
		return area_id;
	}

	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getApproval_status() {
		return approval_status;
	}

	public void setApproval_status(String approval_status) {
		this.approval_status = approval_status;
	}

	public String getApproval_action() {
		return approval_action;
	}

	public void setApproval_action(String approval_action) {
		this.approval_action = approval_action;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEmployee_name() {
		return employee_name;
	}

	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}

	public String getMarital_status() {
		return marital_status;
	}

	public void setMarital_status(String marital_status) {
		this.marital_status = marital_status;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirth_date() {
		return birth_date;
	}

	public void setBirth_date(String birth_date) {
		this.birth_date = birth_date;
	}

	public String getJoin_date() {
		return join_date;
	}

	public void setJoin_date(String join_date) {
		this.join_date = join_date;
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

	public String getPayment_mode() {
		return payment_mode;
	}

	public void setPayment_mode(String payment_mode) {
		this.payment_mode = payment_mode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMiddle_name() {
		return middle_name;
	}

	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
	}

	public String getProcess_start_date() {
		return process_start_date;
	}

	public void setProcess_start_date(String process_start_date) {
		this.process_start_date = process_start_date;
	}
	
	public String getCompany_code() {
		return company_code;
	}

	public void setCompany_code(String company_code) {
		this.company_code = company_code;
	}

	public String getRsm_id() {
		return rsm_id;
	}

	public void setRsm_id(String rsm_id) {
		this.rsm_id = rsm_id;
	}

	public String getRsm() {
		return rsm;
	}

	public void setRsm(String rsm) {
		this.rsm = rsm;
	}

	public String getAsm_id() {
		return asm_id;
	}

	public void setAsm_id(String asm_id) {
		this.asm_id = asm_id;
	}

	public String getAsm() {
		return asm;
	}

	public void setAsm(String asm) {
		this.asm = asm;
	}

	public String getAse_id() {
		return ase_id;
	}

	public void setAse_id(String ase_id) {
		this.ase_id = ase_id;
	}

	public String getAse() {
		return ase;
	}

	public void setAse(String ase) {
		this.ase = ase;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public HqMaster getHqmaster() {
		return hqmaster;
	}

	public void setHqmaster(HqMaster hqmaster) {
		this.hqmaster = hqmaster;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public User(Long id,String login,String firstName, String lastName, String emp_code,
			String team, String roles, String status, LocalDateTime create_date, LocalDateTime resign_date, String region_name,
			String region_id, String state_name, String state_id, String hq_name, String hq_id, String documents,String area_id,String fullName,
			String approval_status, String approval_action,String title, String employee_name, String marital_status, String gender, String birth_date,
			String join_date, String grade, String branch, String department, String payment_mode, String email,
			String middle_name, String process_start_date, String company_code,String rsm_id, String rsm, String asm_id, String asm, String ase_id, String ase) {
		super();
		this.id = id;
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emp_code = emp_code;
		this.team = team;
		this.roles = roles;
		this.status = status;
		this.create_date = create_date;
		this.resign_date = resign_date;
		this.region_name = region_name;
		this.region_id = region_id;
		this.state_name = state_name;
		this.state_id = state_id;
		this.hq_name = hq_name;
		this.hq_id = hq_id;
		this.documents = documents;
		this.area_id=area_id;
		this.fullName = fullName;
		this.approval_status = approval_status;
		this.approval_action = approval_action;
		this.title = title;
		this.employee_name = employee_name;
		this.marital_status = marital_status;
		this.gender = gender;
		this.birth_date = birth_date;
		this.join_date = join_date;
		this.grade = grade;
		this.branch = branch;
		this.department = department;
		this.payment_mode = payment_mode;
		this.email = email;
		this.middle_name = middle_name;
		this.process_start_date = process_start_date;
		this.company_code = company_code;
		this.rsm_id = rsm_id;
		this.rsm = rsm;
		this.asm_id = asm_id;
		this.asm = asm;
		this.ase_id = ase_id;
		this.ase = ase;
	}

	public User() {
		// TODO Auto-generated constructor stub
	}
	
	@Transient
    public String getPhotosImagePath() {
        if (documents == null || id == null) return null;
         
        return "/user-photos/" + id + "/" + documents;
    }
		
}
