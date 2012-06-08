<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.logging.LogClass" %>
<%@ page import="com.easyinsight.users.InternalUserService" %>
<%@ page import="com.easyinsight.preferences.UISettingRetrieval" %>
<%@ page import="com.easyinsight.preferences.ApplicationSkinSettings" %>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String userName = request.getParameter("userName");
    String password = request.getParameter("password");
    EIConnection conn = Database.instance().getConnection();
    org.hibernate.Session hibernateSession = com.easyinsight.database.Database.instance().createSession(conn);
    try {
        conn.setAutoCommit(false);
        java.util.List results = hibernateSession.createQuery("from User where userName = ? or email = ?").setString(0, userName).setString(1, userName).list();
        if(results.size() != 1) {
            response.sendRedirect("login.jsp?error=true");

        } else {
            com.easyinsight.users.User user =(com.easyinsight.users.User) results.get(0);
            String encryptedPass = com.easyinsight.security.PasswordService.getInstance().encrypt(password, user.getHashSalt(), user.getHashType());
            if(!encryptedPass.equals(user.getPassword())) {
                response.sendRedirect("login.jsp?error=true");
            } else {
                com.easyinsight.users.Account account = user.getAccount();
                long accountID = account.getAccountID();
                session.setAttribute("accountID", accountID);
                session.setAttribute("accountAdmin", user.isAccountAdmin());
                session.setAttribute("userID", user.getUserID());
                session.setAttribute("accountType", account.getAccountType());
                session.setAttribute("userName", userName);
                session.setAttribute("dayOfWeek", account.getFirstDayOfWeek());
                session.setAttribute("uiSettings", ApplicationSkinSettings.retrieveSkin(user.getUserID(), hibernateSession, accountID));
                if (account.getAccountState() == Account.CLOSED) {
                    response.sendRedirect("/app/billing/billing.jsp");
                } else if (account.getAccountState() == Account.DELINQUENT) {
                    response.sendRedirect("/app/billing/billing.jsp");
                } else if (account.getAccountState() == Account.BILLING_FAILED) {
                    response.sendRedirect("/app/billing/billing.jsp");
                } else {


                    String urlHash = request.getParameter("urlhash");
                    String rememberMe = request.getParameter("rememberMeCheckbox");
                    if ("on".equals(rememberMe)) {
                        Cookie userNameCookie = new Cookie("eiUserName", userName);
                        userNameCookie.setSecure(true);
                        userNameCookie.setMaxAge(60 * 60 * 24 * 30);
                        response.addCookie(userNameCookie);
                        Cookie tokenCookie = new Cookie("eiRememberMe", new InternalUserService().createCookie(user.getUserID(), conn));
                        tokenCookie.setSecure(true);
                        tokenCookie.setMaxAge(60 * 60 * 24 * 30);
                        response.addCookie(tokenCookie);
                    }
                    if (urlHash == null) {
                        response.sendRedirect("/app/");
                    } else {
                        response.sendRedirect("/app/" + urlHash);
                    }
                }
            }
        }
        conn.commit();
        return;
    } catch (Exception e) {
        LogClass.error(e);
        conn.rollback();
        throw e;
    } finally {
        conn.setAutoCommit(true);
        hibernateSession.close();
        Database.closeConnection(conn);
    }
%>