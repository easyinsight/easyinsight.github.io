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
    <link href="/css/base.css" rel="stylesheet" type="text/css" />
    <link rel="icon" type="image/ico" href="/favicon.ico"/>
    <!-- InstanceBeginEditable name="head" -->
    <style type="text/css">
        #centerPage p {
            text-indent: 0px;
        }
        #centerPage {
            height:400px;
        }

        .loginButton {
            height: 25px;
            width: 61px;
            background-image: url('login.gif');
        }

        .loginButton:hover {
            background-image: url('loginSelected.gif');
        }

        .loginButton:active {
            background-image: url('loginActivated.gif');
        }

    </style>
    <!-- InstanceEndEditable -->
    <link href='http://fonts.googleapis.com/css?family=PT+Sans' rel='stylesheet' type='text/css'/>
    <link type="text/css" rel="stylesheet" media="screen" href="/css/base.css" />
</head>
<body style="width:100%;text-align:center;margin:0px auto;">
    <div style="width:1000px;border-left-style:solid;border-left-color:#DDDDDD;border-left-width:1px;border-right-style:solid;border-right-color:#DDDDDD;border-right-width:1px;margin:0 auto;">
    	<div id="topBar">
        <a href="/index.html"><img src="/images/logo.jpg" alt="Easy Insight Logo" name="logo" id="logo"/></a>
        <div class="signupHeadline">
            <a href="https://www.easy-insight.com/app/">Customer Login</a>
            <a href="/app/newaccount/">Pricing &amp; Signup</a>
            <a href="/contactus.html">Contacts</a>
        </div>
    </div>
    <div class="headline">
        <a class="inactive" href="/product.html">Features</a>
        <a class="inactive" href="/data.html">Connections</a>
        <a class="inactive" href="/webanalytics.html">Solutions</a>
        <a class="inactive" href="/customers.html">Customers</a>
        <a class="inactive" href="/partners.html">Partners</a>
        <a class="inactive" href="/company.html">Company</a>
    </div>
            <div style="width:100%;background-color:#FFFFFF">
          <p style="height:5em">Welcome to Easy Insight!  In order to update your billing information, you must be logged in first.  If you are seeing this page, you have most likely been redirected here because your account is delinquent, and must log in via this page.</p>

              <form method="post" action="billing.jsp" style="width:100%">
                  <table><tbody><tr>
                      <td>User Name:</td>
                      <td> <input type="text" name="username" /></td></tr>
                  <tr><td>Password:</td>
                  <td><input type="password" name="password" /></td></tr>
                  <tr><td colspan="2" style="text-align:center"><input class="loginButton" type="image" src="loginTransparent.gif" alt="Log In" /></td></tr>
                      </tbody></table>

          </form>
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