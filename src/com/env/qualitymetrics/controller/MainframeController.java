package com.env.qualitymetrics.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.env.qualitymetrics.common.SysUtil;
import com.env.qualitymetrics.dto.Page;
import com.env.qualitymetrics.dto.ProjectDto;
import com.env.qualitymetrics.dto.RankingDto;
import com.env.qualitymetrics.dto.SprintDto;
import com.env.qualitymetrics.dto.UserDto;
import com.env.qualitymetrics.dto.WeightDto;
import com.env.qualitymetrics.service.ProjectService;
import com.env.qualitymetrics.service.RankingService;
import com.env.qualitymetrics.service.SprintService;
import com.env.qualitymetrics.service.UserService;
import com.env.qualitymetrics.service.WeightService;
import com.env.qualitymetrics.tasks.QuarterTask;
import com.env.qualitymetrics.tool.RedmineCommon;
import com.env.qualitymetrics.tool.SonarHandler;
import com.env.qualitymetrics.tool.SurveyMonkeyHandler;
import com.env.qualitymetrics.tool.TestLinkForSuiteHandler;
import com.env.qualitymetrics.tool.TestLinkHandler;

@Controller
public class MainframeController {
	@Resource(name="projectService")
	private ProjectService projectService;
	
	@Resource(name="rankingService")
	private RankingService rankingService;
	
	@Resource(name="sprintService")
	private SprintService sprintService;
	
	@Resource(name="userService")
	private UserService userService;
	
	@Resource(name="weightService")
	private WeightService weightService;
	
	@Resource(name="testlinkHandler")
	TestLinkHandler testlinkHandler;
	
	@Resource(name="sonarHandler")
	SonarHandler sonarHandler;
	
	@Resource(name="surveyMonkeyHandler")
	SurveyMonkeyHandler surveyMonkeyHandler;
	
	@Resource(name="testlinkForSuiteHandler")
	TestLinkForSuiteHandler testlinkForSuiteHandler;
	
	@Resource(name="quarterTask")
	private QuarterTask quarterTask;
	
	@Resource(name="redmineCommon")
	RedmineCommon redmineCommon;
	

	
	private static final Logger log = LoggerFactory.getLogger(MainframeController.class);
	
	@RequestMapping("/mainframe")
	//显示主界面
	public ModelAndView showMainframe(HttpServletRequest req)
	{
		String menuIndex=req.getParameter("menuIndex");
		if(menuIndex==null)
		{
			menuIndex="-1";
		}
		
		ModelAndView mv = new ModelAndView();
		List<RankingDto> rankingList = rankingService.getRankingPeriodList();
		mv.addObject("rankingList",rankingList);
		mv.addObject("menuIndex", menuIndex);
		mv.setViewName("mainframe");
		return mv;
	}

	
	@RequestMapping("/backToMainframe")
	//返回主页
	public ModelAndView backToMainframe(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		//mv.setViewName("redirect:mainframe");
		mv.setViewName("mainframe");
		String project_id = req.getParameter("project_id");
		if(project_id!=null)
		{
			String project_name=req.getParameter("project_name");
			mv.addObject("project_id",project_id);
			mv.addObject("project_name",project_name);
		}
		return mv;
	}
	
	
	 
	
	@RequestMapping("/showProjectDetail")
	//显示项目详情
	public ModelAndView showProjectDetail(HttpServletRequest req){
		Integer project_id = Integer.parseInt(req.getParameter("project_id"));
		String project_name = SysUtil.decodeUtf8(req.getParameter("project_name"));
		String thisYearDetail;
		float rate_patch;
		List<SprintDto> sprintDtoList = sprintService.getSprintsByProjectId(project_id);
		ModelAndView mv = new ModelAndView();
		mv.addObject("sprintList",sprintDtoList);
		HttpSession session = req.getSession();
		session.setAttribute("project_name", project_name);
		mv.addObject("project_id", project_id);
		mv.addObject("rank_id", 0);
		mv.setViewName("projectdetail");
		return mv;
	}
	
	@RequestMapping("/showProjectHistoryDetail")
	//显示项目历史详情
	public ModelAndView showProjectHistoryDetail(HttpServletRequest req){
		Integer project_id = Integer.parseInt(req.getParameter("project_id"));
		String project_name =SysUtil.decodeUtf8(req.getParameter("project_name"));
		Integer rank_id = Integer.parseInt(req.getParameter("rank_id"));
		String thisYearDetail;
		float rate_patch;
		List<SprintDto> sprintDtoList = sprintService.getSprintsHistoryByProjectIdRankId(project_id, rank_id);
		ModelAndView mv = new ModelAndView();
		mv.addObject("sprintList",sprintDtoList);
		mv.addObject("rank_id", rank_id);
		HttpSession session = req.getSession();
		session.setAttribute("project_name", project_name);
		mv.addObject("project_id", project_id);
		mv.setViewName("projectdetail");
		return mv;
	}
	
