package com.env.qualitymetrics.tasks;

import java.util.Calendar;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.env.qualitymetrics.dto.RankingDto;
import com.env.qualitymetrics.service.RankingService;

public class QuarterTask {
	@Resource(name="rankingService")
	private RankingService rankingService;
	
	private static final Logger log = LoggerFactory.getLogger(QuarterTask.class); 
	/***
	 * 定时收集排行榜信息：每个季度末的时候把项目的平均分计入历史
	 */
	public void CollectRankings(){
		/*log.info("季度末开始拉取本季度项目平均分。");
		String rankPeriod = getRankPeriod();
		RankingDto rankingDto = rankingService.createNewRanking(rankPeriod);
		rankingService.updateRankingDetail(rankingDto);
		rankingService.updateRankingSprint(rankingDto);*/
	}
	/***
	 * 根据当前时间确定rankperiod名字
	 * 例如：本task在4.1号执行的，那么生成的rankperiod就是20xxQ1
	 * @return
	 */
	private String getRankPeriod() {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		int quarter = month/3;
		//此处原语句有错，导致出现Q0数据  ====zhangdi 140703=======
		//if(month == 0){
		if(quarter == 0){
			quarter = 4;
			year--;
		}
		return year+"Q"+quarter;
	}
}
