<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Sprint基础数据录入</title>
		<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.4.1/build/cssreset/cssreset-min.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap-theme.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico" type="image/x-icon" />
		<link rel="Bookmark" href="${pageContext.request.contextPath}/img/favicon.ico" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.1.js"></script>
	</head>
<body>
	<jsp:include page="commonpart/headerLogoName.jsp"></jsp:include>
	<jsp:include page="commonpart/navMenu.jsp"></jsp:include>
	<jsp:include page="commonpart/containerStart.jsp"></jsp:include>
			
		<form id="formProduct" method="post">
				<h3>产品基础数据录入</h3>
		
					<div>
						<label>产品名称</label>
						<c:if test="${project_name eq ''}">
							<input name="project_name" id="project_name" value="${project_name }" class="form-control"></input>
						</c:if>
						<c:if test="${project_name ne ''}">
							<input name="project_name" id="project_name" readonly="readonly" value="${project_name }" class="form-control" style="color: grey"></input>
						</c:if>
					</div>
					
					<div>
						<label>TestLink名称</label>
						<select name="testlinkName" class="form-control">
							<option value="choose">请选择</option>
							<c:forEach var="item" items="${lstTestlinkName}" varStatus="status">
								<c:choose>
									<c:when test="${item!=project_name_tl}">
										<option value="${item}">${item}</option>
									</c:when>
									<c:otherwise>
										<option value="${item}" selected="selected">${item}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
					</div>
					
					<div>
						<label>Redmine名称</label>
						<select name="redmineName" class="form-control">
							<option value="choose">请选择</option>
								<c:forEach var="item" items="${lstRedmineName}" varStatus="status">
									<c:choose>
										<c:when test="${item!=project_name_rm}">
											<option value="${item}">${item}</option>
										</c:when>
										<c:otherwise>
											<option value="${item}" selected="selected">${item}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
						</select>
					</div>
					
					<div>
						<label>Redmine中的工程技术支持项目名称</label>
						<select name="redmineSupportName" class="form-control">
							<option value="choose">请选择</option>
							<c:forEach var="item" items="${lstRedmineSupportName}" varStatus="status">
								<c:choose>
									<c:when test="${item!=project_name_rm_support}">
										<option value="${item}">${item}</option>
									</c:when>
									<c:otherwise>
										<option value="${item}" selected="selected">${item}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
					</div>
					
					<div>
						<input type="hidden" name="project_id" value="${project_id }"></input>
						<input type="button" value="保存" class="btn btn-primary" onclick="send(0)" />
						<input type="button" value="返回" class="btn btn-primary" onclick="send(1)" />
					</div>
	
			</form>

		<div class="project_tipLabel" id="project_tipLabel">
			<c:if test="${updateResult eq 'ok'}">修改成功！</c:if>
			<c:if test="${updateResult eq 'err'}">修改失败！</c:if>
		</div>

	<jsp:include page="commonpart/containerEnd.jsp"></jsp:include>
	<jsp:include page="commonpart/footer.jsp"></jsp:include>

	
	<script type="text/javascript">
		$(document).ready(function(){
			if($("#project_name").val())
			{
				$("#formProduct").attr("action", "saveModifyProject");
			}
			else
			{
				$("#formProduct").attr("action", "saveNewProject");
			}
		});
		function send(i) {
			if (i == 0) {
				var project_name = $("input[name='project_name']").val();
				var project_name_tl = $("select[name='testlinkName']").val();
				var project_name_rm = $("select[name='redmineName']").val();
				var project_name_rm_support = $("select[name='redmineSupportName']").val();
				
				if (project_name == "" || project_name_tl == ""|| project_name_rm == "" || project_name_rm_support == "") {
					$("#project_tipLabel").html("请完整填写信息！");
					return;
				}
			} else if (i == 1) {
				$("#formProduct").attr("action", "projectlist");
			}
			$("#formProduct").submit();
		}
	</script>
</body>
</html>