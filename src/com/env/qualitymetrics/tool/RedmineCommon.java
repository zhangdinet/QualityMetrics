package com.env.qualitymetrics.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
//import java.util.HashMap;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class RedmineCommon {
	private static final Logger log = LoggerFactory.getLogger(RedmineCommon.class);
	private JdbcTemplate jdbcTemplate;

    public void setdataSource_redmine(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<String> getRedmineSupportProjectNames()
   	{
   		final List<String> lstName=new ArrayList<String>();
   		String sql="select name from issue_categories where project_id=47 order by name";
   		
   		 List rows=this.jdbcTemplate.queryForList(sql);
   		 for(Object o : rows){
   		      Map mapName = (Map) o;
   		      lstName.add((String)mapName.get("name"));
   		 }
   		 return lstName;
   	}     

    public List<String> getVersionNames(int pro_id)
   	{
    	final List<String> lstName=new ArrayList<String>();
    	String sql="select name from versions where project_id ="+pro_id;
    	
    	 List rows=this.jdbcTemplate.queryForList(sql);
		 for(Object o : rows){
		      Map mapName = (Map) o;
		      lstName.add((String)mapName.get("name"));
		 }
		 return lstName;
   	} 
    
    public List<String> getRedmineProjectNames()
	{
		final List<String> lstName=new ArrayList<String>();
		String sql="select name from projects order by name";
		
		List rows=this.jdbcTemplate.queryForList(sql);
		for(Object o : rows){
			Map mapName = (Map) o;
			lstName.add((String)mapName.get("name"));
		}
		return lstName;
	}
    //get category names by project name
    public List<String> getCategoryNamesByProjectName(String projectName)
   	{
    	final List<String> lstName=new ArrayList<String>();
    	String sql="select ic.name from issue_categories ic inner join projects p on p.id=ic.project_id "
    		+"where p.name=?";
    	
    	 List rows=this.jdbcTemplate.queryForList(sql,new Object[]{projectName});
		 Iterator iterator = rows.iterator();
		
		 for(Object o : rows){
		      Map mapName = (Map) o;
		      lstName.add((String)mapName.get("name"));
		 }		 
		 return lstName; 
   	} 
   
    /***
	 * 获取projectId
	 * @param projectName	Redmine项目名
	 * @return
	 */
	public int getProjectId(String projectName){
		int projectId=-1;
		String sql="select id from projects p where p.name=?";
		String sql_check="select count(0) from projects p where p.name=?";
		int check=this.jdbcTemplate.queryForObject(sql_check,new Object[]{projectName}, Integer.class);
		if(check==0){
			log.error("project "+projectName+" is not found in Redmine!");
			return projectId;
		}
			projectId=this.jdbcTemplate.queryForObject(sql,new Object[]{projectName}, Integer.class);
		
		return projectId;
	}
	/***
	 * 获取versionId
	 * @param projectName	Redmine项目名
	 * @param versionName	Redmine中sprint名
	 * @return
	 */
	public int getVersionId(String projectName,String versionName){
		int versionId=-1;
		String sql2="select v.id from versions v inner join projects p on p.id=v.project_id where v.name=? and p.name=?";
		String sql2_check="select count(0) from versions v inner join projects p on p.id=v.project_id where v.name=? and p.name=?";
		int check=this.jdbcTemplate.queryForObject(sql2_check,new Object[]{versionName,projectName}, Integer.class);
		if(check==0){
			log.error("version "+versionName+" is not found in Redmine!");
			return versionId;
		}
			versionId=this.jdbcTemplate.queryForObject(sql2,new Object[]{versionName,projectName}, Integer.class);
		return versionId;
	}
	/***
	 * 获取工程技术支持中CategoryId
	 * @param supportName	'工程技术支持'中category名
	 * @return
	 */
	//get category_id from "工程技术支持"
	public int getSupportId(String supportName){
		int supportId=-1;
		String sql="select ic.id from issue_categories ic "
			+"where ic.name=? and ic.project_id=47";
		String sql2="select count(0) from issue_categories ic "
			+"where ic.name=? and ic.project_id=47";
		int count=this.jdbcTemplate.queryForObject(sql2,new Object[]{supportName}, Integer.class);
		if(count==0){
			log.error(supportName+" doesn't exist in SR 工程技术支持");
			return supportId;
		}
		supportId=this.jdbcTemplate.queryForObject(sql,new Object[]{supportName}, Integer.class);
		return supportId;
	}
	//get category_id 
	public int getCatoryId(String projectName,String categoryName){
		int categoryId=-1;
		String sql="select ic.id from issue_categories ic "
			+"inner join projects p on p.id=ic.project_id "
			+"where ic.name=? and p.name=?";
		String sql2="select count(0) from issue_categories ic "
			+"inner join projects p on p.id=ic.project_id "
			+"where ic.name=? and p.name=?";
		int count=this.jdbcTemplate.queryForObject(sql2,new Object[]{categoryName,projectName}, Integer.class);
		if(count==0){
			log.error(categoryName+" doesn't exist in the project "+projectName);
			return categoryId;
		}
		categoryId=this.jdbcTemplate.queryForObject(sql,new Object[]{categoryName,projectName}, Integer.class);
		return categoryId;
	}
	
	//得到往后增加31天的日期
	public String transferDate(String endDate){
		String delay="";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			 
			Date date = sdf.parse(endDate);
			Calendar   calendar   =   new   GregorianCalendar(); 
		    calendar.setTime(date); 
		    calendar.add(calendar.DATE,31);//把日期往后增加31天.整数往后推,负数往前移动 
		    date=calendar.getTime();   //这个时间就是日期往后推31天的结果
		    delay=sdf.format(date);
		    
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.error("transfer date error!");
			e.printStackTrace();
		}
		//System.out.println("delay date"+delay);
		return delay;
	}
	public int getYear(){
		int year=-1;
		
		Calendar c = new   GregorianCalendar();//可以对每个时间域单独修改
		year = c.get(Calendar.YEAR);
		return year;
	}
	
	public List<String> getFixedBugsByCategory(String redmineName,String version)
	{
		List<String> lstResult=new ArrayList<String>();
		
		String sql="select issue_categories.name, count(*) as bugsCount from issues,issue_categories,versions,projects "+
					"where projects.name = ? and versions.name = ? " +
					"And tracker_id = 1 And issues.category_id = issue_categories.ID and versions.id = issues.fixed_version_id and versions.project_id=projects.id " + 
					"group by category_id order by name";
		
		Object[] params=new Object[]{redmineName,version};
		List rows=this.jdbcTemplate.queryForList(sql, params);
		for(Object o : rows){
			Map map = (Map) o;
			lstResult.add((String)map.get("name")+"#"+map.get("bugsCount"));
		}
		return lstResult;
	}
	

	public List<String> getNewBugsByCategory(String redmineName,String version,String sprintStart,String sprintEnd)
	{
		List<String> lstResult=new ArrayList<String>();
		
		String sql="select issue_categories.name, count(*) as bugsCount from issues,issue_categories,versions,projects "+
					"where projects.name = ? and versions.name = ? and issues.created_on between ? and ? "+
					"And tracker_id = 1 And issues.category_id = issue_categories.ID and issues.status_id <> 6 and versions.id = issues.fixed_version_id and versions.project_id=projects.id " +
					"group by category_id order by name";
		
		Object[] params=new Object[]{redmineName,version,sprintStart+" 00:00:00",sprintEnd+" 23:59:59"};
		List rows=this.jdbcTemplate.queryForList(sql, params);
		for(Object o : rows){
			Map map = (Map) o;
			lstResult.add((String)map.get("name")+"#"+map.get("bugsCount"));
		}
		return lstResult;
	}
	
	public List<String> getFixedRateByCategory(String redmineName,String version,String sprintStart,String sprintEnd)
	{
		List<String> lstResult=new ArrayList<String>();
		List<String> lstName=new ArrayList<String>();
		
		String sql="select issue_categories.name, count(*) as bugsCount from issue_categories " +
				"left join issues on issues.category_id = issue_categories.ID "+
				"join custom_values on custom_values.customized_id = issues.id " +
				"join custom_fields on custom_fields.id = custom_values.custom_field_id " +
				"join projects on projects.id = issues.project_id " +
				"join versions on versions.project_id = projects.id " +
				"where projects.name = ? and versions.name=? and issues.created_on between ? and ? And tracker_id = 1 and issues.status_id <> 6 and custom_fields.ID = 9 "+
				"group by category_id order by name ";
		
		String sqlRate="select issue_categories.name, count(*) as bugsCount from issue_categories " +
				"left join issues on issues.category_id = issue_categories.ID "+
				"join custom_values on custom_values.customized_id = issues.id " +
				"join custom_fields on custom_fields.id = custom_values.custom_field_id " +
				"join projects on projects.id = issues.project_id " +
				"join versions on versions.project_id = projects.id " +
				"where projects.name = ? and versions.name= ? and issues.created_on between ? and ? And tracker_id = 1 and issues.status_id in (4,5,10) and custom_fields.ID = 9 "+
				"group by category_id order by name ";
		
		Object[] params=new Object[]{redmineName,version,sprintStart+" 00:00:00",sprintEnd+" 23:59:59"};
		List rows=this.jdbcTemplate.queryForList(sql, params);
		List rowsFixed=this.jdbcTemplate.queryForList(sqlRate, params);
		List<Long> lstTotal=new ArrayList<Long>();
		List<Long> lstFixed=new ArrayList<Long>();
		for(Object o : rows)
		{
			Map map = (Map) o;
			lstName.add((String)map.get("name"));
			lstTotal.add((Long)map.get("bugsCount"));
		}
		
		for(Object o : rowsFixed)
		{
			Map map = (Map) o;
			lstFixed.add((Long)map.get("bugsCount"));
		}
		
		for(int i=0;i<lstName.size();i++)
		{
			lstResult.add(lstName.get(i) + "#" + (float)lstFixed.get(i)/lstTotal.get(i));
		}
		return lstResult;
	}
	
	public List<String> getImplementedFeaturesByCategory(String redmineName,String version)
	{
		List<String> lstResult=new ArrayList<String>();
		
		String sql="select issue_categories.name, count(*) as featuresCount from issues,issue_categories,versions,projects "+
					"where projects.name = ? and versions.name = ? " +
					"And tracker_id = 2 And issues.category_id = issue_categories.ID and versions.id = issues.fixed_version_id and versions.project_id=projects.id " + 
					"group by category_id order by name";
		
		Object[] params=new Object[]{redmineName,version};
		List rows=this.jdbcTemplate.queryForList(sql, params);
		for(Object o : rows){
			Map map = (Map) o;
			lstResult.add((String)map.get("name")+"#"+map.get("featuresCount"));
		}
		return lstResult;
	}
	
	public List<String> getFixedBugsByDeveloper(String redmineName,String version)
	{
		List<String> lstResult=new ArrayList<String>();
	
		String sql= "select users.login as username,count(*) as bugsCount from issues, Users, versions, projects " + 
					"where projects.name = ? and versions.name = ? "+
					"And tracker_id = 1 and versions.id=issues.fixed_version_id and users.id=issues.assigned_to_id and versions.project_id=projects.id " +
					"group by users.login order by users.login";
		
		Object[] params=new Object[]{redmineName,version};
		List rows=this.jdbcTemplate.queryForList(sql, params);
		for(Object o : rows){
			Map map = (Map) o;
			lstResult.add((String)map.get("username")+"#"+map.get("bugsCount"));
		}
		return lstResult;
	}
	
	public List<String> getNewBugsByDeveloper(String redmineName,String version,String sprintStart,String sprintEnd)
	{
		List<String> lstResult=new ArrayList<String>();
		String sql= "select users.login as username,count(*) as bugsCount from issues, Users, versions, projects " +
					"where projects.name = ? and versions.name = ? and issues.created_on between ? and ? "+
					"And tracker_id = 1 and versions.id=issues.fixed_version_id and issues.status_id <> 6 and users.id=issues.assigned_to_id and versions.project_id=projects.id " +
					"group by users.login order by users.login";
		
		Object[] params=new Object[]{redmineName,version,sprintStart+" 00:00:00",sprintEnd+" 23:59:59"};
		List rows=this.jdbcTemplate.queryForList(sql, params);
		for(Object o : rows){
			Map map = (Map) o;
			lstResult.add((String)map.get("username")+"#"+map.get("bugsCount"));
		}
		return lstResult;
	}
	
	
	public List<String> getFixedByDeveloper(String redmineName,String version,String sprintStart,String sprintEnd)
	{
		List<String> lstResult=new ArrayList<String>();
		String sql= "select users.login as username,count(*) as bugsCount from issues, Users, versions, projects " +
					"where projects.name = ? and versions.name = ? and issues.created_on between ? and ? "+
					"And tracker_id = 1 and versions.id=issues.fixed_version_id and issues.status_id in(4,5,10) and users.id=issues.assigned_to_id and versions.project_id=projects.id " +
					"group by users.login order by users.login";
		
		Object[] params=new Object[]{redmineName,version,sprintStart+" 00:00:00",sprintEnd+" 23:59:59"};
		List rows=this.jdbcTemplate.queryForList(sql, params);
		for(Object o : rows){
			Map map = (Map) o;
			lstResult.add((String)map.get("username")+"#"+map.get("bugsCount"));
		}
		return lstResult;
	}
	
	public List<String> getImplementedFeaturesByDeveloper(String redmineName,String version)
	{
		List<String> lstResult=new ArrayList<String>();
		
		String sql= "select users.login as username,count(*) as featuresCount from issues, Users, versions, projects " +
				"where projects.name = ? and versions.name = ? "+
				"And tracker_id = 2 and versions.id=issues.fixed_version_id and users.id=issues.assigned_to_id and versions.project_id=projects.id " +
				"group by users.login order by users.login";
		
		Object[] params=new Object[]{redmineName,version};
		List rows=this.jdbcTemplate.queryForList(sql, params);
		for(Object o : rows){
			Map map = (Map) o;
			lstResult.add((String)map.get("username")+"#"+map.get("featuresCount"));
		}
		return lstResult;
	}
	
	public List<String> getFixedRateByDeveloper(String redmineName,String version,String sprintStart,String sprintEnd)
	{
		List<String> lstResult=new ArrayList<String>();
		List<String> lstName=new ArrayList<String>();
		
		String sqlTotal = "select users.login as username,count(*) as bugsCount from users " +
						"left join issues on issues.assigned_to_id = users.id "+
						"join custom_values on custom_values.customized_id = issues.id " +
						"join custom_fields on custom_fields.id = custom_values.custom_field_id " +
						"join projects on projects.id = issues.project_id " +
						"join versions on versions.project_id = projects.id "+
						"where projects.name= ? and versions.name= ? and issues.created_on between ? and ? and tracker_id =1 and issues.status_id <> 6 and custom_fields.ID = 9 "+
						"group by username order by username ";
		
		String sqlFixed = "select users.login as username,count(*) as bugsCount from users " +
				"left join issues on issues.assigned_to_id = users.id "+
				"join custom_values on custom_values.customized_id = issues.id " +
				"join custom_fields on custom_fields.id = custom_values.custom_field_id " +
				"join projects on projects.id = issues.project_id " +
				"join versions on versions.project_id = projects.id "+
				"where projects.name= ? and versions.name= ? and issues.created_on between ? and ? and tracker_id =1 and issues.status_id in (4,5,10) and custom_fields.ID = 9 "+
				"group by username order by username ";
		
		Object[] params=new Object[]{redmineName,version,sprintStart+" 00:00:00",sprintEnd+" 23:59:59"};
		List rowsTotal=this.jdbcTemplate.queryForList(sqlTotal, params);
		List rowsFixed=this.jdbcTemplate.queryForList(sqlFixed, params);
		
		List<Long> lstNew=new ArrayList<Long>();
		List<Long> lstFixed=new ArrayList<Long>();
		
		Map<String,Long> userAndFixedCount=new TreeMap<String,Long>();
		Map<String,Long> userAndTotalCount=new TreeMap<String,Long>();
		
		for(Object o : rowsTotal)
		{
			Map map = (Map) o;
			//lstName.add((String)map.get("username"));
			//lstNew.add((Long)map.get("bugsCount"));
			userAndTotalCount.put((String)map.get("username"), (Long)map.get("bugsCount"));
		}
		
		for(Object o : rowsFixed)
		{
			Map map = (Map) o;
			//lstFixed.add((Long)map.get("bugsCount"));
			userAndFixedCount.put((String)map.get("username"), (Long)map.get("bugsCount"));
		}
		
		Iterator iter=userAndTotalCount.entrySet().iterator();
		while(iter.hasNext())
		{
			Map.Entry<String,Long> entry=(Map.Entry<String,Long>)iter.next();
			String username=entry.getKey();
			if(userAndFixedCount.containsKey(username))
			{
				lstResult.add(entry.getKey() + "#" + (float)userAndFixedCount.get(username)/entry.getValue());
			}
			/*else
			{
				lstResult.add(entry.getKey() + "#" + 0);
			}*/
		}
		return lstResult;
	}
	
	
	public List<String> getFixedBugsBySeverity(String redmineName,String version)
	{
		List<String> lstResult=new ArrayList<String>();
		
		String sql= "select custom_values.value as custom,count(*) as bugsCount from issues,custom_values,versions,projects "+ 
					"where projects.name = ? and versions.name = ? " +
					"and versions.id=issues.fixed_version_id and projects.id=versions.project_id and tracker_id = 1 and issues.id=custom_values.customized_id and custom_values.custom_field_id=4 "+
					"group by custom_values.value";
		
		Object[] params=new Object[]{redmineName,version};
		List rows=this.jdbcTemplate.queryForList(sql, params);
		for(Object o : rows){
			Map map = (Map) o;
			lstResult.add((String)map.get("custom")+"#"+map.get("bugsCount"));
		}
		return lstResult;
	}
	
	public List<String> getNewBugsBySeverity(String redmineName,String version,String sprintStart,String sprintEnd)
	{
		List<String> lstResult=new ArrayList<String>();
		
		String sql= "select custom_values.value as custom,count(*) as bugsCount from issues,custom_values,versions,projects "+ 
					"where projects.name = ? and versions.name = ? and issues.created_on between ? and ? "+
					"and versions.id=issues.fixed_version_id and issues.status_id <>6 and projects.id=versions.project_id and tracker_id = 1 and issues.id=custom_values.customized_id and custom_values.custom_field_id=4 "+
					"group by custom_values.value";
		
		Object[] params=new Object[]{redmineName,version,sprintStart+" 00:00:00",sprintEnd+" 23:59:59"};
		List rows=this.jdbcTemplate.queryForList(sql, params);
		for(Object o : rows){
			Map map = (Map) o;
			lstResult.add((String)map.get("custom")+"#"+map.get("bugsCount"));
		}
		return lstResult;
	}
	
	public List<String> getFixedRateBySeverity(String redmineName, String version, String sprintStart, String sprintEnd)
	{
		List<String> lstResult=new ArrayList<String>();
		List<String> lstName=new ArrayList<String>();
		
		String sqlTotal = "select custom_values.value as custom,count(*) as bugsCount from custom_values "+
							"left join issues on issues.id = custom_values.customized_id "+
							"join projects on projects.id = issues.project_id " +
							"join versions on versions.project_id = projects.id "+
							"where projects.name = ? and versions.name = ? and issues.created_on between ? and ? "+
							"and issues.status_id <> 6 and tracker_id = 1 and projects.id=versions.project_id and custom_values.custom_field_id=4 "+
							"group by custom_values.value";
		
		String sqlFixed = "select custom_values.value as custom,count(*) as bugsCount from custom_values "+
							"left join issues on issues.id = custom_values.customized_id "+
							"join projects on projects.id = issues.project_id " +
							"join versions on versions.project_id = projects.id "+
							"where projects.name = ? and versions.name = ? and issues.created_on between ? and ? "+
							"and issues.status_id in(4,5,10) and tracker_id = 1 and projects.id=versions.project_id and custom_values.custom_field_id=4 "+
							"group by custom_values.value";
		
		Object[] params=new Object[]{redmineName,version,sprintStart+" 00:00:00",sprintEnd+" 23:59:59"};
		List rowsTotal=this.jdbcTemplate.queryForList(sqlTotal, params);
		List rowsFixed=this.jdbcTemplate.queryForList(sqlFixed, params);
		
		List<Long> lstNew=new ArrayList<Long>();
		List<Long> lstFixed=new ArrayList<Long>();
		for(Object o : rowsTotal)
		{
			Map map = (Map) o;
			lstName.add((String)map.get("custom"));
			lstNew.add((Long)map.get("bugsCount"));
		}
		
		for(Object o : rowsFixed)
		{
			Map map = (Map) o;
			lstFixed.add((Long)map.get("bugsCount"));
		}
		
		for(int i=0;i<lstName.size();i++)
		{
			lstResult.add(lstName.get(i) + "#" + (float)lstFixed.get(i)/lstNew.get(i));
		}
		return lstResult;
	}
	
	public List<String> getReopenRatio(String redmineName, String version, String sprintStart, String sprintEnd)
	{
		List<String> lstResult=new ArrayList<String>();
		
		String sqlFixed = "select users.login as developer,count(*) as fixedCount from users " +
			"left join issues on issues.assigned_to_id = users.id " +
			"join custom_values on custom_values.customized_id = issues.id " +
			"join custom_fields on custom_fields.id = custom_values.custom_field_id " +
			"join versions on versions.id = issues.fixed_version_id " +
			"join projects on projects.id = versions.project_id " +
			"where projects.name= ? and versions.name= ? and issues.created_on between ? and ? and issues.status_id in (4,5,10) and tracker_id in(1,2) and custom_fields.ID = 9 "+
			"group by developer order by developer";
		
		String sqlReopenByUserAndIssue = "select users.login as developer,issues.id as issid,count(issues.id)*count(issues.id) as reopenCount "+
			"from users "+
			"left join issues on issues.assigned_to_id=users.id "+
			"join projects on projects.id=issues.project_id "+
			"join journals on journals.journalized_id=issues.id "+
			"join journal_details on journal_details.journal_id=journals.id "+
			"join versions on versions.id=issues.fixed_version_id "+
			"where projects.name= ? and versions.name= ? and issues.created_on between ? and ? "+
			"and journal_details.prop_key='status_id' and journal_details.value = 8 "+
			"group by developer,issues.id " +
			"order by developer,issues.id ";
		
		Object[] params=new Object[]{redmineName,version,sprintStart+" 00:00:00",sprintEnd+" 23:59:59"};
		List rowsFixed = this.jdbcTemplate.queryForList(sqlFixed,params);
		List rowsReopenByUserAndIssue =this.jdbcTemplate.queryForList(sqlReopenByUserAndIssue,params);
		
		Map<String,Long> userAndFixedCount=new TreeMap<String,Long>();
		Map<String,Long> userAndReopenCount=new TreeMap<String,Long>();
		Map<String,String> userAndIssues=new TreeMap<String,String>();
		
		for(Object o:rowsReopenByUserAndIssue)
		{
			Map map=(Map)o;
			{
				String key=(String)map.get("developer");
				Long reopenCount=(Long)map.get("reopenCount");
				Integer id=(Integer)map.get("issid");
				if(userAndReopenCount.containsKey(key))
				{
					Long count=userAndReopenCount.get(key);
					//userAndReopenCount.replace(key,count+reopenCount);
					userAndReopenCount.put(key,count+reopenCount);
					String strID=userAndIssues.get(key);
					//userAndIssues.replace(key,strID+"<br>"+id);
					userAndIssues.put(key,strID+"<br>"+id);
				}
				else
				{
					userAndReopenCount.put(key,reopenCount);
					userAndIssues.put(key, id.toString());
				}
			}
		}
		
		for(Object o : rowsFixed)
		{
			Map map = (Map) o;
			String name=(String)map.get("developer");
			if(userAndReopenCount.containsKey(name))
			{
				userAndFixedCount.put(name, (Long)map.get("fixedCount"));
			}
		}
		
		Iterator iter=userAndFixedCount.entrySet().iterator();
		while(iter.hasNext())
		{
			Map.Entry<String,Long> entry=(Map.Entry<String,Long>)iter.next();
			Long reopen=userAndReopenCount.get(entry.getKey());
			float result= (float)reopen/entry.getValue();
			lstResult.add(entry.getKey() + "#" + result+ "#" +userAndIssues.get(entry.getKey()));
		}
		return lstResult;
	}
	
	//========zhangdi 140704========
	public List<String> getBugsScopeStability(String redmineName, String version, String sprintStart, String sprintEnd)
	{
		List<String> lstResult=new ArrayList<String>();
		
		String sqlNew = "select issues.id as id,date_format(issues.created_on,'%Y-%c-%d') as updatedate from issues "+
				"join projects on projects.id=issues.project_id "+
				"join versions on versions.id=issues.fixed_version_id "+
				"where projects.name=? and versions.name=? "+
				"and issues.created_on between ? and ? "+
				"and issues.tracker_id = 1 "+
				"order by issues.created_on desc ";
		
		String sqlUpdate = "select distinct(issues.id) as id, date_format(journals.created_on,'%Y-%c-%d') as updatedate from issues "+
				"join journals on journals.journalized_id=issues.id " +
				"join journal_details on journal_details.journal_id=journals.id " +
				"join projects on projects.id=issues.project_id "+
				"join versions on versions.id=issues.fixed_version_id "+
				"where projects.name=? and versions.name=? "+
				"and issues.created_on between ? and ? "+
				"and journal_details.prop_key='fixed_version_id' "+
				"and issues.tracker_id = 1 "+
				"order by journals.created_on desc ";
		
		Object[] params=new Object[]{redmineName,version,sprintStart+" 00:00:00",sprintEnd+" 23:59:59"};
		
		List rowsNew =this.jdbcTemplate.queryForList(sqlNew,params);
		List rowsUpdate = this.jdbcTemplate.queryForList(sqlUpdate,params);
		Map<Integer,String> idAndDate=new TreeMap<Integer,String>();
		Map<String,Long> dateAndCount=new TreeMap<String,Long>();
		
		for(Object o : rowsNew)
		{
			Map map = (Map) o;
			idAndDate.put((Integer)map.get("id"),(String)map.get("updatedate"));
		}
		
		for(Object o : rowsUpdate)
		{
			Map map = (Map) o;
			idAndDate.put((Integer)map.get("id"),(String)map.get("updatedate"));
		}
		
		Iterator iter=idAndDate.entrySet().iterator();
		
		while(iter.hasNext())
		{
			Map.Entry<Integer,String> entry=(Map.Entry<Integer,String>)iter.next();
			if(dateAndCount.containsKey(entry.getValue()))
			{
				Long count=dateAndCount.get(entry.getValue());
				dateAndCount.put(entry.getValue(), count+1);
			}
			else
			{
				dateAndCount.put(entry.getValue(), 1L);
			}
		}
		
		iter=dateAndCount.entrySet().iterator();
		while(iter.hasNext())
		{
			Map.Entry<String,Long> entry=(Map.Entry<String,Long>)iter.next();
			lstResult.add(entry.getKey() + "#" +entry.getValue().toString());
		}
		return lstResult;
	}
	
	public List<String> getFeaturesScopeStability(String redmineName, String version, String sprintStart, String sprintEnd)
	{
		List<String> lstResult=new ArrayList<String>();
		
		String sqlNew = "select issues.id as id,date_format(issues.created_on,'%Y-%c-%d') as updatedate from issues "+
				"join projects on projects.id=issues.project_id "+
				"join versions on versions.id=issues.fixed_version_id "+
				"where projects.name=? and versions.name=? "+
				"and issues.created_on between ? and ? "+
				"and issues.tracker_id = 2 "+
				"order by issues.created_on desc ";
		
		String sqlUpdate = "select distinct(issues.id) as id, date_format(journals.created_on,'%Y-%c-%d') as updatedate from issues "+
				"join journals on journals.journalized_id=issues.id " +
				"join journal_details on journal_details.journal_id=journals.id " +
				"join projects on projects.id=issues.project_id "+
				"join versions on versions.id=issues.fixed_version_id "+
				"where projects.name=? and versions.name=? "+
				"and issues.created_on between ? and ? "+
				"and journal_details.prop_key='fixed_version_id' "+
				"and issues.tracker_id = 2 "+
				"order by journals.created_on desc ";
		
		Object[] params=new Object[]{redmineName,version,sprintStart+" 00:00:00",sprintEnd+" 23:59:59"};
		
		List rowsNew =this.jdbcTemplate.queryForList(sqlNew,params);
		List rowsUpdate = this.jdbcTemplate.queryForList(sqlUpdate,params);
		Map<Integer,String> idAndDate=new TreeMap<Integer,String>();
		Map<String,Long> dateAndCount=new TreeMap<String,Long>();
		
		for(Object o : rowsNew)
		{
			Map map = (Map) o;
			idAndDate.put((Integer)map.get("id"),(String)map.get("updatedate"));
		}
		
		for(Object o : rowsUpdate)
		{
			Map map = (Map) o;
			idAndDate.put((Integer)map.get("id"),(String)map.get("updatedate"));
		}
		
		Iterator iter=idAndDate.entrySet().iterator();
		
		while(iter.hasNext())
		{
			Map.Entry<Integer,String> entry=(Map.Entry<Integer,String>)iter.next();
			if(dateAndCount.containsKey(entry.getValue()))
			{
				Long count=dateAndCount.get(entry.getValue());
				dateAndCount.put(entry.getValue(), count+1);
			}
			else
			{
				dateAndCount.put(entry.getValue(), 1L);
			}
		}
		
		iter=dateAndCount.entrySet().iterator();
		while(iter.hasNext())
		{
			Map.Entry<String,Long> entry=(Map.Entry<String,Long>)iter.next();
			lstResult.add(entry.getKey() + "#" +entry.getValue().toString());
		}
		return lstResult;
	}
	
	public List<String> getDaysByRange(String strDateStart,String strDateEnd) throws ParseException
	{
		List<String> lstDate=new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date start = sdf.parse(strDateStart);
		Date end = sdf.parse(strDateEnd);
		double between=(end.getTime()-start.getTime())/1000;//除以1000是为了转换成秒
		double day=between/(24*3600);
		for(int i = 1;i<=day;i++)
		{
			Calendar cd = Calendar.getInstance();
			cd.setTime(sdf.parse(strDateStart));
			cd.add(Calendar.DATE, i);//增加一天 
			//cd.add(Calendar.MONTH, n);//增加一个月
			lstDate.add(sdf.format(cd.getTime()));
		}
		return lstDate;
	}
	
	
	public List<String> getScopeStability(String redmineName, String version, String sprintStart, String sprintEnd)
	{
		List<String> lstResult=new ArrayList<String>();
		List<String> lstDate=new ArrayList<String>();
		try
		{
			lstDate = getDaysByRange(sprintStart,sprintEnd);
		}
		catch(Exception e)
		{
			
		}
		
		String sqlNew = "select date_format(issues.created_on,'%Y-%m-%d') as updatedate,count(*) as number from issues "+
				"join projects on projects.id=issues.project_id "+
				"join versions on versions.id=issues.fixed_version_id "+
				"where projects.name=? and versions.name=? "+
				"and issues.created_on between ? and ? "+
				"and issues.tracker_id = 1 "+
				"group by issues.created_on "+
				"order by issues.created_on";
		
		String sqlUpdate = "select date_format(journals.created_on,'%Y-%m-%d') as updatedate,count(*) as number from issues "+
				"join journals on journals.journalized_id=issues.id " +
				"join journal_details on journal_details.journal_id=journals.id " +
				"join projects on projects.id=issues.project_id "+
				"join versions on versions.id=issues.fixed_version_id "+
				"where projects.name=? and versions.name=? "+
				"and issues.created_on between ? and ? "+
				"and journal_details.prop_key='fixed_version_id' "+
				"and issues.tracker_id = 2 "+
				"group by journals.created_on "+
				"order by journals.created_on";
		
		String sqlNewFeature = "select date_format(issues.created_on,'%Y-%m-%d') as updatedate,count(*) as number from issues "+
				"join projects on projects.id=issues.project_id "+
				"join versions on versions.id=issues.fixed_version_id "+
				"where projects.name=? and versions.name=? "+
				"and issues.created_on between ? and ? "+
				"and issues.tracker_id = 2 "+
				"group by issues.created_on "+
				"order by issues.created_on";
		
		String sqlUpdateFeature = "select date_format(journals.created_on,'%Y-%m-%d') as updatedate,count(*) as number from issues "+
				"join journals on journals.journalized_id=issues.id " +
				"join journal_details on journal_details.journal_id=journals.id " +
				"join projects on projects.id=issues.project_id "+
				"join versions on versions.id=issues.fixed_version_id "+
				"where projects.name=? and versions.name=? "+
				"and issues.created_on between ? and ? "+
				"and journal_details.prop_key='fixed_version_id' "+
				"and issues.tracker_id = 2 "+
				"group by journals.created_on "+
				"order by journals.created_on";
		
		Object[] params=new Object[]{redmineName,version,sprintStart+" 00:00:00",sprintEnd+" 23:59:59"};
		
		List rowsNew =this.jdbcTemplate.queryForList(sqlNew,params);
		List rowsUpdate = this.jdbcTemplate.queryForList(sqlUpdate,params);
		List rowsNewFeature=this.jdbcTemplate.queryForList(sqlNewFeature,params);
		List rowsUpdateFeature=this.jdbcTemplate.queryForList(sqlUpdateFeature,params);
		Map<Integer,String> idAndDate=new TreeMap<Integer,String>();
		Map<String,Long> dateAndCount=new TreeMap<String,Long>();
		Map<String,SeverityClass> mapResult=new TreeMap<String,SeverityClass>();
		for(String strDate:lstDate)
		{
			mapResult.put(strDate,new SeverityClass());
		}
		
		for(Object o : rowsNew)
		{
			Map map = (Map) o;
			String date=(String)map.get("updatedate");
			SeverityClass severity=mapResult.get(date);
			Long count = (Long)map.get("number");
			severity.setNewBugsCount(count);
			//mapResult.replace(date, severity);
			mapResult.put(date, severity);
		}
		
		for(Object o : rowsUpdate)
		{
			Map map = (Map) o;
			String date=(String)map.get("updatedate");
			SeverityClass severity=mapResult.get(date);
			Long count = (Long)map.get("number");
			severity.setUpdateBugsCount(count);
			//mapResult.replace(date, severity);
			mapResult.put(date, severity);
		}
		
		for(Object o : rowsNewFeature)
		{
			Map map = (Map) o;
			String date=(String)map.get("updatedate");
			SeverityClass severity=mapResult.get(date);
			Long count = (Long)map.get("number");
			severity.setNewFeaturesCount(count);
			//mapResult.replace(date, severity);
			mapResult.put(date, severity);
		}
		
		for(Object o : rowsUpdateFeature)
		{
			Map map = (Map) o;
			String date=(String)map.get("updatedate");
			SeverityClass severity=mapResult.get(date);
			Long count = (Long)map.get("number");
			severity.setUpdateFeaturesCount(count);
			//mapResult.replace(date, severity);
			mapResult.put(date, severity);
		}
		
		Iterator iter=mapResult.entrySet().iterator();
		while(iter.hasNext())
		{
			Map.Entry<String,SeverityClass> entry=(Map.Entry<String,SeverityClass>)iter.next();
			lstResult.add(entry.getKey()+"#"+entry.getValue().getNewBugsCount()+entry.getValue().getUpdateBugsCount()+"#"+entry.getValue().getNewFeaturesCount()+entry.getValue().getUpdateFeaturesCount());
		}
		return lstResult;
	}

	public List<String> getProjectInfoByVersion(String redmineName,String sprintStart,String sprintEnd)
	{
		List<String> lstResult=new ArrayList<String>();
		Map<String,String> mapResult=new TreeMap<String,String>();
		
		//TargetVerison
		String sqlImplementedFeatures="select versions.name as versionName ,count(*) as number "+
				"from issues "+
				"join projects on projects.id=issues.project_id "+
				"right join versions on versions.id=issues.fixed_version_id "+
				"where projects.name= ? and tracker_id=2 and issues.status_id in(4,5,10) "+
				"group by issues.fixed_version_id "+
				"order by versions.name ";
		
		//TargetVerison
		String sqlFixedBugs="select versions.name as versionName,count(*) as number "+
				"from issues "+
				"join projects on projects.id=issues.project_id "+
				"right join versions on versions.id=issues.fixed_version_id "+
				"where projects.name= ? and tracker_id=1 and issues.status_id in(4,5,10) "+
				"group by issues.fixed_version_id "+
				"order by versions.name ";
		
		//AffectedVersion
		String sqlNewBugs="select versions.name as versionName,count(*) as number "+
				"from issues "+
				"join projects on projects.id=issues.project_id "+
				"join custom_values on custom_values.customized_id=issues.id "+
				"join custom_fields on custom_fields.id=custom_values.custom_field_id "+
				"right join versions on custom_values.value=versions.id "+
				"where custom_fields.id=9 and issues.tracker_id=1 and issues.status_id <> 6 "+
				"and projects.name= ? and issues.created_on between ? and ? "+ 
				"group by versions.name "+
				"order by versions.name ";
	
		
		Object[] params=new Object[]{redmineName};
		List rowsImplementedFeature=this.jdbcTemplate.queryForList(sqlImplementedFeatures, params);
		List rowsFixedBug=this.jdbcTemplate.queryForList(sqlFixedBugs, params);
		Object[] newBugsParams=new Object[]{redmineName,sprintStart,sprintEnd};
		List rowsNewBug=this.jdbcTemplate.queryForList(sqlNewBugs,newBugsParams);

		for(Object o : rowsImplementedFeature)
		{
			Map map = (Map) o;
			String strNumber=((Long)map.get("number")).toString();
			String versionName=(String)map.get("versionName");
			versionName=versionName.toLowerCase();
			if(versionName.indexOf("v")==0)
			{
				mapResult.put(versionName,strNumber);
			}
		}
		
		for(Object o : rowsFixedBug)
		{
			Map map = (Map) o;
			String strNumber=((Long)map.get("number")).toString();
			String versionName=(String)map.get("versionName");
			versionName=versionName.toLowerCase();
			if(versionName.indexOf("v")==0)
			{
				if(!mapResult.containsKey(versionName))
				{
					mapResult.put(versionName,"0"+"#"+strNumber);
				}
				else
				{
					mapResult.put(versionName,mapResult.get(versionName)+"#"+strNumber);
				}
			}
		}
		
		for(Object o:rowsNewBug)
		{
			Map map=(Map) o;
			String strNumber=((Long)map.get("number")).toString();
			String versionName=(String)map.get("versionName");
			versionName=versionName.toLowerCase();
			if(versionName.indexOf("v")==0)
			{
				if(!mapResult.containsKey(versionName))
				{
					mapResult.put(versionName,"0"+"#0"+"#"+strNumber);
				}
				else
				{
					mapResult.put(versionName,mapResult.get(versionName)+"#"+strNumber);
				}
			}
		}
		Iterator iter=mapResult.entrySet().iterator();
		while(iter.hasNext())
		{
			Map.Entry<String,String> entry=(Map.Entry<String,String>)iter.next();
			lstResult.add(entry.getKey() + "#" + entry.getValue());
		}
		return lstResult;
	}
}

//zhangdi 140704 todo long类型只是为方便处理sql类型结果====此类后面重构====
class SeverityClass
{
	private long newBugsCount=0;
	private long updateBugsCount=0;
	private long newFeaturesCount=0;
	private long updateFeaturesCount=0;
	
	public SeverityClass()
	{
		
	}
	
	public long getNewBugsCount()
	{
		return this.newBugsCount;
	}
	public void setNewBugsCount(long count)
	{
		this.newBugsCount=count;
	}
	
	public long getUpdateBugsCount()
	{
		return updateBugsCount;
	}
	public void setUpdateBugsCount(long count)
	{
		this.updateBugsCount=count;
	}
	
	public long getNewFeaturesCount()
	{
		return this.newFeaturesCount;
	}
	public void setNewFeaturesCount(long count)
	{
		this.newFeaturesCount=count;
	}
	
	public long getUpdateFeaturesCount()
	{
		return this.updateFeaturesCount;
	}
	public void setUpdateFeaturesCount(long count)
	{
		this.updateFeaturesCount=count;
	}
}
