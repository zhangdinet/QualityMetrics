package com.env.qualitymetrics.dao;

import java.util.List;

import com.env.qualitymetrics.dto.Page;
import com.env.qualitymetrics.dto.SprintDto;
import com.env.qualitymetrics.entity.Ranking_sprint;

public interface SprintDao {

	List<SprintDto> getSprintsByProjectId(Integer project_id);

	SprintDto getSprintById(Integer sprint_id);
	
	SprintDto getSprintHistoryById(Integer sprint_id,Integer rank_id);

	boolean updateSprintDetail(SprintDto sprintDto);

	SprintDto createNewSprintByProjectId(Integer project_id, String sprint_name);

	List<SprintDto> getUncompleteSprints();

	void updateSprintScore(int sprint_id, float sprint_score);
	
	void updateSprintHistoryScore(int sprint_id, float sprint_score,int rank_id);

	void updateScore_item_score(int sprint_id, float score,
			String score_item_name);

	void updateScore_item_score_origin(int sprint_id, float score_origin,
			String score_item_name);
	
	void updateHistoryScore_item_score(int sprint_id, float score,
			String score_item_name,int rank_id);

	void updateHistoryScore_item_score_origin(int sprint_id, float score_origin,
			String score_item_name,int rank_id);

	List<SprintDto> getSprintsByProjectId(Integer project_id, Page page);
	
	List<SprintDto> getSprintsHistoryByProjectIdRankId(Integer project_id,Integer rank_id);

}
