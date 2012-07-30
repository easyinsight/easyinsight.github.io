<%@ page import="com.easyinsight.users.UserService" %>
<%@ page import="java.util.regex.Pattern" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.regex.Matcher" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    request.getSession().removeAttribute("errorString");
    String userName = request.getParameter("userName");
    String password = request.getParameter("password");
    String passwordConfirm = request.getParameter("confirmPassword");
    String errorString = UserService.checkPassword(password);
    if(errorString != null) {
        // fall through
    } else if(passwordConfirm == null || "".equals(passwordConfirm.trim())) {
        errorString = "Please confirm the new password.";
    }
    else if (userName == null || "".equals(userName.trim())) {
        errorString = "Please specify your email address or user name.";
    }
    else if (!password.equals(passwordConfirm)) {
        errorString = "Your passwords did not match.";
    }
    if (errorString != null) {
        request.getSession().setAttribute("errorString", errorString);
        response.sendRedirect("passwordReset.jsp?error=true");
    } else {
        request.getSession().removeAttribute("errorString");
        boolean success = new UserService().resetPassword((String) session.getAttribute("resetPassword"), userName, password);
        if (success) {
            request.getSession().removeAttribute("resetPassword");
            response.sendRedirect("login.jsp?passwordReset=true");
        } else {
            request.getSession().setAttribute("errorString", "Something was wrong with your password reset. You may wish to generate another reset email. If you still have problems, please contact support@easy-insight.com.");
            response.sendRedirect("passwordReset.jsp?error=true");
        }
    }
%>