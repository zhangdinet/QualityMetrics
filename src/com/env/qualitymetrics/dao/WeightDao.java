package com.env.qualitymetrics.dao;

import java.util.List;

import com.env.qualitymetrics.dto.WeightDto;

public interface WeightDao {
	WeightDto getWeightList();
	boolean updateWeights(WeightDto weightDto);
	boolean updateWeightByName(String indicator_name,float weight_rate);
}
