package com.env.qualitymetrics.tool;

import java.util.Date;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

//补丁发布率
public class RedminePatch {
	private static final Logger log = LoggerFactory.getLogger(RedminePatch.class);
	RedmineCommon redmineCommon=new RedmineCommon();
	private JdbcTemplate jdbcTemplate;

    public void setdataSource_redmine(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	public void setRedmineCommon(RedmineCommon redmineCommon) {
		this.redmineCommon = redmineCommon;
	}
	/***
	 * 补丁发布率
	 * @param projectName	Redmine中的项目名
	 * @return
	 */
	public int getPatchScoreUntilNow(String projectName){
		float patchRate=this.getPatchRateUntilNow(projectName);
		int score=this.rateToScore(patchRate);
		return score;
	}
	public int getPatchScoreWithDate(String projectName,String endDate){
		float patchRate=this.getPatchRateWithDate(projectName,endDate);
		int score=this.rateToScore(patchRate);
		return score;
	}
	public int rateToScore(float patchRate){
		int score=-1;
		if(patchRate==-1) return score;
		if(patchRate<=0.5) score=5;
		if(patchRate>0.5&&patchRate<=0.66) score=4;
		if(patchRate>0.66&&patchRate<=0.75) score=3;
		if(patchRate>0.75&&patchRate<=0.8) score=2;
		if(patchRate>0.8) score=1;
		log.info("patch score is "+score);
		return score;
	}
	//统计当年1月1到现在的补丁发布率
	public float getPatchRateUntilNow(String projectName){
		float rate=-1;
		log.info("get patch score start...");
		int projectId=redmineCommon.getProjectId(projectName);
		if(projectId==-1) return rate;
		int year=redmineCommon.getYear();
		if(year==-1){
			log.error("can not get system time!");
			return rate;
		}
		//正式发布的版本个数:版本名字以小写字母”v”开头的，发布日期为当年1月1日到现在的版本个数（包含补丁版本）
		String sql_v="select count(0) from versions v where v.project_id=? and v.name like 'v%' and v.effective_date between ? and ?";
		//正式发布的补丁个数:版本名字以小写字母”v”开头的，中间含有“_”的，发布日期为当年1月1日到现在的版本个数
		String sql_p="select count(0) from versions v where v.project_id=? and v.name like 'v%/_%' ESCAPE'/' "
				+"and v.effective_date between ? and ?";
		Date now=new Date();
		int versionCount=this.jdbcTemplate.queryForObject(sql_v, new Object[]{projectId,year+"-1-1",now}, Integer.class);
		
		if(versionCount==0){
			log.error("There are no versions in "+projectName);
			return rate;
		}
		log.info("There are "+versionCount+" formal versions in "+projectName+" between "+year+"-1-1 and "+now);
		int patchCount=this.jdbcTemplate.queryForObject(sql_p, new Object[]{projectId,year+"-1-1",now}, Integer.class);
		log.info("There are "+patchCount+" patch versions in "+projectName+" since "+year+"-1-1 and "+now);
		rate=(float)patchCount/versionCount;
		log.info("patch rate is "+rate);
		return rate;
	}
	//统计当年1月1到sprint结束时间的的补丁发布率
	public float getPatchRateWithDate(String projectName,String endDate){
		float rate=-1;
		log.info("get patch score start...");
		int projectId=redmineCommon.getProjectId(projectName);
		if(projectId==-1) return rate;
		String year=endDate.substring(0,4);
		//System.out.println(year);
		//正式发布的版本个数:版本名字以小写字母”v”开头的，发布日期为当年1月1日到sprint结束日期的版本个数（包含补丁版本）
		String sql_v="select count(0) from versions v where v.project_id=? and v.name like 'v%' and v.effective_date between ? and ?";
		//正式发布的补丁个数:版本名字以小写字母”v”开头的，中间含有“_”的，发布日期为当年1月1日到sprint结束日期的版本个数
		String sql_p="select count(0) from versions v where v.project_id=? and v.name like 'v%/_%' ESCAPE'/' "
				+"and v.effective_date between ? and ?";
		
		int versionCount=this.jdbcTemplate.queryForObject(sql_v, new Object[]{projectId,year+"-1-1 00:00:00",endDate+" 23:59:59"}, Integer.class);
		if(versionCount==0){
			log.error("There are no versions in "+projectName);
			return rate;
		}
		log.info("There are "+versionCount+" formal versions in "+projectName+" between "+year+"-1-1 and "+endDate+" 23:59:59");
		int patchCount=this.jdbcTemplate.queryForObject(sql_p, new Object[]{projectId,year+"-1-1 00:00:00",endDate+" 23:59:59"}, Integer.class);
		log.info("There are "+patchCount+" patch versions in "+projectName+" since "+year+"-1-1 and "+endDate+" 23:59:59");
		rate=(float)patchCount/versionCount;
		log.info("patch rate is "+rate);
		return rate;
	}
	
}
