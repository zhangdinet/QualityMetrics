<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>${project_name} Sprint质量衡量分数历史</title>
		<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.4.1/build/cssreset/cssreset-min.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap-theme.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico" type="image/x-icon" />
		<link rel="Bookmark" href="${pageContext.request.contextPath}/img/favicon.ico" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.PrintArea.js"></script>
	</head>
	<body>
		<jsp:include page="commonpart/headerLogoName.jsp"></jsp:include>
		<jsp:include page="commonpart/mainMenu.jsp"></jsp:include>
		<jsp:include page="commonpart/containerStart.jsp"></jsp:include>
		<div id="divSprintPrint">
			<h3>${project_name} ${sprint.sprint_name}质量衡量分数表</h3>
			<label>平均分：
			<c:choose>
				<c:when test="${sprint.sprint_score eq -1}">NA</c:when>
				<c:otherwise>${sprint.sprint_score}</c:otherwise>
			</c:choose></label>
			<br/>
			<label>Sprint区间：
			<fmt:formatDate value="${sprint.sprint_startdate}" pattern="yyyy.MM.dd "/>
			 --
			<fmt:formatDate value="${sprint.sprint_enddate}" pattern="yyyy.MM.dd "/>
			</label>
			<table>
			<tr>
			<th width="15%">衡量维度</th>
			<th>衡量点</th>
			<th>5分制得分</th>
			<th>原始得分</th>
			</tr>
			<tr>
			<td rowspan="2">过程质量</td>
			<td>IPD PDT过程质量</td>
			<td>
			<c:choose>
				<c:when test="${sprint.ipd_score eq -1}">NA</c:when>
				<c:otherwise>${sprint.ipd_score}</c:otherwise>
			</c:choose>
			</td>
			<td>NA
			</td>
			</tr>
			<tr>
			<td>IPD LMT过程质量</td>
			<td>
			<c:choose>
				<c:when test="${sprint.lmt_score eq -1}">NA</c:when>
				<c:otherwise>${sprint.lmt_score}</c:otherwise>
			</c:choose>
			</td>
			<td>
			NA
			</td>
			</tr>
			<tr>
				<td>内部质量</td>
				<td>代码整体质量</td>
				<td>${sprint.sonar_score}</td>
				<td>
				<c:choose>
					<c:when test="${sprint.sonar_score_origin eq -1}">NA</c:when>
					<c:otherwise>Sonar代码质量扫描：${sprint.sonar_score_origin} 分</c:otherwise>
				</c:choose>
			</td>
			</tr>
			<tr>
				<td rowspan="7">外部质量</td>
				<td>测试通过率</td>
				<td>${sprint.test_pass_score}</td>
				<td>
					<c:choose>
						<c:when test="${sprint.test_pass_score_origin eq -1}">NA</c:when>
						<c:otherwise>${sprint.test_pass_score_origin}%</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td>用例可执行率</td>
				<td>${sprint.tc_exec_score}</td>
				<td>
					<c:choose>
						<c:when test="${sprint.tc_exec_score_origin eq -1}">NA</c:when>
						<c:otherwise>${sprint.tc_exec_score_origin}%</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td>缺陷新增率</td>
				<td>${sprint.bug_new_score}</td>
				<td>
					<c:choose>
						<c:when test="${sprint.bug_new_score_origin eq -1}">NA</c:when>
						<c:otherwise>${sprint.bug_new_score_origin}%</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
			<td>问题返工率</td>
			<td>${sprint.bug_reopen_score}</td>
			<td>
			<c:choose>
				<c:when test="${sprint.bug_reopen_score_origin eq -1}">NA</c:when>
				<c:otherwise>${sprint.bug_reopen_score_origin}%</c:otherwise>
			</c:choose>
			</td>
			</tr>
			<tr>
			<td>缺陷遗漏率</td>
			<td>${sprint.bug_escape_score}</td>
			<td>
			<c:choose>
				<c:when test="${sprint.bug_escape_score_origin eq -1}">NA</c:when>
				<c:otherwise>${sprint.bug_escape_score_origin}%</c:otherwise>
			</c:choose>
			</td>
			</tr>
			<tr>
			<td>补丁发布率（年度）</td>
			<td>${rate_patch_score }</td>
			<td>
			<c:choose>
				<c:when test="${rate_patch eq -1}">NA</c:when>
				<c:otherwise>${rate_patch }%</c:otherwise>
			</c:choose>
			</td>
			</tr>
			<tr>
			<td>技术支持率</td>
			<td>${sprint.rate_support_score}</td>
			<td>
			<c:choose>
				<c:when test="${sprint.rate_support_score_origin eq -1}">NA</c:when>
				<c:otherwise>${sprint.rate_support_score_origin}%</c:otherwise>
			</c:choose>
			</td>
			</tr>
			<tr>
			<td>使用质量</td>
			<td>客户满意度</td>
			<td>${sprint.rate_ce_score}</td>
			<td>
			<c:choose>
				<c:when test="${sprint.rate_ce_score_origin eq -1}">NA</c:when>
				<c:otherwise>${sprint.rate_ce_score_origin}</c:otherwise>
			</c:choose>
			</td>
			</tr>
			</table>
		</div>
		
			
	 
			<form class="sprint_details_buttons" id="form" method="post">
				<div class="sprintdetail_buttons">
				<input type="hidden" value="${sprint.sprint_id}" name="sprint_id"/>
				<input type="hidden" value="${sprint.project_id}" name="project_id"/>
				<input type="hidden" value="${rank_id}" name="rank_id"/>

				<%
					if(session.getAttribute("username").equals("admin"))
					{
				%>
						<input type="button" class="btn btn-primary" value="更新" onclick="updateSprint()"/>
				<%
					}
				%>
				
				<input type="button" class="btn btn-primary" value="返回" onclick="send(1)"/>
				<input type="button" class="btn btn-primary" value="打印" onclick="send(2)"/>
				<span id="update_flag" class="update_flag">
					<c:if test="${updateOK eq 'updateOK'}">更新成功！</c:if>
				</span>
				</div>
			</form> 
		<jsp:include page="commonpart/containerEnd.jsp"></jsp:include>
		<jsp:include page="commonpart/footer.jsp"></jsp:include>
		
		<script type="text/javascript">
		
			function send(i){
				if(i==0)
				{
					$("#form").attr("action","updateSprint");
					$("#form").submit();
				}else if(i==1)
				{
					window.history.go(-1);
					return false;
				}else{
					$("#divSprintPrint").printArea();
					return false;
				}
			}
			
			function updateSprint()
			{
				$("#update_flag").html("正在更新，请稍等...");
				var project_id = $("input[name = 'project_id']").val();
				var sprint_id = $("input[name = 'sprint_id']").val();
				var rank_id = $("input[name = 'rank_id']").val();
				if(rank_id==""||rank_id==null)
				{
					window.location="updateSprintScore?project_id="+project_id+"&sprint_id="+sprint_id;
				}
				else
				{
					window.location="updateSprintHistory?project_id="+project_id+"&sprint_id="+sprint_id+"&rank_id="+rank_id;
				}
			}
		</script>
	</body>
</html>