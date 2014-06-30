package com.env.qualitymetrics.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Sprint_source_pk implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7352436638107763748L;
	int sprint_id;
	int source_id;
	public int getSprint_id() {
		return sprint_id;
	}
	public void setSprint_id(int sprint_id) {
		this.sprint_id = sprint_id;
	}
	public int getSource_id() {
		return source_id;
	}
	public void setSource_id(int source_id) {
		this.source_id = source_id;
	}
}
