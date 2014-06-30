package com.env.qualitymetrics.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="tbl_year")
public class Year {
	
	@Id  
    @GeneratedValue(generator="system-uuid")  
    @GenericGenerator(name = "system-uuid",strategy="native")
    @Column
    int year_id;
    
	@Column
	String year_detail;

	public int getYear_id() {
		return year_id;
	}

	public void setYear_id(int year_id) {
		this.year_id = year_id;
	}

	public String getYear_detail() {
		return year_detail;
	}

	public void setYear_detail(String year_detail) {
		this.year_detail = year_detail;
	}
}
