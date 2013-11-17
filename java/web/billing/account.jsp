<!DOCTYPE html>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page import="com.easyinsight.users.UserAccountAdminService" %>
<%@ page import="com.easyinsight.users.AccountStats" %>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.StatelessSession" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Account</title>
    <jsp:include page="../html/bootstrapHeader.jsp"/>
</head>
<body>
<%
    String userName = (String) session.getAttribute("userName");
    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {
        AccountStats accountStats = new UserAccountAdminService().getAccountStats();
        StatelessSession hibernateSession = Database.instance().createStatelessSession();
        Account account;
        try {
            account = (Account) hibernateSession.createQuery("from Account where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list().get(0);
        } finally {
            hibernateSession.close();
        }

        if (!SecurityUtil.isAccountAdmin()) {
            response.sendRedirect("nonAdminProfile.jsp");
            return;
        }
%>
<jsp:include page="../header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.ACCOUNT %>"/>
</jsp:include>
<div class="container">
    <div class="row">
        <div class="col-md12">
            <div class="btn-toolbar pull-right topControlToolbar">
                <div class="btn-group topControlBtnGroup">
                    <a href="#">Account Administration</a>
                </div>
                <div class="btn-group topControlBtnGroup">
                    <a href="../html/users.jsp">Users</a>
                </div>
                <div class="btn-group topControlBtnGroup">
                    <a href="../html/profile.jsp">My Profile</a>
                </div>
                <%--<div class="navbar-inner">
                    <ul class="nav">

                        <li><a href="#"></a></li>
                        <li><a href="../billing/accountType.jsp">Account Upgrade/Downgrade</a></li>
                        <li><a href="../billing/index.jsp">Billing</a></li>
                    </ul>
                </div>--%>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md12">
            <div class="container corePageWell">
                <div class="row">
                    <div class="col-md-offset-1 col-md10">
                        <h2>Your Easy Insight account (<%= account.getName() %>)</h2>
                    </div>
                </div>

                <% if (account.getPricingModel() == 0) { %>
                <div class="row">
                    <div class="col-md-offset-1 col-md10">
                        You have a <strong><%= account.planName() %></strong> account. You're currently using <strong><%= accountStats.getUsedSpaceString() %></strong> of <strong><%= accountStats.getMaxSpaceString() %></strong> storage and <strong><%=accountStats.getCurrentUsers()%></strong> of <strong><%=accountStats.getAvailableUsers()%></strong> users.
                    </div>
                </div>
                <% } else { %>
                <div class="row">
                    <div class="col-md-offset-1 col-md10">
                        You have <strong><%= accountStats.getUsedDesigners() %></strong> of <strong><%= accountStats.getCoreDesigners() + accountStats.getAddonDesigners()%></strong>, <strong><%= accountStats.getCurrentSmallBizConnections()%></strong> of <strong><%= accountStats.getAddonSmallBizConnections() + accountStats.getCoreSmallBizConnections() %></strong> small business connections, and <strong><%= accountStats.getUsedSpaceString()%></strong> of <strong><%= accountStats.getMaxSpaceString()%></strong> custom data storage used.
                    </div>
                </div>
                <% } %>
                <div class="row">
                    <div class="col-md-offset-1 col-md10">
                        <a href="accountType.jsp">Upgrade / Downgrade Account</a>
                    </div>
                </div>
                <hr/>
                <div class="row">
                    <div class="col-md-offset-1 col-md10">
                        <h2>Billing and Invoices</h2>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-offset-1 col-md10">
                        <a href="index.jsp">Configure Billing</a>
                    </div>
                </div>
                <%--<div class="row">
                    <div class="col-md-offset-1 col-md10">
                        <a href="../billing/index.jsp">View Invoices</a>
                    </div>
                </div>--%>
                <%--<hr/>
                <div class="row">
                    <div class="col-md-offset-1 col-md10">
                        <h2>Don't need to use Easy Insight right now?</h2>
                    </div>
                </div>--%>
            </div>
        </div>

    </div>
</div>
</body>
<%
    } finally {
        SecurityUtil.clearThreadLocal();
    }
%>
</html>