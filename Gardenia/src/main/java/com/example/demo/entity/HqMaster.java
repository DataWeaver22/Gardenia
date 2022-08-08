package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "hqmaster")
public class HqMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="hq_code", nullable = false, length = 20)
	private String hq_code;
	
	@Column(name = "hq_name", nullable = false, length = 20)
	private String hq_name;
	
	@Column(name = "hq_designation", nullable = false, length = 20)
	private String hq_designation;
	
	@OneToOne(mappedBy = "hqmaster")
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHq_code() {
		return hq_code;
	}

	public void setHq_code(String hq_code) {
		this.hq_code = hq_code;
	}

	public String getHq_name() {
		return hq_name;
	}

	public void setHq_name(String hq_name) {
		this.hq_name = hq_name;
	}

	public String getHq_designation() {
		return hq_designation;
	}

	public void setHq_designation(String hq_designation) {
		this.hq_designation = hq_designation;
	}

	public HqMaster(String hq_code, String hq_name, String hq_designation) {
		super();
		this.hq_code = hq_code;
		this.hq_name = hq_name;
		this.hq_designation = hq_designation;
	}

	public HqMaster() {
		// TODO Auto-generated constructor stub
	}

	
	
}
