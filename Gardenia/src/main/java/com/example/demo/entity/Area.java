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
@Table(name = "area")
public class Area {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	@Column(name = "area_code", nullable = false, length = 20)
    private String area_code;
	
	@Column(name = "area_name", nullable = false, length = 20)
    private String area_name;
	
	@Column(name = "city_name", nullable = false, length = 20)
    private String city_name;
	
	@Column(name = "city_code", nullable = false, length = 20)
    private String city_code;
	
	public String getCity_code() {
		return city_code;
	}

	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}

	@OneToOne
    @JoinColumn(name="id")
    private City city;
	
	@OneToOne(mappedBy = "area")
	private User user;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getArea_code() {
		return area_code;
	}

	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}

	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	
	public Area(String area_code, String area_name, String city_name) {
		super();
		this.area_code = area_code;
		this.area_name = area_name;
		this.city_name = city_name;
	}

	public Area() {
		// TODO Auto-generated constructor stub
	}
	
	
}
