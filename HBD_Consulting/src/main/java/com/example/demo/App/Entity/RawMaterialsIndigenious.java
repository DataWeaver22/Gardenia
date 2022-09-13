package com.example.demo.App.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="rawmaterialsindigeneous")
public class RawMaterialsIndigenious {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "raw_materials_indigenious_id", length = 20)
	private Long raw_materials_indigenious_id;
	
	@Column(name = "raw_materials_indigenious_name", length = 20)
	private String raw_materials_indigenious_name;
	
	@Column(name = "previous_5th_year", length = 20)
	private String previous_5th_year;
	
	@Column(name = "previous_4th_year", length = 20)
	private String previous_4th_year;
	
	@Column(name = "previous_3rd_year", length = 20)
	private String previous_3rd_year;
	
	@Column(name = "previous_2nd_year", length = 20)
	private String previous_2nd_year;
	
	@Column(name = "previous_1st_year", length = 20)
	private String previous_1st_year;
	
	@Column(name = "previous_year", length = 20)
	private String previous_year;
	
	@Column(name = "current_year", length = 20)
	private String current_year;
	
	@Column(name = "next_year_projection", length = 20)
	private String next_year_projection;
	
	@Column(name = "next_2nd_year_projection", length = 20)
	private String next_2nd_year_projection;
	
	@ManyToOne
    @JoinColumn(name = "cma_two_id", referencedColumnName = "Id")
    private CmaTwo cmatwo;

	public Long getRaw_materials_indigenious_id() {
		return raw_materials_indigenious_id;
	}

	public void setRaw_materials_indigenious_id(Long raw_materials_indigenious_id) {
		this.raw_materials_indigenious_id = raw_materials_indigenious_id;
	}

	public String getRaw_materials_indigenious_name() {
		return raw_materials_indigenious_name;
	}

	public void setRaw_materials_indigenious_name(String raw_materials_indigenious_name) {
		this.raw_materials_indigenious_name = raw_materials_indigenious_name;
	}

	public String getPrevious_5th_year() {
		return previous_5th_year;
	}

	public void setPrevious_5th_year(String previous_5th_year) {
		this.previous_5th_year = previous_5th_year;
	}

	public String getPrevious_4th_year() {
		return previous_4th_year;
	}

	public void setPrevious_4th_year(String previous_4th_year) {
		this.previous_4th_year = previous_4th_year;
	}

	public String getPrevious_3rd_year() {
		return previous_3rd_year;
	}

	public void setPrevious_3rd_year(String previous_3rd_year) {
		this.previous_3rd_year = previous_3rd_year;
	}

	public String getPrevious_2nd_year() {
		return previous_2nd_year;
	}

	public void setPrevious_2nd_year(String previous_2nd_year) {
		this.previous_2nd_year = previous_2nd_year;
	}

	public String getPrevious_1st_year() {
		return previous_1st_year;
	}

	public void setPrevious_1st_year(String previous_1st_year) {
		this.previous_1st_year = previous_1st_year;
	}

	public String getPrevious_year() {
		return previous_year;
	}

	public void setPrevious_year(String previous_year) {
		this.previous_year = previous_year;
	}

	public String getCurrent_year() {
		return current_year;
	}

	public void setCurrent_year(String current_year) {
		this.current_year = current_year;
	}

	public String getNext_year_projection() {
		return next_year_projection;
	}

	public void setNext_year_projection(String next_year_projection) {
		this.next_year_projection = next_year_projection;
	}

	public String getNext_2nd_year_projection() {
		return next_2nd_year_projection;
	}

	public void setNext_2nd_year_projection(String next_2nd_year_projection) {
		this.next_2nd_year_projection = next_2nd_year_projection;
	}

	public RawMaterialsIndigenious(Long raw_materials_indigenious_id, String raw_materials_indigenious_name, String previous_5th_year,
			String previous_4th_year, String previous_3rd_year, String previous_2nd_year, String previous_1st_year,
			String previous_year, String current_year, String next_year_projection, String next_2nd_year_projection) {
		super();
		this.raw_materials_indigenious_id = raw_materials_indigenious_id;
		this.raw_materials_indigenious_name = raw_materials_indigenious_name;
		this.previous_5th_year = previous_5th_year;
		this.previous_4th_year = previous_4th_year;
		this.previous_3rd_year = previous_3rd_year;
		this.previous_2nd_year = previous_2nd_year;
		this.previous_1st_year = previous_1st_year;
		this.previous_year = previous_year;
		this.current_year = current_year;
		this.next_year_projection = next_year_projection;
		this.next_2nd_year_projection = next_2nd_year_projection;
	}
	
	public RawMaterialsIndigenious() {
		
	}
}
