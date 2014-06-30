package com.env.qualitymetrics.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tbl_project_ratepatch_history")
public class Project_ratepatch_history {
	
	@Id
	private Project_ratepatch_history_pk project_ratepatch_history_pk;
	@Column
	private float rate_patch;
	
	public Project_ratepatch_history_pk getProject_ratepatch_pk() {
		return project_ratepatch_history_pk;
	}
	public void setProject_ratepatch_pk(
			Project_ratepatch_history_pk project_ratepatch_pk) {
		this.project_ratepatch_history_pk = project_ratepatch_pk;
	}
	public float getRate_patch() {
		return rate_patch;
	}
	public void setRate_patch(float rate_patch) {
		this.rate_patch = rate_patch;
	}
}
