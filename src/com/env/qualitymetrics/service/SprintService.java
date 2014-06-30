package com.env.qualitymetrics.service;

import java.util.List;

import com.env.qualitymetrics.dto.Page;
import com.env.qualitymetrics.dto.SprintDto;

public interface SprintService {


	List<SprintDto> getSprintsByProjectId(Integer project_id, Page page);

	SprintDto getSprintById(Integer sprint_id);
	
	SprintDto getSprintHistoryById(Integer sprint_id,Integer rank_id);
	/***
	 * 产品设置设置Sprint基本信息，包括名称，映射名称，SurveryMonkey，Build时间，
	 * 开始时间，结束时间，Ipd,Lmt过程质量分数。
	 * @param sprintDto
	 * @return
	 */
	boolean updateSprintDetail(SprintDto sprintDto);
	
	//根据ProjectId新建一个Sprint
	SprintDto createNewSprintByProjectId(Integer project_id, String sprint_name);
	
	//定时任务获取系统中 不完整的sprint列表
	List<SprintDto> getUncompleteSprints();

	void updateScore_item_score(int sprint_id, float score, String score_item_name);
	
	void updateHistoryScore_item_score(int sprint_id, float score, String score_item_name,int rank_id);

	void updateSprintScore(int sprint_id, float sprint_score);
	
	void updateSprintHistoryScore(int sprint_id, float sprint_score,int rank_id);
	
	/***
	 * 更新sprint原始值
	 * @param sprint_id
	 * @param sonar_origin
	 * @param sonar
	 */
	void updateScore_item_score_origin(int sprint_id, float score_origin,
			String score_item_name);
	/***
	 * 更新sprint历史原始值
	 * @param sprint_id
	 * @param sonar_origin
	 * @param sonar
	 */
	void updateHistoryScore_item_score_origin(int sprint_id, float score_origin,
			String score_item_name,int rank_id);

	List<SprintDto> getSprintsByProjectId(Integer project_id);
	
	List<SprintDto> getSprintsHistoryByProjectIdRankId(Integer project_id, Integer rank_id);

}
