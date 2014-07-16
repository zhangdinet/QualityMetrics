package com.env.qualitymetrics.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SysUtil {

	public static long sonarCheckDay = 1;
	public static long afterTestCheckDay = 7;
	public static long afterReleaseCheckDay = 31;
	public static String test_pass = "test_pass";
	public static String tc_exec = "tc_exec";
	public static String bug_new = "bug_new";
	public static String bug_reopen = "bug_reopen";
	public static String sonar = "sonar";
	public static String bug_escape = "bug_escape";
	public static String rate_patch = "rate_patch";
	public static String rate_support = "rate_support";
	public static String rate_ce="rate_ce";
	public static String project_name_rm_support="Redmine_support";
	public static String lmtOrIpd="lmtOrIpd";
	public static String splitFlag="<br>";	//====zhangdi 140509 考虑分割方式归类===
	public static String splitSuite="--";//testlink分模块时分隔top suite和second suite
	public static String category_name_rm="redmine_category";
	public static String suite_name_tl="testlink_topsuite";
	public static int project_flag=1;
	public static int module_flag=0;
	
	public static float des;
	public static float code ;
	public static float ut;
	public static float classComplexity;
	public static float functionComplexity;
	public static float fileComplexity;
	public static float doc;
	public static float rules;
	public static float dryness;
	public static float codeCoverage;
	public static float unitTestSuccessDensity;
	public static int score_item_num = 10;
	public static String initPwd="123456";
	public static String key = "12345678";
	
	
	public static String formatDate(Date date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if(date==null)
		{
			return null;
		}
		return format.format(date);
	}
	
	public static String encodeWithUtf8(String name)
	{
		String tempName="";
		try {
			tempName=URLEncoder.encode(URLEncoder.encode(name,"UTF-8"),"UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return tempName;
	}
	
	public static String decodeUtf8(String name)
	{
		String tempName="";
		try {
			tempName=URLDecoder.decode(URLDecoder.decode(name, "UTF-8"),"UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return tempName;
	}
	
	
	public static String gbkToUft8(String gbk) throws UnsupportedEncodingException{
		String iso = new String(gbk.getBytes("UTF-8"),"ISO-8859-1");
		String utf8 = new String(iso.getBytes("ISO-8859-1"),"UTF-8");
		return utf8;
	}
	
	public static float formatFloat(float rate){
		float formatRate=(float)(Math.round(rate*1000))/10;
		return formatRate;
	}
	//过滤testlink的suite，如果选中top suite，其下的二级suite移除
	public static String suiteFilter(String[] arraySuites){
		//suiteList用于保存总的suite列表，topSuite用于保存top suite
		List suiteList=new ArrayList<String>();
		List topSuite=new ArrayList<String>();
		for(int i=0;i<arraySuites.length;i++){
			if(!arraySuites[i].contains(SysUtil.splitSuite)){
				//是top suite，加入列表
				suiteList.add(arraySuites[i]);
				topSuite.add(arraySuites[i].trim());
			}else{
				//不是top suite，判断给suite的top suite是否已存在
				String[] temp=arraySuites[i].split(SysUtil.splitSuite);
				String temp_top=temp[0].trim();
				//top suite不存在，加入列表
				if(topSuite.size()==0||!temp_top.equals(topSuite.get(topSuite.size()-1))){
						suiteList.add(arraySuites[i]);
						
					}

			}
	
		}
		String suites=(String)suiteList.toString();
		//System.out.println(suites);
		return suites;
	}
	public static Float getDes() {
		return des;
	}

	public static void setDes(Float des) {
		SysUtil.des = des;
	}

	public static long getSonarCheckDay() {
		return sonarCheckDay;
	}

	public static void setSonarCheckDay(long sonarCheckDay) {
		SysUtil.sonarCheckDay = sonarCheckDay;
	}

	public static long getAfterTestCheckDay() {
		return afterTestCheckDay;
	}

	public static void setAfterTestCheckDay(long afterTestCheckDay) {
		SysUtil.afterTestCheckDay = afterTestCheckDay;
	}

	public static long getAfterReleaseCheckDay() {
		return afterReleaseCheckDay;
	}

	public static void setAfterReleaseCheckDay(long afterReleaseCheckDay) {
		SysUtil.afterReleaseCheckDay = afterReleaseCheckDay;
	}

	public static String getTest_pass() {
		return test_pass;
	}

	public static void setTest_pass(String test_pass) {
		SysUtil.test_pass = test_pass;
	}

	public static String getTc_exec() {
		return tc_exec;
	}

	public static void setTc_exec(String tc_exec) {
		SysUtil.tc_exec = tc_exec;
	}

	public static String getBug_new() {
		return bug_new;
	}

	public static void setBug_new(String bug_new) {
		SysUtil.bug_new = bug_new;
	}

	public static String getBug_reopen() {
		return bug_reopen;
	}

	public static void setBug_reopen(String bug_reopen) {
		SysUtil.bug_reopen = bug_reopen;
	}

	public static String getSonar() {
		return sonar;
	}

	public static void setSonar(String sonar) {
		SysUtil.sonar = sonar;
	}

	public static String getBug_escape() {
		return bug_escape;
	}

	public static void setBug_escape(String bug_escape) {
		SysUtil.bug_escape = bug_escape;
	}

	public static String getRate_patch() {
		return rate_patch;
	}

	public static void setRate_patch(String rate_patch) {
		SysUtil.rate_patch = rate_patch;
	}

	public static String getRate_support() {
		return rate_support;
	}

	public static void setRate_support(String rate_support) {
		SysUtil.rate_support = rate_support;
	}

	public static String getRate_ce() {
		return rate_ce;
	}

	public static void setRate_ce(String rate_ce) {
		SysUtil.rate_ce = rate_ce;
	}

	public static String getProject_name_rm_support() {
		return project_name_rm_support;
	}

	public static void setProject_name_rm_support(String project_name_rm_support) {
		SysUtil.project_name_rm_support = project_name_rm_support;
	}

	public static float getCode() {
		return code;
	}

	public static void setCode(float code) {
		SysUtil.code = code;
	}

	public static float getUt() {
		return ut;
	}

	public static void setUt(float ut) {
		SysUtil.ut = ut;
	}

	public static float getClassComplexity() {
		return classComplexity;
	}

	public static void setClassComplexity(float classComplexity) {
		SysUtil.classComplexity = classComplexity;
	}

	public static float getFunctionComplexity() {
		return functionComplexity;
	}

	public static void setFunctionComplexity(float functionComplexity) {
		SysUtil.functionComplexity = functionComplexity;
	}

	public static float getFileComplexity() {
		return fileComplexity;
	}

	public static void setFileComplexity(float fileComplexity) {
		SysUtil.fileComplexity = fileComplexity;
	}

	public static float getDoc() {
		return doc;
	}

	public static void setDoc(float doc) {
		SysUtil.doc = doc;
	}

	public static float getRules() {
		return rules;
	}

	public static void setRules(float rules) {
		SysUtil.rules = rules;
	}

	public static float getDryness() {
		return dryness;
	}

	public static void setDryness(float dryness) {
		SysUtil.dryness = dryness;
	}

	public static float getCodeCoverage() {
		return codeCoverage;
	}

	public static void setCodeCoverage(float codeCoverage) {
		SysUtil.codeCoverage = codeCoverage;
	}

	public static float getUnitTestSuccessDensity() {
		return unitTestSuccessDensity;
	}

	public static void setUnitTestSuccessDensity(float unitTestSuccessDensity) {
		SysUtil.unitTestSuccessDensity = unitTestSuccessDensity;
	}

	public static void setDes(float des) {
		SysUtil.des = des;
	}

	public static int convertProjectScore(float avg_score) {
		if(avg_score>=90){
			return 5;
		}
		if(avg_score>=80){
			return 4;
		}
		if(avg_score>=70){
			return 3;
		}
		if(avg_score>=60){
			return 2;
		}
		if(avg_score>=50){
			return 1;
		}
		return 0;
	}

	public static int convertSprintScore(float sprint_score) {
		if(sprint_score>=30){
			return 5;
		}
		if(sprint_score>=25){
			return 4;
		}
		if(sprint_score>=20){
			return 3;
		}
		if(sprint_score>=15){
			return 2;
		}
		if(sprint_score>=10){
			return 1;
		}
		return 0;
	}

	public static String getCategory_name_rm() {
		return category_name_rm;
	}

	public static void setCategory_name_rm(String category_name_rm) {
		SysUtil.category_name_rm = category_name_rm;
	}

	public static String getSuite_name_tl() {
		return suite_name_tl;
	}

	public static void setSuite_name_tl(String suite_name_tl) {
		SysUtil.suite_name_tl = suite_name_tl;
	}

	public static int getProject_flag() {
		return project_flag;
	}

	public static void setProject_flag(int project_flag) {
		SysUtil.project_flag = project_flag;
	}

	public static int getModule_flag() {
		return module_flag;
	}

	public static void setModule_flag(int module_flag) {
		SysUtil.module_flag = module_flag;
	}

	public static String getLmtOrIpd() {
		return lmtOrIpd;
	}

	public static void setLmtOrIpd(String lmtOrIpd) {
		SysUtil.lmtOrIpd = lmtOrIpd;
	}
}
