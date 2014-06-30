<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h3>指标权重信息</h3>
<form action = "modifyWeight" style="float:right">
<c:if test="${sessionScope.flag_admin == 'yes' and sessionScope.project_id == 0}">
<input type="submit" class="btn btn-primary" value="设置"></input>
</c:if>
</form>
<table id="tbl_indicator_weight">
	<tr>
	<th>衡量指标</th>
	<th>权重值</th>
	</tr>
	<tr>
	<td> 
	IPD过程质量（PDT或LMT）
	</td>
	<td>${weightDto.ipdOrLmt_rate }</td>
	</tr>
	<tr>
	<td>
		代码整体质量
	</td>
	<td>${weightDto.sonar_rate }</td>
	</tr>
	<tr>
	<td> 
	测试通过率
	</td>
	<td>${weightDto.test_pass_rate }</td>
	</tr>
	<tr>
	<td> 
	测试可执行率
	</td>
	<td>${weightDto.tc_exec_rate }</td>
	</tr>
	<tr>
	<td> 
	缺陷新增率
	</td>
	<td>${weightDto.bug_new_rate }</td>
	</tr>
	<tr>
	<td> 
	缺陷返工率
	</td>
	<td>${weightDto.bug_reopen_rate }</td>
	</tr>
	<tr>
	<td> 
	缺陷遗漏率
	</td>
	<td>${weightDto.bug_escape_rate }</td>
	</tr>
	<tr>
	<td> 
	补丁发布率
	</td>
	<td>${weightDto.rate_patch_rate }</td>
	</tr>
	<tr>
	<td> 
	技术支持率
	</td>
	<td>${weightDto.rate_support_rate }</td>
	</tr>
	<tr>
	<td> 
	客户满意度
	</td>
	<td>${weightDto.rate_ce_rate }</td>
	</tr>
</table>
