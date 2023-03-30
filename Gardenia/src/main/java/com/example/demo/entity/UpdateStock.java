package com.example.demo.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "updateStock")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UpdateStock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, length = 20)
	private Long Id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "distId")
	Distributor distributor;
	
	@Column(name = "month",columnDefinition = "Text" , length = 30)
	private String month;
	
	@Column(name = "year" ,columnDefinition = "Text" , length = 5)
	private String year;
	
	@Column(name = "createdDate")
	private Date createdDate;
	
	@Column(name = "updatedDate" )
	private Date updatedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regionId")
	Region region;

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

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	/**
	 * @param id
	 * @param distributor
	 * @param month
	 * @param year
	 * @param createdDate
	 * @param updatedDate
	 * @param region
	 */
	public UpdateStock(Long id, Distributor distributor, String month, String year, Date createdDate, Date updatedDate,
			Region region) {
		Id = id;
		this.distributor = distributor;
		this.month = month;
		this.year = year;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.region = region;
	}

	/**
	 * 
	 */
	public UpdateStock() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
