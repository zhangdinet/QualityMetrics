<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.env.qualitymetrics.dto.ProjectDto" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="com.env.qualitymetrics.common.SysUtil"%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>软件质量龙虎榜排名</title>
	<!--  link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.4.1/build/cssreset/cssreset-min.css">-->
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap-theme.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery-ui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">	
	<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico" type="image/x-icon" />
	<link rel="Bookmark" href="${pageContext.request.contextPath}/img/favicon.ico" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.PrintArea.js"></script>
</head>
	<body>
		<jsp:include page="commonpart/headerLogoName.jsp"></jsp:include>
		<jsp:include page="commonpart/mainMenu.jsp"></jsp:include>
			<div id="divPrint">
				<div id = "divBtnPrint" >
					<button id = "print_button" class="btn btn-primary">打印</button>
					<button id = "back_button" class="btn btn-primary">返回</button>
				</div>
				<div id="divPrintArea">
					<span id="spanPrintTitle">
						软件产品质量龙虎榜
					</span>
					<table id="tbl_rankings">
						<tr>
							<th style="width:80px">排名</th>
							<th style="width:200px">产品模块名称</th>
							<th style="width:120px">总平均分</th>
						</tr>
						<%-- <c:forEach items="${projectList}" var="item" varStatus="status">
							<tr>
								<td >${status.count}</td>
								<td>${item.project_name}</td>
								<td >${item.avg_score}</td>
							</tr>
						</c:forEach> --%>
						<%
							int rankNumber=1;
							List<ProjectDto> projectList=(List<ProjectDto>)request.getAttribute("projectList");
							int projectCount = projectList.size();
							for(int i=0;i<projectCount;i++)
							{
								ProjectDto dto=projectList.get(i);
								ProjectDto prevDto;
								int projectID=dto.getProject_id();
								String projectName=dto.getProject_name();
								String encodeProjectName=SysUtil.encodeWithUtf8(projectName);
						%>
								<tr>
									<td>
						<%
									if(i==0)
									{
						%>
										<%= i+1 %>
						<%			
									}
									else
									{
										prevDto=projectList.get(i-1);
										if(dto.getAvg_score()==prevDto.getAvg_score())
										{
						%>		
											<%= rankNumber %>
						<%			
										}
										else
										{
											rankNumber=i+1;
						%>					
											<%= i+1 %>
						<%
										}
									}
						%>
									</td>
									<td><%=projectName %></td>
									<td><%=dto.getAvg_score()%></td></tr>
						<%
							}
						%>
					</table>
				</div>
			</div>
			
		<jsp:include page="commonpart/footer.jsp"></jsp:include>
		<script>
			$("#print_button").click(function(){
				$("#divPrintArea").printArea();
			});
			$("#back_button").click(function(){
				window.history.go(-1);
			});
		</script>
	</body>
</html>