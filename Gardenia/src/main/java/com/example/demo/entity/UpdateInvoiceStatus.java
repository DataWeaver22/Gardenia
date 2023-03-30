package com.example.demo.entity;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "updateInvoiceStatus")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UpdateInvoiceStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, length = 20)
	private Long Id;
	
	
	
	@Column(name = "month",columnDefinition = "Text" ,length = 30)
	private String month;
	
	@Column(name = "year" ,columnDefinition = "Text" , length = 5)
	private String year;
	
	@Column(name = "createdDate")
	private LocalDateTime createdDate;
	
	@Column(name = "updatedDate" )
	private LocalDateTime updatedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regionId")
	Region region;

	
	@Transient
	private List<String> invoiceno;
	
	
	public List<String> getInvoiceno() {
		return invoiceno;
	}


	@Transient
	private List<String> regionid;
	
	
	public List<String> getRegionid() {
		return regionid;
	}

	
	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
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

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createDateTime) {
		this.createdDate = createDateTime;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDateTime) {
		this.updatedDate = updatedDateTime;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	/**
	 * @param id
	 * @param invoiceNo
	 * @param month
	 * @param year
	 * @param createdDate
	 * @param updatedDate
	 * @param region
	 */
	public UpdateInvoiceStatus(Long id, String month, String year, LocalDateTime createdDate, LocalDateTime updatedDate,
			Region region) {
		Id = id;
		
		this.month = month;
		this.year = year;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.region = region;
	}

	/**
	 * 
	 */
	public UpdateInvoiceStatus() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UpdateInvoiceStatus save(UpdateInvoiceStatus updateInvoiceStatus) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
