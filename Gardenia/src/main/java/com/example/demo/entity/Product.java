package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "aproduct")
public class Product{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "pname",length = 20)
	private String pname;
	
	@Column(name = "code",  length = 20)
	private String code;
	
	@Column(name = "brand", length = 20)
	private String brand;
	
	@Column(name = "category", length = 20)
	private String category;
	
	@Column(name = "family", length = 20)
	private String family;
	
	@Column(name = "variant", length = 20)
	private String variant;
	
	@Column(name = "group_name", length = 20)
	private String group_name;
	
	@Column(name = "uom", length = 20)
	private String uom;
	
	@Column(name = "ptd", length = 20)
	private String ptd;
	
	@Column(name = "ptr", length = 20)
	private String ptr;
	
	@Column(name = "status",  length = 20)
	private String status;
	
	@Column(name = "description", length = 20)
	private String description;
	
	@Column(name = "create_date", length = 20)
	private LocalDateTime create_date;
	
	@Column(name = "inactive_date", length = 20)
	private LocalDateTime inactive_date;
	
	@Column(name = "sales_diary", length = 20)
	private String sales_diary;
	
	@Column(name = "mrp", length = 20)
	private String mrp;
	
	@OneToMany(mappedBy="product",targetEntity = ProductNew.class)
	public List<ProductNew> productNew;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getVariant() {
		return variant;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getPtd() {
		return ptd;
	}

	public void setPtd(String ptd) {
		this.ptd = ptd;
	}

	public String getPtr() {
		return ptr;
	}

	public void setPtr(String ptr) {
		this.ptr = ptr;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getCreate_date() {
		return create_date;
	}

	public void setCreate_date(LocalDateTime create_date) {
		this.create_date = create_date;
	}

	public LocalDateTime getInactive_date() {
		return inactive_date;
	}

	public void setInactive_date(LocalDateTime inactive_date) {
		this.inactive_date = inactive_date;
	}

	public String getSales_diary() {
		return sales_diary;
	}

	public void setSales_diary(String sales_diary) {
		this.sales_diary = sales_diary;
	}

	public String getMrp() {
		return mrp;
	}

	public void setMrp(String mrp) {
		this.mrp = mrp;
	}

	public Product(Long id, String pname, String code, String brand, String category, String family, String variant,
			String group_name, String uom, String ptd, String ptr, String status, String description, LocalDateTime create_date,
			LocalDateTime inactive_date, String sales_diary, String mrp) {
		super();
		this.id = id;
		this.pname = pname;
		this.code = code;
		this.brand = brand;
		this.category = category;
		this.family = family;
		this.variant = variant;
		this.group_name = group_name;
		this.uom = uom;
		this.ptd = ptd;
		this.ptr = ptr;
		this.status = status;
		this.description = description;
		this.create_date = create_date;
		this.inactive_date = inactive_date;
		this.sales_diary = sales_diary;
		this.mrp = mrp;
	}
	
	public Product() {
		// TODO Auto-generated constructor stub
	}
	


}
