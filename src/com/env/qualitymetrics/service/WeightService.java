package com.env.qualitymetrics.service;

import com.env.qualitymetrics.dto.WeightDto;

public interface WeightService {
	WeightDto getWeights();
	boolean updateWeights(WeightDto weightDto);
}
