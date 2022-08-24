package com.example.demo.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="disttso")
public class DistNew {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	@Column(name = "id", nullable = false, length = 20)
    private Long id;
	
	@Column(name = "assign_tso")
	private String assign_tso;
	
	@Column(name = "user_id")
	private String user_id;
	
	@Column(name = "tso_status")
	private String tso_status;
	
	@Column(name = "active_date")
	private Date active_date;
	
	@Column(name = "inactive_date")
	private Date inactive_date;
	
	@Column(name = "dist_id")
	private String dist_id;
	
	@ManyToOne(targetEntity = Distributor.class)
    @JoinColumn(name="id", insertable = false, updatable = false)
	private Distributor distributor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAssign_tso() {
		return assign_tso;
	}

	public void setAssign_tso(String assign_tso) {
		this.assign_tso = assign_tso;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getTso_status() {
		return tso_status;
	}

	public void setTso_status(String tso_status) {
		this.tso_status = tso_status;
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

	public String getDist_id() {
		return dist_id;
	}

	public void setDist_id(String dist_id) {
		this.dist_id = dist_id;
	}

	public DistNew(Long id, String assign_tso, String user_id, String tso_status, Date active_date, Date inactive_date,
			String dist_id) {
		super();
		this.id = id;
		this.assign_tso = assign_tso;
		this.user_id = user_id;
		this.tso_status = tso_status;
		this.active_date = active_date;
		this.inactive_date = inactive_date;
		this.dist_id = dist_id;
	}
	
	public DistNew() {
		// TODO Auto-generated constructor stub
	 
	}
}
