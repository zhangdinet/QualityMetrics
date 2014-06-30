package com.env.qualitymetrics.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.annotation.Resource;

import net.sf.json.JSONArray;

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

@Controller
public class SystemSettingController
{
	@Resource(name="userService")
	UserService userService;
	
	@Resource(name="projectService")
	ProjectService projectService;
	
	private static final Logger log = LoggerFactory.getLogger(SystemSettingController.class);
	
	@RequestMapping("/systemsettinglist")
	public ModelAndView showIndicatorWeight(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		List<UserDto> userList = userService.getUserList();
		mv.addObject("userList",userList);
		mv.setViewName("systemsettinglist");
		return mv;
	}
	
	@RequestMapping("/showUserList")
	public ModelAndView showUserList(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		List<UserDto> userList = userService.getUserList();
		mv.addObject("userList",userList);
		mv.setViewName("userlist");
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
	
	
	@RequestMapping("/saveNewUser")
	public ModelAndView saveNewUser(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		String username = req.getParameter("username");
		int flag_admin = Integer.parseInt(req.getParameter("flag_admin"));
		String select = req.getParameter("select");
		int project_id=0; //project默认为0
		String project_name="无";
		
		if(select != null)
		{
			project_id = Integer.parseInt(req.getParameter("select"));
			project_name = projectService.getProjectNameById(project_id);
		}
		
		if(!userService.checkUserExist(username))
		{
			UserDto user = userService.createNewUser();
			int user_id=user.getUser_id();
			userService.updateUserInfo(user_id, flag_admin, project_id, username);
		}
 
		mv.setViewName("mainframe");
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
		mv.addObject("flag_admin",flag_admin);
		mv.addObject("user_name",username);
		mv.addObject("user_id",user_id);
		mv.addObject("modifyResult","ok");
		mv.setViewName("redirect:/showModifyUsers");
		return mv;
	}
}