package com.env.qualitymetrics.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="tbl_ranking_sprint_score_item")
public class Ranking_sprint_score_item {
	
	@Id
	private Ranking_sprint_score_item_pk ranking_sprint_score_item_pk;
	
	@Column
	private float score;
	
	@Column
	private float score_origin;

	public Ranking_sprint_score_item_pk getRanking_sprint_score_item_pk() {
		return ranking_sprint_score_item_pk;
	}

	public void setRanking_sprint_score_item_pk(
			Ranking_sprint_score_item_pk ranking_sprint_score_item_pk) {
		this.ranking_sprint_score_item_pk = ranking_sprint_score_item_pk;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public float getScore_origin() {
		return score_origin;
	}

	public void setScore_origin(float score_origin) {
		this.score_origin = score_origin;
	}
}
