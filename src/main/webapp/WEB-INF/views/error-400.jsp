<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    Throwable t = (Throwable) request.getAttribute("myException");
    java.util.logging.Logger.getLogger(getClass().getName()).info("myException = " + t);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><c:out value="${project.name}"/> Bad Request Error</title>
        <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/files/main.css" type="text/css">
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/header.jspf" %>
        <div class="main">
            <h1>400 Bad Request</h1>
            <div>
                This is a custom error page.
                <c:out value="${myException.message}"/>
            </div>
        </div>
    </body>
</html>
