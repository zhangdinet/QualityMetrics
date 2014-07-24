<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<div id="divMainNav">
	<div id="divMainNavMenu">
		<ul id="ulMainNavMenu">
			<li id="liMenuRank"><a href="ranklist">质量龙虎榜 </a></li>
			<li id="liMenuProject"><a href="projectlist">产品模块 </a></li>
			<li id="liMenuWeight"><a href="weightlist">权重设置</a></li>
			<li id="liMenuKPI"><a href="employeeskpi">员工质量KPI</a></li>
			<%
				if((Boolean)(session.getAttribute("isAdmin")))
				{
			%>
					<li id="liMenuSystemSetting"><a href="systemsettinglist">系统管理</a></li>
			<%
				}
			%>
		</ul>
	</div>
	<div id="divMainNavLogin">
		<c:choose>
			<c:when test="${fn:contains(username,'envision')}">
				<a id="aLog" href="#"><%=session.getAttribute("username")%></a>
			</c:when>
			<c:otherwise>
				<a id="aLog" href="password"><%=session.getAttribute("username")%></a>
			</c:otherwise>
		</c:choose>
		<a id="aUser" href="logout">退出</a></span>
	</div>
</div>

