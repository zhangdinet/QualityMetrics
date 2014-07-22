package com.env.qualitymetrics.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.MvcNamespaceHandler;

import com.env.qualitymetrics.dao.UserDaozd;
import com.env.qualitymetrics.dto.ProjectDto;
import com.env.qualitymetrics.dto.RankingDto;
import com.env.qualitymetrics.service.ProjectService;
import com.env.qualitymetrics.service.RankingService;
import com.env.qualitymetrics.service.SprintService;
import com.env.qualitymetrics.service.UserService;
import com.env.qualitymetrics.service.WeightService;
import com.env.qualitymetrics.dto.UserDtozd;
import com.env.qualitymetrics.service.UserServicezd;
import com.env.qualitymetrics.tasks.QuarterTask;
import com.env.qualitymetrics.tool.RedmineCommon;
import com.env.qualitymetrics.tool.SonarHandler;
import com.env.qualitymetrics.tool.SurveyMonkeyHandler;
import com.env.qualitymetrics.tool.TestLinkForSuiteHandler;
import com.env.qualitymetrics.tool.TestLinkHandler;

@Controller
public class RankingController {
	
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
	
	@RequestMapping("/ranklist")
	public ModelAndView ranklist(HttpServletRequest req)
	{
		ModelAndView mv = new ModelAndView();
		String strID=req.getParameter("rank_id");
		Integer rank_id=0;
		Integer selectID=-1;
		List<ProjectDto> projectList;
		if(strID == null)
		{
			projectList = projectService.getNewestRankList();
			mv.addObject("selectID",-1);
		}
		else if(strID.equals("0"))
		{
			mv.addObject("selectID",0);
			projectList = projectService.getNewestRankList();
		}
		else
		{
			rank_id = Integer.parseInt(req.getParameter("rank_id"));
			projectList = rankingService.getSelectedRankList(rank_id);
		}
		
		mv.addObject("projectList", projectList);
		mv.addObject("rank_id", rank_id);
		List<RankingDto> rankingList = rankingService.getRankingPeriodList();
		//List<RankingDto> rankingList=rankingService.getRankingPeriodById(rank_id);
		mv.addObject("rankingList",rankingList);
		req.setAttribute("projectList",projectList);
		mv.setViewName("ranklist");
		return mv;
	}
	
	@RequestMapping("/showRankings")
	public ModelAndView showRankings(HttpServletRequest req)
	{
		String strID=req.getParameter("rankId");
		Integer rank_id=0;
		if(strID!=null||strID!="")
		{
			rank_id = Integer.parseInt(strID);
		}
		List<ProjectDto> projectList;
		if(rank_id==0) //往期可能为零？？
		{
			projectList = projectService.getNewestRankList();
		}
		else
		{
			projectList = rankingService.getSelectedRankList(rank_id);
		}
		ModelAndView mv = new ModelAndView();
		mv.addObject("projectList", projectList);
		mv.addObject("rank_id", rank_id);
		mv.setViewName("ranklist");
		return mv;
	}

	@RequestMapping("showRankingChart")
	public void showRankingChart(HttpServletRequest req,HttpServletResponse res)throws IOException
	{
		res.setCharacterEncoding("UTF-8");
		String strID=req.getParameter("rank_id");
		if(strID==null || strID=="")
		{
			strID = "0";
		}
		Integer rank_id = Integer.parseInt(strID);
		PrintWriter out=null;
		ModelAndView mv = new ModelAndView();
		List<ProjectDto> projectList;
		if(rank_id==0) //往期可能为零？？
		{
			projectList = projectService.getNewestRankList();
		}
		else
		{
			projectList = rankingService.getSelectedRankList(rank_id);
		}
		if(projectList.size()!=0){
			JSONArray jsonArray=JSONArray.fromObject(projectList);
			out = res.getWriter();
			out.print(jsonArray.toString());
			if(out!=null)
			{
				out.close();
			}
		}
	}
	
	@RequestMapping("/printRankings")
	public ModelAndView printRankings(HttpServletRequest req){
		Integer rank_id = Integer.parseInt(req.getParameter("selectedPeriodId"));
		String filterName = req.getParameter("filterName");
		String rank_period;
		List<ProjectDto> projectList;
		List<ProjectDto> filterProjectList = new ArrayList<ProjectDto>();;
		
		if(rank_id==0 || rank_id==-1)
		{
			projectList = projectService.getNewestRankList();
			rank_period="最新";
		}
		else
		{
			rank_period = req.getParameter("selectedPeriodName");
			projectList = rankingService.getSelectedRankList(rank_id);
		}
		if(filterName!=null && filterName!="")
		{
			for(ProjectDto dto:projectList)
			{
				String projectName=dto.getProject_name().toLowerCase();
				if(projectName.contains(filterName))
				{
					filterProjectList.add(dto);
				}
			}
		}
		ModelAndView mv = new ModelAndView();
		mv.addObject("projectList", filterProjectList);
		mv.addObject("rank_period",rank_period);
		mv.addObject("filterName", filterName);
		mv.setViewName("rankings_print");
		return mv;
	}
	
	/*@RequestMapping("/printRankings")
	public ModelAndView printRankings(HttpServletRequest req){
		Integer rank_id = Integer.parseInt(req.getParameter("selectedPeriodId"));
		String filterName = req.getParameter("filterName");
		String rank_period;
		List<ProjectDto> projectList;
		if(rank_id==0 || rank_id==-1)
		{
			projectList = projectService.getNewestRankList();
			rank_period="最新";
		}
		else
		{
			rank_period = req.getParameter("selectedPeriodName");
			projectList = rankingService.getSelectedRankList(rank_id);
		}
		ModelAndView mv = new ModelAndView();
		mv.addObject("projectList", projectList);
		mv.addObject("rank_period",rank_period);
		mv.addObject("filterName", filterName);
		mv.setViewName("rankings_print");
		return mv;
	}*/
	
	@RequestMapping("/updateRankings")
	public ModelAndView updateRankings(){
		quarterTask.CollectRankings();
		return null;
	}
}
