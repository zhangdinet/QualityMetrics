package com.env.qualitymetrics.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Project_ratepatch_history_pk implements Serializable{
	int project_id;
	int year_id;
	public int getProject_id() {
		return project_id;
	}
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	public int getYear_id() {
		return year_id;
	}
	public void setYear_id(int year_id) {
		this.year_id = year_id;
	}
}
