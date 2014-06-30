package com.env.qualitymetrics.dto;

public class WeightDto {
	
	//各权重值
	float ipdOrLmt_rate;
	float sonar_rate;
	float test_pass_rate;
	float tc_exec_rate;
	float bug_new_rate;
	float bug_reopen_rate;
	float bug_escape_rate;
	float rate_patch_rate;
	float rate_support_rate;
	float rate_ce_rate;
	
	public float getIpdOrLmt_rate() {
		return ipdOrLmt_rate;
	}
	public void setIpdOrLmt_rate(float ipdOrLmt_rate) {
		this.ipdOrLmt_rate = ipdOrLmt_rate;
	}
	public float getSonar_rate() {
		return sonar_rate;
	}
	public void setSonar_rate(float sonar_rate) {
		this.sonar_rate = sonar_rate;
	}
	public float getTest_pass_rate() {
		return test_pass_rate;
	}
	public void setTest_pass_rate(float test_pass_rate) {
		this.test_pass_rate = test_pass_rate;
	}
	public float getTc_exec_rate() {
		return tc_exec_rate;
	}
	public void setTc_exec_rate(float tc_exec_rate) {
		this.tc_exec_rate = tc_exec_rate;
	}
	public float getBug_new_rate() {
		return bug_new_rate;
	}
	public void setBug_new_rate(float bug_new_rate) {
		this.bug_new_rate = bug_new_rate;
	}
	public float getBug_reopen_rate() {
		return bug_reopen_rate;
	}
	public void setBug_reopen_rate(float bug_reopen_rate) {
		this.bug_reopen_rate = bug_reopen_rate;
	}
	public float getBug_escape_rate() {
		return bug_escape_rate;
	}
	public void setBug_escape_rate(float bug_escape_rate) {
		this.bug_escape_rate = bug_escape_rate;
	}
	public float getRate_patch_rate() {
		return rate_patch_rate;
	}
	public void setRate_patch_rate(float rate_patch_rate) {
		this.rate_patch_rate = rate_patch_rate;
	}
	public float getRate_support_rate() {
		return rate_support_rate;
	}
	public void setRate_support_rate(float rate_support_rate) {
		this.rate_support_rate = rate_support_rate;
	}
	public float getRate_ce_rate() {
		return rate_ce_rate;
	}
	public void setRate_ce_rate(float rate_ce_rate) {
		this.rate_ce_rate = rate_ce_rate;
	}
}
