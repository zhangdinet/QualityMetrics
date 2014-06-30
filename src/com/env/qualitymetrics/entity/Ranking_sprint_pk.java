package com.env.qualitymetrics.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Ranking_sprint_pk implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5163018612979852941L;
	private int rank_id;
	private int sprint_id;
	public int getRank_id() {
		return rank_id;
	}
	public void setRank_id(int rank_id) {
		this.rank_id = rank_id;
	}
	public int getSprint_id() {
		return sprint_id;
	}
	public void setSprint_id(int sprint_id) {
		this.sprint_id = sprint_id;
	}
}
