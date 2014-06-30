package com.env.qualitymetrics.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Project_source_pk implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3731766793461124981L;
	int project_id;
	int source_id;
	
	public int getProject_id() {
		return project_id;
	}
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	public int getSource_id() {
		return source_id;
	}
	public void setSource_id(int source_id) {
		this.source_id = source_id;
	}
}
