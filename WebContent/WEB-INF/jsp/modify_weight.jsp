<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>衡量指标权重配置</title>
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.4.1/build/cssreset/cssreset-min.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/bootstrap-theme.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico" type="image/x-icon" />
<link rel="Bookmark" href="${pageContext.request.contextPath}/img/favicon.ico" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.js"></script>
</head>
<body>
	<jsp:include page="commonpart/headerLogoName.jsp"></jsp:include>
	<jsp:include page="commonpart/mainMenu.jsp"></jsp:include>
	<div class="detailwrapper">
		<div class="sprintdetail">
			<h1>衡量指标权重录入</h1>
			<form id="form" method="post">
				<div style="width:540px;margin-left:auto;margin-right:auto">
					<div id="divAlignLeft">
						<label>IPD过程质量（PDT或LMT）</label>
						<input name="ipdOrLmt_rate" class="form-control" value="${weightDto.ipdOrLmt_rate }" />
					
						<label>代码整体质量</label>
						<input name="sonar_rate" class="form-control" value="${weightDto.sonar_rate }" />
					
						<label>测试通过率</label>
						<input name="test_pass_rate" class="form-control" value="${weightDto.test_pass_rate }" />
					
						<label>测试可执行率</label>
						<input name="tc_exec_rate" class="form-control" value="${weightDto.tc_exec_rate }" />
					
						<label>缺陷新增率</label>
						<input name="bug_new_rate" class="form-control" value="${weightDto.bug_new_rate }" />
					</div>
						
					<div id="divAlignRight">
						<label>缺陷返工率</label>
						<input name="bug_reopen_rate" class="form-control" value="${weightDto.bug_reopen_rate }" />
					
						<label>缺陷遗漏率</label>
						<input name="bug_escape_rate" class="form-control" value="${weightDto.bug_escape_rate }" />
					
						<label>补丁发布率</label>
						<input name="rate_patch_rate" class="form-control" value="${weightDto.rate_patch_rate }" />
					
						<label>技术支持率</label>
						<input name="rate_support_rate" class="form-control" value="${weightDto.rate_support_rate }" />
					
						<label>客户满意度</label>
						<input name="rate_ce_rate" class="form-control" value="${weightDto.rate_ce_rate }" />
					</div>
					<div id="divWeightButton">
						<input type="button" class="btn btn-primary" value="保存" onclick="send(0)" />
						<input type="button" class="btn btn-primary" value="返回" onclick="send(1)" />
						<span class="spanTipWeight" id="project_tipLabel">
							<c:if test="${updateResult eq 'ok'}">修改成功！</c:if>
							<c:if test="${updateResult eq 'err'}">修改失败！</c:if>
						</span>
					</div>
				</div>
			</form>
		</div>
		
	</div>
	<div class="footer">
			<jsp:include page="commonpart/footer.jsp"></jsp:include>
	</div>
	<script type="text/javascript">
		var ipdOrLmt_rate_load=0;
		var sonar_rate_load=0;
		var test_pass_rate_load=0;
		var tc_exec_rate_load=0;
		var bug_new_rate_load=0;
		var bug_reopen_rate_load=0;
		var bug_escape_rate_load=0;
		var rate_patch_rate_load=0;
		var rate_support_rate_load=0;
		var rate_ce_rate_load=0;

		$(function()
		{
			ipdOrLmt_rate_load = $("input[name='ipdOrLmt_rate']").val();
			sonar_rate_load = $("input[name='sonar_rate']").val();
			test_pass_rate_load = $("input[name='test_pass_rate']").val();
			tc_exec_rate_load = $("input[name='tc_exec_rate']").val();
			bug_new_rate_load = $("input[name='bug_new_rate']").val();
			bug_reopen_rate_load = $("input[name='bug_reopen_rate']").val();
			bug_escape_rate_load = $("input[name='bug_escape_rate']").val();
			rate_patch_rate_load = $("input[name='rate_patch_rate']").val();
			rate_support_rate_load = $("input[name='rate_support_rate']").val();
			rate_ce_rate_load = $("input[name='rate_ce_rate']").val();
		})
		function send(i)
		{
			if (i == 0)
			{
				var ipdOrLmt_rate = $("input[name='ipdOrLmt_rate']").val();
				var sonar_rate = $("input[name='sonar_rate']").val();
				var test_pass_rate = $("input[name='test_pass_rate']").val();
				var tc_exec_rate = $("input[name='tc_exec_rate']").val();
				var bug_new_rate = $("input[name='bug_new_rate']").val();
				var bug_reopen_rate = $("input[name='bug_reopen_rate']").val();
				var bug_escape_rate = $("input[name='bug_escape_rate']").val();
				var rate_patch_rate = $("input[name='rate_patch_rate']").val();
				var rate_support_rate = $("input[name='rate_support_rate']").val();
				var rate_ce_rate = $("input[name='rate_ce_rate']").val();
				
				if (ipdOrLmt_rate == "" || sonar_rate == "" || test_pass_rate == "" || tc_exec_rate == ""
						|| bug_new_rate == "" || bug_reopen_rate == "" || bug_escape_rate == "" || rate_patch_rate == ""
						|| rate_support_rate == "" || rate_ce_rate == "") {
					$("#project_tipLabel").html("请完整填写信息！");
					return;
				}
				
				if(isNaN(ipdOrLmt_rate)||isNaN(sonar_rate)||isNaN(test_pass_rate)||isNaN(tc_exec_rate)||isNaN(bug_new_rate)
					||isNaN(bug_reopen_rate)||isNaN(bug_escape_rate)||isNaN(rate_patch_rate)||isNaN(rate_support_rate)||isNaN(rate_ce_rate))
				{
					$("#project_tipLabel").html("权重值为[0-100]之间的数字");
					return;
				}
				
				var ipdOrLmt_float =parseFloat(ipdOrLmt_rate);
				var sonar_float =parseFloat(sonar_rate);
				var test_pass_float =parseFloat(test_pass_rate);
				var tc_exec_float =parseFloat(tc_exec_rate);
				var bug_new_float =parseFloat(bug_new_rate);
				var bug_reopen_float =parseFloat(bug_reopen_rate);
				var bug_escape_float =parseFloat(bug_escape_rate);
				var rate_patch_float =parseFloat(rate_patch_rate);
				var rate_support_float =parseFloat(rate_support_rate);
				var rate_ce_float =parseFloat(rate_ce_rate);
				
				if (!checkNumber(ipdOrLmt_float) || !checkNumber(sonar_float) || !checkNumber(test_pass_float) || !checkNumber(tc_exec_float)
						|| !checkNumber(bug_new_float) || !checkNumber(bug_reopen_float) || !checkNumber(bug_escape_float) || !checkNumber(rate_patch_float)
						|| !checkNumber(rate_support_float) || !checkNumber(rate_ce_float)) {
					$("#project_tipLabel").html("权重值为[0-100]之间的数字");
					return;
				}
				
				if(ipdOrLmt_float==ipdOrLmt_rate_load && sonar_float == sonar_rate_load && test_pass_float == test_pass_rate_load
					&& tc_exec_float == tc_exec_rate_load && bug_new_float == bug_new_rate_load && bug_reopen_float == bug_reopen_rate_load && bug_escape_float == bug_escape_rate_load
					&& rate_patch_float == rate_patch_rate_load && rate_support_float == rate_support_rate_load && rate_ce_float == rate_ce_rate_load )
				{
					$("#project_tipLabel").html("分数值未做改动，无需修改！");
					return;
				}
				else
				{
					$("#form").attr("action", "saveModifyWeight");
				}
			}
			else if (i == 1)
			{
				$("#form").attr("action", "weightlist");
			}
			$("#form").submit();
		}
		
		function checkNumber(num)
		{
			if(num>=0 && num<=100)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	</script>
</body>
</html>