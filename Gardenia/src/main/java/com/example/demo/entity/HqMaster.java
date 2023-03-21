package com.example.demo.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

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
	
	@ManyToOne
    @JoinColumn(name = "parentHq")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	HqMaster parentHq;
	
	@ManyToOne
    @JoinColumn(name = "regionId")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	Region region;
	
	@Transient
	private List<Map<String, Object>> regionList;
	
	@Transient
	private String regionId;
	
	@Transient
	private String parentHqId;
	
	public List<Map<String, Object>> getRegionList() {
		return regionList;
	}
	
	public String getRegionId() {
		return regionId;
	}
	
	public String getParentHqId() {
		return parentHqId;
	}

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

	public Region getRegion() {
		return region;
	}
	
	public void setRegion(Region region) {
		this.region = region;
	}
	
	public HqMaster getParentHq() {
		return parentHq;
	}
	
	public void setParentHq(HqMaster parentHq) {
		this.parentHq = parentHq;
	}
	
	public HqMaster(String hqCode, String hqName, String hqDesignation,Region region,HqMaster parentHq) {
		super();
		this.hqCode = hqCode;
		this.hqName = hqName;
		this.hqDesignation = hqDesignation;
		this.region = region;
		this.parentHq = parentHq;
	}

	public HqMaster() {
		// TODO Auto-generated constructor stub
	}

	
	
}
