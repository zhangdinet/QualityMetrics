package com.env.qualitymetrics.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tbl_project_source")
public class Project_source {
	
	@Id
	private Project_source_pk project_source_pk;
	
	@Column
	private String source_project_name;


	public String getSource_project_name() {
		return source_project_name;
	}

	public void setSource_project_name(String source_project_name) {
		this.source_project_name = source_project_name;
	}

	public Project_source_pk getProject_source_pk() {
		return project_source_pk;
	}

	public void setProject_source_pk(Project_source_pk project_source_pk) {
		this.project_source_pk = project_source_pk;
	}
}
