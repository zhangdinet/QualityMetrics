package com.env.qualitymetrics.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.env.qualitymetrics.common.EncryptUtil;
import com.env.qualitymetrics.common.SysUtil;
import com.env.qualitymetrics.dao.SprintDao;
import com.env.qualitymetrics.dao.UserDao;
import com.env.qualitymetrics.dto.UserDto;
import com.env.qualitymetrics.entity.Project;
import com.env.qualitymetrics.entity.User;
import com.env.qualitymetrics.entity.UserProject;
import com.env.qualitymetrics.dao.UserProjectDao;

public class UserProjectDaoImpl implements UserProjectDao {
	
	SessionFactory sessionFactory;
	private static final Logger log = LoggerFactory.getLogger(UserProjectDaoImpl.class);
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void insertUserAndProject(int userID, int projectID) {
		//String hql = "insert into UserProject(up.user_id,up.project_id) values(? , ?)";
		UserProject up=new UserProject();
		up.setProject_id(projectID);
		up.setUser_id(userID);
		sessionFactory.getCurrentSession().save(up);
	}
	
	@Override
	public void updateUserAndProject(int userID, int projectID) {
		UserProject up=new UserProject();
		up.setProject_id(projectID);
		up.setUser_id(userID);
		sessionFactory.getCurrentSession().save(up);
	}
	
	
	@Override
	public void deleteUserAndProject(int userID)
	{
		String hql="delete from UserProject up where up.user_id = ? ";
		sessionFactory.getCurrentSession().createQuery(hql).setInteger(0, userID).executeUpdate();
	}
	
	@Override
	public List<Integer> getUserProjects(Integer userID)
	{
		String hql="from UserProject up where up.user_id=? ";
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, userID);
		List list=query.list();
		Iterator iterator=list.iterator();
		
		List<Integer> lstProject=new ArrayList<Integer>();
		while(iterator.hasNext())
		{
			UserProject userProject=(UserProject)iterator.next();
			lstProject.add(userProject.getProject_id());
		}
		return lstProject;
	}
}