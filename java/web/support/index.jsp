<!DOCTYPE html>
<html>
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
    <title>Easy Insight Support</title>
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
        <a href="/index.html"><img src="/images/logo.jpg" alt="Easy Insight Logo" id="logo"/></a>

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
        <form method="post" id="profileForm" action="ticket.jsp" enctype='multipart/form-data'>

            <div style="height:450px;padding-left:50px">
                <p style="font-size:32px;font-weight:bold">Having a problem?</p>
                <p style="font-size:24px;padding-top:20px;font-family:'Cabin',arial,serif">Send a support request and we'll get right on it!</p>
                <div style="color:red; font-size:16px;height:25px;padding-top:10px">
                    <% if (errorString != null) { %>
                        <%= errorString %>
                    <% } %>
                </div>
                <table style="padding-top:5px;border-spacing:10px">
                    <tr>
                        <td style="font-size:14px;color:#333333;width:105px;text-align:left;font-weight:bold;font-family:'Cabin',arial,serif">Subject</td>
                        <td><input required="required" style="font-size:14px;width:500px" id="subject" type="text"
                                   value="<%=request.getSession().getAttribute("subject") == null ? "" : request.getSession().getAttribute("subject")%>"
                                   name="subject"/></td>
                    </tr>
                    <tr>
                        <td style="font-size:14px;color:#333333;width:105px;text-align:left;font-weight:bold;font-family:'Cabin',arial,serif">Email</td>
                        <td><input required="required" style="font-size:14px;width:500px" id="email" type="email"
                                   value="<%=request.getSession().getAttribute("email") == null ? "" : request.getSession().getAttribute("email")%>"
                                   name="email"/></td>
                    </tr>
                    <tr>
                        <td style="font-size:14px;color:#333333;width:105px;text-align:left;font-weight:bold;font-family:'Cabin',arial,serif">Description</td>
                        <td><textarea required="required" style="font-size:14px;width:500px" id="description"
                                   name="description" rows="5" cols="80"><%=request.getSession().getAttribute("description") == null ? "" : request.getSession().getAttribute("description")%></textarea></td>
                    </tr>
                    <tr>
                        <td style="font-size:14px;color:#333333;width:105px;text-align:left;font-weight:bold;font-family:'Cabin',arial,serif">Report Type</td>
                        <td><select style="font-size:14px;width:500px" id="reportType" name="reportType">
                                <option value="">N/A</option>
                                <option value="chart">Chart</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td style="font-size:14px;color:#333333;width:105px;text-align:left;font-weight:bold;font-family:'Cabin',arial,serif">Connection Name</td>
                        <td><select style="font-size:14px;width:500px" id="connectionName" name="connectionName">
                                <option value="">N/A</option>
                                <option value="amazon">Amazon</option>
                                <option value="basecamp">Basecamp</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td style="font-size:14px;color:#333333;width:105px;text-align:left;font-weight:bold;font-family:'Cabin',arial,serif">Attachment</td>
                        <td>
                            <input type="file" style="font-size:14px;width:500px" id="attachment" name="attachment" />
                        </td>
                    </tr>
                </table>
                <div style="padding-top:20px">
                    <input type="image" src="/images/GetStartedSmaller2.png" alt="Create Ticket"/>
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
            Copyright �2008-2010 Easy Insight, LLC. All rights reserved.
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