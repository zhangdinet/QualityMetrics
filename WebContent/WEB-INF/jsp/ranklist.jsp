<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.env.qualitymetrics.common.SysUtil"%>
<%@page import="com.env.qualitymetrics.dto.ProjectDto"%>
<%@ page import="java.util.*" %>
<%@ page import="com.env.qualitymetrics.dto.RankingDto" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>排行榜</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap-theme.css">
		<%-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.dataTables.css"> --%>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico" type="image/x-icon"/>
		<link rel="Bookmark" href="${pageContext.request.contextPath}/img/favicon.ico" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
		<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dataTables.js"></script> --%>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/ranking_chart.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/qualitymetrics.js" charset="gb2312"></script>
	</head>
	<body>
		<jsp:include page="commonpart/headerLogoName.jsp"></jsp:include>
		<jsp:include page="commonpart/mainMenu.jsp"></jsp:include>
		<div id="mainDivContent">
			<h3 id="hTitle">软件产品质量龙虎榜</h3>
			<div>
				<span id="spanBrowseHistory" style="float:right">查看往期
					<select name="rankingPeriod" id="rankingPeriod" sytle="float:right">
						<c:choose>
							<c:when test="${selectID == -1}">
								<option value="-1" selected disabled style="color:#888888;display:none">请选择</option>
								<option value="0">最新</option>
							</c:when>
							<c:when test="${selectID == 0}">
								<option selected value="0">最新</option>
							</c:when>
							<c:otherwise>
								<option value="0">最新</option>
							</c:otherwise>
						</c:choose>
						
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
			<br>
			
			<div id="container" style="min-width:800px;max-width:960px;height:400px;padding:10px;margin-left:auto;margin-right:auto;margin-bottom:10px;margin-top:20px;border:2px solid #435871"></div>
			
			<div id="rankingsContent" style="margin-top:80px">
					<label>筛选产品模块</label>
					<input name="selectProject" type="text" id="txtSelectProject" style="padding-left:8px" placeholder="输入后请回车" onchange="selectProject(this)"/>
					<form id="form" class = "print" style="float:right">
						<input type="hidden" name="selectedPeriodId"></input>
						<input type="hidden" name="selectedPeriodName"></input>
						<input type="hidden" name="filterName"></input>
						<input type="submit" value="打印预览" class="btn btn-primary" style="float:right;" onclick="printRankings()"></input>
					</form>
					<table id="tbl_rankings">
						<tr>
							<th>排名</th>
							<th>产品模块名称</th>
							<th>总平均分</th>
							<th>详情</th>
						</tr>
						<c:set value="1" var="rankNumber" />
						<%
							int rankNumber=1;
							List<ProjectDto> projectList=(List<ProjectDto>)request.getAttribute("projectList");
							int projectCount = projectList.size();
							String strRank=request.getParameter("rank_id");
							Integer rank_id=1;
							if(strRank!=null && !strRank.equals("0"))
							{
								rank_id=Integer.parseInt(strRank);
							}
							String strHref="";
							for(int i=0;i<projectCount;i++)
							{
								ProjectDto dto=projectList.get(i);
								ProjectDto prevDto;
								int projectID=dto.getProject_id();
								String projectName=dto.getProject_name();
								String encodeProjectName=SysUtil.encodeWithUtf8(projectName);
						%>
								<tr>
									<td>
						<%
									if(i==0)
									{
						%>
										<%= i+1 %>
						<%			
									}
									else
									{
										prevDto=projectList.get(i-1);
										if(dto.getAvg_score()==prevDto.getAvg_score())
										{
						%>		
											<%= rankNumber %>
						<%			
										}
										else
										{
											rankNumber=i+1;
						%>					
											<%= i+1 %>
						<%
										}
									}
						%>
									</td>
									<td><%=projectName %></td>
									<td><%=dto.getAvg_score()%></td>
									<td>
						<%
									if(strRank==null || strRank.equals("0"))
									{
										strHref="showProjectDetail?project_id=" + projectID + "&project_name=" + encodeProjectName + "&rank_id=" + rank_id;
									}
									else
									{
										strHref="showProjectHistoryDetail?project_id=" + projectID + "&project_name=" + encodeProjectName + "&rank_id=" + rank_id;
									}
						%>
									<a href="<%= strHref%>">查看详情</a></td></tr>
						<%
							}
						%>
					</table>
			</div>

			
		</div>

		<script type="text/javascript">
			$(document).ready(function()
			{
				/* showNewestRankings(); */
				$("#txtSelectProject").val('');
				showRankingChart();
				//changePeriod();
				/* $("#tbl_rankings").dataTable(); */
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
				
				var filterName=$("input[name='selectProject']").val();
				var inputFilter=$("input[name='filterName']");
				inputFilter.attr('value',filterName);
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
					},
					error:function(data){
					}
				});
			}
		</script>
	</body>
</html>