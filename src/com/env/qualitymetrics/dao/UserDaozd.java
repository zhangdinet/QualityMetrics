package com.env.qualitymetrics.dao;

import java.util.List;
import com.env.qualitymetrics.dto.UserDtozd;
import com.env.qualitymetrics.entity.Userzd;

public interface UserDaozd {
	//public boolean checkUserLogin(String username, String password);
	
	//public boolean changePassword(String username, String password); 

	//public boolean checkUserAuthorityByName(String username);

	//public List<UserDtozd> getUserList();

	//public void updateUserInfo(int user_id, int flag_admin, int project_id, String username);

	//public void resetPwd(int user_id);

	//public void deleteUserById(int user_id);

	//public UserDtozd createNewUser();
	
	public void saveUser(UserDtozd user);
	public List<UserDtozd> getAllUsers();
	public void deteteUserById(String id);
	public void resetPassword(String id);
	public void updateUser(UserDtozd user);
}
