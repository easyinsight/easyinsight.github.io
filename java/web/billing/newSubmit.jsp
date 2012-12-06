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
<%@ page import="org.joda.time.Days" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="com.easyinsight.billing.BrainTreeBlueBillingSystem" %>
<%@ page import="java.util.UUID" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.easyinsight.billing.BillingSystem" %>
<%@ page import="com.braintreegateway.exceptions.NotFoundException" %>
<%@ page import="com.braintreegateway.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String postBillingMessage = "";

    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
    try {
        User user = null;
        org.hibernate.Transaction t = null;
        EIConnection conn = Database.instance().getConnection();
        Session hibernateSession = Database.instance().createSession(conn);
        try {


            if (!SecurityUtil.isAccountAdmin()) {
                response.sendRedirect("access.jsp");
                return;
            }
            if ((SecurityUtil.getAccountTier() == Account.PREMIUM || SecurityUtil.getAccountTier() == Account.ENTERPRISE)) {
                response.sendRedirect("access.jsp");
                return;
            }


            double cost = 0;
            double credit = 0;
            boolean success = false;
            Account account = null;
            AccountTypeChange accountTypeChange = (AccountTypeChange) session.getAttribute("accountTypeChange");


            // pull back account, figure out if the data is successfully input and the user created on the vault.
            t = hibernateSession.beginTransaction();

            account = (Account) hibernateSession.createQuery("from Account where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list().get(0);
            Customer btCustomer = new BrainTreeBlueBillingSystem().getCustomer(account);
            if (btCustomer != null) {
                for (CreditCard cc : btCustomer.getCreditCards()) {
                    new BrainTreeBlueBillingSystem().deleteCard(cc);
                }
                for(Address aa : btCustomer.getAddresses()) {
                    new BrainTreeBlueBillingSystem().deleteAddress(aa);
                }
            }

            user = (User) hibernateSession.get(User.class, SecurityUtil.getUserID());

            Result<Customer> result = null;
            try {
                result = new BrainTreeBlueBillingSystem().confirmCustomer(request.getQueryString());
                if (result.isSuccess()) {
                    account.setBillingInformationGiven(true);
                    hibernateSession.save(account);
                } else {
                    t.rollback();
                    response.sendRedirect("newIndex.jsp?error=true&responseCode=" + result.getErrors().getAllDeepValidationErrors().get(0).getCode().code);
                    return;
                }
            } catch (NotFoundException e) {
                // customer has billingInformationGiven = true, but not found in vault somehow.
                account.setBillingInformationGiven(false);
                hibernateSession.save(account);
                t.commit();

                response.sendRedirect("newIndex.jsp?error=true&responseCode=try_again");
                return;
            }


            if (accountTypeChange != null) {
                cost = Account.createTotalCost(account.getPricingModel(), accountTypeChange.getAccountType(), accountTypeChange.getDesigners(),
                        accountTypeChange.getStorage(), accountTypeChange.isYearly());
                credit = Account.calculateCredit(account);
                if (credit >= cost) {
                    session.removeAttribute("accountTypeChange");
                    // not supposed to get here...
                    cost = account.createTotalCost();
                    accountTypeChange = null;
                }
            } else {
                cost = account.createTotalCost();
            }


            t.commit();
            String amount = "1.00";
            String type = "auth";

            boolean chargeNow = false;

            if (accountTypeChange != null && (cost - credit) > 0) {
                type = "sale";
                double toCharge = cost - credit;
                amount = String.valueOf(toCharge);
                chargeNow = true;
            } else if (account.getAccountState() == Account.DELINQUENT || account.getAccountState() == Account.BILLING_FAILED || account.getAccountState() == Account.CLOSED) {
                amount = String.valueOf(cost);
                type = "sale";
                chargeNow = true;
            }

            AccountCreditCardBillingInfo info = null;
            if (chargeNow) {
                t = hibernateSession.beginTransaction();
                BillingSystem bs = new BrainTreeBlueBillingSystem();
                info = bs.billAccount(account.getAccountID(), Double.valueOf(amount), "auth".equals(type));
                if (info.isSuccessful()) {
                    boolean yearly;
                    if (accountTypeChange == null) {
                        yearly = account.getBillingMonthOfYear() != null;
                    } else {
                        yearly = accountTypeChange.isYearly();
                    }
                    int daysBetween;
                    if (account.getBillingDayOfMonth() != null && account.getAccountState() == Account.ACTIVE) {
                        Calendar cal = Calendar.getInstance();
                        Integer billingMonthOfYear = account.getBillingMonthOfYear();
                        if (accountTypeChange.isYearly() && billingMonthOfYear == null) {
                            billingMonthOfYear = cal.get(Calendar.MONTH);
                        } else if (!accountTypeChange.isYearly() && billingMonthOfYear != null) {
                            billingMonthOfYear = null;
                        }
                        account.setBillingMonthOfYear(billingMonthOfYear);

                        cal.set(Calendar.DAY_OF_MONTH, account.getBillingDayOfMonth());
                        if (billingMonthOfYear != null) {
                            cal.set(Calendar.MONTH, billingMonthOfYear);
                        }

                        if (cal.getTime().before(new Date())) {
                            cal.add(Calendar.MONTH, 1);
                        }

                        Date date = cal.getTime();
                        DateTime lastTime = new DateTime(date);
                        DateTime now = new DateTime(System.currentTimeMillis());
                        daysBetween = Days.daysBetween(lastTime, now).getDays();
                    } else {
                        daysBetween = yearly ? 365 : 28;
                    }

                    info.setDays(daysBetween);
                    success = true;
                } else {
                    success = false;

                }
                hibernateSession.save(info);
                t.commit();
            } else {
                success = true;
            }


            // Can only let account admins use the billing
            if ((account.getAccountType() == Account.PROFESSIONAL || account.getAccountType() == Account.PREMIUM || account.getAccountType() == Account.ENTERPRISE)
                    && !user.isAccountAdmin())
                response.sendRedirect("access.jsp");

            t = hibernateSession.beginTransaction();
            if (success) {
                if (!chargeNow) {
                    boolean yearly;
                    if (accountTypeChange == null) {
                        yearly = account.getBillingMonthOfYear() != null;
                    } else {
                        yearly = accountTypeChange.isYearly();
                    }
                    Calendar c = Calendar.getInstance();
                    c.setTime(result.getTarget().getCreatedAt().getTime());
                    // Set up billing day of month
                    if (account.getAccountState() == Account.DELINQUENT || account.getAccountState() == Account.BILLING_FAILED) {
                        account.setBillingDayOfMonth(c.get(Calendar.DAY_OF_MONTH));
                        if (yearly)
                            account.setBillingMonthOfYear(c.get(Calendar.MONTH));
                        else
                            account.setBillingMonthOfYear(null);
                    } else if (account.getAccountState() == Account.TRIAL) {
                        Date trialEnd = new AccountActivityStorage().getTrialTime(account.getAccountID(), conn);
                        // billing day of month is at the end of the trial, if there is one
                        if (trialEnd != null) {
                            System.out.println("Trial end date: " + trialEnd.toString());
                            if (trialEnd.after(new Date()) && account.getBillingDayOfMonth() == null) {
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(trialEnd);
                                System.out.println("Billing day of month: " + cal.get(Calendar.DAY_OF_MONTH));
                                account.setBillingDayOfMonth(cal.get(Calendar.DAY_OF_MONTH));
                                if (yearly)
                                    account.setBillingMonthOfYear(cal.get(Calendar.MONTH));
                                else
                                    account.setBillingMonthOfYear(null);
                            } else {
                                Calendar cal = Calendar.getInstance();
                                account.setBillingDayOfMonth(cal.get(Calendar.DAY_OF_MONTH));
                                if (yearly)
                                    account.setBillingMonthOfYear(cal.get(Calendar.MONTH));
                                else
                                    account.setBillingMonthOfYear(null);
                            }
                        }
                    }

                    // If we've gotten this far, they're updating their billing information instead of submitting it. Allow upgrades to yearly, but if they're already yearly don't update
                    // billing_month_of_year
                    if (yearly && account.getBillingMonthOfYear() == null) {
                        Calendar cal = Calendar.getInstance();
                        if (account.getBillingDayOfMonth() == null) {
                            account.setBillingDayOfMonth(cal.get(Calendar.DAY_OF_MONTH));
                        }
                        //  if the billing day of month is before the billing day of month, yearly billing starts this month, otherwise
                        // it starts next month.
                        if (cal.get(Calendar.DAY_OF_MONTH) < account.getBillingDayOfMonth()) {
                            account.setBillingMonthOfYear(cal.get(Calendar.MONTH));
                        } else {
                            account.setBillingMonthOfYear((cal.get(Calendar.MONTH) + 1) % 12);
                        }
                    } else if (!yearly && account.getBillingMonthOfYear() != null) {
                        account.setBillingMonthOfYear(null);
                    }
                }
                account.setBillingFailures(0);
                account.setAccountState(Account.ACTIVE);
                hibernateSession.save(account);


                postBillingMessage = account.successMessage();

                t.commit();
                session.removeAttribute("accountTypeChange");
            }
        } catch (Exception e) {
            if (t != null)
                t.rollback();
            throw new RuntimeException(e);
        } finally {
            hibernateSession.close();
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
    //    String userName = (String) session.getAttribute("userName");
//    com.easyinsight.security.SecurityUtil.populateThreadLocalFromSession(request);
//    try {

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
                    <i class="icon-user"></i> <%= StringEscapeUtils.escapeHtml(user.getUserName()) %>
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

                <p><%= postBillingMessage %>
                </p>
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