package com.env.qualitymetrics.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="tbl_indicator_weight")
public class Indicator_weight {
	
	@Id  
    @GeneratedValue(generator="system-uuid")  
    @GenericGenerator(name = "system-uuid",strategy="native")
    @Column
	private int indicator_id;
	
	@Column
	private String indicator_name;
	
	@Column
	private Float weight_rate;
	
	@Column
	private String indicator_dscp;
	
	
	public int getIndicator_id() {
		return indicator_id;
	}
	public void setIndicator_id(int indicator_id) {
		this.indicator_id = indicator_id;
	}
	public String getIndicator_name() {
		return indicator_name;
	}
	public void setIndicator_name(String indicator_name) {
		this.indicator_name = indicator_name;
	}
	public Float getWeight_rate() {
		return weight_rate;
	}
	public void setWeight_rate(Float weight_rate) {
		this.weight_rate = weight_rate;
	}
	public String getIndicator_dscp() {
		return indicator_dscp;
	}
	public void setIndicator_dscp(String indicator_dscp) {
		this.indicator_dscp = indicator_dscp;
	}
}
