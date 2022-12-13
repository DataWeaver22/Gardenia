package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "tokenBlacklist")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TokenBlacklist {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long Id;

	@Column(name = "token", columnDefinition = "Text")
    private String token;
	
	public Long getId() {
		return Id;
	}
	
	public String getToken() {
		return token;
	}

	public TokenBlacklist(Long id, String token) {
		super();
		Id = id;
		this.token = token;
	}

	public TokenBlacklist() {
	}
	
	
}
