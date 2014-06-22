<%@ page import="com.easyinsight.users.UserService" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
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
            response.sendRedirect(RedirectUtil.getURL(request, "/app/user/initialUserSetup.jsp?error=true"));
        } else {
            request.getSession().removeAttribute("errorString");
            String validation = new UserService().updatePassword(password);
            if (validation != null) {
                request.getSession().setAttribute("errorString", validation);
                response.sendRedirect(RedirectUtil.getURL(request, "/app/user/initialUserSetup.jsp?error=true"));
            } else {
                request.getSession().removeAttribute("resetPassword");

                // is the account default to HTML?

                String redirectUrl;

                EIConnection conn = Database.instance().getConnection();
                try {
                    PreparedStatement ps = conn.prepareStatement("SELECT ACCOUNT.use_html_version FROM ACCOUNT WHERE ACCOUNT_ID = ?");
                    ps.setLong(1, SecurityUtil.getAccountID());
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    if (rs.getBoolean(1)) {
                        redirectUrl = RedirectUtil.getURL(request, "/app/html");
                    } else {
                        redirectUrl = RedirectUtil.getURL(request, "/app/");
                    }
                    ps.close();
                } finally {
                    Database.closeConnection(conn);
                }

                String urlHash = request.getParameter("urlhash");
                if(session.getAttribute("loginRedirect") != null) {
                    redirectUrl = ((String) session.getAttribute("loginRedirect"));
                    session.removeAttribute("loginRedirect");
                }
                if(urlHash != null)
                    redirectUrl = redirectUrl + urlHash;
                response.sendRedirect(redirectUrl);
            }
        }
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>