package com.env.qualitymetrics.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

//此类和对应的表结构研究一下是否重构 zhangdi  140509
@Entity
@Table(name="tbl_sprint")
public class Sprint {
	@Id  
    @GeneratedValue(generator="system-uuid")  
    @GenericGenerator(name = "system-uuid",strategy="native")
    @Column
    private int sprint_id;
	
	@Column
	private int project_id;
	
	@Column
	private String sprint_name;
	
	@Column
	private Date sprint_startdate;
	
	@Column
	private Date sprint_enddate;
	
	@Column
	private Date sprint_builddate; //zhangdi todo 140430==删除===
	
	@Column
	private String url_surveymonkey;
	
	@Column
	private float sprint_score;
		
	@Column
	private String sprint_build;
	
	public String getSprint_build()
	{
		return sprint_build;
	}
	
	public void setSprint_build(String build) 
	{
		this.sprint_build=build;
	}

	public int getSprint_id() {
		return sprint_id;
	}

	public void setSprint_id(int sprint_id) {
		this.sprint_id = sprint_id;
	}

	public int getProject_id() {
		return project_id;
	}

	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

	public String getSprint_name() {
		return sprint_name;
	}

	public void setSprint_name(String sprint_name) {
		this.sprint_name = sprint_name;
	}

	public Date getSprint_startdate() {
		return sprint_startdate;
	}

	public void setSprint_startdate(Date sprint_startdate) {
		this.sprint_startdate = sprint_startdate;
	}

	public Date getSprint_enddate() {
		return sprint_enddate;
	}

	public void setSprint_enddate(Date sprint_enddate) {
		this.sprint_enddate = sprint_enddate;
	}

	public Date getSprint_builddate() {
		return sprint_builddate;
	}

	public void setSprint_builddate(Date sprint_builddate) {
		this.sprint_builddate = sprint_builddate;
	}

	public String getUrl_surveymonkey() {
		return url_surveymonkey;
	}

	public void setUrl_surveymonkey(String url_surveymonkey) {
		this.url_surveymonkey = url_surveymonkey;
	}

	public float getSprint_score() {
		return sprint_score;
	}

	public void setSprint_score(float sprint_score) {
		this.sprint_score = sprint_score;
	}	
}
