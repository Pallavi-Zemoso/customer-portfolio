<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>List Customers</title>

    <!-- reference our style sheet -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <style>

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

    <div id="container">
        <div id="content">
            <table>
                <tbody>
                   <tr>
                      <td>
                          <form:form action="${pageContext.request.contextPath}/customer/search" method="GET">
                              Search customer: <input type="text" name="searchString" value=${searchString}></input>
                              <input type="submit" name ="formAction" value="Search" class="my-button" />
                              <input type="submit" name ="formAction" value="Clear" class="my-button" />
                          </form:form>
                      </td>
                      <sec:authorize access="hasAnyRole('MANAGER','ADMIN')">
                        <td>
                             <input type="button" value="Add Customer"
                                    onclick="window.location.href='customer/show-add-form'; return false;"
                                    class="my-button"
                        </td>
                      </sec:authorize>
                    </tr>
                </tbody>
            </table><br><br>

            <!--  add our html table here -->
            <table>
                <tr>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <sec:authorize access="hasAnyRole('MANAGER','ADMIN')">
                        <th>Action</th>
                    </sec:authorize>
                </tr>
                <!-- Check if list is empty -->
                <c:choose>
                    <c:when test="${empty customerDTOList}">
                       <tr><td colspan="4">No data available</td></tr>
                    </c:when>
                         <c:otherwise>
                             <!-- loop over and print our customers -->
                             <c:forEach var="tempCustomer" items="${customerDTOList}">
                                 <c:url var="updateLink" value="/customer/${tempCustomer.id}/show-update-form" />
                                 <c:url var="deleteLink" value="/customer/${tempCustomer.id}/delete"/>
                                 <tr>
                                     <td> ${tempCustomer.firstName} </td>
                                     <td> ${tempCustomer.lastName} </td>
                                     <td> ${tempCustomer.email} </td>
                                     <sec:authorize access="hasAnyRole('MANAGER','ADMIN')">
                                       <td>
                                         <a href="${updateLink}">Update</a>
                                         <sec:authorize access="hasRole('ADMIN')">
                                             <a href="${deleteLink}" onclick="if (!confirm('Confirm to delete')) return false">Delete</a>
                                         </sec:authorize>
                                       </td>
                                     </sec:authorize>
                                 </tr>
                             </c:forEach>
                        </c:otherwise>
                    </c:choose>
            </table>
        </div>
    </div>
</body>
</html>









