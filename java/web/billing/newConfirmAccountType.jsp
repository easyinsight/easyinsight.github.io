<!DOCTYPE html>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="com.easyinsight.users.NewModelAccountTypeChange" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Account Management</title>
    <jsp:include page="../html/bootstrapHeader.jsp" />
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


        NewModelAccountTypeChange accountTypeChange = (NewModelAccountTypeChange) session.getAttribute("accountTypeChange");

        Session hibernateSession = Database.instance().createSession();
        Account account = null;
        try {
            account = (Account) hibernateSession.createQuery("from Account where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list().get(0);

        } finally {
            hibernateSession.close();
        }

        NumberFormat cf = NumberFormat.getCurrencyInstance();
%>
<jsp:include page="../header.jsp">
    <jsp:param name="userName" value="<%= userName %>"/>
    <jsp:param name="headerActive" value="<%= HtmlConstants.ACCOUNT %>"/>
</jsp:include>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div style="width:100%;text-align: center">
                <img src="/images/logo2.png" alt="Easy Insight Logo"/>
            </div>
        </div>
        <div class="col-md-12">
            <div class="row well">
                <div class="col-md-4 col-md-offset-4 well" style="background-color: #FFFFFF; border-color: #990000">
                    <%--<div>
                        <span style="font-size: 14px"><%= confirmMessage %></span>
                        <p style="font-size: 14px"><%= message %></p>
                    </div>--%>

                    <div style="width:300px; font-size: 16px">
                        <div style="float:right">
                            <strong><%= accountTypeChange.getAddonDesigners() %></strong>
                        </div>
                        <span><strong>Additional Designers:</strong></span>
                    </div>

                    <div style="width:300px; font-size: 16px">
                        <div style="float:right">
                            <strong><%= accountTypeChange.getAddonConnections() %></strong>
                        </div>
                        <span><strong>Additional Connections:</strong></span>
                    </div>

                    <br>
                    <div style="width:300px; font-size: 16px">
                        <div style="float:right">
                            <strong><%= cf.format(Account.createBaseCost(1, 0, accountTypeChange.getAddonDesigners(), accountTypeChange.getAddonConnections(),
                                    accountTypeChange.getAddonStorage(), accountTypeChange.isYearly()) + account.getEnterpriseAddonCost()) %></strong>
                        </div>
                        <span><strong>Base Price:</strong></span>
                    </div>
                    <div style="width:300px;font-size: 16px">
                        <div style="float:right">
                            <strong><%= cf.format(Account.createDiscount(Account.createBaseCost(1, 0, accountTypeChange.getAddonDesigners(), accountTypeChange.getAddonConnections(),
                                    accountTypeChange.getAddonStorage(), accountTypeChange.isYearly()), accountTypeChange.isYearly())) %></strong>
                        </div>
                        <span><strong>Discount:</strong></span>
                    </div>
                    <hr>
                    <div style="width:300px; font-size: 16px">
                        <div style="float:right">
                            <strong><%= cf.format(Account.createTotalCost(1, 0, accountTypeChange.getAddonDesigners(), accountTypeChange.getAddonConnections(),
                                    accountTypeChange.getAddonStorage(), accountTypeChange.isYearly()) + account.getEnterpriseAddonCost()) %></strong>
                        </div>
                        <span><strong>Total:</strong></span>
                    </div>
                    <hr>
                    <br>
                    <form method="post" action="newConfirmAccountAction.jsp">
                        <button class="btn btn-success" type="submit">Confirm</button>
                    </form>
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
