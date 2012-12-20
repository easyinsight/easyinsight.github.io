<!DOCTYPE html>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.users.*" %>
<%@ page import="com.easyinsight.billing.BrainTreeBlueBillingSystem" %>
<%@ page import="com.braintreegateway.exceptions.NotFoundException" %>
<%@ page import="com.braintreegateway.*" %>
<%@ page import="com.easyinsight.html.BillingResponse" %>
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

            Account account;
            NewModelAccountTypeChange accountTypeChange = (NewModelAccountTypeChange) session.getAttribute("accountTypeChange");


            // pull back account, figure out if the data is successfully input and the user created on the vault.
            t = hibernateSession.beginTransaction();

            account = (Account) hibernateSession.createQuery("from Account where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list().get(0);

            // clear out any existing credit card or address information

            user = (User) hibernateSession.get(User.class, SecurityUtil.getUserID());

            Result<Customer> result;
            try {

                // verify that the information passed in the transparent redirect was valid

                result = new BrainTreeBlueBillingSystem().confirmCustomer(request.getQueryString());
                if (result.getErrors() != null) {
                    for (ValidationError e : result.getErrors().getAllDeepValidationErrors()) {
                        if (e.getCode().equals(ValidationErrorCode.CUSTOMER_ID_IS_IN_USE))
                            account.setBillingInformationGiven(true);
                        hibernateSession.update(account);
                        t.commit();
                        t = hibernateSession.beginTransaction();
                    }
                }
                if (result.isSuccess()) {

                    Customer c = result.getTarget();
                    Subscription currentSubscription = null;
                    CreditCard curCC = null;
                    for (CreditCard cc : c.getCreditCards()) {
                        if (cc.isDefault())
                            curCC = cc;
                        for (Subscription s : cc.getSubscriptions()) {
                            if (s.getStatus() != Subscription.Status.CANCELED && s.getStatus() != Subscription.Status.EXPIRED)
                                currentSubscription = s;
                        }
                    }

                        if (curCC != null && currentSubscription != null) {
                            new BrainTreeBlueBillingSystem().updateSubscriptionCard(curCC, currentSubscription);

                            if (account.getAccountState() != Account.TRIAL) {
                                if (accountTypeChange != null) {
                                    if (currentSubscription.getPlanId().equals("1")) {
                                        new BrainTreeBlueBillingSystem().updateMonthly(currentSubscription, accountTypeChange.getAddonDesigners(),
                                                accountTypeChange.getAddonStorage(), accountTypeChange.getAddonConnections());
                                    } else if (currentSubscription.getPlanId().equals("2")) {
                                        new BrainTreeBlueBillingSystem().updateYearly(currentSubscription, accountTypeChange.getAddonDesigners(),
                                                accountTypeChange.getAddonStorage(), accountTypeChange.getAddonConnections());
                                    }
                                }
                                account.setAccountState(Account.ACTIVE);
                            }
                        } else {
                            if (account.getAccountState() != Account.TRIAL) {
                                if (accountTypeChange != null && accountTypeChange.isYearly())
                                    new BrainTreeBlueBillingSystem().subscribeYearly(account, accountTypeChange.getAddonDesigners(),
                                            accountTypeChange.getAddonStorage(), accountTypeChange.getAddonConnections());
                                else
                                    new BrainTreeBlueBillingSystem().subscribeMonthly(account, accountTypeChange.getAddonDesigners(),
                                            accountTypeChange.getAddonStorage(), accountTypeChange.getAddonConnections());
                                account.setAccountState(Account.ACTIVE);
                            }
                        }
                    if (accountTypeChange != null) {
                        account.setBillingMonthOfYear(accountTypeChange.isYearly() ? 1 : 0);
                    }


                    for (CreditCard cc : c.getCreditCards()) {
                        if (!cc.isDefault())
                            new BrainTreeBlueBillingSystem().deleteCard(cc);
                    }

                    for (Address a : c.getAddresses()) {
                        if (!curCC.getBillingAddress().getId().equals(a.getId()))
                            new BrainTreeBlueBillingSystem().deleteAddress(a);
                    }

                    if (accountTypeChange != null) {
                        accountTypeChange.apply(account, hibernateSession);
                    }
                    account.setBillingInformationGiven(true);
                    account.setBillingFailures(0);
                    hibernateSession.save(account);
                    t.commit();
                } else {
                    t.rollback();
                    String errorCode = result.getErrors().getAllDeepValidationErrors().get(0).getCode().code;
                    int responseCode;
                    if ("200".equals(errorCode)) responseCode = BillingResponse.DECLINED;
                    else if ("204".equals(errorCode)) responseCode = BillingResponse.TRANSACTION_NOT_ALLOWED;
                    else if ("220".equals(errorCode)) responseCode = BillingResponse.BILLING_ERROR;
                    else if ("221".equals(errorCode)) responseCode = BillingResponse.TRANSACTION_NOT_ALLOWED;
                    else if ("222".equals(errorCode)) responseCode = BillingResponse.TRANSACTION_NOT_ALLOWED;
                    else if ("223".equals(errorCode)) responseCode = BillingResponse.TRANSACTION_NOT_ALLOWED;
                    else if ("224".equals(errorCode)) responseCode = BillingResponse.TRANSACTION_NOT_ALLOWED;
                    else if ("300".equals(errorCode)) {
                        responseCode = BillingResponse.BILLING_ERROR;
                    } else {
                        responseCode = BillingResponse.BILLING_ERROR;
                    }
                    response.sendRedirect("newModelBilling.jsp?error=true&response_code=" + responseCode);
                    return;
                }
            } catch (NotFoundException e) {

                // customer has billingInformationGiven = true, but not found in vault somehow.

                account.setBillingInformationGiven(false);
                hibernateSession.save(account);
                t.commit();

                response.sendRedirect("newModelBilling.jsp?error=true&response_code=try_again");
                return;
            }

            postBillingMessage = account.successMessage();
            session.removeAttribute("accountTypeChange");
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