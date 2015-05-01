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
            <h1><c:out value="${project.name}"/> Accounts - Enter New Password</h1>
            <div>
                <c:choose>
                    <c:when test="${requestScope.tokenVerified}">
                        <form name="resetperform" action="perform" method="POST" accept-charset="UTF-8" role="form">
                            <fieldset>
                                <div class="field-container">
                                    <div class="label-container">
                                        <label for="reset-perform-email">Email Address</label>
                                    </div>
                                    <div class="input-container">
                                        <input id="reset-perform-email" class="form-control" name="email" type="text" readonly value="<c:out value='${targetEmail}'/>">
                                    </div>
                                </div>
                                <div class="field-container">
                                    <div class="label-container">
                                        <label for="reset-perform-newpassword">New Password</label>
                                    </div>
                                    <div class="input-container">
                                        <input id="reset-perform-newpassword" class="form-control" name="new_shibboleth" placeholder="new password" type="password" value="">
                                    </div>
                                </div>
                            </fieldset>
                            <input class="form-control" name="sptoken" type="hidden" value="${param.sptoken}">
                            <input type="submit" value="Reset Password">
                        </form>
                        <div class="result">
                            <span id="perform-reset-result"></span>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="token-verified-false">
                            <div>
                                The password reset token is expired, invalid, or not present.
                            </div>
                            <div>
                                <label>Token:
                                    <input type="text" readonly value="${param.sptoken}">
                                </label>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
        <script src="/files/main.js" type="text/javascript"></script>
        <script>
$(document).ready(function(){
    var form = $('form[name="resetperform"]');
    var resultSpan = $('#perform-reset-result');
    var responseMessageCreator = function(data) {
        return 'Password has been reset for ' + data.email;
    };
    new FormResponseHandler(form, resultSpan, 'perform/send', responseMessageCreator).activate();
});
        </script>
    </body>
</html>