	@RequestMapping("/showSprintChart")
	//显示项目详情
	public void showSprintChart(HttpServletRequest req,HttpServletResponse res)throws IOException{
		Integer project_id = Integer.parseInt(req.getParameter("project_id"));
		Integer rank_id = Integer.parseInt(req.getParameter("rank_id"));
		PrintWriter out=null;
		List<SprintDto> sprintDtoList;
		if(rank_id==0){
			sprintDtoList = sprintService.getSprintsByProjectId(project_id);
		}else{
			sprintDtoList = sprintService.getSprintsHistoryByProjectIdRankId(project_id, rank_id);
		}
		if(sprintDtoList.size()!=0){
			JSONArray jsonArray=JSONArray.fromObject(sprintDtoList);
			out = res.getWriter();
			//out.print(jsonArray.toString());
			out.write(jsonArray.toString());
			if(out!=null)
			{
				out.close();
			}
		}
	}
	@RequestMapping("/showSettingsProject")
	//显示产品设置中的项目信息
	public ModelAndView showSettingsProjectDetail(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("settings_project");
		List<ProjectDto> projectList = projectService.getAllProjectsDetail();
		for(ProjectDto pr:projectList)
			System.out.println(pr.getProject_name_tl());
		mv.addObject("projectList",projectList);
		return mv;
	}
	
