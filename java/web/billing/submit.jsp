<%@ page import="com.easyinsight.billing.BillingUtil" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.logging.LogClass" %>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.users.AccountActivityStorage" %>
<%@ page import="com.easyinsight.users.AccountCreditCardBillingInfo" %>
<%@ page import="com.easyinsight.users.User" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.TimeZone" %>
<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: Jun 25, 2009
  Time: 8:00:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String hashStr = request.getParameter("orderid") + "|" + request.getParameter("amount") + "|" + request.getParameter("response") + "|" + request.getParameter("transactionid") + "|" + request.getParameter("avsresponse") + "|" + request.getParameter("cvvresponse") + "|" + request.getParameter("customer_vault_id") + "|" + request.getParameter("time") + "|" + BillingUtil.getKey();
    String hashed = BillingUtil.MD5Hash(hashStr);

    EIConnection conn = Database.instance().getConnection();
    conn.setAutoCommit(false);
    Session s = Database.instance().createSession(conn);
    try {
        long accountID = (Long) request.getSession().getAttribute("accountID");
        long userID = (Long) request.getSession().getAttribute("userID");
        System.out.println("UserID: " + userID + " AccountID: " + accountID);
        Account account = null;
        User user = null;

        user = (User) s.createQuery("from User where userID = ?").setLong(0, userID).list().get(0);
        account = (Account) s.createQuery("from Account where accountID = ?").setLong(0, accountID).list().get(0);

        System.out.println("Creating account billing info...");
        AccountCreditCardBillingInfo info = new AccountCreditCardBillingInfo();
        info.setTransactionID(request.getParameter("transactionid"));
        info.setAmount(request.getParameter("amount"));
        info.setResponse(request.getParameter("response"));
        info.setResponseCode(request.getParameter("response_code"));
        info.setResponseString(request.getParameter("responsetext"));
        info.setAccountId(account.getAccountID());
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
            response.sendRedirect("billing.jsp?error=true&responseCode=" + request.getParameter("response_code"));
        }
        else if (request.getParameter("response").equals("1")) {
            conn.setSavepoint();
            account.setBillingInformationGiven(true);

            boolean yearly = request.getParameter("orderid").startsWith("yearly");

            List l = s.createQuery("from BuyOurStuffTodo where userID = ?").setLong(0, userID).list();
            if(l.size() > 0) {
                s.delete(l.get(0));
            }

            // Can only let account admins use the billing
            if((account.getAccountType() == Account.PROFESSIONAL || account.getAccountType() == Account.PREMIUM || account.getAccountType() == Account.ENTERPRISE)
              && !user.isAccountAdmin())
                response.sendRedirect("access.jsp");

            // Set up billing day of month
            if(account.getAccountState() == Account.DELINQUENT) {
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
            }
            account.setAccountState(Account.ACTIVE);
            s.save(account);

            s.flush();
            conn.commit();
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
<html>
<head>
<!-- InstanceBeginEditable name="doctitle" -->
            <title>Easy Insight - Billing Complete</title>
<!-- InstanceEndEditable -->
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link type="text/css" rel="stylesheet" media="screen" href="/css/base.css" />
</head>
<body style="width:100%;text-align:center;margin:0px auto;">
    <div style="width:1000px;border-left-style:solid;border-left-color:#DDDDDD;border-left-width:1px;border-right-style:solid;border-right-color:#DDDDDD;border-right-width:1px;margin:0 auto;">
    	<div style="width:100%;text-align:left;height:70px;position:relative">
        	<a href="/index.html"><img src="/images/logo.jpg" alt="Easy Insight Logo" name="logo" id="logo" /></a>
            <div class="signupHeadline"><a href="https://www.easy-insight.com/app/" class="signupButton"></a> <a href="https://www.easy-insight.com/app/#page=account" class="signupforfreeButton"></a></div>
            <div class="headline"><a id="productPage" href="/product.html">PRODUCT</a> <a id="dataPage" href="/data.html">DATA</a> <a id="solutionsPage" href="/webanalytics.html">SOLUTIONS</a> <a id="blogPage" href="http://jamesboe.blogspot.com/">BLOG</a>  <a id="companyPage" href="/company.html">COMPANY</a></div>
        </div>
        <div id="content">
        <!-- InstanceBeginEditable name="content" -->
            <div style="width:100%;background-color:#FFFFFF">
        <p> You have successfully set up your billing account. You will not be billed until your free trial has expired. </p>
        <p><a href="/app/#page=account">Go Back</a></p>
                </div>
        <!-- InstanceEndEditable -->
    <div id="footer" style="margin:0px;padding:12px 0px;width:100%;text-align:left">
        <div style="float:right;padding-right:200px;">
            <span style="font-weight:bold;font-size:12px">Security and Privacy</span>
            <ul>
                <li><a href="/terms.html">Terms of Service</a></li>
                <li><a href="/privacy.html">Privacy Policy</a></li>
            </ul>
        </div>
        <div style="padding-left:180px;">
            <span style="font-weight:bold;font-size:12px;">About</span>
            <ul>
                <li><a href="/company.html">Company Overview</a></li>
                <li><a href="/whoweare.html">Who We Are</a></li>
                <li><a href="/contactus.html">Contact Us</a></li>
            </ul>
        </div>
    </div>
            </div>
</div>
    </body>
</html>
