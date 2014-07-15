package com.env.qualitymetrics.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.hibernate.Session;
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
	public ModelAndView sprintlist(HttpServletRequest req){
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
		req.setAttribute("lstSprintDto", sprintList);
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
		return mv;
	}
	
	
	
	@RequestMapping("/updateSprint")
	//编辑Sprint信息
	public ModelAndView updateSprint(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("updateSprint");
		HttpSession httpSession = (HttpSession)req.getSession();
		
		Integer sprint_id = Integer.parseInt(req.getParameter("sprint_id"));
		int project_flag=Integer.parseInt(req.getParameter("role"));
		SprintDto sprint = sprintService.getSprintById(sprint_id);
		mv.addObject("sprint",sprint);
		String project_name="";
		String sprintName="";
		try {
			sprintName=URLDecoder.decode(URLDecoder.decode(req.getParameter("sprintName"),"UTF-8"),"UTF-8");
			project_name=URLDecoder.decode(URLDecoder.decode(req.getParameter("project_name"),"UTF-8"),"UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		int project_id=sprint.getProject_id();
		mv.addObject("project_id", project_id);
		mv.addObject("project_name", project_name);
		mv.addObject("sprintName",sprintName);
		
		Boolean isAdmin=(Boolean)(httpSession.getAttribute("isAdmin"));
		List<Integer> lstProjectID=(List<Integer>)(httpSession.getAttribute("lstProjectID"));
		int role =(Integer)(httpSession.getAttribute("role"));
		if(!isAdmin && !isProjectOwner(lstProjectID, project_id))  //非系统管理员,非本项目人员不能添加Sprint信息
		{
			mv.setViewName("error");
			return mv;
		}
		
		String[] arrSelectedTestPlan = sprint.getTestplan_testlink().split("<br>");
		List<String> lstSelectedTestPlan=new ArrayList<String>();
		for(int i=0;i<arrSelectedTestPlan.length;i++)
		{
			lstSelectedTestPlan.add(arrSelectedTestPlan[i].trim());
		}
		
		String[] arrSelectedRedmine = sprint.getVersion_redmine().split("<br>");
		List<String> lstSelectedRedmine=new ArrayList<String>();
		for(int i=0;i<arrSelectedRedmine.length;i++)
		{
			lstSelectedRedmine.add(arrSelectedRedmine[i].trim());
		}
		
		String[] arrSelectedSurveyMonkey=sprint.getUrl_surveymonkey().split("<br>");
		List<String> lstSelectedSurveyMonkey=new ArrayList<String>();
		for(int i=0;i<arrSelectedSurveyMonkey.length;i++)
		{
			lstSelectedSurveyMonkey.add(arrSelectedSurveyMonkey[i].trim());
		}
		
		String project_name_tl = projectService.getProjectSourceNamesById(project_id,project_flag).getProject_name_tl();
		List<String> lstTestPlan = testlinkHandler.getProjectTestPlan(project_name_tl);
		for(int i=0;i<lstSelectedTestPlan.size();i++)
		{
			for(int j=0;j<lstTestPlan.size();j++)
			{
				if(lstSelectedTestPlan.get(i).equals((String)lstTestPlan.get(j)))
				{
					lstTestPlan.set(j, lstTestPlan.get(j)+"#selected");
					break;
				}
			}
		}
		mv.addObject("lstTestPlan", lstTestPlan);
		
		
		String project_name_rm = projectService.getProjectSourceNamesById(project_id,project_flag).getProject_name_rm();
		int redmine_project_id=this.redmineCommon.getProjectId(project_name_rm);
		List<String> lstRedmine=this.redmineCommon.getVersionNames(redmine_project_id);
		for(int i=0;i<lstSelectedRedmine.size();i++)
		{
			for(int j=0;j<lstRedmine.size();j++)
			{
				if(lstSelectedRedmine.get(i).equals((String)lstRedmine.get(j)))
				{
					lstRedmine.set(j, lstRedmine.get(j)+"#selected");
					break;
				}
			}
		}
		mv.addObject("lstRedmine",lstRedmine);
		
		List<String> lstSonar=sonarHandler.getNames();
		mv.addObject("lstSonar",lstSonar);
 
		List<String> lstTitle=surveyMonkeyHandler.getSurveyMonkeyTitles();
		
		
		for(int i=0;i<lstSelectedSurveyMonkey.size();i++)
		{
			for(int j=0;j<lstTitle.size();j++)
			{
				if(lstSelectedSurveyMonkey.get(i).equals((String)lstTitle.get(j)))
				{
					lstTitle.set(j, lstTitle.get(j)+"#selected");
					break;
				}
			}
		}
		mv.addObject("lstTitle",lstTitle);
		
		//修改时列出已有构建==zhangdi 140504====
		String strBuilds = sprint.getSprint_build();
		if(strBuilds!=null){
			String arrBuild[] = strBuilds.split("<br>");
			mv.addObject("arrBuild",arrBuild);
		}
		mv.addObject("project_flag", project_flag);
		return mv;
	}
	
	@RequestMapping("/saveNewSprint")
	//保存Sprint编辑信息
	public ModelAndView saveNewSprint(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		Integer project_id = Integer.parseInt(req.getParameter("project_id"));
		HttpSession httpSession=(HttpSession)req.getSession();
		Boolean isAdmin=(Boolean)httpSession.getAttribute("isAdmin");
		List<Integer> lstProjectID=(List<Integer>)httpSession.getAttribute("lstProjectID"); 
		
		if(!isAdmin && !lstProjectID.contains(project_id))
		{
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
		mv.setViewName("redirect:sprintlist");
		List<SprintDto> sprintlist = sprintService.getSprintsByProjectId(project_id);
		mv.addObject("sprintlist",sprintlist);
		mv.addObject("project_id",project_id);
		mv.addObject("project_name",project_name);
		mv.addObject("pageNumber",1);
		return mv;
	}
	
	@RequestMapping("/saveUpdateSprint")
	//保存Sprint编辑信息
	public ModelAndView saveUpdateSprint(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		Integer project_id = Integer.parseInt(req.getParameter("project_id"));
		HttpSession httpSession=(HttpSession)req.getSession();
		Boolean isAdmin=(Boolean)httpSession.getAttribute("isAdmin");
		List<Integer> lstProjectID=(List<Integer>)httpSession.getAttribute("lstProjectID"); 
		
		if(!isAdmin && !lstProjectID.contains(project_id))
		{
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
		mv.setViewName("redirect:sprintlist");
		List<SprintDto> sprintlist = sprintService.getSprintsByProjectId(project_id);
		mv.addObject("sprintlist",sprintlist);
		mv.addObject("project_id",project_id);
		mv.addObject("project_name",project_name);
		mv.addObject("pageNumber",1);
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