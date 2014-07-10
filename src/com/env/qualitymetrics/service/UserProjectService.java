package com.env.qualitymetrics.service;
import java.util.List;

import com.env.qualitymetrics.dto.UserDto;
import com.env.qualitymetrics.dao.UserProjectDao;
import com.env.qualitymetrics.entity.User;

public interface UserProjectService {
	public void insertUserAndProject(int userID, int projectID);
	
	public List<Integer> getUserProjects(Integer userID);
	
	public void deleteUserAndProject(int userID);
	
	public void updateUserAndProject(int userID, int projectID);
}
