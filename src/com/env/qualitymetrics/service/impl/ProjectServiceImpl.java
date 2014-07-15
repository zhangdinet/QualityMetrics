package com.env.qualitymetrics.service.impl;

import java.util.List;
import java.util.Map;

import com.env.qualitymetrics.common.SysUtil;
import com.env.qualitymetrics.dao.ProjectDao;
import com.env.qualitymetrics.dto.ProjectDto;
import com.env.qualitymetrics.service.ProjectService;
import com.env.qualitymetrics.dao.UserDao;

public class ProjectServiceImpl implements ProjectService{
	
	ProjectDao projectDao;
	UserDao userDao;
	
	public ProjectDao getProjectDao() {
		return projectDao;
	}
	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}
	
	public UserDao getUserDao()
	{
		return this.userDao;
	}
	public void setUserDao(UserDao userDao)
	{
		this.userDao=userDao;
	}
	
	
	@Override
	public List<ProjectDto> getNewestRankList() {
		return projectDao.getNewestRankList();
	}

	@Override
	public List<String> getAllTestLinkName()
	{
		return projectDao.getAllTestLinkName();
	}
	
	@Override
	public List<ProjectDto> getAllProjectsDetail() {
		return projectDao.getAllProjectsDetail();
	}
	@Override
	public boolean updateProjectSourceNames(Integer project_id,
			String project_name_tl, String project_name_rm,
			String project_name_sn) {
		return projectDao.updateProjectSourceNames(project_id,project_name_tl,project_name_rm,project_name_sn);
	}
	@Override
	public ProjectDto createNewProject() {
		return projectDao.createNewProject();
	}
	@Override
	public boolean updateProjectNameById(Integer project_id,String project_name) {
		return projectDao.updateProjectNameById(project_id,project_name);
	}
	@Override
	public String getProjectNameById(int project_id) {
		return projectDao.getProjectNameById(project_id);
	}
	@Override
	public ProjectDto getProjectSourceNamesById(int project_id,int role) {
		return projectDao.getProjectDetailById(project_id,role);
	}
	
	@Override
	public ProjectDto getProjectDetailById(int project_id,int project_flag) {
		return projectDao.getProjectDetailById(project_id,project_flag);
	}
	
	@Override
	public boolean updateRedmineSupportName(Integer project_id, String project_name_rm_support) {
		// TODO Auto-generated method stub
		return projectDao.updateSourceName(project_id,project_name_rm_support,SysUtil.project_name_rm_support);
	}
	@Override
	public boolean updateProjectRatePatchById(Integer project_id, float rate_patch) {
		return projectDao.updateProjectRatePatchById(project_id,rate_patch);
		
	}
	@Override
	public Map<String, Float> getYeardetail(Integer project_id,String year_detail) {
		// TODO Auto-generated method stub
		return projectDao.getYeardetail(project_id,year_detail);
	}
	@Override
	public void updateProjectAvgScoreById(int project_id) {
		// TODO Auto-generated method stub
		projectDao.updateProjectAvgScoreById(project_id);
		
	}
	@Override
	public void updateProjectHistoryAvgScoreById(int project_id,int rank_id) {
		// TODO Auto-generated method stub
		projectDao.updateProjectHistoryAvgScoreById(project_id,rank_id);
		
	}
	@Override
	public boolean updateRedmineCategoryName(Integer project_id,
			String category_name_rm) {
		// TODO Auto-generated method stub
		return projectDao.updateSourceName(project_id,category_name_rm,SysUtil.category_name_rm);
	}
	@Override
	public boolean updateTestlinkSuiteName(Integer project_id,
			String suite_name_tl) {
		// TODO Auto-generated method stub
		return projectDao.updateSourceName(project_id,suite_name_tl,SysUtil.suite_name_tl);
	}
	@Override
	public boolean updateProjectFlagById(int project_id, int project_flag) {
		return projectDao.updateProjectFlagById(project_id, project_flag);
		
	}
	@Override
	public int getProjectFlagById(int project_id) {
		return projectDao.getProjectFlagById(project_id);
	}
	@Override
	public String getSource_ProjectName(int project_id,String source_name) {
		// TODO Auto-generated method stub
		return projectDao.getSource_ProjectName(project_id, source_name);
	}

	@Override
	public boolean isProjectExist(String projectName)
	{
		return projectDao.isProjectExist(projectName);
	}
	
	@Override
	public int getRole(int userID)
	{
		return userDao.getRole(userID);
	}
	
	@Override
	public int getRole(String username)
	{
		return userDao.getRole(username);
	}
}
