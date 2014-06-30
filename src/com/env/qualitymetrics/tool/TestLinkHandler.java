package com.env.qualitymetrics.tool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.env.qualitymetrics.common.SysUtil;


//测试通过率 用例可执行率
public class TestLinkHandler {
	private static final Logger log = LoggerFactory.getLogger(TestLinkHandler.class);
	
	 private JdbcTemplate jdbcTemplate;

	    public void setdataSource_testlink(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource);
	    }
		
	    /***
	     * 测试通过率
	     * @param projectName	TestLink项目名
	     * @param testplanName	TestLink中Test Plan名
	     * @return
	     */
	public float getPassRate(String projectName, String testplanNames){
		float passrate=-1;
		int passCount=0;
		int totalCount=0;
		String nameArrays[] = testplanNames.split(SysUtil.splitFlag);
		for(int i=0;i<nameArrays.length;i++){
			String testplanName=nameArrays[i].trim();
			log.info("caculate pass rate "+projectName+", "+testplanName+" from TestLink start...");
			int id=this.getTestplanIdByName(projectName, testplanName);
			if(id==-1) continue;
			List<Long> builds=this.getBuildIdByTestplanId(id);
			passCount+=this.getPassNumber(builds);
			totalCount+=this.getTotalCaseNumber(builds);
			
		}
		if(totalCount==0){
			log.error("The total number of test cases is 0!");	
			return passrate;
		}
		
		log.info("There are "+passCount+" pass in "+totalCount+" cases");
		passrate=(float)passCount/totalCount;
		
		log.info("pass rate is "+passrate);
		//通过率大于1时，返回1
		if(passrate>1) passrate=1;
		return passrate;
	}
	public int getPassNumber(List<Long> builds){
		int count=0;
		
		for(int i=0;i<builds.size();i++){
			Long buildId=builds.get(i);
			//System.out.println(buildId);
			//统计testplan中pass的case数量
			String sql="select count(0) from( select e1.`status` from ( "
				+"select * from executions order by execution_ts desc) e1 "
				+"inner join tcversions tc on tc.id=e1.tcversion_id inner join testplan_tcversions tt on tt.tcversion_id=tc.id "
				+"where e1.build_id=? and tt.testplan_id=e1.testplan_id "
				+"group by tc.tc_external_id) e2 where e2.`status`='p'";
			count+=this.jdbcTemplate.queryForObject(sql,new Object[]{buildId}, Integer.class);
		}
		
		log.info("There are "+count+" pass test cases");
		return count;
	}
	/***
	 * 用例可执行率
	 * @param projectName	TestLink项目名
	 * @param testplanName	TestLink中Test Plan名
	 * @return
	 */
	public float getExcutionRate(String projectName, String testplanNames){
		float exrate=-1;
		int blockCount=0;
		int totalCount=0;
		String nameArrays[] = testplanNames.split(SysUtil.splitFlag);
		for(int i=0;i<nameArrays.length;i++){
			String testplanName=nameArrays[i].trim();
			log.info("caculate excution rate "+projectName+", "+testplanName+" from TestLink start...");
			int id=this.getTestplanIdByName(projectName, testplanName);
			if(id==-1) continue;
			List<Long> builds=this.getBuildIdByTestplanId(id);
			blockCount+=this.getBlockNumber(builds);
			totalCount+=this.getTotalCaseNumber(builds);
		}
		if(totalCount==0){
			log.error("The total number of test cases is 0!");	
			return exrate;
		}
		log.info("There are "+blockCount+" block test cases");
		//block的数量大于总数时，返回总数
		if(blockCount>totalCount) blockCount=totalCount;
		exrate=1-(float)blockCount/totalCount;
		log.info("excution rate is "+exrate);

		return exrate;
	}
	
	public float getBlockNumber(List<Long> builds){

		int count=0;
		for(int i=0;i<builds.size();i++){
			Long buildId=builds.get(i);
			//统计testplan中block的case数量
			String sql="select count(0) from( select e1.`status` from ( "
				+"select * from executions order by execution_ts desc) e1 "
				+"inner join tcversions tc on tc.id=e1.tcversion_id inner join testplan_tcversions tt on tt.tcversion_id=tc.id "
				+"where e1.build_id=? and tt.testplan_id=e1.testplan_id "
				+"group by tc.tc_external_id) e2 where e2.`status`='b'";
			count+=this.jdbcTemplate.queryForObject(sql,new Object[]{buildId}, Integer.class);
		}

		log.info("There are "+count+" block test cases");

		return count;
	}
	//get test cases total number in a test plan
	public int getTotalCaseNumber(List<Long> builds){
		int count=0;
		if(builds==null) return count;
		for(int i=0;i<builds.size();i++){
			Long buildId=builds.get(i);
			String sql="select count(0) from(select tc.tc_external_id from tcversions tc "
				+"inner join testplan_tcversions tt on tt.tcversion_id=tc.id inner join user_assignments ua on ua.feature_id=tt.id "
				+"inner join nodes_hierarchy nh on tt.testplan_id=nh.id where ua.build_id=? "
				+"group by tc.tc_external_id) tc1";
			count+=this.jdbcTemplate.queryForObject(sql,new Object[]{buildId}, Integer.class);
		}
		log.info("There are "+count+" test cases in the testplan.");
		return count;
	}
	// transfer rate to score standard
	public int rateToScore(float rate){
		int score=-1;
		if(rate>=0.993) score=5;
		if(rate>=0.93&&rate<0.993) score=4;
		if(rate>=0.85&&rate<0.93) score=3;
		if(rate>=0.8&&rate<0.85) score=2;
		if(rate>=0.72&&rate<0.8) score=1;
		if(rate<0.72) score=0;
		return score;
		}
	/***
	 * 获取Test Plan Id
	 * @param projectName	testlink项目名
	 * @param testplanName	testlink中test plan名
	 * @return
	 */
	//get test plan by testplan_name and testproject_name
	public int getTestplanIdByName(String projectName, String testplanName){
		log.info("get test plan of "+projectName+", "+testplanName+" from TestLink");
		int testplanId=-1;
		String sql3="select count(0) from nodes_hierarchy nh "
			+"where nh.name=? and nh.node_type_id=5 "
			+"and parent_id=(select id from nodes_hierarchy nh2 "
			+"where nh2.name=? and nh2.node_type_id=1)";
		String sql4="select id from nodes_hierarchy nh "
			+"where nh.name=? and nh.node_type_id=5 "
			+"and parent_id=(select id from nodes_hierarchy nh2 "
			+"where nh2.name=? and nh2.node_type_id=1)";
		//先判断testplan是否存在
		int exist=this.jdbcTemplate.queryForObject(sql3,new Object[]{testplanName,projectName}, Integer.class);
		if (exist==1)
			testplanId=this.jdbcTemplate.queryForObject(sql4,new Object[]{testplanName,projectName}, Integer.class);
		else
			log.info("There are "+exist+" project "+projectName+" Testplan "+testplanName+" in TestLink!" );
		return testplanId;
	}
	
	//===================140418 zhangdi==============
		public List<String> getProjectTestPlan(String projectName)
		{
			final List<String> lstName=new ArrayList<String>();
			int id=-1;	
			String sqlID="select id from nodes_hierarchy where name='"+projectName+"'";
			id=jdbcTemplate.queryForInt(sqlID);	
			String sqlList="select name from nodes_hierarchy where parent_id = "+id + " and node_type_id = 5";	
			List rows=this.jdbcTemplate.queryForList(sqlList);
			Iterator iterator = rows.iterator();
			 	 
			for(Object o : rows){
			     Map mapName = (Map) o;
			     lstName.add((String)mapName.get("name"));
			}		 
			return lstName;
		}		
		//===================140416 zhangdi==============
	/***
	 * 获取Test Project Id
	 * @param projectName	testlink项目名
	 * @return
	 */
	public int getTestProjectIdByName(String projectName){  //考虑一次查询  zhangdi  140425
		int testProjectId=-1;
		log.info("get test project "+projectName+" from TestLink");
		String sql3="select count(0) from nodes_hierarchy nh2 "
			+"where nh2.name=? and nh2.node_type_id=1";
		String sql4="select id from nodes_hierarchy nh2 "
			+"where nh2.name=? and nh2.node_type_id=1";
		//先判断testplan是否存在
		int exist=this.jdbcTemplate.queryForObject(sql3,new Object[]{projectName}, Integer.class);
		if (exist==1)
			testProjectId=this.jdbcTemplate.queryForObject(sql4,new Object[]{projectName}, Integer.class);
		else
			log.info("There are "+exist+" project "+projectName+" in TestLink!" );
		return testProjectId;		
	}
	
	//===================140416 zhangdi==============
 
	/*	public List<String> getTestlinkProjectNames()
	{
		final List<String> lstName=new ArrayList<String>();
		String sql="select name from nodes_hierarchy where node_type_id = 1";
	
		this.jdbcTemplate.query(sql, new Object[]{}, new RowCallbackHandler(){
			public void processRow(ResultSet rs) throws SQLException{
				String name=rs.getString("name");
				lstName.add(name);
			}});
			
		return testProjectId;		
	}*/

	public List<String> getTestlinkProjectNames()
	{
		final List<String> lstName=new ArrayList<String>();
		String sql="select name from nodes_hierarchy where node_type_id = 1 order by name";
		
		 List rows=this.jdbcTemplate.queryForList(sql);
		 Iterator iterator = rows.iterator();
		 	 
		 for(Object o : rows){
		      Map mapName = (Map) o;
		      lstName.add((String)mapName.get("name"));
		 }		 
		 return lstName;
	} 
	
	//===============================================
	
	
	//get build ID by test plan ID
	public List<Long> getBuildIdByTestplanId(int testplanId){
		List<Long> builds=new ArrayList();
		String sql_count="select count(0) from builds b where b.testplan_id=?";
		String sql="select id from builds b where b.testplan_id=?";
		int count=this.jdbcTemplate.queryForObject(sql_count, new Object[]{testplanId}, Integer.class);
		if(count==0){
			log.error("There are no build in this test plan!");
			return null;
		}
		List<Map<String,Object>> maps=this.jdbcTemplate.queryForList(sql, new Object[]{testplanId});
		Iterator it=maps.iterator();
		while(it.hasNext()){
			
			Map m=(Map)it.next();
			Long buildId=(Long)m.get("id");
			builds.add(buildId);
		}
		return builds;
	}
}
