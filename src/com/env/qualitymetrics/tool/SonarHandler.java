package com.env.qualitymetrics.tool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;


public class SonarHandler {
	private static final Logger log = LoggerFactory.getLogger(SonarHandler.class);
	private JdbcTemplate jdbcTemplate;
	public void setdataSource_sonar(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public float getSonarScoreOrigin(String project_name){
		/*
		 * 首先判断项目的类型：project表判断
		 * 如果是java项目，则可以直接拉取数据：
		 * 1. 从snapshot表中获取project_id为本项目并且is_last=1的snapshot_id
		 * 2. 从project_measures中找metric_id=152(Total quality),
		 *    snapshot_id为第一步获取的 value值，即为该项目的sonar分数
 		 * */
		log.info("开始拉取...sonar中project_name为"+project_name+"的Sonar数据");
		String project_type = getProjectType(project_name);
		Float score = -1f;
		if(project_type!= null){
			score = getProjectTQ(project_name);
		}
		return score;
	}
	
	
	
	public float getSonarScoreOrigin(String project_name,String buildDate){
		/*
		 * 首先判断项目的类型：project表判断
		 * 如果是java项目，则可以直接拉取数据：
		 * 1. 从snapshot表中获取project_id为本项目并且is_last=1的snapshot_id
		 * 2. 从project_measures中找metric_id=152(Total quality),
		 *    snapshot_id为第一步获取的 value值，即为该项目的sonar分数
 		 * */
		log.info("开始拉取...sonar中project_name为"+project_name+"的Sonar数据");
		String project_type = getProjectType(project_name);
		Float score = -1f;
		if(project_type!= null){
			score = getProjectTQ(project_name,buildDate);
		}
		return score;
	}
	
	//========zhangdi 140630=============
	public long getCodeLine(String projectName,String buildDate)
	{
		log.info("开始拉取...sonar中project_name为"+projectName+"的代码行数");
		String project_type = getProjectType(projectName);
		Long codeLine = 0L;
		if(project_type!= null){
			codeLine = getProjectCodeLine(projectName,buildDate);
		}
		return codeLine;
	}
	
	public float getSonarScore(Float originScore){
		float sonarScore = convertScore(originScore);
		return sonarScore;
	}
	//sonar转换分数60分以上平滑过渡,保留一位小数
	private float convertScore(Float score) {
		if(score == -1f){
			return -1;
		}
	
		if(score >=60){
			float b = (float)(Math.round(score/20*10))/10;
			return b;
		}
		else
			return 0;
	}
	
	public List<String> getNames()
	{
		String sql="select name from projects where qualifier = 'TRK'";
		final List<String> lstName=new ArrayList<String>();

		 List rows=this.jdbcTemplate.queryForList(sql);
		 Iterator iterator = rows.iterator();
		 	 
		 for(Object o : rows){
		      Map mapName = (Map) o;
		      lstName.add((String)mapName.get("name"));
		 }		 
		 return lstName;
	}
	
	//====zhangdi  140513======
	
	
    //====zhangdi todo 140513  重构 添加时间参数   继续重构SQL语句=====
	private Float getProjectTQ(String project_name,String buildDate)
	{
		log.info("开始拉取...Sonar中项目名称为："+project_name+"的分数");
		Float tq = -1f;
		try{
			String sql = "select id from projects where name = ? and qualifier = 'TRK'";
			Integer porject_id = this.jdbcTemplate.queryForInt(sql, new Object[]{project_name});
			
			if(porject_id == null){
				log.error("Sonar中无法找到名为："+project_name+"的项目！");
				return -1f;
			}
			
			sql = "select id from snapshots where project_id = ? and build_date = ?";
			Integer snapshot_id = this.jdbcTemplate.queryForInt(sql, new Object[]{porject_id,buildDate}); //时间字段
			if(snapshot_id == null){
				log.error("Sonar中无法找到："+project_name+"的Snapshot版本！");
				return -1f;
			}
			
			sql = "select value from project_measures where metric_id = 152 and snapshot_id = ?";
			tq = this.jdbcTemplate.queryForObject(sql, new Object[]{snapshot_id}, Float.class);
			
			if(tq == null){
				log.error("Sonar中无法找到："+project_name+"项目的Total Quality分数！");
				return -1f;
			}
			
		}catch(Exception e){
			log.error("计算相关项目TotalQuality出现异常，异常信息： "+e.getMessage());
		}
		return tq;
	}
	
	
	private Long getProjectCodeLine(String projectName,String buildDate)
	{
		log.info("开始拉取...Sonar中项目名称为："+projectName);
		Long tq = -1L;
		try{
			String sql = "select id from projects where name = ? and qualifier = 'TRK'";
			Integer porject_id = this.jdbcTemplate.queryForInt(sql, new Object[]{projectName});
			
			if(porject_id == null){
				log.error("Sonar中无法找到名为："+projectName+"的项目！");
				return -1L;
			}
			
			sql = "select id from snapshots where project_id = ? and build_date = ?";
			Integer snapshot_id = this.jdbcTemplate.queryForInt(sql, new Object[]{porject_id,buildDate}); //时间字段
			if(snapshot_id == null){
				log.error("Sonar中无法找到："+projectName+"的Snapshot版本！");
				return -1L;
			}
			
			sql = "select value from project_measures where metric_id = 3 and snapshot_id = ?";
			tq = this.jdbcTemplate.queryForObject(sql, new Object[]{snapshot_id}, BigDecimal.class).longValue();
			
			if(tq == null){
				log.error("Sonar中无法找到："+projectName+"项目的Total Quality代码行数！");
				return -1L;
			}
			
		}catch(Exception e){
			log.error("计算相关项目TotalQuality出现异常，异常信息： "+e.getMessage());
		}
		return tq;
	}
	
	
	
	
	
	//直接到sonar数据库拉取总分即可
	private Float getProjectTQ(String project_name) {
		log.info("开始拉取...Sonar中项目名称为："+project_name+"的分数");
		Float tq = -1f;
		try{
			String sql = "select id from projects where name = ? and qualifier = 'TRK'";
			Integer porject_id = this.jdbcTemplate.queryForInt(sql, new Object[]{project_name});
			
			if(porject_id == null){
				log.error("Sonar中无法找到名为："+project_name+"的项目！");
				return -1f;
			}
			
			sql = "select id from snapshots where project_id = ? and islast = 1";
			Integer snapshot_id = this.jdbcTemplate.queryForInt(sql, new Object[]{porject_id});
			if(snapshot_id == null){
				log.error("Sonar中无法找到："+project_name+"的Snapshot版本！");
				return -1f;
			}
			
			sql = "select value from project_measures where metric_id = 152 and snapshot_id = ?";
			tq = this.jdbcTemplate.queryForObject(sql, new Object[]{snapshot_id}, Float.class);
			
			if(tq == null){
				log.error("Sonar中无法找到："+project_name+"项目的Total Quality分数！");
				return -1f;
			}
			
		}catch(Exception e){
			log.error("计算相关项目TotalQuality出现异常，异常信息： "+e.getMessage());
		}
		return tq;
	}

	private String getProjectType(String project_name) {
		String sql = "select language from projects where name = ? and qualifier = 'TRK'";
		String language = null;
		try{
			language = this.jdbcTemplate.queryForObject(sql, new Object[]{project_name},String.class);
			if(language == null){
				log.error("Sonar中无法找到名为："+project_name+"的项目");
			}
		}catch(Exception e){
			log.error("Sonar中无法找到名为："+project_name+"的项目,异常信息： "+e.getMessage());
		}
		
		return language;
	}

	public int getProjectId(String sonar_name) {
		String sql = "select id from projects where name = ? and qualifier='TRK'";
		Integer porject_id = null;
		try{
			porject_id = this.jdbcTemplate.queryForInt(sql, new Object[]{sonar_name});
			if(porject_id == null){
				log.error("Sonar中无法找到名为："+sonar_name+"的项目！");
				return -1;
			}else{
				return porject_id;
			}
		}catch(Exception e){
			log.error("Sonar中无法找到名为："+sonar_name+"的项目！,异常信息："+e.getMessage());
			return -1;
		}
	}

	public boolean checkTotalScore(String projectID,String buildDate)
	{
		String sql;
		try
		{
			sql = "select id from snapshots where project_id = ? and build_date = ?";
			Integer snapshot_id = this.jdbcTemplate.queryForInt(sql, new Object[]{projectID,buildDate});
			if(snapshot_id == null)
			{
				return false;
			}
		 
			sql = "select value from project_measures where metric_id = 152 and snapshot_id = ?";
			Float totalScore = -1.0f;
			totalScore = this.jdbcTemplate.queryForObject(sql, new Object[]{snapshot_id}, Float.class);
			if(totalScore == null || totalScore < 0)
			{
				return false;
			}
			
		}catch(Exception e){
			return false;
		}
		return true;
	}

	
	public List<String> getBuildDatesByID(String projectID)
	{
		String sql="select build_date from snapshots where project_id= '"+ projectID+"'";
		DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		final List<String> lstName = new ArrayList<String>();
		List rows=this.jdbcTemplate.queryForList(sql);
		Iterator iterator = rows.iterator();
		String strDate;
		for(Object o : rows)
		{
		     Map mapName = (Map) o;
		     Timestamp t=(Timestamp)mapName.get("build_date");
		     strDate=dateformat.format(t);
		     if(checkTotalScore(projectID,strDate))
		     {
		    	lstName.add(strDate);
		     }
		}
		return lstName;
	}
}
