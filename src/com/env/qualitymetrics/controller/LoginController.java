package com.env.qualitymetrics.controller;

//import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.view.RedirectView;
//import com.env.qualitymetrics.dto.RankingDto;
import com.env.qualitymetrics.service.RankingService;
import com.env.qualitymetrics.service.UserService;

@Controller
public class LoginController{

	@Resource(name="userService")
	private UserService userService;
	
	@Resource(name="rankingService")
	private RankingService rankingService;
	
	@RequestMapping("/login")
	public ModelAndView LoginCheck(HttpServletRequest req, HttpServletResponse resp){
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		ModelAndView mv = new ModelAndView();
		if(username == null){
			mv.setViewName("index");	//session失效或者用户已经退出了跳转到index页面
			return mv;
		}
		if(userService.checkUserLogin(username, password)){
			boolean flag_admin = userService.checkUserAuthorityByName(username);
			HttpSession session = req.getSession();
			if(flag_admin){
				session.setAttribute("flag_admin", "yes");
				Integer user_project_id = userService.getUserProjectIdByUsername(username);
				session.setAttribute("project_id",user_project_id);
			}else{
				session.setAttribute("flag_admin", "no");
			}
			session.setAttribute("username", username);
			session.setAttribute("password", password);
			mv.setViewName("redirect:/mainframe");
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
		ModelAndView mv = new ModelAndView();
		mv.setViewName("index");
		return mv;
	}
}
