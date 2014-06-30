package com.env.qualitymetrics.tool;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.env.qualitymetrics.common.SysUtil;

//问题返工率
public class RedmineReopen {
	private static final Logger log = LoggerFactory.getLogger(RedmineReopen.class);
	RedmineCommon redmineCommon=new RedmineCommon();
	private JdbcTemplate jdbcTemplate;

    public void setdataSource_redmine(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	public void setRedmineCommon(RedmineCommon redmineCommon) {
		this.redmineCommon = redmineCommon;
	}
	/***
	 * 问题返工率
	 * @param projectName	Redmine项目名
	 * @param versionName	Redmine中sprint名
	 * @param startDate		sprint的开始日期
	 * @param endDate		sprint的结束日期
	 * @return
	 */
	public float getReopenRate(String projectName,String versionNames,String startDate,String endDate){
		float rate=-1;
		int reopenCount=0;
		log.info("get reopen score start...");
		int projectId=redmineCommon.getProjectId(projectName);
		if(projectId==-1) return rate;
		String nameArrays[] = versionNames.split(SysUtil.splitFlag);
		//拼接字符串形如 and (version1=? or version2=? or ...)
		StringBuilder sub=new StringBuilder();
		StringBuilder version=new StringBuilder();
		StringBuilder query=new StringBuilder();
		for(int i=0;i<nameArrays.length;i++){
			String field="i.fixed_version_id";
			String versionName=nameArrays[i].trim();
			version.append(" "+versionName);
			int versionId=redmineCommon.getVersionId(projectName, versionName);
			if(versionId==-1){
				if((sub.length()!=0)&&(i==nameArrays.length-1)){
					sub.append(")");
				}
				continue;
			}
			if(sub.length()==0){
				sub.append(" and ("+field+"="+versionId);
			}	
			else{
				sub.append(" or "+field+"="+versionId);
			}
			if(i==nameArrays.length-1){
				sub.append(")");
			}
		}
		if(sub.length()==0) return rate;
		
		//先检查是否有reopen
		String sql_reopen_check="select count(j.created_on) from journal_details jd "
			+"inner join journals j on jd.journal_id = j.id "
			+"inner join issues iss on j.journalized_id = iss.id where iss.project_id=? "
			+"and jd.prop_key='status_id' and jd.value=8 "
			+"and j.created_on between ? and ?";
		int reopenCheck=this.jdbcTemplate.queryForObject(sql_reopen_check,new Object[]{projectId,startDate+" 00:00:00",endDate+" 23:59:59"}, Integer.class);
		if(reopenCheck!=0){
		//统计问题返工加权数量
		String sql_reopen="select sum(co2) from (select count(j.created_on)*count(j.created_on) co2 "
			+"from journal_details jd inner join journals j ON jd.journal_id = j.id inner join issues iss ON j.journalized_id = iss.id "
			+"where j.created_on between ? and ? "
			+"and jd.prop_key='status_id' and jd.value=8 and iss.project_id=? group by iss.id) co1";
		reopenCount=this.jdbcTemplate.queryForObject(sql_reopen,new Object[]{startDate+" 00:00:00",endDate+" 23:59:59",projectId}, Integer.class);
		}
		log.info("The reopen weight count is "+reopenCount+" in "+projectName+" between "+startDate+" and "+endDate);
		//统计target version中feature状态=verified,closed,pending verification的数量
		String sql_feature="select count(0) from issues i "
			+"where i.project_id=? and i.tracker_id=2 and (i.status_id=4 or i.status_id=5 or i.status_id=10)";
		//query.delete(0, query.length());
		query.append(sql_feature);
		query.append(sub);
		int featureCount=this.jdbcTemplate.queryForObject(query.toString(),new Object[]{projectId}, Integer.class);
		//统计target version中bug状态=verified,closed,pending verification的数量
		String sql_fix="select count(0) from issues i "
			+"where i.project_id=? and i.tracker_id=1 and (i.status_id=4 or i.status_id=5 or i.status_id=10)";
		query.delete(0, query.length());
		query.append(sql_fix);
		query.append(sub);
		int fixCount=this.jdbcTemplate.queryForObject(query.toString(),new Object[]{projectId}, Integer.class);
		if((featureCount==0)&&(fixCount==0)){
			log.error("There are no feature and bug in "+projectName+" versions"+version);
			return rate;
		}
		log.info("There are "+featureCount+" features and "+fixCount+" bugs fixed in "+projectName+" versions"+version);
		rate=(float)reopenCount/(featureCount+fixCount);
		log.info("reopen rate is "+rate);
		return rate;
	}
	public int rateToScore(float rate){
		int score=-1;
		if(rate==-1) return score;
		if(rate<=0.04) score=5;
		if(rate>0.04&&rate<=0.07) score=4;
		if(rate>0.07&&rate<=0.1) score=3;
		if(rate>0.1&&rate<=0.15) score=2;
		if(rate>0.15&&rate<=0.2) score=1;
		if(rate>0.2) score=0;
		log.info("reopen score is "+score);
		return score;
	}
	
}
