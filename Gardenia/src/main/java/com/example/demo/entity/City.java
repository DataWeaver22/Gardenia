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
@Table(name = "city")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class City {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long Id;
	
	@Column(name = "cityCode", columnDefinition = "Text")
    private String cityCode;
	
	@Column(name = "cityName", columnDefinition = "Text")
    private String cityName;
	
	@ManyToOne
    @JoinColumn(name = "districtId")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	District district;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public City(String cityCode, String cityName, District district) {
		super();
		this.cityCode = cityCode;
		this.cityName = cityName;
		this.district = district;
	}

	public City() {
		// TODO Auto-generated constructor stub
	}
	
	
}
