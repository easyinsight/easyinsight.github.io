<%@ page import="com.easyinsight.users.UserService" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %><%
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {
        new UserService().switchHtmlFlex(false);
        response.sendRedirect(RedirectUtil.getURL(request, "/app"));
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>