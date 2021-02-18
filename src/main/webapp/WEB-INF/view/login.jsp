<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
    <head>
        <title>Maya Company Login Page</title>
	    <link rel="stylesheet" href="css/style.css" />
	    <link rel="stylesheet" href="css/add-customer-style.css" />
        <style>
            .failed {
                color: red;
            }
            .info {
                color: blue;
            }
        </style>
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
        <h2>Login Page</h2>
        <form:form action="${pageContext.request.contextPath}/authenticate" method="POST">
            <table>
                <tbody>
                    <tr>
                        <td colspan="2">
                            <c:if test="${param.error != null}">
                                <i class="failed"> Invalid username/password </i>
                            </c:if>
                            <c:if test="${param.logout != null}">
                                <i class="info"> User has been logged out </i>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td><label>Username:</label></td>
                        <td><input type="text" name="username" /></td>
                    </tr>

                    <tr>
                        <td><label>Password:</label></td>
                        <td><input type="password" name="password" /></td>
                    </tr>
                    <tr colspan="2">
                        <td><input type="submit" value="Login" class="save"/></td>
                    </tr>
                </tbody>
            </table>
            <!-- Manual CSRF if we are not using spring form
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            -->
        </form:form>
    </body>
</html>