package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "state")
public class State {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	@Column(name = "state_code", length = 20)
    private String state_code;

	@Column(name = "state_name", length = 20)
    private String state_name;
	
	
	@Column(name = "country_name", length = 20)
	private String country_name;
	  
	@Column(name = "country_code", length = 20)
	private String country_code;
	 
	
	@OneToOne
    @JoinColumn(name="id")
    private Country country;
	
	@OneToOne(mappedBy = "state")
    private Region region;
	
	@OneToOne(mappedBy = "state")
	private User user;
	
	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
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

	
	
	  public String getCountry_code() { return country_code; }
	  
	  public void setCountry_code(String country_code) { 
	  this.country_code = country_code; 
	  }
	 
	  
	  public String getCountry_name() { return country_name; }
	  
	  public void setCountry_name(String country_name) { this.country_name =
	  country_name; }
	 
	public State(String state_code, String state_name, String country_name, String country_code) {
		super();
		this.state_code = state_code;
		this.state_name = state_name;
		this.country_name = country_name; 
		this.country_code = country_code;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public State() {
		// TODO Auto-generated constructor stub
	}
	
	
}
