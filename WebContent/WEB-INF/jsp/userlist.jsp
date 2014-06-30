<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h3>系统信息</h3>
<form action = "addUser" style="float:right">
	<c:if test="${sessionScope.flag_admin == 'yes' and sessionScope.project_id == 0}">
		<input type="submit" class="btn btn-primary" value="添加"></input>
	</c:if>
</form>
<table id="tbl_user">
	<tr>
		<th>用户名</th>
		<th>产品模块</th>
		<th>设置</th>
	</tr>
	<c:forEach items="${userList}" var="item">
		<tr>
			<td>${item.username }</td>
			<td>${item.project_name }</td>
			<td>
				<a href="showModifyUsers?user_id=${item.user_id}&user_name=${item.username}&user_project_id=${item.project_id }&project_name=${item.project_name}&flag_admin=${item.flag_admin }" href="#">编辑</a>&nbsp;&nbsp;
				<a onclick="resetPwd('${item.user_id}')" href="#">重置密码</a>&nbsp;&nbsp;
				<a onclick="deleteUser('${item.user_id}')" href="#">删除</a>
			</td>
		</tr>
	</c:forEach>
</table>
