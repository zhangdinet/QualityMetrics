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
	
	@Resource(name="testlinkForSuiteHandler")
	TestLinkForSuiteHandler testlinkForSuiteHandler;
	
	@Resource(name="quarterTask")
	private QuarterTask quarterTask;
	
	@Resource(name="redmineCommon")
	RedmineCommon redmineCommon;
	
	@Resource(name="sonarHandler")
	SonarHandler sonarHandler;
	
	@Resource(name="surveyMonkeyHandler")
	SurveyMonkeyHandler surveyMonkeyHandler;
	
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
	
	@RequestMapping("/showNewestRankings")
	//显示最新的质量龙虎榜排名
	public ModelAndView showNewestRankings(HttpServletRequest req){
		List<ProjectDto> projectList = projectService.getNewestRankList();
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("projectList", projectList);
		mv.addObject("rank_id", 0);
		mv.setViewName("rankings");
		return mv;
	}
	
	@RequestMapping("/showSelectedRankings")
	//显示往期排名
	public ModelAndView  showSelectedRankings(HttpServletRequest req){
		Integer rank_id = Integer.parseInt(req.getParameter("rankId"));
		List<ProjectDto> projectList = rankingService.getSelectedRankList(rank_id);
		ModelAndView mv = new ModelAndView();
		mv.addObject("projectList", projectList);
		mv.addObject("rank_id", rank_id);
		mv.setViewName("rankings");
		return  mv;
	}
	
	@RequestMapping("showRankings")
	public ModelAndView showRankings(HttpServletRequest req)
	{
		Integer rankID = Integer.parseInt(req.getParameter("rankId"));
		List<ProjectDto> projectList;
		if(rankID==0) //往期可能为零？？
		{
			projectList = projectService.getNewestRankList();
		}
		else
		{
			projectList = rankingService.getSelectedRankList(rankID);
		}
		ModelAndView mv = new ModelAndView();
		mv.addObject("projectList", projectList);
		mv.addObject("rank_id", rankID);
		mv.setViewName("rankings");
		return  mv;
	}

	@RequestMapping("showRankingChart")
	public void showRankingChart(HttpServletRequest req,HttpServletResponse res)throws IOException
	{
		Integer rankID = Integer.parseInt(req.getParameter("rankId"));
		PrintWriter out=null;
		ModelAndView mv = new ModelAndView();
		List<ProjectDto> projectList;
		if(rankID==0) //往期可能为零？？
		{
			projectList = projectService.getNewestRankList();
		}
		else
		{
			projectList = rankingService.getSelectedRankList(rankID);
		}
		if(projectList.size()!=0){
			JSONArray jsonArray=JSONArray.fromObject(projectList);
			out = res.getWriter();
			//out.print(jsonArray.toString());
			out.write(jsonArray.toString());
			if(out!=null)
			{
				out.close();
			}
		}
	}
	
	@RequestMapping("/showProjectDetail")
	//显示项目详情
	public ModelAndView showProjectDetail(HttpServletRequest req){
		Integer project_id = Integer.parseInt(req.getParameter("project_id"));
		String project_name = req.getParameter("project_name");
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
		String project_name = req.getParameter("project_name");
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
	
	@RequestMapping("/showSettingsSprint")
	//显示产品设置中的Sprint信息
	public ModelAndView showSettingsSprintDetail(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("settings_sprint");
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
	
	@RequestMapping("/showModifyProject")
	//编辑产品信息
	public ModelAndView showModifyProjectDetail(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("modify_projectdetail");
		String project_name = req.getParameter("project_name");
		Integer project_id = Integer.parseInt(req.getParameter("project_id"));
		ProjectDto projectDto=this.projectService.getProjectSourceNamesById(project_id,SysUtil.project_flag);
		String project_name_tl = projectDto.getProject_name_tl();//req.getParameter("project_name_tl");
		String project_name_rm = projectDto.getProject_name_rm();//req.getParameter("project_name_rm");
		String project_name_sn = projectDto.getProject_name_sn();//req.getParameter("project_name_sn");
		String project_name_rm_support = projectDto.getProject_name_rm_support();//req.getParameter("project_name_rm_support");
		mv.addObject("project_name", project_name).addObject("project_name_tl", project_name_tl)
		.addObject("project_name_rm", project_name_rm).addObject("project_name_sn", project_name_sn)
		.addObject("project_id", project_id).addObject("project_name_rm_support",project_name_rm_support)
		.addObject("project_flag", SysUtil.project_flag);
		//======================zhangdi  140416===============
				List<String> lstTestlineName=testlinkHandler.getTestlinkProjectNames();
				mv.addObject("lstTestlinkName",lstTestlineName);
				
				List<String> lstRedmineName=redmineCommon.getRedmineProjectNames();
				mv.addObject("lstRedmineName",lstRedmineName);
				
				List<String> lstRedmineSupportName=redmineCommon.getRedmineSupportProjectNames();
				mv.addObject("lstRedmineSupportName", lstRedmineSupportName);		
			 
		//====================================================
		return mv;
	}
	@RequestMapping("/showModifyProjectModule")
	//编辑产品信息
	public ModelAndView showModifyProjectModuleDetail(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("modify_project_moduledetail");
		String project_name = req.getParameter("project_name");
		Integer project_id = Integer.parseInt(req.getParameter("project_id"));
		ProjectDto projectDto=this.projectService.getProjectSourceNamesById(project_id,SysUtil.module_flag);
		String project_name_tl = projectDto.getProject_name_tl();//req.getParameter("project_name_tl");
		String project_name_rm = projectDto.getProject_name_rm();//req.getParameter("project_name_rm");
		String project_name_sn = projectDto.getProject_name_sn();//req.getParameter("project_name_sn");
		String project_name_rm_support = projectDto.getProject_name_rm_support();//req.getParameter("project_name_rm_support");
		String suite_name_tl=projectDto.getSuite_name_tl();
		String category_name_rm=projectDto.getCategory_name_rm();
		mv.addObject("project_name", project_name).addObject("project_name_tl", project_name_tl)
		.addObject("project_name_rm", project_name_rm).addObject("project_name_sn", project_name_sn)
		.addObject("project_id", project_id).addObject("project_name_rm_support",project_name_rm_support)
		.addObject("suite_name_tl", suite_name_tl).addObject("category_name_rm", category_name_rm)
		.addObject("project_flag", SysUtil.module_flag);
		//======================zhangdi  140416===============
				List<String> lstTestlineName=testlinkHandler.getTestlinkProjectNames();
				mv.addObject("lstTestlinkName",lstTestlineName);
				
				List<String> lstRedmineName=redmineCommon.getRedmineProjectNames();
				mv.addObject("lstRedmineName",lstRedmineName);
				
				List<String> lstRedmineSupportName=redmineCommon.getRedmineSupportProjectNames();
				mv.addObject("lstRedmineSupportName", lstRedmineSupportName);
			 
		//====================================================
				List<String> lstTestlinkSuiteName=testlinkForSuiteHandler.getSuiteNamesByTestprojectName(project_name_tl);
				mv.addObject("lstTestlinkSuiteName", lstTestlinkSuiteName);
				
				List<String> lstRedmineCategoryName=redmineCommon.getCategoryNamesByProjectName(project_name_rm);
				mv.addObject("lstRedmineCategoryName", lstRedmineCategoryName);
		return mv;
	}
	
	//==============zhangdi 140417============================
	@RequestMapping("/saveModifyProject")
	//保存编辑结果
	public ModelAndView saveModifyProject(HttpServletRequest req){
		String project_id_string = req.getParameter("project_id");
		Integer project_id = null;
		if(project_id_string.equals("0")){
			ProjectDto project = projectService.createNewProject();
			project_id = project.getProject_id();
		}else{
			project_id = Integer.parseInt(req.getParameter("project_id"));
		}

		Integer user_project_id = (Integer) req.getSession().getAttribute("project_id");
		if(user_project_id == null || (user_project_id != project_id && user_project_id != 0)){
			//非管理员，也不是本Project的管理人员
			ModelAndView mv = new ModelAndView();
			mv.setViewName("error");
			return mv;
		}
		
		String project_name = req.getParameter("project_name");
		String project_name_tl = req.getParameter("testlinkName");
		String project_name_rm = req.getParameter("redmineName");		
		String project_name_rm_support = req.getParameter("redmineSupportName");		
 
		ModelAndView mv = new ModelAndView();
		if(projectService.updateProjectNameById(project_id,project_name) &&
				projectService.updateProjectSourceNames(project_id,project_name_tl,project_name_rm,"")
				&& projectService.updateRedmineSupportName(project_id,project_name_rm_support)
				&& projectService.updateProjectFlagById(project_id, SysUtil.project_flag)){
			mv.addObject("updateResult","ok");
		}else{
			mv.addObject("updateResult","err");
		}
		mv.addObject("project_name", project_name).addObject("project_name_tl", project_name_tl)
		.addObject("project_name_rm", project_name_rm).addObject("project_name_sn", "")
		.addObject("project_id", project_id).addObject("project_name_rm_support", project_name_rm_support);
		//=================zhangdi 140421===============
		 //mv.setViewName("modify_projectdetail");
		mv.setViewName("mainframe");
		//=======================
		return mv;
	}
	
	@RequestMapping("/saveModifyProjectModule")
	//保存项目模块编辑结果
	public ModelAndView saveModifyProjectModule(HttpServletRequest req){
		String project_id_string = req.getParameter("project_id");
		Integer project_id = null;
		if(project_id_string.equals("0")){
			ProjectDto project = projectService.createNewProject();
			project_id = project.getProject_id();
		}else{
			project_id = Integer.parseInt(req.getParameter("project_id"));
		}

		Integer user_project_id = (Integer) req.getSession().getAttribute("project_id");
		if(user_project_id == null || (user_project_id != project_id && user_project_id != 0)){
			//非管理员，也不是本Project的管理人员
			ModelAndView mv = new ModelAndView();
			mv.setViewName("error");
			return mv;
		}
		
		String project_name = req.getParameter("project_name");
		String project_name_tl = req.getParameter("testlinkName");
		String project_name_rm = req.getParameter("redmineName");		
		String project_name_rm_support = req.getParameter("redmineSupportName");
		
		String[] topsuites_testlinks = req.getParameterValues("topsuite_testlink");
		//suiteFilter选择了top suite，其下的二级目录剔除
		String suite_name_tl=SysUtil.suiteFilter(topsuites_testlinks);//Arrays.toString(topsuites_testlinks);
		suite_name_tl=suite_name_tl.substring(1,suite_name_tl.length()-1);
		suite_name_tl=suite_name_tl.replaceAll(",",SysUtil.splitFlag);
		
		String[] categories_redmine = req.getParameterValues("category_redmine");
		String category_name_rm=Arrays.toString(categories_redmine);
		category_name_rm=category_name_rm.substring(1,category_name_rm.length()-1);
		category_name_rm=category_name_rm.replaceAll(",",SysUtil.splitFlag);		
 
		ModelAndView mv = new ModelAndView();
		if(projectService.updateProjectNameById(project_id,project_name) &&
				projectService.updateProjectSourceNames(project_id,project_name_tl,project_name_rm,"")
				&& projectService.updateRedmineSupportName(project_id,project_name_rm_support)
				&& projectService.updateRedmineCategoryName(project_id, category_name_rm)
				&& projectService.updateTestlinkSuiteName(project_id, suite_name_tl)
				&& projectService.updateProjectFlagById(project_id, SysUtil.module_flag)){
			mv.addObject("updateResult","ok");
		}else{
			mv.addObject("updateResult","err");
		}
		mv.addObject("project_name", project_name).addObject("project_name_tl", project_name_tl)
		.addObject("project_name_rm", project_name_rm).addObject("project_name_sn", "")
		.addObject("project_id", project_id).addObject("project_name_rm_support", project_name_rm_support)
		.addObject("suite_name_tl", suite_name_tl).addObject("category_name_rm", category_name_rm);
		
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
		
		mv.setViewName("modify_project_moduledetail");
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
	
	@RequestMapping("/modifyPassword")
	public ModelAndView modifyPassword(HttpServletRequest req){
		String username = req.getParameter("username");
		String password = req.getParameter("newPassword");
		boolean changePwd = userService.changePassword(username,password);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("modify_password");
		if(changePwd){
			mv.addObject("modifyResult", "ok");
		}else{
			mv.addObject("modifyResult", "err");
		}
		return mv;
	}
	
	@RequestMapping("/addProject")
	public ModelAndView addProject(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		Integer user_project_id = (Integer) req.getSession().getAttribute("project_id");
		String flag_admin = (String) req.getSession().getAttribute("flag_admin");
		if(user_project_id == null || user_project_id != 0){
			//非系统管理员不能添加项目
			mv.setViewName("error");
			return mv;
		}
		mv.addObject("project_name", "");
		//======================zhangdi  140416===============
		List<String> lstTestlineName=testlinkHandler.getTestlinkProjectNames();
		mv.addObject("lstTestlinkName",lstTestlineName);
		
		List<String> lstRedmineName=redmineCommon.getRedmineProjectNames();
		mv.addObject("lstRedmineName",lstRedmineName);
		
		List<String> lstRedmineSupportName=redmineCommon.getRedmineSupportProjectNames();
		mv.addObject("lstRedmineSupportName", lstRedmineSupportName);
	 
		//====================================================
		mv.setViewName("modify_projectdetail");
		return mv;
	}
	
	@RequestMapping("/addProjectModule")
	public ModelAndView addProjectModule(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		Integer user_project_id = (Integer) req.getSession().getAttribute("project_id");
		String flag_admin = (String) req.getSession().getAttribute("flag_admin");
		if(user_project_id == null || user_project_id != 0){
			//非系统管理员不能添加项目
			mv.setViewName("error");
			return mv;
		}
		mv.addObject("project_name","");
		//======================zhangdi 140416===============
		List<String> lstTestlineName=testlinkHandler.getTestlinkProjectNames();
		mv.addObject("lstTestlinkName",lstTestlineName);
		
		List<String> lstRedmineName=redmineCommon.getRedmineProjectNames();
		mv.addObject("lstRedmineName",lstRedmineName);
		
		List<String> lstRedmineSupportName=redmineCommon.getRedmineSupportProjectNames();
		mv.addObject("lstRedmineSupportName", lstRedmineSupportName);
		//====================================================
		mv.setViewName("modify_project_moduledetail");
		return mv;
	}
	
	@RequestMapping("/addSprint")
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
	
	@RequestMapping("/printRankings")
	public ModelAndView printRankings(HttpServletRequest req){
		Integer rank_id = Integer.parseInt(req.getParameter("selectedPeriodId"));
		String rank_period;
		List<ProjectDto> projectList;
		if(rank_id==0)
		{
			projectList = projectService.getNewestRankList();
			rank_period="当前";
		}
		else
		{
			rank_period = req.getParameter("selectedPeriodName");
			projectList = rankingService.getSelectedRankList(rank_id);
		}
		ModelAndView mv = new ModelAndView();
		mv.addObject("projectList", projectList);
		mv.addObject("rank_period",rank_period);
		mv.setViewName("rankings_print");
		return mv;
	}
	
	
	/***
	 * 显示用户列表，进行用户管理
	 * @return
	 */
	
	
	//======zhangdi todo 多个项目======
	//todo
	//================================
	
	

	@RequestMapping("/resetPwd")
	public void resetPwd(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		resp.setContentType("text/plain");
		int user_id = Integer.parseInt(req.getParameter("user_id"));
		PrintWriter pw = resp.getWriter();
		userService.resetPwd(user_id);
		pw.write("ok");
	}
	
	@RequestMapping("/deleteUser")
	public void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		resp.setContentType("text/plain");
		PrintWriter pw = resp.getWriter();
		int user_id = Integer.parseInt(req.getParameter("user_id"));
		userService.deleteUserById(user_id);
		pw.write("ok");
	}
	
	@RequestMapping("/addUser")
	public ModelAndView addUser(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		Integer project_id = (Integer) req.getSession().getAttribute("project_id");
		String flag_admin =  (String) req.getSession().getAttribute("flag_admin");
		if(flag_admin.equals("yes") && project_id==0){
			mv.addObject("project_name","");
			mv.addObject("user_project_id","-1");//新增，设置传入的project_id和user_id为-1，保存时进行判断
			mv.addObject("flag_admin","0");//flag_admin默认为0
			mv.addObject("user_name","");
			mv.addObject("user_id","-1");
			mv.setViewName("redirect:/showModifyUsers");
			return mv;
		}else{
			mv.setViewName("error");
			return mv;
		}
		
	}
	
	@RequestMapping("/updateRankings")
	public ModelAndView updateRankings(){
		quarterTask.CollectRankings();
		return null;
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
