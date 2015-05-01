<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><c:out value="${project.name}"/> Administration Tools</title>
        <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/files/main.css" type="text/css">
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/header.jspf" %>
        <div class="main">
            <h2>User Accounts</h2>
            <div>
                <ul><a href="account/password-reset/request">Reset Password</a></ul>
            </div>
        </div>
    </body>
</html>
