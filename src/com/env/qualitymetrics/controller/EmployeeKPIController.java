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
import com.env.qualitymetrics.service.RankingService;
import com.env.qualitymetrics.service.UserService;
import com.env.qualitymetrics.dto.UserDtozd;
import com.env.qualitymetrics.service.UserServicezd;
import com.env.qualitymetrics.tool.RedmineCommon;

@Controller
public class EmployeeKPIController
{
	@Resource(name="redmineCommon")
	RedmineCommon redmineCommon;
	
	private static final Logger log = LoggerFactory.getLogger(EmployeeKPIController.class);
	
	@RequestMapping("/employeeskpi")
	public ModelAndView employeesKPI()
	{
		List<String> redmineProjectNames=redmineCommon.getRedmineProjectNames();
		ModelAndView mv = new ModelAndView();
		mv.addObject("redmineProjectNames",redmineProjectNames);
		mv.setViewName("employeeskpi");
		return mv;
	}
	
	@RequestMapping("/getVersionNames")
	public void getVersionsByProjectName(HttpServletRequest req,HttpServletResponse resp)  throws IOException
	{
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		String projectName = req.getParameter("redmineName");
		int projectID = redmineCommon.getProjectId(projectName);
		List<String> versionNames = redmineCommon.getVersionNames(projectID);
		
		String[] arrNames=new String[versionNames.size()];
		for(int i=0;i<versionNames.size();i++)
		{
			arrNames[i]=versionNames.get(i);
		}
		String strVersionNames = Arrays.toString(arrNames);
		strVersionNames = strVersionNames.substring(1,strVersionNames.length()-1);
		writer.write(strVersionNames);
	}
	
	@RequestMapping("/getFixedBugsByCategory")
	public void getFixedBugsByCategory(HttpServletRequest req,HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter writer = resp.getWriter();
		String redmineName = req.getParameter("redmineName");
		String version=req.getParameter("version");
		List<String> lstBugs=redmineCommon.getFixedBugsByCategory(redmineName, version);
		JSONArray jsonArray=JSONArray.fromObject(lstBugs);
		String jsonString=jsonArray.toString();
		writer.write(jsonString);
		if(writer!=null)
		{
			writer.close();
		}
	}
	
	@RequestMapping("/getNewBugsByCategory")
	public void getNewBugsByCategory(HttpServletRequest req,HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter writer = resp.getWriter();
		String redmineName = req.getParameter("redmineName");
		String version=req.getParameter("version");
		String sprintStart=req.getParameter("sprintStart");
		String sprintEnd=req.getParameter("sprintEnd");
		List<String> lstBugs=redmineCommon.getNewBugsByCategory(redmineName, version,sprintStart,sprintEnd);
		JSONArray jsonArray=JSONArray.fromObject(lstBugs);
		String jsonString=jsonArray.toString();
		writer.write(jsonString);
		if(writer!=null)
		{
			writer.close();
		}
	}
	
	
	@RequestMapping("/getFixedRateByCategory")
	public void getFixedRateByCategory(HttpServletRequest req,HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter writer = resp.getWriter();
		String redmineName = req.getParameter("redmineName");
		String version=req.getParameter("version");
		String sprintStart=req.getParameter("sprintStart");
		String sprintEnd=req.getParameter("sprintEnd");
		List<String> lstBugs=redmineCommon.getFixedRateByCategory(redmineName, version,sprintStart,sprintEnd);
		JSONArray jsonArray=JSONArray.fromObject(lstBugs);
		String jsonString=jsonArray.toString();
		writer.write(jsonString);
		if(writer!=null)
		{
			writer.close();
		}
	}
	
	
	@RequestMapping("/getImplementedFeaturesByCategory")
	public void getImplementedFeaturesByCategory(HttpServletRequest req,HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter writer = resp.getWriter();
		String redmineName = req.getParameter("redmineName");
		String version=req.getParameter("version");
		List<String> lstBugs=redmineCommon.getImplementedFeaturesByCategory(redmineName, version);
		JSONArray jsonArray=JSONArray.fromObject(lstBugs);
		String jsonString=jsonArray.toString();
		writer.write(jsonString);
		if(writer!=null)
		{
			writer.close();
		}
	}
	
