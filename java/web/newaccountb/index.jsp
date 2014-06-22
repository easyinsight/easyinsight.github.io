<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
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
    <title>Easy Insight &mdash; SaaS Business Intelligence, Reporting, and Analytics</title>
    <meta name="description"
          content="Easy Insight provides a self service, business user friendly solution for quick, easy, and affordable business intelligence over the cloud."/>
    <meta name="keywords"
          content="business intelligence, bi, saas bi, saas business intelligence, saas reporting, saas analytics, saas analysis, google analytics, salesforce, basecamp, highrise, ec2, mysql, sql server, saas, software as a service"/>

    <!-- font -->
    <link href='https://fonts.googleapis.com/css?family=Cabin:regular,bold' rel='stylesheet' type='text/css'>

    <!-- main includes -->

    <script type="text/javascript" src="/js/modernizr.js"></script>
    <script type="text/javascript" src="/js/jquery-1.10.2.js"></script>
    <script type="text/javascript" src="/js/jquery-ui.min.js"></script>
    <link href="/css/bootstrap.css" rel="stylesheet">
    <link href="/css/jquery-ui.css" rel="stylesheet">
    <link href="/css/bootstrap-website.css" rel="stylesheet"/>
    <script type="text/javascript" src="/js/bootstrap.js"></script>

    <!-- lightbox -->

    <script type="text/javascript" src="/js/jqvideobox.min.js"></script>
    <script type="text/javascript" src="/js/swfobject.js"></script>
    <script type="text/javascript" src="/js/jquery.lightbox-0.5.min.js"></script>
    <script type="text/javascript" src="/js/startup.js"></script>
    <link rel="stylesheet" href="/css/jquery.lightbox-0.5.css" type="text/css" media="screen"/>
    <link rel="stylesheet" href="/css/jqvideobox.css" type="text/css" media="screen"/>
</head>
<body>
<nav class="navbar navbar-static-top navbar-main website-navbar" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand eiBrand" href="/index.html"><img src="/images/logo.jpg" alt="Easy Insight Logo"
                                                                    name="logo"/></a>
        </div>

        <div class="collapse navbar-collapse navbar-ex1-collapse">
            <ul class="nav navbar-nav navbar-right navbar-main-item">
                <li><a href="/app/" class="fontA">Customer Login</a></li>
                <li><a href="/app/newaccount/" class="fontA">Free Trial</a></li>
                <li><a href="/app/news/" class="fontA">What's New</a></li>
            </ul>
        </div>
    </div>
