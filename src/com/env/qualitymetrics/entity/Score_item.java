package com.env.qualitymetrics.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="tbl_score_item")
public class Score_item {
	
	@Id  
    @GeneratedValue(generator="system-native")  
    @GenericGenerator(name = "system-native",strategy="native")
    @Column
    private int score_item_id;
	
    @Column
    private String score_item_name;
    
    @Column
    private String score_item_dscp;

	public int getScore_item_id() {
		return score_item_id;
	}

	public void setScore_item_id(int score_item_id) {
		this.score_item_id = score_item_id;
	}

	public String getScore_item_name() {
		return score_item_name;
	}

	public void setScore_item_name(String score_item_name) {
		this.score_item_name = score_item_name;
	}

	public String getScore_item_dscp() {
		return score_item_dscp;
	}

	public void setScore_item_dscp(String score_item_dscp) {
		this.score_item_dscp = score_item_dscp;
	}
}
