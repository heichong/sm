<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="root" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>用户管理</title>
</head>
<body>

<h1><a href="<c:url value="/" />">首页</a></h1>
<hr>

<form:form method="post" modelAttribute="user" action="${root }/user/save">
<form:hidden path="id" />
<form:errors path="*" cssClass="error" />
<table>
  <tr>
    <td>姓名</td>
    <td><form:input path="name" /></td>
    <td><form:errors path="name" cssClass="error" /></td>
  </tr>
  <tr>
    <td>密码</td>
    <td><form:input path="pass" /></td>
    <td><form:errors path="pass" cssClass="error" /></td>
  </tr>
  <tr>
    <td>年龄</td>
    <td><form:input path="age" /></td>
    <td><form:errors path="age" cssClass="error" /></td>
  </tr>
  <tr>
    <td colspan="3"><input type="submit" /></td>
  </tr>
</table>
</form:form>

</body>
</html>