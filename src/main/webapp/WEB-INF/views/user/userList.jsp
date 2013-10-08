<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="root" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>用户列表</title>
</head>
<body>

<h1><a href="<c:url value="/" />">首页</a></h1>
<hr>
<div><a href="<c:url value="/user/addInit" />">新增用戶</a></div>
<table border=1>
  <tr>
    <td>序号</td>
    <td>姓名</td>
    <td>密码</td>
    <td>年龄</td>
    <td>操作</td>
  </tr>
  <c:forEach items="${userList }" var="user" varStatus="status">
  <tr>
    <td>${status.count }</td>
    <td>${user.name }</td>
    <td>${user.pass }</td>
    <td>${user.age }</td>
    <td><a href='<c:url value="/user/updateInit/${user.id }" />'>修改</a>&nbsp;&nbsp;
    |&nbsp;&nbsp;<a href='<c:url value="/user/delete/${user.id }" />'>删除</a></td>
  </tr>
  </c:forEach>
</table>
<hr>


</body>
</html>