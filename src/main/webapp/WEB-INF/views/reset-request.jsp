<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><c:out value="${project.name}"/> Accounts</title>
        <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/files/main.css" type="text/css">
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/header.jspf" %>
        <div class="main">
            <h1><c:out value="${project.name}"/> Accounts - Request Password Reset</h1>
            <div>
                <form name="resetrequest" accept-charset="UTF-8" role="form">
                    <fieldset>
                        <div class="form-group">
                            <input class="form-control" placeholder="Email" name="email" type="text" value="<c:out value='${userData.profile.email}'/>">
                        </div>
                        <input type="submit" value="Send Reset Request">
                        <div class="status-indicator status-inline status-small">
                            <img src="${pagecontext.request.contextPath}/files/images/loading.gif" alt="Processing">
                        </div>
                    </fieldset>
                </form>
                <div class="result">
                    <span id="send-request-result"></span>
                </div>
            </div>
        </div>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
        <script src="${pageContext.request.contextPath}/files/main.js" type="text/javascript"></script>
        <script>
$(document).ready(function(){
    var form = $('form[name="resetrequest"]');
    var resultSpan = $('#send-request-result');
    var responseMessageCreator = function(data) {
        return 'Password reset email from change-me@stormpath.com has been sent to ' + data.email + '.';
    };
    new FormResponseHandler(form, resultSpan, 'request/send', responseMessageCreator).activate();
});
        </script>
    </body>
</html>
