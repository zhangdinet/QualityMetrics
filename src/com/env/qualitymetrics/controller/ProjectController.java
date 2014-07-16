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

import com.env.qualitymetrics.common.SysUtil;
import com.env.qualitymetrics.dao.UserDaozd;
import com.env.qualitymetrics.dto.ProjectDto;
import com.env.qualitymetrics.dto.RankingDto;
import com.env.qualitymetrics.dto.WeightDto;
import com.env.qualitymetrics.service.RankingService;
import com.env.qualitymetrics.service.UserService;
import com.env.qualitymetrics.dto.UserDtozd;
import com.env.qualitymetrics.service.UserServicezd;
import com.env.qualitymetrics.tool.RedmineCommon;
import com.env.qualitymetrics.tool.TestLinkForSuiteHandler;
import com.env.qualitymetrics.service.WeightService;
import com.env.qualitymetrics.service.ProjectService;
import com.env.qualitymetrics.tool.TestLinkHandler;

@Controller
public class ProjectController
{
	@Resource(name="redmineCommon")
	RedmineCommon redmineCommon;
	
	@Resource(name="weightService")
	WeightService weightService;
	
	@Resource(name="projectService")
	ProjectService projectService;
	
	@Resource(name="testlinkHandler")
	TestLinkHandler testlinkHandler;
	
	@Resource(name="testlinkForSuiteHandler")
	TestLinkForSuiteHandler testlinkForSuiteHandler;
	
	private static final Logger log = LoggerFactory.getLogger(WeightController.class);
	
	@RequestMapping("/projectlist")
	public ModelAndView projectList(HttpServletRequest req)
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
	
	@RequestMapping("/saveNewProductProject")
	//保存编辑结果
	public ModelAndView saveNewProject(HttpServletRequest req){
		String project_name = req.getParameter("project_name");
		String project_name_tl = req.getParameter("testlinkName");
		String project_name_rm = req.getParameter("redmineName");
		String project_name_rm_support = req.getParameter("redmineSupportName");
		ModelAndView mv = new ModelAndView();
		Integer project_id = null;
		if(projectService.isProjectExist(project_name))
		{
			mv.addObject("project_name", "");
			List<String> lstTestlineName=testlinkHandler.getTestlinkProjectNames();
			mv.addObject("lstTestlinkName",lstTestlineName);
			List<String> lstRedmineName=redmineCommon.getRedmineProjectNames();
			mv.addObject("lstRedmineName",lstRedmineName);
			List<String> lstRedmineSupportName=redmineCommon.getRedmineSupportProjectNames();
			mv.addObject("lstRedmineSupportName", lstRedmineSupportName);
			mv.setViewName("add_product_project");
			mv.addObject("saveResult","产品名称重复！");
			return mv;
		}
		
		ProjectDto project = projectService.createNewProject();
		project_id = project.getProject_id();

		if(projectService.updateProjectNameById(project_id,project_name) && projectService.updateProjectSourceNames(project_id,project_name_tl,project_name_rm,"")
			&& projectService.updateRedmineSupportName(project_id,project_name_rm_support) && projectService.updateProjectFlagById(project_id, SysUtil.project_flag))
		{
			mv.addObject("saveResult","添加成功！");
			mv.setViewName("redirect:projectlist");
		}
		else
		{
			mv.addObject("project_name", "");
			List<String> lstTestlineName=testlinkHandler.getTestlinkProjectNames();
			mv.addObject("lstTestlinkName",lstTestlineName);
			List<String> lstRedmineName=redmineCommon.getRedmineProjectNames();
			mv.addObject("lstRedmineName",lstRedmineName);
			List<String> lstRedmineSupportName=redmineCommon.getRedmineSupportProjectNames();
			mv.addObject("lstRedmineSupportName", lstRedmineSupportName);
			mv.setViewName("add_product_project");
			mv.addObject("saveResult","添加失败！");
		}
		return mv;
	}
	
