<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery-ui-1.10.4.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico" type="image/x-icon"/>
		<link rel="Bookmark" href="${pageContext.request.contextPath}/img/favicon.ico" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.1.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-ui-1.10.4.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.ui.datepicker-zh-CN.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/qualitymetrics.js" charset="gb2312"></script>
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
					if(isAdmin)
					{
						
				%>
						<input type="submit" class="btn btn-primary" value="添加" onclick="showPromptWait()"></input>
				<%
					}
				%>
			
				<c:if test="${sessionScope.flag_admin == 'yes' and sessionScope.project_id == project_id or sessionScope.project_id == 0}">
					<input type="submit" class="btn btn-primary" value="添加" onclick="showPromptWait()"></input>
				</c:if>
				<input type="hidden" name="project_id" value="${project_id}"></input>
				<input type="hidden" name="project_name" value="${project_name}"></input>
				<input type="hidden" name="project_flag" value="${project_flag}"></input>
			</div>
		</form>
	
		<table id="tbl_sprint">
			<tr>
				<th>名称</th>
				<th>Test Plan名称</th>
				<th>Version名称</th>
				<th width="300px" align="left" >Build名称及构建日期</th>
				<th>开始日期</th>
				<th>结束日期</th>
				<th>IPD过程质量分数（PDT或LMT）</th>
			</tr>
			<c:forEach items="${sprintList}" var="item">
				<tr>
					<td>
						<c:choose>
							<c:when test="${sessionScope.project_id == item.project_id or sessionScope.project_id == 0}">
								<a onclick="showPromptWait()" href="showModifySprint?sprint_id=${item.sprint_id }&project_id=${project_id}&project_name=${project_name }&project_flag=${project_flag }">${item.sprint_name }</a>
							</c:when>
							<c:otherwise>
								${item.sprint_name}
							</c:otherwise>
						</c:choose>
					</td>
					<td>${item.testplan_testlink }</td>
					<td>${item.version_redmine }</td>
					<td>${item.sprint_build }</td>
					<td><fmt:formatDate value="${item.sprint_startdate}" pattern="yyyy-MM-dd "/></td>
					<td><fmt:formatDate value="${item.sprint_enddate}" pattern="yyyy-MM-dd "/></td>
					<td>
						<c:choose>
							<c:when test="${item.ipd_score eq -1}">${item.lmt_score }</c:when>
							<c:otherwise>${item.ipd_score }</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
		</table>
	
	<script type="text/javascript">
	</script>
	</body>
</html>