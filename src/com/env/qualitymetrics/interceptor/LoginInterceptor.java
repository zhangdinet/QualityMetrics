package com.env.qualitymetrics.interceptor;

//import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LoginInterceptor implements HandlerInterceptor  {

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp,
			Object handler) throws Exception {
		// TODO Auto-generated method stub
		
		//System.out.print(req.getServletPath());
		if(req.getServletPath().equals("/login")){
			return true;
		}
		if(req.getSession().getAttribute("username") != null){
			return true;
		}
		resp.sendRedirect(req.getContextPath() + "/login");
		return false;
	}

}
