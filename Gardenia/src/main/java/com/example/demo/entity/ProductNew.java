package com.example.demo.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name="productnew")
public class ProductNew {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
 	
	@Column(name = "mrp", length = 20)
	private String mrp;
	
	@Column(name = "active_date", length = 20)
	private Date active_date;
	
	@Column(name = "inactive_date", length = 20)
	private Date inactive_date;
	
	@Column(name = "mrp_status", length = 20)
	private String mrp_status;
	
	@ManyToOne(targetEntity = Product.class)
    @JoinColumn(name="id", insertable = false, updatable = false)
	private Product product;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMrp() {
		return mrp;
	}

	public void setMrp(String mrp) {
		this.mrp = mrp;
	}

	public Date getActive_date() {
		return active_date;
	}

	public void setActive_date(Date active_date) {
		this.active_date = active_date;
	}

	public Date getInactive_date() {
		return inactive_date;
	}

	public void setInactive_date(Date inactive_date) {
		this.inactive_date = inactive_date;
	}

	public String getMrp_status() {
		return mrp_status;
	}

	public void setMrp_status(String mrp_status) {
		this.mrp_status = mrp_status;
	}

	public ProductNew(Long id, String mrp, Date active_date, Date inactive_date, String mrp_status) {
		super();
		this.id = id;
		this.mrp = mrp;
		this.active_date = active_date;
		this.inactive_date = inactive_date;
		this.mrp_status = mrp_status;
	}
	
	public ProductNew() {
		// TODO Auto-generated constructor stub
	}
//	public void getProductNew(ArrayList<ProductNew> lDD) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	public void setProductNew(ArrayList<ProductNew> lDD) {
//		// TODO Auto-generated method stub
//		
//	}

}
