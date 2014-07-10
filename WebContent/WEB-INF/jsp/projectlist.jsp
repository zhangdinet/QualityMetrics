<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.env.qualitymetrics.dao.*" %>
<%@ page import="com.env.qualitymetrics.dto.*" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>员工质量KPI</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap-theme.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery-ui-1.10.4.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico" type="image/x-icon"/>
		<link rel="Bookmark" href="${pageContext.request.contextPath}/img/favicon.ico" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.1.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-ui-1.10.4.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.ui.datepicker-zh-CN.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/exporting.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/qualitymetrics.js" charset="gb2312"></script>
	</head>
	<body>
		<jsp:include page="commonpart/headerLogoName.jsp"></jsp:include>
		<jsp:include page="commonpart/navMenu.jsp"></jsp:include>
		<jsp:include page="commonpart/containerStart.jsp"></jsp:include>
		
		<h3>产品模块信息</h3>
		
		<%
			if((Boolean)(session.getAttribute("isAdmin")))
			{
		%>
				<form action = "addProductProject" style="float:right">
					<input type="submit" id="addProductProject" class="btn btn-primary" value="添加产品类项"></input>
				</form>
				<form action = "addModuleProject" style="float:right">
					<input type="submit" id="addModuleProject" class="btn btn-primary" value="添加模块类项"></input>
				</form>
		<%
			}
		%>
		
		<table id="tbl_project">
			<tr>
				<th>产品模块名称</th>
				<th>TestLink名称</th>
				<th>Redmine名称</th>
				<th>Redmine技术支持率名称</th>
				<th>Sprint信息</th>
			</tr>
			
		<%
				List<ProjectDto> lstProject=(List<ProjectDto>)request.getAttribute("projectList");
				List<Integer> lstProjectID=(List<Integer>)session.getAttribute("lstProjectID");
				int count=lstProject.size();
				int idCount=lstProjectID.size();
				for(int i=0;i<count;i++)
				{
					ProjectDto pDto=lstProject.get(i);
					Integer pID=pDto.getProject_id();
					int pFlag=pDto.getProject_flag();
					Boolean isAdmin = (Boolean)(session.getAttribute("isAdmin"));
					String pName=pDto.getProject_name();
					String pNameTL=pDto.getProject_name_tl();
					String pNameRM=pDto.getProject_name_rm();
					String pNameSN=pDto.getProject_name_sn();
					String pNameRMSP=pDto.getProject_name_rm_support();
					String pNameCategoryRM = pDto.getCategory_name_rm();
					String pNameSuiteTL = pDto.getSuite_name_tl();
					String str = "";
					String sprintDetail="<a href='sprintlist?project_id=" + pID + "&project_name=" + pName +"&pageNumber=1'>Sprint详情</a>";
		%>
			 	<tr>
		<%
					if(isAdmin)
					{
						if(pFlag==1)
						{
							str = "<a href='updateProductProject?project_id=" + pID + "&project_name=" + pName+"'>";
						}
						else
						{
							str="<a href='updateModuleProject?project_id="+ pID + "&project_name=" + pName + "&project_name_tl="
									+ pNameTL + "&project_name_rm=" + pNameRM + "&project_name_sn=" + pNameSN + "&project_name_rm_support=" + pNameRMSP + "'>";
						}
					
		%>						
						<td><%= str %><%= pName %></a></td>
		<% 
					}
					else
					{
						boolean ownerFlag=false;
						for(int j=0;j<idCount;j++)
						{
							ownerFlag=false;
							if(lstProjectID.get(i).equals(pID))
							{
								ownerFlag=true;
							}
						}
						
						if(ownerFlag)
						{
							if(pFlag==1)
							{
								str = "<a href='updateProductProject?project_id=" + pID + "&project_name=" + pName+"'>";
							}
							else
							{
								str="<a href='updateModuleProject?project_id="+ pID + "&project_name=" + pName + "&project_name_tl="
										+ pNameTL + "&project_name_rm=" + pNameRM + "&project_name_sn=" + pNameSN + "&project_name_rm_support=" + pNameRMSP + "'>";
							}
						}
		%>
						<td><%= str %><%= pName %></a></td>
		<%
					}
					if( pFlag == 1)
					{
		%>
						<td><%= pNameTL %></td>
						<td><%= pNameRM %></td>
		<%
					}
					else
					{
						String tempPNameTL= pNameTL + "--";
						String tempPNameSuiteTL="";
						if(pNameSuiteTL!=null)
						{
							tempPNameSuiteTL = pNameSuiteTL.replaceAll("<br>", tempPNameTL);
						}
						
		%>					
						<td><%= pNameTL %> -- <%= tempPNameSuiteTL %></td>
						<td><%= pNameRM %> -- <%= pNameCategoryRM %></td>
		<%					
					}
		%>
				<td><%= pNameRMSP %></td>
				<td><%= sprintDetail %></td>
			</tr>
		<%	
			}
		%>
		
		</table>
		<div id="settings_sprint"></div>
		<jsp:include page="commonpart/containerEnd.jsp"></jsp:include>
		<jsp:include page="commonpart/footer.jsp"></jsp:include>
		
		<script type="text/javascript">
			function showSprintByProjectId(project_id,project_name,pageNumber){
				$.ajax({
					url: 'showSettingsSprint',
					type: 'post',
					data: {"project_id":project_id,"project_name":project_name,"pageNumber":pageNumber},
					/* success:function(data){
					},
					error:function(){
					}, */
				});
			}

			function gotoSelectedPage()
			{
				var pageNumber = $('select[name="pageNumber"]').children('option:selected').val();
				var project_id = $("input[name='project_id']").val();
				var project_name = $("input[name='project_name']").val();
				showSprintByProjectId(project_id,project_name,pageNumber);
			}
		</script>
	</body>
</html>



