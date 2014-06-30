package com.env.qualitymetrics.tool;



import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.env.qualitymetrics.common.SysUtil;
//技术支持率
public class RedmineSupportForCategory {
	private static final Logger log = LoggerFactory.getLogger(RedmineSupportForCategory.class);
	RedmineCommon redmineCommon=new RedmineCommon();
	private JdbcTemplate jdbcTemplate;

    public void setdataSource_redmine(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	public void setRedmineCommon(RedmineCommon redmineCommon) {
		this.redmineCommon = redmineCommon;
	}
	/***
	 * 技术支持率
	 * @param projectName	Redmine项目名
	 * @param versionName	Redmine中sprint名
	 * @param supportName	Redmine中‘工程技术支持’项目里category名
	 * @param endDate		sprint的结束日期
	 * @return
	 */
	public float getSupportRate(String projectName,String versionNames,String categoryNames,String supportName,String endDate){
		float supportRate=-1;
		int featureNumber=this.getFeatureNumber(projectName, versionNames, categoryNames);
		if((featureNumber==0)||(featureNumber==-1)){
			log.error("get feature number error!");
			return supportRate;
		}
		int supportNumber=this.getSupportNumber(supportName, endDate);
		if(supportNumber==-1){
			log.error("get support number error!");
			return supportRate;
		}
		supportRate=(float)supportNumber/featureNumber;
		log.info("support rate is "+supportRate);
		return supportRate;
	}
	public int rateToScore(float supportRate){
		int score=-1;
		if(supportRate==-1) return score;
		if(supportRate<=0.03) score=5;
		if(supportRate>0.03&supportRate<=0.05) score=4;
		if(supportRate>0.05&supportRate<=0.1) score=3;
		if(supportRate>0.1&supportRate<=0.15) score=2;
		if(supportRate>0.15&supportRate<=0.2) score=1;
		if(supportRate>0.2) score=0;
		log.info("support score is "+score);
		return score;
	}
	
	public int getFeatureNumber(String projectName,String versionNames,String categoryNames){
		int flag=-1;
		log.info("get support score start...");
		int projectId=redmineCommon.getProjectId(projectName);
		if(projectId==-1) return flag;
		String nameArrays[] = versionNames.split(SysUtil.splitFlag);
		//拼接字符串形如 and (version1=? or version2=? or ...)
		StringBuilder query=new StringBuilder();
		StringBuilder sub=new StringBuilder();
		StringBuilder version=new StringBuilder();
		for(int i=0;i<nameArrays.length;i++){
			String field1="fixed_version_id";
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
				sub.append(" and ("+field1+"="+versionId);
			}	
			else{
				sub.append(" or "+field1+"="+versionId);
			}
			if(i==nameArrays.length-1){
				sub.append(")");
			}
		}
		if(sub.length()==0) return flag;
		nameArrays = categoryNames.split(SysUtil.splitFlag);
		//拼接字符串形如 and (category_id=? or category_id=? or ...)
		StringBuilder sub2=new StringBuilder();
		StringBuilder category=new StringBuilder();
		for(int i=0;i<nameArrays.length;i++){
			String field2="category_id";
			String categoryName=nameArrays[i].trim();
			category.append(" "+categoryName);
			int categoryId=redmineCommon.getCatoryId(projectName, categoryName);
			if(categoryId==-1){
				if((sub2.length()!=0)&&(i==nameArrays.length-1)){
					sub2.append(")");
				}
				continue;
			}
			if(sub2.length()==0){
				sub2.append(" and ("+field2+"="+categoryId);
			}	
			else{
				sub2.append(" or "+field2+"="+categoryId);
			}
			if(i==nameArrays.length-1){
				sub2.append(")");
			}
		}
		if(sub2.length()==0) return flag;
		//统计version的feature状态=verified,closed,pending verification个数
		String sql="select count(0) from issues where (status_id=4 or status_id=5 or status_id=10) and project_id=? and tracker_id=2";
		query.append(sql);
		query.append(sub);
		query.append(sub2);
		int feature=this.jdbcTemplate.queryForObject(query.toString(),new Object[]{projectId}, Integer.class);
		System.out.println("There are "+feature+" features in "+projectName+" version<"+version+"> category<"+category+">");
		//System.out.print("feature"+feature);
		return feature;
		
	}
	
	public int getSupportNumber(String supportName,String endDate){
		int supportId=redmineCommon.getSupportId(supportName);
		if(supportId==-1) return supportId;
		String delayDate=redmineCommon.transferDate(endDate);
		//统计在“工程技术支持”中，相应项目为SR，时间为sprint结束31天内的issue状态!=rejected数
		String sql="select count(0) from issues i "
			+"where project_id=47 and category_id=? and tracker_id=7 and status_id!=6 "
			+"and created_on between ? and ?";
		int supportNumber=this.jdbcTemplate.queryForObject(sql,new Object[]{supportId,endDate+" 00:00:00",delayDate+" 23:59:59"}, Integer.class);
		System.out.println("There are "+supportNumber+" supports in SR:"+supportName+" between "+endDate+" and "+delayDate);
		return supportNumber;
	}
	
	
	
}
