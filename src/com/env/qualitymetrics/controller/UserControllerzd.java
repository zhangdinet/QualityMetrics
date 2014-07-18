package com.env.qualitymetrics.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.env.qualitymetrics.dao.UserDaozd;
import com.env.qualitymetrics.dto.RankingDto;
import com.env.qualitymetrics.service.RankingService;
import com.env.qualitymetrics.service.UserService;
import com.env.qualitymetrics.dto.UserDtozd;
import com.env.qualitymetrics.service.UserServicezd;

/*
///@///Controller///
*/
public class UserControllerzd
{
	@Resource(name="userServicezd")
	private UserServicezd userServicezd;
	
	@RequestMapping("/addUserzd")
	public ModelAndView addUser(HttpServletRequest req)
	{
		ModelAndView mv = new ModelAndView();
		mv.setViewName("addUser");
		return mv;
	}
	
	@RequestMapping("/saveNewUser")
	public ModelAndView saveNewUser(HttpServletRequest req)
	{
		//zhangdi todo 验证逻辑 140529
		String username=req.getParameter("username");
		String password=req.getParameter("password");
		String confirmPassword=req.getParameter("confirmPassword");
		int role=Integer.parseInt(req.getParameter("role"));
		
		UserDtozd userDtozd=new UserDtozd();
		userDtozd.setUsername(username);
		userDtozd.setPassword(password);
		userDtozd.setRole(role);
		userServicezd.saveUser(userDtozd);
		
		ModelAndView mv = new ModelAndView();
		List<UserDtozd> userList=userServicezd.getAllUsers();
		mv.addObject("userList",userList);
		mv.setViewName("/users");
		return mv;
	}
	
	@RequestMapping("/users")
	public ModelAndView showUsers()
	{
		List<UserDtozd> userList=userServicezd.getAllUsers();
		ModelAndView mv = new ModelAndView();
		mv.addObject("userList",userList);
		mv.setViewName("users");
		return mv;
	}
	
	@RequestMapping("/resetPassword")
	public ModelAndView resetPassword(HttpServletRequest req)
	{
		String id = req.getParameter("id");
		userServicezd.resetPassword(id);
		List<UserDtozd> userList=userServicezd.getAllUsers();
		ModelAndView mv = new ModelAndView();
		mv.addObject("userList",userList);
		mv.setViewName("users");
		return mv;
	}
	
	@RequestMapping("/saveEditUser")
	public ModelAndView saveEditUser(HttpServletRequest req)
	{
		int id = Integer.parseInt(req.getParameter("id"));
		int role=Integer.parseInt(req.getParameter("role"));
		
		UserDtozd userDtozd=new UserDtozd();
		userDtozd.setUser_id(id);
		userDtozd.setRole(role);
		userServicezd.updateUser(userDtozd);
		
		ModelAndView mv = new ModelAndView();
		List<UserDtozd> userList=userServicezd.getAllUsers();
		mv.addObject("userList",userList);
		mv.setViewName("/users");
		return mv;
	}
}
