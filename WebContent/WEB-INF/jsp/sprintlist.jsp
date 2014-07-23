<%@page import="java.net.URLDecoder"%>
<%@page import="org.hibernate.annotations.common.reflection.java.JavaXMember"%>
<%@page import="javassist.compiler.Javac"%>
<%@page import="java.beans.Encoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.env.qualitymetrics.dao.*" %>
<%@ page import="com.env.qualitymetrics.dto.*" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.text.*" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Sprint信息</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap-theme.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery-ui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico" type="image/x-icon"/>
		<link rel="Bookmark" href="${pageContext.request.contextPath}/img/favicon.ico" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-ui.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.ui.datepicker-zh-CN.js"></script>
	</head>
	<body>
		<jsp:include page="commonpart/headerLogoName.jsp"></jsp:include>
		<jsp:include page="commonpart/navMenu.jsp"></jsp:include>
		<jsp:include page="commonpart/containerStart.jsp"></jsp:include>

		<form action = "addSprint" style="margin-top:40px;">
			<h3 style="display:inline">Sprint信息</h3>
			<span id="spanPromptWait" class="hideElement">
				<img src="${pageContext.request.contextPath}/img/loading.gif">
			</span>
			<div style="float:right;">
				<%
					Boolean isAdmin=(Boolean)session.getAttribute("isAdmin");
					List<Integer> lstProjectID=(List<Integer>)session.getAttribute("lstProjectID");
					int idCount=lstProjectID.size();
					Integer pID=Integer.parseInt(request.getParameter("project_id"));
					
					boolean ownerFlag=false;
					for(int i=0;i<idCount;i++)
					{
						ownerFlag=false;
						if(lstProjectID.get(i).equals(pID))
						{
							ownerFlag=true;
							break;
						}
					}
				
					if( isAdmin || ownerFlag )
					{
				%>
						<input type="submit" class="btn btn-primary" value="添加" onclick="showPromptWait()"></input>
				<%
					}
				%>
				<input type="hidden" name="project_id" value="${project_id}"></input>
				<input type="hidden" name="project_name" value="${project_name}"></input>
				<input type="hidden" name="project_flag" value="${project_flag}"></input>
			</div>
		</form>
	
		<table id="tbl_sprint">
			<tr>
				<th>名称</th>
				<th>Testplan名称</th>
				<th>Version名称</th>
				<th width="300px" align="left" >Build名称及构建日期</th>
				<th>开始日期</th>
				<th>结束日期</th>
				<th>IPD过程质量分数（PDT或LMT）</th>
			</tr>
			
			<%
				List<SprintDto> lstSprintDto=(List<SprintDto>)(request.getAttribute("lstSprintDto"));
				int sprintCount=lstSprintDto.size();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				
				for(int i=0;i<sprintCount;i++)
				{
					SprintDto dto=lstSprintDto.get(i);
					Integer projectID=dto.getProject_id();
					String projectName=request.getParameter("project_name");
					String sprintName=dto.getSprint_name();
					Integer sprintID=dto.getSprint_id();
					Integer role=(Integer)(request.getSession().getAttribute("role"));
					String encodeSprintName="";
					String encodeProjectName="";
					try
					{
						encodeSprintName=URLEncoder.encode(URLEncoder.encode(sprintName,"UTF-8"),"UTF-8");
						encodeProjectName=URLEncoder.encode(URLEncoder.encode(projectName,"UTF-8"),"UTF-8");
					}
					catch(Exception e)
					{
					}
					
					if( isAdmin || ownerFlag )
					{
						String strHref="updateSprint?sprint_id="+sprintID+"&project_id="+projectID+
								"&project_name="+encodeProjectName+"&role="+role+"&sprintName="+encodeSprintName;
			%>
						<tr><td><a onclick="showPromptWait()" href="<%=strHref%>"><%=sprintName%></a>
			<%
					}
					else
					{
			%>
						<tr><td><%= sprintName %>
			<%
					}
			%>
					</td>
					<td><%= dto.getTestplan_testlink() %>
					<td><%= dto.getVersion_redmine() %>
					<td><%= dto.getSprint_build() %>
					<td><%= sdf.format(dto.getSprint_startdate()) %></td>
					<td><%= sdf.format(dto.getSprint_enddate()) %></td>
					
			<%
					if(dto.getIpd_score()!=-1)
					{
						
			%>
						<td><%=dto.getIpd_score() %></td>
			<%
					}
					else
					{
			%>
						<td><%=dto.getLmt_score() %></td>
			<%
					}
				}
			%>
			</tr>
		</table>
	
	<script type="text/javascript">
		function showPromptWait()
		{
			$("#spanPromptWait").removeClass("hideElement");
		}
	
		function hidePromptWait()
		{
			$("#spanPromptWait").addClass("hideElement");
		}
	</script>
	</body>
</html>