package com.env.qualitymetrics.dao;

import java.util.List;
import java.util.Map;

import com.env.qualitymetrics.dto.ProjectDto;

public interface ProjectDao {

	List<ProjectDto> getNewestRankList();

	List<ProjectDto> getAllProjectsDetail();
	/***
	 * 更新project对应的名字
	 * @param project_id
	 * @param project_name_tl
	 * @param project_name_rm
	 * @param project_name_sn
	 * @return
	 */
	boolean updateProjectSourceNames(Integer project_id,
			String project_name_tl, String project_name_rm,
			String project_name_sn);
	
	ProjectDto createNewProject();
	

	public ProjectDto getProjectDetailById(int project_id,int role);

	boolean updateProjectNameById(Integer project_id, String project_name);

	String getProjectNameById(int project_id);
	//传入project_flag
	/*ProjectDto getProjectDetailById(int project_id,int project_flag);*/

	boolean updateSourceName(Integer project_id,
			String project_name_rm_support, String project_name_rm_support2);

	boolean updateProjectRatePatchById(Integer project_id, float rate_patch);
	
	Map<String, Float> getYeardetail(Integer project_id, String year_detail);

	void updateProjectAvgScoreById(int project_id);
	
	void updateProjectHistoryAvgScoreById(int project_id,int rank_id);

	public List<String> getAllTestLinkName();

	//更新project_flag
	boolean updateProjectFlagById(Integer project_id,Integer project_flag);
	//根据project id 获取 project flag
	int getProjectFlagById(int project_id);
	//根据project id 获取系统保存的source name
	String getSource_ProjectName(int project_id, String source_name);
	
	public boolean isProjectExist(String projectName);
}
