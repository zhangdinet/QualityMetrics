<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>员工质量KPI</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap-theme.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery-ui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico" type="image/x-icon"/>
		<link rel="Bookmark" href="${pageContext.request.contextPath}/img/favicon.ico" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-ui.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.ui.datepicker-zh-CN.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/exporting.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/qualitymetrics.js" charset="gb2312"></script>
	</head>
	<body>
		<script type="text/javascript">
			function getVersions()
			{
				var redmineName = $("select[name='selectRedmine']").val();
				$.ajax({
					url : 'getVersionNames',
					type : 'post',
					async: false,
					data : {
						'redmineName' : redmineName
					},
					success : function(data) {
						var arrVersion=data.split(",");
						$("#selectSprintVersion").empty();
						for(var i=0;i<arrVersion.length;i++)
						{
							var strOption="<option value='" + arrVersion[i]+ "'>" + arrVersion[i] + "</option>";
							$("#selectSprintVersion").append(strOption);
						}
					},
					error : function(data) {
					},
				});
			}
		</script>
		<jsp:include page="commonpart/headerLogoName.jsp"></jsp:include>
		<jsp:include page="commonpart/mainMenu.jsp"></jsp:include>
		<jsp:include page="commonpart/containerStart.jsp"></jsp:include>

		<form id="formKPI">
			<h3>员工质量KPI</h3>
			<label>产品模块</label>
			<select id="selectRedmine" name="selectRedmine" onchange="getVersions()">
				<c:forEach var="projectName" items="${redmineProjectNames}">
					<option value="${projectName}">${projectName}</option>
				</c:forEach>
			</select>
			<label>版本</label>
			<select id="selectSprintVersion" name="version" ></select>
			<label>开始日期</label>
			<input type="text" id="sprintStart" name="sprintStart" style="padding-left:5px" placeholder="请选择日期" readonly />
			<label>结束日期</label>
			<input type="text" id="sprintEnd" name="sprintEnd" style="padding-left:5px" placeholder="请选择日期" readonly />
			<button type="button" id="btnOK" class="btn btn-primary" onclick="renderChart()">确定</button>
			<span id="spanFlag"></span>
		</form>
	
		<div id="divReopenRatio" style="margin-top:10px;"width="800px" height="1000px"></div>
		<div id="divScopeStability" style="margin-top:10px;"width="800px" height="1000px"></div>
		<div id="divProjectInfo" style="margin-top:10px;"width="800px" height="1000px"></div>
		<div id="divFixedBugsByCategory" style="margin-top:10px;"width="800px" height="1000px"></div>
		<div id="divImplementedFeaturesByCategory" style="margin-top:10px;"width="800px" height="1000px"></div>
		<div id="divFixedBugsByDeveloper" style="margin-top:10px;"width="800px" height="1000px"></div>
		<div id="divImplementedFeaturesByDeveloper" style="margin-top:10px;"width="800px" height="1000px"></div>
		<div id="divFixedBugsBySeverity" style="margin-top:10px;"width="800px" height="1000px"></div>
		<div id="divImplementedFeaturesBySeverity" style="margin-top:10px;"width="800px" height="1000px"></div>
		<div id="divNewBugsByCategory" style="margin-top:10px;"width="800px" height="1000px"></div>
		<div id="divNewBugsByDeveloper" style="margin-top:10px;"width="800px" height="1000px"></div>
		<div id="divNewBugsBySeverity" style="margin-top:10px;"width="800px" height="1000px"></div>
		<div id="divFixedRateByCategory" style="margin-top:10px;"width="800px" height="1000px"></div>
		<div id="divFixedRateByDeveloper" style="margin-top:10px;"width="800px" height="1000px"></div>
		<div id="divFixedRateBySeverity" style="margin-top:10px;"width="800px" height="1000px"></div>
		
		<jsp:include page="commonpart/containerEnd.jsp"></jsp:include>
		<jsp:include page="commonpart/footer.jsp"></jsp:include>
		
		
		<script type="text/javascript">
			$(function() {
				$("#sprintStart").datepicker({
				});
				$("#sprintStart").datepicker($.datepicker.regional["zh-CN"]);
				$("#sprintStart").datepicker("option", "dateFormat", "yy-mm-dd");
				$("#sprintStart").datepicker("option", "showAnim", "fold");
				$("#sprintEnd").datepicker($.datepicker.regional["zh-CN"]);
				$("#sprintEnd").datepicker("option", "dateFormat", "yy-mm-dd");
				$("#sprintEnd").datepicker("option", "showAnim", "fold");
				getVersions();
			});
			
			function checkEmpty()
			{
				if( $("#sprintStart").val()=="" || $("#sprintEnd").val()=="")
				{
					$("#spanFlag").html("请选择日期！");
					return false;
				}
				else
				{
					$("#spanFlag").html("");
					return true;
				}
			}
			
			function renderChart()
			{
				if(checkEmpty())
				{
					getReopenRatio();
					getScopeStability();
					getProjectInfo();
					getFixedBugsByCategory();
					getFixedBugsByDeveloper();
					getFixedBugsBySeverity();
					getImplementedFeaturesByCategory();
					getImplementedFeaturesByDeveloper();
					getNewBugsByCategory();
					getNewBugsByDeveloper();
					getNewBugsBySeverity();
					getFixedRateByCategory();
					getFixedRateByDeveloper();
					getFixedRateBySeverity();
				}
			}
			
			
			//==============zhangdi 140704========================
			function getProjectInfo()
			{
				var redmineName = $("select[name='selectRedmine']").val().trim();
				//var version=$("select[name='version']").val().trim();
				var sprintStart=$("input[name='sprintStart']").val().trim();
				var sprintEnd=$("input[name='sprintEnd']").val().trim();
				
				$.ajax({
					url : 'getProjectInfoByVersion',
					type : 'post',
					async: false,
					data : {
						'redmineName' : redmineName,
						//'version':version,
						'sprintStart':sprintStart,
						'sprintEnd':sprintEnd
					},
					success : function(data) {
						if(data=='[]')
						{
							return;
						}
						var tempData=data.substring(2,data.length-2);
						var arrData=tempData.split('","');
						
						var columnName=[];
						var columnImplementedFeatures=[];
						var columnFixedBugs=[];
						var columnNewBugs=[];
						for(var i=0;i<arrData.length;i++)
						{
							arrItem=arrData[i].split("#");
							columnName.push(arrItem[0]);
							columnImplementedFeatures.push(parseInt(arrItem[1])||0);
							columnFixedBugs.push(parseInt(arrItem[2])||0);
							columnNewBugs.push(parseInt(arrItem[3])||0);
						}
						var chart=prepareProjectInfoChart(columnImplementedFeatures,columnFixedBugs,columnNewBugs);
						chart.xAxis[0].setCategories(columnName);
					},
					error : function(data) {
					},
				});
			}
			
			function prepareProjectInfoChart(columnImplementedFeatures,columnFixedBugs,columnNewBugs)
			{
				var chart;
				var options={
					chart:{
						renderTo:'divProjectInfo',
						//type:'column'
					},
					title: {
						text: 'Features Implemented -- Bugs Fixed -- Bugs Found',
						style:{
						fontWeight:'bold'
						}
					},
					credits: { //右下角网址信息
						enabled: false
					},
					xAxis: {
						title:{
							text:'Version',
							style:{
								fontWeight:'bold',
								fontSize:'16px',
								width:'200px',
								marginLeft:'auto',
								marginRight:'auto'
							}
            		},
					categories: [],
					labels: {
						rotation: 60
					}
					},
					yAxis: {
						title: {
							text: 'Count',
							style:{
		                		fontWeight:'bold',
		                		fontSize:'16px',
                			}
						}
					},
					legend:{
						enabled:true
					},
					plotOptions: {
						column:{
							pointPadding: 0.2,
							borderWidth: 0
						}
					},
					tooltip: {
						formatter:function(){
							 return ' <b>'+ this.x +
							'</b><br/><b>'+ this.y +'</b>';
						}
					},
					legend: {
					          layout: 'vertical',
					          align: 'right',
					          verticalAlign: 'middle',
					          borderWidth: 0
					      },
					series: [{
					            name: 'Features Implemented',
					            data: columnImplementedFeatures
					        }, {
					            name: 'Bugs Fixed',
					            data: columnFixedBugs
					
					        }, {
					            name: 'Bugs Found',
					            data: columnNewBugs
					        }]
				};
				var chart = new Highcharts.Chart(options);
				return chart;
			}
			
			function getScopeStability()
			{
				var redmineName = $("select[name='selectRedmine']").val().trim();
				var version=$("select[name='version']").val().trim();
				var sprintStart=$("input[name='sprintStart']").val().trim();
				var sprintEnd=$("input[name='sprintEnd']").val().trim();
				
				$.ajax({
					url : 'getScopeStability',
					type : 'post',
					async: false,
					data : {
						'redmineName' : redmineName,
						'version':version,
						'sprintStart':sprintStart,
						'sprintEnd':sprintEnd
					},
					success : function(data) {
						if(data=='[]')
						{
							return;
						}
						var tempData=data.substring(2,data.length-2);
						var arrData=tempData.split('","');
						var columnValue=[];
						var columnName=[];
						var arrItem=[];
						var arrBugsCount=[];
						var arrFeaturesCount=[];
						for(var i=0;i<arrData.length;i++)
						{
							/* arrBugItem=arrData[i].split("#");
							columnName.push(arrBugItem[0]);
							arrBugItem[1]=parseFloat(arrBugItem[1]);
							columnValue.push(arrBugItem); */
							arrItem=arrData[i].split("#");
							columnName.push(arrItem[0]);
							arrBugsCount.push(parseInt(arrItem[1]));
							arrFeaturesCount.push(parseInt(arrItem[2]));
							
						}
						var chart=prepareScopeStabilityChart(arrBugsCount,arrFeaturesCount);
						//chart.series[0].setData(arrBugsCount,true);
						//chart.series[1].setData(arrFeaturesCount,true);
						chart.xAxis[0].setCategories(columnName);
					},
					error : function(data) {
					},
				});
			}
			
			function prepareScopeStabilityChart(arrBugsCount,arrFeaturesCount)
			{
				var chart;
				var options={
					chart:{
						renderTo:'divScopeStability',
						//type:'column'
					},
					title: {
						text: 'Scope Stability',
						 
						style:{
	                		fontWeight:'bold'
						}
					},
					credits: { //右下角网址信息
						enabled: false
					},
					xAxis: {
						title:{
							text:'Date',
							/* align:'high', */
							style:{
		                		fontWeight:'bold',
		                		fontSize:'16px',
		                		width:'20%',
		                		marginLeft:'auto',
		                		marginRight:'auto'
		                	}
						},
						categories: [],
						labels: {
							rotation: 60
						}
					},
					yAxis: {
						title: {
							text: 'Count',
							style:{
		                		fontWeight:'bold',
		                		fontSize:'16px',
                			}
						}
					},
					legend:{
						enabled:false
					},
					plotOptions: {
						column:{
							pointPadding: 0.2,
							borderWidth: 0
						}
					},
					tooltip: {
						formatter:function(){
							/* return ' <b>'+ this.x +
							'</b><br/><b>'+ this.y +'</b>'; */
							return '<b>'+ this.y +'</b>';
						}
					},
					legend: {
			            layout: 'vertical',
			            align: 'right',
			            verticalAlign: 'middle',
			            borderWidth: 0
			        },
					series: [
							{
								name:'Bugs',
								data:arrBugsCount
							},
							{
								name:'Features',
								data:arrFeaturesCount
							}
						]
				};
				var chart = new Highcharts.Chart(options);
				return chart;
			}
			
			
			function getReopenRatio()
			{
				var redmineName = $("select[name='selectRedmine']").val().trim();
				var version=$("select[name='version']").val().trim();
				var sprintStart=$("input[name='sprintStart']").val().trim();
				var sprintEnd=$("input[name='sprintEnd']").val().trim();
				
				$.ajax({
					url : 'getReopenRatio',
					type : 'post',
					async: false,
					data : {
						'redmineName' : redmineName,
						'version':version,
						'sprintStart':sprintStart,
						'sprintEnd':sprintEnd
					},
					success : function(data) {
						if(data=='[]')
						{
							return;
						}
						var tempData=data.substring(2,data.length-2);
						var arrData=tempData.split('","');
						var columnValue=[];
						var columnName=[];
						var issueTip={};
						for(var i=0;i<arrData.length;i++)
						{
							arrBugItem=arrData[i].split("#");
							var name=arrBugItem[0];
							columnName.push(name);
							arrBugItem[1]=parseFloat(arrBugItem[1]);
							columnValue.push(arrBugItem[1]);
							issueTip[name]=arrBugItem[2];
							//issueTip.push(arrBugItem[2]);
						}
						var chart=prepareReopenRatioChart(issueTip);
						chart.series[0].setData(columnValue,true);
						chart.xAxis[0].setCategories(columnName);
					},
					error : function(data) {
					},
				});
			}
			
			
			function prepareReopenRatioChart(issTip)
			{
				var chart;
				var options={
					chart:{
						renderTo:'divReopenRatio',
						type:'column'
					},
					title: {
						text: 'Reopen Ratio',
						 
						style:{
	                		fontWeight:'bold'
						}
					},
					credits: { //右下角网址信息
						enabled: false
					},
					xAxis: {
						title:{
							text:'Developer',
							style:{
		                		fontWeight:'bold',
		                		fontSize:'16px',
		                		width:'200px',
		                		marginLeft:'auto',
		                		marginRight:'auto'
		                	}
						},
						categories: [],
						labels: {
							rotation: 60
						}
					},
					yAxis: {
						title: {
							text: 'Rate',
							style:{
		                		fontWeight:'bold',
		                		fontSize:'16px',
                			}
						}
					},
					legend:{
						enabled:false
					},
					plotOptions: {
						column:{
							pointPadding: 0.2,
							borderWidth: 0,
							/* events:{
								click:function(event)
								{
									alert(this.name);
								}
							} */
						}
					},
					tooltip: {
						formatter:function(){
							return '<b>'+ this.x +
							'</b><br/><b>'+ parseFloat(this.y*100).toFixed(2) +'%</b>'+ '<br>'
							+'<b>Issues ID(s):<br></b>'+ issTip[this.x];
						}
					},
					series: [{
						data: []
					}]
				};
				var chart = new Highcharts.Chart(options);
				return chart;
			}
			
			
			
			function getFixedBugsBySeverity()
			{
				var redmineName = $("select[name='selectRedmine']").val().trim();
				var version=$("select[name='version']").val().trim();
				
				$.ajax({
					url : 'getFixedBugsBySeverity',
					type : 'post',
					async: false,
					data : {
						'redmineName' : redmineName,
						'version':version
					},
					success : function(data) {
						if(data=='[]')
						{
							return;
						}
						var tempData=data.substring(2,data.length-2);
						var arrData=tempData.split('","');
						var pieData=[];
						for(var i=0;i<arrData.length;i++)
						{
							arrBugItem=arrData[i].split("#");
							arrBugItem[1]=parseInt(arrBugItem[1]);
							pieData.push(arrBugItem);
						}
						var chart=prepareFixedBugsBySeverityChart();
						chart.series[0].setData(pieData);
					},
					error : function(data) {
					},
				});
			}
			
			
			function getFixedRateBySeverity()
			{
				var redmineName = $("select[name='selectRedmine']").val().trim();
				var version=$("select[name='version']").val().trim();
				var sprintStart=$("input[name='sprintStart']").val().trim();
				var sprintEnd=$("input[name='sprintEnd']").val().trim();
				
				$.ajax({
					url : 'getFixedRateBySeverity',
					type : 'post',
					async: false,
					data : {
						'redmineName' : redmineName,
						'version':version,
						'sprintStart':sprintStart,
						'sprintEnd':sprintEnd
					},
					success : function(data) {
						if(data=='[]')
						{
							return;
						}
						var tempData=data.substring(2,data.length-2);
						var arrData=tempData.split('","');
						var columnValue=[];
						var columnName=[];
						var severityName=['Critical','Major','Average','Minor','Enhancement'];
						var severityValue=[0,0,0,0,0];
						for(var i=0;i<arrData.length;i++)
						{
							
							arrBugItem=arrData[i].split("#");
							switch(arrBugItem[0])
							{
								case 'Critical':
									severityValue[0]=parseFloat(arrBugItem[1]);
								case 'Major':
									severityValue[1]=parseFloat(arrBugItem[1]);
								case 'Average':
									severityValue[2]=parseFloat(arrBugItem[1]);
								case 'Minor':
									severityValue[3]=parseFloat(arrBugItem[1]);
								case 'Enhancement':
									severityValue[4]=parseFloat(arrBugItem[1]);
							}
							//columnName.push(arrBugItem[0]);
							//arrBugItem[1]=parseFloat(arrBugItem[1]);
							//columnValue.push(arrBugItem);
						}
						var chart=prepareFixedRateBySeverityChart();
						//chart.series[0].setData(columnValue,true);
						//chart.xAxis[0].setCategories(columnName);
						chart.series[0].setData(severityValue,true);
						chart.xAxis[0].setCategories(severityName);
					},
					error : function(data) {
					},
				});
			}
			
			
			function prepareFixedBugsBySeverityChart()
			{
				var chart;
				var options={
					chart: {
						type:"pie",
						renderTo:'divFixedBugsBySeverity'
					},
					title: {
						text: 'Fixed Bugs by Severity',
						style:{
	                		fontWeight:'bold'
						}
					},
					credits: { //右下角网址信息
						enabled: false
					},
					tooltip: {
						//pointFormat: 'zhangdi'
						//pointFormat: '{series.name}'
					},
					plotOptions: {
						pie: {
							allowPointSelect: true,
							cursor: 'pointer',
							dataLabels: {
								enabled: true,
								color: '#000000',
								connectorColor: '#000000',
								//format:'demo format'
								format: '<b>{point.name}</b>: {point.y}'
							}
						}
					},
					series: [{
						type: 'pie',
						name: 'Value',
						data: []
					}]
				}
				var chart = new Highcharts.Chart(options);
				return chart;
			}
			//=============================
			function getNewBugsBySeverity()
			{
				var redmineName = $("select[name='selectRedmine']").val().trim();
				var version=$("select[name='version']").val().trim();
				var sprintStart=$("input[name='sprintStart']").val().trim();
				var sprintEnd=$("input[name='sprintEnd']").val().trim();
				
				$.ajax({
					url : 'getNewBugsBySeverity',
					type : 'post',
					async: false,
					data : {
						'redmineName' : redmineName,
						'version':version,
						'sprintStart':sprintStart,
						'sprintEnd':sprintEnd
					},
					success : function(data) {
						if(data=='[]')
						{
							return;
						}
						var tempData=data.substring(2,data.length-2);
						var arrData=tempData.split('","');
						var pieData=[];
						for(var i=0;i<arrData.length;i++)
						{
							arrBugItem=arrData[i].split("#");
							arrBugItem[1]=parseInt(arrBugItem[1]);
							pieData.push(arrBugItem);
						}
						var chart=prepareNewBugsBySeverityChart();
						chart.series[0].setData(pieData);
					},
					error : function(data) {
					},
				});
			}
			
			function prepareNewBugsBySeverityChart()
			{
				var chart;
				var options={
					chart: {
						type:"pie",
						renderTo:'divNewBugsBySeverity'
					},
					title: {
						text: 'New Bugs by Severity',
						style:{
	                		fontWeight:'bold'
						}
					},
					credits: { //右下角网址信息
						enabled: false
					},
					tooltip: {
						//pointFormat: 'zhangdi'
						//pointFormat: '{series.name}'
					},
					plotOptions: {
						pie: {
							allowPointSelect: true,
							cursor: 'pointer',
							dataLabels: {
								enabled: true,
								color: '#000000',
								connectorColor: '#000000',
								//format:'demo format'
								format: '<b>{point.name}</b>: {point.y}'
							}
						}
					},
					series: [{
						type: 'pie',
						name: 'Value',
						data: []
					}]
				}
				var chart = new Highcharts.Chart(options);
				return chart;
			}
			//=========================Developer===============
			function getFixedBugsByDeveloper()
			{
				var redmineName = $("select[name='selectRedmine']").val().trim();
				var version=$("select[name='version']").val().trim();
				
				$.ajax({
					url : 'getFixedBugsByDeveloper',
					type : 'post',
					async: false,
					data : {
						'redmineName' : redmineName,
						'version':version
					},
					success : function(data) {
						if(data=='[]')
						{
							return;
						}
						var tempData=data.substring(2,data.length-2);
						var arrData=tempData.split('","');
						var pieData=[];
						for(var i=0;i<arrData.length;i++)
						{
							arrBugItem=arrData[i].split("#");
							arrBugItem[1]=parseInt(arrBugItem[1]);
							pieData.push(arrBugItem);
						}
						var chart=prepareFixedBugsByDeveloperChart();
						chart.series[0].setData(pieData);
					},
					error : function(data) {
					},
				});
			}
			
			function prepareFixedBugsByDeveloperChart()
			{
				var chart;
				var options={
					chart: {
						type:"pie",
						renderTo:'divFixedBugsByDeveloper'
					},
					title: {
						text: 'Fixed Bugs by Developer',
						style:{
	                		fontWeight:'bold'
						}
					},
					credits: { //右下角网址信息
						enabled: false
					},
					tooltip: {
						//pointFormat: 'zhangdi'
						//pointFormat: '{series.name}'
					},
					plotOptions: {
						pie: {
							allowPointSelect: true,
							cursor: 'pointer',
							dataLabels: {
								enabled: true,
								color: '#000000',
								connectorColor: '#000000',
								//format:'demo format'
								format: '<b>{point.name}</b>: {point.y}'
							}
						}
					},
					series: [{
						type: 'pie',
						name: 'Value',
						data: []
					}]
				}
				var chart = new Highcharts.Chart(options);
				return chart;
			}
			
			function getNewBugsByDeveloper()
			{
				var redmineName = $("select[name='selectRedmine']").val().trim();
				var version=$("select[name='version']").val().trim();
				var sprintStart=$("input[name='sprintStart']").val().trim();
				var sprintEnd=$("input[name='sprintEnd']").val().trim();
				
				$.ajax({
					url : 'getNewBugsByDeveloper',
					type : 'post',
					async: false,
					data : {
						'redmineName' : redmineName,
						'version':version,
						'sprintStart':sprintStart,
						'sprintEnd':sprintEnd
					},
					success : function(data) {
						if(data=='[]')
						{
							return;
						}
						var tempData=data.substring(2,data.length-2);
						var arrData=tempData.split('","');
						var pieData=[];
						for(var i=0;i<arrData.length;i++)
						{
							arrBugItem=arrData[i].split("#");
							arrBugItem[1]=parseInt(arrBugItem[1]);
							pieData.push(arrBugItem);
						}
						var chart=prepareNewBugsByDeveloperChart();
						chart.series[0].setData(pieData);
					},
					error : function(data) {
					},
				});
			}
			
			function prepareNewBugsByDeveloperChart()
			{
				var chart;
				var options={
					chart: {
						type:"pie",
						renderTo:'divNewBugsByDeveloper'
					},
					title: {
						text: 'New Bugs by Developer',
						style:{
	                		fontWeight:'bold'
						}
					},
					credits: { //右下角网址信息
						enabled: false
					},
					tooltip: {
						//pointFormat: 'zhangdi'
						//pointFormat: '{series.name}'
					},
					plotOptions: {
						pie: {
							allowPointSelect: true,
							cursor: 'pointer',
							dataLabels: {
								enabled: true,
								color: '#000000',
								connectorColor: '#000000',
								//format:'demo format'
								format: '<b>{point.name}</b>: {point.y}'
							}
						}
					},
					series: [{
						type: 'pie',
						name: 'Value',
						data: []
					}]
				}
				var chart = new Highcharts.Chart(options);
				return chart;
			}
			
			
			function getFixedRateByDeveloper()
			{
				var redmineName = $("select[name='selectRedmine']").val().trim();
				var version=$("select[name='version']").val().trim();
				var sprintStart=$("input[name='sprintStart']").val().trim();
				var sprintEnd=$("input[name='sprintEnd']").val().trim();
				
				$.ajax({
					url : 'getFixedRateByDeveloper',
					type : 'post',
					async: false,
					data : {
						'redmineName' : redmineName,
						'version':version,
						'sprintStart':sprintStart,
						'sprintEnd':sprintEnd
					},
					success : function(data) {
						if(data=='[]')
						{
							return;
						}
						var tempData=data.substring(2,data.length-2);
						var arrData=tempData.split('","');
						var columnValue=[];
						var columnName=[];
						for(var i=0;i<arrData.length;i++)
						{
							arrBugItem=arrData[i].split("#");
							columnName.push(arrBugItem[0]);
							arrBugItem[1]=parseFloat(arrBugItem[1]);
							columnValue.push(arrBugItem);
						}
						var chart=prepareFixedRateByDeveloperChart();
						chart.series[0].setData(columnValue,true);
						chart.xAxis[0].setCategories(columnName);
					},
					error : function(data) {
					},
				});
			}
			
			function getImplementedFeaturesByDeveloper()
			{
				var redmineName = $("select[name='selectRedmine']").val().trim();
				var version=$("select[name='version']").val().trim();
				
				$.ajax({
					url : 'getImplementedFeaturesByDeveloper',
					type : 'post',
					async: false,
					data : {
						'redmineName' : redmineName,
						'version':version
					},
					success : function(data) {
						if(data=='[]')
						{
							return;
						}
						var tempData=data.substring(2,data.length-2);
						var arrData=tempData.split('","');
						var pieData=[];
						for(var i=0;i<arrData.length;i++)
						{
							arrBugItem=arrData[i].split("#");
							arrBugItem[1]=parseInt(arrBugItem[1]);
							pieData.push(arrBugItem);
						}
						var chart=prepareImplementedFeaturesByDeveloperChart();
						chart.series[0].setData(pieData);
					},
					error : function(data) {
					},
				});
			}
			
			function prepareImplementedFeaturesByDeveloperChart()
			{
				var chart;
				var options={
					chart: {
						type:"pie",
						renderTo:'divImplementedFeaturesByDeveloper'
					},
					title: {
						text: 'Implemented Features by Developer',
						style:{
	                		fontWeight:'bold'
						}
					},
					credits: { //右下角网址信息
						enabled: false
					},
					tooltip: {
						//pointFormat: 'zhangdi'
						//pointFormat: '{series.name}'
					},
					plotOptions: {
						pie: {
							allowPointSelect: true,
							cursor: 'pointer',
							dataLabels: {
								enabled: true,
								color: '#000000',
								connectorColor: '#000000',
								//format:'demo format'
								format: '<b>{point.name}</b>: {point.y}'
							}
						}
					},
					series: [{
						type: 'pie',
						name: 'Value',
						data: []
					}]
				}
				var chart = new Highcharts.Chart(options);
				return chart;
			}
			
			function getFixedBugsByCategory()
			{
				var redmineName = $("select[name='selectRedmine']").val().trim();
				var version=$("select[name='version']").val().trim();
				
				$.ajax({
					url : 'getFixedBugsByCategory',
					type : 'post',
					async: false,
					data : {
						'redmineName' : redmineName,
						'version':version
					},
					success : function(data) {
						if(data=='[]')
						{
							return;
						}
						var tempData=data.substring(2,data.length-2);
						var arrData=tempData.split('","');
						var pieData=[];
						for(var i=0;i<arrData.length;i++)
						{
							arrBugItem=arrData[i].split("#");
							arrBugItem[1]=parseInt(arrBugItem[1]);
							pieData.push(arrBugItem);
						}
						var chart=prepareFixedBugsByCategoryChart();
						chart.series[0].setData(pieData);
					},
					error : function(data) {
					},
				});
			}
			
			function getNewBugsByCategory()
			{
				var redmineName = $("select[name='selectRedmine']").val().trim();
				var version=$("select[name='version']").val().trim();
				var sprintStart=$("input[name='sprintStart']").val().trim();
				var sprintEnd=$("input[name='sprintEnd']").val().trim();
				
				$.ajax({
					url : 'getNewBugsByCategory',
					type : 'post',
					async: false,
					data : {
						'redmineName' : redmineName,
						'version':version,
						'sprintStart':sprintStart,
						'sprintEnd':sprintEnd
					},
					success : function(data) {
						if(data=='[]')
						{
							return;
						}
						var tempData=data.substring(2,data.length-2);
						var arrData=tempData.split('","');
						var pieData=[];
						for(var i=0;i<arrData.length;i++)
						{
							arrBugItem=arrData[i].split("#");
							arrBugItem[1]=parseInt(arrBugItem[1]);
							pieData.push(arrBugItem);
						}
						var chart=prepareNewBugsByCategoryChart();
						chart.series[0].setData(pieData);
					},
					error : function(data) {
					},
				});
			}
			
			function getFixedRateByCategory()
			{
				var redmineName = $("select[name='selectRedmine']").val().trim();
				var version=$("select[name='version']").val().trim();
				var sprintStart=$("input[name='sprintStart']").val().trim();
				var sprintEnd=$("input[name='sprintEnd']").val().trim();
				
				$.ajax({
					url : 'getFixedRateByCategory',
					type : 'post',
					async: false,
					data : {
						'redmineName' : redmineName,
						'version':version,
						'sprintStart':sprintStart,
						'sprintEnd':sprintEnd
					},
					success : function(data) {
						if(data=='[]')
						{
							return;
						}
						var tempData=data.substring(2,data.length-2);
						var arrData=tempData.split('","');
						var columnValue=[];
						var columnName=[];
						for(var i=0;i<arrData.length;i++)
						{
							arrBugItem=arrData[i].split("#");
							columnName.push(arrBugItem[0]);
							arrBugItem[1]=parseFloat(arrBugItem[1]);
							columnValue.push(arrBugItem);
						}
						var chart=prepareFixedRateByCategoryChart();
						chart.series[0].setData(columnValue,true);
						chart.xAxis[0].setCategories(columnName);
					},
					error : function(data) {
					},
				});
			}
			
			function getImplementedFeaturesByCategory()
			{
				var redmineName = $("select[name='selectRedmine']").val().trim();
				var version=$("select[name='version']").val().trim();
				
				$.ajax({
					url : 'getImplementedFeaturesByCategory',
					type : 'post',
					async: false,
					data : {
						'redmineName' : redmineName,
						'version':version
					},
					success : function(data) {
						if(data=='[]')
						{
							return;
						}
						var tempData=data.substring(2,data.length-2);
						var arrData=tempData.split('","');
						var pieData=[];
						for(var i=0;i<arrData.length;i++)
						{
							arrBugItem=arrData[i].split("#");
							arrBugItem[1]=parseInt(arrBugItem[1]);
							pieData.push(arrBugItem);
						}
						var chart=prepareImplementedFeaturesByCategoryChart();
						chart.series[0].setData(pieData);
					},
					error : function(data) {
					},
				});
			}
			
			function prepareImplementedFeaturesByCategoryChart()
			{
				var chart;
				var options={
					chart: {
						type:"pie",
						renderTo:'divImplementedFeaturesByCategory'
					},
					title: {
						text: 'Implemented Features by Category',
						style:{
	                		fontWeight:'bold'
						}
					},
					credits: { //右下角网址信息
						enabled: false
					},
					tooltip: {
						//pointFormat: 'zhangdi'
						//pointFormat: '{series.name}'
					},
					plotOptions: {
						pie: {
							allowPointSelect: true,
							cursor: 'pointer',
							dataLabels: {
								enabled: true,
								color: '#000000',
								connectorColor: '#000000',
								//format:'demo format'
								format: '<b>{point.name}</b>: {point.y}'
							}
						}
					},
					series: [{
						type: 'pie',
						name: 'Value',
						data: []
					}]
				}
				var chart = new Highcharts.Chart(options);
				return chart;
			}
			
			function prepareFixedBugsByCategoryChart()
			{
				var chart;
				var options={
					chart: {
						type:"pie",
						renderTo:'divFixedBugsByCategory'
					},
					title: {
						text: 'Fixed Bugs by Category',
						style:{
	                		fontWeight:'bold'
						}
					},
					credits: { //右下角网址信息
						enabled: false
					},
					tooltip: {
						//pointFormat: 'zhangdi'
						//pointFormat: '{series.name}'
					},
					plotOptions: {
						pie: {
							allowPointSelect: true,
							cursor: 'pointer',
							dataLabels: {
								enabled: true,
								color: '#000000',
								connectorColor: '#000000',
								//format:'demo format'
								format: '<b>{point.name}</b>: {point.y}'
							}
						}
					},
					series: [{
						type: 'pie',
						name: 'Value',
						data: []
					}]
				}
				var chart = new Highcharts.Chart(options);
				return chart;
			}
			
			function prepareNewBugsByCategoryChart()
			{
				var chart;
				var options={
					chart: {
						type:"pie",
						renderTo:'divNewBugsByCategory'
					},
					title: {
						text: 'New Bugs by Category',
						style:{
	                		fontWeight:'bold'
						}
					},
					credits: { //右下角网址信息
						enabled: false
					},
					tooltip: {
						//pointFormat: 'zhangdi'
						//pointFormat: '{series.name}'
					},
					plotOptions: {
						pie: {
							allowPointSelect: true,
							cursor: 'pointer',
							dataLabels: {
								enabled: true,
								color: '#000000',
								connectorColor: '#000000',
								//format:'demo format'
								format: '<b>{point.name}</b>: {point.y}'
							}
						}
					},
					series: [{
						type: 'pie',
						name: 'Value',
						data: []
					}]
				}
				var chart = new Highcharts.Chart(options);
				return chart;
			}
			
			function prepareFixedRateByCategoryChart()
			{
				var chart;
				var options={
					chart:{
						renderTo:'divFixedRateByCategory',
						type:'column'
					},
					title: {
						text: 'Fixed Rate by Category',
						style:{
	                		fontWeight:'bold'
						}
					},
					credits: { //右下角网址信息
						enabled: false
					},
					xAxis: {
						title:{
							text:'Category',
							/* align:'high', */
							style:{
		                		fontWeight:'bold',
		                		fontSize:'16px',
		                		width:'200px',
		                		marginLeft:'auto',
		                		marginRight:'auto'
		                	}
						},
						categories: [],
						labels: {
							rotation: 60
						}
					},
					yAxis: {
						title: {
							text: 'Rate',
							style:{
		                		fontWeight:'bold',
		                		fontSize:'16px',
                			}
						}
					},
					legend:{
						enabled:false
					},
					plotOptions: {
						column:{
							pointPadding: 0.2,
							borderWidth: 0
						}
					},
					tooltip: {
						formatter:function(){
							return ' <b>'+ this.x +
							'</b><br/><b>'+ parseInt(this.y * 100) +'%</b>';
						}
					},
					series: [{
						data: []
					}]
				};
				var chart = new Highcharts.Chart(options);
				return chart;
			}
			
			
			function prepareFixedRateByDeveloperChart()
			{
				var chart;
				var options={
					chart:{
						renderTo:'divFixedRateByDeveloper',
						type:'column'
					},
					title: {
						text: 'Fixed Rate by Developer',
						 
						style:{
	                		fontWeight:'bold'
						}
					},
					credits: { //右下角网址信息
						enabled: false
					},
					xAxis: {
						title:{
							text:'Developer',
							/* align:'high', */
							style:{
		                		fontWeight:'bold',
		                		fontSize:'16px',
		                		width:'200px',
		                		marginLeft:'auto',
		                		marginRight:'auto'
		                	}
						},
						categories: [],
						labels: {
							rotation: 60
						}
					},
					yAxis: {
						title: {
							text: 'Rate',
							style:{
		                		fontWeight:'bold',
		                		fontSize:'16px',
                			}
						}
					},
					legend:{
						enabled:false
					},
					plotOptions: {
						column:{
							pointPadding: 0.2,
							borderWidth: 0
						}
					},
					tooltip: {
						formatter:function(){
							return ' <b>'+ this.x +
							'</b><br/><b>'+ parseInt(this.y * 100) +'%</b>';
						}
					},
					series: [{
						data: []
					}]
				};
				var chart = new Highcharts.Chart(options);
				return chart;
			}
			
			function prepareFixedRateBySeverityChart()
			{
				var chart;
				var options={
					chart:{
						renderTo:'divFixedRateBySeverity',
						type:'column'
					},
					title: {
						text: 'Fixed Rate by Severity',
						 
						style:{
						fontWeight:'bold'
						}
					},
					credits: { //右下角网址信息
						enabled: false
					},
					xAxis: {
						title:{
							text:'Severity',
							style:{
								fontWeight:'bold',
								fontSize:'16px',
								width:'200px',
								marginLeft:'auto',
								marginRight:'auto'
							}
						},
						categories: [],
						labels: {
							rotation: 60
						}
					},
					yAxis: {
						title: {
							text: 'Rate',
							style:{
		                		fontWeight:'bold',
		                		fontSize:'16px',
                			}
						}
					},
					legend:{
						enabled:false
					},
					plotOptions: {
						column:{
							pointPadding: 0.2,
							borderWidth: 0
						}
					},
					tooltip: {
						formatter:function(){
							return ' <b>'+ this.x +
							'</b><br/><b>'+ parseInt(this.y * 100) +'%</b>';
						}
					},
					series: [{
						data: []
					}]
				};
				var chart = new Highcharts.Chart(options);
				return chart;
			}
		</script>
	</body>
</html>