<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<div id="divMainMenu">
	<ul id="ulMainMenu">
		<li><a href="#" onclick="showRankings()">质量龙虎榜 </a></li>
		<li id="menu_settings"><a href="#" onclick="showSettings()">产品模块设置 </a></li>
		<li id="liMenuWeight"><a href="weightlist">权重设置</a></li>
		<!-- <li id="menu_weightSettings"><a href="#" onclick="showWeightSettings()">权重设置 </a></li> -->
		<li id="liMenuKPI"><a href="employeeskpi">员工质量KPI</a></li>
		<%
			if(session.getAttribute("username").equals("admin"))
			{
		%>
			<!-- <li id="menu_usersManagement"><a href="#" onclick="showUserConfig()">系统管理 </a></li> -->
			<li id="liMenuSystemSetting"><a href="systemsettinglist">系统管理</a></li>
		<%
			}
		%>
		
		<!-- <li id="liRanking"><a href="rankinglist">龙虎榜Demo</a></li> -->
		<!-- <li id="liMenuUser"><a href="users">用户管理</a></li> -->
	</ul>
	<span id="spanUser"><a id="aUser" href="logout">退出</a></span>
	<span id="spanLog">
		<c:choose>
			<c:when test="${fn:contains(username,'envision')}">
				<span id="spanEnvision">
					<a id="aEnvisoinLog" href="#"><%=session.getAttribute("username")%></a>
				</span>
			</c:when>
			<c:otherwise>
				<span id="spanNotEnvision">
					<a id="aLog" href="mainframe?menuIndex=4"><%=session.getAttribute("username")%></a>
				</span>
			</c:otherwise>
		</c:choose>
	</span>
</div>