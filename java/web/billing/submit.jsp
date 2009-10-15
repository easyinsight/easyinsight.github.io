<%@ page import="org.hibernate.Session" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.database.EIConnection" %>
<%@ page import="com.easyinsight.billing.BrainTreeBillingSystem" %>
<%@ page import="com.easyinsight.billing.BillingUtil" %>
<%@ page import="com.easyinsight.users.AccountCreditCardBillingInfo" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.*" %>
<%@ page import="com.easyinsight.users.User" %>
<%@ page import="com.easyinsight.users.AccountActivityStorage" %>
<%@ page import="com.easyinsight.outboundnotifications.TodoBase" %>
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


    if(!hashed.equals(request.getParameter("hash")) || !request.getParameter("response").equals("1") || !request.getParameter("cvvresponse").equals("M") || !Arrays.asList("X", "Y", "D", "M", "W", "Z", "P", "L").contains(request.getParameter("avsresponse")))
        response.sendRedirect("/app/billing/billing.jsp?error=true");
    else
    {
        long accountID = (Long) request.getSession().getAttribute("accountID");
        long userID = (Long) request.getSession().getAttribute("userID");
        System.out.println("UserID: " + userID + " AccountID: " + accountID);
        Account account = null;
        User user = null;
        EIConnection conn = Database.instance().getConnection();
        conn.setAutoCommit(false);
        Session s = Database.instance().createSession(conn);
        try {
            user = (User) s.createQuery("from User where userID = ?").setLong(0, userID).list().get(0);
            account = (Account) s.createQuery("from Account where accountID = ?").setLong(0, accountID).list().get(0);
            account.setBillingInformationGiven(true);

            List l = s.createQuery("from BuyOurStuffTodo where userID = ?").setLong(0, userID).list();
            if(l.size() > 0) {
                s.delete(l.get(0));
            }

            if((account.getAccountType() == Account.GROUP || account.getAccountType() == Account.PROFESSIONAL || account.getAccountType() == Account.ENTERPRISE)
              && !user.isAccountAdmin())
                response.sendRedirect("access.jsp");

            if(account.getAccountState() == Account.DELINQUENT) {
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
                account.setBillingDayOfMonth(c.get(Calendar.DAY_OF_MONTH));
                info.setTransactionTime(transTime);
                account.getBillingInfo().add(info);
                s.save(info);
                System.out.println("Saved account billing info.");

            } else {
                Date trialEnd = new AccountActivityStorage().getTrialTime(account.getAccountID(), conn);
                if(trialEnd != null) {
                    System.out.println("Trial end date: " + trialEnd.toString());
                    if(trialEnd.after(new Date()) && account.getBillingDayOfMonth() == null) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(trialEnd);
                        System.out.println("Billing day of month: " + c.get(Calendar.DAY_OF_MONTH));
                        account.setBillingDayOfMonth(c.get(Calendar.DAY_OF_MONTH));
                    }
                    else {
                        Calendar c = Calendar.getInstance();
                        account.setBillingDayOfMonth(c.get(Calendar.DAY_OF_MONTH));
                    }
                }
            }
            account.setAccountState(Account.ACTIVE);
            s.save(account);

            s.flush();

            conn.commit();
        }
        catch(Exception e) {
            conn.rollback();
            throw new RuntimeException(e);
        }
        finally {
          Database.closeConnection(conn);
        }
    }

%>
<html>
<head>
<!-- InstanceBeginEditable name="doctitle" -->
            <title>Easy Insight - Billing Complete</title>
<!-- InstanceEndEditable -->
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link href="/website.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="/history/history.css" />
    <link rel="icon" type="image/ico" href="/favicon.ico"/>
    <script src="/AC_OETags.js" language="javascript"></script>
    <script src="/history/history.js" language="javascript"></script>
    <!-- InstanceBeginEditable name="head" -->
    <!-- InstanceEndEditable -->
</head>
<body>
<div id="allPage">
    <div id="header">
        <div id="navigationElements">
            <div id="topLinks" style="width:100%">
                <a href="/contactus.html">contact us</a><div></div>
                <a href="http://jamesboe.blogspot.com/">blog</a><div></div>
                <a href="/index.html">home</a>
            </div>
            <div id="mainLinks" style="width:100%">
                <a href="/company.html">COMPANY</a><div></div>
                <a href="/consulting.html">CONSULTING</a><div></div>
                <!--<a href="index.html">COMMUNITY</a><div></div>-->
                <a href="/solutions.html">SOLUTIONS</a><div></div>
                <a href="/product.html">PRODUCT</a><div></div>
                <a href="/index.html">HOME</a>
            </div>
        </div>
        <div id="logo">
            <img src="/logo2.PNG" alt="Easy Insight Logo"/>
        </div>
    </div>
    <img src="/redbar.PNG" alt="Red Bar"/>
    <div id="centerPage">
        <!-- InstanceBeginEditable name="content" -->
        <p> You have successfully set up your billing account. You will not be billed until your free trial has expired. </p>
        <p><a href="/app/#page=account">Go Back</a></p>

        <!-- InstanceEndEditable -->
    </div>

    <div id="footer">
        <div style="width:400px">
          &copy; 2009 Easy Insight LLC. All rights reserved.
        </div>
        <div>
          <a href="/index.html">Home</a>
        </div>
        <div>
          <a href="/sitemap.html">Site Map</a>
        </div>
        <div>
          <a href="/privacy.html">Privacy Policy</a>
        </div>
        <div>
          <a href="/tos.html">Terms of Service</a>
        </div>
    </div>

</div>

    </body>
</html>