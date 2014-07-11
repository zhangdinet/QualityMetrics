package com.env.qualitymetrics.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.env.qualitymetrics.common.SysUtil;
import com.env.qualitymetrics.dao.ProjectDao;
import com.env.qualitymetrics.dto.ProjectDto;
import com.env.qualitymetrics.entity.Project;
import com.env.qualitymetrics.entity.Project_ratepatch_history;
import com.env.qualitymetrics.entity.Project_ratepatch_history_pk;
import com.env.qualitymetrics.entity.Project_source;
import com.env.qualitymetrics.entity.Project_source_pk;
import com.env.qualitymetrics.entity.Source;
import com.env.qualitymetrics.entity.Sprint;
import com.env.qualitymetrics.entity.Year;
import com.env.qualitymetrics.tasks.DailyTask;

public class ProjectDaoImpl implements ProjectDao{
	SessionFactory sessionFactory;
	private static final Logger log = LoggerFactory.getLogger(ProjectDaoImpl.class); 
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	@Override
	public List<ProjectDto> getNewestRankList() {
		String hql = "from Project p order by p.avg_score desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql); 
		List<ProjectDto> projectList = new ArrayList<ProjectDto>();
		List resultList = query.list();
		Iterator iterator = resultList.iterator();
		while(iterator.hasNext()){
			Project p = (Project)iterator.next();
			ProjectDto projectDto = new ProjectDto();
			projectDto.setProject_id(p.getProject_id());
			projectDto.setProject_name(p.getProject_name());
			projectDto.setAvg_score(p.getAvg_score());
			//=====zhangdi 140701======
			//int avg_score_five = SysUtil.convertProjectScore(p.getAvg_score());
			//===========
			float avg_score_five = p.getAvg_score();
			projectDto.setAvg_score_five(avg_score_five);
			projectDto.setProject_flag(p.getProject_flag());
			projectList.add(projectDto);
		}
		return projectList;
	}
	
	@Override
	public List<ProjectDto> getAllProjectsDetail() {
		// TODO Auto-generated method stub
		String hql="from Project p order by p.project_id";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		List<ProjectDto> projectList = new ArrayList<ProjectDto>();
		List resultList = query.list();
		Iterator iterator = resultList.iterator();
		while(iterator.hasNext()){
			Project p = (Project)iterator.next();
			int project_id = p.getProject_id();
			int project_flag=p.getProject_flag();
			ProjectDto pDto = getProjectDetailById(project_id,project_flag);
			pDto.setProject_id(project_id);
			pDto.setAvg_score(p.getAvg_score());
			pDto.setProject_name(p.getProject_name());
			pDto.setProject_flag(p.getProject_flag());
			projectList.add(pDto);
		}
		return projectList;
	}
	
	public List<String> getAllTestLinkName()
	{
		String hql="select name from nodes_hierarchy where node_type_id = 1";
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		List<String> lstName=new ArrayList<String>();
		List resultList=query.list();
		Iterator iterator=resultList.iterator();
		while(iterator.hasNext()){
			String name=(String)iterator.next();
			lstName.add(name);	
		}
		return lstName; 
	}
	
	//获取project_id对应的映射源的名称
	
	@Override
	public ProjectDto getProjectDetailById(int project_id,int role) {
		String hql = "select s.source_name, ps.source_project_name from Project_source ps, Source s where ps.project_source_pk.project_id = ? " +
				"and s.source_id=ps.project_source_pk.source_id" +
				" order by ps.project_source_pk.source_id";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		ProjectDto projectDto = new ProjectDto();
		query.setString(0, project_id+"");
		List resultList = query.list();
		Iterator iterator = resultList.iterator();
		if(resultList.size() == 0){
			return projectDto;
		}
		Map<String,String> map = new HashMap<String,String>();
		while(iterator.hasNext()){
			Object[] o = (Object[])iterator.next();
			String source_name = (String)o[0];
			String source_project_name = (String)o[1];
			map.put(source_name, source_project_name);
		}
		projectDto.setProject_id(project_id);
		projectDto.setProject_name_tl(map.get("Testlink"));
		projectDto.setProject_name_sn(map.get("Sonar"));
		projectDto.setProject_name_rm(map.get("Redmine"));
		projectDto.setProject_name_rm_support(map.get(SysUtil.project_name_rm_support));
		if(role!=1){
			projectDto.setSuite_name_tl(map.get(SysUtil.suite_name_tl));
			projectDto.setCategory_name_rm(map.get(SysUtil.category_name_rm));
		}
		return projectDto;
	}
	
	
/*	public ProjectDto getProjectDetailById(int project_id,int project_flag) {
		String hql = "select s.source_name, ps.source_project_name from Project_source ps, Source s where ps.project_source_pk.project_id = ? " +
				"and s.source_id=ps.project_source_pk.source_id" +
				" order by ps.project_source_pk.source_id";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		ProjectDto projectDto = new ProjectDto();
		query.setString(0, project_id+"");
		List resultList = query.list();
		Iterator iterator = resultList.iterator();
		if(resultList.size() == 0){
			return projectDto;
		}
		Map<String,String> map = new HashMap<String,String>();
		while(iterator.hasNext()){
			Object[] o = (Object[])iterator.next();
			String source_name = (String)o[0];
			String source_project_name = (String)o[1];
			map.put(source_name, source_project_name);
		}
		projectDto.setProject_id(project_id);
		projectDto.setProject_name_tl(map.get("Testlink"));
		projectDto.setProject_name_sn(map.get("Sonar"));
		projectDto.setProject_name_rm(map.get("Redmine"));
		projectDto.setProject_name_rm_support(map.get(SysUtil.project_name_rm_support));
		if(project_flag==0){
			projectDto.setSuite_name_tl(map.get(SysUtil.suite_name_tl));
			projectDto.setCategory_name_rm(map.get(SysUtil.category_name_rm));
		}
		return projectDto;
	}
	*/
	
