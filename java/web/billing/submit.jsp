<!DOCTYPE html>
<%@ page import="com.easyinsight.billing.BillingUtil" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.logging.LogClass" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.users.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String hashStr = request.getParameter("orderid") + "|" + request.getParameter("amount") + "|" + request.getParameter("response") + "|" + request.getParameter("transactionid") + "|" + request.getParameter("avsresponse") + "|" + request.getParameter("cvvresponse") + "|" + request.getParameter("customer_vault_id") + "|" + request.getParameter("time") + "|" + BillingUtil.getKey();
    String hashed = BillingUtil.MD5Hash(hashStr);

    EIConnection conn = Database.instance().getConnection();
    String postBillingMessage;
    conn.setAutoCommit(false);
    Session s = Database.instance().createSession(conn);
    try {
        long accountID = (Long) request.getSession().getAttribute("accountID");
        long userID = (Long) request.getSession().getAttribute("userID");
        System.out.println("UserID: " + userID + " AccountID: " + accountID);

        User user = (User) s.createQuery("from User where userID = ?").setLong(0, userID).list().get(0);
        Account account = (Account) s.createQuery("from Account where accountID = ?").setLong(0, accountID).list().get(0);

        AccountTypeChange accountTypeChange = (AccountTypeChange) session.getAttribute("accountTypeChange");

        boolean yearly;
        if (accountTypeChange == null) {
            yearly = account.getBillingMonthOfYear() != null;
        } else {
            yearly = accountTypeChange.isYearly();
        }

        //boolean yearly = request.getParameter("orderid").startsWith("yearly");

        postBillingMessage = account.successMessage();

        System.out.println("Creating account billing info...");
        AccountCreditCardBillingInfo info = new AccountCreditCardBillingInfo();
        info.setTransactionID(request.getParameter("transactionid"));
        info.setAmount(request.getParameter("amount"));
        info.setResponse(request.getParameter("response"));
        info.setResponseCode(request.getParameter("response_code"));
        info.setResponseString(request.getParameter("responsetext"));
        info.setAccountId(account.getAccountID());
        info.setDays(yearly ? 365 : 28);
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date transTime = df.parse(request.getParameter("time"));
        Calendar c = Calendar.getInstance();
        c.setTime(transTime);
        info.setTransactionTime(transTime);
        account.getBillingInfo().add(info);
        s.save(info);
        conn.commit();
        System.out.println("Saved account billing info.");

        if(!hashed.equals(request.getParameter("hash")) || !request.getParameter("response").equals("1")) {
            response.sendRedirect("index.jsp?error=true&responseCode=" + request.getParameter("response_code"));
        }
        else if (request.getParameter("response").equals("1")) {
            conn.setSavepoint();
            account.setBillingInformationGiven(true);




            // Can only let account admins use the billing
            if((account.getAccountType() == Account.PROFESSIONAL || account.getAccountType() == Account.PREMIUM || account.getAccountType() == Account.ENTERPRISE)
              && !user.isAccountAdmin())
                response.sendRedirect("access.jsp");

            // Set up billing day of month
            if(account.getAccountState() == Account.DELINQUENT || account.getAccountState() == Account.BILLING_FAILED) {
                account.setBillingDayOfMonth(c.get(Calendar.DAY_OF_MONTH));
                if(yearly)
                    account.setBillingMonthOfYear(c.get(Calendar.MONTH));
                else
                    account.setBillingMonthOfYear(null);
            } else {
                Date trialEnd = new AccountActivityStorage().getTrialTime(account.getAccountID(), conn);
                // billing day of month is at the end of the trial, if there is one
                if(trialEnd != null) {
                    System.out.println("Trial end date: " + trialEnd.toString());
                    if(trialEnd.after(new Date()) && account.getBillingDayOfMonth() == null) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(trialEnd);
                        System.out.println("Billing day of month: " + cal.get(Calendar.DAY_OF_MONTH));
                        account.setBillingDayOfMonth(cal.get(Calendar.DAY_OF_MONTH));
                        if(yearly)
                            account.setBillingMonthOfYear(cal.get(Calendar.MONTH));
                        else
                            account.setBillingMonthOfYear(null);
                    }
                    else {
                        Calendar cal = Calendar.getInstance();
                        account.setBillingDayOfMonth(cal.get(Calendar.DAY_OF_MONTH));
                        if(yearly)
                            account.setBillingMonthOfYear(cal.get(Calendar.MONTH));
                        else
                            account.setBillingMonthOfYear(null);
                    }
                }
            }

            // If we've gotten this far, they're updating their billing information instead of submitting it. Allow upgrades to yearly, but if they're already yearly don't update
            // billing_month_of_year
            if(yearly && account.getBillingMonthOfYear() == null) {
                Calendar cal = Calendar.getInstance();
                if(account.getBillingDayOfMonth() == null) {
                    account.setBillingDayOfMonth(cal.get(Calendar.DAY_OF_MONTH));
                }
                //  if the billing day of month is before the billing day of month, yearly billing starts this month, otherwise
                // it starts next month.
                if(cal.get(Calendar.DAY_OF_MONTH) < account.getBillingDayOfMonth()) {
                    account.setBillingMonthOfYear(cal.get(Calendar.MONTH));
                } else {
                    account.setBillingMonthOfYear((cal.get(Calendar.MONTH) + 1) % 12);
                }
            } else if (!yearly && account.getBillingMonthOfYear() != null) {
                account.setBillingMonthOfYear(null);
            }
            account.setBillingFailures(0);
            account.setAccountState(Account.ACTIVE);
            s.save(account);

            s.flush();
            conn.commit();
            session.removeAttribute("accountTypeChange");
        }
    }
    catch(Exception e) {
        LogClass.error(e);
        conn.rollback();

        throw new RuntimeException(e);
    }
    finally {
      Database.closeConnection(conn);
    }


%>
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
                    <li><a href="../html/flashAppAction.jsp">Back to Full Interface</a></li>
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
        <div class="span12">
            <div class="well" style="text-align:center">
                <h3>Billing Successful!</h3>
                <p><%= postBillingMessage %></p>
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