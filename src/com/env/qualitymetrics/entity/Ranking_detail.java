package com.env.qualitymetrics.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tbl_ranking_detail")
public class Ranking_detail {
	
	@Id
	private Ranking_detail_pk ranking_detail_pk;	
	@Column
	private float rank_score;
	
	@Column
	private String project_name;
	
	public float getRank_score() {
		return rank_score;
	}
	public void setRank_score(float rank_score) {
		this.rank_score = rank_score;
	}
	public void setRanking_detail_pk(Ranking_detail_pk ranking_detail_pk) {
		this.ranking_detail_pk = ranking_detail_pk;
	}
	public Ranking_detail_pk getRanking_detail_pk() {
		return ranking_detail_pk;
	}
	public String getProject_name() {
		return project_name;
	}
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
}
