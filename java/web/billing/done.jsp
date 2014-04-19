<!DOCTYPE html>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.html.RedirectUtil" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Account Management</title>
    <jsp:include page="../html/bootstrapHeader.jsp"/>
</head>
<%


%>
<body>
<%
    String userName = (String) session.getAttribute("userName");
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {
        if (!SecurityUtil.isAccountAdmin()) {
            response.sendRedirect("access.jsp");
            return;
        }
        if ((SecurityUtil.getAccountTier() == Account.PREMIUM || SecurityUtil.getAccountTier() == Account.ENTERPRISE)) {
            response.sendRedirect(RedirectUtil.getURL(request, "/app/html/enterprise.jsp"));
            return;
        }

        Session hibernateSession = Database.instance().createSession();
        Account account = null;
        try {
            account = (Account) hibernateSession.createQuery("from Account where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list().get(0);
        } finally {
            hibernateSession.close();
        }

%>
<jsp:include page="../header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.ACCOUNT %>"/>
</jsp:include>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div style="width:100%;text-align: center">
                <img src="/images/logo2.PNG" alt="Easy Insight Logo"/>
            </div>
        </div>
        <div class="col-md-12 well">
            <div class="row">
                <div class="col-md-8 col-md-offset-2 well" style="background-color: #FFFFFF">
                    <div style="float:left;height:90px;padding-top:30px;padding-right:60px"><h4>Your account
                        has been changed to:</h4></div>
                    <div style="height:90px">
                        <p><%= account.planName() %>
                        </p>

                        <p><%= account.designers() %>
                        </p>

                        <p><%= account.billingInterval() %>
                        </p>

                        <p><%= account.storageString() %>
                        </p>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6 col-md-offset-3 well" style="background-color: #FFFFFF">
                    <a href="<%= RedirectUtil.getURL(request, "/app") %>" class="btn">Back to Application</a>
                </div>
            </div>
        </div>
    </div>
</div>
</div>

<%
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>
</body>
</html>
