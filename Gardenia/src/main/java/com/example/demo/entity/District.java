package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "district")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class District {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long Id;
	
	@Column(name = "districtCode", columnDefinition = "Text")
	private String districtCode;
	
	@Column(name = "districtName", columnDefinition = "Text")
    private String districtName;
	
	@ManyToOne
    @JoinColumn(name = "regionId")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	Region region;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public District(Long id, String districtCode, String districtName, Region region) {
		super();
		Id = id;
		this.districtCode = districtCode;
		this.districtName = districtName;
		this.region = region;
	}
	
	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public District() {
		// TODO Auto-generated constructor stub
	}
	
	
}
