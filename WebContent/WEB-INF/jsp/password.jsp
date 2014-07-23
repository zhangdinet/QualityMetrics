<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>更改密码</title>
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
		<jsp:include page="commonpart/navMenu.jsp"></jsp:include>
		<jsp:include page="commonpart/containerStart.jsp"></jsp:include>
		<h3 style="width:200px;margin-left:auto;margin-right:auto;">更改密码</h3>
		<div class="loginBox">
			<div class="loginBoxCenter">
				<p><label for="password">原密码</label></p>
				<p><input type="password" id="oldPassword" name="password" class="loginInput" placeholder="原密码" value="" /></p>
				
				<p><label for="password">新密码</label></p>
				<p><input type="password" id="newPassword" name="password" class="loginInput" placeholder="新密码" value="" /></p>
				
				<p><label for="password">确认密码</label></p>
				<p><input type="password" id="confirmPassword" name="password" class="loginInput" placeholder="确认新密码" value="" /></p>
				<p><label for="password" id = "tipLabel" style="color:red"></label></p>
			</div>
			<div class="loginBoxButtons">
				<button class="btn btn-primary" id = "save" onclick = "modifyPassword()">保存</button>
				<button class= "btn btn-primary" id = "cancel" onclick = "cancelModifyPassword()">取消</button>
			</div>
		</div>
	<jsp:include page="commonpart/containerEnd.jsp"></jsp:include>
	<jsp:include page="commonpart/footer.jsp"></jsp:include>
	<script type="text/javascript">
		function modifyPassword(){
			var oldPwd = $("#oldPassword").val();
			var newPwd = $("#newPassword").val();
			var confirmPwd = $("#confirmPassword").val();
			var password = "<%=session.getAttribute("password")%>";
			if(password == null){
				window.location = "login";
		    	return;
			}
	
			if(oldPwd == ""||newPwd ==""||confirmPwd == ""){
				$("#tipLabel").html("相关记录不能为空！");
				return;
			}
			//alert(password);
			if(oldPwd != password){
				$("#tipLabel").html("原密码输入错误！");
				return;
			}
			if(newPwd == confirmPwd){
				var username = "<%=session.getAttribute("username")%>";
				$.ajax({
					   url:'modifyPassword',
					   type:'get',
					   data:{"username":username,"newPassword":confirmPwd},
					   success:function(data){
						    if(data.indexOf('<html>')>-1){
						    	window.location = "login";
						    	return;
							}
					    	$("#changePassword").html(data);
					   },
					   error:function(data){
						   $("#tipLabel").html("系统异常，请稍后再试！");
					   },
				});
			}else{
				$("#tipLabel").html("新密码前后输入不一致！");
			}
		}
		
		function cancelModifyPassword(){
			$("#oldPassword").val("");
		 	$("#newPassword").val("");
		 	$("#confirmPassword").val("");
		 	$("#tipLabel").html("");
		}
	</script>
	</body>
</html>