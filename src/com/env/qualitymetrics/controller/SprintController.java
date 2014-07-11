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
import com.env.qualitymetrics.dto.Page;
import com.env.qualitymetrics.dto.ProjectDto;
import com.env.qualitymetrics.dto.RankingDto;
import com.env.qualitymetrics.dto.SprintDto;
import com.env.qualitymetrics.dto.WeightDto;
import com.env.qualitymetrics.service.RankingService;
import com.env.qualitymetrics.service.UserService;
import com.env.qualitymetrics.dto.UserDtozd;
import com.env.qualitymetrics.service.UserServicezd;
import com.env.qualitymetrics.tool.RedmineCommon;
import com.env.qualitymetrics.tool.SonarHandler;
import com.env.qualitymetrics.tool.SurveyMonkeyHandler;
import com.env.qualitymetrics.tool.TestLinkHandler;
import com.env.qualitymetrics.service.WeightService;
import com.env.qualitymetrics.service.ProjectService;
import com.env.qualitymetrics.service.SprintService;

@Controller
public class SprintController
{
	@Resource(name="redmineCommon")
	RedmineCommon redmineCommon;
	
	@Resource(name="weightService")
	WeightService weightService;
	
	@Resource(name="projectService")
	ProjectService projectService;
	
	@Resource(name="sprintService")
	SprintService sprintService;
	
	@Resource(name="testlinkHandler")
	TestLinkHandler testlinkHandler;
	
	@Resource(name="sonarHandler")
	SonarHandler sonarHandler;
	
	@Resource(name="surveyMonkeyHandler")
	SurveyMonkeyHandler surveyMonkeyHandler;
	
	
	private static final Logger log = LoggerFactory.getLogger(WeightController.class);
	
	@RequestMapping("/sprintlist")
	//显示产品设置中的Sprint信息
	public ModelAndView showSettingsSprintDetail(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("sprintlist");
		Integer project_id = Integer.parseInt(req.getParameter("project_id"));
		String project_name = req.getParameter("project_name");
		int project_flag=projectService.getProjectFlagById(project_id);
		int pageNumber = Integer.parseInt(req.getParameter(("pageNumber")));
		Page page = new Page();
		page.setCurrentPage(pageNumber);
		List<SprintDto> sprintList = sprintService.getSprintsByProjectId(project_id,page);
		int totalPages = page.getTotal()/page.getPageSize()+ ((page.getTotal()%page.getPageSize())>0?1:0);
		mv.addObject("sprintList", sprintList);
		mv.addObject("project_name",project_name);
		mv.addObject("project_id", project_id);
		mv.addObject("totalPages",totalPages);
		mv.addObject("pageNumber",page.getCurrentPage());
		mv.addObject("pageSize",page.getPageSize());
		mv.addObject("project_flag", project_flag);
		return mv;
	}
	
	@RequestMapping("/addSprint")
	public ModelAndView addSprint(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		Integer project_id = Integer.parseInt(req.getParameter("project_id"));
		Integer user_project_id = (Integer) req.getSession().getAttribute("project_id");
		Boolean isAdmin=(Boolean)(req.getSession().getAttribute("isAdmin"));
		List<Integer> lstProjectID=(List<Integer>)(req.getSession().getAttribute("lstProjectID"));
		int role =(Integer)(req.getSession().getAttribute("role"));
		
		
		if(!isAdmin && !isProjectOwner(lstProjectID, project_id))  //非系统管理员,非本项目人员不能添加Sprint信息
		{
			mv.setViewName("error");
			return mv;
		}
		String project_name = req.getParameter("project_name");
		mv.setViewName("addSprint");
		mv.addObject("project_name", project_name);
		mv.addObject("project_id", project_id);
		//==========zhangdi 140418======todo 考虑重构==========================================================
		ProjectDto projectDto=projectService.getProjectDetailById(project_id,role);
		String project_name_tl = projectDto.getProject_name_tl();
		List<String> lstTestPlan = testlinkHandler.getProjectTestPlan(project_name_tl);
		mv.addObject("lstTestPlan", lstTestPlan);

		String project_name_rm = projectDto.getProject_name_rm();
		int redmine_project_id=this.redmineCommon.getProjectId(project_name_rm);
		List<String> lstRedmine=this.redmineCommon.getVersionNames(redmine_project_id);
		mv.addObject("lstRedmine",lstRedmine);

		List<String> lstSonar=sonarHandler.getNames();
		mv.addObject("lstSonar",lstSonar);

		List<String> lstTitle=surveyMonkeyHandler.getSurveyMonkeyTitles();
		mv.addObject("lstTitle",lstTitle);
		//============================
		return mv;
	}
	
	public boolean isProjectOwner(List<Integer> lstProjectID, int projectID)
	{
		if(lstProjectID.contains(projectID))
		{
			return true;
		}
		return false;
	}
}