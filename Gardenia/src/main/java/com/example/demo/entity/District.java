package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "district")
public class District {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	@Column(name = "district_code", nullable = false, length = 20)
    private String district_code;
	
	@Column(name = "district_name", nullable = false, length = 20)
    private String district_name;
	
	@Column(name = "region_name", nullable = false, length = 20)
	private String region_name;
	
	@Column(name = "region_code", nullable = false, length = 20)
    private String region_code;
	
	@OneToOne
    @JoinColumn(name="id")
    private Region region;
	
	@OneToOne(mappedBy = "district")
	private City city;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getDistrict_code() {
		return district_code;
	}

	public void setDistrict_code(String district_code) {
		this.district_code = district_code;
	}

	public String getDistrict_name() {
		return district_name;
	}

	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}

	public String getRegion_code() {
		return region_code;
	}

	public void setRegion_code(String region_code) {
		this.region_code = region_code;
	}

	public String getRegion_name() {
		return region_name;
	}

	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}

	public District(Long id, String district_code, String district_name, String region_name, String region_code) {
		super();
		Id = id;
		this.district_code = district_code;
		this.district_name = district_name;
		this.region_name = region_name;
		this.region_code = region_code;
	}
	
	public District() {
		// TODO Auto-generated constructor stub
	}
	
	
}
