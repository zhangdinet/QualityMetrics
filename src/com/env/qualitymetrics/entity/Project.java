package com.env.qualitymetrics.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="tbl_project")
public class Project {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid",strategy="native")
	@Column
	private int project_id;
	
	@Column
	private String project_name;
	
	@Column
	private float avg_score;
	
	@Column
	private float rate_patch;
	
	@Column
	private int project_flag;

	public int getProject_id() {
		return project_id;
	}

	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}

	public float getAvg_score() {
		return avg_score;
	}

	public void setAvg_score(float avg_score) {
		this.avg_score = avg_score;
	}

	public float getRate_patch() {
		return rate_patch;
	}

	public void setRate_patch(float rate_patch) {
		this.rate_patch = rate_patch;
	}

	public int getProject_flag() {
		return project_flag;
	}

	public void setProject_flag(int project_flag) {
		this.project_flag = project_flag;
	}
}
