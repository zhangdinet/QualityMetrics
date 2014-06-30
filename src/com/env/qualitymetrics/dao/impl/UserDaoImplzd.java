package com.env.qualitymetrics.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.env.qualitymetrics.common.EncryptUtil;
import com.env.qualitymetrics.common.SysUtil;
import com.env.qualitymetrics.dao.RankingDao;
import com.env.qualitymetrics.dao.UserDaozd;
import com.env.qualitymetrics.dto.ProjectDto;
import com.env.qualitymetrics.dto.RankingDto;
import com.env.qualitymetrics.dto.UserDtozd;
import com.env.qualitymetrics.entity.Indicator_weight;
import com.env.qualitymetrics.entity.Project;
import com.env.qualitymetrics.entity.Project_source;
import com.env.qualitymetrics.entity.Project_source_pk;
import com.env.qualitymetrics.entity.Ranking_detail;
import com.env.qualitymetrics.entity.Rankings;
import com.env.qualitymetrics.entity.Userzd;


public class UserDaoImplzd implements UserDaozd{
	
	//private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void saveUser(UserDtozd userDtozd)
	{
		Userzd userzd=new Userzd();
		userzd.setUsername(userDtozd.getUsername());
		String password=EncryptUtil.encrypt(userDtozd.getPassword(), SysUtil.key);
		userzd.setPassword(password);
		this.sessionFactory.getCurrentSession().save(userzd);
	}
	
	public List<UserDtozd> getAllUsers()
	{
		String hql="from Userzd u order by u.user_id";
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		
		List<UserDtozd> userDtozdList = new ArrayList<UserDtozd>();
		List resultList = query.list();
		Iterator iterator = resultList.iterator();
		while(iterator.hasNext()){
			Userzd u = (Userzd)iterator.next();
			UserDtozd userDto=new UserDtozd();
			userDto.setUser_id(u.getUser_id());
			userDto.setUsername(u.getUsername());
			userDtozdList.add(userDto);
			}
		return userDtozdList;
	}
	
	public void deteteUserById(String id)
	{
		String hql = "delete from Userzd u where u.user_id = ?";
		Query query =  sessionFactory.getCurrentSession().createQuery(hql); 
		query.setString(0, id+"");
		query.executeUpdate();
	}
	
	public void resetPassword(String id)
	{
		String initPwd = SysUtil.initPwd;
		initPwd = EncryptUtil.encrypt(initPwd, SysUtil.key);
		String hql = "update Userzd u set u.password = ? where u.user_id=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql); 
		query.setString(0,initPwd);
		query.setString(1,id+"");
		query.executeUpdate();
	}
	
	public void updateUser(UserDtozd user)
	{
		String hql = "update Userzd u set u.role = ? where " +"u.user_id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql); 
		query.setString(0, user.getRole()+"");
		query.setString(1, user.getUser_id()+"");
		query.executeUpdate();
	}
}