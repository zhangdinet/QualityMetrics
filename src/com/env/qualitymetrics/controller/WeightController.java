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
import com.env.qualitymetrics.dto.RankingDto;
import com.env.qualitymetrics.dto.WeightDto;
import com.env.qualitymetrics.service.RankingService;
import com.env.qualitymetrics.service.UserService;
import com.env.qualitymetrics.dto.UserDtozd;
import com.env.qualitymetrics.service.UserServicezd;
import com.env.qualitymetrics.tool.RedmineCommon;
import com.env.qualitymetrics.service.WeightService;

@Controller
public class WeightController
{
	@Resource(name="redmineCommon")
	RedmineCommon redmineCommon;
	
	@Resource(name="weightService")
	WeightService weightService;
	
	private static final Logger log = LoggerFactory.getLogger(WeightController.class);
	
	@RequestMapping("/weightlist")
	public ModelAndView showIndicatorWeight(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("weightlist");
		WeightDto weightDto = weightService.getWeights();
		mv.addObject("weightDto",weightDto);
		return mv;
	}
	
	@RequestMapping("/modifyWeight")
	//进入指标权重修改页面
	public ModelAndView modifyWeight(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("modify_weight");
		WeightDto weightDto = weightService.getWeights();
		mv.addObject("weightDto",weightDto);
		return mv;
	}
	
	@RequestMapping("/saveModifyWeight")
	public ModelAndView saveModifyWeight(HttpServletRequest req){
		ModelAndView mv = new ModelAndView();
		float ipdOrLmt_rate = Float.parseFloat(req.getParameter("ipdOrLmt_rate"));
		float sonar_rate = Float.parseFloat(req.getParameter("sonar_rate"));
		float test_pass_rate = Float.parseFloat(req.getParameter("test_pass_rate"));
		float tc_exec_rate = Float.parseFloat(req.getParameter("tc_exec_rate"));
		float bug_new_rate = Float.parseFloat(req.getParameter("bug_new_rate"));
		float bug_reopen_rate = Float.parseFloat(req.getParameter("bug_reopen_rate"));
		float bug_escape_rate = Float.parseFloat(req.getParameter("bug_escape_rate"));
		float rate_patch_rate = Float.parseFloat(req.getParameter("rate_patch_rate"));
		float rate_support_rate = Float.parseFloat(req.getParameter("rate_support_rate"));
		float rate_ce_rate = Float.parseFloat(req.getParameter("rate_ce_rate"));
		
		WeightDto weightDto=new WeightDto();
		weightDto.setIpdOrLmt_rate(ipdOrLmt_rate);
		weightDto.setSonar_rate(sonar_rate);
		weightDto.setTest_pass_rate(test_pass_rate);
		weightDto.setTc_exec_rate(tc_exec_rate);
		weightDto.setBug_new_rate(bug_new_rate);
		weightDto.setBug_reopen_rate(bug_reopen_rate);
		weightDto.setBug_escape_rate(bug_escape_rate);
		weightDto.setRate_patch_rate(rate_patch_rate);
		weightDto.setRate_support_rate(rate_support_rate);
		weightDto.setRate_ce_rate(rate_ce_rate);
		if(weightService.updateWeights(weightDto))
			mv.addObject("updateResult","ok");
		else 
			mv.addObject("updateResult","err");
		
		mv.addObject("weightDto",weightDto);
		mv.setViewName("modify_weight");
		return mv;
	}
}