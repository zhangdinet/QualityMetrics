<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<form action = "addSprint" style="margin-top:40px;">
	<label style="font-size:20px">Sprint信息</label>
	<span id="spanPromptWait" class="hideElement">
		<img src="${pageContext.request.contextPath}/img/loading.gif">
	</span>
	<div style="float:right;">
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
		<c:if test="${sessionScope.project_id == item.project_id}">
			<a onclick="showPromptWait()" href="showModifySprint?sprint_id=${item.sprint_id }&project_name=${project_name }&project_flag=${project_flag }">${item.sprint_name }</a>
		</c:if>
		
		<c:if test="${sessionScope.project_id != item.project_id and sessionScope.project_id == 0}">
			<a onclick="showPromptWait()" href="showModifySprint?sprint_id=${item.sprint_id }&project_id=${project_id}&project_name=${project_name }&project_flag=${project_flag }">${item.sprint_name }</a>
		</c:if>
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
<jsp:include page="commonpart/page_footer.jsp"></jsp:include>
