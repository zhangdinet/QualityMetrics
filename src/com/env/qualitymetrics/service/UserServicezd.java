package com.env.qualitymetrics.service;

import java.util.List;
import com.env.qualitymetrics.dto.UserDtozd;

public interface UserServicezd
{
	public void saveUser(UserDtozd user);
	public List<UserDtozd> getAllUsers();
	public void deleteUserById(String id);
	public void resetPassword(String id);
	public void updateUser(UserDtozd user);
}
