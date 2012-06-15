<%@ page import="com.easyinsight.users.UserService" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    new UserService().resendActivationEmail();
%>