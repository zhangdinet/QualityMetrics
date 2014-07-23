<%@page import="java.net.URLDecoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>更新sprint</title>
	<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.4.1/build/cssreset/cssreset-min.css">	
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap-theme.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery-ui-1.10.4.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
	<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico" type="image/x-icon" />
	<link rel="Bookmark" href="${pageContext.request.contextPath}/img/favicon.ico" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
	<script type="text/javascript"	src="${pageContext.request.contextPath}/js/jquery-ui-1.10.4.js"></script>
	<script type="text/javascript"	src="${pageContext.request.contextPath}/js/jquery.ui.datepicker-zh-CN.js"></script>
</head>

<body>
		<jsp:include page="commonpart/headerLogoName.jsp"></jsp:include>
		<jsp:include page="commonpart/navMenu.jsp"></jsp:include>
		<jsp:include page="commonpart/containerStart.jsp"></jsp:include>
			<div id="divSetSprintDetail">
			<h1>更新Sprint</h1>
			<form id="form" class="frmSetSprintDetail" method="post">
				<div class="form-group">
					<label for="project_name" style="width:90px">产品名称</label>
					<input id="project_name" name="project_name" readonly="readonly" style="width:150px;height:30px" value="${project_name}" />
					<label style="width:80px;margin-left:60px">开始日期</label>
					<input type="text" id="begindate" readonly name="sprint_startdate" style="padding-left:5px" placeholder="请选择日期" style="height:30px"/>
				</div>

				<div class="form-group">
					<label for="sprint_name" style="width:90px">Sprint名称</label>
					<input id="sprint_name" name="sprint_name" readonly="readonly" value="<%=URLDecoder.decode(request.getParameter("sprintName"),"UTF-8")%>" style="width:150px;height:30px" />
					<label style="width:80px;margin-left:60px">结束日期</label>
					<input type="text" id="enddate" readonly name="sprint_enddate" style="padding-left:5px" placeholder="请选择日期" style="height:30px"/>
				</div>
				
				<div class="form-group">
					<label style="margin-top:15px">TestLink测试计划名称</label>
					<select id="testplan_testlink" name="testplan_testlink" class="form-control" multiple="multiple">
						<c:forEach var="itemTest" items="${lstTestPlan}">
								<c:set var="boolFlag" value="${fn:contains(itemTest,'#selected')}"/>
								<c:choose>
									<c:when test="${boolFlag}">
										<option value="${fn:substringBefore(itemTest,'#selected')}" selected="selected"> ${fn:substringBefore(itemTest,'#selected')} </option>
									</c:when>
									<c:otherwise>
										<option value="${itemTest}">${itemTest}</option>
									</c:otherwise>
								</c:choose>
						</c:forEach>
					</select>
					<span id="testplan_testlink_flag" class="span_flag"></span>
				</div>
				
				<div class="form-group">
					<label>Redmine版本名称</label>
					<select id="version_redmine" name="version_redmine" class="form-control" multiple="multiple">
						<c:forEach var="itemRedmine" items="${lstRedmine}">
								<c:set var="boolFlag" value="${fn:contains(itemRedmine,'#selected')}"/>
								<c:choose>
									<c:when test="${boolFlag}">
										<option value="${fn:substringBefore(itemRedmine,'#selected')}" selected="selected"> ${fn:substringBefore(itemRedmine,'#selected')} </option>
									</c:when>
									<c:otherwise>
										<option value="${itemRedmine}">${itemRedmine}</option>
									</c:otherwise>
								</c:choose>
						</c:forEach>
					</select>
					<span id="version_redmine_flag" class="span_flag"></span>
				</div>
				
				<div class="form-group">
					<label>SurveyMonkey标题</label>
					<div>
						<select name="url_surveymonkey" multiple="multiple" class="form-control">
							<c:forEach var="itemTitle" items="${lstTitle}">
									<c:set var="boolFlag" value="${fn:contains(itemTitle,'#selected')}" />
									<c:choose>
										<c:when test="${boolFlag}">
											<option value="${fn:substringBefore(itemTitle,'#selected')}" selected="selected"> ${fn:substringBefore(itemTitle,'#selected')} </option>
										</c:when>
										<c:otherwise>
											<option value="${itemTitle}"> ${itemTitle} </option>
										</c:otherwise>
									</c:choose>
							</c:forEach>
						</select>
						
						<span id="url_surveymonkey_flag" class="flagTip"></span>
					</div>
				</div>
				
				<div class="form-group">
					<label>Sonar构建名称</label>
					<span id="build_sonar_flag"></span>
					<div>
						<select name="build_sonar" id="build_sonar" class="form-control" onchange="getBuildDates()">
							<c:forEach var="itemSonar" items="${lstSonar}">
								<c:set var="boolFlag" value="${fn:contains(sprint.build_sonar,itemSonar)}"/>
								<c:choose>
									<c:when test="${boolFlag}">
										<option value="${itemSonar}" selected="selected" >${itemSonar}</option>
									</c:when>
									<c:otherwise>
										<option value="${itemSonar}">${itemSonar}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
					</div>
				</div>
				
				<div  class="form-group">
					<div class="form-group">
						<label>构建日期</label>
						<select name="build_date" id="build_date" class="form-control"></select>
					</div>
					<div class="form-group">
						<input type="button" class="btn btn-primary" value="添加构建" onclick="addBuild()"/>
						<input type="button" class="btn btn-primary" value="删除已选" onclick="deleteSelectedBuild()"/>
						<input type="button" class="btn btn-primary" value="删除所有" onclick="deleteAllBuild()" />
					</div>
					<div>
						<div>
							<select name="selected_builds" id="selected_builds" class="form-control" multiple="multiple">
								<c:forEach var="itemBuild" items="${arrBuild}">
									<option value="${itemBuild}" selected="selected" >${itemBuild}</option>
								</c:forEach>
							</select>
							<input type="hidden" name="inputBuilds" id="inputBuilds"/>
						</div>
					</div>
				</div>
				
				<div class="form-group">
					<input type="radio" #id="ipdScore" name="ipdOrLmtScore" class="radidItem" checked="checked" value="1"/>
					<label>IPD分数(0-5)</label>
					<input name="ipd_score" value="${sprint.ipd_score}" style="width:80px;height:30px" onblur="checkScore()" />
					<span id="ipd_score_flag"></span>
					
					<input type="radio" #id="lmtScore" name="ipdOrLmtScore" class="radidItem" value="2" style="margin-left:50px"/>
					<label>LMT分数(0-5)</label>
					<input name="lmt_score" value="${sprint.lmt_score}" disabled="disabled" style="width:80px;height:30px" onblur="checkScore()" />
					<span id="lmt_score_flag"></span>
				</div>
				
				<div class="form-group">
					<input type="button" class="btn btn-primary"  value="更新" onclick="saveSprintDetails()" />
					<input type="button" class="btn btn-primary"  value="取消" onclick="backToMainframe()" />
				</div>
				
				<div id="sprint_tipLabel">
					<c:choose>
						<c:when test="${updateResult eq 'ok'}">更新成功！
						</c:when>
						<c:when test="${updateResult eq 'err'}">更新失败！</c:when>
						<c:otherwise>${updateResult}</c:otherwise>
					</c:choose>
				</div>
				
				<input type="hidden" value="<fmt:formatDate value='${sprint.sprint_builddate }' pattern='yyyy-MM-dd'/>" id="builddateHidden" />
				<input type="hidden" value="<fmt:formatDate value='${sprint.sprint_startdate }' pattern='yyyy-MM-dd'/>" id="startdateHidden" />
				<input type="hidden" value="<fmt:formatDate value='${sprint.sprint_enddate }' pattern='yyyy-MM-dd'/>" id="enddateHidden" />
				<input type="hidden" value="${project_id}" name="project_id" />
				<input type="hidden" value="${sprint.sprint_id }" name="sprint_id" />
				<input type="hidden" value="${sprint.ipd_score }" name="ipd_hidden" />
				<input type="hidden" value="${sprint.lmt_score }" name="lmt_hidden" />
				<input type="hidden" value="${project_flag}" name="project_flag" />
			</form>
		</div>
		
	<jsp:include page="commonpart/containerEnd.jsp"></jsp:include>
	<jsp:include page="commonpart/footer.jsp"></jsp:include>
	<script type="text/javascript">
		function checkScore()
		{
			//var scorePattern = /^\d+[\.\d]?\d{0,2}$/;
			var oneNumberPattern = /^\d+$/;
			var numberPattern= /^\d+[\.\d]?\d+$/;
			var scorePattern;
			var value = $("input[name='ipdOrLmtScore']:checked").val();
			var strScore;
			if (value == 1)
			{
				// zhangdi 140504 修改原有判断逻辑
				strScore=$("input[name='ipd_score']").val();
				if(strScore.indexOf('.')!=-1)
				{
					scorePattern = /^\d+[\.\d]?\d+$/;
				}
				else
				{
					scorePattern = /^\d+$/;
				}
				if(scorePattern.test(strScore))
				{
					var fScore =parseFloat(strScore);
					if(isNaN(fScore))
					{
						$("#ipd_score_flag").html("×");
						$("input[name='ipd_score']").val("");
					}
					else if(fScore<0 || fScore>5)
					{
						$("#ipd_score_flag").html("×");
						$("input[name='ipd_score']").val("");
					}
					else
					{
						$("#ipd_score_flag").html("√");
					}
				}
				else
				{
					$("#ipd_score_flag").html("×");
					$("input[name='ipd_score']").val("");
				}
			}
			else if (value == 2)
			{
				var strScore=$("input[name='lmt_score']").val();
				if(strScore.indexOf('.')!=-1)
				{
					scorePattern = /^\d+[\.\d]?\d+$/;
				}
				else
				{
					scorePattern = /^\d+$/;
				}
				if(scorePattern.test(strScore))
				{
					var fScore =parseFloat(strScore);
					if(isNaN(fScore))
					{
						$("#lmt_score_flag").html("×");
						$("input[name='lmt_score']").val("");
					}
					else if(fScore<0 || fScore>5)
					{
						$("#lmt_score_flag").html("×");
						$("input[name='lmt_score']").val("");
					}
					else
					{
						$("#lmt_score_flag").html("√");
					}
				}
				else
				{
					$("#lmt_score_flag").html("×");
					$("input[name='lmt_score']").val("");
				}
			}
		}
		
		$(document).ready(function() {
			var ipd_hidden = $("input[name='ipd_hidden']").val();
			var lmt_hidden = $("input[name='lmt_hidden']").val();
			if (ipd_hidden == -1) {
				$("input[value='2']").attr("checked", "checked");
				//显示lmt可编辑
				$("input[name='ipd_score']").attr("disabled", true);
				$("input[name='lmt_score']").attr("disabled", false);
				$("input[name='ipd_score']").attr("value", "");
			} else {
				$("input[value='1']").attr("checked", "checked");
				//显示ipd可编辑
				$("input[name='ipd_score']").attr("disabled", false);
				$("input[name='lmt_score']").attr("disabled", true);
				$("input[name='lmt_score']").attr("value", "");
			}
		});
		$(function() {
			$("#begindate").datepicker({
				beforeShow:function(input,inst)
				{
					$.datepicker._pos = $.datepicker._findPos(input);
					$.datepicker._pos[0] = input.offsetLeft;
					$.datepicker._pos[1] += input.offsetTop+input.offsetHeight;
				}
			});
			$("#enddate").datepicker({
				beforeShow:function(input,inst)
				{
					$.datepicker._pos = $.datepicker._findPos(input);
					$.datepicker._pos[0] = input.offsetLeft;
					$.datepicker._pos[1] += input.offsetTop;
				}
			});
			
			$("#begindate").datepicker($.datepicker.regional["zh-CN"]);
			$("#begindate").datepicker("option", "dateFormat", "yy-mm-dd");
			$("#begindate").datepicker("option", "showAnim", "fold");
			$("#begindate").val($("#startdateHidden").val());

			$("#enddate").datepicker($.datepicker.regional["zh-CN"]);
			$("#enddate").datepicker("option", "dateFormat", "yy-mm-dd");
			$("#enddate").datepicker("option", "showAnim", "fold");
			$("#enddate").val($("#enddateHidden").val());
		});
		
		//重构  zhangdi todo 140509======== 
		function saveSprintDetails()
		{
			var sprint_name = $("input[name='sprint_name']").val();
			var testplan_testlink = $("select[name='testplan_testlink']").val();
			var version_redmine = $("select[name='version_redmine']").val();
			var begindate = $("#begindate").val();
			var enddate = $("#enddate").val();
			var ipd_score = $("input[name='ipd_score']").val();
			var lmt_score = $("input[name='lmt_score']").val();
			var url_surveymonkey = $("select[name='url_surveymonkey']").val();
			var selected_builds = $("select[name='selected_builds']").val();
			
			var selected_builds=new Array();
			var optionCount=$("#selected_builds option").size();
			var arrBuild=$("#selected_builds option");
			for(var i=0;i<optionCount;i++)
			{
				selected_builds[i]=arrBuild[i].text;
			}
			
			var inputBuilds=$("#inputBuilds");
			inputBuilds.attr('value',selected_builds);
			
			var value = $("input[name='ipdOrLmtScore']:checked").val();
			
			if(value==1)  //改为禁用判断  zhangdi todo ==========
			{
				if (sprint_name == "" || testplan_testlink == "" || version_redmine == "" || build_sonar == "" 
					|| begindate == "" || enddate == "" || ipd_score == "" || url_surveymonkey == "" ||selected_builds=="")
				{
					$("#sprint_tipLabel").html("请完整填写信息！");
					return;
				}
			}
			else if(value==2)
			{
				if (sprint_name == "" || testplan_testlink == ""|| version_redmine == "" || build_sonar == ""
					|| begindate == "" || enddate == ""	|| lmt_score == "" || url_surveymonkey == "" ||selected_builds=="")
				{
					$("#sprint_tipLabel").html("请完整填写信息！");
					return;
				}
			}
			$("#form").attr("action", "saveUpdateSprint");
			$("#form").submit();
		}

		function backToMainframe()
		{
			history.go(-1);
			/* $("#form").attr("action", "projectlist");
			$("#form").submit(); */
		}

		$(".radidItem").change(function() {
			var value = $("input[name='ipdOrLmtScore']:checked").val();
			if (value == 1) {
				//显示ipd可编辑
				$("input[name='ipd_score']").attr("disabled", false);
				$("input[name='lmt_score']").attr("disabled", true);
				$("#lmt_score_flag").html("");
			}
			if (value == 2) {
				$("input[name='ipd_score']").attr("disabled", true);
				$("input[name='lmt_score']").attr("disabled", false);
				$("#ipd_score_flag").html("");
			}
		});
		
		//获取所选项目的所有构建日期 
		function getBuildDates()
		{
			var sonarProjectName = $("select[name='build_sonar']").val();
			var buildDates;
			if(sonarProjectName==null)
			{
				return;
			}
			
			var sonarProjectID=-1;
			sonarProjectID=getSonarProjectIDByName(sonarProjectName);
			if(sonarProjectID==-1)
			{
				return;		//考虑给出未找到提示   zhangdi 140428
			}

			$.ajax({
				url : 'getBuildDates',
				type : 'post',
				async: false,
				data : {
					'sonarProjectID' : sonarProjectID
				},
				success : function(data) {
					buildDates=data;
				},
				error : function(data) {
				},
			});
			if(buildDates=="" || buildDates==null)
			{
				$("#build_sonar_flag").html("该构建为空，请选择其它构建！");
			}
			else
			{
				var arrDate = new Array();
				arrDate = buildDates.split(",");
				$("#build_date").empty();
				for(var i=0;i<arrDate.length;i++)
				{
					var strOption="<option value='" + arrDate[i]+ "'>" + arrDate[i] + "</option>";
					$("#build_date").append(strOption);
				}
				$("#build_sonar_flag").html("");
			}
		}
			
		function getSonarProjectIDByName(name)
		{
			var sonarProjectID=-1;
			if(name==null)
			{
				return;
			}
			$.ajax({
				url : 'getSonarProjectIDByName',
				type : 'post',
				async: false,  //wait for data return ===zhangdi===
				data : {
					'sonarProjectName' : name
				},
				success : function(data) {
					if (data.indexOf("ok")>=0)
					{
						sonarProjectID=data.substring(3);  //data:"ok&ID"  ==zhangdi===
					}
					else
					{
						$("#build_sonar_flag").html("");  //zhangdi todo 考虑剔除无构建的选项
					}
				},
				error : function(data) {
				},
			});
			return sonarProjectID;
		}
	
		//以后改为jQuery实现  zhangdi === todo =========
		function addBuild()
		{
			var selected_builds=document.getElementById("selected_builds");
			var objOption=document.createElement("option");
			var value= document.getElementById("build_sonar").value + "=" + document.getElementById("build_date").value;
			if(isSelectdBuildExistence(value))
			{
				return;
			}
			objOption.value =value;
			objOption.selected="selected";
			objOption.text = value;
			selected_builds.add(objOption);
		}
		
		//判断添加构建是否已经选择 zhangdi 140504
		function isSelectdBuildExistence(build)
		{
			var selectedBuilds=$("#selected_builds").val();
			if(selectedBuilds==null)
			{
				return false;
			}
			else if(selectedBuilds.indexOf(build)!=-1)
			{
				return true;
			}
			return false;
		}
		
		function deleteSelectedBuild()
		{
			$('#selected_builds option:selected').remove();
		}
			
		function deleteAllBuild()
		{
			$("#selected_builds").empty();
		}
		
		//加载时取得默认Sonar项目的构建日期
		$(document).ready(function(){ 
			getBuildDates();
		});
	</script>
</body>
</html>