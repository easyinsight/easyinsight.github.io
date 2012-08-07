<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%
    session.invalidate();
    Cookie userNameCookie = new Cookie("eiUserName", "");
    userNameCookie.setSecure(true);
    userNameCookie.setMaxAge(-1);
    response.addCookie(userNameCookie);
    Cookie tokenCookie = new Cookie("eiRememberMe", "");
    tokenCookie.setSecure(true);
    tokenCookie.setMaxAge(-1);
    response.addCookie(tokenCookie);
    response.sendRedirect(RedirectUtil.getURL(request, "/app/login.jsp"));
%>