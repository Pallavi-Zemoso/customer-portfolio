<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <title>Maya Company Home Page</title>
	    <link rel="stylesheet" href="css/style.css" />
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
            <p> Welcome to Customer Management Systems Portal !! </p>
            <br>
            <a href="${pageContext.request.contextPath}/customer">Customer List</a> <sec:authorize access="!isAuthenticated()">(requires login)</sec:authorize>
    	</div>
    </body>
</html>