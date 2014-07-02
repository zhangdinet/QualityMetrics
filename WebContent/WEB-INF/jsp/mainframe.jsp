<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>远景能源软件产品质量衡量系统</title>
		<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.4.1/build/cssreset/cssreset-min.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap-theme.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico"  type="image/x-icon"/>
		<link rel="Bookmark" href="${pageContext.request.contextPath}/img/favicon.ico" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.1.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/ranking_chart.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/qualitymetrics.js" charset="gb2312"></script>
	</head>
	<body>
		<jsp:include page="commonpart/headerLogoName.jsp"></jsp:include>
		<jsp:include page="commonpart/mainMenu.jsp"></jsp:include>
		
		<div class = "container theme-showcase">
			<div id="divMainContent" class="jumbotron">
				<div id="rankings">
					<div class="contentTitle">
						<h3>软件产品质量龙虎榜</h3>
						<span id="spanBrowseHistory" style="float:right">查看往期
							<select name="rankingPeriod">
								<option value="0">请选择</option>
								<c:forEach var="item" items="${rankingList}" varStatus="status">
									<option value="${item.rank_id}">${item.rank_period}</option>
								</c:forEach>
							</select>
						</span>
				</div>
					<div id="container" style="min-width:800px;height:400px;margin-bottom:10px"></div>
					<div id="rankingsContent">
					</div>
					<form id="form" class = "print">
						<input type="hidden" name="selectedPeriodId"></input>
						<input type="hidden" name="selectedPeriodName"></input>
						<input type="submit" value="打印预览" class="btn btn-primary" style="float:right;" onclick="printRankings()"></input>
					</form>
				</div>
				
				<div id="settings">
					<div id="settings_project">
					</div>
					<div id="settings_sprint">
					</div>
				</div>
					
				<div id="users"></div>
				<div id="indicator_weight"></div>
				<div id="changePassword" style="display:none">
					<div class="loginBox">
						<div class="loginBoxCenter">
							<p><label for="password">原密码</label></p>
							<p><input type="password" id="oldPassword" name="password" class="loginInput" placeholder="原密码" value="" /></p>
							
							<p><label for="password">新密码</label></p>
							<p><input type="password" id="newPassword" name="password" class="loginInput" placeholder="新密码" value="" /></p>
							
							<p><label for="password">确认密码</label></p>
							<p><input type="password" id="confirmPassword" name="password" class="loginInput" placeholder="确认新密码" value="" /></p>
							<p><label for="password" id = "tipLabel" style="color:red"></label></p>
						</div>
						<div class="loginBoxButtons">
							<button class="btn btn-primary" id = "save" onclick = "modifyPassword()">保存</button>
							<button class= "btn btn-primary" id = "cancel" onclick = "cancelModifyPassword()">取消</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		
	<jsp:include page="commonpart/footer.jsp"></jsp:include>
	<script type="text/javascript">
		var projectName=[];
		var avgScore=[];
		var menuIndex=-1;
		
		$(document).ready(function(){
		    var strReferrer=document.referrer;		//zhangdi 以后JQuery获取
		    menuIndex=<%=request.getAttribute("menuIndex")%>;
		    if(menuIndex==1)
		    {
		    	showSettings();
		    }
		    else if(menuIndex==2)
		    {
		    	showUserConfig();
		    }
		    else if(menuIndex==3)
		    {
		    	showWeightSettings();
		    }
		    else if(menuIndex==4)
		    {
		    	showMidifyPassword();
		    }
		    else if((strReferrer==null) || (strReferrer==undefined)||(strReferrer==""))
		    {
		    	showNewestRankings();
		    	showRankingChart();
		    }
		    else if(strReferrer.indexOf("showModifyProject")!=-1)
		    {
		    	showSettings();
		    }
		    else if(strReferrer.indexOf("showModifySprint")!=-1)
		    {
		    	var projectID = "${param.project_id}";
		    	var projectName="${project_name}";
		    	var pageNumber=1;
		    	
		    	showSettings();
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
				/* $("#menu_settings").hide();
				$("#menu_modifypwd").hide(); */
				$("#menu_usersManagement").hide();
				/* $("#menu_weightSettings").hide(); */
			}else{
				var project_id = '<%=session.getAttribute("project_id")%>';
				if(project_id != '0'){
					$("#menu_usersManagement").hide();
				}
			}
		});
		$('li').click(function(){
		});

		$('select[name="rankingPeriod"]').change(function(){
			var selectedRankId = $(this).children('option:selected').val();
			if(selectedRankId == "0"){
				showNewestRankings();
				showRankingChart();
				return;
			}
			$.ajax({
				url: 'showRankings',
				type: 'post',
				data: {'rankId':selectedRankId},
				success:function(data){
					$("#rankingsContent").html(data);
				},
				error:function(){
					alert("showSelectedRankings error!");
				},
			});
			$.ajax({
				url: 'showRankingChart',
				type: 'post',
				data: {'rankId':selectedRankId},
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
					alert("showSelectedRankingChart error!");
				},
			});
		});
		
		function showRankings(){
			$("#rankings").show();
			$("#settings").hide();
			$("#changePassword").hide();
			$("#users").hide();
			$("#indicator_weight").hide();
			showNewestRankings();
			showRankingChart();
			$('select[name="rankingPeriod"]').val("0");
			hidePromptWait();
		}
		function showSettings(){
			$("#settings").show();
			$("#rankings").hide();
			$("#changePassword").hide();
			$("#users").hide();
			$("#indicator_weight").hide();
			showSettingsDetail();
			hidePromptWait();
		}
		function showMidifyPassword(){
			$("#changePassword").show();
			$("#settings").hide();
			$("#rankings").hide();
			$("#users").hide();
			$("#indicator_weight").hide();
			$("#oldPassword").val("");
		 	$("#newPassword").val("");
		 	$("#confirmPassword").val("");
		 	$("#tipLabel").html("");
		}
		function showUserConfig(){
			$("#users").show();
			$("#changePassword").hide();
			$("#settings").hide();
			$("#rankings").hide();
			$("#indicator_weight").hide();
			showUserList();
			hidePromptWait();
		}
		
		function showUserManagement()
		{
			alert("test");
		}
				
		function showWeightSettings(){
			$("#indicator_weight").show();
			$("#users").hide();
			$("#changePassword").hide();
			$("#settings").hide();
			$("#rankings").hide();
			showWeightList();
			hidePromptWait();
		}
		
		function showUserList(){
			$.ajax({
				url:'showUserList',
				type:'post',
				success: function(data){
					$("#users").html(data);
				},
				error:function(){
					alert("error!");
				}
			});
		}
		
		function showNewestRankings(){
			$.ajax({
				url: 'showRankings',
				data: {'rankId':0},
				type: 'post',
				success:function(data){
					if(data.indexOf('<html>')>-1){
						window.location = "login";
						return;
					}
					$("#rankingsContent").html(data);
					},
					error:function(){
						alert("showNewestRankings error!");
				},
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

		function showWeightList(){
			$.ajax({
				url: 'showIndicatorWeight',
				type: 'post',
				success:function(data){
				    if(data.indexOf('<html>')>-1){
				    	window.location = "login";
				    	return;
					}
					$("#indicator_weight").html(data);
		  		 },
		   		error:function(){
		  			alert("showWeightList error!");
		  	     },
			});
		}

		function modifyPassword(){
			var oldPwd = $("#oldPassword").val();
			var newPwd = $("#newPassword").val();
			var confirmPwd = $("#confirmPassword").val();
			var password = "<%=session.getAttribute("password")%>";
			if(password == null){
				window.location = "login";
		    	return;
			}

			if(oldPwd == ""||newPwd ==""||confirmPwd == ""){
				$("#tipLabel").html("相关记录不能为空！");
				return;
			}
			//alert(password);
			if(oldPwd != password){
				$("#tipLabel").html("原密码输入错误！");
				return;
			}
			if(newPwd == confirmPwd){
				var username = "<%=session.getAttribute("username")%>";
				$.ajax({
					   url:'modifyPassword',
					   type:'get',
					   data:{"username":username,"newPassword":confirmPwd},
					   success:function(data){
						    if(data.indexOf('<html>')>-1){
						    	window.location = "login";
						    	return;
							}
					    	$("#changePassword").html(data);
					   },
					   error:function(data){
						   $("#tipLabel").html("系统异常，请稍后再试！");
					   },
				});
			}else{
				$("#tipLabel").html("新密码前后输入不一致！");
			}
		}
		
		function cancelModifyPassword(){
			$("#oldPassword").val("");
		 	$("#newPassword").val("");
		 	$("#confirmPassword").val("");
		 	$("#tipLabel").html("");
		}

		//添加Project信息
		function addProject(){
			alert("add");
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
				error:function(){
					alert("showSprintByProjectId error!");
				},
			});
		}
		function resetPwd(user_id){
			if(confirm("确定要重置用户密码吗？")){
				$.ajax({
					url: 'resetPwd',
					type: 'post',
					data: {"user_id":user_id},
					success:function(data){
						if(data == 'ok'){
							alert("重置成功");
							
						}else{
							alert("重置失败");
						}
					},
					error:function(){
						alert("showSprintByProjectId error!");
					},
				});
				
			}
		}

		function deleteUser(user_id){
			if(confirm("删除后无法恢复，确定要删除用户吗？")){
				$.ajax({
					url: 'deleteUser',
					type: 'post',
					data: {"user_id":user_id},
					success:function(data){
						if(data == 'ok'){
							alert("已删除！");
							showUserList();
						}else{
							alert("删除失败");
						}
					},
					error:function(){
						alert("delete user error!");
					},
				});
				
			}
		}
	</script>
</body>
</html>