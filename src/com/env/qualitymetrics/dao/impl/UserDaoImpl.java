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
import com.env.qualitymetrics.dao.UserDao;
import com.env.qualitymetrics.dto.UserDto;
import com.env.qualitymetrics.entity.Project;
import com.env.qualitymetrics.entity.User;
import com.env.qualitymetrics.entity.UserProject;
import com.env.qualitymetrics.dao.UserProjectDao;

public class UserDaoImpl implements UserDao{
	private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);
	private SessionFactory sessionFactory;
	
	private UserProjectDao userProjectDao;
	public void setUserProjectDao(UserProjectDao userProjectDao)
	{
		this.userProjectDao=userProjectDao;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public boolean isAdmin(String username)
	{
		String hql="from User u where u.username=? ";
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, username);
		List list=query.list();
		Iterator iterator=list.iterator();
		while(iterator.hasNext())
		{
			User u=(User)iterator.next();
			if(u.getRole()==0)
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int getRole(String username)
	{
		String hql="from User u where u.username=? ";
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, username);
		List list=query.list();
		Iterator iterator=list.iterator();
		while(iterator.hasNext())
		{
			User u=(User)iterator.next();
			return u.getRole();
		}
		return 100;
	}
	
	@Override
	public int getRole(int userID)
	{
		String hql="from User u where u.user_id=? ";
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		query.setInteger(0, userID);
		List list=query.list();
		Iterator iterator=list.iterator();
		while(iterator.hasNext())
		{
			User u=(User)iterator.next();
			return u.getRole();
		}
		return 100;
	}
	
	@Override
	public Integer getUserID(String username)
	{
		String hql="from User u where u.username=? ";
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, username);
		List list=query.list();
		Iterator iterator=list.iterator();
		while(iterator.hasNext())
		{
			User u=(User)iterator.next();
			return u.getUser_id();
		}
		return -1;
	}
	
	@Override
	public boolean checkUserExist(String username)
	{
		String hql="from User u where u.username=? ";
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, username);
		List list=query.list();
		if(list.size()==0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	@Override
	public boolean checkUserLogin(String username, String password) {
		String hql = "from User u where u.username=? and u.password=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		password = EncryptUtil.encrypt(password, EncryptUtil.key);
		query.setString(0, username);
		query.setString(1, password);
		List list = query.list();
		if(list.size() == 0){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public boolean changePassword(String username, String password) {
		// TODO Auto-generated method stub
		try{
			String hql = "update User u set u.password = ? where u.username = ?";
			Query query = sessionFactory.getCurrentSession().createQuery(hql); 
			password = EncryptUtil.encrypt(password, EncryptUtil.key);
			query.setString(0, password);
			query.setString(1, username);
			query.executeUpdate();
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	@Override
	public List<Integer> getUserProjectIdByUsername(String username)
	{
		String hql = "from User u where u.username=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, username);
		List list = query.list();
		if(list.size() == 0){
			log.error("用户名为："+username+" 未找到！");
			return null;
		}
		User user = (User) list.get(0);
		return userProjectDao.getUserProjects(user.getUser_id());
	}
	
	//zhangdi todo 一个管理员可以管理多个项目时此处可能需要改动 140527
/*	@Override
	public Integer (String username) {
		// TODO Auto-generated method stub
		String hql = "from User u where u.username=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, username);
		List list = query.list();
		if(list.size() == 0){
			log.error("用户名为："+username+" 未找到！");
			return null;
		}
		User user = (User) list.get(0);
		return user.getProject_id();
	}*/
	
	/*@Override
	public boolean checkUserAuthorityByName(String username) {
		String hql = "from User u where u.username=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql); 
		query.setString(0, username);
		List list = query.list();
		if(list.size() == 0){
			log.error("用户名为："+username+" 未找到！");
			return false;
		}
		User user = (User) list.get(0);
		if(user.getFlag_admin() == 0){
			return false;	//普通用户
		}else if(user.getFlag_admin() == 1){
			return true;
		}
		return false;
	}*/
	
	@Override
	public List<UserDto> getUserList() {
		String hql = "from User u ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql); 
		List<UserDto> userList = new ArrayList<UserDto>();
		List resultList = query.list();
		Iterator iterator = resultList.iterator();
		while(iterator.hasNext())
		{
			User u = (User) iterator.next();
			UserDto userDto = new UserDto();
			userDto.setUser_id(u.getUser_id());
			userDto.setUsername(u.getUsername());
			userList.add(userDto);
		/*	if(u.getRole()==0)
			{
				continue;
			}
			else if(u.getRole()==2)
			{
				userDto.setProject_name("无");
				userList.add(userDto);
				continue;
			}*/
			
			/*List<Integer> lstProjectID = userProjectDao.getUserProjects(u.getUser_id());
			String projectNames="";
			for(Integer i:lstProjectID)
			{
				hql = "from Project p where p.project_id = ?";
				query =  sessionFactory.getCurrentSession().createQuery(hql); 
				query.setString(0, i+"");
				List projectList = query.list();
				if(projectNames.equals(""))
				{
					projectNames=((Project)projectList.get(0)).getProject_name();
				}
				else
				{
					projectNames=projectNames+ "<br>" + ((Project)projectList.get(0)).getProject_name();
				}
			}
			userDto.setProject_name(projectNames);
			userList.add(userDto);*/
			/*hql = "from Project p where p.project_id = ?";
			query =  sessionFactory.getCurrentSession().createQuery(hql); 
			query.setString(0, u.getProject_id()+"");
			List projectList = query.list();
			String project_name =  ((Project)projectList.get(0)).getProject_name();
			userDto.setProject_name(project_name);
			userList.add(userDto);*/
		}
		return userList;
	}
	
	@Override
	public void updateUserInfo(int user_id, int flag_admin, int project_id,String username) {
		String hql = "update User u set u.username = ?, u.flag_admin = ?, u.project_id = ? where " +"u.user_id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql); 
		query.setString(0, username);
		query.setString(1, flag_admin+"");
		query.setString(2, project_id+"");
		query.setString(3, user_id+"");
		query.executeUpdate();
	}
	
	@Override
	public void updateUserInfo(int user_id, String username)
	{
		String hql = "update User u set u.username = ? where u.user_id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, username);
		//query.setString(1, flag_admin+"");
		//query.setString(2, project_id+"");
		query.setString(1, user_id+"");
		query.executeUpdate();
	}
	
	@Override
	public void updateUserInfo(int user_id, String username,int role)
	{
		String hql = "update User u set u.username = ?,u.role= ? where u.user_id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, username);
		//query.setString(1, flag_admin+"");
		//query.setString(2, project_id+"");
		query.setInteger(1,role);
		query.setString(2, user_id+"");
		query.executeUpdate();
	}
	/*@Override
	public void addUserInfo(int user_id, int flag_admin, int project_id,String username)
	{
		User u = new User();
		String initPwd = SysUtil.initPwd;
		initPwd = EncryptUtil.encrypt(initPwd, SysUtil.key);
		u.setPassword(initPwd);
		u.setUsername("");
		this.sessionFactory.getCurrentSession().save(u);
		UserDto userDto = new UserDto();
		userDto.setUser_id(u.getUser_id());
		return userDto;
	}*/
	
	@Override
	public void resetPwd(int user_id) {
		// TODO Auto-generated method stub
		String initPwd = SysUtil.initPwd;
		initPwd = EncryptUtil.encrypt(initPwd, SysUtil.key);
		String hql = "update User u set u.password = ? where u.user_id=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql); 
		query.setString(0,initPwd);
		query.setString(1, user_id+"");
		query.executeUpdate();
		log.info("已重置ID为"+user_id+"的用户密码！");
	}
	
	@Override
	public void deleteUserById(int user_id) {
		// TODO Auto-generated method stub
		String hql = "delete from User u where u.user_id = ?";
		Query query =  sessionFactory.getCurrentSession().createQuery(hql); 
		query.setString(0, user_id+"");
		query.executeUpdate();
		log.info("已更新ID为"+user_id+"的用户！");
	}
	
	@Override
	public UserDto createNewUser() {
		// TODO Auto-generated method stub
		User u = new User();
		String initPwd = SysUtil.initPwd;
		initPwd = EncryptUtil.encrypt(initPwd, SysUtil.key);
		u.setPassword(initPwd);
		u.setUsername("");
		this.sessionFactory.getCurrentSession().save(u);
		UserDto userDto = new UserDto();
		userDto.setUser_id(u.getUser_id());
		return userDto;
	}
}
