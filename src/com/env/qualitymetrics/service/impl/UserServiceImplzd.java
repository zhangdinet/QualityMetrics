package com.env.qualitymetrics.service.impl;

import java.util.List;

import com.env.qualitymetrics.dto.UserDtozd;
import com.env.qualitymetrics.dao.UserDaozd;
import com.env.qualitymetrics.service.UserServicezd;

public class UserServiceImplzd implements UserServicezd{
	
	UserDaozd userDaozd;
	public UserDaozd getUserDaozd()
	{
		return this.userDaozd;
	}
	
	public void setUserDaozd(UserDaozd userDaozd)
	{
		this.userDaozd=userDaozd;
	}
	
	public void saveUser(UserDtozd userDtozd)
	{
		userDaozd.saveUser(userDtozd);
	}
	
	public List<UserDtozd> getAllUsers()
	{
		return userDaozd.getAllUsers();
	}
	
	public void deleteUserById(String id)
	{
		this.userDaozd.deteteUserById(id);
	}
	
	public void resetPassword(String id)
	{
		userDaozd.resetPassword(id);
	}
	
	public void updateUser(UserDtozd user)
	{
		userDaozd.updateUser(user);
	}
	/*
	 * public void resetPassword(String id);
	public void updateUser(UserDtozd user);
	 */
}
