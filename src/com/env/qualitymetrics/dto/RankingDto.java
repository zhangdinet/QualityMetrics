package com.env.qualitymetrics.dto;

public class RankingDto {
	int rank_id;
	String rank_period;
	String rank_detail;
	
	public int getRank_id() {
		return rank_id;
	}
	public void setRank_id(int rank_id) {
		this.rank_id = rank_id;
	}
	public String getRank_period() {
		return rank_period;
	}
	public void setRank_period(String rank_period) {
		this.rank_period = rank_period;
	}
	public String getRank_detail() {
		return rank_detail;
	}
	public void setRank_detail(String rank_detail) {
		this.rank_detail = rank_detail;
	}
}
