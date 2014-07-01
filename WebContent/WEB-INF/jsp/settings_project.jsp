<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<h3>产品模块信息</h3>

<!-- todo 140514 合并form 根据按钮判断action -->
<form action = "addProject" style="float:right">
	<c:if test="${sessionScope.flag_admin == 'yes' and sessionScope.project_id == 0}">
		<input type="submit" id="addProductProject" class="btn btn-primary" value="添加产品类Item"></input>
	</c:if>
</form>
<form action = "addProjectModule" style="float:right">
	<c:if test="${sessionScope.flag_admin == 'yes' and sessionScope.project_id == 0}">
		<input type="submit" id="addModuleProject" class="btn btn-primary" value="添加模块类Item"></input>
	</c:if>
</form>
<table id="tbl_project">
	<tr>
		<th>产品模块名称</th>
		<th>TestLink名称</th>
		<th>Redmine名称</th>
		<th>Redmine技术支持率名称</th>
		<th>Sprint信息</th>
	</tr>
	<c:forEach items="${projectList}" var="item">
		<tr>
			<td> 
				<c:if test="${sessionScope.project_id != item.project_id and sessionScope.project_id != 0}">
   					 ${item.project_name }
				</c:if>
				<c:if test="${sessionScope.project_id != item.project_id and sessionScope.project_id == 0}">
					<c:choose>
						<c:when test="${item.project_flag==1}">
    						<a href="showModifyProject?project_id=${item.project_id}&project_name=${item.project_name }">
								${item.project_name }
							</a>
						</c:when>
						<c:otherwise>
							<a href="showModifyProjectModule?project_id=${item.project_id}&project_name=${item.project_name }">
								${item.project_name }
							</a>
						</c:otherwise>
					</c:choose>
				</c:if>
	<c:if test="${sessionScope.project_id == item.project_id}">
	<a href="showModifyProject?project_id=${item.project_id}&project_name=${item.project_name }&project_name_tl=${item.project_name_tl }&
	project_name_rm=${item.project_name_rm }&project_name_sn=${item.project_name_sn }&project_name_rm_support=${item.project_name_rm_support }">
	${item.project_name }
	</a>
	</c:if>
	</td>
	<c:choose>
	<c:when test="${item.project_flag==1}">
		<td>${item.project_name_tl }</td>
		<td>${item.project_name_rm }</td>
	</c:when>
	<c:otherwise>
	<td>
		<c:set value="<br>${item.project_name_tl}--" var="newString_tl"/>
		<c:set var="nameString_tl" value="${fn:replace(item.suite_name_tl, '<br>', newString_tl)}"/>
		${item.project_name_tl }--${nameString_tl }
	</td>
	<td>
		<c:set value="<br>${item.project_name_rm}--" var="newString_rm"/>
		<c:set var="nameString_rm" value="${fn:replace(item.category_name_rm, '<br>', newString_rm)}"/>
		${item.project_name_rm }--${nameString_rm }
	</td>
	</c:otherwise>
	</c:choose>
	<td>${item.project_name_rm_support }</td>
	<td>
		<a onclick="showSprintByProjectId('${item.project_id}','${item.project_name }','1')" href="#">Sprint详情</a>
	</td>
	</tr>
	</c:forEach>
</table>
