package com.env.qualitymetrics.dto;

public class ProjectDto {
	int project_id;
	String project_name;
	float avg_score;
	//int avg_score_five;
	float avg_score_five;
	String rank_period;
	float rate_patch;
	int project_flag;
	
	//映射相关信息
	String project_name_tl;
	String project_name_sn;
	String project_name_rm;
	String project_name_rm_support;
	String suite_name_tl;
	String category_name_rm;
	
	
	public int getProject_id() {
		return project_id;
	}
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	public String getProject_name() {
		return project_name;
	}
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	public float getAvg_score() {
		return avg_score;
	}
	public void setAvg_score(float avg_score) {
		this.avg_score = avg_score;
	}
	public String getRank_period() {
		return rank_period;
	}
	public void setRank_period(String rank_period) {
		this.rank_period = rank_period;
	}
	public String getProject_name_tl() {
		return project_name_tl;
	}
	public void setProject_name_tl(String project_name_tl) {
		this.project_name_tl = project_name_tl;
	}
	public String getProject_name_sn() {
		return project_name_sn;
	}
	public void setProject_name_sn(String project_name_sn) {
		this.project_name_sn = project_name_sn;
	}
	public String getProject_name_rm() {
		return project_name_rm;
	}
	public void setProject_name_rm(String project_name_rm) {
		this.project_name_rm = project_name_rm;
	}
	public String getProject_name_rm_support() {
		return project_name_rm_support;
	}
	public void setProject_name_rm_support(String project_name_rm_support) {
		this.project_name_rm_support = project_name_rm_support;
	}
	public float getRate_patch() {
		return rate_patch;
	}
	public void setRate_patch(float rate_patch) {
		this.rate_patch = rate_patch;
	}
	//public int getAvg_score_five() {
	public float getAvg_score_five() {
		return avg_score_five;
	}
	public void setAvg_score_five(float avg_score_five) {
		this.avg_score_five = avg_score_five;
	}
	public int getProject_flag() {
		return project_flag;
	}
	public void setProject_flag(int project_flag) {
		this.project_flag = project_flag;
	}
	public String getSuite_name_tl() {
		return suite_name_tl;
	}
	public void setSuite_name_tl(String suite_name_tl) {
		this.suite_name_tl = suite_name_tl;
	}
	public String getCategory_name_rm() {
		return category_name_rm;
	}
	public void setCategory_name_rm(String category_name_rm) {
		this.category_name_rm = category_name_rm;
	}
}
