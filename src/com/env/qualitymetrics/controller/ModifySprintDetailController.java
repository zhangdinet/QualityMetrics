package com.env.qualitymetrics.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//import org.apache.catalina.tribes.util.Arrays;
import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.env.qualitymetrics.dto.ProjectDto;
import com.env.qualitymetrics.dto.SprintDto;
import com.env.qualitymetrics.service.ProjectService;
import com.env.qualitymetrics.service.SprintService;
import com.env.qualitymetrics.tool.RedmineCommon;
import com.env.qualitymetrics.tool.SonarHandler;
import com.env.qualitymetrics.tool.SurveyMonkeyHandler;
import com.env.qualitymetrics.tool.TestLinkHandler;

@Controller
public class ModifySprintDetailController {

	@Resource(name="sprintService")
	private SprintService sprintService;
	
	@Resource(name="redmineCommon")
	RedmineCommon redmineCommon;
	
	@Resource(name="sonarHandler")
	SonarHandler sonarHandler;
	
	@Resource(name="surveyMonkeyHandler")
	SurveyMonkeyHandler surveyMonkeyHandler;
	
	@Resource(name="testlinkHandler")
	TestLinkHandler testlinkHandler;
	
	@Resource(name="projectService")
	private ProjectService projectService;
	
	
	@RequestMapping("/checkTestplanTestlinkName")  //===zhangdi 140418=======
	public void checkTestplanTestlinkName(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		String testplan_testlink = req.getParameter("testplan_testlink");
		Integer project_id = Integer.parseInt(req.getParameter("project_id"));
		Integer project_flag = Integer.parseInt(req.getParameter("project_flag"));
		String project_name_tl = projectService.getProjectSourceNamesById(project_id,project_flag).getProject_name_tl();
		String nameArray[] = testplan_testlink.split(",");
		for(int i=0;i<nameArray.length;i++){
			int testplan_testlink_id = this.testlinkHandler.getTestplanIdByName(project_name_tl, nameArray[i]);
			if(testplan_testlink_id == -1){
				writer.write("error");
				return;
			}
		}
		writer.write("ok");
	}
	
	@RequestMapping("/checkVersionRedmineName")
	public void checkVersionRedmineName(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		String version_redmine = req.getParameter("version_redmine");
		Integer project_id = Integer.parseInt(req.getParameter("project_id"));
		Integer project_flag = Integer.parseInt(req.getParameter("project_flag"));
		String project_name_rm = projectService.getProjectSourceNamesById(project_id,project_flag).getProject_name_rm();
		String nameArray[] = version_redmine.split(",");
		for(int i=0;i<nameArray.length;i++){
			int version_redmine_id = this.redmineCommon.getVersionId(project_name_rm, nameArray[i]);
			if(version_redmine_id == -1){
				writer.write("error");
				return;
			}
		}

		writer.write("ok");
	}

	@RequestMapping("/checkBuildSonarName")
	public void checkBuildSonarName(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		String build_sonar = req.getParameter("build_sonar"); 
		String sonarArray[] = build_sonar.split(",");
		for(int i=0;i<sonarArray.length;i++)
		{
			int id = this.sonarHandler.getProjectId(sonarArray[i]);
			if(id == -1)
			{
				writer.write("error");
				return;
			}
		}		
		writer.write("ok");
	}
	
	@RequestMapping("/checkUrlSuerveymonkey")
	public void checkUrlSuerveymonkey(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		String url_surveymonkey = req.getParameter("url_surveymonkey");
		String urlArray[] = url_surveymonkey.split(",");
		for(int i=0;i<urlArray.length;i++){
			String survey_id = this.surveyMonkeyHandler.getSurveyIdByTitle(urlArray[i]);
			if(survey_id == null){
				writer.write("error");
				return;
			}
		}		
		writer.write("ok");
	}
}
