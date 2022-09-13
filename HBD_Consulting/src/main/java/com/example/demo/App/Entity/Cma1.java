package com.example.demo.App.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cma1")
public class Cma1 {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	@Column(name = "name_of_bank", nullable = false, length = 20)
    private String name_of_bank;
	
	@Column(name = "name_of_facility", nullable = false, length = 20)
    private String name_of_facility;
	
	@Column(name = "existing_limits", nullable = false, length = 20)
    private String existing_limits;
	
	@Column(name = "extent_limits", nullable = false, length = 20)
    private String extent_limits;
	
	@Column(name = "balance", nullable = false, length = 20)
    private String balance;
	
	@Column(name = "limits", nullable = false, length = 20)
    private String limits;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getName_of_bank() {
		return name_of_bank;
	}

	public void setName_of_bank(String name_of_bank) {
		this.name_of_bank = name_of_bank;
	}

	public String getName_of_facility() {
		return name_of_facility;
	}

	public void setName_of_facility(String name_of_facility) {
		this.name_of_facility = name_of_facility;
	}

	public String getExisting_limits() {
		return existing_limits;
	}

	public void setExisting_limits(String existing_limits) {
		this.existing_limits = existing_limits;
	}

	public String getExtent_limits() {
		return extent_limits;
	}

	public void setExtent_limits(String extent_limits) {
		this.extent_limits = extent_limits;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getLimits() {
		return limits;
	}

	public void setLimits(String limits) {
		this.limits = limits;
	}

	public Cma1(Long id, String name_of_bank, String name_of_facility, String existing_limits,
			String extent_limits, String balance, String limits) {
		super();
		this.Id = id;
		this.name_of_bank = name_of_bank;
		this.name_of_facility = name_of_facility;
		this.existing_limits = existing_limits;
		this.extent_limits = extent_limits;
		this.balance = balance;
		this.limits = limits;
	}
	
	public Cma1() {
		// TODO Auto-generated constructor stub
	}
}

