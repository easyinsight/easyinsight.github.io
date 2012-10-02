<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.users.UserService" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %><%
    SecurityUtil.populateThreadLocalFromSession(request);
    try {
        new UserService().reactivate();
        response.sendRedirect(RedirectUtil.getURL(request, "/app"));
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>