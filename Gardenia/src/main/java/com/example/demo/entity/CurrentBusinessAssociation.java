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
@Table(name = "currentBusinessAssociation")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CurrentBusinessAssociation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, length = 20)
	private Long Id;
	
	@ManyToOne
    @JoinColumn(name = "distributorId")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	Distributor distributor;
	
	@Column(name = "companyName", columnDefinition = "Text")
    private String companyName;

	@Column(name = "distributorSince", columnDefinition = "Text")
    private String distributorSince;
	
	@Column(name = "routesCovered", columnDefinition = "Text")
    private String routesCovered;

	@Column(name = "annualTurnover", columnDefinition = "Text")
    private String annualTurnover;
	
	@Column(name = "typeOfDistributor", columnDefinition = "Text")
    private String typeOfDistributor;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Distributor getDistributor() {
		return distributor;
	}

	public void setDistributor(Distributor distributor) {
		this.distributor = distributor;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getDistributorSince() {
		return distributorSince;
	}

	public void setDistributorSince(String distributorSince) {
		this.distributorSince = distributorSince;
	}

	public String getRoutesCovered() {
		return routesCovered;
	}

	public void setRoutesCovered(String routesCovered) {
		this.routesCovered = routesCovered;
	}

	public String getAnnualTurnover() {
		return annualTurnover;
	}

	public void setAnnualTurnover(String annualTurnover) {
		this.annualTurnover = annualTurnover;
	}

	public String getTypeOfDistributor() {
		return typeOfDistributor;
	}

	public void setTypeOfDistributor(String typeOfDistributor) {
		this.typeOfDistributor = typeOfDistributor;
	}

	public CurrentBusinessAssociation(Long id, Distributor distributor, String companyName, String distributorSince,
			String routesCovered, String annualTurnover, String typeOfDistributor) {
		Id = id;
		this.distributor = distributor;
		this.companyName = companyName;
		this.distributorSince = distributorSince;
		this.routesCovered = routesCovered;
		this.annualTurnover = annualTurnover;
		this.typeOfDistributor = typeOfDistributor;
	}

	public CurrentBusinessAssociation() {
	}
	
	

}
