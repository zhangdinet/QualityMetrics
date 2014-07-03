<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>登录</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap-theme.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico" type="image/x-icon" />
		<link rel="Bookmark" href="${pageContext.request.contextPath}/img/favicon.ico" />
	</head>
	<body id="bodyLogin">
		<jsp:include page="commonpart/headerLogoName.jsp"></jsp:include>

			<div id="divLogin">
				<form method = "post" action = "login" id="frmLogin">
					<div id="divTitle">欢迎登录QMS</div>
					<div id="divSubTitle">远景软件产品质量衡量体系系统</div>
					<input type="text" class="form-control" name="username" style="height:40px;width:300px;margin-bottom:15px;margin-top:30px" placeholder="用户名" required autofocus>
					<input type="password" class="form-control" name="password" style="height:40px;width:300px;margin-top:20px" placeholder="密码" required>
					<div style="width:40%; margin-left:auto; margin-right:auto;margin-top:20px;text-align:center">
						<button class="btn btn-primary" style="height:40px;width:80px;margin-right:10px" type="submit">确定</button>
						<button class="btn btn-primary" style="height:40px;width:80px;margin-left:10px;" type="reset">取消</button>
					</div>
				</form>
			</div>
		<jsp:include page="commonpart/footer.jsp"></jsp:include>
	</body>
</html>