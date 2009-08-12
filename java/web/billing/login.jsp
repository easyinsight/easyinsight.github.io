<%--
  Created by IntelliJ IDEA.
  User: abaldwin
  Date: Jul 3, 2009
  Time: 10:17:02 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <style type="text/css">
        #centerPage p {
            text-indent: 0px;
        }
        #centerPage {
            height:400px;
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
          <p style="height:5em">Welcome to Easy Insight!  In order to update your billing information, you must be logged in first.  If you are seeing this page, you have most likely been redirected here because your account is delinquent, and must log in via this page.</p>

              <form method="post" action="billing.jsp" style="width:100%">
                  <table><tbody><tr>
                      <td>User Name:</td>
                      <td> <input type="text" name="username" /></td></tr>
                  <tr><td>Password:</td>
                  <td><input type="password" name="password" /></td></tr>
                  <tr><td colspan="2" style="text-align:center"><input type="image" src="login.gif" alt="Log In" /></td></tr>
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