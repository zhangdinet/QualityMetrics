package com.env.qualitymetrics.dto;

import java.util.Date;
//import java.text.SimpleDateFormat;

public class SprintDto {
	int sprint_id;
	int project_id;
	String sprint_name;
	float sprint_score;
	int sprint_score_five;
	Date sprint_startdate;
	Date sprint_enddate;
	Date sprint_builddate;
	String url_surveymonkey;
	String sprint_build;
	//关于该Sprint详情得分项
	float ipd_score;
	float lmt_score;
	float sonar_score;
	float test_pass_score;
	float tc_exec_score;
	float bug_new_score;
	float bug_reopen_score;
	float bug_escape_score;
	float rate_patch_score;
	float rate_support_score;
	float rate_ce_score;
	
	float ipd_score_origin;
	float lmt_score_origin;
	float sonar_score_origin;
	float test_pass_score_origin;
	float tc_exec_score_origin;
	float bug_new_score_origin;
	float bug_reopen_score_origin;
	float bug_escape_score_origin;
	float rate_patch_score_origin;
	float rate_support_score_origin;
	float rate_ce_score_origin;
	//Sprint映射信息
	String testplan_testlink;
	String version_redmine;
	String build_sonar;	

	public String getSprint_build()
	{
		return this.sprint_build;
	}
	
	public void setSprint_build(String build)  //abc=2013-10-10 23:23:23
	{
		this.sprint_build=build;		
	}
	
	public int getSprint_id() {
		return sprint_id;
	}
	public void setSprint_id(int sprint_id) {
		this.sprint_id = sprint_id;
	}
	public int getProject_id() {
		return project_id;
	}
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	public String getSprint_name() {
		return sprint_name;
	}
	public void setSprint_name(String sprint_name) {
		this.sprint_name = sprint_name;
	}
	public float getSprint_score() {
		return sprint_score;
	}
	public void setSprint_score(float sprint_score) {
		this.sprint_score = sprint_score;
	}
	public Date getSprint_startdate() {
		return sprint_startdate;
	}
	public void setSprint_startdate(Date sprint_startdate) {
		this.sprint_startdate = sprint_startdate;
	}
	public Date getSprint_enddate() {
		return sprint_enddate;
	}
	public void setSprint_enddate(Date sprint_enddate) {
		this.sprint_enddate = sprint_enddate;
	}
	
