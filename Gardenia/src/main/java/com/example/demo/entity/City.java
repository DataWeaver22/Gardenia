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
@Table(name = "city")
public class City {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	@Column(name = "city_code", length = 20)
    private String city_code;
	
	@Column(name = "city_name", nullable = false, length = 20)
    private String city_name;
	
	@Column(name = "district_name", nullable = false, length = 20)
    private String district_name;
	
	@Column(name = "district_code", nullable = false, length = 20)
    private String district_code;
	
	@OneToOne
    @JoinColumn(name="id")
    private District district;
	
	@OneToOne(mappedBy = "city")
	private Area area;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getCity_code() {
		return city_code;
	}

	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getDistrict_name() {
		return district_name;
	}

	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}
	
	public String getDistrict_code() {
		return district_code;
	}

	public void setDistrict_code(String district_code) {
		this.district_code = district_code;
	}

	public City(String city_code, String city_name, String district_name, String district_code) {
		super();
		this.city_code = city_code;
		this.city_name = city_name;
		this.district_name = district_name;
		this.district_code = district_code;
	}

	public City() {
		// TODO Auto-generated constructor stub
	}
	
	
}
