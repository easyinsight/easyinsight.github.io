<%@ page import="java.util.Date" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="java.security.MessageDigest" %>
<%@ page import="com.easyinsight.users.Account" %>
<%@ page import="com.easyinsight.database.Database" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="java.security.NoSuchAlgorithmException" %>
<%@ page import="com.easyinsight.billing.BillingUtil" %>
<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: Jun 21, 2009
  Time: 11:36:48 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <%
      Account account = null;
      long accountID = (Long) request.getSession().getAttribute("accountID");
      Session s = Database.instance().createSession();
      try {
          account = (Account) s.createQuery("from Account where accountID = ?").setLong(0, accountID).list().get(0);
      }
      finally {
          s.close();
      }
      String keyID = BillingUtil.getKeyID();
      String key = BillingUtil.getKey();
      String orderID = "";
      String amount = "1.00";
      String type = "auth";
      if(account.getAccountState() == Account.DELINQUENT) {
          amount = String.valueOf(account.monthlyCharge());
          type = "sale";
      }

      DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
      df.setTimeZone(TimeZone.getTimeZone("UTC"));
      String time = df.format(new Date());
      request.getSession().setAttribute("billingTime", time);
      String hashString = orderID + "|" + amount + "|" + String.valueOf(accountID) + "|" + time + "|" + key;
      String hash = BillingUtil.MD5Hash(hashString);

  %>
<head>
<!-- InstanceBeginEditable name="doctitle" -->
        <title>Billing</title>
<!-- InstanceEndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link href="/website.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="/history/history.css" />
    <link rel="icon" type="image/ico" href="/favicon.ico"/>
    <script src="/AC_OETags.js" language="javascript"></script>
    <script src="/history/history.js" language="javascript"></script>
    <!-- InstanceBeginEditable name="head" -->
    <script language="javascript" type="text/javascript">
        function setCCexp() {
            document.getElementById("ccexp").value = document.getElementById("ccexpMonth").value + document.getElementById("ccexpYear").value;
        }
    </script>
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
        <% if(request.getParameter("error") != null) { %>
            <span style="color:red">There was an error with your billing information. Please input the correct information below.</span>
        <% } %>
  <form method="post" action="https://secure.braintreepaymentgateway.com/api/transact.php" onsubmit="setCCexp()">
      <input id="ccexp" type="hidden" value="" name="ccexp"/>
      <input id="customer_vault_id" type="hidden" value="<%= accountID %>" name="customer_vault_id" />
      <input id="customer_vault" type="hidden" value="<%= (account.isBillingInformationGiven() != null && account.isBillingInformationGiven()) ? "update_customer" : "add_customer" %>" name="customer_vault" />
      <input id="redirect" type="hidden" value="https://localhost/app/billing/submit.jsp" name="redirect"/>
      <input id="payment" type="hidden" value="creditcard" name="creditcard" />
      <input id="key_id" type="hidden" value="<%= keyID %>" name="key_id"/>
      <input id="orderid" type="hidden" value="<%= orderID %>" name="orderid"/>
      <input id="amount" type="hidden" value="<%= amount %>" name="amount"/>
      <input id="time" type="hidden" value="<%= time %>" name="time"/>
      <input id="hash" type="hidden" value="<%= hash %>" name="hash"/>
      <input id="type" type="hidden" value="<%= type %>" name="type" />
      <p>Company: <input id="company" type="text" value="" name="company" /></p>
      <p>First Name: <input id="firstname" type="text" value="" name="firstname" /> Last Name: <input id="lastname" type="text" value="" name="lastname" /></p>
      <p>Address 1: <input id="address1" type="text" value="" name="address1" /></p>
      <p>Address 2: <input id="address2" type="text" value="" name="address2" /></p>
      <p>City: <input id="city" type="text" value="" name="city" /> State: <input id="state" type="text" value="" name="state" /> Zip Code: <input id="zip" type="text" value="" name="zip" /></p>
      <p>Phone #: <input id="phone" type="text" value="" name="phone" /></p>

      <p>Credit Card Number: <input id="ccnumber" type="text" value="4111111111111111" name="ccnumber"/> CVV/CVC: <input id="cvv" type="text" value="" name="cvv" /> </p>
      <p>
      Expiration date: <select id="ccexpMonth">
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
          <option value="09">09</option>
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
      </p>

      <p><input type="submit" value="Submit" name="commit"/></p>
  </form>
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