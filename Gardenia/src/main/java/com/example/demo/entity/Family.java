package com.example.demo.entity;

import java.time.LocalDateTime;

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
@Table(name="family")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Family {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, length = 20)
	private Long Id;

	@Column(name = "familyName", columnDefinition = "Text")
    private String familyName;
	
	@Column(name = "updatedDateTime")
    private LocalDateTime updatedDateTime;
	 
	@ManyToOne
    @JoinColumn(name = "categoryId")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	Category category;

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public LocalDateTime getUpdatedDateTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateTime(LocalDateTime updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Family(String familyName, Category category,LocalDateTime updatedDateTime) {
		super();
		this.familyName = familyName;
		this.category = category;
		this.updatedDateTime = updatedDateTime;
	}

	public Family() {
	}
	
	
}
