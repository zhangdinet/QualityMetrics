package com.env.qualitymetrics.tool;

import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.env.qualitymetrics.common.SysUtil;
//缺陷遗漏率
public class RedmineBugOmission{
	private static final Logger log = LoggerFactory.getLogger(RedmineBugOmission.class);
	HashMap<String,Integer> b_map=new HashMap<String,Integer>();
	HashMap<String,Integer> t_map=new HashMap<String,Integer>();
	String severity[]={"Critical","Major","Average","Minor"};
	StringBuilder version=new StringBuilder();
	RedmineCommon redmineCommon=new RedmineCommon();
	private JdbcTemplate jdbcTemplate;

    public void setdataSource_redmine(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	public void setRedmineCommon(RedmineCommon redmineCommon) {
		this.redmineCommon = redmineCommon;
	}

	/***
	 * 缺陷遗漏率
	 * @param projectName	Redmine项目名
	 * @param versionName	Redmine中sprint名
	 * @param supportName	Redmine中‘工程技术支持’项目里category名
	 * @param startDate		sprint的开始日期
	 * @param endDate		sprint的结束日期
	 * @return
	 */
	public float getBugOmissionRate(String projectName,String versionNames,String supportName,String startDate,String endDate){
		float rate=-1;
		int flag=this.setMap(projectName, versionNames, supportName,startDate, endDate);
		if(flag==-1) {
			log.error("BugOmission setMap failed!");
			return flag;
		}
		//权重系数critical为3，major为2，average为1，minor为0
		float b=b_map.get("Critical")*3+b_map.get("Major")*2+b_map.get("Average")+b_map.get("Minor")*0.5f;
		float total=t_map.get("Critical")*3+t_map.get("Major")*2+t_map.get("Average")+t_map.get("Minor")*0.5f;
		log.info("b grade is "+b+", total grade is "+total+" in "+projectName+" version<"+version+"> between "+startDate+" and "+endDate);
		if((total==0)&&(b==0)){
			log.error("There are no bug in "+projectName+" version<"+version+"> between "+startDate+" and "+endDate);
			return rate;
		}
		rate=b/(total+b);
		log.info("bug omission rate is "+rate);
		return rate;
	}
	public int setMap(String projectName,String versionNames,String supportName,String startDate,String endDate){
		int flag=-1;
		log.info("get bug omission score start...");
		int projectId=redmineCommon.getProjectId(projectName);
		if(projectId==-1) return flag;	
		int supportId=redmineCommon.getSupportId(supportName);
		if(supportId==-1) return supportId;
		String delayDate=redmineCommon.transferDate(endDate);
		String nameArrays[] = versionNames.split(SysUtil.splitFlag);
		//拼接字符串形如 and (version1=? or version2=? or ...)
		StringBuilder query=new StringBuilder();
		StringBuilder sub1=new StringBuilder();
		for(int i=0;i<nameArrays.length;i++){
			String field1="cv2.value";
			String versionName=nameArrays[i].trim();
			version.append(" "+versionName);
			int versionId=redmineCommon.getVersionId(projectName, versionName);
			if(versionId==-1){
				if((sub1.length()!=0)&&(i==nameArrays.length-1)){
					sub1.append(")");
				}
				continue;
			}
			if(sub1.length()==0){
				sub1.append(" and ("+field1+"="+versionId);
			}	
			else{
				sub1.append(" or "+field1+"="+versionId);
			}
			if(i==nameArrays.length-1){
				sub1.append(")");
			}
		}
		if(sub1.length()==0) return flag;
		//统计每个severity的缺陷状态!=rejected总数量
		String sql="select count(distinct issues.id) from issues "
			+"inner join custom_values cv1 on cv1.customized_id=issues.id "
			+"inner join custom_values cv2 on cv2.customized_id=issues.id "
			+"inner join versions on versions.id=cv2.value "
			+"where cv1.custom_field_id = 4 and cv2.custom_field_id = 9 "
			+"and issues.project_id=? and issues.status_id!=6 "
			+"and issues.created_on between ? and ? and cv1.value=?";
		query.append(sql);
		query.append(sub1);
		//统计每个severity外部缺陷总数: 统计在“工程技术支持”中，相应项目为DF，时间为sprint结束31天内的issue状态!=rejected数
		String sql_2="select count(0) from issues i inner join custom_values cv1 on cv1.customized_id=i.id "
			+"where project_id=47 and category_id=? and tracker_id=6 and status_id!=6 "
			+"and cv1.custom_field_id = 4 and cv1.value=? "
			+"and created_on between ? and ?";
		//severity为空默认统计到average
		String sql_add="select count(0) from issues i where i.id not in "
			+"(select cv1.customized_id from custom_values cv1 where cv1.customized_id=i.id and cv1.custom_field_id = 4) "
			+"and project_id=47 and category_id=? and tracker_id=6 and status_id!=6 "
			+"and created_on between ? and ?";
		for(int i=0;i<severity.length;i++){
			int count=this.jdbcTemplate.queryForObject(query.toString(),new Object[]{projectId,startDate+" 00:00:00",endDate+" 23:59:59",severity[i]}, Integer.class);
			t_map.put(severity[i], count);
			
			count=this.jdbcTemplate.queryForObject(sql_2,new Object[]{supportId,severity[i],endDate+" 00:00:00",delayDate+" 23:59:59"}, Integer.class);
			if(i==2){
				int addition=this.jdbcTemplate.queryForObject(sql_add,new Object[]{supportId,endDate+" 00:00:00",delayDate+" 23:59:59"}, Integer.class);
				count+=addition;
			}
			
			b_map.put(severity[i], count);
		}
		flag=1;
		return flag;
	}
	
	public int rateToScore(float rate){
		int score=-1;
		if(rate==-1) return score;
		if(rate<=0.05) score=5;
		if(rate>0.05&&rate<=0.1) score=4;
		if(rate>0.1&&rate<=0.15) score=3;
		if(rate>0.15&&rate<=0.2) score=2;
		if(rate>0.2&&rate<=0.25) score=1;
		if(rate>0.25) score=0;
		log.info("bug omission score is "+score);
		return score;
	}
	
}
