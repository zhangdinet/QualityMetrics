package com.env.qualitymetrics.dao;

import java.util.List;

import com.env.qualitymetrics.dto.ProjectDto;
import com.env.qualitymetrics.dto.RankingDto;

public interface RankingDao {

	List<RankingDto> getRankingPeroidList();

	List<ProjectDto> getSelectedRankList(Integer rank_id);

	RankingDto createNewRanking(String rankPeriod);

	void updateRankingDetail(RankingDto rankingDto);
	
	void updateRankingSprint(RankingDto rankingDto);
	
	String getRankingPeriodById(int rank_id);
}
