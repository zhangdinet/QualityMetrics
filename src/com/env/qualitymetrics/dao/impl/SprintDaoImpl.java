package com.env.qualitymetrics.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.env.qualitymetrics.common.SysUtil;
import com.env.qualitymetrics.dao.SprintDao;
import com.env.qualitymetrics.dto.Page;
import com.env.qualitymetrics.dto.SprintDto;
import com.env.qualitymetrics.entity.Ranking_sprint;
import com.env.qualitymetrics.entity.Ranking_sprint_score_item;
import com.env.qualitymetrics.entity.Ranking_sprint_score_item_pk;
import com.env.qualitymetrics.entity.Score_item;
import com.env.qualitymetrics.entity.Source;
import com.env.qualitymetrics.entity.Sprint;
import com.env.qualitymetrics.entity.Sprint_score_item;
import com.env.qualitymetrics.entity.Sprint_score_item_pk;
import com.env.qualitymetrics.entity.Sprint_source;
import com.env.qualitymetrics.entity.Sprint_source_pk;

public class SprintDaoImpl implements SprintDao{
	SessionFactory sessionFactory;
	private static final Logger log = LoggerFactory.getLogger(SprintDao.class); 
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<SprintDto> getSprintsByProjectId(Integer project_id) {
		String hql = "from Sprint s where s.project_id = ? order by s.sprint_startdate";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, project_id+"");
		List list = query.list();
		Iterator iterator = list.iterator();
		List<SprintDto> sprintDtoList = new ArrayList<SprintDto>();
		while(iterator.hasNext()){
			Sprint s = (Sprint)iterator.next();
			SprintDto sprintDto = getSprintById(s.getSprint_id());
			sprintDtoList.add(sprintDto);
		}
		return sprintDtoList;
	}
	
	//获取Sprint映射信息
	public SprintDto getSprintSourceNamesById(int sprint_id) {
		SprintDto sprintDto = new SprintDto();
		String hql = "select s.source_name, ss.sprint_source_name from Source s, Sprint_source ss " +
				"where ss.sprint_source_pk.sprint_id = ? and s.source_id = ss.sprint_source_pk.source_id " +
				"order by s.source_id";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, sprint_id+"");
		List list = query.list();
		if(list.size() == 0){
			return sprintDto;
		}
		Iterator iterator = list.iterator();
		Map<String,String> map = new HashMap<String,String>();
		while(iterator.hasNext()){
			Object[] o  = (Object[]) iterator.next();
			String source_name = (String) o[0];
			String sprint_source_name = (String) o[1];
			map.put(source_name, sprint_source_name);
		}
		sprintDto.setTestplan_testlink(map.get("Testlink"));
		sprintDto.setVersion_redmine(map.get("Redmine"));
		sprintDto.setBuild_sonar(map.get("Sonar"));
		return sprintDto;
	}

	@Override
	public SprintDto getSprintById(Integer sprint_id) {
		SprintDto sprintDto = new SprintDto();
		//根据sprint_id获取对应的Sprint，基本信息：名称，得分，开始时间，结束时间，等
		sprintDto = getSprintSourceNamesById(sprint_id);
		String hql = "from Sprint s where s.sprint_id=? order by s.sprint_startdate";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, sprint_id+"");
		List sprintList = query.list();
		Iterator iterator = sprintList.iterator();
		while(iterator.hasNext()){
			Sprint s = (Sprint)iterator.next();
			sprintDto.setSprint_id(s.getSprint_id());
			sprintDto.setProject_id(s.getProject_id());
			sprintDto.setSprint_name(s.getSprint_name());
			sprintDto.setSprint_startdate(s.getSprint_startdate());
			sprintDto.setSprint_enddate(s.getSprint_enddate());
			sprintDto.setSprint_score(s.getSprint_score());
			sprintDto.setSprint_score_five(SysUtil.convertSprintScore(s.getSprint_score()));
			//sprintDto.setSprint_builddate(s.getSprint_builddate());
			//===  todo zhangdi  140430=====
			sprintDto.setSprint_build(s.getSprint_build());
			//=========
			sprintDto.setUrl_surveymonkey(s.getUrl_surveymonkey());
		}
		//设置默认值
		sprintDto.setIpd_score(0);
		sprintDto.setLmt_score(0);
		sprintDto.setSonar_score(0);
		sprintDto.setTest_pass_score(0);
		sprintDto.setTc_exec_score(0);
		sprintDto.setBug_new_score(0);
		sprintDto.setBug_reopen_score(0);
		sprintDto.setBug_escape_score(0);
		sprintDto.setRate_patch_score(0);
		sprintDto.setRate_support_score(0);
		sprintDto.setRate_ce_score(0);
		
		sprintDto.setIpd_score_origin(-1);
		sprintDto.setLmt_score_origin(-1);
		sprintDto.setSonar_score_origin(-1);
		sprintDto.setTest_pass_score_origin(-1);
		sprintDto.setTc_exec_score_origin(-1);
		sprintDto.setBug_new_score_origin(-1);
		sprintDto.setBug_reopen_score_origin(-1);
		sprintDto.setBug_escape_score_origin(-1);
		sprintDto.setRate_patch_score_origin(-1);
		sprintDto.setRate_support_score_origin(-1);
		sprintDto.setRate_ce_score_origin(-1);
		
		//获取sprint_id对应的各评分项名称以及得分
		hql = "select si.score_item_name, s.score,s.score_origin from Sprint_score_item s, Score_item si, Sprint sprint " +
				"where s.sprint_score_item_pk.sprint_id = ? and s.sprint_score_item_pk.score_item_id = si.score_item_id" +
				" order by s.sprint_score_item_pk.score_item_id";
		query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, sprint_id+"");
		List list = query.list();
		if(list.size()==0){
			return sprintDto;
		}
		
		iterator = list.iterator();
		Map<String,Float[]> scoreMap = new HashMap<String,Float[]>();
		while(iterator.hasNext()){
			Object[] o = (Object[]) iterator.next();
			String score_item_name = (String)o[0];
			Float score = (Float)o[1];
			Float score_origin = (Float)o[2];
			scoreMap.put(score_item_name, new Float[]{score,score_origin});
		}
		if(scoreMap.containsKey("ipd")){
			sprintDto.setIpd_score(scoreMap.get("ipd")[0]);
			sprintDto.setIpd_score_origin(scoreMap.get("ipd")[1]);
		}
		if(scoreMap.containsKey("lmt")){
			sprintDto.setLmt_score(scoreMap.get("lmt")[0]);
			sprintDto.setLmt_score_origin(scoreMap.get("lmt")[1]);
		}
		if(scoreMap.containsKey("sonar")){
			sprintDto.setSonar_score(scoreMap.get("sonar")[0]);
			sprintDto.setSonar_score_origin(scoreMap.get("sonar")[1]);
		}
		if(scoreMap.containsKey("test_pass")){
			sprintDto.setTest_pass_score(scoreMap.get("test_pass")[0]);
			sprintDto.setTest_pass_score_origin(scoreMap.get("test_pass")[1]);
		}
		if(scoreMap.containsKey("tc_exec")){
			sprintDto.setTc_exec_score(scoreMap.get("tc_exec")[0]);
			sprintDto.setTc_exec_score_origin(scoreMap.get("tc_exec")[1]);
		}
		if(scoreMap.containsKey("bug_new")){
			sprintDto.setBug_new_score(scoreMap.get("bug_new")[0]);
			sprintDto.setBug_new_score_origin(scoreMap.get("bug_new")[1]);
		}
		if(scoreMap.containsKey("bug_reopen")){
			sprintDto.setBug_reopen_score(scoreMap.get("bug_reopen")[0]);
			sprintDto.setBug_reopen_score_origin(scoreMap.get("bug_reopen")[1]);
		}
		if(scoreMap.containsKey("bug_escape")){
			sprintDto.setBug_escape_score(scoreMap.get("bug_escape")[0]);
			sprintDto.setBug_escape_score_origin(scoreMap.get("bug_escape")[1]);
		}
		if(scoreMap.containsKey("rate_patch")){
			sprintDto.setRate_patch_score(scoreMap.get("rate_patch")[0]);
			sprintDto.setRate_patch_score_origin(scoreMap.get("rate_patch")[1]);
		}
		if(scoreMap.containsKey("rate_support")){
			sprintDto.setRate_support_score(scoreMap.get("rate_support")[0]);
			sprintDto.setRate_support_score_origin(scoreMap.get("rate_support")[1]);
		}
		if(scoreMap.containsKey("rate_ce")){
			sprintDto.setRate_ce_score(scoreMap.get("rate_ce")[0]);
			sprintDto.setRate_ce_score_origin(scoreMap.get("rate_ce")[1]);
		}
		
		
		return sprintDto;
	}
	
	@Override
	public SprintDto getSprintHistoryById(Integer sprint_id,Integer rank_id) {
		SprintDto sprintDto = new SprintDto();
		//根据sprint_id获取对应的Sprint，基本信息：名称，得分，开始时间，结束时间，等
		sprintDto = getSprintSourceNamesById(sprint_id);
		String hql = "from Sprint s where s.sprint_id=? order by s.sprint_startdate";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, sprint_id+"");
		List sprintList = query.list();
		Iterator iterator = sprintList.iterator();
		while(iterator.hasNext()){
			Sprint s = (Sprint)iterator.next();
			sprintDto.setSprint_id(s.getSprint_id());
			sprintDto.setProject_id(s.getProject_id());
			//sprintDto.setSprint_name(s.getSprint_name());
			sprintDto.setSprint_startdate(s.getSprint_startdate());
			sprintDto.setSprint_enddate(s.getSprint_enddate());
			//sprintDto.setSprint_score(s.getSprint_score());
			//sprintDto.setSprint_score_five(SysUtil.convertSprintScore(s.getSprint_score()));
			sprintDto.setSprint_builddate(s.getSprint_builddate());
			sprintDto.setUrl_surveymonkey(s.getUrl_surveymonkey());
		}
		//查询历史记录分数
		String hql_rank = "from Ranking_sprint rs where rs.ranking_sprint_pk.sprint_id=? and rs.ranking_sprint_pk.rank_id=?";
		query = sessionFactory.getCurrentSession().createQuery(hql_rank);
		query.setString(0, sprint_id+"");
		query.setString(1, rank_id+"");
		List rankSprintList = query.list();
		iterator = rankSprintList.iterator();
		while(iterator.hasNext()){
			Ranking_sprint ranking_sprint=(Ranking_sprint)iterator.next();
			sprintDto.setSprint_name(ranking_sprint.getSprint_name());
			sprintDto.setSprint_score(ranking_sprint.getSprint_score());
			sprintDto.setSprint_score_five(SysUtil.convertSprintScore(ranking_sprint.getSprint_score()));
		}
		//设置默认值
		sprintDto.setIpd_score(0);
		sprintDto.setLmt_score(0);
		sprintDto.setSonar_score(0);
		sprintDto.setTest_pass_score(0);
		sprintDto.setTc_exec_score(0);	
		sprintDto.setBug_new_score(0);
		sprintDto.setBug_reopen_score(0);
		sprintDto.setBug_escape_score(0);
		sprintDto.setRate_patch_score(0);
		sprintDto.setRate_support_score(0);
		sprintDto.setRate_ce_score(0);
		
		sprintDto.setIpd_score_origin(-1);
		sprintDto.setLmt_score_origin(-1);
		sprintDto.setSonar_score_origin(-1);
		sprintDto.setTest_pass_score_origin(-1);
		sprintDto.setTc_exec_score_origin(-1);	
		sprintDto.setBug_new_score_origin(-1);
		sprintDto.setBug_reopen_score_origin(-1);
		sprintDto.setBug_escape_score_origin(-1);
		sprintDto.setRate_patch_score_origin(-1);
		sprintDto.setRate_support_score_origin(-1);
		sprintDto.setRate_ce_score_origin(-1);
		
		//获取sprint_id对应的各历史评分项名称以及得分
		hql = "select si.score_item_name, rs.score,rs.score_origin from Ranking_sprint_score_item rs, Score_item si " +
				"where rs.ranking_sprint_score_item_pk.sprint_id = ? and rs.ranking_sprint_score_item_pk.score_item_id = si.score_item_id" +
				" and rs.ranking_sprint_score_item_pk.rank_id = ? order by rs.ranking_sprint_score_item_pk.score_item_id";
		query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, sprint_id+"");
		query.setString(1, rank_id+"");
		List list = query.list();
		if(list.size()==0){
			return sprintDto;
		}
		
		iterator = list.iterator();
		Map<String,Float[]> scoreMap = new HashMap<String,Float[]>();
		while(iterator.hasNext()){
			Object[] o = (Object[]) iterator.next();
			String score_item_name = (String)o[0];
			Float score = (Float)o[1];
			Float score_origin = (Float)o[2];
			scoreMap.put(score_item_name, new Float[]{score,score_origin});
		}
		if(scoreMap.containsKey("ipd")){
			sprintDto.setIpd_score(scoreMap.get("ipd")[0]);
			sprintDto.setIpd_score_origin(scoreMap.get("ipd")[1]);
		}
		if(scoreMap.containsKey("lmt")){
			sprintDto.setLmt_score(scoreMap.get("lmt")[0]);
			sprintDto.setLmt_score_origin(scoreMap.get("lmt")[1]);
		}
		if(scoreMap.containsKey("sonar")){
			sprintDto.setSonar_score(scoreMap.get("sonar")[0]);
			sprintDto.setSonar_score_origin(scoreMap.get("sonar")[1]);
		}
		if(scoreMap.containsKey("test_pass")){
			sprintDto.setTest_pass_score(scoreMap.get("test_pass")[0]);
			sprintDto.setTest_pass_score_origin(scoreMap.get("test_pass")[1]);
		}
		if(scoreMap.containsKey("tc_exec")){
			sprintDto.setTc_exec_score(scoreMap.get("tc_exec")[0]);
			sprintDto.setTc_exec_score_origin(scoreMap.get("tc_exec")[1]);
		}
		if(scoreMap.containsKey("bug_new")){
			sprintDto.setBug_new_score(scoreMap.get("bug_new")[0]);
			sprintDto.setBug_new_score_origin(scoreMap.get("bug_new")[1]);
		}
		if(scoreMap.containsKey("bug_reopen")){
			sprintDto.setBug_reopen_score(scoreMap.get("bug_reopen")[0]);
			sprintDto.setBug_reopen_score_origin(scoreMap.get("bug_reopen")[1]);
		}
		if(scoreMap.containsKey("bug_escape")){
			sprintDto.setBug_escape_score(scoreMap.get("bug_escape")[0]);
			sprintDto.setBug_escape_score_origin(scoreMap.get("bug_escape")[1]);
		}
		if(scoreMap.containsKey("rate_patch")){
			sprintDto.setRate_patch_score(scoreMap.get("rate_patch")[0]);
			sprintDto.setRate_patch_score_origin(scoreMap.get("rate_patch")[1]);
		}
		if(scoreMap.containsKey("rate_support")){
			sprintDto.setRate_support_score(scoreMap.get("rate_support")[0]);
			sprintDto.setRate_support_score_origin(scoreMap.get("rate_support")[1]);
		}
		if(scoreMap.containsKey("rate_ce")){
			sprintDto.setRate_ce_score(scoreMap.get("rate_ce")[0]);
			sprintDto.setRate_ce_score_origin(scoreMap.get("rate_ce")[1]);
		}
		
		
		return sprintDto;
	}
	
	@Override
	//更新Sprint相关信息，包括包括名称，映射名称，SurveryMonkey，Build时间，
	//开始时间，结束时间，Ipd,Lmt过程质量分数。
	public boolean updateSprintDetail(SprintDto sprintDto) {
		try{
			//String hql = "update Sprint s set s.sprint_name = ?, s.sprint_startdate = ?, s.sprint_enddate = ?, " +
			//"s.sprint_builddate = ? , s.url_surveymonkey = ? where s.sprint_id= ?";
			
			
			String hql = "update Sprint s set s.sprint_name = ?, s.sprint_startdate = ?, s.sprint_enddate = ?, " +
					"s.url_surveymonkey = ?, s.sprint_build = ?  where s.sprint_id= ?";
			
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setString(0, sprintDto.getSprint_name());
			query.setString(1, fmt.format(sprintDto.getSprint_startdate()));
			query.setString(2, fmt.format(sprintDto.getSprint_enddate()));
			//query.setString(3, fmt.format(sprintDto.getSprint_builddate()));
			//query.setString(4, sprintDto.getUrl_surveymonkey());
			//query.setString(5, sprintDto.getSprint_id()+"");		
			
			query.setString(3, sprintDto.getUrl_surveymonkey());
			query.setString(4, sprintDto.getSprint_build());
			query.setString(5, sprintDto.getSprint_id()+"");
			
			
			query.executeUpdate();
			
			if(!updateSprintSourceNames(sprintDto)){
				return false;
			}
			
			if(!updateSprintItemScores(sprintDto)){
				return false;
			}
			
		}catch(Exception e){
			return false;
		}
		
		
		return true;
	}
	
	//更细Spint得分项
	private boolean updateSprintItemScores(SprintDto sprintDto) {
		boolean ipd = updateSprintItemScore(sprintDto.getSprint_id(),sprintDto.getIpd_score(),"ipd");
		if(ipd){
			boolean lmt = updateSprintItemScore(sprintDto.getSprint_id(),sprintDto.getLmt_score(),"lmt");
			if(lmt){
				return true;
			}
		}
		
		return false;
	}
	
	
	private boolean updateSprintItemScore(int sprint_id, float item_score,
			String score_item_name) {
		try{
			String hql = "from Score_item si where si.score_item_name = '"+score_item_name+"'";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			List score_itemList = query.list();
			if(score_itemList.size() == 0){
				return false;
			}
			Iterator iterator = score_itemList.iterator();
			Score_item si = new Score_item();
			while(iterator.hasNext()){
				si = (Score_item) iterator.next();
			}
			
			hql = "from Sprint_score_item ssi where ssi.sprint_score_item_pk.sprint_id = ? and ssi.sprint_score_item_pk.score_item_id = ?";
			query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setString(0, sprint_id+"");
			query.setString(1, si.getScore_item_id()+"");
			List list = query.list();
			if(list.size() == 0){
				//没有记录则新插入一条
				Sprint_score_item ssi = new Sprint_score_item();
				Sprint_score_item_pk sprint_score_item_pk = new Sprint_score_item_pk();
				sprint_score_item_pk.setScore_item_id(si.getScore_item_id());
				sprint_score_item_pk.setSprint_id(sprint_id);
				ssi.setSprint_score_item_pk(sprint_score_item_pk);
				ssi.setScore(item_score);
				sessionFactory.getCurrentSession().save(ssi);
			}else{
				//有记录则更新
				hql = "update Sprint_score_item ssi set ssi.score = ? where ssi.sprint_score_item_pk.sprint_id = ? " +
						"and ssi.sprint_score_item_pk.score_item_id = ?";
				query = sessionFactory.getCurrentSession().createQuery(hql);
				query.setString(1, sprint_id+"");
				query.setString(2, si.getScore_item_id()+"");
				query.setString(0, item_score+"");
				query.executeUpdate();
			}
		}catch(Exception e){
			return false;
		}
		return true;
	}

	//更新Sprint映射信息
	private boolean updateSprintSourceNames(SprintDto sprintDto) {
		boolean testlink = updateSourceName(sprintDto.getSprint_id(),sprintDto.getTestplan_testlink(),"Testlink");
		if(testlink){
			boolean redmine = updateSourceName(sprintDto.getSprint_id(),sprintDto.getVersion_redmine(),"Redmine");
			if(redmine){
				//boolean sonar = updateSourceName(sprintDto.getSprint_id(),sprintDto.getBuild_sonar(),"Sonar");
				boolean sonar = updateSourceName(sprintDto.getSprint_id(),sprintDto.getSprint_build(),"Sonar");
				if(sonar){
					return true;
				}
			}
		}
		return false;
	}

	private boolean updateSourceName(int sprint_id, String sprint_source_name,
			String source_name) {
		try{
			String hql = "from Source s where s.source_name = '"+source_name+"'";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			List sourceList = query.list();
			if(sourceList.size() == 0){
				return false;
			}
			Iterator iterator = sourceList.iterator();
			Source s  = new Source();
			while(iterator.hasNext()){
				s = (Source) iterator.next();
			}
			hql = "from Sprint_source ss where ss.sprint_source_pk.source_id = ? and ss.sprint_source_pk.sprint_id=?";
			query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setString(0, s.getSource_id()+"");
			query.setString(1, sprint_id+"");
			List list = query.list();
			//如果没有就插入一条新纪录
			if(list.size()==0){
				Sprint_source ss= new Sprint_source();
				Sprint_source_pk sprint_source_pk = new Sprint_source_pk();
				sprint_source_pk.setSource_id(s.getSource_id());
				sprint_source_pk.setSprint_id(sprint_id);
				ss.setSprint_source_pk(sprint_source_pk);
				ss.setSprint_source_name(sprint_source_name);
				sessionFactory.getCurrentSession().save(ss);
			}else{
				hql = "update Sprint_source ss set ss.sprint_source_name= ?  "+
						"where ss.sprint_source_pk.source_id = ? and ss.sprint_source_pk.sprint_id=?";
				query = sessionFactory.getCurrentSession().createQuery(hql);
				query.setString(1, s.getSource_id()+"");
				query.setString(2, sprint_id+"");
				query.setString(0, sprint_source_name);
				query.executeUpdate();
			}
		}catch(Exception e){
			return false;
		}
		
		return true;
	}

	@Override
	public SprintDto createNewSprintByProjectId(Integer project_id,String sprint_name) {
		Sprint sprint = new Sprint();
		sprint.setProject_id(project_id);
		sprint.setSprint_score(-1f);
		if(sprint_name.equals("")){
			sprint.setSprint_name("sprint name");
		}else{
			sprint.setSprint_name(sprint_name);
		}
		sessionFactory.getCurrentSession().save(sprint);
		SprintDto sprintDto = new SprintDto();
		sprintDto.setSprint_id(sprint.getSprint_id());
		return sprintDto;
	}

	@Override
	public List<SprintDto> getUncompleteSprints() {
		List<SprintDto> sprintDtoList = new ArrayList<SprintDto>();
		String hql = "from Sprint s where s.sprint_score = -1";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		List resultList = query.list();
		Iterator iterator = resultList.iterator();
		while(iterator.hasNext()){
			Sprint s = (Sprint)iterator.next();
			SprintDto sprintDto = this.getSprintById(s.getSprint_id());
			sprintDtoList.add(sprintDto);
		}
		return sprintDtoList;
	}


	@Override
	public void updateScore_item_score(int sprint_id, float score,
			String score_item_name) {
		String hql = "from Score_item si where si.score_item_name = '"+score_item_name+"'";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		List list = query.list();
		if(list.size() == 0){
			log.error(score_item_name+" score_item_id 找不到！");
			return;
		}
		Score_item si = (Score_item) list.get(0);
		hql = "from Sprint_score_item ssi where ssi.sprint_score_item_pk.sprint_id=? and ssi.sprint_score_item_pk.score_item_id = ?";
		query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, sprint_id+"");
		query.setString(1, si.getScore_item_id()+"");
		List resultList = query.list();
		if(resultList.size() == 0){
			//没有记录，新建一条
			Sprint_score_item ssi = new Sprint_score_item();
			Sprint_score_item_pk sprint_score_item_pk = new Sprint_score_item_pk();
			sprint_score_item_pk.setSprint_id(sprint_id);
			sprint_score_item_pk.setScore_item_id(si.getScore_item_id());
			ssi.setSprint_score_item_pk(sprint_score_item_pk);
			ssi.setScore(score);
			sessionFactory.getCurrentSession().save(ssi);
		}else{
			hql = "update Sprint_score_item ssi " +
					"set ssi.score = ? where " +
					"ssi.sprint_score_item_pk.sprint_id=? and " +
					"ssi.sprint_score_item_pk.score_item_id = ?";
			query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setString(0, score+"");
			query.setString(1, sprint_id+"");
			query.setString(2, si.getScore_item_id()+"");
			query.executeUpdate();
		}
	}
	
	@Override
	public void updateHistoryScore_item_score(int sprint_id, float score,
			String score_item_name,int rank_id) {
		String hql = "from Score_item si where si.score_item_name = '"+score_item_name+"'";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		List list = query.list();
		if(list.size() == 0){
			log.error(score_item_name+" score_item_id 找不到！");
			return;
		}
		Score_item si = (Score_item) list.get(0);
		hql = "from Ranking_sprint_score_item rssi where rssi.ranking_sprint_score_item_pk.sprint_id=? and rssi.ranking_sprint_score_item_pk.score_item_id = ?"+
		" and rssi.ranking_sprint_score_item_pk.rank_id=?";
		query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, sprint_id+"");
		query.setString(1, si.getScore_item_id()+"");
		query.setString(2, rank_id+"");
		List resultList = query.list();
		if(resultList.size() == 0){
			//没有记录，新建一条
			Ranking_sprint_score_item rssi = new Ranking_sprint_score_item();
			Ranking_sprint_score_item_pk ranking_sprint_score_item_pk = new Ranking_sprint_score_item_pk();
			ranking_sprint_score_item_pk.setSprint_id(sprint_id);
			ranking_sprint_score_item_pk.setScore_item_id(si.getScore_item_id());
			ranking_sprint_score_item_pk.setRank_id(rank_id);
			rssi.setRanking_sprint_score_item_pk(ranking_sprint_score_item_pk);
			rssi.setScore(score);
			sessionFactory.getCurrentSession().save(rssi);
		}else{
			hql = "update Ranking_sprint_score_item rssi " +
					"set rssi.score = ? where " +
					"rssi.ranking_sprint_score_item_pk.sprint_id=? and " +
					"rssi.ranking_sprint_score_item_pk.score_item_id = ? and " +
					"rssi.ranking_sprint_score_item_pk.rank_id = ?";
			query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setString(0, score+"");
			query.setString(1, sprint_id+"");
			query.setString(2, si.getScore_item_id()+"");
			query.setString(3, rank_id+"");
			query.executeUpdate();
		}
	}

	@Override
	public void updateSprintScore(int sprint_id, float sprint_score) {
		// TODO Auto-generated method stub
		String hql = "update Sprint s set s.sprint_score = ? where s.sprint_id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, sprint_score+"");
		query.setString(1, sprint_id+"");
		query.executeUpdate();
	}
	
	@Override
	public void updateSprintHistoryScore(int sprint_id, float sprint_score,int rank_id) {
		// TODO Auto-generated method stub
		String hql = "update Ranking_sprint rs set rs.sprint_score = ? where rs.ranking_sprint_pk.sprint_id = ? " +
					"and rs.ranking_sprint_pk.rank_id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, sprint_score+"");
		query.setString(1, sprint_id+"");
		query.setString(2, rank_id+"");
		query.executeUpdate();
	}

	@Override
	public void updateScore_item_score_origin(int sprint_id, float score_origin, String score_item_name) {
		// TODO Auto-generated method stub
		String hql = "from Score_item si where si.score_item_name = '"+score_item_name+"'";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		List list = query.list();
		if(list.size() == 0){
			log.error(score_item_name+" score_item_id 找不到！");
			return;
		}
		Score_item si = (Score_item) list.get(0);
		hql = "from Sprint_score_item ssi where ssi.sprint_score_item_pk.sprint_id=? and ssi.sprint_score_item_pk.score_item_id = ?";
		query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, sprint_id+"");
		query.setString(1, si.getScore_item_id()+"");
		List resultList = query.list();
		if(resultList.size() == 0){
			//没有记录，新建一条
			Sprint_score_item ssi = new Sprint_score_item();
			Sprint_score_item_pk sprint_score_item_pk = new Sprint_score_item_pk();
			sprint_score_item_pk.setSprint_id(sprint_id);
			sprint_score_item_pk.setScore_item_id(si.getScore_item_id());
			ssi.setSprint_score_item_pk(sprint_score_item_pk);
			ssi.setScore_origin(score_origin);
			sessionFactory.getCurrentSession().save(ssi);
		}else{
			hql = "update Sprint_score_item ssi " +
					"set ssi.score_origin = ? where " +
					"ssi.sprint_score_item_pk.sprint_id=? and " +
					"ssi.sprint_score_item_pk.score_item_id = ?";
			query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setString(0, score_origin+"");
			query.setString(1, sprint_id+"");
			query.setString(2, si.getScore_item_id()+"");
			query.executeUpdate();
		}
	}

	@Override
	public void updateHistoryScore_item_score_origin(int sprint_id,
			float score_origin, String score_item_name,int rank_id) {
		// TODO Auto-generated method stub
		String hql = "from Score_item si where si.score_item_name = '"+score_item_name+"'";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		List list = query.list();
		if(list.size() == 0){
			log.error(score_item_name+" score_item_id 找不到！");
			return;
		}
		Score_item si = (Score_item) list.get(0);
		hql = "from Ranking_sprint_score_item rssi where rssi.ranking_sprint_score_item_pk.sprint_id=? and rssi.ranking_sprint_score_item_pk.score_item_id = ?" +
				" and rssi.ranking_sprint_score_item_pk.rank_id=?";
		query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, sprint_id+"");
		query.setString(1, si.getScore_item_id()+"");
		query.setString(2, rank_id+"");
		List resultList = query.list();
		if(resultList.size() == 0){
			//没有记录，新建一条
			Ranking_sprint_score_item rssi = new Ranking_sprint_score_item();
			Ranking_sprint_score_item_pk ranking_sprint_score_item_pk = new Ranking_sprint_score_item_pk();
			ranking_sprint_score_item_pk.setSprint_id(sprint_id);
			ranking_sprint_score_item_pk.setScore_item_id(si.getScore_item_id());
			ranking_sprint_score_item_pk.setRank_id(rank_id);
			rssi.setRanking_sprint_score_item_pk(ranking_sprint_score_item_pk);
			rssi.setScore_origin(score_origin);
			sessionFactory.getCurrentSession().save(rssi);
		}else{
			hql = "update Ranking_sprint_score_item rssi " +
					"set rssi.score_origin = ? where " +
					"rssi.ranking_sprint_score_item_pk.sprint_id=? and " +
					"rssi.ranking_sprint_score_item_pk.score_item_id = ? and " +
					"rssi.ranking_sprint_score_item_pk.rank_id = ?";
			query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setString(0, score_origin+"");
			query.setString(1, sprint_id+"");
			query.setString(2, si.getScore_item_id()+"");
			query.setString(3, rank_id+"");
			query.executeUpdate();
		}
	}
	@Override
	public List<SprintDto> getSprintsByProjectId(Integer project_id, Page page) {
		String hql = "from Sprint s where s.project_id = ? order by s.sprint_startdate";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, project_id+"");
		List totallist = query.list();
		page.setTotal(totallist.size());
		
		//分页查询
		query.setFirstResult((page.getCurrentPage()-1)*page.getPageSize());//定义从第几条开始查询  
        query.setMaxResults(page.getPageSize());//定义返回的记录数 
        List list = query.list();
		Iterator iterator = list.iterator();
		List<SprintDto> sprintDtoList = new ArrayList<SprintDto>();
		while(iterator.hasNext()){
			Sprint s = (Sprint)iterator.next();
			SprintDto sprintDto = getSprintById(s.getSprint_id());
			sprintDtoList.add(sprintDto);
		}
		return sprintDtoList;
	}

	@Override
	public List<SprintDto> getSprintsHistoryByProjectIdRankId(
			Integer project_id, Integer rank_id) {
		String hql = "select rs.ranking_sprint_pk.sprint_id,rs.sprint_name,rs.sprint_score from Ranking_sprint rs, Sprint s " +
		"where s.project_id= ? and s.sprint_id = rs.ranking_sprint_pk.sprint_id and rs.ranking_sprint_pk.rank_id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, project_id+"");
		query.setString(1, rank_id+"");
		List list = query.list();
		Iterator iterator = list.iterator();
		List<SprintDto> sprintDtoList = new ArrayList<SprintDto>();
		while(iterator.hasNext()){
			Object[] o = (Object[]) iterator.next();
			int sprint_id = (Integer)o[0];
			String sprint_name=(String)o[1];
			float sprint_score=(Float)o[2];
			SprintDto sprintDto = new SprintDto();
			sprintDto.setSprint_id(sprint_id);
			sprintDto.setSprint_name(sprint_name);
			sprintDto.setSprint_score(sprint_score);
			sprintDtoList.add(sprintDto);
		}
		return sprintDtoList;
	}
}
