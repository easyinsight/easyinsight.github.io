<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.users.UserService" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %><%
//    SecurityUtil.populateThreadLocalFromSession(request);
    try {
        String error = new UserService().reactivate(request.getParameter("key"), request.getParameter("password"), request);
        if("".equals(error))
            response.sendRedirect(RedirectUtil.getURL(request, "/app"));
        else
            response.sendRedirect(RedirectUtil.getURL(request, "/app/reactivate/index.jsp?error=" + error));
    } finally {
//        SecurityUtil.clearThreadLocal();
    }
%>