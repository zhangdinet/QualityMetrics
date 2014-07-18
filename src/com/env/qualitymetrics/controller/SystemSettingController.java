package com.env.qualitymetrics.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.env.qualitymetrics.dao.UserDaozd;
import com.env.qualitymetrics.dto.ProjectDto;
import com.env.qualitymetrics.dto.RankingDto;
import com.env.qualitymetrics.dto.UserDto;
import com.env.qualitymetrics.dto.WeightDto;
import com.env.qualitymetrics.service.RankingService;
import com.env.qualitymetrics.service.UserService;
import com.env.qualitymetrics.dto.UserDtozd;
import com.env.qualitymetrics.service.UserServicezd;
import com.env.qualitymetrics.tool.RedmineCommon;
import com.env.qualitymetrics.service.WeightService;
import com.env.qualitymetrics.service.ProjectService;
import com.env.qualitymetrics.service.UserProjectService;

@Controller
public class SystemSettingController
{
	@Resource(name="userService")
	UserService userService;
	
	@Resource(name="projectService")
	ProjectService projectService;
	
	@Resource(name="userProjectService")
	UserProjectService userProjectService;
	
	private static final Logger log = LoggerFactory.getLogger(SystemSettingController.class);
	
	@RequestMapping("/systemsettinglist")
	public ModelAndView systemSettingList(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		List<UserDto> userList = userService.getUserList();
		
		userList.remove(0);
		for(UserDto userDto:userList)
		{	
			String projectName="";
			 List<Integer> lstProjectID = userProjectService.getUserProjects(userDto.getUser_id());
			 for(Integer i : lstProjectID)
			 {
				 String pName=projectService.getProjectNameById(i);
				 if(projectName.equals(""))
				 {
					 projectName=pName;
				 }
				 else
				 {
					 projectName=projectName + "<br>" + pName;
				 }
			 }
			 userDto.setProject_name(projectName);
		}
		mv.addObject("userList",userList);
		mv.setViewName("systemsettinglist");
		return mv;
	}
	
	/***
	 * 编辑用户信息
	 * @return
	 */
	@RequestMapping("/showModifyUsers")
	public ModelAndView showModifyUsers(HttpServletRequest req){
		String username = req.getParameter("user_name");
		int user_id = Integer.parseInt(req.getParameter("user_id"));
		int project_id =  Integer.parseInt(req.getParameter("user_project_id"));
		String project_name = req.getParameter("project_name");
		int flag_admin = Integer.parseInt(req.getParameter("flag_admin"));
		String modifyResult = req.getParameter("modifyResult");
		List<ProjectDto> projectList = projectService.getAllProjectsDetail();
		ModelAndView mv = new ModelAndView();
		List<UserDto> userList = userService.getUserList();
		mv.addObject("project_name",project_name);
		mv.addObject("project_id",project_id);
		mv.addObject("flag_admin",flag_admin);
		mv.addObject("username",username);
		mv.addObject("user_id",user_id);
		mv.addObject("projectList",projectList);
		mv.addObject("modifyResult",modifyResult);
		mv.setViewName("modify_users");
		return mv;
	}
	
	@RequestMapping("/saveModifyUser")
	public ModelAndView saveModifyUser(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		String username = req.getParameter("username");
		int flag_admin = Integer.parseInt(req.getParameter("flag_admin"));
		String select = req.getParameter("select");
		int user_id = Integer.parseInt(req.getParameter("user_id"));
		if(user_id == -1){
			//新增用户
			UserDto user = userService.createNewUser();
			user_id = user.getUser_id();
		}
		int project_id = 0;//project默认为0
		String project_name = "";
		if(select != null){
			project_id = Integer.parseInt(req.getParameter("select"));
			project_name = projectService.getProjectNameById(project_id);
		}else{
			project_name = "无";
		}
		userService.updateUserInfo(user_id,flag_admin,project_id,username);
		
		mv.addObject("project_name",project_name);
		mv.addObject("user_project_id",project_id);
		mv.addObject("flag_admin",flag_admin);
		mv.addObject("user_name",username);
		mv.addObject("user_id",user_id);
		mv.addObject("modifyResult","ok");
		mv.setViewName("redirect:/showModifyUsers");
		return mv;
	}
	
