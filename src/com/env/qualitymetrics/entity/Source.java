package com.env.qualitymetrics.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="tbl_source")
public class Source {
	
	@Id  
    @GeneratedValue(generator="system-native")  
    @GenericGenerator(name = "system-native",strategy="native")
    @Column 
    private int source_id;
	
	@Column
	private String source_name;
	
	@Column
	private String source_dscp;

	public int getSource_id() {
		return source_id;
	}

	public void setSource_id(int source_id) {
		this.source_id = source_id;
	}

	public String getSource_name() {
		return source_name;
	}

	public void setSource_name(String source_name) {
		this.source_name = source_name;
	}

	public String getSource_dscp() {
		return source_dscp;
	}

	public void setSource_dscp(String source_dscp) {
		this.source_dscp = source_dscp;
	}
}