</nav>
<nav class="navbar navbar-subheader website-navbar" role="navigation">
    <div class="container-fluid">
        <div class="collapse navbar-collapse navbar-ex1-collapse">
            <ul class="nav navbar-nav navbar-subheader-item">
                <li><a href="/product.html">Features</a></li>
                <li><a href="/data.html">Connections</a></li>
                <li><a href="/pricing.html">Pricing</a></li>
                <li><a href="/documentation.html">Documentation</a></li>
                <li><a href="/contactus.html">Contact Us</a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="container-fluid screenshotDiv">
    <div class="row">
        <div class="col-sm-12">
            <div class="container">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="dragDropDone" style="padding-top:0">Beautiful dashboards in a few clicks!</div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="dragDropDone" style="padding-top:0;font-size:16px">Drag. Drop. Done. Business Intelligence Made Easy.</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="container corePageWell">
    <div class="row">
        <div class="col-md-12">
            <form method="post" id="profileForm" action="accountCreation.jsp">
                <input type="hidden" name='OWASP_CSRFTOKEN' value="<%= session.getAttribute("OWASP_CSRFTOKEN")%>" />
                <input type="hidden" id="tier" name="tier" value="<%=StringEscapeUtils.escapeHtml(request.getParameter("tier"))%>"/>
                <input type="hidden" id="wasSubmit" name="wasSubmit" value="1"/>
                <div class="row">
                    <div class="col-md-7" style="text-align: left">
                        <div class="row">
                            <div class="col-md-12">
                                <div style="color:red; font-size:16px;height:25px;padding-top:10px;padding-bottom:10px;;margin-bottom:40px">
                                    <% if (errorString != null) { %>
                                    <%= errorString %>
                                    <% } %>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <label for="firstName" class="promptLabel" style="font-size:14px;color:#333333;font-family:'Cabin',sans-serif">
                                First Name
                            </label>
                            <input type="text" class="form-control" name="firstName" id="firstName" style="width:100%;font-size:16px;height:32px" autocapitalize="off" autocorrect="off" autoFocus
                                   value="<%= request.getSession().getAttribute("firstName") == null ? "" : StringEscapeUtils.escapeHtml(request.getSession().getAttribute("firstName").toString())%>"/>
                        </div>
                        <div class="row"  style="margin-top:15px">
                            <label for="lastName" class="promptLabel" style="font-size:14px;color:#333333;font-family:'Cabin',sans-serif">
                                Last Name
                            </label>
                            <input type="text" class="form-control" name="lastName" id="lastName" style="width:100%;font-size:14px;height:32px" autocapitalize="off" autocorrect="off"
                                   value="<%= request.getSession().getAttribute("lastName") == null ? "" : StringEscapeUtils.escapeHtml(request.getSession().getAttribute("lastName").toString())%>"/>
                        </div>
                        <div class="row" style="margin-top:15px">
                            <label for="email" class="promptLabel">
                                Email
                            </label>
                            <input type="text" class="form-control" name="email" id="email" style="width:100%;font-size:14px;height:32px" autocapitalize="off" autocorrect="off"
                                   value="<%= request.getSession().getAttribute("email") == null ? "" : StringEscapeUtils.escapeHtml(request.getSession().getAttribute("email").toString())%>"/>
                        </div>
                        <div class="row" style="margin-top:15px">
                            <label for="password" class="promptLabel">
                                Password
                            </label>
                            <input type="password" class="form-control" name="password" id="password" style="width:100%;font-size:14px;height:32px"
                                   value=""/>
                        </div>
                        <div class="row" style="margin-top:30px">
                            <div class="col-md-12">
                                <input type="submit" class="btn btn-primary btn-large" style="font-size: 24px" value="Get Started Now"/>
                            </div>
                        </div>
                        <div class="row" style="margin-top:30px">
                            <div class="col-md-12">
                                <div style="fontSize:12px;font-family:'Helvetica Neue',Helvetica,Arial, sans-serif;color:#333333;">By
                                    clicking Get
                                    Started Now you agree to the <a href="/terms.html"
                                                                    style="text-decoration:underline;color:#CC0033">Terms
                                        of Service</a> and <a href="/privacy.html" style="text-decoration:underline;color:#CC0033">Privacy</a>
                                    policies.
                                </div>
                            </div>
                        </div>

                    </div>
                    <div class="col-md-4 col-md-offset-1">
                        <div class="row well">
                            <div class="col-md-12">
                                <p style="font-size:14px;font-family:'Cabin',arial,serif;text-align: center">What Our Customers are Saying</p>

                                <p style="font-size:12px;font-family:'Cabin',arial,serif;padding-top: 20px">
                                    I am neither a database analyst nor SaaS engineer, but I am able to create the reports I need
                                    using EI's simple drag and drop interface. Job well done!
                                </p>

                                <p style="font-size:13px;font-family:'Cabin',arial,serif">Michael Aaron, Attention</p>

                                <p style="font-size:12px;font-family:'Cabin',arial,serif;padding-top: 20px">
                                    Easy Insight has proven to be a perfect compliment to Highrise. I highly recommend this software.
                                </p>

                                <p style="font-size:13px;font-family:'Cabin',arial,serif">Susan Wildeboer, Talenthouse</p>

                                <p style="font-size:12px;font-family:'Cabin',arial,serif;padding-top: 20px">
                                    Easy Insight has proven to be a hugely powerful
                                    system for us. Coupled with the incredibly fast and attentive support, it has enabled ChannelCreator to
                                    automate 90% of its reporting function and focus on our strength, selling our product.
                                </p>

                                <p style="font-size:13px;font-family:'Cabin',arial,serif">Matt Ball, ChannelCreator Ltd.</p>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="container-fluid" id="newFooter">
    <div class="row">
        <div class="col-sm-2 col-sm-offset-2">
            <div class="row">
                <div class="col-sm-12">
                    <a href="/contactus.html">Contact Us</a>
                </div>
            </div>
        </div>
        <div class="col-sm-2">
            <div class="row">
                <div class="col-sm-12">
                    <a href="/pricing.html">Pricing</a>
                </div>
            </div>
        </div>
        <div class="col-sm-2">
            <div class="row">
                <div class="col-sm-12">
                    <a href="/partners.html">Partners</a>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-12">
                    <a href="/api/">Developers</a>
                </div>
            </div>
        </div>
        <div class="col-sm-2">
            <div class="row">
                <div class="col-sm-12">
                    <a href="/privacy.html">Privacy Policy</a>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-12">
                    <a href="/terms.html">Terms of Service</a>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12 copyright">
            Copyright ©2008-2014 Easy Insight, LLC. All rights reserved.
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
