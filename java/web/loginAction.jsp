<%@ page import="com.easyinsight.users.InternalUserService" %>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.users.UserService" %>
<%@ page import="com.easyinsight.users.UserServiceResponse" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String userName = request.getParameter("userName");
    String password = request.getParameter("password");



    UserServiceResponse userServiceResponse = new UserService().authenticate(userName, password, false);
    if (!userServiceResponse.isSuccessful()) {
        response.sendRedirect("login.jsp?error=true");
    } else {
        SecurityUtil.populateSession(session, userServiceResponse);
        if (userServiceResponse.getAccountState() == Account.CLOSED) {
            response.sendRedirect(RedirectUtil.getURL(request,"/app/billing/index.jsp"));
        } else if (userServiceResponse.getAccountState() == Account.DELINQUENT) {
            response.sendRedirect(RedirectUtil.getURL(request,"/app/billing/index.jsp"));
        } else if (userServiceResponse.getAccountState() == Account.BILLING_FAILED) {
            response.sendRedirect(RedirectUtil.getURL(request,"/app/billing/index.jsp"));
        } else if (userServiceResponse.getAccountState() == Account.INACTIVE) {
            response.sendRedirect(RedirectUtil.getURL(request, "/app/activation/reactivate.jsp"));
        } else {
            String urlHash = request.getParameter("urlhash");
            String rememberMe = request.getParameter("rememberMeCheckbox");
            if ("on".equals(rememberMe)) {
                Cookie userNameCookie = new Cookie("eiUserName", userName);
                userNameCookie.setSecure(true);
                userNameCookie.setMaxAge(60 * 60 * 24 * 30);
                response.addCookie(userNameCookie);
                Cookie tokenCookie = new Cookie("eiRememberMe", new InternalUserService().createCookie(userServiceResponse.getUserID()));
                tokenCookie.setSecure(true);
                tokenCookie.setMaxAge(60 * 60 * 24 * 30);
                response.addCookie(tokenCookie);
            }
            /*if (userServiceResponse.isFirstLogin()) {
                response.sendRedirect(RedirectUtil.getURL(request, "/app/user/initialUserSetup.jsp"));
            } else {*/
                String redirectUrl = RedirectUtil.getURL(request, "/app/");
                if(session.getAttribute("loginRedirect") != null) {
                   redirectUrl = ((String) session.getAttribute("loginRedirect"));
                   session.removeAttribute("loginRedirect");
                }
                if(urlHash != null)
                   redirectUrl = redirectUrl + urlHash;
                response.sendRedirect(redirectUrl);
            //}
        }
    }
%>