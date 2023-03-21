package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "rsmAssociatedRegion")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RSMAssociatedRegion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long Id;
	
	@ManyToOne
    @JoinColumn(name = "loginId")
	Login login;
	
	@ManyToOne
    @JoinColumn(name = "regionId")
	Region region;
	
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	
	public Login getLogin() {
		return login;
	}
	
	public void setLogin(Login login) {
		this.login = login;
	}
	
	public Region getRegion() {
		return region;
	}
	
	public void setRegion(Region region) {
		this.region = region;
	}
	public RSMAssociatedRegion(Long id, Login login, Region region) {
		Id = id;
		this.login = login;
		this.region = region;
	}
	public RSMAssociatedRegion() {
	}
	
	
}