	@RequestMapping("/getFixedBugsByDeveloper")
	public void getFixedBugsByDeveloper(HttpServletRequest req,HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		String redmineName = req.getParameter("redmineName");
		String version=req.getParameter("version");
		List<String> lstBugs=redmineCommon.getFixedBugsByDeveloper(redmineName, version);
		JSONArray jsonArray=JSONArray.fromObject(lstBugs);
		String jsonString=jsonArray.toString();
		writer.write(jsonString);
		if(writer!=null)
		{
			writer.close();
		}
	}
	
	@RequestMapping("/getNewBugsByDeveloper")
	public void getNewBugsByDeveloper(HttpServletRequest req,HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		String redmineName = req.getParameter("redmineName");
		String version=req.getParameter("version");
		String sprintStart=req.getParameter("sprintStart");
		String sprintEnd=req.getParameter("sprintEnd");
		
		List<String> lstBugs=redmineCommon.getNewBugsByDeveloper(redmineName, version,sprintStart,sprintEnd);
		JSONArray jsonArray=JSONArray.fromObject(lstBugs);
		String jsonString=jsonArray.toString();
		writer.write(jsonString);
		if(writer!=null)
		{
			writer.close();
		}
	}
	
	@RequestMapping("/getImplementedFeaturesByDeveloper")
	public void getImplementedFeaturesByDeveloper(HttpServletRequest req,HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		String redmineName = req.getParameter("redmineName");
		String version=req.getParameter("version");
		List<String> lstBugs=redmineCommon.getImplementedFeaturesByDeveloper(redmineName, version);
		JSONArray jsonArray=JSONArray.fromObject(lstBugs);
		String jsonString=jsonArray.toString();
		writer.write(jsonString);
		if(writer!=null)
		{
			writer.close();
		}
	}
	
	@RequestMapping("/getFixedRateByDeveloper")
	public void getFixedRateByDeveloper(HttpServletRequest req,HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		String redmineName = req.getParameter("redmineName");
		String version=req.getParameter("version");
		String sprintStart=req.getParameter("sprintStart");
		String sprintEnd=req.getParameter("sprintEnd");
		List<String> lstBugs=redmineCommon.getFixedRateByDeveloper(redmineName, version,sprintStart,sprintEnd);
		JSONArray jsonArray=JSONArray.fromObject(lstBugs);
		String jsonString=jsonArray.toString();
		writer.write(jsonString);
		if(writer!=null)
		{
			writer.close();
		}
	}
	
	//=======================
	
	@RequestMapping("/getFixedBugsBySeverity")
	public void getFixedBugsBySeverity(HttpServletRequest req,HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		String redmineName = req.getParameter("redmineName");
		String version=req.getParameter("version");
		List<String> lstBugs=redmineCommon.getFixedBugsBySeverity(redmineName, version);
		JSONArray jsonArray=JSONArray.fromObject(lstBugs);
		String jsonString=jsonArray.toString();
		writer.write(jsonString);
		if(writer!=null)
		{
			writer.close();
		}
	}
	
	@RequestMapping("/getNewBugsBySeverity")
	public void getNewBugsBySeverity(HttpServletRequest req,HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		String redmineName = req.getParameter("redmineName");
		String version=req.getParameter("version");
		String sprintStart=req.getParameter("sprintStart");
		String sprintEnd=req.getParameter("sprintEnd");
		
		
		List<String> lstBugs=redmineCommon.getNewBugsBySeverity(redmineName, version, sprintStart, sprintEnd);
		JSONArray jsonArray=JSONArray.fromObject(lstBugs);
		String jsonString=jsonArray.toString();
		writer.write(jsonString);
		if(writer!=null)
		{
			writer.close();
		}
	}
	
