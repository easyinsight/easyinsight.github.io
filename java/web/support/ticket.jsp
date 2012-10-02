<%@ page import="com.easyinsight.datafeeds.zendesk.ZenDeskBugReportProvider" %>
<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload" %>
<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.fileupload.FileItem" %>
<%@ page import="com.easyinsight.logging.LogClass" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %><%

    ServletFileUpload upload = new ServletFileUpload(ZenDeskBugReportProvider.uploadFactory);
    List items = upload.parseRequest(request);
    Map<String, Object> params = new HashMap<String, Object>();
    ZenDeskBugReportProvider provider = new ZenDeskBugReportProvider();
    String uploadToken = "";

    for(Object o : items) {
        FileItem fileItem = (FileItem) o;
        if(fileItem.isFormField()) {
            params.put(fileItem.getFieldName(), fileItem.getString());
        } else if("attachment".equals(fileItem.getFieldName())){
            byte[] file = fileItem.get();
            String fileName = fileItem.getName();
            if(file.length > 0 && fileName.length() > 0)
                uploadToken = provider.uploadAttachment(file, fileName, uploadToken);
        }
    }

    String subject = (String) params.get("subject");
    subject = subject == null ? "" : subject;
    String description = (String) params.get("description");
    description = description == null ? "" : description;
    String email = (String) params.get("email");
    email = email == null ? "" : email;
//    String reportType = (String) params.get("reportType");
//    reportType = reportType == null ? "" : reportType;
//    String connectionName = (String) params.get("connectionName");
//    connectionName = connectionName == null ? "" : connectionName;
    String problemType = (String) params.get("problemType");
    problemType = problemType == null ? "" : problemType;
    String attachment = (String) params.get("attachment");
    attachment = attachment == null ? "" : attachment;

    if(subject.trim().isEmpty() || description.trim().isEmpty() || email.trim().isEmpty()) {
        request.getSession().setAttribute("errorString", "Please enter a subject, description, and a valid email.");
        response.sendRedirect("index.jsp");
    }

    provider.reportBug(subject, description, email, problemType, uploadToken);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!-- InstanceBegin template="/Templates/Base.dwt" codeOutsideHTMLIsLocked="false" -->
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
            list-style:none;
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
    <link href='https://fonts.googleapis.com/css?family=Cabin:regular' rel='stylesheet' type='text/css'>
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
            <div style="height:350px;padding-left:50px;padding-top:20px;padding-right:50px">
                <div style="font-size:20px;font-family:'Cabin',arial,serif;color:#333333;height:40px;align:center">
                    <div style="margin-top:0px;float:left;"><img src="/images/EI32.PNG" alt=""/></div>
                    <div style="margin-top:5px;margin-left:20px;float:left;">Your ticket has been created!</div>
                </div>
                <div style="font-size:18px;font-family:'Cabin',arial,serif;color:#333333">
                    A ticket has been created for your request.
                </div>
            </div>
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