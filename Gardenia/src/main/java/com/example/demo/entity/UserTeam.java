package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "userTeam")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class UserTeam {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hqId")
	HqMaster hqMaster;

	@Column(name = "designation", columnDefinition = "Text")
	private String designation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employeeId")
	User employee;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public HqMaster getHqMaster() {
		return hqMaster;
	}

	public void setHqMaster(HqMaster hqMaster) {
		this.hqMaster = hqMaster;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getEmployee() {
		return employee;
	}
	
	public void setEmployee(User employee) {
		this.employee = employee;
	}
	
	public UserTeam(Long id, HqMaster hqMaster, String designation, User user, User employee) {
		this.id = id;
		this.hqMaster = hqMaster;
		this.designation = designation;
		this.user = user;
		this.employee = employee;
	}

	public UserTeam() {
	}

	
}
