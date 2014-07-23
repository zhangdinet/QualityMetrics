package com.env.qualitymetrics.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.env.qualitymetrics.dao.UserProjectDao;
//import org.springframework.web.servlet.view.RedirectView;
//import com.env.qualitymetrics.dto.RankingDto;
import com.env.qualitymetrics.service.RankingService;
import com.env.qualitymetrics.service.UserService;
import com.env.qualitymetrics.service.UserProjectService;

@Controller
public class LoginController{

	@Resource(name="userService")
	private UserService userService;
	
	@Resource(name="rankingService")
	private RankingService rankingService;
	
	@Resource(name="userProjectService")
	private UserProjectService userProjectService;
	
	@RequestMapping("/login")
	public ModelAndView LoginCheck(HttpServletRequest req, HttpServletResponse resp){
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		
		ModelAndView mv = new ModelAndView();
		if(username == null)
		{
			mv.setViewName("index");
			return mv;
		}
		
		if(userService.checkUserLogin(username, password))
		{
			boolean isAdmin=userService.isAdmin(username);
			int userID=userService.getUserID(username);
			int role = userService.getRole(userID);
			HttpSession session = req.getSession();
			session.setAttribute("isAdmin", isAdmin);
			session.setAttribute("username", username);
			session.setAttribute("password", password);
			session.setAttribute("userID", userID);
			session.setAttribute("role", role);
			List<Integer> lstProjectID=userProjectService.getUserProjects(userID);
			session.setAttribute("lstProjectID",lstProjectID);
			session.setMaxInactiveInterval(-1);
			mv.setViewName("redirect:ranklist");
			mv.addObject("rank_id",0);
		}else{
			req.setAttribute("errFlag", "error");
			mv.setViewName("index");
		}
		return mv;
	}
	
	@RequestMapping("/logout")
	public ModelAndView Logout(HttpServletRequest req){
		HttpSession session = req.getSession();
		session.removeAttribute("username");
		session.removeAttribute("password");
		session.removeAttribute("flag_admin");
		session.removeAttribute("project_id");
		session.removeAttribute("isAdmin");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("index");
		return mv;
	}
}