	@RequestMapping("/getFixedRateBySeverity")
	public void getFixedRateBySeverity(HttpServletRequest req,HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		String redmineName = req.getParameter("redmineName");
		String version=req.getParameter("version");
		String sprintStart=req.getParameter("sprintStart");
		String sprintEnd=req.getParameter("sprintEnd");
		List<String> lstBugs=redmineCommon.getFixedRateBySeverity(redmineName, version,sprintStart,sprintEnd);
		JSONArray jsonArray=JSONArray.fromObject(lstBugs);
		String jsonString=jsonArray.toString();
		writer.write(jsonString);
		if(writer!=null)
		{
			writer.close();
		}
	}
	
	
	@RequestMapping("/getReopenRatio")
	public void getReopenRatio(HttpServletRequest req,HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		String redmineName = req.getParameter("redmineName");
		String version=req.getParameter("version");
		String sprintStart=req.getParameter("sprintStart");
		String sprintEnd=req.getParameter("sprintEnd");
		List<String> lstBugs=redmineCommon.getReopenRatio(redmineName, version,sprintStart,sprintEnd);
		JSONArray jsonArray=JSONArray.fromObject(lstBugs);
		String jsonString=jsonArray.toString();
		writer.write(jsonString);
		if(writer!=null)
		{
			writer.close();
		}
	}
	
	@RequestMapping("/getScopeStability")
	public void getScopeStability(HttpServletRequest req,HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		String redmineName = req.getParameter("redmineName");
		String version=req.getParameter("version");
		String sprintStart=req.getParameter("sprintStart");
		String sprintEnd=req.getParameter("sprintEnd");
		List<String> lstBugs=redmineCommon.getScopeStability(redmineName, version, sprintStart, sprintEnd);
		JSONArray jsonArray=JSONArray.fromObject(lstBugs);
		String jsonString=jsonArray.toString();
		writer.write(jsonString);
		if(writer!=null)
		{
			writer.close();
		}
	}
	
	@RequestMapping("/getBugsScopeStability")
	public void getBugsScopeStability(HttpServletRequest req,HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		String redmineName = req.getParameter("redmineName");
		String version=req.getParameter("version");
		String sprintStart=req.getParameter("sprintStart");
		String sprintEnd=req.getParameter("sprintEnd");
		List<String> lstBugs=redmineCommon.getBugsScopeStability(redmineName, version, sprintStart, sprintEnd);
		JSONArray jsonArray=JSONArray.fromObject(lstBugs);
		String jsonString=jsonArray.toString();
		writer.write(jsonString);
		if(writer!=null)
		{
			writer.close();
		}
	}
	
	@RequestMapping("/getFeaturesScopeStability")
	public void getFeaturesScopeStability(HttpServletRequest req,HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		String redmineName = req.getParameter("redmineName");
		String version=req.getParameter("version");
		String sprintStart=req.getParameter("sprintStart");
		String sprintEnd=req.getParameter("sprintEnd");
		List<String> lstBugs=redmineCommon.getFeaturesScopeStability(redmineName, version, sprintStart, sprintEnd);
		JSONArray jsonArray=JSONArray.fromObject(lstBugs);
		String jsonString=jsonArray.toString();
		writer.write(jsonString);
		if(writer!=null)
		{
			writer.close();
		}
	}
	
	@RequestMapping("/getProjectInfoByVersion")
	public void getProjectInfoByVersion(HttpServletRequest req,HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/plain");
		PrintWriter writer = resp.getWriter();
		String redmineName = req.getParameter("redmineName");
		//String version=req.getParameter("version");
		String sprintStart=req.getParameter("sprintStart");
		String sprintEnd=req.getParameter("sprintEnd");
		List<String> lstBugs=redmineCommon.getProjectInfoByVersion(redmineName,sprintStart+" 00:00:00",sprintEnd+" 23:59:59");
		JSONArray jsonArray=JSONArray.fromObject(lstBugs);
		String jsonString=jsonArray.toString();
		writer.write(jsonString);
		if(writer!=null)
		{
			writer.close();
		}
	}
}
