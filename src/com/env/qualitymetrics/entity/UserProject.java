package com.env.qualitymetrics.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="tbl_user_project")
public class UserProject {
    @Id  
    @GeneratedValue(generator="system-native")
    @GenericGenerator(name = "system-native",strategy="native")
    @Column(length=100)
    private int id;
      
    @Column(length=100)
    private int user_id;
      
    @Column(length=100)
    private int project_id;
    
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
}