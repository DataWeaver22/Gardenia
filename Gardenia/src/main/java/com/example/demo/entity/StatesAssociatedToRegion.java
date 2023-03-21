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
@Table(name="statesAssociatedToRegion")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StatesAssociatedToRegion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, length = 20)
	private Long Id;
	
	@ManyToOne
    @JoinColumn(name = "stateId")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	State state;
	
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

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public StatesAssociatedToRegion(Long id, State state, Region region) {
		Id = id;
		this.state = state;
		this.region = region;
	}

	public StatesAssociatedToRegion() {
	}
	
	

}
