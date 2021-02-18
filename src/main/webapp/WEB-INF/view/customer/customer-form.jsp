<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>

<html>

<head>
	<title>Customer Form</title>

	<!-- reference our style sheet -->
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/add-customer-style.css" />
</head>

<body>
	<div id="wrapper">
		<div id="header">
			<h2>CMS - Customer Management Systems</h2>
		</div>
		<div id="userSection">
		    <sec:authorize access="isAuthenticated()">
			    <span width=70%>
			        Welcome <sec:authentication property="principal.username" />!!
			        <form:form action="${pageContext.request.contextPath}/logout" method="POST" float="right" style="display:inline; float: right">
                        <input class="my-button" type="submit" value="Logout" />
                    </form:form>
			        <br>
                    Roles: <sec:authentication property="principal.authorities" />
			    </span><br><br>
            </sec:authorize>
		</div>
	</div>

	<div id="container">
		<h3>Customer Details</h3>
        <c:if test="${param.error != null}">
                                <i class="failed"> Invalid username/password </i>
        </c:if>
		<form:form action="${pageContext.request.contextPath}/customer${todo}" modelAttribute="customer" method="POST">
		    <form:hidden path="id" />
			<table>
				<tbody>
					<tr>
						<td><label>First name:</label></td>
						<td><form:input path="firstName" /></td>
		                <td class="error-td"><form:errors path="firstName" cssClass="error-text"/></td>
					</tr>

					<tr>
						<td><label>Last name:</label></td>
						<td><form:input path="lastName" /></td>
		                <td><form:errors path="lastName" cssClass="error-text"/></td>
					</tr>

					<tr>
						<td><label>Email:</label></td>
						<td><form:input path="email" /></td>
		                <td><form:errors path="email" cssClass="error-text"/></td>
					</tr>

					<tr>
						<td colspan=3><input type="submit" value="Save" class="save" /></td>
					</tr>
				</tbody>
			</table>
		</form:form>
		<div style="clear; both;"></div>
		<br>
		<p>
			<a href="${pageContext.request.contextPath}/customer">Back to List</a>
		</p>
	</div>
</body>
</html>









