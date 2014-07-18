<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
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
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.1.js"></script>
	</head>
	<body>
		<jsp:include page="commonpart/headerLogoName.jsp"></jsp:include>
		<jsp:include page="commonpart/navMenu.jsp"></jsp:include>
		<div class="detailwrapper">
			<div class="projectdetail">
				<h1>系统管理</h1>
				<form id="formUser" method="post">
					<div>
						<label>用户名</label>
						<input name="updateUsername" id="updateUsername" readonly="readonly" class="form-control" value="${updateUsername}" />
					</div>
					<div>
						<label>产品模块管理员</label>
						<input type="radio" name="role" class = "radidItem" checked="checked" value="1"><label>是</label></input>
						<input type="radio" name="role" class = "radidItem" value="2"><label>否</label></input>
					</div>
					
					<div>
						<label>管理的产品模块</label>
						<select id="select" name="select" class="form-control">
							<c:forEach var = "item" items="${lstProjectDto}">
								<option value="${item.project_id}">${item.project_name }</option>
							</c:forEach>
						</select>
					</div>
					<div>
						<input type="button" class="btn btn-primary" value="更新" onclick="saveUpdateUser()"/>
						<input type="button" class="btn btn-primary" value="返回" onclick="cancel()"/>
						<label id="labelTip">${strTip}</label>
					</div>
					<input type="hidden" name="hiddenUserID" id="hiddenUserID" value="${userID}" />
				</form>
			</div>
		</div>
		<div class="footer">
			<jsp:include page="commonpart/footer.jsp"></jsp:include>
		</div>
		<script type="text/javascript">
			//非管理员不能选择项目
			$(".radidItem").change(function(){
				var value = $("input[name='role']:checked").val();
				if(value == '1')
				{
					$("#select").attr("disabled",false);
				}
				else
				{
					$("#select").attr("disabled",true);
				}
			});
			
			function saveUpdateUser()
			{
				if(checkEmpty())
				{
					$("#formUser").attr("action","saveUpdateUser");
					$("#formUser").submit();
				}
				else
				{
					$("#updateUsername").focus();
				}
			}
			
			function cancel()
			{
				$("#formUser").attr("action","systemsettinglist");
				$("#formUser").submit();
			}
			
			function checkEmpty()
			{
				if( ($("#updateUsername").val()==null) || $("#updateUsername").val()=='')
				{
					return false;
				}
				return true;
			}
		</script>
	</body>
</html>