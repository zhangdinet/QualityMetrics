<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>${project_name} Sprint质量衡量分数历史</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap-theme.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery-ui.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
	<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico"  type="image/x-icon" />
	<link rel="Bookmark" href="${pageContext.request.contextPath}/img/favicon.ico" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/sprint_chart.js"></script>
</head>
<body>
	<jsp:include page="commonpart/headerLogoName.jsp"></jsp:include>
	<jsp:include page="commonpart/mainMenu.jsp"></jsp:include>
	<jsp:include page="commonpart/containerStart.jsp"></jsp:include>

		<h3 style="margin-bottom:20px">${project_name} Sprint质量衡量分数表</h3>
		<div id="container" style="min-width:800px;height:400px;"></div>
		<table>
			<tr>
			<th>Sprint</th>
			<th>评分</th>
			<th>详情</th>
			</tr>
			<c:forEach items="${sprintList}" var="item">
				<tr>
					<td>${item.sprint_name}</td>
					<td>
						<c:choose>
							<c:when test="${item.sprint_score eq -1}">NA</c:when>
							<c:otherwise>${item.sprint_score}</c:otherwise>
						</c:choose>
					</td>
					<td>
						<c:choose>
							<c:when test="${rank_id !=0}">
								<a href="showSprintHistoryDetail?sprint_id=${item.sprint_id}&rank_id=${rank_id }">查看详情</a>
							</c:when>
							<c:otherwise>
								<a href="showSprintDetail?sprint_id=${item.sprint_id}">查看详情</a>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
		</table>

		<form action="ranklist">
			<div class="project_detail_bottom"></div>
			<input type="submit" class="btn btn-primary" value="返回"/>
		</form>
		<input type="hidden" id="project_id" value="${project_id}" name="project_id"/>
		<input type="hidden" id="rank_id" value="${rank_id}" name="rank_id"/>
	<jsp:include page="commonpart/containerEnd.jsp"></jsp:include>
	<jsp:include page="commonpart/footer.jsp"></jsp:include>
	<script type="text/javascript">
	var sprintScore=[];
	var sprintName=[];
		$(document).ready(function(){
			$("tr").addClass("sucesss");
			showSprintChart();
		});
		
		
		function showSprintChart(){
			$.ajax({
				url: 'showSprintChart',
				data:
				{
					'rank_id' : $("#rank_id").val(),
					'project_id' : $("#project_id").val()
				},
				type: 'post',
				success:function(data)
				{
					if(data==null || data=="")
					{
						$("#container").css('display','none');
						return;
					}
					
					$.each(JSON.parse(data),function(i,d)
					{
						if(d.sprint_score!=-1)
						{
							sprintName.push([d.sprint_name]);
							sprintScore.push([d.sprint_score]);
						}
					});
					chart.xAxis[0].setCategories(sprintName);
					chart.series[0].setData(sprintScore,true);
					
					//zhangdi todo 有时间考虑修改不显示的方式===
					if(sprintName.length==1 ||sprintName.length==0 )
					{
						$("#container").css('display','none');
					}
					sprintScore=[];
					sprintName=[];
				},
				error:function()
				{
					//alert("showSprintChart error!");
				}
			});
		}
	</script>
</body>
</html>