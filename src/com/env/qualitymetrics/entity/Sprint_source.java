package com.env.qualitymetrics.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tbl_sprint_source")
public class Sprint_source {
	
	@Id
	private Sprint_source_pk sprint_source_pk;
	@Column
	private String sprint_source_name;
	public Sprint_source_pk getSprint_source_pk() {
		return sprint_source_pk;
	}
	public void setSprint_source_pk(Sprint_source_pk sprint_source_pk) {
		this.sprint_source_pk = sprint_source_pk;
	}
	public String getSprint_source_name() {
		return sprint_source_name;
	}
	public void setSprint_source_name(String sprint_source_name) {
		this.sprint_source_name = sprint_source_name;
	}
}
