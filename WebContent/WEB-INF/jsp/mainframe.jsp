<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>远景能源软件产品质量衡量系统</title>
		<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.4.1/build/cssreset/cssreset-min.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap-theme.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico" type="image/x-icon"/>
		<link rel="Bookmark" href="${pageContext.request.contextPath}/img/favicon.ico" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/ranking_chart.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/qualitymetrics.js" charset="gb2312"></script>
	</head>
	<body>
		<jsp:include page="commonpart/headerLogoName.jsp"></jsp:include>
		<jsp:include page="commonpart/mainMenu.jsp"></jsp:include>
		
		<div class = "container theme-showcase">
			<div id="divMainContent" class="jumbotron">
				just for debug!!!
			</div>
		</div>
	<jsp:include page="commonpart/footer.jsp"></jsp:include>
	<script type="text/javascript">
		var projectName=[];
		var avgScore=[];
		var menuIndex=-1;
		
		$(document).ready(function(){
			var strReferrer=document.referrer;	//zhangdi 以后JQuery获取
			if((strReferrer==null) || (strReferrer==undefined)||(strReferrer==""))
			{
				showNewestRankings();
				showRankingChart();
			}
		    else if(strReferrer.indexOf("showModifySprint")!=-1)
		    {
		    	var projectID = "${param.project_id}";
		    	var projectName="${project_name}";
		    	var pageNumber=1;
		    	showSprintByProjectId(projectID,projectName,pageNumber);
		    }
			else
			{
				showNewestRankings();
				showRankingChart();
			}
			var flag_admin = '<%=session.getAttribute("flag_admin")%>';
			//普通用户 不显示产品设置
			if(flag_admin == 'no'){
				$("#menu_usersManagement").hide();
			}else{
				var project_id = '<%=session.getAttribute("project_id")%>';
				if(project_id != '0'){
					$("#menu_usersManagement").hide();
				}
			}
		});
		$('li').click(function(){
		});
		
		function showRankings(){
			$("#rankings").show();
			$("#users").hide();
			showNewestRankings();
			showRankingChart();
			$('select[name="rankingPeriod"]').val("0");
			hidePromptWait();
		}

		function showNewestRankings(){
			$.ajax({
				url: 'showRankings',
				data: {'rankId':0},
				type: 'post',
			/* 	success:function(data){
					if(data.indexOf('<html>')>-1){
						window.location = "login";
						return;
					}
					$("#rankingsContent").html(data);
					},
					error:function(){
						alert("showNewestRankings error!");
				}, */
			});
		}
		
		function showRankingChart(){
			$.ajax({
				url: 'showRankingChart',
				data: {'rankId':0},
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
					error:function(){
						alert("showRankingChart error!");
					},
			});
		}

		function showSettingsDetail(){
			$.ajax({
				url: 'showSettingsProject',
				type: 'post',
				success:function(data){
				    if(data.indexOf('<html>')>-1){
				    	window.location = "login";
				    	return;
					}
					$("#settings_project").html(data);
		  		 },
		   		error:function(){
		  			alert("showSettingsDetail error!");
		  	     },
			});
		}

		function printRankings(){
			var selectedRankId = $('select[name="rankingPeriod"]').children('option:selected').val();
			var input1 = $("input[name='selectedPeriodId']");
			input1.attr('value',selectedRankId);
			var input2 = $("input[name='selectedPeriodName']");
			var selectedPeriodName = $('select[name="rankingPeriod"]').children('option:selected').html();
			input2.attr('value',selectedPeriodName);
			$("#form").attr("action","printRankings");
			$("#form").submit();
		}
		
		function gotoSelectedPage()
		{  
		    var pageNumber = $('select[name="pageNumber"]').children('option:selected').val();
		    var project_id = $("input[name='project_id']").val();
		    var project_name = $("input[name='project_name']").val();
		    showSprintByProjectId(project_id,project_name,pageNumber);
		}
		
		function gotoPage(pageNumber){
			 var project_id = $("input[name='project_id']").val();
			 var project_name = $("input[name='project_name']").val();
			 showSprintByProjectId(project_id,project_name,pageNumber);
		}
		
		
		function showSprintByProjectId(project_id,project_name,pageNumber){
			$.ajax({
				url: 'showSettingsSprint',
				type: 'post',
				data: {"project_id":project_id,"project_name":project_name,"pageNumber":pageNumber},
				success:function(data){
					if(data.indexOf('<html>')>-1){
						window.location = "login";
						return;
					}
					$("#settings_sprint").html(data);
				},
				/* error:function(){
					alert("showSprintByProjectId error!");
				}, */
			});
		}
		
	</script>
</body>
</html>