package com.env.qualitymetrics.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.env.qualitymetrics.common.SysUtil;
import com.env.qualitymetrics.dto.ProjectDto;
//import com.env.qualitymetrics.dto.RankingDto;
import com.env.qualitymetrics.dto.SprintDto;
import com.env.qualitymetrics.service.ProjectService;
import com.env.qualitymetrics.service.RankingService;
import com.env.qualitymetrics.service.SprintService;
import com.env.qualitymetrics.tasks.DailyTask;
import com.env.qualitymetrics.tool.RedminePatch;

@Controller
public class ProjectDetailController {
	@Resource(name="rankingService")
	private RankingService rankingService;
	
	@Resource(name="sprintService")
	private SprintService sprintService;
	
	@Resource(name="dailyTask")
	private DailyTask dailyTask;
	
	@Resource(name="projectService")
	private ProjectService projectService;
	
	@Resource(name="redminePatch")
	RedminePatch redminePatch;
	
	@RequestMapping("showSprintDetail")
	public ModelAndView showSprintDetail(HttpServletRequest req) throws ParseException{
		
		Integer sprint_id = Integer.parseInt(req.getParameter("sprint_id"));
		SprintDto sprintDto = sprintService.getSprintById(sprint_id);
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		String year_detail = SysUtil.formatDate(sprintDto.getSprint_enddate()).substring(0,4);
		float rate_patch;
		int project_flag=projectService.getProjectFlagById(sprintDto.getProject_id());
		ProjectDto projectDto = projectService.getProjectSourceNamesById(sprintDto.getProject_id(),project_flag);
		String project_name_rm = projectDto.getProject_name_rm();
		if((today.getTime()-sprintDto.getSprint_enddate().getTime())>0){
			//当前时间在sprint 结束日期后，显示sprint结束日期那时的补丁发布率
			rate_patch = redminePatch.getPatchRateWithDate(project_name_rm,SysUtil.formatDate(sprintDto.getSprint_enddate()));
		}else{
			//当前时间在sprint结束日期之前，获取当前的补丁发布率
			rate_patch = redminePatch.getPatchRateUntilNow(project_name_rm);
		}
		ModelAndView mv = new ModelAndView();
		mv.addObject("sprint",sprintDto);
		mv.setViewName("sprintdetail");
		float rate_patch_percent = -1;
		float rate_patch_score = -1;
		if(rate_patch == -1){
			rate_patch_score = 0f;
			rate_patch_percent = -1;
		}else{
			rate_patch = (float)Math.round(rate_patch*1000)/1000;
			rate_patch_percent = SysUtil.formatFloat(rate_patch);//rate_patch*100;
			rate_patch_score = redminePatch.rateToScore(rate_patch);
		}
		mv.addObject("rate_patch", rate_patch_percent);
		mv.addObject("rate_patch_score",rate_patch_score);
		mv.addObject("thisYearDetail", year_detail);
		return mv;
	}
	
	
	@RequestMapping("showSprintHistoryDetail")
	public ModelAndView showSprintHistoryDetail(HttpServletRequest req) throws ParseException{
		
		Integer sprint_id = Integer.parseInt(req.getParameter("sprint_id"));
		Integer rank_id = Integer.parseInt(req.getParameter("rank_id"));
		SprintDto sprintDto = sprintService.getSprintHistoryById(sprint_id,rank_id);
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		String year_detail = SysUtil.formatDate(sprintDto.getSprint_enddate()).substring(0,4);
		float rate_patch;
		int project_flag=projectService.getProjectFlagById(sprintDto.getProject_id());
		ProjectDto projectDto = projectService.getProjectSourceNamesById(sprintDto.getProject_id(),project_flag);
		String project_name_rm = projectDto.getProject_name_rm();
		if((today.getTime()-sprintDto.getSprint_enddate().getTime())>0){
			//当前时间在sprint 结束日期后，显示sprint结束日期那时的补丁发布率
			rate_patch = redminePatch.getPatchRateWithDate(project_name_rm,SysUtil.formatDate(sprintDto.getSprint_enddate()));
		}else{
			//当前时间在sprint结束日期之前，获取当前的补丁发布率
			rate_patch = redminePatch.getPatchRateUntilNow(project_name_rm);
		}
		ModelAndView mv = new ModelAndView();
		mv.addObject("sprint",sprintDto);
		mv.setViewName("sprintdetail");
		float rate_patch_percent = -1;
		float rate_patch_score = -1;
		if(rate_patch == -1){
			rate_patch_score = 0f;
			rate_patch_percent = -1;
		}else{
			rate_patch = (float)Math.round(rate_patch*1000)/1000;
			rate_patch_percent = rate_patch*100;
			rate_patch_score = redminePatch.rateToScore(rate_patch);
		}
		mv.addObject("rate_patch", rate_patch_percent);
		mv.addObject("rate_patch_score",rate_patch_score);
		mv.addObject("thisYearDetail", year_detail);
		mv.addObject("rank_id", rank_id);
		return mv;
	}
	/***
	 * 手动更新sprint信息
	 * @param req
	 * @return
	 */
	@RequestMapping("/updateSprintScore")
	public ModelAndView updateSprintScore(HttpServletRequest req){
		String project_id_s = req.getParameter("project_id");
		if(project_id_s == null){
			return null;
		}
		Integer project_id = Integer.parseInt(project_id_s);
		Integer user_project_id = (Integer) req.getSession().getAttribute("project_id");
		Integer sprint_id = Integer.parseInt(req.getParameter("sprint_id"));
		
		/*String flag_admin = (String) req.getSession().getAttribute("flag_admin");
		if(flag_admin.equals("no") || (user_project_id.intValue() != project_id.intValue() && user_project_id.intValue()!=0))
		{
			return null;
		}*/
		if(!(Boolean)(req.getSession().getAttribute("isAdmin")))
		{
			return null;
		}
		
		
		SprintDto sprintDto = sprintService.getSprintById(sprint_id);
		int project_flag=projectService.getProjectFlagById(sprintDto.getProject_id());
		dailyTask.checkUpdateManual(sprintDto,project_flag);
		ModelAndView mv = new ModelAndView();
		mv.addObject("sprint",sprintDto);
		mv.addObject("updateOK","updateOK");
		mv.setViewName("sprintdetail");
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		
		String year_detail = SysUtil.formatDate(sprintDto.getSprint_enddate()).substring(0,4);
		float rate_patch;
		
		ProjectDto projectDto = projectService.getProjectSourceNamesById(sprintDto.getProject_id(),project_flag);
		String project_name_rm = projectDto.getProject_name_rm();
		if((today.getTime()-sprintDto.getSprint_enddate().getTime())>0){
			//当前时间在sprint 结束日期后，显示sprint结束日期那时的补丁发布率
			rate_patch = redminePatch.getPatchRateWithDate(project_name_rm,SysUtil.formatDate(sprintDto.getSprint_enddate()));
		}else{
			//当前时间在sprint结束日期之前，获取当前的补丁发布率
			rate_patch = redminePatch.getPatchRateUntilNow(project_name_rm);
		}
		float rate_patch_percent = -1;
		float rate_patch_score = -1;
		if(rate_patch == -1){
			rate_patch_score = 0;
			rate_patch_percent = -1;
		}else{
			rate_patch = (float)Math.round(rate_patch*1000)/1000;
			rate_patch_percent = SysUtil.formatFloat(rate_patch);//rate_patch*100;
			rate_patch_score = redminePatch.rateToScore(rate_patch);
		}
		mv.addObject("rate_patch", rate_patch_percent);
		mv.addObject("rate_patch_score",rate_patch_score);
		mv.addObject("thisYearDetail", year_detail);
		return mv;
	}
	/***
	 * 手动更新sprint往期历史信息
	 * @param req
	 * @return
	 */
	@RequestMapping("/updateSprintHistory")
	public ModelAndView updateSprintHistory(HttpServletRequest req){
		String project_id_s = req.getParameter("project_id");
		Integer rank_id = Integer.parseInt(req.getParameter("rank_id"));
		if(project_id_s == null){
			return null;
		}
		Integer project_id = Integer.parseInt(project_id_s);
		Integer user_project_id = (Integer) req.getSession().getAttribute("project_id");
		Integer sprint_id = Integer.parseInt(req.getParameter("sprint_id"));
		String flag_admin = (String) req.getSession().getAttribute("flag_admin");
		if(flag_admin.equals("no") || (user_project_id.intValue() != project_id.intValue() && user_project_id.intValue()!=0)){
			return null;
		}
		SprintDto sprintDto = sprintService.getSprintById(sprint_id);
		int project_flag=projectService.getProjectFlagById(sprintDto.getProject_id());
		//判断是否在历史页面更新的
		
			String ranking_period=rankingService.getRankingPeriodById(rank_id);
			//获取当前季度
			Calendar cal = Calendar.getInstance();
			int month = cal.get(Calendar.MONTH);
			int year = cal.get(Calendar.YEAR);
			int quarter = month/3;
			if(month == 0){
				quarter = 4;
				year--;
			}
			String period_now=year+"Q"+quarter;
			//判断是否更新的历史记录当前季度
			if(period_now.equals(ranking_period))
				dailyTask.checkUpdateManual(sprintDto,project_flag);
			else dailyTask.checkUpdateHistoryManual(sprintDto, project_flag, rank_id);	
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("sprint",sprintDto);
		mv.addObject("updateOK","updateOK");
		mv.addObject("rank_id", rank_id);
		mv.setViewName("sprintdetail");
		Date today = cal.getTime();
		
		String year_detail = SysUtil.formatDate(sprintDto.getSprint_enddate()).substring(0,4);
		float rate_patch;
		
		ProjectDto projectDto = projectService.getProjectSourceNamesById(sprintDto.getProject_id(),project_flag);
		String project_name_rm = projectDto.getProject_name_rm();
		if((today.getTime()-sprintDto.getSprint_enddate().getTime())>0){
			//当前时间在sprint 结束日期后，显示sprint结束日期那时的补丁发布率
			rate_patch = redminePatch.getPatchRateWithDate(project_name_rm,SysUtil.formatDate(sprintDto.getSprint_enddate()));
		}else{
			//当前时间在sprint结束日期之前，获取当前的补丁发布率
			rate_patch = redminePatch.getPatchRateUntilNow(project_name_rm);
		}
		float rate_patch_percent = -1;
		float rate_patch_score = -1;
		if(rate_patch == -1){
			rate_patch_score = 0;
			rate_patch_percent = -1;
		}else{
			rate_patch = (float)Math.round(rate_patch*1000)/1000;
			rate_patch_percent = rate_patch*100;
			rate_patch_score = redminePatch.rateToScore(rate_patch);
		}
		mv.addObject("rate_patch", rate_patch_percent);
		mv.addObject("rate_patch_score",rate_patch_score);
		mv.addObject("thisYearDetail", year_detail);
		return mv;
	}
}
