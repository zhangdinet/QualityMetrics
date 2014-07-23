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
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
	</head>
	<body>
		<jsp:include page="commonpart/headerLogoName.jsp"></jsp:include>
		<jsp:include page="commonpart/mainMenu.jsp"></jsp:include>
		<div class="detailwrapper">
			<div class="projectdetail">
				<h1>系统管理</h1>
				<form id="formUser" method="post">
					<div>
						<label>用户名</label>
						<input name="username" id="username" class="form-control" value="${username}"></input>
					</div>
					<div>
						<label>管理员</label>
						<c:choose>
							<c:when test="${flag_admin == 1}">
								<input type="radio" name="flag_admin" class = "radidItem" checked="checked" value="1"><label>是</label></input>
								<input type="radio" name="flag_admin" class = "radidItem" value="0"><label>否</label></input>
							</c:when>
							<c:otherwise>
								<input type="radio" name="flag_admin" style="font-size:16px" class = "radidItem"  value="1"><label>是</label></input>
								<input type="radio" name="flag_admin" style="font-size:16px" class = "radidItem" checked="checked" value="0"><label>否</label></input>
							</c:otherwise>
						</c:choose>
					</div>
					<div>
						<label>管理的产品模块</label>
						<select id="select" name="select" multiple="multiple" class="form-control">
							<c:forEach var = "item" items="${projectList}">
								<c:choose>
										<c:when test="${item.project_id eq project_id}">
											<option value="${item.project_id}" selected="selected">${item.project_name }</option>
										</c:when>
										<c:otherwise>
											 <option value="${item.project_id}">${item.project_name }</option>
										</c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
					</div>
					<div>
						<input type="hidden" name="flag_admin_hide" value="${flag_admin }"></input>
						<input type="hidden" name="user_id" value="${user_id}"></input>
						<%
							String name=(String)request.getParameter("user_name");
							System.out.println(name);
							if(name==null || name=="")
							{
						%>
 							<input type="button" class="btn btn-primary" value="添加" onclick="saveNewUser()"/>
						<%	}
							else
							{
						%>
							<input type="button" class="btn btn-primary" value="更新" onclick="saveModifyUser()"/>
						<%	}	%>
						<input type="button" class="btn btn-primary" value="返回" onclick="cancel()"/>
					</div>
				</form>
			</div>
			<div class="project_tipLabel" id="user_tipLabel">
				<c:if test="${modifyResult eq 'ok'}">修改成功！</c:if>
				<c:if test="${modifyResult eq 'err'}">修改失败！</c:if>
			</div>
		</div>
		<div class="footer">
			<jsp:include page="commonpart/footer.jsp"></jsp:include>
		</div>
		<script type="text/javascript">
			$(document).ready(function(){
				var flag_admin = $("input[name='flag_admin_hide']").val();
				if(flag_admin == 0){
					$("#select").attr("disabled",true);
				}else{
					$("#select").attr("disabled",false);
				}
			});
			//非管理员不能选择项目
			$(".radidItem").change(function(){
				var value = $("input[name='flag_admin']:checked").val();
				if(value == '0'){
					$("#select").attr("disabled",true);
				}else{
					$("#select").attr("disabled",false);
				}
			});

			function saveNewUser()
			{
				if(checkEmpty())
				{
					$("#formUser").attr("action","saveNewUser");
					$("#formUser").submit();
				}
				else
				{
					$("#username").focus();
				}
			}
			
			function saveModifyUser()
			{
				if(checkEmpty())
				{
					$("#formUser").attr("action","saveModifyUser");
					$("#formUser").submit();
				}
				else
				{
					$("#username").focus();
				}
			}
			
			function cancel()
			{
				$("#formUser").attr("action","systemsettinglist");
				$("#formUser").submit();
			}
			
			function checkEmpty()
			{
				if( ($("#username").val()==null) || $("#username").val()=='')
				{
					return false;
				}
				return true;
			}
		</script>
	</body>
</html>