<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>排行榜</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap-theme.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico" type="image/x-icon"/>
		<link rel="Bookmark" href="${pageContext.request.contextPath}/img/favicon.ico" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.1.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/ranking_chart.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/qualitymetrics.js" charset="gb2312"></script>
	</head>
	<body>
		<jsp:include page="commonpart/headerLogoName.jsp"></jsp:include>
		<jsp:include page="commonpart/navMenu.jsp"></jsp:include>
		<jsp:include page="commonpart/containerStart.jsp"></jsp:include>
			<h3>软件产品质量龙虎榜</h3>
			<div style="margin-top:20px">
				<span id="spanBrowseHistory" style="float:right">查看往期
					<select name="rankingPeriod" id="rankingPeriod" sytle="float:right">
						<option value="0">请选择</option>
						<c:forEach var="item" items="${rankingList}" varStatus="status">
							<c:choose>
								<c:when test="${item.rank_id == rank_id}">
									<option selected="selected" value="${item.rank_id}">${item.rank_period}</option>
								</c:when>
								<c:otherwise>
									<option value="${item.rank_id}">${item.rank_period}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</span>
			</div>
			<div id="container" style="min-width:800px;height:400px;margin-bottom:10px"></div>
			
			<div id="rankingsContent" style="margin-top:80px">
					<label>筛选产品模块</label>
					<input name="selectProject" type="text" id="txtSelectProject" placeholder="输入后请回车" onchange="selectProject(this)"/>
					<table id="tbl_rankings">
						<tr>
							<th>排名</th>
							<th>产品模块名称</th>
							<th>总平均分</th>
							<th>详情</th>
						</tr>
						<c:set value="1" var="rankNumber" />
						<c:forEach items="${projectList}" var="item" varStatus="status">
							<tr>
								<td>
									<c:choose>
										<c:when test="${status.count==1}">
											${status.count}
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${item.avg_score eq projectList[status.index-1].avg_score}">
													<c:out value="${rankNumber}" />
												</c:when>
												<c:otherwise>
													${status.count}
													<c:set value="${status.count}" var="rankNumber"/>
												</c:otherwise>
											</c:choose>
										</c:otherwise>
									</c:choose>
								</td>
								<td>${item.project_name}</td>
								<td>${item.avg_score}</td>
								<td>
									<c:choose>
										<c:when test="${rank_id != '0'}">
											<a href="showProjectHistoryDetail?project_id=${item.project_id}&project_name=${item.project_name}&rank_id=${rank_id}">查看详情</a>
										</c:when>
										<c:otherwise>
											<a href="showProjectDetail?project_id=${item.project_id}&project_name=${item.project_name}&rank_id=${rank_id}">查看详情</a>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</c:forEach>
					</table>
			</div>
			

			<form id="form" class = "print">
				<input type="hidden" name="selectedPeriodId"></input>
				<input type="hidden" name="selectedPeriodName"></input>
				<input type="submit" value="打印预览" class="btn btn-primary" style="float:right;" onclick="printRankings()"></input>
			</form>

		<jsp:include page="commonpart/containerEnd.jsp"></jsp:include>
		<script type="text/javascript">
			$(document).ready(function()
			{
				/* showNewestRankings(); */
				showRankingChart();
				//changePeriod();
			});
			
			$('select[name="rankingPeriod"]').change(function(){
				var projectName=[];
				var avgScore=[];
				var selectedRankId = $("#rankingPeriod").val();
				location.href="ranklist?rank_id="+selectedRankId;
				
			});
		
			function selectProject(name)
			{
				var tableRank = document.getElementById('tbl_rankings');
				var rowLength = tableRank.rows.length;
				var projectName = name.value.toLowerCase();
				var columnIndex = 1;
				for(var i=1;i<rowLength;i++)
				{
					var proName = tableRank.rows[i].cells[columnIndex].innerHTML.toLowerCase();
					if(proName.indexOf(projectName)!=-1)
					{
						tableRank.rows[i].style.display = '';
					}
					else
					{
						tableRank.rows[i].style.display = 'none';
					}
				}
			}
		
			function printRankings()
			{
				var selectedRankId = $('select[name="rankingPeriod"]').children('option:selected').val();
				var input1 = $("input[name='selectedPeriodId']");
				input1.attr('value',selectedRankId);
				var input2 = $("input[name='selectedPeriodName']");
				var selectedPeriodName = $('select[name="rankingPeriod"]').children('option:selected').html();
				input2.attr('value',selectedPeriodName);
				$("#form").attr("action","printRankings");
				$("#form").submit();
			}
			
			function showRankingChart(){
				var projectName=[];
				var avgScore=[];
				var menuIndex=-1;
				$.ajax({
					url: 'showRankingChart',
					data: {'rank_id':$("#rankingPeriod").val()},
					type: 'post',
					success:function(data){
						$.each(JSON.parse(data),function(i,d){
								projectName.push([d.project_name]);
								avgScore.push([d.avg_score]);
						});
						chart.series[0].setData(avgScore,true);
						chart.xAxis[0].setCategories(projectName);
						projectName=[];
						avgScore=[];
					}
				});
			}
		</script>
	</body>
</html>