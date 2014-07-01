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
import com.env.qualitymetrics.dto.WeightDto;
import com.env.qualitymetrics.service.RankingService;
import com.env.qualitymetrics.service.UserService;
import com.env.qualitymetrics.dto.UserDtozd;
import com.env.qualitymetrics.service.UserServicezd;
import com.env.qualitymetrics.tool.RedmineCommon;
import com.env.qualitymetrics.service.WeightService;
import com.env.qualitymetrics.service.ProjectService;

@Controller
public class ProjectController
{
	@Resource(name="redmineCommon")
	RedmineCommon redmineCommon;
	
	@Resource(name="weightService")
	WeightService weightService;
	
	@Resource(name="projectService")
	ProjectService projectService;
	
	private static final Logger log = LoggerFactory.getLogger(WeightController.class);
	
	@RequestMapping("/projectlist")
	public ModelAndView showIndicatorWeight(HttpServletRequest req)
	{
		ModelAndView mv = new ModelAndView();
		mv.setViewName("projectlist");
		List<ProjectDto> projectList = projectService.getAllProjectsDetail();
		for(ProjectDto pr:projectList)
		{
			System.out.println(pr.getProject_name_tl());
		}
		mv.addObject("projectList",projectList);
		return mv;
	}
	
	/*@RequestMapping("/showSettingsProject")
	//显示产品设置中的项目信息
	public ModelAndView showSettingsProjectDetail(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("projectlist");
		List<ProjectDto> projectList = projectService.getAllProjectsDetail();
		for(ProjectDto pr:projectList)
		{
			System.out.println(pr.getProject_name_tl());
		}
		mv.addObject("projectList",projectList);
		return mv;
	}*/
}