	@Override
	public boolean updateProjectSourceNames(Integer project_id,
			String project_name_tl, String project_name_rm,
			String project_name_sn) {
		//更新TestLink中的名称
		boolean testlink = updateSourceName(project_id,project_name_tl,"Testlink");
		if(testlink){
			boolean redmine = updateSourceName(project_id,project_name_rm,"Redmine");
			if(redmine){
					return true;
			}
		}
		return false;
	}
	public boolean updateSourceName(Integer project_id,
			String source_project_name, String source_name) {
		try{
			String hql = "from Source s where s.source_name = '"+source_name+"'";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			List sourceList =query.list();
			if(sourceList.size()==0){
				return false;
			}
			Iterator iterator = sourceList.iterator();
			Source s  = new Source();
			while(iterator.hasNext()){
				s = (Source) iterator.next();
			}
			hql = "from Project_source ps where ps.project_source_pk.source_id = ? and project_source_pk.project_id = ?";
			query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setString(0, s.getSource_id()+"");
			query.setString(1, project_id+"");
			List list = query.list();
			if(list.size() == 0){
				//没有记录，插入
				Project_source ps = new Project_source();
				Project_source_pk project_source_pk = new Project_source_pk();
				project_source_pk.setSource_id(s.getSource_id());
				project_source_pk.setProject_id(project_id);
				ps.setProject_source_pk(project_source_pk);
				ps.setSource_project_name(source_project_name);
				sessionFactory.getCurrentSession().save(ps);
			}else{
				//更新
				hql = "update Project_source ps set ps.source_project_name = ? where ps.project_source_pk.source_id = ? and project_source_pk.project_id = ?";
				query = sessionFactory.getCurrentSession().createQuery(hql);
				query.setString(0, source_project_name);
				query.setString(1, s.getSource_id()+"");
				query.setString(2, project_id+"");
				query.executeUpdate();
			}
			
		}catch(Exception e){
			return false;
		}
		
		return true;
	}
	@Override
	public ProjectDto createNewProject() {
		Project project = new Project();
		project.setProject_name("");
		sessionFactory.getCurrentSession().save(project);
		ProjectDto projectDto = new ProjectDto();
		projectDto.setProject_id(project.getProject_id());
		projectDto.setProject_name(project.getProject_name());
		return projectDto;
	}
	@Override
	public boolean updateProjectNameById(Integer project_id, String project_name) {
		try{
			sessionFactory.getCurrentSession().clear();
			Project project = new Project();
			project.setProject_id(project_id);
			project.setProject_name(project_name);
			sessionFactory.getCurrentSession().saveOrUpdate(project);
		}catch(Exception e){
			log.error("更新项目名称异常，异常信息 "+e.getMessage());
			return false;
		}
		return true;
	}
	@Override
	public String getProjectNameById(int project_id) {
		String hql = "from Project p where p.project_id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0,project_id+"");
		List list = query.list();
		if(list.size() == 0){
			log.error("project_id为 "+project_id+"在数据库中不存在！！");
			return null;
		}
		String project_name = ((Project)list.get(0)).getProject_name();
		return project_name;
	}
	@Override
	public boolean updateProjectRatePatchById(Integer project_id, float rate_patch) {
		String hql = "update Project p set p.rate_patch = ? where p.project_id=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0,rate_patch+"");
		query.setString(1,project_id+"");
		query.executeUpdate();
		//更新年度表
		Calendar cal = Calendar.getInstance();
		hql = "from Year y where y.year_detail = ?";
		query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, cal.get(Calendar.YEAR)+"");
		List yearList = query.list();
		int year_id = 0;
		if(yearList.size() == 0){
			Year y = new Year();
			y.setYear_detail(cal.get(Calendar.YEAR)+"");
			sessionFactory.getCurrentSession().save(y);
			year_id = y.getYear_id();
		}else{
			Year y = (Year) yearList.get(0);
			year_id = y.getYear_id();
		}
		Project_ratepatch_history prh = new Project_ratepatch_history();
		Project_ratepatch_history_pk project_ratepatch_pk = new Project_ratepatch_history_pk();
		project_ratepatch_pk.setProject_id(project_id);
		project_ratepatch_pk.setYear_id(year_id);
		prh.setProject_ratepatch_pk(project_ratepatch_pk);
		prh.setRate_patch(rate_patch);
		sessionFactory.getCurrentSession().saveOrUpdate(prh);
		return true;
	}
	@Override
	public Map<String, Float> getYeardetail(Integer project_id,String year_detail) {
		// TODO Auto-generated method stub
		String hql = "from Year y where y.year_detail = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, year_detail+"");
		List yearList = query.list();
		if(yearList.size() == 0){
			log.error("无法找到year_deatail为： "+year_detail+"的年度信息！");
			return null;
		}
		Map<String, Float> resultMap = new HashMap<String, Float>();
		Year year = (Year)yearList.get(0);
		hql = "from Project_ratepatch_history prh " +
				"where prh.project_ratepatch_history_pk.project_id = ? " +
				"and prh.project_ratepatch_history_pk.year_id = ?";
		query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, project_id+"");
		query.setString(1, year.getYear_id()+"");  
		List list = query.list();
		if(list.size() == 0){
			log.error("无法找到Project_id 为： "+project_id+","+year_detail+"年的补丁发布率历史信息");
			resultMap.put(year_detail, 0f);
			return resultMap;
		}
		float rate_patch = ((Project_ratepatch_history)list.get(0)).getRate_patch();
		resultMap.put(year_detail, rate_patch);
		return resultMap;
	}
	@Override
	public void updateProjectAvgScoreById(int project_id) {
		String hql = "from Sprint s where s.sprint_score <> -1 and s.project_id = ?";
		sessionFactory.getCurrentSession().clear();
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, project_id+"");
		List sprintList = query.list();
		//计算总平均分
		Float avgScore = 0f;
		Iterator iterator = sprintList.iterator();
		while(iterator.hasNext()){
			Sprint s = (Sprint)iterator.next();
			avgScore += s.getSprint_score();
		}
		if(sprintList.size() != 0){
			avgScore = avgScore/sprintList.size();
		}
		//更新总平均分
		hql = "update Project p set p.avg_score = ? where p.project_id = ?";
		query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, avgScore+"");
		query.setString(1, project_id+"");
		query.executeUpdate();
	}
	@Override
	public void updateProjectHistoryAvgScoreById(int project_id,int rank_id) {
		String hql = "select rs.sprint_score from Ranking_sprint rs,Sprint s where rs.sprint_score <> -1 and " +
				"rs.ranking_sprint_pk.sprint_id=s.sprint_id and s.project_id = ? and rs.ranking_sprint_pk.rank_id=?";
		sessionFactory.getCurrentSession().clear();
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, project_id+"");
		query.setString(1, rank_id +"");
		List sprintList = query.list();
		//计算总平均分
		Float avgScore = 0f;
		Iterator iterator = sprintList.iterator();
		while(iterator.hasNext()){
			float score = (Float)iterator.next();
			avgScore += score;
		}
		if(sprintList.size() != 0){
			avgScore = avgScore/sprintList.size();
		}
		//更新总平均分
		hql = "update Ranking_detail rd set rd.rank_score = ? where rd.ranking_detail_pk.project_id = ? and rd.ranking_detail_pk.rank_id=?";
		query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, avgScore+"");
		query.setString(1, project_id+"");
		query.setString(2, rank_id+"");
		query.executeUpdate();
	}
	@Override
	public boolean updateProjectFlagById(Integer project_id, Integer project_flag) {
		try {
			String hql="update Project p set p.project_flag = ? where p.project_id=?";
			Query query=sessionFactory.getCurrentSession().createQuery(hql);
			query.setString(0, project_flag+"");
			query.setString(1, project_id+"");
			query.executeUpdate();
		} catch (HibernateException e) {
			log.error("更新项目flag异常，异常信息 "+e.getMessage());
			return false;
		}
		return true;
	}
	@Override
	public int getProjectFlagById(int project_id) {
		String hql = "from Project p where p.project_id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0,project_id+"");
		List list = query.list();
		if(list.size() == 0){
			log.error("project_id为 "+project_id+"在数据库中不存在！！");
			return -1;
		}
		int project_flag = ((Project)list.get(0)).getProject_flag();
		return project_flag;
	}
	@Override
	public String getSource_ProjectName(int project_id,String source_name) {
		String hql = "from Source s where s.source_name = '"+source_name+"'";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		List sourceList =query.list();
		if(sourceList.size()==0){
			log.error("Source "+source_name+" doesn't exist!");
			return null;
		}
		Iterator iterator = sourceList.iterator();
		Source s  = new Source();
		while(iterator.hasNext()){
			s = (Source) iterator.next();
		}
		hql = "from Project_source ps where ps.project_source_pk.source_id = ? and project_source_pk.project_id = ?";
		query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, s.getSource_id()+"");
		query.setString(1, project_id+"");
		List list = query.list();
		if(list.size() == 0){
			log.error("project_source doesn't exist!");
			return null;
		}else{
			String source_project=((Project_source)list.get(0)).getSource_project_name();
			return source_project;
		}
	}
	
	@Override
	public boolean isProjectExist(String projectName)
	{
		String hql="from Project p where p.project_name=? ";
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, projectName);
		List list=query.list();
		if(list.size()==0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}
