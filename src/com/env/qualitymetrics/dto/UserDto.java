package com.env.qualitymetrics.dto;

public class UserDto {
	int flag_admin;
	int user_id;
	int project_id;
	String username;
	String password;
	String project_name;
	
	public int getFlag_admin() {
		return flag_admin;
	}
	public void setFlag_admin(int flag_admin) {
		this.flag_admin = flag_admin;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getProject_id() {
		return project_id;
	}
	public void setProject_id(int project_id) {
		this.project_id = project_id;
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
	public String getProject_name() {
		return project_name;
	}
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
}
