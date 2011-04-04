<%@ page import="com.easyinsight.users.UserService" %>
<%@ page import="com.easyinsight.users.UserTransferObject" %>
<%@ page import="com.easyinsight.users.AccountTransferObject" %>
<%@ page import="com.easyinsight.users.Account" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" style="width:100%">
<!-- InstanceBegin template="/Templates/Base.dwt" codeOutsideHTMLIsLocked="false" -->
<%
    boolean accountCreated = false;
    String errorString = null;
    String tier = request.getParameter("tier");
    if (tier == null) {
        response.sendRedirect("index.jsp");
    } else {
        try {
            int tierNumber = Integer.parseInt(tier);
            if (tierNumber < com.easyinsight.users.Account.BASIC || tierNumber > com.easyinsight.users.Account.PROFESSIONAL) {
                response.sendRedirect("index.jsp");
                return;
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("index.jsp");
            return;
        }
    }
    String firstName = request.getParameter("firstName");
    String lastName = request.getParameter("lastName");
    String email = request.getParameter("email");
    String company = request.getParameter("company");
    String userName = request.getParameter("userName");
    String password = request.getParameter("password");
    String confirmPassword = request.getParameter("confirmPassword");
    String wasSubmit = request.getParameter("wasSubmit");
    if (wasSubmit != null) {
        if (firstName == null || "".equals(firstName)) {
            errorString = "Please specify a first name.";
        } else if (lastName == null || "".equals(lastName)) {
            errorString = "Please specify a last name.";
        } else if (email == null || "".equals(email)) {
            errorString = "Please specify an email address";
        } else if (company == null || "".equals(company)) {
            errorString = "Please specify a company name.";
        } else if (userName == null || "".equals(userName)) {
            errorString = "Please specify a user name.";
        } else if (userName.length() < 3) {
            errorString = "Your user name must be at least three characters.";
        } else if (userName.length() > 20) {
            errorString = "Your user name must be less than twenty characters.";
        } else if (password == null || "".equals(password)) {
            errorString = "Please specify a password.";
        } else if (confirmPassword == null || "".equals(confirmPassword)) {
            errorString = "Please confirm your password.";
        } else if (!password.equals(confirmPassword)) {
            errorString = "Your passwords do not match.";
        } else if (password.length() < 8) {
            errorString = "Your password must be at least eight characters.";
        } else if (password.length() > 20) {
            errorString = "Your password must be less than twenty characters.";
        } else {
            com.easyinsight.users.UserTransferObject user = new com.easyinsight.users.UserTransferObject();
            user.setUserName(userName);
            user.setFirstName(firstName);
            user.setName(lastName);
            user.setEmail(email);
            user.setAccountAdmin(true);
            user.setOptInEmail(true);
            com.easyinsight.users.AccountTransferObject account = new com.easyinsight.users.AccountTransferObject();
            account.setName(company);
            account.setAccountType(Integer.parseInt(request.getParameter("tier")));
            String exists = new com.easyinsight.users.UserService().doesUserExist(user.getUserName(), user.getEmail(), account.getName());
            if (exists == null) {
                String url = "https://www.easy-insight.com/app";
                new com.easyinsight.users.UserService().createAccount(user, account, request.getParameter("password"), url);
                accountCreated = true;
            } else {
                errorString = exists;
            }
        }
    }
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- InstanceBeginEditable name="doctitle" -->
    <title>Easy Insight Signup</title>
    <style type="text/css">
        h2 {
            color: #000000;
            font-size: 16px;
            font-weight: bold;
        }

        h3 {
            color: #CC0033;
            font-size: 14px;
            font-weight: bold;
        }

        p {
            text-align: left;
            padding: 10px 30px;
        }

        ul {
            height: 220px;
            list-style:none;
            text-align: left;
        }
        ul li {
            padding: 3px 0px;
        }
    </style>
    <!-- InstanceEndEditable -->
    <!-- InstanceBeginEditable name="head" -->
    <!-- InstanceEndEditable -->
    <link type="text/css" rel="stylesheet" media="screen" href="../../css/base.css"/>
    <link href='https://fonts.googleapis.com/css?family=PT+Sans' rel='stylesheet' type='text/css'/>
    <script type="text/javascript" src="/js/jquery.min.js"></script>
    <script type="text/javascript" src="/js/jqvideobox.min.js"></script>
    <script type="text/javascript" src="/js/swfobject.js"></script>
    <script type="text/javascript" src="/js/jquery.lightbox-0.5.min.js"></script>
    <script type="text/javascript" src="/js/startup.js"></script>
    <link rel="stylesheet" href="/css/jquery.lightbox-0.5.css" type="text/css" media="screen"/>
    <link rel="stylesheet" href="/css/jqvideobox.css" type="text/css" media="screen"/>
</head>
<body style="width:100%;text-align:center;margin:0px auto;">
<div style="position:absolute;height:38px;background-color:#848080;width:100%;margin-top:70px"></div>
<div style="width:1000px;margin:0 auto;">
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
    <div id="contentHeaderStart"></div>
        <div id="midContent">
            <% if (!accountCreated) { %>
            <form method="post" id="profileForm" action="accountsetup.jsp">
                <input type="hidden" id="tier" name="tier" value="<%=request.getParameter("tier")%>"/>
                <input type="hidden" id="wasSubmit" name="wasSubmit" value="1"/>
                <input type="hidden" id="connectionID" name="connectionID" value="<%=request.getParameter("connectionID")%>"/>

                <div id="sideBar">

                    <img style="padding-top:20px" src="/images/chart_column.png"/>
                    <div class="sideBarHeader">Easy to Use</div>
                    <div style="font-size:14px">Business User Friendly</div>
                    <div style="font-size:14px">Drag and Drop Reporting</div>

                    <img style="padding-top:40px" src="/images/lock.png"/>
                    <div class="sideBarHeader">Secure and Reliable</div>
                    <div style="font-size:14px">All SSL, All the Time</div>
                    <div style="font-size:14px">Daily Backups</div>

                    <img style="padding-top:40px" src="/images/user_headset.png"/>
                    <div class="sideBarHeader">Dedicated Customer Service</div>
                    <div style="font-size:14px">We're determined to give you the best possible user experience</div>
                </div>
            <div style="height:450px;padding-left:50px;padding-top:20px">
                <div style="color:red; font-size:16px;height:25px">
                <% if(errorString != null) {
                    out.println(errorString);
                                }
                %>
                </div>
                <div style="font-size:18px;font-family:Arial,serif;color:#333333;height:40px">
                    <div style="margin-top:0px;float:left;"><img src="/images/EI32.PNG" alt=""/></div>
                    <div style="margin-top:5px;margin-left:20px;float:left;">First, your account information:</div>
                </div>
                <table style="padding-top:5px">
                    <tr>
                        <td style="font-size:14px;color:#333333;width:120px;text-align:right">First Name:</td>
                        <td><input style="font-size:14px;width:300px" id="firstName" type="text" value="<%=request.getParameter("firstName") == null ? "" : request.getParameter("firstName")%>" name="firstName"/></td>
                    </tr>
                    <tr>
                        <td style="font-size:14px;color:#333333;width:120px;text-align:right">Last Name:</td>
                        <td><input style="font-size:14px;width:300px" id="lastName" type="text" value="<%=request.getParameter("lastName") == null ? "" : request.getParameter("lastName")%>" name="lastName"/></td>
                    </tr>
                    <tr>
                        <td style="font-size:14px;color:#333333;width:120px;text-align:right">Email:</td>
                        <td><input style="font-size:14px;width:300px" id="email" type="text" value="<%=request.getParameter("email") == null ? "" : request.getParameter("email")%>" name="email"/></td>
                    </tr>
                    <tr>
                        <td style="font-size:14px;color:#333333;width:120px;text-align:right">Company:</td>
                        <td><input style="font-size:14px;width:300px" id="company" type="text" value="<%=request.getParameter("company") == null ? "" : request.getParameter("company")%>" name="company"/></td>
                    </tr>
                </table>
                <div style="font-size:18px;padding-top:10px;font-family:Arial,serif;color:#333333;height:40px">
                    <div style="margin-top:0px;float:left;"><img src="/images/user3.png" alt=""/></div>
                    <div style="margin-top:5px;margin-left:20px;float:left;">Now choose a username and password:</div>
                </div>
                <table style="padding-top:5px">
                    <tr>
                        <td style="font-size:14px;color:#333333;width:120px;text-align:right">Username:</td>
                        <td><input style="font-size:14px;width:300px" id="userName" type="text" value="<%=request.getParameter("userName") == null ? "" : request.getParameter("userName")%>" name="userName"/></td>
                    </tr>
                    <tr>
                        <td style="font-size:14px;color:#333333;width:120px;text-align:right">Password:</td>
                        <td><input style="font-size:14px;width:300px" id="password" type="password" value="<%=request.getParameter("password") == null ? "" : request.getParameter("password")%>" value="" name="password"/></td>
                    </tr>
                    <tr>
                        <td style="font-size:14px;color:#333333;width:120px;text-align:right">Confirm Password:</td>
                        <td><input style="font-size:14px;width:300px" id="confirmPassword" type="password" value="<%=request.getParameter("confirmPassword") == null ? "" : request.getParameter("confirmPassword")%>" name="confirmPassword"/></td>
                    </tr>
                </table>
                <div style="fontSize:16px;font-family:Arial,serif;color:#333333;padding-top:15px">By clicking Get Started Now! you agree to the <a href="/terms.html" style="text-decoration:underline;color:#CC0033">Terms of Service</a> and <a href="/privacy.html" style="text-decoration:underline;color:#CC0033">Privacy</a> policies.</div>
                <div style="padding-top:20px">
                    <input type="image" src="/images/GetStartedSmaller2.png" alt="Submit Form"/>
                </div>
            </div>
                </form>
            <% } else { %>
            <div style="height:350px;padding-left:50px;padding-top:20px;padding-right:50px">
                <div style="font-size:20px;font-family:Arial,serif;color:#333333;height:40px;align:center">
                    <div style="margin-top:0px;float:left;"><img src="/images/EI32.PNG" alt=""/></div>
                    <div style="margin-top:5px;margin-left:20px;float:left;">Your account has been created!</div>
                </div>
                <div style="font-size:18px;font-family:Verdana,serif;color:#333333">
                    A welcome email has been sent to you with some basic information about your new Easy Insight account. Click on the link in the email to activate your new account and get started! 
                </div>
            </div>
            <% } %>
        </div>
    <div id="contentHeaderEnd"></div>
    		<!-- InstanceEndEditable -->
<div id="footer" style="margin:0px;width:100%;height:60px">
                <div id="linkLine"
                        style="margin:0px;padding:12px 0px;width:100%;text-align:left">
                    <div style="float:left;padding-left:10px;">
                        <a href="/documentation/toc.html">Documentation</a>
                    </div>
                    <div style="float:left;padding-left:40px;">
                        |
                    </div>
                    <div style="float:left;padding-left:40px;">
                        <a href="/app/newaccount/">Pricing</a>
                    </div>
                    <div style="float:left;padding-left:40px;">
                        |
                    </div>
                    <div style="float:left;padding-left:40px;">
                        <a href="/developers.html">Developers</a>
                    </div>
                    <div style="float:left;padding-left:35px;">
                        |
                    </div>
                    <div style="float:left;padding-left:40px;">
                        <a href="/privacy.html">Privacy Policy</a>
                    </div>
                    <div style="float:left;padding-left:40px;">
                        |
                    </div>
                    <div style="float:left;padding-left:40px;">
                        <a href="/terms.html">Terms of Service</a>
                    </div>
                    <div style="float:left;padding-left:40px;">
                        |
                    </div>
                    <div style="float:left;padding-left:40px;">
                        <a href="/partners.html">Partners</a>
                    </div>
                    <div style="float:left;padding-left:40px;">
                        |
                    </div>
                    <div style="float:left;padding-left:40px;">
                        <a href="/contactus.html">Contact Us</a>
                    </div>
                </div>
                <div style="width:100%;text-align:left;float:left;padding-left:10px;padding-top:8px">
                    Copyright ©2008-2010 Easy Insight, LLC. All rights reserved.
                </div>
            </div>
		</div>
<script type="text/javascript">
    var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>

<script type="text/javascript">
    var pageTracker = _gat._getTracker("UA-8316271-1");
    pageTracker._trackPageview();
</script>
</body>
<!-- InstanceEnd --></html>
