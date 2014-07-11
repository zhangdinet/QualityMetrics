package com.env.qualitymetrics.service;

import java.util.List;

import com.env.qualitymetrics.dto.UserDto;
import com.env.qualitymetrics.entity.User;

public interface UserService {
	public User getUser();
	public boolean isAdmin(String username);
	public Integer getUserID(String username);
	public int getRole(int userID);
	public int getRole(String userName);
	
	public boolean checkUserLogin(String username, String password);
	public boolean changePassword(String username, String password);
	/***
	 * 根据username获取用户对应的project_id
	 * @param username
	 * @return
	 */
	
	public List<Integer> getUserProjectIdByUsername(String username);
	
	public boolean checkUserExist(String username);
	
	
	//public boolean checkUserAuthorityByName(String username);
	
	/***
	 * 获取用户列表
	 * @return
	 */
	public List<UserDto> getUserList();
	/***
	 * 修改用户基本信息：用户名，用户所属项目，管理员标记
	 * @param user_id
	 * @param flag_admin
	 * @param project_id
	 * @param username
	 */
	public void updateUserInfo(int user_id, int flag_admin, int project_id,String username);
	
	
	public void updateUserInfo(int user_id, String username);
	
	public void updateUserInfo(int user_id, String username,int role);
	/***
	 * 重置密码
	 * @param user_id
	 */
	public void resetPwd(int user_id);
	/***
	 * 删除用户
	 * @param user_id
	 */
	public void deleteUserById(int user_id);
	/***
	 * 新增用户
	 * @return
	 */
	public UserDto createNewUser();
}