	@RequestMapping("/saveUpdateProductProject")
	public ModelAndView saveUpdateProductProject(HttpServletRequest req){
		Integer project_id = Integer.parseInt(req.getParameter("project_id"));
		String project_name = req.getParameter("project_name");
		String project_name_tl = req.getParameter("testlinkName");
		String project_name_rm = req.getParameter("redmineName");
		String project_name_rm_support = req.getParameter("redmineSupportName");
 
		ModelAndView mv = new ModelAndView();
		if(projectService.updateProjectNameById(project_id,project_name) &&
				projectService.updateProjectSourceNames(project_id,project_name_tl,project_name_rm,"")
				&& projectService.updateRedmineSupportName(project_id,project_name_rm_support)
				&& projectService.updateProjectFlagById(project_id, SysUtil.project_flag)){
			mv.addObject("saveResult","更新成功！");
			mv.setViewName("redirect:projectlist");
		}else{
			mv.addObject("saveResult","更新失败！");
			mv.setViewName("update_product_project");
		}
		return mv;
	}
	
	@RequestMapping("/updateModuleProject")
	public ModelAndView updateModuleProject(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("update_module_project");
		String project_name ="";
		project_name=SysUtil.decodeUtf8(req.getParameter("project_name"));
		Integer project_id = Integer.parseInt(req.getParameter("project_id"));
		ProjectDto projectDto=this.projectService.getProjectSourceNamesById(project_id,SysUtil.module_flag);
		String project_name_tl = projectDto.getProject_name_tl();
		String project_name_rm = projectDto.getProject_name_rm();
		String project_name_sn = projectDto.getProject_name_sn();
		String project_name_rm_support = projectDto.getProject_name_rm_support();
		String suite_name_tl=projectDto.getSuite_name_tl();
		String category_name_rm=projectDto.getCategory_name_rm();
		mv.addObject("project_name", project_name).addObject("project_name_tl", project_name_tl)
		.addObject("project_name_rm", project_name_rm).addObject("project_name_sn", project_name_sn)
		.addObject("project_id", project_id).addObject("project_name_rm_support",project_name_rm_support)
		.addObject("suite_name_tl", suite_name_tl).addObject("category_name_rm", category_name_rm)
		.addObject("project_flag", SysUtil.module_flag);

		List<String> lstTestlineName=testlinkHandler.getTestlinkProjectNames();
		mv.addObject("lstTestlinkName",lstTestlineName);
		List<String> lstRedmineName=redmineCommon.getRedmineProjectNames();
		mv.addObject("lstRedmineName",lstRedmineName);
		List<String> lstRedmineSupportName=redmineCommon.getRedmineSupportProjectNames();
		mv.addObject("lstRedmineSupportName", lstRedmineSupportName);
		List<String> lstTestlinkSuiteName=testlinkForSuiteHandler.getSuiteNamesByTestprojectName(project_name_tl);
		mv.addObject("lstTestlinkSuiteName", lstTestlinkSuiteName);
		List<String> lstRedmineCategoryName=redmineCommon.getCategoryNamesByProjectName(project_name_rm);
		mv.addObject("lstRedmineCategoryName", lstRedmineCategoryName);
		return mv;
	}
	
