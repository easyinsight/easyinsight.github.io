<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.easyinsight.admin.NewsEntry" %>
<%@ page import="java.util.*" %>
<%@ page import="com.easyinsight.admin.AdminService" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
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
<div id="whiteDiv"></div>
<div id="topMenuDiv"></div>
<div id="trialDiv" style="margin-top: 108px"></div>
<div style="width:1000px;margin:0 auto;">
    <div id="topBar">
        <a href="/index.html"><img src="/images/logo.jpg" alt="Easy Insight Logo" name="logo" id="logo"/></a>

        <div class="signupHeadline">
            <a href="/app/">Customer Login</a>
            <a href="/app/newaccount/">Free Trial</a>
            <a href="/contactus.html">Contact Us</a>
        </div>
    </div>
    <div class="headline">
        <a class="inactive" href="/product.html">Features</a>
        <a class="inactive" href="/data.html">Connections</a>
        <a class="inactive" href="/pricing.html">Pricing</a>
        <a class="inactive" href="/screencasts.html">Screencasts and Docs</a>
        <a class="inactive" href="/api.html">Developers</a>
    </div>
    <div id="trialBar">
        <div style="font-size: 18px;padding-top:15px">More Info? Call 1 (720) 285-8652</div>
    </div>
    <div id="contentHeaderStart"></div>
    <div id="midContent">

                <%
                    List<NewsEntry> newsEntryList = new AdminService().getNews();
                    Set<String> tags = new HashSet<String>();
                    for (NewsEntry newsEntry : newsEntryList) {
                        String tagString = newsEntry.getTags();
                        if (tagString == null) {
                            continue;
                        }
                        String[] tagArray = tagString.split(",");
                        for (String tag : tagArray) {
                            String trimmedTag = tag.trim();
                            if (!"".equals(trimmedTag)) {
                                tags.add(trimmedTag);
                            }
                        }
                    }
                    List<String> tagList = new ArrayList<String>(tags);
                    Collections.sort(tagList);
                %>


                            <div style="text-align: left;float:left;width: 200px;padding-left: 10px">
                                <div><a href="/app/news" style="font-size:16px">All</a></div>
                                <%
                                    for (String tag : tagList) {
                                %>
                                <div><a href="/app/news/<%= tag %>" style="font-size:16px"><%= tag %></a></div>
                                <%
                                    }
                                %>
                            </div>
        <div style="margin-left:200px;padding-right: 10px;min-height: 300px">
                    <%
                        /*String pageNumberString = request.getParameter("page");
                        int pageNumber = 0;
                        if (pageNumberString != null) {
                            pageNumber = Integer.parseInt(pageNumberString);
                        }*/



                        String tag = request.getParameter("tag");

                        Collections.reverse(newsEntryList);
                        for (NewsEntry newsEntry : newsEntryList) {
                            if (tag != null) {
                                boolean valid = false;
                                String tagString = newsEntry.getTags();
                                if (tagString == null) {
                                    continue;
                                }
                                String[] tagArray = tagString.split(",");
                                for (String testTag : tagArray) {
                                    String trimmed = testTag.trim();
                                    if (trimmed.equals(tag)) {
                                        valid = true;
                                    }
                                }
                                if (!valid) {
                                    continue;
                                }
                            }
                    %>
                    <div style="padding-top:10px">

                                <div style="width:100%;text-align:center">
                                    <h3 style="font-size:14px"><%= newsEntry.getTitle() %> - <%= new SimpleDateFormat("yyyy-MM-dd").format(newsEntry.getDate()) %></h3>
                                </div>

                                <div style="width:100%;text-align:center">
                                    <p><%= newsEntry.getNews() %></p>
                                </div>

                    </div>
                    <%
                        }
                    %>
        </div>
    </div>
    <div id="contentHeaderEnd"></div>


    <!-- InstanceEndEditable -->
    <div id="footer">
        <div id="linkLine">
            <div style="padding-left:15px;">
                <a href="/company.html">Company</a>
            </div>
            <div>
                |
            </div>
            <div>
                <a href="/pricing.html">Pricing</a>
            </div>
            <div>
                |
            </div>
            <div>
                <a href="/api.html">Developers</a>
            </div>
            <div>
                |
            </div>
            <div>
                <a href="/privacy.html">Privacy Policy</a>
            </div>
            <div>
                |
            </div>
            <div>
                <a href="/terms.html">Terms of Service</a>
            </div>
            <div>
                |
            </div>
            <div>
                <a href="/partners.html">Partners</a>
            </div>
            <div>
                |
            </div>
            <div>
                <a href="/contactus.html">Contact Us</a>
            </div>
        </div>
        <div style="width:100%;text-align:left;float:left;padding-left:10px;padding-top:8px">
            Copyright �2008-2012 Easy Insight, LLC. All rights reserved.
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