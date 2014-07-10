package com.env.qualitymetrics.dao;

import java.util.List;
//import java.util.ArrayList;
//import com.env.qualitymetrics.dto.UserDto;

public interface UserProjectDao {

	public List<Integer> getUserProjects(Integer userID);
	
	public void insertUserAndProject(int userID, int projectID);
	
	public void deleteUserAndProject(int userID);
	
	public void updateUserAndProject(int userID, int projectID);
}