	@RequestMapping("/saveUpdateModuleProject")
	//保存项目模块编辑结果
	public ModelAndView saveUpdateModuleProject(HttpServletRequest req){
		ModelAndView mv=new ModelAndView();
		Integer project_id = Integer.parseInt(req.getParameter("project_id"));
		String project_name = req.getParameter("project_name");
		String project_name_tl = req.getParameter("testlinkName");
		String project_name_rm = req.getParameter("redmineName");
		String project_name_rm_support = req.getParameter("redmineSupportName");
		
		String[] topsuites_testlinks = req.getParameterValues("topsuite_testlink");
		String suite_name_tl=SysUtil.suiteFilter(topsuites_testlinks);
		suite_name_tl=suite_name_tl.substring(1,suite_name_tl.length()-1);
		suite_name_tl=suite_name_tl.replaceAll(",",SysUtil.splitFlag);
		String[] categories_redmine = req.getParameterValues("category_redmine");
		String category_name_rm=Arrays.toString(categories_redmine);
		category_name_rm=category_name_rm.substring(1,category_name_rm.length()-1);
		category_name_rm=category_name_rm.replaceAll(",",SysUtil.splitFlag);
		
		if(projectService.updateProjectNameById(project_id,project_name) &&
				projectService.updateProjectSourceNames(project_id,project_name_tl,project_name_rm,"")
				&& projectService.updateRedmineSupportName(project_id,project_name_rm_support)
				&& projectService.updateRedmineCategoryName(project_id, category_name_rm)
				&& projectService.updateTestlinkSuiteName(project_id, suite_name_tl)
				&& projectService.updateProjectFlagById(project_id, SysUtil.module_flag)){
			mv.addObject("updateResult","更新成功！");
			mv.setViewName("redirect:projectlist");
			return mv;
		}else{
			mv.addObject("updateResult","更新失败！");
			List<String> lstTestlineName=testlinkHandler.getTestlinkProjectNames();
			mv.addObject("lstTestlinkName",lstTestlineName);
			List<String> lstRedmineName=redmineCommon.getRedmineProjectNames();
			mv.addObject("lstRedmineName",lstRedmineName);
			List<String> lstRedmineSupportName=redmineCommon.getRedmineSupportProjectNames();
			mv.addObject("lstRedmineSupportName", lstRedmineSupportName);
			List<String> lstTestlinkSuiteName=testlinkForSuiteHandler.getSuiteNamesByTestprojectName(project_name_tl);
			mv.addObject("lstTestlinkSuiteName", lstTestlinkSuiteName);
			List<String> lstRedmineCategoryName=redmineCommon.getCategoryNamesByProjectName(project_name_rm);
			mv.addObject("lstRedmineCategoryName", lstRedmineCategoryName);
			mv.setViewName("update_module_project");
			return mv;
		}
	}

	@RequestMapping("/addProductProject")
	public ModelAndView addProject(HttpServletRequest req){
		ModelAndView mv=new ModelAndView();
		mv.addObject("project_name", "");
		List<String> lstTestlineName=testlinkHandler.getTestlinkProjectNames();
		mv.addObject("lstTestlinkName",lstTestlineName);
		List<String> lstRedmineName=redmineCommon.getRedmineProjectNames();
		mv.addObject("lstRedmineName",lstRedmineName);
		List<String> lstRedmineSupportName=redmineCommon.getRedmineSupportProjectNames();
		mv.addObject("lstRedmineSupportName", lstRedmineSupportName);
		mv.setViewName("add_product_project");
		return mv;
	}
	
	@RequestMapping("/updateProductProject")
	//编辑产品信息
	public ModelAndView updateProductProject(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("update_product_project");
		String project_name ="";
		project_name=SysUtil.decodeUtf8(req.getParameter("project_name"));
		Integer project_id = Integer.parseInt(req.getParameter("project_id"));
		ProjectDto projectDto=this.projectService.getProjectSourceNamesById(project_id,SysUtil.project_flag);
		String project_name_tl = projectDto.getProject_name_tl();
		String project_name_rm = projectDto.getProject_name_rm();
		String project_name_sn = projectDto.getProject_name_sn();
		String project_name_rm_support = projectDto.getProject_name_rm_support();
		mv.addObject("project_name", project_name).addObject("project_name_tl", project_name_tl)
		.addObject("project_name_rm", project_name_rm).addObject("project_name_sn", project_name_sn)
		.addObject("project_id", project_id).addObject("project_name_rm_support",project_name_rm_support)
		.addObject("project_flag", SysUtil.project_flag);
		List<String> lstTestlineName=testlinkHandler.getTestlinkProjectNames();
		mv.addObject("lstTestlinkName",lstTestlineName);
		List<String> lstRedmineName=redmineCommon.getRedmineProjectNames();
		mv.addObject("lstRedmineName",lstRedmineName);
		List<String> lstRedmineSupportName=redmineCommon.getRedmineSupportProjectNames();
		mv.addObject("lstRedmineSupportName", lstRedmineSupportName);
		return mv;
	}
	
	@RequestMapping("/addModuleProject")
	public ModelAndView addProjectModule(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		mv.addObject("project_name","");
		List<String> lstTestlineName=testlinkHandler.getTestlinkProjectNames();
		mv.addObject("lstTestlinkName",lstTestlineName);
		List<String> lstRedmineName=redmineCommon.getRedmineProjectNames();
		mv.addObject("lstRedmineName",lstRedmineName);
		List<String> lstRedmineSupportName=redmineCommon.getRedmineSupportProjectNames();
		mv.addObject("lstRedmineSupportName", lstRedmineSupportName);
		mv.setViewName("add_module_project");
		return mv;
	}
	
