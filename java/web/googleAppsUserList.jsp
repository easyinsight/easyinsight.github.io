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
<%@ page import="com.easyinsight.users.User" %>
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
            text-align: center;
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

                <div style="width:100%;text-align: center">
                    <img src="/images/logo2.PNG" alt="Easy Insight Logo"/>
                </div>
                <div style="width:100%;text-align: center">
                    <h1 style="padding-top:20px">Import Users</h1>
                </div>

                <p style="font-size: 20px; line-height:24px;padding-top: 20px;padding-bottom:20px">
                    Which users do you want to import into Easy Insight from your Google Apps account?
                </p>
                <form class="well" style="background-color: #FAFAFA">
                <%
                    Session hibernateSession = Database.instance().createSession();
                    Transaction t = hibernateSession.beginTransaction();
                    String secretToken = null;
                    String token = null;
                    String domain = null;
                    String email = null;
                    try {
                        long accountId = (Long) session.getAttribute("accountID");
                        Account account = (Account) hibernateSession.get(Account.class, accountId);
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

                    UserService us = new UserService("Easy Insight");
                    GoogleOAuthParameters params = new GoogleOAuthParameters();
                    params.setOAuthConsumerKey("119099431019.apps.googleusercontent.com");
                    params.setOAuthConsumerSecret("UuuYup6nE4M2PjnOv_jEg8Ki");
                    us.setOAuthCredentials(params, new OAuthHmacSha1Signer());

                    UserFeed feed = us.getFeed(new URL("https://apps-apis.google.com/a/feeds/" + domain + "/user/2.0?xoauth_requestor_id=" + email), UserFeed.class);
                    for (UserEntry user : feed.getEntries()) {
                        if (!email.equals(user.getLogin().getUserName() + "@" + domain)) {
                %>

                <p style="font-size:20px"><input type="checkbox"
                          name="user_<%= user.getLogin().getUserName() %>"/> <%= user.getLogin().getUserName() %>@<%= domain %>
                </p>
                <% }
                }
                %>
                </form>
                <div class="row" style="padding-top:20px">
                    <div class="span4" style="padding-top: 4px">
                        <a style="font-size:20px" href="<%= (String) session.getAttribute("googleCallbackURL") %>">I'll Do It Later</a>
                    </div>
                    <div class="span3">
                        <div style="float:right">
                            <input type="submit" style="font-size:20px" class="btn btn-primary" value="Add Selected Contacts"/>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>