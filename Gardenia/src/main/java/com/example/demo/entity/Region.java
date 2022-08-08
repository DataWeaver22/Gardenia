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
@Table(name="region")
public class Region {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	@Column(name = "region_code", nullable = false, length = 20)
    private String region_code;

	@Column(name = "region_name", nullable = false, length = 20)
    private String region_name;
	
	@Column(name = "state_name", nullable = false, length = 20)
    private String state_name;
	
	@Column(name = "state_code", nullable = false, length = 20)
    private String state_code;
	
	@OneToOne
    @JoinColumn(name="id")
    private State state;
	
	@OneToOne(mappedBy = "region")
    private District district;
	
	@OneToOne(mappedBy = "region")
	private User user;
	
	@OneToOne(mappedBy = "region")
    private DistributorCode distributorCode;
	
	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
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

	public String getState_code() {
		return state_code;
	}

	public void setState_code(String state_code) {
		this.state_code = state_code;
	}
	
	public String getState_name() {
		return state_name;
	}

	public void setState_name(String state_name) {
		this.state_name = state_name;
	}

	public Region(String region_code, String region_name, String state_name, String state_code) {
		super();
		this.region_code = region_code;
		this.region_name = region_name;
		this.state_name = state_name;
		this.state_code = state_code;
	}

	public Region() {
		// TODO Auto-generated constructor stub
	}
	
	

	
	
}
