<%@ page import="com.easyinsight.users.UserService" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String ipAddress  = request.getHeader("X-FORWARDED-FOR");
    if(ipAddress == null) {
        ipAddress = request.getRemoteAddr();
    }
    try {
        com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    } catch(Exception e) {
        throw new RuntimeException("Problem on firstLoginAction page from " + ipAddress);
    }
    try {
        request.getSession().removeAttribute("errorString");
        String password = request.getParameter("password");
        String passwordConfirm = request.getParameter("confirmPassword");
        String errorString = null;
        if (password == null || "".equals(password.trim())) {
            errorString = "Please enter the new password.";
        } else if (passwordConfirm == null || "".equals(passwordConfirm.trim())) {
            errorString = "Please confirm the new password.";
        } else if (password.length() < 8) {
            errorString = "Your password must be at least eight characters.";
        } else if (password.length() > 20) {
            errorString = "Your password must be less than twenty characters.";
        } else if (!password.equals(passwordConfirm)) {
            errorString = "Your passwords did not match.";
        }
        if (errorString != null) {
            request.getSession().setAttribute("errorString", errorString);
            response.sendRedirect("initialUserSetup.jsp?error=true");
        } else {
            request.getSession().removeAttribute("errorString");
            new UserService().updatePassword(password);
            request.getSession().removeAttribute("resetPassword");
            String redirectUrl = RedirectUtil.getURL(request, "/app/");
            String urlHash = request.getParameter("urlhash");
            if(session.getAttribute("loginRedirect") != null) {
                redirectUrl = ((String) session.getAttribute("loginRedirect"));
                session.removeAttribute("loginRedirect");
            }
            if(urlHash != null)
                redirectUrl = redirectUrl + urlHash;
            response.sendRedirect(redirectUrl);
        }
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>