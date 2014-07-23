<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>系统管理</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap-theme.css">
		<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.4.1/build/cssreset/cssreset-min.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico" type="image/x-icon"/>
		<link rel="Bookmark" href="${pageContext.request.contextPath}/img/favicon.ico" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
	</head>
	<body>
		<jsp:include page="commonpart/headerLogoName.jsp"></jsp:include>
		<jsp:include page="commonpart/mainMenu.jsp"></jsp:include>
		<jsp:include page="commonpart/containerStart.jsp"></jsp:include>
		
		<div id="divAddUser">
			<h3>添加用户</h3>
			<form id="formAddUser">
				<label for="username">用户名</label>
				<input type="text" class="form-control" id="username" name="username" placeholder="用户名"/>
				<label for="role">角色</label>
				<select id="role" name="role" class="form-control">
					<option value="0">系统管理员</option>
					<option value="1">产品管理员</option>
					<option value="2">普通用户</option>
				</select>
				<label for="password">密码</label>
				<input type="password" class="form-control" id="password" name="password" placeholder="密码"/>
				<label for="password">确认密码</label>
				<input type="password" class="form-control" id="confirmPassword" name="confirmPassword" placeholder="确认密码"/>
				<button class= "btn btn-primary" id = "cancel" onclick = "cancelAdd()">取消</button>
				<button class="btn btn-primary" id = "save" onclick = "verify()">保存</button>
			</form>
		</div>
		<jsp:include page="commonpart/containerEnd.jsp"></jsp:include>
		<jsp:include page="commonpart/footer.jsp"></jsp:include>
	</body>
	<script type="text/javascript">
		function verify()
		{
			if( checkEmpty() && verifyPassword() )
			{
				$("#formAddUser").attr("action","saveNewUser");
			}
			else
			{
				alert("请正确填写信息!");
			}
		}
		
		function checkEmpty()
		{
			var username = $.trim($("#username").val());
			var password = $.trim($("#password").val());
			var confirmPassword = $.trim($("#confirmPassword").val());
			if( (username==null) || (password==null) || (confirmPassword==null) || (username=="") || (password=="") || (confirmPassword=="") )
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		
		function verifyPassword()
		{
			var password=$.trim($("#password").val());
			var confirmPassword=$.trim($("#confirmPassword").val());
			if(password == confirmPassword)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		
		function cancelAdd()
		{
			$("#formAddUser").attr("action","users");
		}
	</script>
</html>

