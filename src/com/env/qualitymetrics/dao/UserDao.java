package com.env.qualitymetrics.dao;

import java.util.List;

import com.env.qualitymetrics.dto.UserDto;

public interface UserDao {
	public boolean checkUserLogin(String username, String password);
	
	public boolean changePassword(String username, String password);

	public Integer getUserProjectIdByUsername(String username);

	public boolean checkUserAuthorityByName(String username);

	public List<UserDto> getUserList();

	public void updateUserInfo(int user_id, int flag_admin, int project_id, String username);

	public void resetPwd(int user_id);

	public void deleteUserById(int user_id);

	public UserDto createNewUser();
	
	public boolean checkUserExist(String username);

}
