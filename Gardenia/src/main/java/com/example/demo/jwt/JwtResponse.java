package com.example.demo.jwt;

public class JwtResponse {
	String token;
	String message;
	String role;
	
	public JwtResponse(String token,String message,String role) {
		super();
		this.token = token;
		this.message = message;
		this.role = role;
	}

	public JwtResponse() {
		super();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
