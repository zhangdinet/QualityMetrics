package com.env.qualitymetrics.tool;

import javax.sql.DataSource;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

//测试通过率 用例可执行率
public class TestLinkHandler_old {
	private static final Logger log = LoggerFactory.getLogger(TestLinkHandler_old.class);
	
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
	public int getPassScore(String projectName, String testplanName){
		int passscore=-1;
		float passrate=this.getPassRate(projectName, testplanName);
		if(passrate==-1) return passscore;
		passscore=rateToScore(passrate);
		log.info("pass score is "+passscore);
		return passscore;

	}
	public float getPassRate(String projectName, String testplanName){
		float passrate=-1;
		log.info("caculate pass score "+projectName+", "+testplanName+" from TestLink start...");
		int id=this.getTestplanIdByName(projectName, testplanName);
		if(id==-1) return passrate;
		int total=this.getTotalCaseNumber(id);
		if(total==0){
			log.error("The total number of test cases is 0!");	
			return passrate;
		}
		//统计testplan中pass的case数量
		String sql="select count(0) from( select e1.`status` from ( "
			+"select * from executions order by execution_ts desc) e1 "
			+"inner join tcversions tc on tc.id=e1.tcversion_id inner join testplan_tcversions tt on tt.tcversion_id=tc.id "
			+"where e1.testplan_id=? and tt.testplan_id=e1.testplan_id "
			+"group by tc.tc_external_id) e2 where e2.`status`='p'";
		int count=this.jdbcTemplate.queryForObject(sql,new Object[]{id}, Integer.class);
		log.info("There are "+count+" pass test cases");
		passrate=(float)count/total;
		log.info("pass rate is "+passrate);
		return passrate;
	}
	/***
	 * 用例可执行率
	 * @param projectName	TestLink项目名
	 * @param testplanName	TestLink中Test Plan名
	 * @return
	 */
	//return excution score
	public int getExcutionScore(String projectName, String testplanName){
		int exscore=-1;
		float exrate=this.getExcutionRate(projectName, testplanName);
		if(exrate==-1) return exscore;
		exscore=rateToScore(exrate);
		log.info("excution score is "+exscore);
		return exscore;

	}
	public float getExcutionRate(String projectName, String testplanName){
		float exrate=-1;
		log.info("caculate excution score "+projectName+", "+testplanName+" from TestLink start...");
		int id=this.getTestplanIdByName(projectName, testplanName);
		if(id==-1) return exrate;
		int total=this.getTotalCaseNumber(id);
		if(total==0){
			log.error("The total number of test cases is 0!");	
			return exrate;
		}
		//统计testplan中block的case数量
		String sql="select count(0) from( select e1.`status` from ( "
			+"select * from executions order by execution_ts desc) e1 "
			+"inner join tcversions tc on tc.id=e1.tcversion_id inner join testplan_tcversions tt on tt.tcversion_id=tc.id "
			+"where e1.testplan_id=? and tt.testplan_id=e1.testplan_id "
			+"group by tc.tc_external_id) e2 where e2.`status`='b'";
		int count=this.jdbcTemplate.queryForObject(sql,new Object[]{id}, Integer.class);
		log.info("There are "+count+" block test cases");
		exrate=1-(float)count/total;
		log.info("excution rate is "+exrate);
		return exrate;
	}
	//get test cases total number in a test plan
	public int getTotalCaseNumber(int testplanId){
		String sql="select count(0) from(select tc.tc_external_id from tcversions tc "
			+"inner join testplan_tcversions tt on tt.tcversion_id=tc.id left join user_assignments ua on ua.feature_id=tt.id "
			+"inner join nodes_hierarchy nh on tt.testplan_id=nh.id where tt.testplan_id=? "
			+"group by tc.tc_external_id) tc1";
		int count=this.jdbcTemplate.queryForObject(sql,new Object[]{testplanId}, Integer.class);
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
	/***
	 * 获取Test Project Id
	 * @param projectName	testlink项目名
	 * @return
	 */
	public int getTestProjectIdByName(String projectName){
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
}
