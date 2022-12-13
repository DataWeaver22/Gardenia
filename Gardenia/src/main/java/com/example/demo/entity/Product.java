package com.example.demo.entity;

import java.math.BigDecimal;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "product")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "pname",columnDefinition = "TEXT")
	private String pname;
	
	@Column(name = "code",columnDefinition = "TEXT")
	private String code;
	
	@ManyToOne
    @JoinColumn(name = "brandId")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	Brand brand;
	
	@ManyToOne
    @JoinColumn(name = "categoryId")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	Category category;
	
	@ManyToOne
    @JoinColumn(name = "familyId")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	Family family;
	
	@Column(name = "variant", columnDefinition = "TEXT")
	private String variant;
	
	@Column(name = "group_name", columnDefinition = "TEXT")
	private String group_name;
	
	@Column(name = "uom", columnDefinition = "TEXT")
	private String uom;
	
	@Column(name = "ptd", columnDefinition = "Decimal(10,2)")
	private BigDecimal ptd;
	
	@Column(name = "ptr", columnDefinition = "Decimal(10,2)")
	private BigDecimal ptr;
	
	@Column(name = "status",  columnDefinition = "TEXT")
	private String status;
	
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	
	@Column(name = "create_date")
	private LocalDateTime create_date;
	
	@Column(name = "inactive_date")
	private LocalDateTime inactive_date;
	
	@Column(name = "salesDiaryCode",columnDefinition = "TEXT")
	private String salesDiaryCode;
	
	@Column(name = "mrp", columnDefinition = "Decimal(10,2)")
	private BigDecimal mrp;
	
	@Column(name = "approval_status", columnDefinition = "TEXT")
	private String approval_status;
	
	@Column(name="updatedDateTime")
	private LocalDateTime updatedDateTime;
	
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

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Family getFamily() {
		return family;
	}

	public void setFamily(Family family) {
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

	public BigDecimal getPtd() {
		return ptd;
	}

	public void setPtd(BigDecimal ptd) {
		this.ptd = ptd;
	}

	public BigDecimal getPtr() {
		return ptr;
	}

	public void setPtr(BigDecimal ptr) {
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

	public String getSalesDiaryCode() {
		return salesDiaryCode;
	}

	public void setSalesDiaryCode(String salesDiaryCode) {
		this.salesDiaryCode = salesDiaryCode;
	}

	public BigDecimal getMrp() {
		return mrp;
	}

	public void setMrp(BigDecimal mrp) {
		this.mrp = mrp;
	}

	public String getApproval_status() {
		return approval_status;
	}

	public void setApproval_status(String approval_status) {
		this.approval_status = approval_status;
	}
	
	public LocalDateTime getUpdatedDateTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateTime(LocalDateTime updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

	public Product(Long id, String pname, String code, Brand brand, Category category, Family family, String variant,
			String group_name, String uom, BigDecimal ptd, BigDecimal ptr, String status, String description, LocalDateTime create_date,
			LocalDateTime inactive_date, String salesDiaryCode, BigDecimal mrp, String approval_status,LocalDateTime updatedDateTime) {
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
		this.salesDiaryCode = salesDiaryCode;
		this.mrp = mrp;
		this.approval_status = approval_status;
		this.updatedDateTime = updatedDateTime;
	}
	
	public Product() {
		// TODO Auto-generated constructor stub
	}
	


}
