<!DOCTYPE html>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="java.util.*" %>
<%@ page import="com.easyinsight.security.SecurityUtil" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.easyinsight.users.AccountTypeChange" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="com.easyinsight.billing.BrainTreeBlueBillingSystem" %>
<%@ page import="com.braintreegateway.CustomerRequest" %>
<%@ page import="com.easyinsight.html.BillingResponse" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
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
            document.getElementById("ccexp").value = document.getElementById("ccexpMonth").value + "/" + document.getElementById("ccexpYear").value;
        }

        function changeBilling() {
            for(i = 0;i < document.forms[0].billingType.length;i++) {
                if(document.forms[0].billingType[i].checked)
                    window.location.search = "billingType=" + document.forms[0].billingType[i].value;
            }
        }

    </script>
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
        AccountTypeChange accountTypeChange;

        try {
            account = (Account) hibernateSession.createQuery("from Account where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list().get(0);
            System.out.println("Account " + account.getAccountID() + " starting billing process.");
            if (account.getPricingModel() == Account.NEW) {
                System.out.println("Account " + account.getAccountID() + " is on new pricing model, starting new model billing...");
                response.sendRedirect("newModelBilling.jsp");
                return;
            }
            accountTypeChange = (AccountTypeChange) session.getAttribute("accountTypeChange");
            if (accountTypeChange != null) {
                System.out.println("Account " + account.getAccountID() + " is changing the cost.");
                cost = Account.createTotalCost(account.getPricingModel(), accountTypeChange.getAccountType(), accountTypeChange.getDesigners(),
                        accountTypeChange.getStorage(), 0, accountTypeChange.isYearly());
                credit = Account.calculateCredit(account);
                System.out.println("Account " + account.getAccountID() + " new cost: " + cost);
                if (credit >= cost) {
                    session.removeAttribute("accountTypeChange");
                    // not supposed to get here...
                    cost = account.createTotalCost();
                    accountTypeChange = null;
                }
            } else {
                cost = account.createTotalCost();
                System.out.println("Account " + account.getAccountID() + " cost: " + cost);
            }
        } finally {
            hibernateSession.close();
        }

        boolean monthly = account.getBillingMonthOfYear() == null && (request.getParameter("billingType") == null || !request.getParameter("billingType").equals("yearly"));

        boolean chargeNow;

        if (accountTypeChange != null && (cost - credit) > 0) {

            // it's an upgrade
            System.out.println("Account " + account.getAccountID() + " is upgrading their account.");

            chargeNow = true;
        }  else if (account.getAccountState() == Account.DELINQUENT || account.getAccountState() == Account.BILLING_FAILED || account.getAccountState() == Account.CLOSED) {

            // it's restoring service to a locked account
            System.out.println("Account " + account.getAccountID() + " is restoring service to their locked account.");
            chargeNow = true;
        } else {

            // it's putting in new info or updating on a trial or active account
            System.out.println("Account " + account.getAccountID() + " is putting in new info or updating a trial or active account.");
            chargeNow = false;
        }

        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date d = new Date();
        String time = df.format(d);

        CustomerRequest trParams = new CustomerRequest();
        if(account.isBillingInformationGiven() != null && account.isBillingInformationGiven()) {
            trParams = trParams.customerId(String.valueOf(account.getAccountID()));
            System.out.println("Account " + account.getAccountID() + " billing information is given - customer id is set.");
        }
        else {
            trParams = trParams.id(String.valueOf(account.getAccountID()));
            System.out.println("Account " + account.getAccountID() + " billing information is NOT given - customer id is set to be created.");
        }
        trParams = trParams.creditCard().options().makeDefault(true).done().done();

        String trData = new BrainTreeBlueBillingSystem().getRedirect(trParams, account.getPricingModel());

        request.getSession().setAttribute("billingTime", time);
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
            billingMessage = "You are signing up for the " + accountInfoString + " account tier. You will be charged " + NumberFormat.getCurrencyInstance().format(cost) + " USD " + (monthly ? "monthly" : "yearly") + " for your subscription.";
            billingHeader = account.billingHeader();
            billingIntroParagraph = account.billingIntroParagraph();
        } else {
            billingMessage = "You are signing up for the " + accountInfoString + " account tier. You will be charged " + charge + " USD now, and " +
                    NumberFormat.getCurrencyInstance().format(Account.createTotalCost(account.getPricingModel(), accountTypeChange.getAccountType(), 0, 0, 0,
                            accountTypeChange.isYearly())) + (!accountTypeChange.isYearly() ? " monthly" : " yearly") + " for your subscription.";
            billingHeader = "";
            billingIntroParagraph = "";
        }


        if (chargeNow) {
             billingMessage += " Your credit card will be charged upon submitting this form.";
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
        <div class="col-md-6 col-md-offset-3" style="padding-top: 10px;padding-bottom: 10px">
            <h3 style="width:100%;text-align: center"><%= billingHeader %></h3>
            <p style="font-size:14px;font-family: 'PT Sans',arial,serif"><%= billingIntroParagraph %></p>
        </div>
        <div class="row">
            <div class="col-md-8 col-md-offset-1">
                <% if(request.getParameter("error") != null) { %>
                <p><label class="error"><%
                    String errorCode = request.getParameter("response_code");
                    try {
                        int code = Integer.parseInt(errorCode);
                        if (code == BillingResponse.DECLINED) {
                            %>The transaction was declined by the credit card processor. If this error continues, contact support@easy-insight.com.<%
                        } else if (code == BillingResponse.BILLING_ERROR) {
                            %>There was an error with your billing information. Please input the correct information below.<%
                        } else if (code == BillingResponse.TRANSACTION_NOT_ALLOWED) {
                            %>The transaction was not allowed by the credit card processor. Please contact support@easy-insight.com.<%
                        } else if (code == BillingResponse.NOT_FOUND_IN_VAULT) {
                            %>There was a server error with saving the information for your account. Please try again. If this error continues, contact support@easy-insight.com.<%
                        } else {
                            %>There was an error with your billing information. Please input the correct information below.<%
                        }
                    } catch (NumberFormatException nfe) {
                            %>There was an error with your billing information. Please input the correct information below.<%
                    }
                %></label></p>
                <% } %>
                <form method="post" action="<%= new BrainTreeBlueBillingSystem().getTargetUrl() %>" onsubmit="setCCexp()" class="well form-horizontal">
                    <fieldset>
                        <input id="ccexp" type="hidden" value="" name="customer[credit_card][expiration_date]"/>
                        <input id="customer_vault_id" type="hidden" value="<%= account.getAccountID() %>" name="customer_vault_id" />
                        <input id="tr_data" type="hidden" value="<%= trData %>" name="tr_data"/>

                        <div class="control-group">
                            <label class="control-label" for="firstname">First Name:</label>
                            <div class="controls">
                                <input id="firstname" type="text" value="" name="customer[first_name]" class="col-md-3"/>
                            </div>

                        </div>
                        <div class="control-group">
                            <label class="control-label" for="lastname">Last Name:</label>
                            <div class="controls">
                                <input id="lastname" type="text" value="" name="customer[last_name]" class="col-md-3"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label" for="zip">Billing Zip/Postal Code:</label>
                            <div class="controls">
                                <input id="zip" type="text" value="" name="customer[credit_card][billing_address][postal_code]" />
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label" for="ccnumber">Credit Card Number:</label>
                            <div class="controls">
                                <input id="ccnumber" type="text" style="width:16.5em" name="customer[credit_card][number]"/>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label" for="cvv">CVV/CVC:</label>
                            <div class="controls">
                                <input id="cvv" type="text" value="" name="customer[credit_card][cvv]" style="width:3.5em" />
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
            <div class="col-md-3">
                <div class="well" style="background-color: #d5d5d5">
                    <p><strong>Have questions?</strong></p>
                    <p>You can contact Easy Insight at 1-720-316-8174 or sales@easy-insight.com if you have any questions or concerns around your account billing.</p>
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
