package com.example.demo.entity;

import java.security.KeyStore.PrivateKeyEntry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="distributorcode")
public class DistributorCode {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	@Column(name = "region_code", nullable = false, length = 20)
    private String region_code;
	
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

	public String getRegion_code() {
		return region_code;
	}

	public void setRegion_code(String region_code) {
		this.region_code = region_code;
	}

	public DistributorCode(Long id, String region_code, String codeNumber) {
		super();
		Id = id;
		this.region_code = region_code;
		this.codeNumber = codeNumber;
	}
	
	public DistributorCode() {
		// TODO Auto-generated constructor stub
	}
	
	@OneToOne
    @JoinColumn(name="id")
    private Region region;
	
	
}
