package com.env.qualitymetrics.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.catalina.tribes.util.Arrays;
import java.util.Arrays;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
	

	@RequestMapping("/saveModifySprint")
	//保存Sprint编辑信息
	public ModelAndView saveModifySprint(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		Integer project_id = Integer.parseInt(req.getParameter("project_id"));
		Integer project_flag = Integer.parseInt(req.getParameter("project_flag"));
		Integer user_project_id = (Integer) req.getSession().getAttribute("project_id");;
		if(user_project_id == null || (user_project_id != project_id && user_project_id != 0)){
			//非管理员，也不是本Project的管理人员
			return null;
		}
		String sprint_name = req.getParameter("sprint_name");
		String[] testplan_testlinks = req.getParameterValues("testplan_testlink");
		String testplan_testlink=Arrays.toString(testplan_testlinks);
		testplan_testlink=testplan_testlink.substring(1,testplan_testlink.length()-1);
		testplan_testlink=testplan_testlink.replaceAll(",","<br>");  //考虑js端替换  ===zhangdi== todo ===
		String[] version_redmines = req.getParameterValues("version_redmine");
		String version_redmine=Arrays.toString(version_redmines);
		version_redmine=version_redmine.substring(1,version_redmine.length()-1);
		version_redmine=version_redmine.replaceAll(",","<br>");
		
		String sprint_startdate = req.getParameter("sprint_startdate");
		String sprint_enddate = req.getParameter("sprint_enddate");
		
		String ipd_score = req.getParameter("ipd_score");
		String lmt_score = req.getParameter("lmt_score");
		String[] url_surveymonkeys = req.getParameterValues("url_surveymonkey");
		String url_surveymonkey=Arrays.toString(url_surveymonkeys);
		url_surveymonkey=url_surveymonkey.substring(1,url_surveymonkey.length()-1);
		url_surveymonkey=url_surveymonkey.replaceAll(",", "<br>");
		Integer sprint_id = null;
		if(req.getParameter("sprint_id").equals("")){
			SprintDto sprint = sprintService.createNewSprintByProjectId(project_id,sprint_name);
			sprint_id = sprint.getSprint_id();
		}else{
			sprint_id= Integer.parseInt(req.getParameter("sprint_id"));
		}
		String[] selected_builds = req.getParameterValues("selected_builds");
		String strBuilds=Arrays.toString(selected_builds);
		strBuilds=strBuilds.substring(1,strBuilds.length()-1);
		strBuilds = strBuilds.replaceAll(",","<br>");
		SprintDto sprintDto = new SprintDto();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		boolean update = false;
		
		try {
			Date startDate = fmt.parse(sprint_startdate);
			Date endDate = fmt.parse(sprint_enddate);
			sprintDto.setSprint_build(strBuilds);
			sprintDto.setSprint_name(sprint_name);
			sprintDto.setTestplan_testlink(testplan_testlink);
			sprintDto.setVersion_redmine(version_redmine);
			//sprintDto.setBuild_sonar(build_sonar);
			sprintDto.setSprint_startdate(startDate);
			sprintDto.setSprint_enddate(endDate);
			//sprintDto.setSprint_builddate(buildDate);
			if(ipd_score!=null){
				sprintDto.setIpd_score(Float.parseFloat(ipd_score));
				sprintDto.setLmt_score(-1);
			}
			if(lmt_score!=null){
				sprintDto.setLmt_score(Float.parseFloat(lmt_score));
				sprintDto.setIpd_score(-1);
			}
			sprintDto.setUrl_surveymonkey(url_surveymonkey);
			sprintDto.setSprint_id(sprint_id);
			update = sprintService.updateSprintDetail(sprintDto);
			if(update){
				mv.addObject("updateResult","ok");
			}else{
				mv.addObject("updateResult","err");
			}
		} catch (ParseException e) {
			mv.addObject("updateResult","日期为空！");
		}
		String project_name = req.getParameter("project_name");
		mv.addObject("sprint",sprintDto);
		mv.addObject("project_name", project_name);
		mv.addObject("project_flag", project_flag);
		mv.setViewName("mainframe");
		return mv;
	}
	
	
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
