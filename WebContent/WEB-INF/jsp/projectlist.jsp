<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>员工质量KPI</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap-theme.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery-ui-1.10.4.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico" type="image/x-icon"/>
		<link rel="Bookmark" href="${pageContext.request.contextPath}/img/favicon.ico" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.1.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-ui-1.10.4.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.ui.datepicker-zh-CN.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/exporting.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/qualitymetrics.js" charset="gb2312"></script>
	</head>
	<body>
		<jsp:include page="commonpart/headerLogoName.jsp"></jsp:include>
		<jsp:include page="commonpart/navMenu.jsp"></jsp:include>
		<jsp:include page="commonpart/containerStart.jsp"></jsp:include>
		
		<h3>产品模块信息</h3>
		<form action = "addProductProject" style="float:right">
			<c:if test="${sessionScope.flag_admin == 'yes' and sessionScope.project_id == 0}">
				<input type="submit" id="addProductProject" class="btn btn-primary" value="添加产品类项"></input>
			</c:if>
		</form>
		<form action = "addModuleProject" style="float:right">
			<c:if test="${sessionScope.flag_admin == 'yes' and sessionScope.project_id == 0}">
				<input type="submit" id="addModuleProject" class="btn btn-primary" value="添加模块类项"></input>
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
						<c:choose>
							<c:when test="${sessionScope.project_id == item.project_id or sessionScope.project_id == 0}">
								<c:choose>
									<c:when test="${item.project_flag==1}">
										<a href="showModifyProject?project_id=${item.project_id}&project_name=${item.project_name }">
											${item.project_name }
										</a>
									</c:when>
									<c:otherwise>
										<a href="showModifyProject?project_id=${item.project_id}&project_name=${item.project_name }&project_name_tl=${item.project_name_tl }&
											project_name_rm=${item.project_name_rm }&project_name_sn=${item.project_name_sn }&project_name_rm_support=${item.project_name_rm_support }">
											${item.project_name }
										</a>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								${item.project_name }
							</c:otherwise>
						</c:choose>
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
					<a href="sprintlist?project_id=${item.project_id}&project_name=${item.project_name}&pageNumber=1">Sprint详情</a>
				</td>
			</tr>
			</c:forEach>
		</table>
		<div id="settings_sprint"></div>
		<jsp:include page="commonpart/containerEnd.jsp"></jsp:include>
		<jsp:include page="commonpart/footer.jsp"></jsp:include>
		
		<script type="text/javascript">
			function showSprintByProjectId(project_id,project_name,pageNumber){
				$.ajax({
					url: 'showSettingsSprint',
					type: 'post',
					data: {"project_id":project_id,"project_name":project_name,"pageNumber":pageNumber},
					/* success:function(data){
					},
					error:function(){
					}, */
				});
			}

			function gotoSelectedPage()
			{
				var pageNumber = $('select[name="pageNumber"]').children('option:selected').val();
				var project_id = $("input[name='project_id']").val();
				var project_name = $("input[name='project_name']").val();
				showSprintByProjectId(project_id,project_name,pageNumber);
			}
		</script>
	</body>
</html>