<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!-- InstanceBegin template="/Templates/Base.dwt" codeOutsideHTMLIsLocked="false" -->
<%
    String errorString = (String) request.getSession().getAttribute("errorString");
    if ("".equals(errorString) || "null".equals(errorString)) {
        errorString = null;
    }
    if (errorString != null) {
        request.getSession().removeAttribute("errorString");
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
            list-style: none;
            text-align: left;
        }

        ul li {
            padding: 3px 0;
        }
    </style>
    <!-- InstanceEndEditable -->
    <!-- InstanceBeginEditable name="head" -->
    <!-- InstanceEndEditable -->
    <link type="text/css" rel="stylesheet" media="screen" href="../../css/base.css"/>
    <link href='https://fonts.googleapis.com/css?family=PT+Sans' rel='stylesheet' type='text/css'/>
    <link href='https://fonts.googleapis.com/css?family=Cabin:regular,bold' rel='stylesheet' type='text/css'>
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
            <a href="https://www.easy-insight.com/app/htmlreport">Mobile Login</a>
            <a href="/app/newaccount/">Sign Up</a>
            <a href="/contactus.html">Contact Us</a>
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
        <form method="post" id="profileForm" action="accountCreation.jsp">
            <input type="hidden" id="tier" name="tier" value="<%=request.getParameter("tier")%>"/>
            <input type="hidden" id="wasSubmit" name="wasSubmit" value="1"/>
            <input type="hidden" id="connectionID" name="connectionID"
                   value="<%=request.getParameter("connectionID")%>"/>

            <div id="sideBar">

                <img style="padding-top:20px" src="/images/chart_column.png" alt="Report Creation"/>

                <div class="sideBarHeader">Easy to Use</div>
                <div style="font-size:14px">Business User Friendly</div>
                <div style="font-size:14px">Drag and Drop Reporting</div>

                <img style="padding-top:40px" src="/images/lock.png" alt="Security"/>

                <div class="sideBarHeader">Secure and Reliable</div>
                <div style="font-size:14px">All SSL, All the Time</div>
                <div style="font-size:14px">Daily Backups</div>

                <img style="padding-top:40px" src="/images/user_headset.png" alt="Customer Service"/>

                <div class="sideBarHeader">Dedicated Customer Service</div>
                <div style="font-size:14px">We're determined to give you the best possible user experience</div>
            </div>
            <div style="height:450px;padding-left:50px">
                <p style="font-size:32px;font-weight:bold">Start Using Easy Insight Now!</p>
                <p style="font-size:24px;padding-top:20px;font-family:'Cabin',arial,serif">Fully functional 30 day free trial, no credit card required</p>
                <div style="color:red; font-size:16px;height:25px;padding-top:10px">
                    <% if (errorString != null) { %>
                        <%= errorString %>
                    <% } %>
                </div>
                <table style="padding-top:5px;border-spacing:10px">
                    <tr>
                        <td style="font-size:14px;color:#333333;width:105px;text-align:left;font-weight:bold;font-family:'Cabin',arial,serif">First Name</td>
                        <td><input style="font-size:14px;width:500px" id="firstName" type="text"
                                   value="<%=request.getSession().getAttribute("firstName") == null ? "" : request.getSession().getAttribute("firstName")%>"
                                   name="firstName"/></td>
                    </tr>
                    <tr>
                        <td style="font-size:14px;color:#333333;width:105px;text-align:left;font-weight:bold;font-family:'Cabin',arial,serif">Last Name</td>
                        <td><input style="font-size:14px;width:500px" id="lastName" type="text"
                                   value="<%=request.getSession().getAttribute("lastName") == null ? "" : request.getSession().getAttribute("lastName")%>"
                                   name="lastName"/></td>
                    </tr>
                    <tr>
                        <td style="font-size:14px;color:#333333;width:105px;text-align:left;font-weight:bold;font-family:'Cabin',arial,serif">Company</td>
                        <td><input style="font-size:14px;width:500px" id="company" type="text"
                                   value="<%=request.getSession().getAttribute("company") == null ? "" : request.getSession().getAttribute("company")%>"
                                   name="company"/></td>
                    </tr>
                    <tr>
                        <td style="font-size:14px;color:#333333;width:105px;text-align:left;font-weight:bold;font-family:'Cabin',arial,serif">Email</td>
                        <td><input style="font-size:14px;width:500px" id="email" type="text"
                                   value="<%=request.getSession().getAttribute("email") == null ? "" : request.getSession().getAttribute("email")%>"
                                   name="email"/></td>
                    </tr>
                    <tr>
                        <td style="font-size:14px;color:#333333;width:105px;text-align:left;font-weight:bold">Password</td>
                        <td><input style="font-size:14px;width:500px" id="password" type="password"
                                   value="" name="password"/></td>
                    </tr>
                </table>
                <div style="padding-top:20px">
                    <input type="image" src="/images/GetStartedSmaller2.png" alt="Create Account"/>
                </div>
                <div style="fontSize:12px;font-family:'Cabin',arial,serif;color:#333333;padding-top:30px">By clicking Get
                    Started Now you agree to the <a href="/terms.html" style="text-decoration:underline;color:#CC0033">Terms
                        of Service</a> and <a href="/privacy.html" style="text-decoration:underline;color:#CC0033">Privacy</a>
                    policies.
                </div>
            </div>
        </form>
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
                <a href="/pricing.html">Pricing</a>
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