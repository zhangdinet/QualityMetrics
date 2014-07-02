package com.env.qualitymetrics.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tbl_ranking_sprint")
public class Ranking_sprint {
	
	@Id
	private Ranking_sprint_pk ranking_sprint_pk;
	
	@Column
	private float sprint_score;
	
	@Column
	private String sprint_name;
	
	public Ranking_sprint_pk getRanking_sprint_pk() {
		return ranking_sprint_pk;
	}
	public void setRanking_sprint_pk(Ranking_sprint_pk ranking_sprint_pk) {
		this.ranking_sprint_pk = ranking_sprint_pk;
	}
	public float getSprint_score() {
		return sprint_score;
	}
	public void setSprint_score(float sprint_score) {
		this.sprint_score = sprint_score;
	}
	public String getSprint_name() {
		return sprint_name;
	}
	public void setSprint_name(String sprint_name) {
		this.sprint_name = sprint_name;
	}
}
