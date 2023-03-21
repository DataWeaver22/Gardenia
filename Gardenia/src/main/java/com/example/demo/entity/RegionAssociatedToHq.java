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
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "regionAssociatedToHq")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RegionAssociatedToHq {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, length = 20)
	private Long Id;
	
	@ManyToOne
    @JoinColumn(name = "hqId")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	HqMaster hqMaster;
	
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

	public HqMaster getHqMaster() {
		return hqMaster;
	}

	public void setHqMaster(HqMaster hqMaster) {
		this.hqMaster = hqMaster;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public RegionAssociatedToHq(Long id, HqMaster hqMaster, Region region) {
		Id = id;
		this.hqMaster = hqMaster;
		this.region = region;
	}

	public RegionAssociatedToHq() {
	}
	
	
}
