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
<%@ page import="com.easyinsight.security.PasswordService" %>
<%@ page import="com.easyinsight.users.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Formatter" %>
<%@ page import="com.easyinsight.config.ConfigLoader" %>
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
      long accountID = 0;
      Account account = null;
      User user = null;
      Session s = Database.instance().createSession();
      try {
          if(request.getParameter("username") != null && request.getParameter("password") != null) {
              String encryptedPass = PasswordService.getInstance().encrypt(request.getParameter("password"));

              List results = s.createQuery("from User where userName = ? and password = ?").setString(0, request.getParameter("username")).setString(1, encryptedPass).list();
              if(results.size() != 1)
                  response.sendRedirect("login.jsp?error=true");
              user =(User) results.get(0);
              account = user.getAccount();
              accountID = account.getAccountID();
              request.getSession().setAttribute("accountID", accountID);
          }
          if(account == null) {
              if(request.getSession().getAttribute("accountID")== null)
                response.sendRedirect("login.jsp?error=true");
              accountID = (Long) request.getSession().getAttribute("accountID");
              long userID = (Long) request.getSession().getAttribute("userID");
              user = (User) s.createQuery("from User where userID = ?").setLong(0, userID).list().get(0);
              account = (Account) s.createQuery("from Account where accountID = ?").setLong(0, accountID).list().get(0);
          }
      }
      finally {
          s.close();
      }
      if((account.getAccountType() == Account.GROUP || account.getAccountType() == Account.PROFESSIONAL || account.getAccountType() == Account.ENTERPRISE)
              && !user.isAccountAdmin())
        response.sendRedirect("access.jsp");

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
      String accountInfoString = null;
      switch(account.getAccountType()) {
          case Account.INDIVIDUAL:
              accountInfoString = "Individual";
              break;
          case Account.GROUP:
              accountInfoString = "Group";
              break;
          default:
              accountInfoString = "";
      }
      Formatter f = new Formatter();
      String charge = f.format("%.2f", account.monthlyCharge()).toString();

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
    <style type="text/css">
        #centerPage p {
            text-indent:0px;
        }
        td:first-child {
            text-align:right;
        }
        span {
            color: red;
        }

        .submitButton {
            height:25px;
            width:66px;
            background-image: url('submit.gif');
        }

        .submitButton:hover{
            background-image: url('submitSelected.gif');
        }

        .submitButton:active{
            background-image: url('submitActive.gif');
        }

    </style>
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
                <!--<a href="index.html">COMMUNITY</a><div></div>--->
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
        <p>Please input your billing information below. Your first billing cycle will start upon completion of any remaining trial time. Easy Insight does not offer any type of refund after billing.</p>
        <p style="font-size:10px">Items marked with a <span>*</span> are required.</p>
        <% if(request.getParameter("error") != null) { %>
            <p><span>There was an error with your billing information. Please input the correct information below.</span></p>
        <% } %>
  <form method="post" action="https://secure.braintreepaymentgateway.com/api/transact.php" onsubmit="setCCexp()">
      <input id="ccexp" type="hidden" value="" name="ccexp"/>
      <input id="customer_vault_id" type="hidden" value="<%= accountID %>" name="customer_vault_id" />
      <input id="customer_vault" type="hidden" value="<%= (account.isBillingInformationGiven() != null && account.isBillingInformationGiven()) ? "update_customer" : "add_customer" %>" name="customer_vault" />
      <input id="redirect" type="hidden" value="<%= ConfigLoader.instance().getRedirectLocation() %>/app/billing/submit.jsp" name="redirect"/>
      <input id="payment" type="hidden" value="creditcard" name="creditcard" />
      <input id="key_id" type="hidden" value="<%= keyID %>" name="key_id"/>
      <input id="orderid" type="hidden" value="<%= orderID %>" name="orderid"/>
      <input id="amount" type="hidden" value="<%= amount %>" name="amount"/>
      <input id="time" type="hidden" value="<%= time %>" name="time"/>
      <input id="hash" type="hidden" value="<%= hash %>" name="hash"/>
      <input id="type" type="hidden" value="<%= type %>" name="type" />
      <table cellpadding="3"><tbody>
      <tr>
            <td>Company:</td><td><input id="company" type="text" value="" name="company" style="width:16.5em" /></td>
      </tr>
      <tr>
          <td>First Name:</td><td><input id="firstname" type="text" value="" name="firstname" style="width:16.5em" /><span>*</span></td>
      </tr>
      <tr>
          <td>Address 1:</td><td> <input id="address1" type="text" value="" name="address1" style="width:16.5em" /><span>*</span></td>
      </tr>
      <tr>
          <td>Address 2:</td><td><input id="address2" type="text" value="" name="address2" style="width:16.5em" /></td>
      </tr>
      <tr>
          <td>City:</td><td><input id="city" type="text" value="" name="city" style="width:16.5em" /><span>*</span></td><td style="text-align:right">State:</td><td><input id="state" type="text" value="" style="width:2.5em" maxlength="2" name="state" /><span>*</span></td><td style="text-align:right">Zip Code:</td><td> <input id="zip" type="text" value="" name="zip" /><span>*</span></td>
      </tr>
      <tr>
          <td>Phone #:</td> <td><input id="phone" type="text" value="" name="phone" style="width:16.5em" /><span>*</span></td>
      </tr>
      <tr>
          <td>Credit Card Number:</td><td><input id="ccnumber" type="text" style="width:16.5em" name="ccnumber"/><span>*</span></td><td>CVV/CVC:</td> <td><input id="cvv" type="text" value="" name="cvv" style="width:3.5em" /><span>*</span></td>
      </tr>
      <tr><td>Expiration date:</td><td><select id="ccexpMonth">
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
      <span>*</span></td></tr>
      <tr><td colspan="6" style="font-size:14px;text-align:left">You are signing up for the <%= accountInfoString %> account tier, and you will be charged $<%= charge %> USD monthly for your subscription.  <% if(account.getAccountState() == Account.DELINQUENT) { %> Since your account is currently delinquent, the first charge will be applied today. <% } %>  </td></tr>
      <tr><td colspan="6" style="text-align:center"> <input class="submitButton" type="image" value="" src="transparent.gif" name="commit"/></td></tr>
      </tbody></table>


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