package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "aproduct")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
		
	@Column(name = "pname", nullable = false, length = 20)
	private String pname;
	
	@Column(name = "code", nullable = false, length = 20)
	private String code;
	
	@Column(name = "brand", nullable = false, length = 20)
	private String brand;
	
	@Column(name = "category", nullable = false, length = 20)
	private String category;
	
	@Column(name = "family", nullable = false, length = 20)
	private String family;
	
	@Column(name = "variant", nullable = false, length = 20)
	private String variant;
	
	@Column(name = "group_name", nullable = false, length = 20)
	private String group_name;
	
	@Column(name = "uom", nullable = false, length = 20)
	private String uom;
	
	@Column(name = "ptd", nullable = false, length = 20)
	private String ptd;
	
	@Column(name = "ptr", nullable = false, length = 20)
	private String ptr;
	
	@Column(name = "status", nullable = false, length = 20)
	private String status;
	
	@Column(name = "description", nullable = false, length = 20)
	private String description;
	
	@Column(name = "create_date", length = 20)
	private String create_date;
	
	@Column(name = "inactive_date", length = 20)
	private String inactive_date;
	
	@Column(name = "sales_diary", nullable = false, length = 20)
	private String sales_diary;

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

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getInactive_date() {
		return inactive_date;
	}

	public void setInactive_date(String inactive_date) {
		this.inactive_date = inactive_date;
	}

	public String getSales_diary() {
		return sales_diary;
	}

	public void setSales_diary(String sales_diary) {
		this.sales_diary = sales_diary;
	}

	public Product(Long id, String pname, String code, String brand, String category, String family, String variant,
			String group_name, String uom, String ptd, String status, String description, String create_date,
			String inactive_date, String sales_diary) {
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
		this.status = status;
		this.description = description;
		this.create_date = create_date;
		this.inactive_date = inactive_date;
		this.sales_diary = sales_diary;
	}
	
	public Product() {
		// TODO Auto-generated constructor stub
	}

}
