<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>系统管理</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap-theme.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico" type="image/x-icon"/>
		<link rel="Bookmark" href="${pageContext.request.contextPath}/img/favicon.ico" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/qualitymetrics.js" charset="gb2312"></script>
	</head>
	<body>
		<jsp:include page="commonpart/headerLogoName.jsp"></jsp:include>
		<jsp:include page="commonpart/mainMenu.jsp"></jsp:include>
		<jsp:include page="commonpart/containerStart.jsp"></jsp:include>
			<h3>用户列表</h3>
			<form id="fromAddUser" action = "addUserzd" style="float:right">
				<c:if test="${sessionScope.flag_admin == 'yes' and sessionScope.project_id == 0}">
					<input type="submit" class="btn btn-primary" value="添加"></input>
				</c:if>
			</form>
			
			<table id="tableUser">
				<tr>
					<th>用户</th>
					<th>设置</th>
				</tr>
				<c:forEach items="${userList}" var="item">
					<tr>
						<td>${item.username}</td>
						<td>
							<a href="editUserzd?id=${item.user_id}&username=${item.username}&role=${item.role}">编辑</a>
							<a href="deleteUserzd?id=${item.user_id}">删除</a>
							<a href="resetPassword?id=${item.user_id}">重置密码</a>
						</td>
					</tr>
				</c:forEach>
			</table>
		<jsp:include page="commonpart/containerEnd.jsp"></jsp:include>
		
	</body>
</html>