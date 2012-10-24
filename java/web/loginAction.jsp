<%@ page import="com.easyinsight.users.InternalUserService" %>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.users.UserService" %>
<%@ page import="com.easyinsight.users.UserServiceResponse" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String userName = request.getParameter("userName");
    String password = request.getParameter("password");



    UserServiceResponse userServiceResponse = new UserService().authenticate(userName, password, false);
    String ipAddress  = request.getHeader("X-FORWARDED-FOR");
    if(ipAddress == null) {
        ipAddress = request.getRemoteAddr();
    }

    new UserService().logAuthentication(userName, userServiceResponse.getUserID(), userServiceResponse.isSuccessful(), ipAddress, request.getHeader("User-Agent"));

    if (!userServiceResponse.isSuccessful()) {
        response.sendRedirect("login.jsp?error=true");
    } else {

        session.invalidate();
        session = request.getSession(true);
        SecurityUtil.populateSession(session, userServiceResponse);

        UserService.checkAccountStateOnLogin(session, userServiceResponse, request, response);
    }
%><%!
%>