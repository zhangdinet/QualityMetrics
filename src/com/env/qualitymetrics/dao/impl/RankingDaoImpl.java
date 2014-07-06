package com.env.qualitymetrics.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.env.qualitymetrics.dao.RankingDao;
import com.env.qualitymetrics.dto.ProjectDto;
import com.env.qualitymetrics.dto.RankingDto;
import com.env.qualitymetrics.entity.Project;
import com.env.qualitymetrics.entity.Ranking_detail;
import com.env.qualitymetrics.entity.Ranking_detail_pk;
import com.env.qualitymetrics.entity.Ranking_sprint;
import com.env.qualitymetrics.entity.Ranking_sprint_pk;
import com.env.qualitymetrics.entity.Ranking_sprint_score_item;
import com.env.qualitymetrics.entity.Ranking_sprint_score_item_pk;
import com.env.qualitymetrics.entity.Rankings;
import com.env.qualitymetrics.entity.Sprint;
import com.env.qualitymetrics.entity.Sprint_score_item;

public class RankingDaoImpl implements RankingDao{
	SessionFactory sessionFactory;
	private static final Logger log = LoggerFactory.getLogger(RankingDaoImpl.class);
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<RankingDto> getRankingPeroidList() {
		String hql = "from Rankings r order by rank_id desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		List<RankingDto> rankingDtoList = new ArrayList<RankingDto>();
		List resultList = query.list();
		Iterator iterator = resultList.iterator();
		while(iterator.hasNext()){
			Rankings r = (Rankings) iterator.next();
			RankingDto rankingsDto = new RankingDto();
			rankingsDto.setRank_id(r.getRank_id());
			rankingsDto.setRank_period(r.getRank_period());
			rankingsDto.setRank_detail(r.getRank_detail());
			rankingDtoList.add(rankingsDto);
		}
		return rankingDtoList;
	}

	@Override
	public List<ProjectDto> getSelectedRankList(Integer rank_id) {
		// TODO Auto-generated method stub
		String hql="from Ranking_detail rd " +
				"where rd.ranking_detail_pk.rank_id=? order by rd.rank_score desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, rank_id+"");
		List<ProjectDto> projectDtoList = new ArrayList<ProjectDto>();
		List resultList = query.list();
		Iterator iterator = resultList.iterator();
		while(iterator.hasNext()){
			Ranking_detail rd = (Ranking_detail)iterator.next();
			ProjectDto projectDto = new ProjectDto();
			projectDto.setProject_name(rd.getProject_name());
			projectDto.setAvg_score(rd.getRank_score());
			projectDto.setProject_id(rd.getRanking_detail_pk().getProject_id());
			projectDtoList.add(projectDto);
		}
		return projectDtoList;
	}

	@Override
	public RankingDto createNewRanking(String rankPeriod)
	{
		String hql = "from Rankings r where r.rank_period = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, rankPeriod);
		List rankList = query.list();
		RankingDto rankingDto = new RankingDto();
		rankingDto.setRank_period(rankPeriod);
		if(rankList.size() == 0)
		{
			Rankings r = new Rankings();
			r.setRank_period(rankPeriod);
			sessionFactory.getCurrentSession().save(r);
			rankingDto.setRank_id(r.getRank_id());
		}
		else
		{
			Rankings r = (Rankings) rankList.get(0);
			rankingDto.setRank_id(r.getRank_id());
		}
		return rankingDto;
	}
	

	@Override
	public void updateRankingDetail(RankingDto rankingDto) {
		//首先从数据库把当前的项目信息拉取出来
		String ranking_period = rankingDto.getRank_period();
		String year = ranking_period.substring(0,4);
		Integer quarter = Integer.parseInt(ranking_period.substring(5,6));
		
		String hql = "from Project p";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		List projectList = query.list();
		Iterator iterator = projectList.iterator();
		while(iterator.hasNext())
		{
			Ranking_detail ranking_detail = new Ranking_detail();
			Ranking_detail_pk ranking_detail_pk = new Ranking_detail_pk();
			ranking_detail_pk.setRank_id(rankingDto.getRank_id());
			Project p = (Project) iterator.next();
			ranking_detail_pk.setProject_id(p.getProject_id());
			ranking_detail.setRank_score(p.getAvg_score());
			ranking_detail.setProject_name(p.getProject_name());
			ranking_detail.setRanking_detail_pk(ranking_detail_pk);
			sessionFactory.getCurrentSession().saveOrUpdate(ranking_detail);
		}
	}

	@Override
	public void updateRankingSprint(RankingDto rankingDto) {
		//首先从数据库把当前的sprint信息拉取出来
		String ranking_period = rankingDto.getRank_period();
		//String year = ranking_period.substring(0,4);
		//Integer quarter = Integer.parseInt(ranking_period.substring(5,6));
		
		String hql = "from Sprint s";
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		//List projectList = query.list();
		List sprintList=query.list();
		Iterator iterator = sprintList.iterator();
		while(iterator.hasNext()){
			Ranking_sprint ranking_sprint = new Ranking_sprint();
			Ranking_sprint_pk ranking_sprint_pk = new Ranking_sprint_pk();
			ranking_sprint_pk.setRank_id(rankingDto.getRank_id());
			Sprint s = (Sprint) iterator.next();
			ranking_sprint_pk.setSprint_id(s.getSprint_id());
			ranking_sprint.setSprint_score(s.getSprint_score());
			ranking_sprint.setSprint_name(s.getSprint_name());
			ranking_sprint.setRanking_sprint_pk(ranking_sprint_pk);
			sessionFactory.getCurrentSession().saveOrUpdate(ranking_sprint);
			//遍历每个sprint，更新分数
			String hql_item="from Sprint_score_item ssi where ssi.sprint_score_item_pk.sprint_id=?";
			query = this.sessionFactory.getCurrentSession().createQuery(hql_item);
			query.setString(0, s.getSprint_id()+"");
			List scoreItemList = query.list();
			Iterator iterator_scoreItem = scoreItemList.iterator();
			while(iterator_scoreItem.hasNext()){
				Ranking_sprint_score_item ranking_sprint_score_item=new Ranking_sprint_score_item();
				Ranking_sprint_score_item_pk ranking_sprint_score_item_pk=new Ranking_sprint_score_item_pk();
				ranking_sprint_score_item_pk.setRank_id(rankingDto.getRank_id());
				ranking_sprint_score_item_pk.setSprint_id(s.getSprint_id());
				Sprint_score_item sprint_score_item=(Sprint_score_item)iterator_scoreItem.next();
				ranking_sprint_score_item_pk.setScore_item_id(sprint_score_item.getSprint_score_item_pk().getScore_item_id());
				ranking_sprint_score_item.setScore(sprint_score_item.getScore());
				ranking_sprint_score_item.setScore_origin(sprint_score_item.getScore_origin());
				ranking_sprint_score_item.setRanking_sprint_score_item_pk(ranking_sprint_score_item_pk);
				sessionFactory.getCurrentSession().saveOrUpdate(ranking_sprint_score_item);
			}
		}
		
	}
	public String getRankingPeriodById(int rank_id){
		String hql = "select r.rank_period from Rankings r where r.rank_id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, rank_id+"");
		List rankList = query.list();
		if(rankList.size() == 0){
			return null;
		}else{
			String rank_period = (String) rankList.get(0);
			return rank_period;
		}
	}
}