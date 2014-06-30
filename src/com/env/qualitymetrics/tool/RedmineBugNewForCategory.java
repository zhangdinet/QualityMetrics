package com.env.qualitymetrics.tool;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.env.qualitymetrics.common.SysUtil;

//缺陷新增率
public class RedmineBugNewForCategory {
	private static final Logger log = LoggerFactory.getLogger(RedmineBugNewForCategory.class);
	RedmineCommon redmineCommon=new RedmineCommon();
	private JdbcTemplate jdbcTemplate;

    public void setdataSource_redmine(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	public void setRedmineCommon(RedmineCommon redmineCommon) {
		this.redmineCommon = redmineCommon;
	}
	/***
	 * 缺陷新增率
	 * @param projectName	Redmine项目名
	 * @param versionName	Redmine中sprint名
	 * @param startDate		sprint的开始日期
	 * @param endDate		sprint的结束日期
	 * @return
	 */
	public float getBugNewRate(String projectName,String versionNames,String categoryNames,String startDate,String endDate){
		float rate=-1;
		StringBuilder query=new StringBuilder();
		log.info("get bug new score start...");
		int projectId=redmineCommon.getProjectId(projectName);
		if(projectId==-1) return rate;	
		String nameArrays[] = versionNames.split(SysUtil.splitFlag);
		//拼接字符串形如 and (version1=? or version2=? or ...)
		StringBuilder sub1=new StringBuilder();
		StringBuilder sub2=new StringBuilder();
		StringBuilder version=new StringBuilder();
		for(int i=0;i<nameArrays.length;i++){
			String field1="cv2.value";
			String field2="i.fixed_version_id";
			String versionName=nameArrays[i].trim();
			version.append(" "+versionName);
			int versionId=redmineCommon.getVersionId(projectName, versionName);
			if(versionId==-1){
				if((sub1.length()!=0)&&(i==nameArrays.length-1)){
					sub1.append(")");
					sub2.append(")");
				}
				continue;
			}
			if(sub1.length()==0){
				sub1.append(" and ("+field1+"="+versionId);
				sub2.append(" and ("+field2+"="+versionId);
			}	
			else{
				sub1.append(" or "+field1+"="+versionId);
				sub2.append(" or "+field2+"="+versionId);
			}
			if(i==nameArrays.length-1){
				sub1.append(")");
				sub2.append(")");
			}
		}
		if(sub1.length()==0) return rate;
		nameArrays = categoryNames.split(SysUtil.splitFlag);
		//拼接字符串形如 and (category_id=? or category_id=? or ...)
		StringBuilder sub3=new StringBuilder();
		StringBuilder category=new StringBuilder();
		for(int i=0;i<nameArrays.length;i++){
			String field3="i.category_id";
			String categoryName=nameArrays[i].trim();
			category.append(" "+categoryName);
			int categoryId=redmineCommon.getCatoryId(projectName, categoryName);
			if(categoryId==-1){
				if((sub3.length()!=0)&&(i==nameArrays.length-1)){
					sub3.append(")");
				}
				continue;
			}
			if(sub3.length()==0){
				sub3.append(" and ("+field3+"="+categoryId);
			}	
			else{
				sub3.append(" or "+field3+"="+categoryId);
			}
			if(i==nameArrays.length-1){
				sub3.append(")");
			}
		}
		if(sub3.length()==0) return rate;
		//统计affected version中bug状态!=rejected的数量
		String sql_new="select count(0) from issues i "
			+"inner join custom_values cv2 on cv2.customized_id=i.id "
			+"inner join versions on versions.id=cv2.value "
			+"where cv2.custom_field_id = 9 "
			+"and i.project_id=? "
			+"and i.tracker_id=1 and i.status_id!=6 and i.created_on between ? and ?";
		//query.delete(0, query.length());
		query.append(sql_new);
		query.append(sub1);
		query.append(sub3);
		int newCount=this.jdbcTemplate.queryForObject(query.toString(),new Object[]{projectId,startDate+" 00:00:00",endDate+" 23:59:59"}, Integer.class);
		log.info("There are "+newCount+" bugs in "+projectName+" version<"+version+"> category<"+category+">");
		//统计target version中feature状态=verified,closed,pending verification的数量
		String sql_feature="select count(0) from issues i "
			+"where i.project_id=? and i.tracker_id=2 and (i.status_id=4 or i.status_id=5 or i.status_id=10)";
		query.delete(0, query.length());
		query.append(sql_feature);
		query.append(sub2);
		query.append(sub3);
		int featureCount=this.jdbcTemplate.queryForObject(query.toString(),new Object[]{projectId}, Integer.class);
		//统计target version中bug状态=verified,closed,pending verification的数量
		String sql_fix="select count(0) from issues i "
			+"where i.project_id=? and i.tracker_id=1 and (i.status_id=4 or i.status_id=5 or i.status_id=10)";
		query.delete(0, query.length());
		query.append(sql_fix);
		query.append(sub2);
		query.append(sub3);
		int fixCount=this.jdbcTemplate.queryForObject(query.toString(),new Object[]{projectId}, Integer.class);
		if((featureCount==0)&&(fixCount==0)){
			log.error("There are no feature and bug in "+projectName+" version<"+version+"> category<"+category+">");
			return rate;
		}
		log.info("There are "+featureCount+" features and "+fixCount+" bugs fixed in "+projectName+" version<"+version+"> category<"+category+">");
		rate=(float)newCount/(featureCount+fixCount);
		log.info("bugNew rate is "+rate);
		return rate;
	}
	public int rateToScore(float rate){
		int score=-1;
		if(rate==-1) return score;
		if(rate<=0.5) score=5;
		if(rate>0.5&&rate<=1.0) score=4;
		if(rate>1.0&&rate<=1.5) score=3;
		if(rate>1.5&&rate<=2.0) score=2;
		if(rate>2.0&&rate<=2.5) score=1;
		if(rate>2.5) score=0;
		log.info("bugNew score is "+score);
		return score;
	}

}
