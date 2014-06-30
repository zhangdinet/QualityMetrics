package com.env.qualitymetrics.service;

import java.util.List;

import com.env.qualitymetrics.dto.UserDto;
import com.env.qualitymetrics.entity.User;

public interface UserService {
	public User getUser();
	public boolean checkUserLogin(String username, String password);
	public boolean changePassword(String username, String password);
	/***
	 * 根据username获取用户对应的project_id
	 * @param username
	 * @return
	 */
	public Integer getUserProjectIdByUsername(String username);
	
	public boolean checkUserExist(String username);
	
	/***
	 * 根据username判断用户权限：普通用户（查看） or 管理员（可编辑）
	 * @param username
	 * @return
	 */
	public boolean checkUserAuthorityByName(String username);
	
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
	public void updateUserInfo(int user_id, int flag_admin, int project_id,
			String username);
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
