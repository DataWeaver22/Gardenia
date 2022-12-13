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
@Table(name="distributorcode")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DistributorCode {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, length = 20)
	private Long Id;
	
	@Column(name = "regionCode", nullable = false, length = 20)
    private String regionCode;
	
	@OneToOne
    @JoinColumn(name = "regionDCId")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	Region regionDCId;
	
	@Column(name = "codenumber", nullable = false, length = 3)
	private String codeNumber;

	public String getCodeNumber() {
		return codeNumber;
	}

	public void setCodeNumber(String codeNumber) {
		this.codeNumber = codeNumber;
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

	
	public Region getRegion() {
		return regionDCId;
	}

	public void setRegion(Region regionDCId) {
		this.regionDCId = regionDCId;
	}

	public DistributorCode(Long id, String regionCode, String codeNumber,Region regionDCId) {
		super();
		Id = id;
		this.regionCode = regionCode;
		this.codeNumber = codeNumber;
		this.regionDCId = regionDCId;
	}
	
	public DistributorCode() {
		// TODO Auto-generated constructor stub
	}
	
	
}
