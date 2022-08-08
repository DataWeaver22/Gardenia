package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "country")
public class Country {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	
	@Column(name = "id", nullable = false, length = 20)
    private Long id;
	
	@Column(name = "country_code", nullable = false, length = 20)
    private String country_code;
	
	@Column(name = "country_name", nullable = false, length = 20)
    private String country_name;
	
	@OneToOne(mappedBy = "country")
    private State state;

    
	
	/*
	 * public Long getCountryId() { return CountryId; }
	 */
    
    public Country() {
		// TODO Auto-generated constructor stub
	 
	}
    
    public Country(Long id, String country_code, String country_name) {
		super();
		this.id = id;
		this.country_code = country_code;
		this.country_name = country_name;
	}

	/*
	 * public void setId(Long countryid) { CountryId = countryid; }
	 */

    public Long getId() {
		return id;
	}
    
    public void setId(Long id) {
		this.id = id;
	}
    
	public String getCountry_code() {
		return country_code;
	}

	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}

	public String getCountry_name() {
		return country_name;
	}

	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}

	
}
