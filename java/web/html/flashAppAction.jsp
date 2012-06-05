<%@ page import="com.easyinsight.users.UserService" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %><%
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {
        new UserService().switchHtmlFlex(false);
        response.sendRedirect("/app");
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>