<%@ page import="com.easyinsight.users.UserService" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String email = request.getParameter("email");
    if (new UserService().remindPassword(email)) {
        response.sendRedirect("forgot.jsp?result=emailed");
    } else {
        response.sendRedirect("forgot.jsp?result=noEmail");
    }
%>