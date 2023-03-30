package com.example.demo.entity;

import java.beans.Transient;
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

@Entity
@Table(name = "invoiceNo")
public class InvoiceNo {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, length = 20)
	private Long Id;
	
	@Column(name = "invoiceNo" , length = 200)
	private String invoiceNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "updateInvoiceId")
	UpdateInvoiceStatus updateInvoiceStatus;

	
	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public UpdateInvoiceStatus getUpdateInvoiceStatus() {
		return updateInvoiceStatus;
	}

	public void setUpdateInvoiceStatus(UpdateInvoiceStatus updateInvoiceStatus) {
		this.updateInvoiceStatus = updateInvoiceStatus;
	}

	/**
	 * @param id
	 * @param invoiceNo
	 * @param updateInvoiceStatus
	 */
	public InvoiceNo(Long id, String invoiceNo, UpdateInvoiceStatus updateInvoiceStatus) {
		Id = id;
		this.invoiceNo = invoiceNo;
		this.updateInvoiceStatus = updateInvoiceStatus;
	}

	/**
	 * 
	 */
	public InvoiceNo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
