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
<%@ page import="com.easyinsight.logging.LogClass" %>
<%@ page import="com.easyinsight.html.HtmlConstants" %>
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

                    for (CreditCard cc : c.getCreditCards()) {
                        if (!cc.isDefault())
                            new BrainTreeBlueBillingSystem().deleteCard(cc);
                    }

                    /*for (Address a : c.getAddresses()) {
                        if (!curCC.getBillingAddress().getId().equals(a.getId()))
                            new BrainTreeBlueBillingSystem().deleteAddress(a);
                    }*/
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
                    response.sendRedirect("enterpriseBilling.jsp?error=true&response_code=" + responseCode);
                    return;
                }
            } catch (NotFoundException e) {
                LogClass.error(e);
                // customer has billingInformationGiven = true, but not found in vault somehow.

                account.setBillingInformationGiven(false);
                hibernateSession.save(account);
                t.commit();

                response.sendRedirect("enterpriseBilling.jsp?error=true&response_code=try_again");
                return;
            }

            postBillingMessage = "Your account has been configured to use the specified credit card.";
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
    <jsp:include page="../html/bootstrapHeader.jsp" />
</head>
<%


%>
<body>
<jsp:include page="../header.jsp">
    <jsp:param name="userName" value="<%= user.getName() %>"/>
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