	@RequestMapping("/addUser")
	public ModelAndView addUser(HttpServletRequest req,HttpServletResponse resp){
		ModelAndView mv = new ModelAndView();
		Boolean isAdmin=(Boolean)req.getSession().getAttribute("isAdmin");
		String strTip=req.getParameter("strTip");
		if(strTip!=null)
		{
			mv.addObject("strTip", strTip);
		}
		if(isAdmin)
		{
			List<ProjectDto> projectDto=projectService.getAllProjectsDetail();
			mv.addObject("lstProjectDto", projectDto);
			mv.setViewName("addUser");
			return mv;
		}
		else
		{
			mv.setViewName("error");
			return mv;
		}
	}
	
	@RequestMapping("/saveNewUser")
	public ModelAndView saveNewUser(HttpServletRequest req,HttpServletResponse resp){
		//resp.setCharacterEncoding("UTF-8");
		ModelAndView mv = new ModelAndView();
		String username = req.getParameter("newUsername");
		int role = Integer.parseInt(req.getParameter("role"));
		
		int projectID=0; //project默认为0
		String projectName="无";
		String strTip="";
		if(userService.checkUserExist(username))
		{
			strTip="该用户已存在，请选择其他名称！";
		}
		else
		{
			UserDto user = userService.createNewUser();
			int userID = user.getUser_id();
			userService.updateUserInfo(userID,username,role);
			if(role==1)
			{	
				projectID = Integer.parseInt(req.getParameter("select"));
				projectName = projectService.getProjectNameById(projectID);
				userProjectService.insertUserAndProject(userID, projectID);
			}
			strTip="添加成功！";
		}
		mv.addObject("strTip", strTip);
		mv.setViewName("addUser");
		List<ProjectDto> projectDto=projectService.getAllProjectsDetail();
		mv.addObject("lstProjectDto", projectDto);
		return mv;
	}
	
	@RequestMapping("/saveUpdateUser")
	public ModelAndView saveUpdateUser(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		String username = req.getParameter("updateUsername");
		int userID= Integer.parseInt(req.getParameter("hiddenUserID"));
		int role = Integer.parseInt(req.getParameter("role"));
		userService.updateUserInfo(userID,username,role);
		String select=req.getParameter("select");
		int projectID=0;
		if(select!=null)
		{
			projectID = Integer.parseInt(select);
			userProjectService.updateUserAndProject(userID, projectID);
		}
		else
		{
			userProjectService.deleteUserAndProject(userID);
		}
		mv.setViewName("updateUser");
		mv.addObject("strTip","更新成功！");
		List<ProjectDto> projectDto=projectService.getAllProjectsDetail();
		mv.addObject("lstProjectDto", projectDto);
		return mv;
	}
	
	@RequestMapping("/addProjectUser")
	public ModelAndView addProjectUser(HttpServletRequest req)
	{
		ModelAndView mv = new ModelAndView();
		String username = req.getParameter("username");
		int flag_admin = Integer.parseInt(req.getParameter("flag_admin"));
		String select = req.getParameter("select");
		UserDto user = userService.createNewUser();
		int user_id = user.getUser_id();
		
		int project_id = 0;//project默认为0
		String project_name = "";
		if(select != null){
			project_id = Integer.parseInt(req.getParameter("select"));
			project_name = projectService.getProjectNameById(project_id);
		}else{
			project_name = "无";
		}
		
		/*//重复用户给出提示？？？ zhangdi todo 140627
		if(!userService.checkUserExist(username))
		{*/
			userService.updateUserInfo(user_id,flag_admin,project_id,username);
	/*	}*/
		
		mv.addObject("project_name",project_name);
		mv.addObject("user_project_id",project_id);
		//mv.addObject("flag_admin",flag_admin);
		mv.addObject("user_name",username);
		mv.addObject("user_id",user_id);
		mv.addObject("modifyResult","ok");
		mv.setViewName("redirect:/showModifyUsers");
		return mv;
	}
	
	@RequestMapping("/deleteUser")
	public ModelAndView deleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		int user_id = Integer.parseInt(req.getParameter("user_id"));
		userProjectService.deleteUserAndProject(user_id);
		userService.deleteUserById(user_id);
		ModelAndView mv = new ModelAndView();
		/*List<UserDto> userList = userService.getUserList();
		mv.addObject("userList",userList);*/
		mv.setViewName("systemsettinglist");
		return mv;
	}
}