	@RequestMapping("/saveNewModuleProject")
	//保存项目模块编辑结果
	public ModelAndView saveNewModuleProject(HttpServletRequest req)
	{
		ModelAndView mv = new ModelAndView();
		String project_name = req.getParameter("project_name");
		String project_name_tl = req.getParameter("testlinkName");
		String project_name_rm = req.getParameter("redmineName");
		String project_name_rm_support = req.getParameter("redmineSupportName");
		String[] topsuites_testlinks = req.getParameterValues("topsuite_testlink");
		
		if(projectService.isProjectExist(project_name))
		{
			List<String> lstTestlineName=testlinkHandler.getTestlinkProjectNames();
			mv.addObject("lstTestlinkName",lstTestlineName);
			List<String> lstRedmineName=redmineCommon.getRedmineProjectNames();
			mv.addObject("lstRedmineName",lstRedmineName);
			List<String> lstRedmineSupportName=redmineCommon.getRedmineSupportProjectNames();
			mv.addObject("lstRedmineSupportName", lstRedmineSupportName);
			List<String> lstTestlinkSuiteName=testlinkForSuiteHandler.getSuiteNamesByTestprojectName(project_name_tl);
			mv.addObject("lstTestlinkSuiteName", lstTestlinkSuiteName);
			List<String> lstRedmineCategoryName=redmineCommon.getCategoryNamesByProjectName(project_name_rm);
			mv.addObject("lstRedmineCategoryName", lstRedmineCategoryName);
			mv.setViewName("add_module_project");
			mv.addObject("saveResult","模块名称重复！");
			return mv;
		}
		
		//suiteFilter选择了top suite，其下的二级目录剔除
		String suite_name_tl=SysUtil.suiteFilter(topsuites_testlinks);
		suite_name_tl=suite_name_tl.substring(1,suite_name_tl.length()-1);
		suite_name_tl=suite_name_tl.replaceAll(",",SysUtil.splitFlag);
		ProjectDto project = projectService.createNewProject();
		Integer project_id = project.getProject_id();
		String[] categories_redmine = req.getParameterValues("category_redmine");
		String category_name_rm=Arrays.toString(categories_redmine);
		category_name_rm=category_name_rm.substring(1,category_name_rm.length()-1);
		category_name_rm=category_name_rm.replaceAll(",",SysUtil.splitFlag);
		
		if(projectService.updateProjectNameById(project_id,project_name) &&
				projectService.updateProjectSourceNames(project_id,project_name_tl,project_name_rm,"")
				&& projectService.updateRedmineSupportName(project_id,project_name_rm_support)
				&& projectService.updateRedmineCategoryName(project_id, category_name_rm)
				&& projectService.updateTestlinkSuiteName(project_id, suite_name_tl)
				&& projectService.updateProjectFlagById(project_id, SysUtil.module_flag)){
			mv.addObject("updateResult","添加成功！");
			mv.setViewName("redirect:projectlist");
			return mv;
		}else{
			mv.addObject("updateResult","添加失败！");
			List<String> lstTestlineName=testlinkHandler.getTestlinkProjectNames();
			mv.addObject("lstTestlinkName",lstTestlineName);
			List<String> lstRedmineName=redmineCommon.getRedmineProjectNames();
			mv.addObject("lstRedmineName",lstRedmineName);
			List<String> lstRedmineSupportName=redmineCommon.getRedmineSupportProjectNames();
			mv.addObject("lstRedmineSupportName", lstRedmineSupportName);
			List<String> lstTestlinkSuiteName=testlinkForSuiteHandler.getSuiteNamesByTestprojectName(project_name_tl);
			mv.addObject("lstTestlinkSuiteName", lstTestlinkSuiteName);
			List<String> lstRedmineCategoryName=redmineCommon.getCategoryNamesByProjectName(project_name_rm);
			mv.addObject("lstRedmineCategoryName", lstRedmineCategoryName);
			mv.setViewName("add_module_project");
			return mv;
		}
	}
}