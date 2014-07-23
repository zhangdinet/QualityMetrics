<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加模块</title>
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.4.1/build/cssreset/cssreset-min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico" type="image/x-icon" />
<link rel="Bookmark" href="${pageContext.request.contextPath}/img/favicon.ico" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
</head>
<body>
	<jsp:include page="commonpart/headerLogoName.jsp"></jsp:include>
	<jsp:include page="commonpart/navMenu.jsp"></jsp:include>
	<jsp:include page="commonpart/containerStart.jsp"></jsp:include>
		<form id="formModule" method="post">
			<h3>添加模块类项</h3>
			<div>
				<label>模块名称</label>
				<input name="project_name" id="project_name" value="${project_name }" class="form-control"></input>
			</div>
			<div>
				<label>TestLink名称</label>
				<select name="testlinkName" onchange="getSuiteNames()" class="form-control">
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
				<label>TestLink系统中TestSuite名称</label>
				<select id="topsuite_testlink" name="topsuite_testlink" multiple="multiple" style="height:120px" class="form-control">
					<c:forEach var="itemTest" items="${lstTestlinkSuiteName}">
							<c:set var="boolFlag" value="false"/>
							<c:set var="suite_name_new" value="${fn:replace(suite_name_tl, '<br>', ',')}"/>
							<c:set var="suite_names" value="${fn:split(suite_name_new,',')}"/>
								<c:forEach items="${suite_names}" var="suite_name">
									<c:if test="${itemTest eq fn:trim(suite_name)}">
										<c:set var="boolFlag" value="true"/>
									</c:if>
								</c:forEach>
							<c:choose>
							<c:when test="${boolFlag}">
								<option value="${itemTest}" selected="selected" >${itemTest}</option>
							</c:when>
							<c:otherwise>
								<option value="${itemTest}">${itemTest}</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
			</div>
			
			<div>
				<label>Redmine名称</label>
				<select name="redmineName" onchange="getCategoryNames()" class="form-control">
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
				<label>Redmine中Category名称</label>
				<select id="category_redmine" name="category_redmine" multiple="multiple" class="form-control">
					<c:forEach var="itemTest" items="${lstRedmineCategoryName}">
						<c:set var="boolFlag" value="${fn:contains(category_name_rm,itemTest)}"/>
						<c:choose>
							<c:when test="${boolFlag}">
								<option value="${itemTest}" selected="selected" >${itemTest}</option>
							</c:when>
							<c:otherwise>
								<option value="${itemTest}"> ${itemTest} </option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
			</div>
			
			<div>
				<label>Redmine工程技术支持类别名称</label>
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
				<input type="hidden" class="btn btn-primary" name="project_id" value="${project_id }" />
				<input type="button" class="btn btn-primary" value="保存" onclick="send(0)" />
				<input type="button" class="btn btn-primary" value="返回" onclick="send(1)" />
				<span id="spanResult">${saveResult}</span>
			</div>
		</form>
	<jsp:include page="commonpart/containerEnd.jsp"></jsp:include>
	<jsp:include page="commonpart/footer.jsp"></jsp:include>
	
	<script type="text/javascript">
	$(function(){
		$("#topsuite_testlink").children().each(function(){
			var flag=false;
			var str=$(this).attr('value');
			//判断select option的suite是否被选择
			$("#topsuite_testlink").find("option:selected").each(function(){
				var firstText=$(this).text();
				var secondText=firstText+"--";
				if(str.indexOf(secondText)>=0){
						flag=true;
					 }
				});
			if(flag==true)
				$(this).attr('selected','selected');
			});
		});
	
		function send(i)
		{
			if (i == 0)
			{
				var project_name = $("input[name='project_name']").val();
				var project_name_tl = $("select[name='testlinkName']").val();
				var project_name_rm = $("select[name='redmineName']").val();
				var project_name_rm_support = $("select[name='redmineSupportName']").val();
				var topsuite_name_tl = $('#topsuite_testlink option:selected').text();
				var category_name_rm = $('#category_redmine option:selected').text();
				if (project_name == "" || project_name_tl == "" || project_name_rm == "" || project_name_rm_support == "" || topsuite_name_tl == "" || category_name_rm == "")
				{
					$("#project_tipLabel").html("请完整填写信息！");
					return;
				}
				$("#formModule").attr("action", "saveNewModuleProject");
			}
			else if (i == 1)
			{
				$("#formModule").attr("action", "projectlist");
			}
			$("#formModule").submit();
		}
		
		
		//获取所选testlink项目的top suites
		function getSuiteNames()
		{
			var testProjectName = $("select[name='testlinkName']").val();
			var suiteNames;
			if(testProjectName==null)
			{
				return;
			}
			$.ajax({
				url : 'getSuiteNames',
				type : 'post',
				async: false,
				data : {
					'testProjectName' : testProjectName
				},
				success : function(data) {
					suiteNames=data;
					var arrDate = new Array();
					arrDate = suiteNames.split(",");
					$("#topsuite_testlink").empty();
					for(var i=0;i<arrDate.length;i++)
					{
						var strOption="<option value='" + arrDate[i]+ "'>" + arrDate[i] + "</option>";
						$("#topsuite_testlink").append(strOption);
					}
				},
				error : function(data) {
				},
			});
			 
			
		}
		//获取所选redmine项目的category
		function getCategoryNames()
		{
			var projectName = $("select[name='redmineName']").val();
			var categoryNames;
			if(projectName==null)
			{
				return;
			}
			$.ajax({
				url : 'getCategoryNames',
				type : 'post',
				async: false,
				data : {
					'projectName' : projectName
				},
				success : function(data) {
					categoryNames=data;
					var arrDate = new Array();
					arrDate = categoryNames.split(",");
					$("#category_redmine").empty();
					for(var i=0;i<arrDate.length;i++)
					{
						var strOption="<option value='" + arrDate[i]+ "'>" + arrDate[i] + "</option>";
						$("#category_redmine").append(strOption);
					}
				},
				error : function(data) {
				},
			});
		}
		
		//选择top suite时，其下的second suite全选
		$("#topsuite_testlink").click(function(){
			$("#topsuite_testlink").children().each(function(){
				var flag=false;
				var str=$(this).attr('value');
				$("#topsuite_testlink").find("option:selected").each(function(){
					var firstText=$(this).text();
					var secondText=firstText+"--";
					if(str.indexOf(secondText)>=0){
							flag=true;
						 }
					});
				if(flag==true)
					$(this).attr('selected','selected');
				});
			});

	</script>
</body>
</html>