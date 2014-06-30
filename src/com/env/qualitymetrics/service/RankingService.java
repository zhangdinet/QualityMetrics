package com.env.qualitymetrics.service;

import java.util.List;

import com.env.qualitymetrics.dto.ProjectDto;
import com.env.qualitymetrics.dto.RankingDto;

public interface RankingService {

	List<RankingDto> getRankingPeriodList();

	List<ProjectDto> getSelectedRankList(Integer rank_id);
	
	/***
	 * 创建一个新的排名记录
	 * @param rankPeriod 
	 * @return
	 */
	RankingDto createNewRanking(String rankPeriod);
	/***
	 * 更新排名信息
	 * @param rankingDto
	 */
	void updateRankingDetail(RankingDto rankingDto);
	
	/***
	 * 更新sprint季度分数
	 * @param rankingDto
	 */
	void updateRankingSprint(RankingDto rankingDto);
	
	/***
	 * 获取一个排名记录的名字
	 * @param rank_id
	 * @return
	 */
	String getRankingPeriodById(int rank_id);

}
