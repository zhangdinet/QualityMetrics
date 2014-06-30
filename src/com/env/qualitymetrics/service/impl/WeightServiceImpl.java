package com.env.qualitymetrics.service.impl;

import com.env.qualitymetrics.dao.WeightDao;
import com.env.qualitymetrics.dto.WeightDto;
import com.env.qualitymetrics.service.WeightService;

public class WeightServiceImpl implements WeightService {
	WeightDao weightDao;
	@Override
	public WeightDto getWeights() {
		// TODO Auto-generated method stub
		return weightDao.getWeightList();
	}
	public WeightDao getWeightDao() {
		return weightDao;
	}
	public void setWeightDao(WeightDao weightDao) {
		this.weightDao = weightDao;
	}
	@Override
	public boolean updateWeights(WeightDto weightDto) {
		// TODO Auto-generated method stub
		return weightDao.updateWeights(weightDto);
	}

}
