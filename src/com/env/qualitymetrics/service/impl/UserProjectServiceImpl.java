package com.env.qualitymetrics.service.impl;

import java.util.List;
import com.env.qualitymetrics.service.UserProjectService;
import com.env.qualitymetrics.dao.UserProjectDao;

public class UserProjectServiceImpl implements UserProjectService{
	UserProjectDao userProjectDao;
	
	@Override
	public List<Integer> getUserProjects(Integer userID)
	{
		return userProjectDao.getUserProjects(userID);
	}
	
	@Override
	public void insertUserAndProject(int userID, int projectID)
	{
		this.userProjectDao.insertUserAndProject(userID, projectID);
	}
	
	public void setUserProjectDao(UserProjectDao userProjectDao)
	{
		this.userProjectDao=userProjectDao;
	}
	public UserProjectDao getUserProjectDao()
	{
		return this.userProjectDao;
	}
	
	@Override
	public void deleteUserAndProject(int userID)
	{
		this.userProjectDao.deleteUserAndProject(userID);
	}
	
	@Override
	public void updateUserAndProject(int userID, int projectID)
	{
		this.userProjectDao.updateUserAndProject(userID, projectID);
	}
}
