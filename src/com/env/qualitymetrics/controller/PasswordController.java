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
public class PasswordController
{
	@Resource(name="userService")
	UserService userService;
	
	private static final Logger log = LoggerFactory.getLogger(PasswordController.class);
	
	@RequestMapping("/password")
	public ModelAndView editPassword(HttpServletRequest req){
		ModelAndView mv=new ModelAndView();
		mv.setViewName("password");
		return mv;
	}
	
	@RequestMapping("/modifyPassword")
	public ModelAndView modifyPassword(HttpServletRequest req){
		String username = req.getParameter("username");
		String password = req.getParameter("newPassword");
		boolean changePwd = userService.changePassword(username,password);
		ModelAndView mv = new ModelAndView();
		
		if(changePwd)
		{
			mv.addObject("modifyResult", "ok");
			mv.setViewName("mainframe");
			mv.addObject(username,username);
		}
		else
		{
			mv.addObject("modifyResult", "err");
			mv.addObject("error");
		}
		return mv;
	}
}