<%@ page import="com.easyinsight.users.UserService" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    SecurityUtil.populateThreadLocalFromSession(request);
    try {
        new UserService().resendActivationEmail();
        response.sendRedirect("reactivate.jsp?result=emailed");
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>