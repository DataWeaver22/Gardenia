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
@Table(name="region")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Region {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, length = 20)
	private Long Id;
	
	@Column(name = "regionCode", nullable = false, length = 20)
    private String regionCode;

	@Column(name = "regionName", nullable = false, length = 20)
    private String regionName;
	
	@ManyToOne
    @JoinColumn(name = "stateId")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	State state;
	
	@Transient
	private List<Integer> stateList;
	
	@Transient
	private String stateId;
	
	public String getStateId() {
		return stateId;
	}
	
	public List<Integer> getStateList() {
		return stateList;
	}
	
	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	
	

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Region(String regionCode, String regionName, State state) {
		super();
		this.regionCode = regionCode;
		this.regionName = regionName;
		this.state = state;
	}

	public Region() {
		// TODO Auto-generated constructor stub
	}
	
	

	
	
}
