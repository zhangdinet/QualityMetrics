package com.env.qualitymetrics.service;
import java.util.List;

import com.env.qualitymetrics.dto.UserDto;
import com.env.qualitymetrics.dao.UserProjectDao;
import com.env.qualitymetrics.entity.User;

public interface UserProjectService {
	public List<Integer> getUserProjects(Integer userID);
}
