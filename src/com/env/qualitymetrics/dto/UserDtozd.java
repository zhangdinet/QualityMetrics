package com.env.qualitymetrics.dto;

public class UserDtozd {
	int user_id;
	String username;
	String password;
	int role;
	
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setRole(int role)
	{
		this.role=role;
	}
	public int getRole()
	{
		return this.role;
	}
}
