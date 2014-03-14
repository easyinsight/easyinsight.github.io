<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %><%
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {
        response.sendRedirect(RedirectUtil.getURL(request, "/app?full"));
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>