package com.env.qualitymetrics.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.env.qualitymetrics.common.SysUtil;

//测试通过率 用例可执行率
public class TestLinkForSuiteHandler {
	private static final Logger log = LoggerFactory.getLogger(TestLinkForSuiteHandler.class);
	
	 private JdbcTemplate jdbcTemplate;

	    public void setdataSource_testlink(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource);
	    }
	    //get pass rate& exrate and put it into array
		public float[] getResults(String projectName, String suiteNames,String testplanNames){
			float passrate=-1;
			float exrate=-1;
			float result[]={-1f,-1f};
			int passCount=0;
			int blockCount=0;
			int totalCount=0;
			int projectId=this.getTestProjectIdByName(projectName);
			if(projectId==-1) return result;
			String nameArrays[] = testplanNames.split(SysUtil.splitFlag);
			for(int i=0;i<nameArrays.length;i++){
				String testplanName=nameArrays[i].trim();
				log.info("caculate pass rate "+projectName+", "+testplanName+" from TestLink start...");
				int testplanId=this.getTestplanIdByName(projectName, testplanName);
				if(testplanId==-1)
				{
					continue;
				}
				Map suiteId=this.getSuiteIdByName(projectName, suiteNames);
				totalCount+=this.getTotalCaseNumber(projectId, testplanId, suiteId);
				passCount+=this.getPassNumber(projectId, testplanId, suiteId);
				blockCount+=this.getBlockNumber(projectId, testplanId, suiteId);
				
			}
			if(totalCount==0){
				log.error("The total number of test cases is 0!");	
				return result;
			}
			
			log.info("There are "+passCount+" pass in "+totalCount+" cases");
			passrate=(float)passCount/totalCount;
			//结果放入数组中
			result[0]=passrate;
			log.info("pass rate is "+passrate);
			//通过率大于1时，返回1
			if(passrate>1) passrate=1;
			
			//block的数量大于总数时，返回总数
			if(blockCount>totalCount) blockCount=totalCount;
			exrate=1-(float)blockCount/totalCount;
			//结果放入数组中
			result[1]=exrate;
			log.info("excution rate is "+exrate);
			return result;
		}
		
	  //get test cases total number in a test plan
		public int getPassFromTotal(int testprojectId,int testplanId,Map<Integer,String> suiteId){
			int count=0;
			int passCount=0;
			int blockCount=0;
			String sql="select * from(select tc.id,tc.tc_external_id from tcversions tc "
				+"inner join testplan_tcversions tt on tt.tcversion_id=tc.id "
				+"inner join nodes_hierarchy nh on tt.testplan_id=nh.id where tt.testplan_id=? "
				+"group by tc.tc_external_id) tc1";
			List<Map<String,Object>> tcversion_ids=this.jdbcTemplate.queryForList(sql, new Object[]{testplanId});
			Iterator it=tcversion_ids.iterator();
			while(it.hasNext()){
				Map m=(Map)it.next();
				Long tcversion_Id=(Long)m.get("id");
				System.out.println(tcversion_Id);
				int node_type=4;
				Long node_id=tcversion_Id;
				Map node=null;
				while(node_type!=2||node_id!=testprojectId){
					String sql_findSuite="select * from nodes_hierarchy nh where nh.id=?";
					List<Map<String,Object>> nodeMap=this.jdbcTemplate.queryForList(sql_findSuite, new Object[]{node_id});
					if(nodeMap.size()==0) break;
					node=nodeMap.get(0);
					node_type=Integer.valueOf(node.get("node_type_id").toString());
					node_id=(Long)node.get("parent_id");
					//System.out.println(node.get("name"));
				}
				int id=Integer.valueOf(node.get("id").toString());
				String suiteName=suiteId.get(id);
				if(suiteName==null) continue;
				else{
					++count;
					Long external_id=(Long)m.get("tc_external_id");
					String sql_status="select e.`status` from executions e inner join tcversions tc on tc.id=e.tcversion_id " 
						+"inner join testplan_tcversions tt on tt.tcversion_id=tc.id "
						+"where e.testplan_id=?  and tc.tc_external_id=? order by e.execution_ts desc limit 1";
					String status=this.jdbcTemplate.queryForObject(sql_status, new Object[]{testplanId,external_id},String.class);
					if(status.equals("p"))
						++passCount;
					if(status.equals("b"))
						++blockCount;
				}
			}
			System.out.println("There are "+count+" test cases in the testplan.");
			System.out.println("There are "+passCount+" pass test cases in the testplan.");
			System.out.println("There are "+blockCount+" block test cases in the testplan.");
			return count;
		}
	    /***
	     * 测试通过率
	     * @param projectName	TestLink项目名
	     * @param testplanName	TestLink中Test Plan名
	     * @return
	     */
	public float getPassRate(String projectName, String suiteNames,String testplanNames){
		float passrate=-1;
		int passCount=0;
		int totalCount=0;
		int projectId=this.getTestProjectIdByName(projectName);
		if(projectId==-1) return projectId;
		String nameArrays[] = testplanNames.split(SysUtil.splitFlag);
		for(int i=0;i<nameArrays.length;i++){
			String testplanName=nameArrays[i].trim();
			log.info("caculate pass rate "+projectName+", "+testplanName+" from TestLink start...");
			int testplanId=this.getTestplanIdByName(projectName, testplanName);
			if(testplanId==-1) continue;
			Map suiteId=this.getSuiteIdByName(projectName, suiteNames);
			passCount+=this.getPassNumber(projectId, testplanId, suiteId);
			totalCount+=this.getTotalCaseNumber(projectId, testplanId, suiteId);
			
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
	public int getPassNumber(int testprojectId,int testplanId,Map<Integer,String> suiteId){
		int count=0;
		String sql="select * from( select e1.tcversion_id,e1.`status` from ( "
			+"select * from executions order by execution_ts desc) e1 "
			+"inner join tcversions tc on tc.id=e1.tcversion_id "
			+"where e1.testplan_id=? group by tc.tc_external_id) e2 where e2.`status`='p'";
		List<Map<String,Object>> tcversion_ids=this.jdbcTemplate.queryForList(sql, new Object[]{testplanId});
		Iterator it=tcversion_ids.iterator();
		while(it.hasNext()){
			
			Map m=(Map)it.next();
			Long tcversion_Id=(Long)m.get("tcversion_id");
			System.out.println(tcversion_Id);
			int node_type=4;
			Long node_id=tcversion_Id;
			Map node=null;
			while(node_type!=2||node_id!=testprojectId){
				String sql_findSuite="select * from nodes_hierarchy nh where nh.id=?";
				List<Map<String,Object>> nodeMap=this.jdbcTemplate.queryForList(sql_findSuite, new Object[]{node_id});
				if(nodeMap.size()==0) break;
				node=nodeMap.get(0);
				System.out.println(node.get("name"));
				int id=Integer.valueOf(node.get("id").toString());
				String suiteName=suiteId.get(id);
				//判断当前suite Id是否在suite name list中存在
				if(suiteName==null){
					//不存在，继续递归查找
					node_type=Integer.valueOf(node.get("node_type_id").toString());
					node_id=(Long)node.get("parent_id");
				}else{
					++count;
					break;
				}	
			}
			/*int id=Integer.valueOf(node.get("id").toString());
			String suiteName=suiteId.get(id);
			if(suiteName==null) continue;
			else ++count;*/
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
	public float getExcutionRate(String projectName,String suiteNames, String testplanNames){
		float exrate=-1;
		int blockCount=0;
		int totalCount=0;
		int projectId=this.getTestProjectIdByName(projectName);
		if(projectId==-1) return projectId;
		String nameArrays[] = testplanNames.split(SysUtil.splitFlag);
		for(int i=0;i<nameArrays.length;i++){
			String testplanName=nameArrays[i].trim();
			log.info("caculate excution rate "+projectName+", "+testplanName+" from TestLink start...");
			int testplanId=this.getTestplanIdByName(projectName, testplanName);
			if(testplanId==-1) continue;
			Map suiteId=this.getSuiteIdByName(projectName, suiteNames);
			blockCount+=this.getBlockNumber(projectId, testplanId, suiteId);
			totalCount+=this.getTotalCaseNumber(projectId, testplanId, suiteId);
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
	
	public int getBlockNumber(int testprojectId,int testplanId,Map<Integer,String> suiteId){
		int count=0;
		String sql="select * from( select e1.tcversion_id,e1.`status` from ( "
			+"select * from executions order by execution_ts desc) e1 "
			+"inner join tcversions tc on tc.id=e1.tcversion_id "
			+"where e1.testplan_id=? group by tc.tc_external_id) e2 where e2.`status`='b'";
		List<Map<String,Object>> tcversion_ids=this.jdbcTemplate.queryForList(sql, new Object[]{testplanId});
		Iterator it=tcversion_ids.iterator();
		while(it.hasNext()){
			
			Map m=(Map)it.next();
			Long tcversion_Id=(Long)m.get("tcversion_id");
			System.out.println(tcversion_Id);
			int node_type=4;
			Long node_id=tcversion_Id;
			Map node=null;
			while(node_type!=2||node_id!=testprojectId){
				String sql_findSuite="select * from nodes_hierarchy nh where nh.id=?";
				List<Map<String,Object>> nodeMap=this.jdbcTemplate.queryForList(sql_findSuite, new Object[]{node_id});
				if(nodeMap.size()==0) break;
				node=nodeMap.get(0);
				System.out.println(node.get("name"));
				int id=Integer.valueOf(node.get("id").toString());
				String suiteName=suiteId.get(id);
				//判断当前suite Id是否在suite name list中存在
				if(suiteName==null){
					//不存在，继续递归查找
					node_type=Integer.valueOf(node.get("node_type_id").toString());
					node_id=(Long)node.get("parent_id");
				}else{
					++count;
					break;
				}	
			}
		}
		log.info("There are "+count+" block test cases");

		return count;
	}
	//get test cases total number in a test plan
	public int getTotalCaseNumber(int testprojectId,int testplanId,Map<Integer,String> suiteId){
		int count=0;
		String sql="select * from(select tc.id,tc.tc_external_id from tcversions tc "
			+"inner join testplan_tcversions tt on tt.tcversion_id=tc.id "
			+"inner join nodes_hierarchy nh on tt.testplan_id=nh.id where tt.testplan_id=? "
			+"group by tc.tc_external_id) tc1";
		List<Map<String,Object>> tcversion_ids=this.jdbcTemplate.queryForList(sql, new Object[]{testplanId});
		Iterator it=tcversion_ids.iterator();
		while(it.hasNext()){
			Map m=(Map)it.next();
			Long tcversion_Id=(Long)m.get("id");
			System.out.println(tcversion_Id);
			int node_type=4;
			Long node_id=tcversion_Id;
			Map node=null;
			while(node_type!=2||node_id!=testprojectId){
				String sql_findSuite="select * from nodes_hierarchy nh where nh.id=?";
				List<Map<String,Object>> nodeMap=this.jdbcTemplate.queryForList(sql_findSuite, new Object[]{node_id});
				if(nodeMap.size()==0) break;
				node=nodeMap.get(0);
				System.out.println(node.get("name"));
				int id=Integer.valueOf(node.get("id").toString());
				String suiteName=suiteId.get(id);
				//判断当前suite Id是否在suite name list中存在
				if(suiteName==null){
					//不存在，继续递归查找
					node_type=Integer.valueOf(node.get("node_type_id").toString());
					node_id=(Long)node.get("parent_id");
				}else{
					++count;
					break;
				}	
			}
			
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
	//get top suite id by suite names
	public Map getSuiteIdByName(String projectName, String suiteNames){
		Map<Integer,String> suiteMap=new HashMap<Integer,String>();
		String nameArrays[] = suiteNames.split(SysUtil.splitFlag);
		for(int i=0;i<nameArrays.length;i++){
			String suiteName=nameArrays[i].trim();
			System.out.println(suiteName);
			//判断该suite name是否包含二级目录
			if(suiteName.contains(SysUtil.splitSuite)){
				String nameArrays2[]=suiteName.split(SysUtil.splitSuite);
				//查询该second suite
				String sql2_check="select count(0) from nodes_hierarchy nh2 where nh2.node_type_id=2 and " +
						"nh2.name=? and nh2.parent_id in(select id from nodes_hierarchy nh "+
						"where nh.node_type_id=2 and nh.name=? and parent_id=(select id from nodes_hierarchy nh2 "+
						"where nh2.name=? and nh2.node_type_id=1))";
				String sql2="select nh2.id from nodes_hierarchy nh2 where nh2.node_type_id=2 and " +
					"nh2.name=? and nh2.parent_id in(select id from nodes_hierarchy nh "+
					"where nh.node_type_id=2 and nh.name=? and parent_id=(select id from nodes_hierarchy nh2 "+
					"where nh2.name=? and nh2.node_type_id=1))";
				int exist2=this.jdbcTemplate.queryForObject(sql2_check,new Object[]{nameArrays2[1].trim(),nameArrays2[0].trim(),projectName}, Integer.class);
				if(exist2==1){
					int suiteId=this.jdbcTemplate.queryForObject(sql2,new Object[]{nameArrays2[1].trim(),nameArrays2[0].trim(),projectName}, Integer.class);
					suiteMap.put(suiteId, nameArrays2[1].trim());
				}else{
					log.info("There are "+exist2+"test suite "+nameArrays2[0]+" in TestLink!" );
					continue;
				}
			}else{
				//查询top suite
				String sql_check="select count(0) from nodes_hierarchy nh "
					+"where nh.name=? and nh.node_type_id=2 "
					+"and parent_id=(select id from nodes_hierarchy nh2 "
					+"where nh2.name=? and nh2.node_type_id=1)";
				String sql="select id from nodes_hierarchy nh "
					+"where nh.name=? and nh.node_type_id=2 "
					+"and parent_id=(select id from nodes_hierarchy nh2 "
					+"where nh2.name=? and nh2.node_type_id=1)";
				//String sql_2="select nh.name from nodes_hierarchy nh where nh.id=50 and nh.node_type_id=2 and nh.name='菜单栏'";
				//String e=this.jdbcTemplate.queryForObject(sql_2,String.class);
				//System.out.println(e);
				//先判断testplan是否存在
				int exist=this.jdbcTemplate.queryForObject(sql_check,new Object[]{suiteName,projectName}, Integer.class);
				if(exist==1){
					int suiteId=this.jdbcTemplate.queryForObject(sql,new Object[]{suiteName,projectName}, Integer.class);
					suiteMap.put(suiteId, suiteName);
				}else{
					log.info("There are "+exist+"test suite "+suiteName+" in TestLink!" );
					continue;
				}
			}
		}
		return suiteMap;
	}
	//get top and second suite names by project name
	public List<String> getSuiteNamesByTestprojectName(String projectName){
		final List<String> lstName=new ArrayList<String>();
		String sql="select nh.id,nh.name from nodes_hierarchy nh "
				+"where nh.node_type_id=2 "
				+"and parent_id=(select id from nodes_hierarchy nh2 "
				+"where nh2.name=? and nh2.node_type_id=1)";
		List rows=this.jdbcTemplate.queryForList(sql,new Object[]{projectName});
		if(rows.size()==0){
			log.error("There are no suites in test project "+projectName);
			return null;
		}
		Iterator iterator = rows.iterator();
	 	 
		for(Object o : rows){
		     Map mapName = (Map) o;
		     Long topSuiteId=(Long)mapName.get("id");
		     String topSuiteName=(String)mapName.get("name");
		     //add top suite name to list
		     lstName.add(topSuiteName);
		     //查询二级suite
		     String sql_secondSuite="select nh2.name from nodes_hierarchy nh2 "
		    	 	+"where nh2.node_type_id=2 and nh2.parent_id = ?";
		     List rows2=this.jdbcTemplate.queryForList(sql_secondSuite,new Object[]{topSuiteId});
		     if(rows2.size()==0){
					log.error("There are no second suites under top suite "+topSuiteName);
					continue;
				}
		     Iterator iterator2 = rows2.iterator();
		     for(Object o2:rows2){
		    	 Map secondSuite = (Map) o2;
		    	 String secondSuiteName=(String)secondSuite.get("name");
		    	 //add second suite name to list
		    	 String suiteJoint=topSuiteName+"--"+secondSuiteName;
		    	 lstName.add(suiteJoint);
		     }
		     
		}		 
		return lstName;
	}
}