	@RequestMapping("/showModifySprint")
	//编辑Sprint信息
	public ModelAndView showModifySprint(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("modify_sprintdetail");
		Integer sprint_id = Integer.parseInt(req.getParameter("sprint_id"));
		String project_name = req.getParameter("project_name");
		int project_flag=Integer.parseInt(req.getParameter("project_flag"));
		SprintDto sprint = sprintService.getSprintById(sprint_id);
		mv.addObject("sprint",sprint);
		mv.addObject("project_name", project_name);
		int project_id=sprint.getProject_id();
		mv.addObject("project_id", project_id);
		
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
	
	@RequestMapping("/addSprintzd")
	public ModelAndView addSprint(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		Integer project_id = Integer.parseInt(req.getParameter("project_id"));
		Integer user_project_id = (Integer) req.getSession().getAttribute("project_id");
		int project_flag=Integer.parseInt(req.getParameter("project_flag"));
		if(user_project_id == null || (user_project_id != project_id && user_project_id != 0)){
			//非系统管理员,非本项目人员不能添加Sprint信息
			mv.setViewName("error");
			return mv;
		}
		String project_name = req.getParameter("project_name");
		mv.setViewName("modify_sprintdetail");
		mv.addObject("project_name", project_name);
		mv.addObject("project_id", project_id);
		mv.addObject("project_flag", project_flag);
		//==========zhangdi 140418======todo 考虑重构==========================================================
		String project_name_tl = projectService.getProjectSourceNamesById(project_id,project_flag).getProject_name_tl();	
		List<String> lstTestPlan = testlinkHandler.getProjectTestPlan(project_name_tl);
		mv.addObject("lstTestPlan", lstTestPlan);
		//===================zhangdi 140421====
		String project_name_rm = projectService.getProjectSourceNamesById(project_id,project_flag).getProject_name_rm();
		int redmine_project_id=this.redmineCommon.getProjectId(project_name_rm);
		List<String> lstRedmine=this.redmineCommon.getVersionNames(redmine_project_id);
		mv.addObject("lstRedmine",lstRedmine);
		//=================zhangdi 140421 ====
		List<String> lstSonar=sonarHandler.getNames();
		mv.addObject("lstSonar",lstSonar);
		//================zhangdi 140421====
		List<String> lstTitle=surveyMonkeyHandler.getSurveyMonkeyTitles();
		mv.addObject("lstTitle",lstTitle);
		//============================
		return mv;
	}
	
	@RequestMapping("/checkTestLinkName")
	public void checkTestlinkName(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		String testlink_name = req.getParameter("testlink_name");
		int testlink_id = testlinkHandler.getTestProjectIdByName(testlink_name);
		if(testlink_id == -1){
			writer.write("error");
		}else{
			writer.write("ok");
		}
	}
	
	@RequestMapping("/checkRedmineName")
	public void checkRedmineName(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		String redmine_name = req.getParameter("redmine_name");
		int redmine_id = redmineCommon.getProjectId(redmine_name);
		if(redmine_id == -1){
			writer.write("error");
		}else{
			writer.write("ok");
		}
	}
	
	@RequestMapping("/checkSonarName")
	public void checkSonarName(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		String sonar_name = req.getParameter("sonar_name");
		int sonar_id = sonarHandler.getProjectId(sonar_name); //zhangdi 140425
		if(sonar_id == -1){
			writer.write("error");
		}else{
			writer.write("ok");
		}
	}
	
	@RequestMapping("/checkRmSupportName")
	public void checkRmSupportName(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		String redmine_support_name = req.getParameter("redmine_support_name");
		int redmine_id = this.redmineCommon.getSupportId(redmine_support_name);
		if(redmine_id == -1){
			writer.write("error");
		}else{
			writer.write("ok");
		}
	}
	
	
	//======zhangdi 140428====
	@RequestMapping("/getSonarProjectIDByName")
	public void getSonarProjectIDByName(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{ 
		resp.setContentType("text/plain");
		PrintWriter writer=resp.getWriter();
		String sonarProjectName=req.getParameter("sonarProjectName");
		int sonarID=this.sonarHandler.getProjectId(sonarProjectName);
		if(sonarID==-1)
		{
			writer.write("error");
		}
		else
		{
			writer.write("ok&"+sonarID);
		}
	}
	
	//========================
	@RequestMapping("/getBuildDates")
	public void getBuildDates(HttpServletRequest req,HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/plain");
		PrintWriter writer=resp.getWriter();
		String sonarProjectID=req.getParameter("sonarProjectID");
		List<String> buildDates=this.sonarHandler.getBuildDatesByID(sonarProjectID);
		String[] arrBuildDates=new String[buildDates.size()];
		for(int i=0;i<buildDates.size();i++)
		{
			arrBuildDates[i]=buildDates.get(i);
		}
		String strBuildDates = Arrays.toString(arrBuildDates);
		strBuildDates = strBuildDates.substring(1,strBuildDates.length()-1);
		if(strBuildDates==null || strBuildDates=="")
		{
			
		}
		writer.write(strBuildDates);
	}
	//========================
	
	@RequestMapping("/getSuiteNames")
	public void getSuiteNames(HttpServletRequest req,HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter writer=resp.getWriter();
		String projectName=req.getParameter("testProjectName");
		List<String> suiteNames=this.testlinkForSuiteHandler.getSuiteNamesByTestprojectName(projectName);
		String[] arrSuiteNames=new String[suiteNames.size()];
		for(int i=0;i<suiteNames.size();i++)
		{
			arrSuiteNames[i]=suiteNames.get(i);
		}
		String strSuiteNames = Arrays.toString(arrSuiteNames);
		System.out.println(strSuiteNames);
		strSuiteNames = strSuiteNames.substring(1,strSuiteNames.length()-1);
		writer.write(strSuiteNames);
	}
	
	@RequestMapping("/getCategoryNames")
	public void getCategoryNames(HttpServletRequest req,HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter writer=resp.getWriter();
		String projectName=req.getParameter("projectName");
		List<String> categoryNames=this.redmineCommon.getCategoryNamesByProjectName(projectName);
		String[] arrcategoryNames=new String[categoryNames.size()];
		for(int i=0;i<categoryNames.size();i++)
		{
			arrcategoryNames[i]=categoryNames.get(i);
		}
		String strcategoryNames = Arrays.toString(arrcategoryNames);
		System.out.println(strcategoryNames);
		strcategoryNames = strcategoryNames.substring(1,strcategoryNames.length()-1);
		writer.write(strcategoryNames);
	}
	
	
	
	
	/***
	 * 显示用户列表，进行用户管理
	 * @return
	 */

	@RequestMapping("/resetPwd")
	public void resetPwd(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		resp.setContentType("text/plain");
		int user_id = Integer.parseInt(req.getParameter("user_id"));
		PrintWriter pw = resp.getWriter();
		userService.resetPwd(user_id);
		pw.write("ok");
	}
	

	
	@RequestMapping("/updateUser")
	public ModelAndView updateUser(HttpServletRequest req)
	{
		ModelAndView mv = new ModelAndView();
		Boolean isAdmin=(Boolean)req.getSession().getAttribute("isAdmin");
		if(isAdmin)
		{
			List<ProjectDto> projectDto=projectService.getAllProjectsDetail();
			mv.addObject("lstProjectDto", projectDto);
			mv.setViewName("updateUser");
			String updateUsername=req.getParameter("user_name");
			String userID=req.getParameter("userID");
			mv.addObject("updateUsername",updateUsername);
			mv.addObject("userID", userID);
			return mv;
		}
		else
		{
			mv.setViewName("error");
			return mv;
		}
	}


	
	@RequestMapping("/showIndicatorWeight")
	//显示指标权重设置信息
	public ModelAndView showIndicatorWeight(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("indicator_weightlist");
		WeightDto weightDto = weightService.getWeights();
		mv.addObject("weightDto",weightDto);
		return mv;
	}
}
