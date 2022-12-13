package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
 
@Entity
@Table(name="login")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Login {
	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
	@Column(name = "username")
    private String username;
	@Column(name = "password")
    private String password;
	@Column(name = "role")
    private String role;
    
public Login()
{
}
 
public Login(Long id, String username, String password, String role) {
this.id = id;
this.username = username;
this.password = password;
this.role=role;
}
public Long getId() {
return id;
}
public void setId(Long id) {
this.id = id;
}
public String getUsername() {
return username;
}
public void setUsername(String username) {
this.username = username;
}
public String getPassword() {
return password;
}
public void setPassword(String password) {
this.password = password;
}
public String getRole() {
	return role;
}
public void setRole(String role) {
	this.role = role;
}
}