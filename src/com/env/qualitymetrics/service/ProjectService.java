package com.env.qualitymetrics.service;

import java.util.List;
import java.util.Map;

import com.env.qualitymetrics.dto.ProjectDto;

public interface ProjectService {
	public List<String> getAllTestLinkName();
	List<ProjectDto> getNewestRankList();
	/**
	 * 获取所有项目信息
	 * @return
	 */
	List<ProjectDto> getAllProjectsDetail();
	
	public ProjectDto getProjectDetailById(int project_id,int project_flag);
	
	/***
	 * 更新Project的映射信息，包括testlink,redmine,sonar。
	 * 后续添加其他映射源后请单独加方法。
	 * @param project_id
	 * @param project_name_tl
	 * @param project_name_rm
	 * @param project_name_sn
	 * @return
	 */
	boolean updateProjectSourceNames(Integer project_id,
			String project_name_tl, String project_name_rm,
			String project_name_sn);
	/***
	 * 新增一个Project记录，设置其project_id。
	 * @return
	 */
	ProjectDto createNewProject();
	
	/***
	 * 根据project_id更新project name
	 * @param project_id
	 * @param project_name
	 * @return
	 */
	boolean updateProjectNameById(Integer project_id, String project_name);
	
	/***
	 * 根据project的id获取projectName
	 * @param project_id
	 * @return
	 */
	String getProjectNameById(int project_id);
	
	/***
	 * 根据Project_id获取映射源的ProjectName，
	 * @param project_id
	 * @param project_flag
	 * @return
	 */
	ProjectDto getProjectSourceNamesById(int project_id,int project_flag);
	
	/***
	 * 更新testlink top suite的映射名字
	 * @param project_id 
	 * @param suite_name_tl
	 * @return
	 */
	boolean updateTestlinkSuiteName(Integer project_id, String suite_name_tl);
	
	/***
	 * 更新redmine 技术支持率的映射名字
	 * @param project_id 
	 * @param project_name_rm_support
	 * @return
	 */
	boolean updateRedmineSupportName(Integer project_id, String project_name_rm_support);
	
	/***
	 * 更新redmine category的映射名字
	 * @param project_id 
	 * @param category_name_rm
	 * @return
	 */
	boolean updateRedmineCategoryName(Integer project_id, String category_name_rm);
	
	/***
	 * 更新补丁发布率
	 * @param project_id
	 * @param rate_patch 
	 */
	boolean updateProjectRatePatchById(Integer project_id, float rate_patch);
	/***
	 * 获取年度的信息
	 * @param project_id 
	 * @param year_detail 
	 * @return
	 */
	Map<String, Float> getYeardetail(Integer project_id, String year_detail);
	/***
	 * 更新项目的总平均分
	 * @param project_id
	 */
	void updateProjectAvgScoreById(int project_id);
	/***
	 * 更新往期项目的历史平均分
	 * @param project_id
	 */
	void updateProjectHistoryAvgScoreById(int project_id,int rank_id);
	/***
	 * 更新项目的flag标志位 1--project,0--module
	 * @param project_id
	 * @param project_flag
	 */
	boolean updateProjectFlagById(int project_id,int project_flag);
	/***
	 * 根据project的id获取project_flag
	 * @param project_id
	 * @return
	 */
	int getProjectFlagById(int project_id);
	/***
	 * 根据project的id和source name获取 Source_Project映射名
	 * @param project_id
	 * @return
	 */
	String getSource_ProjectName(int project_id, String source_name);
	
	public boolean isProjectExist(String projectName);
	
	public int getRole(int userID);
	
	public int getRole(String username);
}
