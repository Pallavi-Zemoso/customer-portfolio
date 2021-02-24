<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Error Page</title>
	    <link rel="stylesheet"
		  href="${pageContext.request.contextPath}/css/style.css" />
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
        <h4>${errorObject.message}</h4>
        <span>
        Status : ${errorObject.status} <br>
        Timestamp : ${errorObject.timestamp}
        </span>
        <br><br>
		<sec:authorize access="!isAuthenticated()"><a href="${pageContext.request.contextPath}/" >Go Back</a></sec:authorize>
		<sec:authorize access="isAuthenticated()"><a href="${pageContext.request.contextPath}/customers" >Go back</a></sec:authorize>
    </div>
    </body>
</html>