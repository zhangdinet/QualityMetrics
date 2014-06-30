package com.env.qualitymetrics.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity  
@Table(name="tbl_sprint_score_item")  
public class Sprint_score_item {	
	@Id
	private Sprint_score_item_pk sprint_score_item_pk;
	
	@Column
	private float score;
	
	@Column
	private float score_origin;

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public Sprint_score_item_pk getSprint_score_item_pk() {
		return sprint_score_item_pk;
	}

	public void setSprint_score_item_pk(Sprint_score_item_pk sprint_score_item_pk) {
		this.sprint_score_item_pk = sprint_score_item_pk;
	}

	public float getScore_origin() {
		return score_origin;
	}

	public void setScore_origin(float score_origin) {
		this.score_origin = score_origin;
	}
}
