<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>权重信息</title>
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
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/exporting.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/qualitymetrics.js" charset="gb2312"></script>
	</head>
	<body>
		<jsp:include page="commonpart/headerLogoName.jsp"></jsp:include>
		<jsp:include page="commonpart/mainMenu.jsp"></jsp:include>
		<jsp:include page="commonpart/containerStart.jsp"></jsp:include>

		<h3>指标权重信息</h3>
		<form action = "modifyWeight" style="float:right">
			<%
				if((Boolean)(session.getAttribute("isAdmin")))
				{
			%>
				 	<input type="submit" class="btn btn-primary" value="设置"></input>
			<%
				}
			%>
		</form>
		<table id="tblWeight">
			<tr>
			<th>衡量指标</th>
			<th>权重值</th>
			</tr>
			<tr>
			<td> 
			IPD过程质量（PDT或LMT）
			</td>
			<td>${weightDto.ipdOrLmt_rate }</td>
			</tr>
			<tr>
			<td>
				代码整体质量
			</td>
			<td>${weightDto.sonar_rate }</td>
			</tr>
			<tr>
			<td> 
			测试通过率
			</td>
			<td>${weightDto.test_pass_rate }</td>
			</tr>
			<tr>
			<td> 
			测试可执行率
			</td>
			<td>${weightDto.tc_exec_rate }</td>
			</tr>
			<tr>
			<td> 
			缺陷新增率
			</td>
			<td>${weightDto.bug_new_rate }</td>
			</tr>
			<tr>
			<td> 
			缺陷返工率
			</td>
			<td>${weightDto.bug_reopen_rate }</td>
			</tr>
			<tr>
			<td> 
			缺陷遗漏率
			</td>
			<td>${weightDto.bug_escape_rate }</td>
			</tr>
			<tr>
			<td> 
			补丁发布率
			</td>
			<td>${weightDto.rate_patch_rate }</td>
			</tr>
			<tr>
			<td> 
			技术支持率
			</td>
			<td>${weightDto.rate_support_rate }</td>
			</tr>
			<tr>
			<td> 
			客户满意度
			</td>
			<td>${weightDto.rate_ce_rate }</td>
			</tr>
		</table>
	<jsp:include page="commonpart/containerEnd.jsp"></jsp:include>
	<jsp:include page="commonpart/footer.jsp"></jsp:include>
	</body>
</html>