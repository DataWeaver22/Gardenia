package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "userTargetDetails")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class UserTargetDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "hq", columnDefinition = "Text")
	private String hq;

	@Column(name = "present", columnDefinition = "Text")
	private String present;

	@Column(name = "goal", columnDefinition = "Text")
	private String goal;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	User user;
	
	@Transient
	private String userId;

	public String getUserId() {
		return userId;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHq() {
		return hq;
	}

	public void setHq(String hq) {
		this.hq = hq;
	}

	public String getPresent() {
		return present;
	}

	public void setPresent(String present) {
		this.present = present;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserTargetDetails(Long id, String hq, String present, String goal, User user, String userId) {
		this.id = id;
		this.hq = hq;
		this.present = present;
		this.goal = goal;
		this.user = user;
		this.userId = userId;
	}

	public UserTargetDetails() {
	}
	
}
