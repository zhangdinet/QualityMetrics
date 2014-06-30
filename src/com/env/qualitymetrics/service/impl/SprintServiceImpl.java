package com.env.qualitymetrics.service.impl;

import java.util.List;

import com.env.qualitymetrics.dao.SprintDao;
import com.env.qualitymetrics.dto.Page;
import com.env.qualitymetrics.dto.SprintDto;
import com.env.qualitymetrics.service.SprintService;

public class SprintServiceImpl implements SprintService{
	
	SprintDao sprintDao;
	@Override
	public List<SprintDto> getSprintsByProjectId(Integer project_id) {
		return sprintDao.getSprintsByProjectId(project_id);
	}
	public SprintDao getSprintDao() {
		return sprintDao;
	}
	public void setSprintDao(SprintDao sprintDao) {
		this.sprintDao = sprintDao;
	}
	@Override
	public SprintDto getSprintById(Integer sprint_id) {
		// TODO Auto-generated method stub
		return sprintDao.getSprintById(sprint_id);
	}
	@Override
	public SprintDto getSprintHistoryById(Integer sprint_id,Integer rank_id) {
		// TODO Auto-generated method stub
		return sprintDao.getSprintHistoryById(sprint_id,rank_id);
	}
	@Override
	public boolean updateSprintDetail(SprintDto sprintDto) {
		// TODO Auto-generated method stub
		return sprintDao.updateSprintDetail(sprintDto);
	}
	@Override
	public SprintDto createNewSprintByProjectId(Integer project_id,String sprint_name) {
		// TODO Auto-generated method stub
		return sprintDao.createNewSprintByProjectId(project_id,sprint_name);
	}
	@Override
	public List<SprintDto> getUncompleteSprints() {
		// TODO Auto-generated method stub
		return sprintDao.getUncompleteSprints();
	}
	@Override
	public void updateScore_item_score(int sprint_id, float score,
			String score_item_name) {
		sprintDao.updateScore_item_score(sprint_id,score,score_item_name);
		
	}
	@Override
	public void updateSprintScore(int sprint_id, float sprint_score) {
		// TODO Auto-generated method stub
		sprintDao.updateSprintScore(sprint_id,sprint_score);
	}
	@Override
	public void updateSprintHistoryScore(int sprint_id, float sprint_score,int rank_id) {
		// TODO Auto-generated method stub
		sprintDao.updateSprintHistoryScore(sprint_id,sprint_score,rank_id);
	}
	@Override
	public void updateScore_item_score_origin(int sprint_id,
			float score_origin, String score_item_name) {
		// TODO Auto-generated method stub
		sprintDao.updateScore_item_score_origin(sprint_id,score_origin,score_item_name);
	}
	@Override
	public List<SprintDto> getSprintsByProjectId(Integer project_id, Page page) {
		// TODO Auto-generated method stub
		return sprintDao.getSprintsByProjectId(project_id,page);
	}
	@Override
	public List<SprintDto> getSprintsHistoryByProjectIdRankId(
			Integer project_id, Integer rank_id) {
		// TODO Auto-generated method stub
		return sprintDao.getSprintsHistoryByProjectIdRankId(project_id, rank_id);
	}
	@Override
	public void updateHistoryScore_item_score(int sprint_id, float score,
			String score_item_name, int rank_id) {
		// TODO Auto-generated method stub
		sprintDao.updateHistoryScore_item_score(sprint_id, score, score_item_name, rank_id);
		
	}
	@Override
	public void updateHistoryScore_item_score_origin(int sprint_id,
			float score_origin, String score_item_name, int rank_id) {
		// TODO Auto-generated method stub
		sprintDao.updateHistoryScore_item_score_origin(sprint_id, score_origin, score_item_name, rank_id);
	}
}
