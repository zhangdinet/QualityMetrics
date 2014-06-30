package com.env.qualitymetrics.service.impl;

import java.util.List;

import com.env.qualitymetrics.dao.RankingDao;
import com.env.qualitymetrics.dto.ProjectDto;
import com.env.qualitymetrics.dto.RankingDto;
import com.env.qualitymetrics.service.RankingService;

public class RankingServiceImpl implements RankingService{
	RankingDao rankingDao;

	public RankingDao getRankingDao() {
		return rankingDao;
	}

	public void setRankingDao(RankingDao rankingDao) {
		this.rankingDao = rankingDao;
	}

	@Override
	public List<RankingDto> getRankingPeriodList() {
		
		return rankingDao.getRankingPeroidList();
	}

	@Override
	public List<ProjectDto> getSelectedRankList(Integer rank_id) {
		// TODO Auto-generated method stub
		return rankingDao.getSelectedRankList(rank_id);
	}

	@Override
	public RankingDto createNewRanking(String rankPeriod) {
		// TODO Auto-generated method stub
		return rankingDao.createNewRanking(rankPeriod);
	}

	@Override
	public void updateRankingDetail(RankingDto rankingDto) {
		// TODO Auto-generated method stub
		rankingDao.updateRankingDetail(rankingDto);
	}

	@Override
	public void updateRankingSprint(RankingDto rankingDto) {
		// TODO Auto-generated method stub
		rankingDao.updateRankingSprint(rankingDto);
	}

	@Override
	public String getRankingPeriodById(int rank_id) {
		// TODO Auto-generated method stub
		return rankingDao.getRankingPeriodById(rank_id);
	}
}
