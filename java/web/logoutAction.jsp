<%@ page import="com.easyinsight.html.RedirectUtil" %><%
    session.invalidate();
    response.sendRedirect(RedirectUtil.getURL(request, "login.jsp"));
%>