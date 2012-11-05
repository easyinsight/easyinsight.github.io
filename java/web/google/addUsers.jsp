<%@ page import="java.util.Map" %>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="org.hibernate.Transaction" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.google.gdata.data.appsforyourdomain.provisioning.UserEntry" %>
<%@ page import="com.google.gdata.data.appsforyourdomain.provisioning.UserFeed" %>
<%@ page import="com.google.gdata.client.authn.oauth.GoogleOAuthParameters" %>
<%@ page import="com.google.gdata.client.appsforyourdomain.UserService" %>
<%@ page import="com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer" %>
<%@ page import="java.net.URL" %>
<%@ page import="com.easyinsight.users.UserAccountAdminService" %>
<%@ page import="com.easyinsight.users.UserTransferObject" %>
<%@ page import="com.easyinsight.preferences.UserDLS" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.easyinsight.users.User" %>
<%
    try {
        SecurityUtil.populateThreadLocalFromSession(request);
        Session hibernateSession = Database.instance().createSession();
        Transaction t = hibernateSession.beginTransaction();

        String domain = null;
        String secretToken = null;
        String token = null;
        String email = null;
        try {
            long accountId = (Long) session.getAttribute("accountID");
            Account account = (Account) hibernateSession.get(Account.class, accountId);
            secretToken = account.getGoogleSecretToken();
            token = account.getGoogleToken();
            domain = account.getGoogleDomainName();
            long userId = (Long) session.getAttribute("userID");
            User user = (User) hibernateSession.get(User.class, userId);
            email = user.getEmail();
            t.commit();
        } catch (Exception e) {
            t.rollback();
            throw new RuntimeException(e);
        } finally {
            hibernateSession.close();
        }


        Map<String, Object> p = request.getParameterMap();

        UserService us = new UserService("Easy Insight");
        GoogleOAuthParameters params = new GoogleOAuthParameters();
        params.setOAuthConsumerKey("119099431019.apps.googleusercontent.com");
        params.setOAuthConsumerSecret("UuuYup6nE4M2PjnOv_jEg8Ki");

        us.setOAuthCredentials(params, new OAuthHmacSha1Signer());
        UserFeed feed = us.getFeed(new URL("https://apps-apis.google.com/a/feeds/" + domain + "/user/2.0?xoauth_requestor_id=" + email), UserFeed.class);
        UserAccountAdminService uaas = new UserAccountAdminService();
        List<UserDLS> dlsList = new ArrayList<UserDLS>();
        for (UserEntry user : feed.getEntries()) {
            for (String s : p.keySet()) {
                if (s.startsWith("user_") && "on".equals(request.getParameter(s))) {
                    String username = s.substring(5, s.length());
                    if (username.equals(user.getLogin().getUserName())) {
                        String eiUsername = username + "@" + domain;
                        uaas.addUserToAccount(new UserTransferObject(eiUsername, 0, eiUsername, user.getName().getFamilyName(), user.getName().getGivenName()), dlsList, true);
                    }
                }
            }
        }
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>