	//zhangdi 140507 todo 此处考虑数据库字段sprint_build是否需要删掉？
	public Date getSprint_builddate() 
	{
		return sprint_builddate;
	}
	public void setSprint_builddate(Date sprint_builddate) {
		this.sprint_builddate = sprint_builddate;
	}
	public String getUrl_surveymonkey() {
		return url_surveymonkey;
	}
	public void setUrl_surveymonkey(String url_surveymonkey) {
		this.url_surveymonkey = url_surveymonkey;
	}
	public float getIpd_score() {
		return ipd_score;
	}
	public void setIpd_score(float ipd_score) {
		this.ipd_score = ipd_score;
	}
	public float getLmt_score() {
		return lmt_score;
	}
	public void setLmt_score(float lmt_score) {
		this.lmt_score = lmt_score;
	}
	public float getSonar_score() {
		return sonar_score;
	}
	public void setSonar_score(float sonar_score) {
		this.sonar_score = sonar_score;
	}
	public float getTest_pass_score() {
		return test_pass_score;
	}
	public void setTest_pass_score(float test_pass_score) {
		this.test_pass_score = test_pass_score;
	}
	public float getTc_exec_score() {
		return tc_exec_score;
	}
	public void setTc_exec_score(float tc_exec_score) {
		this.tc_exec_score = tc_exec_score;
	}
	public float getBug_new_score() {
		return bug_new_score;
	}
	public void setBug_new_score(float bug_new_score) {
		this.bug_new_score = bug_new_score;
	}
	public float getBug_reopen_score() {
		return bug_reopen_score;
	}
	public void setBug_reopen_score(float bug_reopen_score) {
		this.bug_reopen_score = bug_reopen_score;
	}
	public float getBug_escape_score() {
		return bug_escape_score;
	}
	public void setBug_escape_score(float bug_escape_score) {
		this.bug_escape_score = bug_escape_score;
	}
	public float getRate_patch_score() {
		return rate_patch_score;
	}
	public void setRate_patch_score(float rate_patch_score) {
		this.rate_patch_score = rate_patch_score;
	}
	public float getRate_support_score() {
		return rate_support_score;
	}
	public void setRate_support_score(float rate_support_score) {
		this.rate_support_score = rate_support_score;
	}
	public float getRate_ce_score() {
		return rate_ce_score;
	}
	public void setRate_ce_score(float rate_ce_score) {
		this.rate_ce_score = rate_ce_score;
	}
	public String getTestplan_testlink() {
		return testplan_testlink;
	}
	public void setTestplan_testlink(String testplan_testlink) {
		this.testplan_testlink = testplan_testlink;
	}
	public String getVersion_redmine() {
		return version_redmine;
	}
	public void setVersion_redmine(String version_redmine) {
		this.version_redmine = version_redmine;
	}
	public String getBuild_sonar() {
		return build_sonar;
	}
	public void setBuild_sonar(String build_sonar) {
		this.build_sonar = build_sonar;
	}
	public int getSprint_score_five() {
		return sprint_score_five;
	}
	public void setSprint_score_five(int sprint_score_five) {
		this.sprint_score_five = sprint_score_five;
	}
	public float getIpd_score_origin() {
		return ipd_score_origin;
	}
	public void setIpd_score_origin(float ipd_score_origin) {
		this.ipd_score_origin = ipd_score_origin;
	}
	public float getLmt_score_origin() {
		return lmt_score_origin;
	}
	public void setLmt_score_origin(float lmt_score_origin) {
		this.lmt_score_origin = lmt_score_origin;
	}
	public float getSonar_score_origin() {
		return sonar_score_origin;
	}
	public void setSonar_score_origin(float sonar_score_origin) {
		this.sonar_score_origin = sonar_score_origin;
	}
	public float getTest_pass_score_origin() {
		return test_pass_score_origin;
	}
	public void setTest_pass_score_origin(float test_pass_score_origin) {
		this.test_pass_score_origin = test_pass_score_origin;
	}
	public float getTc_exec_score_origin() {
		return tc_exec_score_origin;
	}
	public void setTc_exec_score_origin(float tc_exec_score_origin) {
		this.tc_exec_score_origin = tc_exec_score_origin;
	}
	public float getBug_new_score_origin() {
		return bug_new_score_origin;
	}
	public void setBug_new_score_origin(float bug_new_score_origin) {
		this.bug_new_score_origin = bug_new_score_origin;
	}
	public float getBug_reopen_score_origin() {
		return bug_reopen_score_origin;
	}
	public void setBug_reopen_score_origin(float bug_reopen_score_origin) {
		this.bug_reopen_score_origin = bug_reopen_score_origin;
	}
	public float getBug_escape_score_origin() {
		return bug_escape_score_origin;
	}
	public void setBug_escape_score_origin(float bug_escape_score_origin) {
		this.bug_escape_score_origin = bug_escape_score_origin;
	}
	public float getRate_patch_score_origin() {
		return rate_patch_score_origin;
	}
	public void setRate_patch_score_origin(float rate_patch_score_origin) {
		this.rate_patch_score_origin = rate_patch_score_origin;
	}
	public float getRate_support_score_origin() {
		return rate_support_score_origin;
	}
	public void setRate_support_score_origin(float rate_support_score_origin) {
		this.rate_support_score_origin = rate_support_score_origin;
	}
	public float getRate_ce_score_origin() {
		return rate_ce_score_origin;
	}
	public void setRate_ce_score_origin(float rate_ce_score_origin) {
		this.rate_ce_score_origin = rate_ce_score_origin;
	}
}
