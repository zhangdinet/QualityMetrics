<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>  
<!-- <div id="sprint">2014Q4</div> -->
<div class="loginBox">
					<div class="loginBoxCenter">
						<p><label for="password">原密码</label></p>
						<p><input type="password" id="oldPassword" name="password" class="loginInput" placeholder="请输入原密码" value="" /></p>
						
						<p><label for="password">新密码</label></p>
						<p><input type="password" id="newPassword" name="password" class="loginInput" placeholder="请输入新密码" value="" /></p>
						
						<p><label for="password">确认新密码</label></p>
						<p><input type="password" id="confirmPassword" name="password" class="loginInput" placeholder="请确认新密码" value="" /></p>
						<p><label for="password" id = "tipLabel" style="color:red">
							<c:if test="${modifyResult eq 'ok'}">修改成功，下次登陆生效！</c:if>
							<c:if test="${modifyResult eq 'err'}">修改失败！</c:if>
						</label></p>
					</div>
					<div class="loginBoxButtons">
						<button class="loginBtnCenter" id = "save" onclick = "modifyPassword()">保存</button>
						<button class= "loginBtn" id = "cancel" onclick = "cancelModifyPassword()">取消</button>
					</div>
</div>