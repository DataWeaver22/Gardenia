package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "area", indexes = {
		@Index(
			name="idx_areaName_city",
			columnList = "areaName,cityId",
			unique = true
		)
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Area {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long Id;
	
	@Column(name = "areaCode", columnDefinition = "Text")
    private String areaCode;
	
	@Column(name = "areaName", columnDefinition = "Text")
    private String areaName;
	
	@ManyToOne
    @JoinColumn(name = "cityId")
	City city;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	
	
	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Area(String areaCode, String areaName, City city) {
		super();
		this.areaCode = areaCode;
		this.areaName = areaName;
		this.city = city;
	}

	public Area() {
		// TODO Auto-generated constructor stub
	}
	
	
}
