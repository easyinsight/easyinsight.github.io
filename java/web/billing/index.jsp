<!DOCTYPE html>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.billing.BillingUtil" %>
<%@ page import="com.easyinsight.config.ConfigLoader" %>
<%@ page import="java.util.*" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.users.AccountTypeChange" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Easy Insight Billing Configuration</title>
    <script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="/js/jquery-ui-1.8.20.custom.min.js"></script>
    <link href="/css/bootstrap.css" rel="stylesheet">
    <link href="/css/smoothness/jquery-ui-1.8.20.custom.css" rel="stylesheet">

    <script language="javascript" type="text/javascript">
        function setCCexp() {
            document.getElementById("ccexp").value = document.getElementById("ccexpMonth").value + document.getElementById("ccexpYear").value;
        }

        function changeBilling() {
            for(i = 0;i < document.forms[0].billingType.length;i++) {
                if(document.forms[0].billingType[i].checked)
                    window.location.search = "billingType=" + document.forms[0].billingType[i].value;
            }
        }

    </script>

    <style type="text/css">
        body {
            padding-top: 45px;
            padding-bottom: 40px;
        }
    </style>
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
        if((SecurityUtil.getAccountTier() == Account.PREMIUM || SecurityUtil.getAccountTier() == Account.ENTERPRISE)) {
            response.sendRedirect("access.jsp");
            return;
        }

        Session hibernateSession = Database.instance().createSession();
        double cost = 0;
        double credit = 0;
        Account account = null;
        AccountTypeChange accountTypeChange = (AccountTypeChange) session.getAttribute("accountTypeChange");
        try {
            account = (Account) hibernateSession.createQuery("from Account where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list().get(0);
            if (accountTypeChange != null) {
                cost = Account.createTotalCost(account.getPricingModel(), accountTypeChange.getAccountType(), accountTypeChange.getDesigners(),
                        accountTypeChange.getStorage(), accountTypeChange.isYearly());
                credit = Account.calculateCredit(account);
            } else {
                cost = account.createTotalCost();
            }
        } finally {
            hibernateSession.close();
        }

        String keyID = BillingUtil.getKeyID();
        String key = BillingUtil.getKey();
        boolean monthly = account.getBillingMonthOfYear() == null && (request.getParameter("billingType") == null || !request.getParameter("billingType").equals("yearly"));
        String orderID = UUID.randomUUID().toString();

        String amount = "1.00";
        String type = "auth";

        boolean chargeNow = false;

        if (accountTypeChange != null) {
            type = "sale";
            double toCharge = cost - credit;
            amount = String.valueOf(toCharge);
            chargeNow = true;
        }  else if(account.getAccountState() == Account.DELINQUENT || account.getAccountState() == Account.BILLING_FAILED || account.getAccountState() == Account.CLOSED) {
            amount = String.valueOf(cost);
            type = "sale";
            chargeNow = true;
        }

        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date d = new Date();
        String time = df.format(d);
        request.getSession().setAttribute("billingTime", time);
        String hashString = orderID + "|" + amount + "|" + String.valueOf(account.getAccountID()) + "|" + time + "|" + key;
        String hash = BillingUtil.MD5Hash(hashString);
        String accountInfoString;
        String charge;
        int accountType;
        if (accountTypeChange == null) {
            accountType = account.getAccountType();
        } else {
            accountType = accountTypeChange.getAccountType();
        }
        switch(accountType) {
            case Account.BASIC:
                accountInfoString = "Basic";
                break;
            case Account.PLUS:
                accountInfoString = "Plus";
                break;
            case Account.PROFESSIONAL:
                accountInfoString = "Professional";
                break;
            default:
                accountInfoString = "";
        }
        charge = NumberFormat.getCurrencyInstance().format(cost - credit);

        String billingMessage;
        String billingHeader;
        String billingIntroParagraph;
        if (accountTypeChange == null) {
            billingMessage = "You are signing up for the " + accountInfoString + " account tier, You will be charged " + charge + " USD " + (monthly ? "monthly" : "yearly") + " for your subscription.";
            billingHeader = account.billingHeader();
            billingIntroParagraph = account.billingIntroParagraph();
        } else {
            billingMessage = "You are signing up for the " + accountInfoString + " account tier. You will be charged " + charge + " USD now, and " +
                    NumberFormat.getCurrencyInstance().format(Account.createTotalCost(account.getPricingModel(), accountTypeChange.getAccountType(), accountTypeChange.getDesigners(), accountTypeChange.getStorage(),
                            accountTypeChange.isYearly())) + (!accountTypeChange.isYearly() ? " monthly" : " yearly") + " for your subscription.";
            billingHeader = "";
            billingIntroParagraph = "";
        }

        if (chargeNow) {
             billingMessage += " Your credit card will be charged upon submitting this form.";
        }
%>
<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container-fluid">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <%--<a class="brand" href="#"><img src="/images/logo3.jpg"/></a>--%>
            <div class="btn-group pull-right">
                <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                    <i class="icon-user"></i> <%= StringEscapeUtils.escapeHtml(userName) %>
                    <span class="caret"></span>
                </a>
                <ul class="dropdown-menu">
                    <%
                        if (account.getAccountState() == Account.TRIAL || account.getAccountState() == Account.ACTIVE) {
                    %>
                    <li><a href="../html/flashAppAction.jsp">Back to Full Interface</a></li>
                    <%
                        }
                    %>
                    <li><a href="/app/logoutAction.jsp">Sign Out</a></li>
                </ul>
            </div>
            <div class="nav-collapse">
                <ul class="nav">
                    <li><a href="accountType.jsp">Account Configuration</a></li>
                    <li class="active"><a href="#">Billing Setup</a></li>
                </ul>
            </div>
        </div>
    </div>
</div>
<div class="container">
    <div class="row">
        <div class="span12">
            <div style="width:100%;text-align: center">
                <img src="/images/logo2.PNG" alt="Easy Insight Logo"/>
            </div>
        </div>
        <div class="span6 offset3" style="padding-top: 10px;padding-bottom: 10px">
            <h3 style="width:100%;text-align: center"><%= billingHeader %></h3>
            <p style="font-size:14px;font-family: 'PT Sans',arial,serif"><%= billingIntroParagraph %></p>
        </div>
        <div class="row">
            <div class="span8 offset1">
                <% if(request.getParameter("error") != null) { %>
                <p><label class="error"><%
                    String errorCode = request.getParameter("response_code");
                    if ("200".equals(errorCode)) out.println("The transaction was declined by the credit card processor. If this error continues, contact support@easy-insight.com.");
                    else if ("204".equals(errorCode)) out.println("The transaction was not allowed by the credit card processor. Please contact support@easy-insight.com.");
                    else if ("220".equals(errorCode)) out.println("There was an error with your billing information. Please double check the payment information below. If this error continues, contact support@easy-insight.com.");
                    else if ("221".equals(errorCode)) out.println("The transaction was not allowed by the credit card processor. Please contact support@easy-insight.com.");
                    else if ("222".equals(errorCode)) out.println("The transaction was not allowed by the credit card processor. Please contact support@easy-insight.com.");
                    else if ("223".equals(errorCode)) out.println("The transaction was not allowed by the credit card processor. Please contact support@easy-insight.com.");
                    else if ("224".equals(errorCode)) out.println("The transaction was not allowed by the credit card processor. Please contact support@easy-insight.com.");
                    else if ("300".equals(errorCode)) {
                        out.println("There was an error with your billing information. Please input the correct information below.");
                    } else {
                        out.println("There was an error with your billing information. Please input the correct information below.");
                    }
                %></label></p>
                <% } %>
                <form method="post" action="https://secure.braintreepaymentgateway.com/api/transact.php" onsubmit="setCCexp()" class="well form-horizontal">
                    <fieldset>
                        <input id="ccexp" type="hidden" value="" name="ccexp"/>
                        <input id="customer_vault_id" type="hidden" value="<%= account.getAccountID() %>" name="customer_vault_id" />
                        <input id="customer_vault" type="hidden" value="<%= (account.isBillingInformationGiven() != null && account.isBillingInformationGiven()) ? "update_customer" : "add_customer" %>" name="customer_vault" />
                        <input id="redirect" type="hidden" value="<%= ConfigLoader.instance().getRedirectLocation() %>/app/billing/submit.jsp" name="redirect"/>
                        <input id="payment" type="hidden" value="creditcard" name="creditcard" />
                        <input id="key_id" type="hidden" value="<%= keyID %>" name="key_id"/>
                        <input id="orderid" type="hidden" value="<%= orderID %>" name="orderid"/>
                        <input id="amount" type="hidden" value="<%= amount %>" name="amount"/>
                        <input id="time" type="hidden" value="<%= time %>" name="time"/>
                        <input id="hash" type="hidden" value="<%= hash %>" name="hash"/>
                        <input id="type" type="hidden" value="<%= type %>" name="type" />

                        <div class="control-group">
                            <label class="control-label" for="firstname">First Name:</label>
                            <div class="controls">
                                <input id="firstname" type="text" value="" name="firstname" class="span3"/>
                            </div>

                        </div>
                        <div class="control-group">
                            <label class="control-label" for="lastname">Last Name:</label>
                            <div class="controls">
                                <input id="lastname" type="text" value="" name="lastname" class="span3"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label" for="zip">Billing Zip/Postal Code:</label>
                            <div class="controls">
                                <input id="zip" type="text" value="" name="zip" />
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label" for="ccnumber">Credit Card Number:</label>
                            <div class="controls">
                                <input id="ccnumber" type="text" style="width:16.5em" name="ccnumber"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label" for="cvv">CVV/CVC:</label>
                            <div class="controls">
                                <input id="cvv" type="text" value="" name="cvv" style="width:3.5em" />
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label" for="ccexpMonth">Expiration date:</label>
                            <div class="controls">
                                <select id="ccexpMonth">
                                    <option value="01">01 - January</option>
                                    <option value="02">02 - February</option>
                                    <option value="03">03 - March</option>
                                    <option value="04">04 - April</option>
                                    <option value="05">05 - May</option>
                                    <option value="06">06 - June</option>
                                    <option value="07">07 - July</option>
                                    <option value="08">08 - August</option>
                                    <option value="09">09 - September</option>
                                    <option value="10">10 - October</option>
                                    <option value="11">11 - November</option>
                                    <option value="12">12 - December</option>
                                </select> /
                                <select id="ccexpYear">
                                    <option value="10">10</option>
                                    <option value="11">11</option>
                                    <option value="12">12</option>
                                    <option value="13">13</option>
                                    <option value="14">14</option>
                                    <option value="15">15</option>
                                    <option value="16">16</option>
                                    <option value="17">17</option>
                                    <option value="18">18</option>
                                    <option value="19">19</option>
                                    <option value="20">20</option>
                                </select>
                            </div>
                        </div>
                        <p><%= billingMessage %></p>
                        <button class="btn btn-inverse" type="submit" value="" name="commit">Submit</button>
                    </fieldset>
                </form>
            </div>
            <div class="span3">
                <div class="well" style="background-color: #d5d5d5">
                    <p><strong>Have questions?</strong></p>
                    <p>You can contact Easy Insight at 1-720-285-8652 or sales@easy-insight.com if you have any questions or concerns around your account billing.</p>
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
