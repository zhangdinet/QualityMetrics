package com.env.qualitymetrics.service.impl;

import java.util.List;

import com.env.qualitymetrics.dao.UserDao;
import com.env.qualitymetrics.dto.UserDto;
import com.env.qualitymetrics.entity.User;
import com.env.qualitymetrics.service.UserService;

public class UserServiceImpl implements UserService{
	UserDao userDao;
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public boolean isAdmin(String username) {
		// TODO Auto-generated method stub
		return this.userDao.isAdmin(username);
	}
	
	@Override
	public boolean checkUserLogin(String username, String password) {
		// TODO Auto-generated method stub
		return this.userDao.checkUserLogin(username, password);
	}

	@Override
	public User getUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean changePassword(String username, String password) {
		// TODO Auto-generated method stub
		return this.userDao.changePassword(username, password);
	}

	@Override
	public Integer getUserProjectIdByUsername(String username) {
		// TODO Auto-generated method stub
		return this.userDao.getUserProjectIdByUsername(username);
	}

	/*@Override
	public boolean checkUserAuthorityByName(String username) {
		// TODO Auto-generated method stub
		return this.userDao.checkUserAuthorityByName(username);
	}*/

	@Override
	public List<UserDto> getUserList() {
		// TODO Auto-generated method stub
		return this.userDao.getUserList();
	}

	@Override
	public void updateUserInfo(int user_id, int flag_admin, int project_id,
			String username) {
		this.userDao.updateUserInfo(user_id,flag_admin,project_id,username);
	}

	@Override
	public boolean checkUserExist(String username)
	{
		return userDao.checkUserExist(username);
	}
	
	
	@Override
	public void resetPwd(int user_id) {
		// TODO Auto-generated method stub
		userDao.resetPwd(user_id);
	}

	@Override
	public void deleteUserById(int user_id) {
		// TODO Auto-generated method stub
		userDao.deleteUserById(user_id);
	}

	@Override
	public UserDto createNewUser() {
		// TODO Auto-generated method stub
		return userDao.createNewUser();
	}

	@Override
	public Integer getUserID(String username)
	{
		return userDao.getUserID(username);
	}
}
