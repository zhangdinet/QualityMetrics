package com.env.qualitymetrics.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="tbl_userszd")
public class Userzd {
	@Id
	@GeneratedValue(generator="system-native")
	@GenericGenerator(name="system-native",strategy="native")
	@Column(length=100)
	private int user_id;
	
	@Column(length=100)
	private String username;
	
	@Column(length=100)
	private String password;

	@Column(length=100)
	private int role;

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