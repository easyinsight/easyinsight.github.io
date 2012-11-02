<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Transaction" %>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.google.gdata.client.appsforyourdomain.UserService" %>
<%@ page import="com.google.gdata.client.authn.oauth.GoogleOAuthParameters" %>
<%@ page import="com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer" %>
<%@ page import="com.google.gdata.data.appsforyourdomain.provisioning.UserFeed" %>
<%@ page import="java.net.URL" %>
<%@ page import="com.google.gdata.data.appsforyourdomain.provisioning.UserEntry" %>

<!DOCTYPE html>
<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Sign In</title>
    <script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
    <link href="/css/bootstrap.min.css" rel="stylesheet">

    <style type="text/css">
        body {
            padding-top: 45px;
            padding-bottom: 40px;
        }

        .center_stuff {
            text-align:center;
        }
    </style>
    <link href="/css/bootstrap-responsive.min.css" rel="stylesheet">
    <script type="text/javascript" src="/js/bootstrap.min.js"></script>
</head>
<%


%>
<body>
<div class="container">
    <div class="row">

        <div class="span8 offset2">

            <form class="well" method="post" action="/app/google/addUsers.jsp" id="loginForm">

                <%
                    Session hibernateSession = Database.instance().createSession();
                    Transaction t = hibernateSession.beginTransaction();
                    String secretToken = null;
                    String token = null;
                    String domain = null;
                    try {
                        long accountId = (Long) session.getAttribute("accountID");
                        Account account = (Account) hibernateSession.get(Account.class, accountId);
                        secretToken = account.getGoogleSecretToken();
                        token = account.getGoogleToken();
                        domain = account.getGoogleDomainName();
                        t.commit();
                    } catch (Exception e) {
                        t.rollback();
                        throw new RuntimeException(e);
                    } finally {
                        hibernateSession.close();
                    }

                    UserService us = new UserService("Easy Insight");
                    GoogleOAuthParameters params = new GoogleOAuthParameters();
                    params.setOAuthConsumerKey("119099431019.apps.googleusercontent.com");
                    params.setOAuthConsumerSecret("UuuYup6nE4M2PjnOv_jEg8Ki");
                    params.setOAuthToken(token);
                    params.setOAuthTokenSecret(secretToken);
                    us.setOAuthCredentials(params, new OAuthHmacSha1Signer());
                    UserFeed feed = us.getFeed(new URL("https://apps-apis.google.com/a/feeds/" + domain + "/user/2.0"), UserFeed.class);
                    for(UserEntry user : feed.getEntries()) {
                        %>
                        <p><input type="checkbox" name="user_<%= user.getLogin().getUserName() %>" /> <%= user.getLogin().getUserName() %>@<%= domain %></p>
                    <%}
                %>
                <input type="submit" />
            </form>
        </div>
    </div>
</div>
</body>
</html>