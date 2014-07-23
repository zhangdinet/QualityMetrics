<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>更改系统信息</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap-theme.css">
		<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.4.1/build/cssreset/cssreset-min.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico" type="image/x-icon"/>
		<link rel="Bookmark" href="${pageContext.request.contextPath}/img/favicon.ico" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
	</head>
	<body id="bodyEditUser">
		<jsp:include page="commonpart/headerLogoName.jsp"></jsp:include>
		<jsp:include page="commonpart/mainMenu.jsp"></jsp:include>
		<jsp:include page="commonpart/containerStart.jsp"></jsp:include>
			<div id="divEditUser">
				<h3>系统管理</h3>
				<form id="formEditUser" method="post">
					<label>用户名</label>
					<input name="username" id="username" class="form-control" readonly value="${username}"></input>
					<label>角色</label>
					<select name="role" id="role" class="form-control">产
						<option value="0">系统管理员</option>
						<option value="1">产品管理员</option>
						<option value="2">普通用户</option>
					</select>
					<input type="hidden" name="id" value="${id}"/>
					<button class= "btn btn-primary" id = "cancel" onclick = "cancelAdd()">取消</button>
					<button class="btn btn-primary" id = "save" onclick = "verify()">保存</button>
				</form>
			</div>
		<jsp:include page="commonpart/containerStart.jsp"></jsp:include>
		<jsp:include page="commonpart/footer.jsp"></jsp:include>
		<script type="text/javascript">
			/* $(document).ready(function(){
	
			}); */
			
			function verify()
			{
				$("#formEditUser").attr("action","saveEditUser");
			}
			
			function cancelAdd()
			{
				$("#formEditUser").attr("action","users");
			}
		</script>
	</body>
</html>