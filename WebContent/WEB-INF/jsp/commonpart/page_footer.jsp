<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<div class="page_footer">
<form id="navigatorForm">
	<a href="#" onclick="gotoPage('1')">首页</a>&nbsp;
	<c:choose>
		<c:when test="${pageNumber > 1}">
			<a href="#" onclick="gotoPage('${pageNumber-1 }')">上一页</a>
		</c:when>
		<c:otherwise>
			上一页
		</c:otherwise>
	</c:choose>
	<c:if test="${pageNumber > 1}">
	</c:if>
	&nbsp; 跳转到第&nbsp;
	<select name="pageNumber" onchange="gotoSelectedPage();">
		<c:forEach begin="1" end="${totalPages}" var="pageIndex">
			<c:choose>
				<c:when test="${pageIndex eq pageNumber}">
					<option value="${pageIndex}" selected="selected">${pageIndex}</option>
				</c:when>
				<c:otherwise>
					 <option value="${pageIndex}">${pageIndex}</option>  
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</select>&nbsp;页   &nbsp;
	<c:choose>
		<c:when test="${pageNumber<totalPages}">
			  <a href="#" onclick="gotoPage('${pageNumber+1 }')">下一页</a>  
		</c:when>
		<c:otherwise>
			下一页
		</c:otherwise>
	</c:choose> 
	&nbsp;
    <a href="#" onclick="gotoPage('${totalPages }')">末页</a> 
           &nbsp;每页${pageSize }条记录
</form>
</div>