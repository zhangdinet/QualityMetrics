package com.env.qualitymetrics.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
	
	@RequestMapping("/rankinglist")
	public ModelAndView ranking(HttpServletRequest req)
	{
		String strID=req.getParameter("rankID");
		Integer rankID=0;
		List<ProjectDto> projectList;
		if(strID == null)
		{
			projectList = projectService.getNewestRankList();
		}
		else
		{
			rankID = Integer.parseInt(req.getParameter("rankId"));
			projectList = rankingService.getSelectedRankList(rankID);
		}
		ModelAndView mv = new ModelAndView();
		mv.addObject("projectList", projectList);
		mv.addObject("rank_id", rankID);
		List<RankingDto> rankingList = rankingService.getRankingPeriodList();
		mv.addObject("rankingList",rankingList);
		mv.setViewName("rankinglist");
		return mv;
	}
}
