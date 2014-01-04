<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<P>  The time on the server is ${serverTime}. </P>

<h1>系统功能列表</h1>
<hr>
<ul>
	<li><a href="<c:url value="/user/list" />">用户管理</a></li>
	<li><a href="<c:url value="/product/nimibus" />">奇诺饰品</a></li>
	<li><a href="<c:url value="/product/pj" />">平价饰品</a></li>
	<li><a href="<c:url value="/product/ass" />">爱时尚淘宝数据csv导入</a></li>
</ul>
</body>
</html>
