<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>员工质量KPI</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap-theme.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery-ui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico" type="image/x-icon">
		<link rel="Bookmark" href="${pageContext.request.contextPath}/img/favicon.ico" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-ui.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/qualitymetrics.js" charset="gb2312"></script>
	</head>
	<body>
		<jsp:include page="commonpart/headerLogoName.jsp"></jsp:include>
		<jsp:include page="commonpart/mainMenu.jsp"></jsp:include>
		<jsp:include page="commonpart/containerStart.jsp"></jsp:include>
			<h3>系统信息</h3>
			<form action = "addUser" style="float:right">
				<c:if test="${sessionScope.isAdmin == 'true'}">
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
							<a href="updateUser?userID=${item.user_id}&user_name=${item.username}&user_project_id=${item.project_id }&project_name=${item.project_name}">编辑</a>&nbsp;&nbsp;
							<a onclick="resetPwd('${item.user_id}')" href="#">重置密码</a>&nbsp;&nbsp;
							<a onclick="deleteUser('${item.user_id}')" href="#">删除</a>
						</td>
					</tr>
				</c:forEach>
			</table>
		<jsp:include page="commonpart/containerEnd.jsp"></jsp:include>
		<jsp:include page="commonpart/footer.jsp"></jsp:include>
		<script type="text/javascript">
			function resetPwd(user_id){
				if(confirm("确定要重置用户密码吗？")){
					$.ajax({
						url: 'resetPwd',
						type: 'post',
						data: {"user_id":user_id},
						success:function(data){
							if(data == 'ok'){
								alert("重置成功");
								
							}else{
								alert("重置失败");
							}
						},
					/* 	error:function(){
							alert("showSprintByProjectId error!");
						}, */
					});
					
				}
			}
	
			function deleteUser(user_id){
				if(confirm("删除后无法恢复，确定要删除用户吗？")){
					$.ajax({
						url: 'deleteUser',
						type: 'post',
						data: {"user_id":user_id},
						success:function(data)
						{
							//alert("已删除！");
							location.reload();
						},
						/* error:function(){
							alert("delete user error!");
						}, */
					});
				}
			}
		</script>
	</body>
</html>