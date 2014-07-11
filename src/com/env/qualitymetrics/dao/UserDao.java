package com.env.qualitymetrics.dao;

import java.util.List;

import com.env.qualitymetrics.dto.UserDto;

public interface UserDao {
	public boolean checkUserLogin(String username, String password);
	
	public boolean changePassword(String username, String password);

	/*public Integer getUserProjectIdByUsername(String username);*/
	
	public List<Integer> getUserProjectIdByUsername(String username);

	/*public boolean checkUserAuthorityByName(String username);*/

	public List<UserDto> getUserList();

	public void updateUserInfo(int user_id, int flag_admin, int project_id, String username);
	
	public void updateUserInfo(int user_id, String username);
	
	public void updateUserInfo(int user_id, String username,int role);

	public void resetPwd(int user_id);

	public void deleteUserById(int user_id);

	public UserDto createNewUser();
	
	boolean checkUserExist(String username);
	
	public boolean isAdmin(String username);
	
	public int getRole(int userID);
	
	public int getRole(String username);
	
	public Integer getUserID(String username);
}
