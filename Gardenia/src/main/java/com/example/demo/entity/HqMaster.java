package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "hqmaster")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HqMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, length = 20)
	private Long id;
	
	@Column(name="hqCode", nullable = false,columnDefinition = "TEXT")
	private String hqCode;
	
	@Column(name = "hqName", nullable = false,columnDefinition = "TEXT")
	private String hqName;
	
	@Column(name = "hqDesignation", nullable = false,columnDefinition = "TEXT")
	private String hqDesignation;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHqCode() {
		return hqCode;
	}

	public void setHqCode(String hqCode) {
		this.hqCode = hqCode;
	}

	public String getHqName() {
		return hqName;
	}

	public void setHqName(String hqName) {
		this.hqName = hqName;
	}

	public String getHqDesignation() {
		return hqDesignation;
	}

	public void setHqDesignation(String hqDesignation) {
		this.hqDesignation = hqDesignation;
	}

	public HqMaster(String hqCode, String hqName, String hqDesignation) {
		super();
		this.hqCode = hqCode;
		this.hqName = hqName;
		this.hqDesignation = hqDesignation;
	}

	public HqMaster() {
		// TODO Auto-generated constructor stub
	}

	
	
}
