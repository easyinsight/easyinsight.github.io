<!DOCTYPE html>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="java.util.*" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.users.AccountTypeChange" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="org.joda.time.Days" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Account Management</title>
    <script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="/js/jquery-ui-1.8.20.custom.min.js"></script>
    <link href="/css/bootstrap.css" rel="stylesheet">
    <link href="/css/smoothness/jquery-ui-1.8.20.custom.css" rel="stylesheet">
    <link href='https://fonts.googleapis.com/css?family=PT+Sans' rel='stylesheet' type='text/css'/>
    <link href="/css/bootstrap-responsive.css" rel="stylesheet">
    <script type="text/javascript" src="/js/bootstrap.js"></script>
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
            response.sendRedirect("access.jsp");
            return;
        }


        AccountTypeChange accountTypeChange = (AccountTypeChange) session.getAttribute("accountTypeChange");

        double cost;
        double credit;
        double proratedCost;
        double priorCost;

        Session hibernateSession = Database.instance().createSession();
        Account account = null;
        try {
            account = (Account) hibernateSession.createQuery("from Account where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list().get(0);
            priorCost = account.createTotalCost();
            Calendar cal = Calendar.getInstance();
            Integer billingMonthOfYear = account.getBillingMonthOfYear();
            if (accountTypeChange.isYearly() && billingMonthOfYear == null) {
                billingMonthOfYear = cal.get(Calendar.MONTH);
            } else if (!accountTypeChange.isYearly() && billingMonthOfYear != null) {
                billingMonthOfYear = null;
            }
            cal.set(Calendar.DAY_OF_MONTH, account.getBillingDayOfMonth());
            if (billingMonthOfYear != null) {
                cal.set(Calendar.MONTH, billingMonthOfYear);
            }

            if (cal.getTime().before(new Date())) {
                if (accountTypeChange.isYearly()) {
                    cal.add(Calendar.YEAR, 1);
                } else {
                    cal.add(Calendar.MONTH, 1);
                }
            }

            Date date = cal.getTime();
            DateTime lastTime = new DateTime(date);
            DateTime now = new DateTime(System.currentTimeMillis());
            int daysBetween = Days.daysBetween(now, lastTime).getDays();

            // how many days until the next billing cycle?

            cost = Account.createTotalCost(account.getPricingModel(), accountTypeChange.getAccountType(), 0, 0, 0, accountTypeChange.isYearly());
            credit = Account.calculateCredit(account);

            proratedCost = cost * ((double) daysBetween / (double) ((accountTypeChange.isYearly() ? 365 : 31)));
        } finally {
            hibernateSession.close();
        }
        String message;
        String confirmMessage;
        String gradeMessage;
        if (accountTypeChange.getAccountType() > account.getAccountType()) {
            gradeMessage = "upgrade";
        } else if (accountTypeChange.getAccountType() < account.getAccountType()) {
            gradeMessage = "downgrade";
        } else {
            gradeMessage = "change";
        }
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

        String accountTypeString;
        if (accountTypeChange.getAccountType() == Account.BASIC) {
            accountTypeString = "Basic";
        } else if (accountTypeChange.getAccountType() == Account.PLUS) {
            accountTypeString = "Plus";
        } else if (accountTypeChange.getAccountType() == Account.PROFESSIONAL) {
            accountTypeString = "Professional";
        } else {
            throw new RuntimeException("Unknown type " + accountTypeChange.getAccountType());
        }

        if (account.getPricingModel() > 0) {
            confirmMessage = "You are about to " + gradeMessage + " to a " + accountTypeString + " account with " + accountTypeChange.getDesigners() + " Designers and " +
                    accountTypeChange.storageString() + " in storage.";
        } else {
            confirmMessage = "You are about to " + gradeMessage + " to a " + accountTypeString + " account with " +
                    accountTypeChange.storageString() + " in storage.";
        }

        double amountToBill;
        if (priorCost > cost) {
            amountToBill = 0;
        } else {
            amountToBill = Math.max(cost - credit, 0);
        }
        if (credit == 0) {
            message = "You'll be billed <b>" + currencyFormat.format(amountToBill) + "</b> for this change.";
        } else if (credit >= cost) {
            message = "You won't be billed for this change because of existing payments made on your account.";
        } else {
            message = "You'll be billed <b>" + currencyFormat.format(amountToBill) + "</b> for this change.";
        }
        double calcCOst = Account.createBaseCost(account.getPricingModel(), accountTypeChange.getAccountType(),
                                    0, 0, 0, accountTypeChange.isYearly());



        NumberFormat cf = NumberFormat.getCurrencyInstance();
        String blah = cf.format(Account.createDiscount(cost, accountTypeChange.isYearly()));
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
        <div class="col-md-12">
            <div class="row well">
                <div class="col-md-4 col-md-offset-4 well" style="background-color: #FFFFFF; border-color: #990000">
                    <div>
                        <span style="font-size: 14px"><%= confirmMessage %></span>
                        <p style="font-size: 14px"><%= message %></p>
                    </div>
                    <%
                        if (account.getPricingModel() != 0) {
                    %>
                    <div style="width:300px; font-size: 16px">
                        <div style="float:right">
                            <strong><%= accountTypeChange.getDesigners() %></strong>
                        </div>
                        <span><strong>Max Designers:</strong></span>
                    </div>
                    <%
                        }
                    %>
                    <div style="width:300px; font-size: 16px">
                        <div style="float:right">
                            <strong><%= accountTypeChange.storageString() %></strong>
                        </div>
                        <span><strong>Max Storage:</strong></span>
                    </div>
                    <br>
                    <div style="width:300px; font-size: 16px">
                        <div style="float:right">
                            <strong><%= cf.format(Account.createBaseCost(account.getPricingModel(), accountTypeChange.getAccountType(),
                                    0, 0, 0, accountTypeChange.isYearly())) %></strong>
                        </div>
                        <span><strong>Base Price:</strong></span>
                    </div>
                    <div style="width:300px;font-size: 16px">
                        <div style="float:right">
                            <strong><%= blah %></strong>
                        </div>
                        <span><strong>Discount:</strong></span>
                    </div>
                    <hr>
                    <div style="width:300px; font-size: 16px">
                        <div style="float:right">
                            <strong><%= cf.format(Account.createTotalCost(account.getPricingModel(), accountTypeChange.getAccountType(),
                                    0, 0, 0, accountTypeChange.isYearly())) %></strong>
                        </div>
                        <span><strong>Total:</strong></span>
                    </div>
                    <div style="width:300px;font-size: 16px">
                        <div style="float:right">
                            <strong><%= cf.format(credit) %></strong>
                        </div>
                        <span><strong>Existing Credit:</strong></span>
                    </div>
                    <hr>
                    <div style="width:300px;font-size: 16px">
                        <div style="float:right">
                            <strong><%= cf.format(amountToBill) %></strong>
                        </div>
                        <span><strong>Amount To Bill Now:</strong></span>
                    </div>
                    <div style="width:300px;font-size: 16px">
                        <div style="float:right">
                            <strong><%= cf.format(Account.createTotalCost(account.getPricingModel(), accountTypeChange.getAccountType(),
                                    0, 0, 0, accountTypeChange.isYearly())) %></strong>
                        </div>
                        <span><strong>Amount <%=accountTypeChange.isYearly() ? "Billed Yearly" : "Billed Monthly"%> </strong></span>
                    </div>
                    <br>
                    <form method="post" action="confirmAccountAction.jsp">
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
