package com.example.demo.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="brand")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Brand {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, length = 20)
	private Long Id;

	@Column(name = "brandName", length = 20)
    private String brandName;
	
	@Column(name = "updatedDateTime", length = 20)
    private LocalDateTime updatedDateTime;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	public LocalDateTime getUpdatedDateTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateTime(LocalDateTime updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

	public Brand(Long id, String brandName,LocalDateTime updatedDateTime) {
		super();
		Id = id;
		this.brandName = brandName;
		this.updatedDateTime = updatedDateTime;
	}

	public Brand() {
	}
	
	
}
