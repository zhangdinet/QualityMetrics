<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<label>筛选产品模块</label>
<input name="selectProject" type="text" id="txtSelectProject" class="from-control" placeholder="输入后，请回车" style="padding-left:5px" onchange="selectProject(this)" value=""/>

<table id="tbl_rankings">
	<tr>
		<th>排名</th>
		<th>产品模块名称</th>
		<th>总平均分</th>
		<th>详情</th>
	</tr>
	<c:forEach items="${projectList}" var="item" varStatus="status">
		<tr>
			<td>${status.count}</td>
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

<script type="text/javascript">
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
</script>